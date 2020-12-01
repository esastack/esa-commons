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

/**
 * Implementation of {@link StripedBuffer} that aims to be used in Multiple producer-Single consumer environment.
 */
public class MpscArrayBuffer<E> extends StripedBuffer<E> {

    private final int capacityPerQueue;

    public MpscArrayBuffer(int capacityPerQueue) {
        // avoid endless loop in newBuffer()s
        Checks.checkArg(capacityPerQueue > 1);
        this.capacityPerQueue = capacityPerQueue;
    }

    @Override
    protected Buffer<E> newBuffer(E e) {
        MpscArrayQueue<E> queue = new MpscArrayQueue<>(capacityPerQueue);
        while (!queue.offer(e)) {
        }
        return queue;
    }
}
