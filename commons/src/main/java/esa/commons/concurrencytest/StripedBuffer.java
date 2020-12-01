/*
 * Copyright 2020 OPPO ESA Stack Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package esa.commons.concurrencytest;

import esa.commons.Platforms;
import sun.misc.Unsafe;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

/**
 * Similar to 'java.util.concurrent.atomic.Striped64'.
 */
abstract class StripedBuffer<E> implements Buffer<E> {

    private static final Unsafe U;
    private static final int NCPU = Platforms.cpuNum();
    private static final int SPINS = NCPU << 1;
    private volatile Buffer<E>[] cells;
    private volatile int cellsBusy;

    @Override
    public boolean offer(E e) {
        Buffer<E>[] as;
        Buffer<E> a;
        int m;
        int r;
        boolean uncontended = true;
        if ((as = cells) == null
                || (m = as.length - 1) < 0
                || (a = as[getProbe() & m]) == null
                // cas failed
                || !(uncontended = ((r = a.relaxedOffer(e)) != -1))) {
            r = accumulate(e, uncontended);
        }
        return r == 0;
    }

    @SuppressWarnings("unchecked")
    final int accumulate(E e, boolean wasUncontended) {
        int h;
        if ((h = getProbe()) == 0) {
            // force initialization
            ThreadLocalRandom.current();
            h = getProbe();
            wasUncontended = true;
        }
        // True if last slot nonempty
        boolean collide = false;
        for (int i = 0; i < SPINS; i++) {
            Buffer<E>[] as;
            Buffer<E> a;
            int n;
            int r;
            if ((as = cells) != null && (n = as.length) > 0) {
                if ((a = as[(n - 1) & h]) == null) {
                    if (cellsBusy == 0 && casCellsBusy()) {
                        // Try to attach new Cell
                        boolean created = false;
                        try {
                            Buffer<E>[] rs;
                            int m, j;
                            if ((rs = cells) != null &&
                                    (m = rs.length) > 0 &&
                                    rs[j = (m - 1) & h] == null) {
                                rs[j] = newBuffer(e);
                                created = true;
                            }
                        } finally {
                            cellsBusy = 0;
                        }
                        if (created) {
                            return 0;
                        }
                        // Slot is now non-empty
                        continue;
                    }
                    collide = false;
                } else if (!wasUncontended) {
                    // CAS already known to fail
                    // Continue after rehash
                    wasUncontended = true;
                } else if ((r = a.relaxedOffer(e)) != -1) {
                    return r;
                } else if (n >= NCPU || cells != as) {
                    // At max size or stale
                    collide = false;
                } else if (!collide) {
                    collide = true;
                } else if (cellsBusy == 0 && casCellsBusy()) {
                    try {
                        if (cells == as) {
                            // Expand table unless stale
                            Buffer<E>[] rs = new Buffer[n << 1];
                            System.arraycopy(as, 0, rs, 0, n);
                            cells = rs;
                        }
                    } finally {
                        cellsBusy = 0;
                    }
                    collide = false;
                    continue;
                }
                h = advanceProbe(h);
            } else if (cellsBusy == 0 && cells == as && casCellsBusy()) {
                boolean init = false;
                try {
                    // Initialize table
                    if (cells == as) {
                        Buffer<E>[] rs = new Buffer[2];
                        rs[h & 1] = newBuffer(e);
                        cells = rs;
                        init = true;
                    }
                } finally {
                    cellsBusy = 0;
                }
                if (init) {
                    return 0;
                }
            }
        }
        return -1;
    }

    @Override
    public int drain(Consumer<E> c) {
        Buffer<E>[] queues = cells;
        if (queues == null) {
            return 0;
        }
        int cnt = 0;
        for (Buffer<E> queue : queues) {
            if (queue != null) {
                cnt += queue.drain(c);
            }
        }
        return cnt;
    }

    /**
     * CASes the cellsBusy field from 0 to 1 to acquire lock.
     */
    final boolean casCellsBusy() {
        return U.compareAndSwapInt(this, CELLS_BUSY, 0, 1);
    }

    protected abstract Buffer<E> newBuffer(E e);

    /**
     * Returns the probe value for the current thread. Duplicated from ThreadLocalRandom because of packaging
     * restrictions.
     */
    static int getProbe() {
        return U.getInt(Thread.currentThread(), PROBE);
    }

    /**
     * Pseudo-randomly advances and records the given probe value for the given thread. Duplicated from
     * ThreadLocalRandom because of packaging restrictions.
     */
    static int advanceProbe(int probe) {
        // xorshift
        probe ^= probe << 13;
        probe ^= probe >>> 17;
        probe ^= probe << 5;
        U.putInt(Thread.currentThread(), PROBE, probe);
        return probe;
    }

    private static final long CELLS_BUSY;
    private static final long PROBE;

    static {
        try {
            U = UnsafeUtils.getUnsafe();
            CELLS_BUSY = U.objectFieldOffset(StripedBuffer.class.getDeclaredField("cellsBusy"));
            PROBE = U.objectFieldOffset(Thread.class.getDeclaredField("threadLocalRandomProbe"));
        } catch (Exception e) {
            throw new Error(e);
        }
    }

}
