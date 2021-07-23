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

import esa.commons.Checks;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Implementation of {@link Buffer} that wraps an {@link #underlying}.
 * @deprecated use https://github.com/esastack/esa-commons-net instead
 */
@Deprecated
public class BufferImpl implements Buffer {

    /**
     * @see Unpooled#EMPTY_BUFFER
     */
    static final BufferImpl EMPTY_BUFFER = new BufferImpl(Unpooled.EMPTY_BUFFER);

    private final io.netty.buffer.ByteBuf underlying;

    public BufferImpl() {
        this(Unpooled.buffer());
    }

    public BufferImpl(int initialCapacity) {
        this(Unpooled.buffer(initialCapacity));
    }

    public BufferImpl(int initialCapacity, int maxCapacity) {
        this(Unpooled.buffer(initialCapacity, maxCapacity));
    }

    public BufferImpl(ByteBufAllocator allocator) {
        this(allocator.buffer());
    }

    public BufferImpl(ByteBufAllocator allocator, int initialCapacity) {
        this(allocator.buffer(initialCapacity));
    }

    public BufferImpl(ByteBufAllocator allocator, int initialCapacity, int maxCapacity) {
        this(allocator.buffer(initialCapacity, maxCapacity));
    }

    public BufferImpl(io.netty.buffer.ByteBuf underlying) {
        Checks.checkNotNull(underlying, "underlying");
        this.underlying = underlying;
    }

    @Override
    public byte getByte(int pos) {
        return underlying.getByte(pos);
    }

    @Override
    public boolean getBoolean(int pos) {
        return underlying.getBoolean(pos);
    }

    @Override
    public char getChar(int index) {
        return underlying.getChar(index);
    }

    @Override
    public short getShort(int pos) {
        return underlying.getShort(pos);
    }

    @Override
    public int getInt(int pos) {
        return underlying.getInt(pos);
    }

    @Override
    public int getMedium(int pos) {
        return underlying.getMedium(pos);
    }

    @Override
    public long getLong(int pos) {
        return underlying.getLong(pos);
    }

    @Override
    public double getDouble(int pos) {
        return underlying.getDouble(pos);
    }

    @Override
    public float getFloat(int pos) {
        return underlying.getFloat(pos);
    }

    @Override
    public byte readByte() {
        return underlying.readByte();
    }

    @Override
    public boolean readBoolean() {
        return underlying.readBoolean();
    }

    @Override
    public char readChar() {
        return underlying.readChar();
    }

    @Override
    public short readShort() {
        return underlying.readShort();
    }

    @Override
    public int readMedium() {
        return underlying.readMedium();
    }

    @Override
    public int readInt() {
        return underlying.readInt();
    }

    @Override
    public long readLong() {
        return underlying.readLong();
    }

    @Override
    public double readDouble() {
        return underlying.readDouble();
    }

    @Override
    public float readFloat() {
        return underlying.readFloat();
    }

    @Override
    public Buffer setByte(int pos, byte b) {
        underlying.setByte(pos, b);
        return this;
    }

    @Override
    public Buffer setBoolean(int pos, boolean b) {
        underlying.setBoolean(pos, b);
        return this;
    }

    @Override
    public Buffer setChar(int index, int value) {
        underlying.setChar(index, value);
        return this;
    }

    @Override
    public Buffer setShort(int pos, short s) {
        underlying.setShort(pos, s);
        return this;
    }

    @Override
    public Buffer setMedium(int pos, int i) {
        underlying.setMedium(pos, i);
        return this;
    }

    @Override
    public Buffer setInt(int pos, int i) {
        underlying.setInt(pos, i);
        return this;
    }

    @Override
    public Buffer setLong(int pos, long l) {
        underlying.setLong(pos, l);
        return this;
    }

    @Override
    public Buffer setDouble(int pos, double d) {
        underlying.setDouble(pos, d);
        return this;
    }

    @Override
    public Buffer setFloat(int pos, float f) {
        underlying.setFloat(pos, f);
        return this;
    }

    @Override
    public Buffer writeByte(byte b) {
        this.underlying.writeByte(b);
        return this;
    }

    @Override
    public Buffer writeBoolean(boolean b) {
        this.underlying.writeBoolean(b);
        return this;
    }

    @Override
    public Buffer writeChar(int b) {
        this.underlying.writeChar(b);
        return this;
    }

    @Override
    public Buffer writeShort(short s) {
        this.underlying.writeShort(s);
        return this;
    }

    @Override
    public Buffer writeMedium(int s) {
        this.underlying.writeMedium(s);
        return this;
    }

    @Override
    public Buffer writeInt(int i) {
        this.underlying.writeInt(i);
        return this;
    }

    @Override
    public Buffer writeLong(long l) {
        this.underlying.writeLong(l);
        return this;
    }

    @Override
    public Buffer writeDouble(double d) {
        this.underlying.writeDouble(d);
        return this;
    }

    @Override
    public Buffer writeFloat(float f) {
        this.underlying.writeFloat(f);
        return this;
    }

    @Override
    public Buffer getBytes(int index, byte[] dst) {
        underlying.getBytes(index, dst);
        return this;
    }

    @Override
    public Buffer getBytes(int index, byte[] dst, int dstIndex, int length) {
        underlying.getBytes(index, dst, dstIndex, length);
        return this;
    }

    @Override
    public Buffer readBytes(byte[] dst) {
        underlying.readBytes(dst);
        return this;
    }

    @Override
    public Buffer readBytes(byte[] dst, int dstIndex, int length) {
        underlying.readBytes(dst, dstIndex, length);
        return this;
    }

    @Override
    public Buffer setBytes(int index, byte[] src) {
        underlying.setBytes(index, src);
        return this;
    }

    @Override
    public Buffer setBytes(int index, byte[] src, int srcIndex, int length) {
        underlying.setBytes(index, src, srcIndex, length);
        return this;
    }

    @Override
    public Buffer writeBytes(byte[] src) {
        underlying.writeBytes(src);
        return this;
    }

    @Override
    public Buffer writeBytes(byte[] src, int srcIndex, int length) {
        underlying.writeBytes(src, srcIndex, length);
        return this;
    }

    @Override
    public int capacity() {
        return underlying.capacity();
    }

    @Override
    public int writerIndex() {
        return underlying.writerIndex();
    }

    @Override
    public int readerIndex() {
        return underlying.readerIndex();
    }

    @Override
    public boolean isReadable() {
        return underlying.isReadable();
    }

    @Override
    public boolean isReadable(int size) {
        return underlying.isReadable(size);
    }

    @Override
    public boolean isWritable() {
        return underlying.isWritable();
    }

    @Override
    public boolean isWritable(int size) {
        return underlying.isWritable(size);
    }

    @Override
    public int readableBytes() {
        return underlying.readableBytes();
    }

    @Override
    public int writableBytes() {
        return underlying.writableBytes();
    }

    @Override
    public int maxWritableBytes() {
        return underlying.maxWritableBytes();
    }

    @Override
    public Buffer clear() {
        underlying.clear();
        return this;
    }

    @Override
    public Buffer copy() {
        return new BufferImpl(underlying.copy());
    }

    @Override
    public Buffer slice() {
        return new BufferImpl(underlying.slice());
    }

    @Override
    public Buffer slice(int start, int length) {
        return new BufferImpl(underlying.slice(start, length));
    }

    @Override
    public io.netty.buffer.ByteBuf getByteBuf() {
        return underlying;
    }

    @Override
    public String string(Charset charset) {
        return underlying.toString(StandardCharsets.UTF_8);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BufferImpl buffer1 = (BufferImpl) o;
        return Objects.equals(underlying, buffer1.underlying);
    }

    @Override
    public String toString() {
        return underlying.toString();
    }

    @Override
    public int hashCode() {
        return underlying.hashCode();
    }

}
