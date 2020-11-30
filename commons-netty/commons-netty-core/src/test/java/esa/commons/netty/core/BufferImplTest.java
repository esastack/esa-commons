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

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.charThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BufferImplTest {

    @Test
    void testDelegate() {
        final ByteBuf mock = mock(ByteBuf.class);
        final BufferImpl buffer = new BufferImpl(mock);

        when(mock.getByte(0)).thenReturn((byte) 1);
        assertEquals((byte) 1, buffer.getByte(0));
        verify(mock).getByte(eq(0));

        when(mock.getBoolean(0)).thenReturn(true);
        assertTrue(buffer.getBoolean(0));
        verify(mock).getBoolean(eq(0));

        when(mock.getChar(0)).thenReturn((char) 1);
        assertEquals((char) 1, buffer.getChar(0));
        verify(mock).getChar(eq(0));

        when(mock.getShort(0)).thenReturn((short) 1);
        assertEquals((short) 1, buffer.getShort(0));
        verify(mock).getShort(eq(0));

        when(mock.getInt(0)).thenReturn(1);
        assertEquals(1, buffer.getInt(0));
        verify(mock).getInt(eq(0));

        when(mock.getMedium(0)).thenReturn(1);
        assertEquals(1, buffer.getMedium(0));
        verify(mock).getMedium(eq(0));

        when(mock.getLong(0)).thenReturn(1L);
        assertEquals(1L, buffer.getLong(0));
        verify(mock).getLong(eq(0));

        when(mock.getDouble(0)).thenReturn(1.0D);
        assertEquals(1.0D, buffer.getDouble(0));
        verify(mock).getDouble(eq(0));

        when(mock.getFloat(0)).thenReturn(1.0f);
        assertEquals(1.0f, buffer.getFloat(0));
        verify(mock).getFloat(eq(0));

        when(mock.readByte()).thenReturn((byte) 1);
        assertEquals((byte) 1, buffer.readByte());
        verify(mock).readByte();

        when(mock.readBoolean()).thenReturn(true);
        assertTrue(buffer.readBoolean());
        verify(mock).readBoolean();

        when(mock.readChar()).thenReturn((char) 1);
        assertEquals((char) 1, buffer.readChar());
        verify(mock).readChar();

        when(mock.readShort()).thenReturn((short) 1);
        assertEquals((short) 1, buffer.readShort());
        verify(mock).readShort();

        when(mock.readMedium()).thenReturn(1);
        assertEquals(1, buffer.readMedium());
        verify(mock).readMedium();

        when(mock.readInt()).thenReturn(1);
        assertEquals(1, buffer.readInt());
        verify(mock).readInt();

        when(mock.readLong()).thenReturn(1L);
        assertEquals(1L, buffer.readLong());
        verify(mock).readLong();

        when(mock.readDouble()).thenReturn(1.0D);
        assertEquals(1.0D, buffer.readDouble());
        verify(mock).readDouble();

        when(mock.readFloat()).thenReturn(1.0f);
        assertEquals(1.0f, buffer.readFloat());
        verify(mock).readFloat();

        assertSame(buffer, buffer.setByte(0, (byte) 1));
        verify(mock).setByte(eq(0), eq(1));

        assertSame(buffer, buffer.setBoolean(0, true));
        verify(mock).setBoolean(eq(0), eq(true));

        assertSame(buffer, buffer.setChar(0, (char) 1));
        verify(mock).setChar(eq(0), eq(1));

        assertSame(buffer, buffer.setShort(0, (short) 1));
        verify(mock).setShort(eq(0), eq(1));

        assertSame(buffer, buffer.setMedium(0, 1));
        verify(mock).setMedium(eq(0), eq(1));

        assertSame(buffer, buffer.setInt(0, 1));
        verify(mock).setInt(eq(0), eq(1));

        assertSame(buffer, buffer.setLong(0, 1L));
        verify(mock).setLong(eq(0), eq(1L));

        assertSame(buffer, buffer.setDouble(0, 1.0D));
        verify(mock).setDouble(eq(0), eq(1.0D));

        assertSame(buffer, buffer.setFloat(0, 1.0f));
        verify(mock).setFloat(eq(0), eq(1.0f));

        assertSame(buffer, buffer.writeByte((byte) 1));
        verify(mock).writeByte(eq(1));

        assertSame(buffer, buffer.writeBoolean(true));
        verify(mock).writeBoolean(eq(true));

        assertSame(buffer, buffer.writeShort((short) 1));
        verify(mock).writeShort(eq(1));

        assertSame(buffer, buffer.writeMedium(1));
        verify(mock).writeMedium(eq(1));

        assertSame(buffer, buffer.writeInt(1));
        verify(mock).writeInt(eq(1));

        assertSame(buffer, buffer.writeLong(1L));
        verify(mock).writeLong(eq(1L));

        assertSame(buffer, buffer.writeDouble(1.0D));
        verify(mock).writeDouble(eq(1.0D));

        assertSame(buffer, buffer.writeFloat(1.0f));
        verify(mock).writeFloat(eq(1.0f));

        final byte[] bytes = new byte[4];

        assertEquals(buffer, buffer.getBytes(0, bytes));
        verify(mock).getBytes(eq(0), same(bytes));

        assertEquals(buffer, buffer.getBytes(0, bytes, 1, 2));
        verify(mock).getBytes(eq(0), same(bytes), eq(1), eq(2));

        assertEquals(buffer, buffer.readBytes(bytes));
        verify(mock).readBytes(same(bytes));

        assertEquals(buffer, buffer.readBytes(bytes, 1, 2));
        verify(mock).readBytes(same(bytes), eq(1), eq(2));

        assertEquals(buffer, buffer.setBytes(0, bytes));
        verify(mock).setBytes(eq(0), same(bytes));

        assertEquals(buffer, buffer.setBytes(0, bytes, 1, 2));
        verify(mock).setBytes(eq(0), same(bytes), eq(1), eq(2));

        assertEquals(buffer, buffer.writeBytes(bytes));
        verify(mock).writeBytes(same(bytes));

        assertEquals(buffer, buffer.writeBytes(bytes, 1, 2));
        verify(mock).writeBytes(same(bytes), eq(1), eq(2));

        when(mock.capacity()).thenReturn(1);
        assertEquals(1, buffer.capacity());
        verify(mock).capacity();

        when(mock.writerIndex()).thenReturn(1);
        assertEquals(1, buffer.writerIndex());
        verify(mock).writerIndex();

        when(mock.readerIndex()).thenReturn(1);
        assertEquals(1, buffer.readerIndex());
        verify(mock).readerIndex();

        when(mock.isReadable()).thenReturn(true);
        assertTrue(buffer.isReadable());
        verify(mock).isReadable();

        when(mock.isReadable(1)).thenReturn(true);
        assertTrue(buffer.isReadable(1));
        verify(mock).isReadable(eq(1));

        when(mock.isWritable()).thenReturn(true);
        assertTrue(buffer.isWritable());
        verify(mock).isWritable();

        when(mock.isWritable(1)).thenReturn(true);
        assertTrue(buffer.isWritable(1));
        verify(mock).isWritable(eq(1));

        when(mock.readableBytes()).thenReturn(1);
        assertEquals(1, buffer.readableBytes());
        verify(mock).readableBytes();

        when(mock.writableBytes()).thenReturn(1);
        assertEquals(1, buffer.writableBytes());
        verify(mock).writableBytes();

        when(mock.maxWritableBytes()).thenReturn(1);
        assertEquals(1, buffer.maxWritableBytes());
        verify(mock).maxWritableBytes();

        assertSame(buffer, buffer.clear());
        verify(mock).clear();

        when(mock.copy()).thenReturn(Unpooled.EMPTY_BUFFER);
        assertSame(Unpooled.EMPTY_BUFFER, buffer.copy().getByteBuf());
        verify(mock).copy();

        when(mock.slice()).thenReturn(Unpooled.EMPTY_BUFFER);
        assertSame(Unpooled.EMPTY_BUFFER, buffer.slice().getByteBuf());
        verify(mock).slice();

        when(mock.slice(1, 2)).thenReturn(Unpooled.EMPTY_BUFFER);
        assertSame(Unpooled.EMPTY_BUFFER, buffer.slice(1, 2).getByteBuf());
        verify(mock).slice(eq(1), eq(2));

        assertSame(mock, buffer.getByteBuf());

        when(mock.toString(StandardCharsets.UTF_8)).thenReturn("foo");
        assertEquals("foo", buffer.string(StandardCharsets.UTF_8));

        when(mock.toString()).thenReturn("bar");
        assertEquals("bar", buffer.toString());

        assertEquals(mock.hashCode(), buffer.hashCode());
    }

    @Test
    void testEquals() {
        final Buffer buf = new BufferImpl(Unpooled.buffer(1).writeByte(1));
        assertEquals(buf, buf);
        assertEquals(new BufferImpl(Unpooled.EMPTY_BUFFER), new BufferImpl(Unpooled.EMPTY_BUFFER));

        assertNotEquals(buf, null);
        assertNotEquals(buf, "foo");
        assertNotEquals(buf, new BufferImpl(Unpooled.EMPTY_BUFFER));
    }

    @Test
    void testPrimitiveSetAndGet() {
        final Buffer buf = new BufferImpl(ByteBufAllocator.DEFAULT);
        buf.setByte(0, (byte) '0');
        assertEquals('0', buf.getByte(0));
        allZeroIndex(buf);

        int val = Byte.MAX_VALUE + 1;
        buf.setShort(0, (short) val);
        assertEquals(val, buf.getShort(0));
        allZeroIndex(buf);

        buf.setMedium(0, val);
        assertEquals(val, buf.getMedium(0));
        allZeroIndex(buf);

        val = ThreadLocalRandom.current().nextInt();
        buf.setInt(0, val);
        assertEquals(val, buf.getInt(0));
        allZeroIndex(buf);

        val = ThreadLocalRandom.current().nextInt();
        buf.setLong(0, val);
        assertEquals(val, buf.getLong(0));
        allZeroIndex(buf);

        final float f = ThreadLocalRandom.current().nextFloat();
        buf.setFloat(0, f);
        assertEquals(f, buf.getFloat(0));
        allZeroIndex(buf);

        final double d = ThreadLocalRandom.current().nextDouble();
        buf.setDouble(0, d);
        assertEquals(d, buf.getDouble(0));
        allZeroIndex(buf);

        final boolean b = ThreadLocalRandom.current().nextBoolean();
        buf.setBoolean(0, b);
        assertEquals(b, buf.getBoolean(0));
        allZeroIndex(buf);

        final char c = '\0';
        buf.setChar(0, c);
        assertEquals(c, buf.getChar(0));
        allZeroIndex(buf);
    }

    @Test
    void testPrimitiveWriteAndRead() {
        final Buffer buf = new BufferImpl(ByteBufAllocator.DEFAULT);
        buf.writeByte((byte) '0');
        assertEquals('0', buf.readByte());
        allNotZeroIndex(buf);

        int val = Byte.MAX_VALUE + 1;
        buf.writeShort((short) val);
        assertEquals(val, buf.readShort());
        allNotZeroIndex(buf);

        buf.writeMedium(val);
        assertEquals(val, buf.readMedium());
        allNotZeroIndex(buf);

        val = ThreadLocalRandom.current().nextInt();
        buf.writeInt(val);
        assertEquals(val, buf.readInt());
        allNotZeroIndex(buf);

        long l = ThreadLocalRandom.current().nextLong();
        buf.writeLong(l);
        assertEquals(l, buf.readLong());
        allNotZeroIndex(buf);

        final float f = ThreadLocalRandom.current().nextFloat();
        buf.writeFloat(f);
        assertEquals(f, buf.readFloat());
        allNotZeroIndex(buf);

        final double d = ThreadLocalRandom.current().nextDouble();
        buf.writeDouble(d);
        assertEquals(d, buf.readDouble());
        allNotZeroIndex(buf);

        final boolean b = ThreadLocalRandom.current().nextBoolean();
        buf.writeBoolean(b);
        assertEquals(b, buf.readBoolean());
        allNotZeroIndex(buf);

        final char c = '\0';
        buf.writeChar(c);
        assertEquals(c, buf.readChar());
        allNotZeroIndex(buf);
    }

    @Test
    void testSetAndGetBytes() {
        final byte[] source = randomByteArray(100);

        Buffer b = from(randomByteArray(100));
        b.setBytes(0, source);

        byte[] dest = new byte[100];
        b.getBytes(0, dest);
        assertArrayEquals(source, dest);

        b.setBytes(0, source, 90, 10);
        dest = new byte[10];
        b.getBytes(0, dest, 0, 10);

        for (int i = 0; i < 10; i++) {
            assertEquals(source[90 + i], dest[i]);
        }
    }

    @Test
    void testWriteAndReadBytes() {
        int bytesLen = 100;
        byte[] bytes = randomByteArray(bytesLen);

        Buffer b = from();
        b.writeBytes(bytes);
        assertEquals(b.writerIndex(), bytes.length);

        byte[] dest = new byte[bytesLen];
        b.readBytes(dest);
        assertArrayEquals(bytes, dest);
        assertEquals(b.readerIndex(), bytes.length);

        b.writeBytes(bytes, 90, 10);
        assertEquals(b.writerIndex(), bytes.length + 10);

        dest = new byte[10];
        b.readBytes(dest, 0, 10);
        for (int i = 0; i < 10; i++) {
            assertEquals(bytes[90 + i], dest[i]);
        }
    }

    @Test
    void testSlice() {
        Buffer buff = randomBuf(100);
        Buffer sliced = buff.slice();
        assertEquals(buff, sliced);
        long rand = ThreadLocalRandom.current().nextLong();
        sliced.setLong(0, rand);
        assertEquals(rand, buff.getLong(0));
        buff.writeBytes(randomByteArray(100));
        assertEquals(100, sliced.writerIndex());
    }

    @Test
    void testSlice2() {
        Buffer buff = randomBuf(100);
        Buffer sliced = buff.slice(10, 20);
        for (int i = 0; i < 10; i++) {
            assertEquals(buff.getByte(10 + i), sliced.getByte(i));
        }
        long rand = ThreadLocalRandom.current().nextLong();
        sliced.setLong(0, rand);
        assertEquals(rand, buff.getLong(10));
    }

    @Test
    void testCopy() {
        byte[] source = randomByteArray(100);
        Buffer buf = from();
        buf.writeBytes(source);

        assertEquals(buf.readerIndex(), 0);
        assertEquals(buf.writerIndex(), 100);

        Buffer copied = buf.copy();
        assertEquals(copied.readerIndex(), 0);
        assertEquals(copied.writerIndex(), 100);

        buf.writeBytes(source);
        buf.readBytes(new byte[100]);
        assertEquals(buf.readerIndex(), 100);
        assertEquals(buf.writerIndex(), 200);

        assertEquals(copied.readerIndex(), 0);
        assertEquals(copied.writerIndex(), 100);
    }

    @Test
    void testGetByteBuf() {
        final io.netty.buffer.ByteBuf underlying = ByteBufAllocator.DEFAULT.buffer();
        Buffer buf = new BufferImpl(underlying);
        assertSame(underlying, buf.getByteBuf());
    }

    private void allZeroIndex(Buffer buf) {
        assertEquals(0, buf.readerIndex());
        assertEquals(0, buf.writerIndex());
    }

    private void allNotZeroIndex(Buffer buf) {
        assertNotEquals(0, buf.readerIndex());
        assertNotEquals(0, buf.writerIndex());
    }

    private static Buffer from() {
        return new BufferImpl(Unpooled.EMPTY_BUFFER.alloc());
    }

    private static Buffer from(byte[] data) {
        return new BufferImpl(Unpooled.EMPTY_BUFFER.alloc(), data.length).writeBytes(data);
    }

    private static Buffer randomBuf(int length) {
        return from(randomByteArray(length));
    }

    private static byte[] randomByteArray(int length) {
        byte[] bytes = new byte[length];
        ThreadLocalRandom.current().nextBytes(bytes);
        return bytes;
    }
}
