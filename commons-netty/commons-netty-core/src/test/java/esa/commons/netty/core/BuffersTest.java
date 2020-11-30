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
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BuffersTest {

    @Test
    void testEmptyBuffer() {
        assertSame(Unpooled.EMPTY_BUFFER, Buffers.EMPTY_BUFFER.getByteBuf());
    }

    @Test
    void testCreateBufferByUnpooled() {
        final Buffer buffer = Buffers.buffer();
        assertTrue(buffer.getByteBuf().hasArray());

        final Buffer buffer1 = Buffers.buffer(1);
        assertTrue(buffer1.getByteBuf().hasArray());
        assertEquals(1, buffer1.capacity());

        final Buffer buffer2 = Buffers.buffer(1, 2);
        assertTrue(buffer2.getByteBuf().hasArray());
        assertEquals(1, buffer2.capacity());
        assertEquals(2, buffer2.getByteBuf().maxCapacity());
    }

    @Test
    void testCreateBufferByWrappingBytes() {
        final byte[] bytes = new byte[4];

        final Buffer buffer = Buffers.buffer(bytes);
        assertTrue(buffer.getByteBuf().hasArray());
        assertSame(bytes, buffer.getByteBuf().array());
        assertEquals(bytes.length, buffer.readableBytes());

        final Buffer buffer1 = Buffers.buffer(bytes, 1, 2);
        assertTrue(buffer1.getByteBuf().hasArray());
        assertSame(bytes, buffer1.getByteBuf().array());
        assertEquals(2, buffer1.readableBytes());
    }

}
