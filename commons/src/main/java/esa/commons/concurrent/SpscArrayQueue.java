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
package esa.commons.concurrent;

import esa.commons.Checks;

import java.util.Iterator;
import java.util.function.Consumer;

/**
 * Implementation of {@link Buffer} that aims to be used in Single producer-Single consumer environment.
 */
public class SpscArrayQueue<E> extends LhsSpscArrayQueueConsumerIdxPad<E> {

    public SpscArrayQueue(int capacity) {
        super(capacity);
    }

    @Override
    public boolean offer(E e) {
        Checks.checkNotNull(e);
        final long mask = mask();
        final long producerIdx = getProducerIdx();
        final long offset = calcElementOffset(producerIdx, mask);
        final E[] arr = this.elements;

        if (UnsafeArrayUtils.getElementAcquire(arr, offset) == null) {
            UnsafeArrayUtils.lazySetElement(arr, offset, e);
            // visible for size()
            lazySetProducerIdx(producerIdx + 1);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public E poll() {
        final long consumerIndex = getConsumerIdx();
        final long offset = calcElementOffset(consumerIndex, mask());
        final E[] arr = this.elements;
        final E e = UnsafeArrayUtils.getElementAcquire(arr, offset);
        if (e == null) {
            return null;
        }
        UnsafeArrayUtils.lazySetElement(arr, offset, null);
        lazySetConsumerIdx(consumerIndex + 1);
        return e;
    }

    @Override
    public E peek() {
        return UnsafeArrayUtils.getElementAcquire(elements, calcElementOffset(getConsumerIdx(), mask()));
    }

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
            final E e = UnsafeArrayUtils.getElementAcquire(buffer, offset);
            if (e == null) {
                return i;
            }
            UnsafeArrayUtils.lazySetElement(buffer, offset, null);
            lazySetConsumerIdx(index + 1);
            c.accept(e);
        }
        return limit;
    }

    @Override
    public Iterator<E> iterator() {
        // TODO: implement it
        throw new UnsupportedOperationException();
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
}

abstract class RhsSpscArrayQueuePad<E> extends UnsafePaddedArray<E> {

    long p1, p2, p3, p4, p5, p6, p7;

    RhsSpscArrayQueuePad(int capacity) {
        super(capacity);
    }
}

abstract class SpscArrayQueueProducerIdxValue<E> extends RhsSpscArrayQueuePad<E> {

    private static final long PRODUCER_OFFSET =
            UnsafeUtils.objectFieldOffset(SpscArrayQueueProducerIdxValue.class, "producerIdx");

    @SuppressWarnings("unused")
    private long producerIdx;

    SpscArrayQueueProducerIdxValue(int capacity) {
        super(capacity);
    }

    final long getProducerIdx() {
        return producerIdx;
    }

    final long getProducerIdxAcquire() {
        return U.getLongVolatile(this, PRODUCER_OFFSET);
    }

    void lazySetProducerIdx(long value) {
        U.putOrderedLong(this, PRODUCER_OFFSET, value);
    }
}

abstract class RhsSpscArrayQueueProducerIdxPad<E> extends SpscArrayQueueProducerIdxValue<E> {

    long p1, p2, p3, p4, p5, p6, p7;

    RhsSpscArrayQueueProducerIdxPad(int capacity) {
        super(capacity);
    }
}

abstract class SpscArrayQueueConsumerIdxValue<E> extends RhsSpscArrayQueueProducerIdxPad<E> {

    private static final long CONSUMER_OFFSET =
            UnsafeUtils.objectFieldOffset(SpscArrayQueueConsumerIdxValue.class, "consumerIdx");

    @SuppressWarnings("unused")
    private long consumerIdx;

    SpscArrayQueueConsumerIdxValue(int capacity) {
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

abstract class LhsSpscArrayQueueConsumerIdxPad<E> extends SpscArrayQueueConsumerIdxValue<E> {
    long p1, p2, p3, p4, p5, p6, p7, p8;

    LhsSpscArrayQueueConsumerIdxPad(int capacity) {
        super(capacity);
    }
}





