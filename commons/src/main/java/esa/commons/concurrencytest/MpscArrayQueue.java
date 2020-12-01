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


import esa.commons.Checks;

import java.util.Iterator;
import java.util.function.Consumer;

import static esa.commons.concurrencytest.UnsafeArrayUtils.getElementAcquire;
import static esa.commons.concurrencytest.UnsafeArrayUtils.lazySetElement;
import static esa.commons.concurrencytest.UnsafeArrayUtils.setElement;

/**
 * Implementation of {@link Buffer} that aims to be used in Multiple producer-Single consumer environment.
 */
public class MpscArrayQueue<E> extends LhsMpscArrayQueueConsumerIdxPad<E> implements Buffer<E> {

    public MpscArrayQueue(int capacity) {
        super(capacity);
    }

    @Override
    public boolean offer(E e) {
        Checks.checkNotNull(e);
        long producerLimit = getProducerLimitAcquire();
        final long mask = mask();
        long producerIdx;
        do {
            producerIdx = getProducerIdxAcquire();
            if (producerIdx >= producerLimit) {
                final long consumerIdx = getConsumerIdxAcquire();
                producerLimit = consumerIdx + mask + 1;
                if (producerIdx >= producerLimit) {
                    // full
                    return false;
                }
                lazySetProducerLimit(producerLimit);
            }
        } while (!casProducerIdx(producerIdx, producerIdx + 1));
        final long offset = calcElementOffset(producerIdx, mask);
        lazySetElement(this.elements, offset, e);
        return true;
    }

    @Override
    public int relaxedOffer(E e) {
        Checks.checkNotNull(e);
        final long mask = mask();
        final long producerIdx = getProducerIdxAcquire();
        long producerLimit = getProducerLimitAcquire();

        if (producerIdx >= producerLimit) {
            final long consumerIdx = getConsumerIdxAcquire();
            producerLimit = consumerIdx + mask + 1;
            if (producerIdx >= producerLimit) {
                // full
                return 1;
            }
            lazySetProducerLimit(producerLimit);
        }

        if (!casProducerIdx(producerIdx, producerIdx + 1)) {
            return -1;
        }

        final long offset = calcElementOffset(producerIdx, mask);
        lazySetElement(this.elements, offset, e);
        return 0;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public E poll() {
        final long consumerIdx = getConsumerIdx();
        final long offset = calcElementOffset(consumerIdx, mask());
        final E[] arr = this.elements;
        E e = getElementAcquire(arr, offset);
        if (e == null) {
            if (consumerIdx != getProducerIdxAcquire()) {
                do {
                    e = getElementAcquire(arr, offset);
                } while (e == null);
            } else {
                return null;
            }
        }
        setElement(arr, offset, null);
        lazySetConsumerIdx(consumerIdx + 1);
        return e;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public E peek() {
        final long consumerIdx = getConsumerIdx();
        final long offset = calcElementOffset(consumerIdx, mask());
        final E[] arr = this.elements;
        E e = getElementAcquire(arr, offset);
        if (e == null) {
            if (consumerIdx != getProducerIdxAcquire()) {
                do {
                    e = getElementAcquire(arr, offset);
                } while (e == null);
            } else {
                return null;
            }
        }
        return e;
    }

    @Override
    public int drain(Consumer<E> c) {
        return drain(c, capacity());
    }

    public int drain(Consumer<E> c, int limit) {
        final E[] buffer = this.elements;
        final long mask = mask();
        final long consumerIdx = getConsumerIdx();

        for (int i = 0; i < limit; i++) {
            final long index = consumerIdx + i;
            final long offset = calcElementOffset(index, mask);
            final E e = getElementAcquire(buffer, offset);
            if (e == null) {
                return i;
            }
            setElement(buffer, offset, null);
            lazySetConsumerIdx(index + 1);
            c.accept(e);
        }
        return limit;
    }

    @Override
    public boolean isEmpty() {
        return getConsumerIdxAcquire() == getProducerIdxAcquire();
    }

    @Override
    public int size() {
        long consumerIdx = getConsumerIdxAcquire();
        long size;
        while (true) {
            final long consumerIdx1 = consumerIdx;
            final long producerIndex = getProducerIdxAcquire();
            consumerIdx = getConsumerIdxAcquire();
            if (consumerIdx1 == consumerIdx) {
                size = (producerIndex - consumerIdx);
                break;
            }
        }
        if (size > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        } else {
            return (int) size;
        }
    }

    @Override
    public Iterator<E> iterator() {
        // TODO: implement it
        throw new UnsupportedOperationException();
    }
}

abstract class RhsMpscArrayQueuePad<E> extends UnsafePaddedArray<E> {

    long p1, p2, p3, p4, p5, p6, p7;

    RhsMpscArrayQueuePad(int capacity) {
        super(capacity);
    }
}

abstract class MpscArrayQueueProducerIdxValue<E> extends RhsMpscArrayQueuePad<E> {

    private static final long PRODUCER_OFFSET =
            UnsafeUtils.objectFieldOffset(MpscArrayQueueProducerIdxValue.class, "producerIdx");

    @SuppressWarnings("unused")
    private volatile long producerIdx;

    MpscArrayQueueProducerIdxValue(int capacity) {
        super(capacity);
    }

    final long getProducerIdxAcquire() {
        return producerIdx;
    }

    boolean casProducerIdx(long expect, long update) {
        return U.compareAndSwapLong(this, PRODUCER_OFFSET, expect, update);
    }
}

abstract class RhsMpscArrayQueueProducerIdxPad<E> extends MpscArrayQueueProducerIdxValue<E> {

    long p1, p2, p3, p4, p5, p6, p7;

    RhsMpscArrayQueueProducerIdxPad(int capacity) {
        super(capacity);
    }
}

abstract class MpscArrayQueueProducerLimitValue<E> extends RhsMpscArrayQueueProducerIdxPad<E> {

    private static final long PRODUCER_LIMIT_OFFSET =
            UnsafeUtils.objectFieldOffset(MpscArrayQueueProducerLimitValue.class, "producerLimit");

    private volatile long producerLimit;

    MpscArrayQueueProducerLimitValue(int capacity) {
        super(capacity);
        this.producerLimit = capacity;
    }

    final long getProducerLimitAcquire() {
        return producerLimit;
    }

    final void lazySetProducerLimit(long v) {
        U.putOrderedLong(this, PRODUCER_LIMIT_OFFSET, v);
    }

}

abstract class RhsMpscArrayQueueProducerLimitPad<E> extends MpscArrayQueueProducerLimitValue<E> {

    long p1, p2, p3, p4, p5, p6, p7;

    RhsMpscArrayQueueProducerLimitPad(int capacity) {
        super(capacity);
    }
}

abstract class MpscArrayQueueConsumerIdxValue<E> extends RhsMpscArrayQueueProducerLimitPad<E> {

    private static final long CONSUMER_OFFSET
            = UnsafeUtils.objectFieldOffset(MpscArrayQueueConsumerIdxValue.class, "consumerIdx");

    @SuppressWarnings("unused")
    private long consumerIdx;

    MpscArrayQueueConsumerIdxValue(int capacity) {
        super(capacity);
    }

    final long getConsumerIdx() {
        return consumerIdx;
    }

    final long getConsumerIdxAcquire() {
        return U.getLongVolatile(this, CONSUMER_OFFSET);
    }

    void lazySetConsumerIdx(long v) {
        U.putOrderedLong(this, CONSUMER_OFFSET, v);
    }
}

abstract class LhsMpscArrayQueueConsumerIdxPad<E> extends MpscArrayQueueConsumerIdxValue<E> {
    long p1, p2, p3, p4, p5, p6, p7, p8;

    LhsMpscArrayQueueConsumerIdxPad(int capacity) {
        super(capacity);
    }
}
