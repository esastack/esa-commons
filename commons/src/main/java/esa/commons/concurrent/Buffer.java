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

import java.util.function.Consumer;

/**
 * An Simple version of {@link java.util.Queue}, which provides high performance {@link #offer(Object)}(lock less) and
 * {@link #drain(Consumer)}(wait free)
 */
public interface Buffer<E> {

    /**
     * Inserts the specified element into this buffer if it is possible to do so immediately without violating capacity
     * restrictions.
     *
     * @param e element
     *
     * @return {@code true} if the element was added to this buffer, else {@code false}
     */
    boolean offer(E e);

    /**
     * Called from a producer thread subject to the restrictions appropriate to the implementation. As opposed to {@link
     * java.util.Queue#offer(Object)} this method may return failed without the buffer being full.
     *
     * @param e element
     *
     * @return {@code 0} if element was inserted into the buffer, {@code -1} cas failed, {@code 1} buffer is full.
     */
    default int relaxedOffer(E e) {
        return offer(e) ? 0 : 1;
    }

    /**
     * Removes all available elements from this buffer and apply them to the given consumer.
     *
     * @param c consumer
     *
     * @return the number of elements transferred
     */
    int drain(Consumer<E> c);

}
