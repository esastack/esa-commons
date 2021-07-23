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
package esa.commons.netty.core;

import io.netty.buffer.Unpooled;

/**
 * Unity class of {@link Buffer}.
 * @deprecated use https://github.com/esastack/esa-commons-net instead
 */
@Deprecated
public final class Buffers {

    /**
     * @see BufferImpl#EMPTY_BUFFER
     */
    public static Buffer EMPTY_BUFFER = BufferImpl.EMPTY_BUFFER;

    /**
     * Creates a new, empty buffer.
     *
     * @return buffer
     */
    public static Buffer buffer() {
        return new BufferImpl();
    }

    /**
     * Creates a new, empty buffer using specified initial size.
     *
     * @param initialCapacity initial size
     *
     * @return buffer
     */
    public static Buffer buffer(int initialCapacity) {
        return new BufferImpl(initialCapacity);
    }

    /**
     * Creates a new, empty buffer using specified initial size and max capacity.
     *
     * @param initialCapacity initial size
     *
     * @return buffer
     */
    public static Buffer buffer(int initialCapacity, int maxCapacity) {
        return new BufferImpl(initialCapacity, maxCapacity);
    }

    /**
     * Creates a new, buffer wrapping the given bytes.
     *
     * @param src src
     *
     * @return buffer
     */
    public static Buffer buffer(byte[] src) {
        return new BufferImpl(Unpooled.wrappedBuffer(src));
    }

    /**
     * Creates a new, buffer wrapping the given range of bytes.
     *
     * @param src src
     * @param off offset
     * @param len length
     *
     * @return buffer
     */
    public static Buffer buffer(byte[] src, int off, int len) {
        return new BufferImpl(Unpooled.wrappedBuffer(src, off, len));
    }

    private Buffers() {
    }

}
