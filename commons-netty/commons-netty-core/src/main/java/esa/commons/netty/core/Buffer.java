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

import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

/**
 * Interface wraps the {@link ByteBuf}.
 */
public interface Buffer {

    /**
     * Gets a byte at the specified absolute {@code index} in this buffer. This method does not modify {@code
     * readerIndex} or {@code writerIndex} of this buffer.
     *
     * @throws IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or {@code index + 1} is
     *                                   greater than {@code this.capacity}
     */
    byte getByte(int pos);

    /**
     * Gets a boolean at the specified absolute (@code index) in this buffer. This method does not modify the {@code
     * readerIndex} or {@code writerIndex} of this buffer.
     *
     * @throws IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or {@code index + 1} is
     *                                   greater than {@code this.capacity}
     */
    boolean getBoolean(int pos);

    /**
     * Gets a 2-byte UTF-16 character at the specified absolute {@code index} in this buffer.  This method does not
     * modify {@code readerIndex} or {@code writerIndex} of this buffer.
     *
     * @throws IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or {@code index + 2} is
     *                                   greater than {@code this.capacity}
     */
    char getChar(int index);

    /**
     * Gets a 16-bit short integer at the specified absolute {@code index} in this buffer.  This method does not modify
     * {@code readerIndex} or {@code writerIndex} of this buffer.
     *
     * @throws IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or {@code index + 2} is
     *                                   greater than {@code this.capacity}
     */
    short getShort(int pos);

    /**
     * Gets a 32-bit integer at the specified absolute {@code index} in this buffer.  This method does not modify {@code
     * readerIndex} or {@code writerIndex} of this buffer.
     *
     * @throws IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or {@code index + 4} is
     *                                   greater than {@code this.capacity}
     */
    int getInt(int pos);

    /**
     * Gets a 24-bit medium integer at the specified absolute {@code index} in this buffer.  This method does not modify
     * {@code readerIndex} or {@code writerIndex} of this buffer.
     *
     * @throws IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or {@code index + 3} is
     *                                   greater than {@code this.capacity}
     */
    int getMedium(int pos);

    /**
     * Gets a 64-bit long integer at the specified absolute {@code index} in this buffer.  This method does not modify
     * {@code readerIndex} or {@code writerIndex} of this buffer.
     *
     * @throws IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or {@code index + 8} is
     *                                   greater than {@code this.capacity}
     */
    long getLong(int pos);

    /**
     * Gets a 64-bit floating point number at the specified absolute {@code index} in this buffer.  This method does not
     * modify {@code readerIndex} or {@code writerIndex} of this buffer.
     *
     * @throws IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or {@code index + 8} is
     *                                   greater than {@code this.capacity}
     */
    double getDouble(int pos);

    /**
     * Gets a 32-bit floating point number at the specified absolute {@code index} in this buffer.  This method does not
     * modify {@code readerIndex} or {@code writerIndex} of this buffer.
     *
     * @throws IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or {@code index + 4} is
     *                                   greater than {@code this.capacity}
     */
    float getFloat(int pos);

    /**
     * Gets a byte at the current {@code readerIndex} and increases the {@code readerIndex} by {@code 1} in this
     * buffer.
     * <p>
     * The operation will modify the {@code readerIndex}.
     *
     * @return byte
     * @throws IndexOutOfBoundsException if {@code this.readableBytes} is less than {@code 1}
     */
    byte readByte();

    /**
     * Gets a boolean at the current {@code readerIndex} and increases the {@code readerIndex} by {@code 1} in this
     * buffer.
     * <p>
     * The operation will modify the {@code readerIndex}.
     *
     * @throws IndexOutOfBoundsException if {@code this.readableBytes} is less than {@code 1}
     */
    boolean readBoolean();

    /**
     * Gets a 2-byte UTF-16 character at the current {@code readerIndex} and increases the {@code readerIndex} by {@code
     * 2} in this buffer.
     * <p>
     * The operation will modify the {@code readerIndex}.
     *
     * @return char
     * @throws IndexOutOfBoundsException if {@code this.readableBytes} is less than {@code 2}
     */
    char readChar();

    /**
     * Gets a 16-bit short integer at the current {@code readerIndex} and increases the {@code readerIndex} by {@code 2}
     * in this buffer.
     * <p>
     * The operation will modify the {@code readerIndex}.
     *
     * @return short
     * @throws IndexOutOfBoundsException if {@code this.readableBytes} is less than {@code 2}
     */
    short readShort();

    /**
     * Gets a 24-bit medium integer at the current {@code readerIndex} and increases the {@code readerIndex} by {@code
     * 3} in this buffer.
     * <p>
     * The operation will modify the {@code readerIndex}.
     *
     * @return int
     * @throws IndexOutOfBoundsException if {@code this.readableBytes} is less than {@code 3}
     */
    int readMedium();

    /**
     * Gets a 32-bit integer at the current {@code readerIndex} and increases the {@code readerIndex} by {@code 4} in
     * this buffer.
     * <p>
     * The operation will modify the {@code readerIndex}.
     *
     * @return int
     * @throws IndexOutOfBoundsException if {@code this.readableBytes} is less than {@code 4}
     */
    int readInt();

    /**
     * Gets a 64-bit integer at the current {@code readerIndex} and increases the {@code readerIndex} by {@code 8} in
     * this buffer.
     * <p>
     * The operation will modify the {@code readerIndex}.
     *
     * @return long
     * @throws IndexOutOfBoundsException if {@code this.readableBytes} is less than {@code 8}
     */
    long readLong();

    /**
     * Gets a 64-bit floating point number at the current {@code readerIndex} and increases the {@code readerIndex} by
     * {@code 8} in this buffer.
     * <p>
     * The operation will modify the {@code readerIndex}.
     *
     * @return double
     * @throws IndexOutOfBoundsException if {@code this.readableBytes} is less than {@code 8}
     */
    double readDouble();

    /**
     * Gets a 32-bit floating point number at the current {@code readerIndex} and increases the {@code readerIndex} by
     * {@code 4} in this buffer.
     * <p>
     * The operation will modify the {@code readerIndex}.
     *
     * @return float
     * @throws IndexOutOfBoundsException if {@code this.readableBytes} is less than {@code 4}
     */
    float readFloat();

    /**
     * Sets the specified byte at the specified absolute {@code index} in this buffer.  The 24 high-order bits of the
     * specified value are ignored. This method does not modify {@code readerIndex} or {@code writerIndex} of this
     * buffer.
     *
     * @return this
     * @throws IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or {@code index + 1} is
     *                                   greater than {@code this.capacity}
     */
    Buffer setByte(int pos, byte b);

    /**
     * Sets the specified boolean at the specified absolute {@code index} in this buffer. This method does not modify
     * {@code readerIndex} or {@code writerIndex} of this buffer.
     *
     * @return this
     * @throws IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or {@code index + 1} is
     *                                   greater than {@code this.capacity}
     */
    Buffer setBoolean(int pos, boolean b);

    /**
     * Sets the specified 2-byte UTF-16 character at the specified absolute {@code index} in this buffer. The 16
     * high-order bits of the specified value are ignored. This method does not modify {@code readerIndex} or {@code
     * writerIndex} of this buffer.
     *
     * @return this
     * @throws IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or {@code index + 2} is
     *                                   greater than {@code this.capacity}
     */
    Buffer setChar(int index, int value);

    /**
     * Sets the specified 16-bit short integer at the specified absolute {@code index} in this buffer.  The 16
     * high-order bits of the specified value are ignored. This method does not modify {@code readerIndex} or {@code
     * writerIndex} of this buffer.
     *
     * @return this
     * @throws IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or {@code index + 2} is
     *                                   greater than {@code this.capacity}
     */
    Buffer setShort(int pos, short s);

    /**
     * Sets the specified 24-bit medium integer at the specified absolute {@code index} in this buffer.  Please note
     * that the most significant byte is ignored in the specified value. This method does not modify {@code readerIndex}
     * or {@code writerIndex} of this buffer.
     *
     * @return this
     * @throws IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or {@code index + 3} is
     *                                   greater than {@code this.capacity}
     */
    Buffer setMedium(int pos, int i);

    /**
     * Sets the specified 32-bit integer at the specified absolute {@code index} in this buffer. This method does not
     * modify {@code readerIndex} or {@code writerIndex} of this buffer.
     *
     * @return this
     * @throws IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or {@code index + 4} is
     *                                   greater than {@code this.capacity}
     */
    Buffer setInt(int pos, int i);

    /**
     * Sets the specified 64-bit long integer at the specified absolute {@code index} in this buffer. This method does
     * not modify {@code readerIndex} or {@code writerIndex} of this buffer.
     *
     * @return this
     * @throws IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or {@code index + 8} is
     *                                   greater than {@code this.capacity}
     */
    Buffer setLong(int pos, long l);

    /**
     * Sets the specified 64-bit floating-point number at the specified absolute {@code index} in this buffer. This
     * method does not modify {@code readerIndex} or {@code writerIndex} of this buffer.
     *
     * @return this
     * @throws IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or {@code index + 8} is
     *                                   greater than {@code this.capacity}
     */
    Buffer setDouble(int pos, double d);

    /**
     * Sets the specified 32-bit floating-point number at the specified absolute {@code index} in this buffer. This
     * method does not modify {@code readerIndex} or {@code writerIndex} of this buffer.
     *
     * @return this
     * @throws IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or {@code index + 4} is
     *                                   greater than {@code this.capacity}
     */
    Buffer setFloat(int pos, float f);

    /**
     * Sets the specified byte at the current {@code writerIndex} and increases the {@code writerIndex} by {@code 1} in
     * this buffer.
     *
     * @return this
     */
    Buffer writeByte(byte b);

    /**
     * Sets the specified boolean at the current {@code writerIndex} and increases the {@code writerIndex} by {@code 1}
     * in this buffer.
     *
     * @return this
     */
    Buffer writeBoolean(boolean b);

    /**
     * Sets the specified 2-byte UTF-16 character at the current {@code writerIndex} and increases the {@code
     * writerIndex} by {@code 2} in this buffer.  The 16 high-order bits of the specified value are ignored.
     *
     * @return this
     */
    Buffer writeChar(int b);

    /**
     * Sets the specified 16-bit short integer at the current {@code writerIndex} and increases the {@code writerIndex}
     * by {@code 2} in this buffer.  The 16 high-order bits of the specified value are ignored.
     *
     * @return this
     */
    Buffer writeShort(short s);

    /**
     * Sets the specified 24-bit medium integer at the current {@code writerIndex} and increases the {@code writerIndex}
     * by {@code 3} in this buffer.
     *
     * @return this
     */
    Buffer writeMedium(int s);

    /**
     * Sets the specified 32-bit integer at the current {@code writerIndex} and increases the {@code writerIndex} by
     * {@code 4} in this buffer.
     *
     * @return this
     */
    Buffer writeInt(int i);

    /**
     * Sets the specified 64-bit long integer at the current {@code writerIndex} and increases the {@code writerIndex}
     * by {@code 8} in this buffer.
     *
     * @return this
     */
    Buffer writeLong(long l);

    /**
     * Sets the specified 64-bit floating point number at the current {@code writerIndex} and increases the {@code
     * writerIndex} by {@code 8} in this buffer.
     *
     * @return this
     */
    Buffer writeDouble(double d);

    /**
     * Sets the specified 32-bit floating point number at the current {@code writerIndex} and increases the {@code
     * writerIndex} by {@code 4} in this buffer.
     *
     * @return this
     */
    Buffer writeFloat(float f);

    /**
     * Transfers this buffer's data to the specified destination starting at the specified absolute {@code index}. This
     * method does not modify {@code readerIndex} or {@code writerIndex} of this buffer
     *
     * @param index start position
     * @param dst   destination array
     *
     * @return byteBuf
     * @throws IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or if {@code index +
     *                                   dst.length} is greater than {@code this.capacity}
     */
    Buffer getBytes(int index, byte[] dst);

    /**
     * Transfers this buffer's data to the specified destination starting at the specified absolute {@code index}. This
     * method does not modify {@code readerIndex} or {@code writerIndex} of this buffer.
     *
     * @param index    the first index of start
     * @param dst      destination array
     * @param dstIndex the first index of the destination
     * @param length   the number of bytes to transfer
     *
     * @return byteBuf
     * @throws IndexOutOfBoundsException if the specified {@code index} is less than {@code 0}, if the specified {@code
     *                                   dstIndex} is less than {@code 0}, if {@code index + length} is greater than
     *                                   {@code this.capacity}, or if {@code dstIndex + length} is greater than {@code
     *                                   dst.length}
     */
    Buffer getBytes(int index, byte[] dst, int dstIndex, int length);

    /**
     * Transfers this buffer's data to the specified destination starting at the current {@code readerIndex} and
     * increases the {@code readerIndex} by the number of the transferred bytes (= {@code dst.length}).
     *
     * @throws IndexOutOfBoundsException if {@code dst.length} is greater than {@code this.readableBytes}
     */
    Buffer readBytes(byte[] dst);

    /**
     * Transfers this buffer's data to the specified destination starting at the current {@code readerIndex} and
     * increases the {@code readerIndex} by the number of the transferred bytes (= {@code length}).
     *
     * @throws IndexOutOfBoundsException if the specified {@code dstIndex} is less than {@code 0}, if {@code length} is
     *                                   greater than {@code this.readableBytes}, or if {@code dstIndex + length} is
     *                                   greater than {@code dst.length}
     */
    Buffer readBytes(byte[] dst, int dstIndex, int length);

    /**
     * Transfers the specified source array's data to this buffer starting at the specified absolute {@code index}. This
     * method does not modify {@code readerIndex} or {@code writerIndex} of this buffer.
     *
     * @param index the first index of start
     * @param src   destination array
     *
     * @throws IndexOutOfBoundsException if the specified {@code index} is less than {@code 0} or if {@code index +
     *                                   src.length} is greater than {@code this.capacity}
     */
    Buffer setBytes(int index, byte[] src);

    /**
     * Transfers the specified source array's data to this buffer starting at the specified absolute {@code index}. This
     * method does not modify {@code readerIndex} or {@code writerIndex} of this buffer.
     *
     * @param index    the first index of start
     * @param src      destination array
     * @param srcIndex the first index of the destination
     * @param length   the number of bytes to transfer
     *
     * @throws IndexOutOfBoundsException if the specified {@code index} is less than {@code 0}, if the specified {@code
     *                                   srcIndex} is less than {@code 0}, if {@code index + length} is greater than
     *                                   {@code this.capacity}, or if {@code srcIndex + length} is greater than {@code
     *                                   src.length}
     */
    Buffer setBytes(int index, byte[] src, int srcIndex, int length);

    /**
     * Transfers the specified source array's data to this buffer starting at the current {@code writerIndex} and
     * increases the {@code writerIndex} by the number of the transferred bytes (= {@code src.length}).
     *
     * @return this
     */
    Buffer writeBytes(byte[] src);

    /**
     * Transfers the specified source array's data to this buffer starting at the current {@code writerIndex} and
     * increases the {@code writerIndex} by the number of the transferred bytes (= {@code length}).
     *
     * @return this
     * @throws IndexOutOfBoundsException if the specified {@code srcIndex} is less than {@code 0}, or if {@code srcIndex
     *                                   + length} is greater than {@code src.length}
     */
    Buffer writeBytes(byte[] src, int srcIndex, int length);

    /**
     * Returns the number of bytes (octets) this buffer can contain.
     */
    int capacity();

    /**
     * Returns the writerIndex of the buffer, measured in bytes.
     *
     * @return writerIndex
     */
    int writerIndex();

    /**
     * Returns the readerIndex of the buffer, measured in bytes.
     *
     * @return readerIndex
     */
    int readerIndex();

    /**
     * Returns {@code true} if and only if {@code (this.writerIndex - this.readerIndex)} is greater than {@code 0}.
     */
    boolean isReadable();

    /**
     * Returns {@code true} if and only if this buffer contains equal to or more than the specified number of elements.
     */
    boolean isReadable(int size);

    /**
     * Returns {@code true} if and only if {@code (this.capacity - this.writerIndex)} is greater than {@code 0}.
     */
    boolean isWritable();

    /**
     * Returns {@code true} if and only if this buffer has enough room to allow writing the specified number of
     * elements.
     */
    boolean isWritable(int size);

    /**
     * Returns the number of readable bytes which is equal to {@code (this.writerIndex - this.readerIndex)}.
     */
    int readableBytes();

    /**
     * Returns the number of writable bytes which is equal to {@code (this.capacity - this.writerIndex)}.
     */
    int writableBytes();

    /**
     * Returns the maximum possible number of writable bytes, which is equal to {@code (this.maxCapacity -
     * this.writerIndex)}.
     */
    int maxWritableBytes();

    /**
     * Sets the {@code readerIndex} and {@code writerIndex} of this buffer to {@code 0}.
     */
    Buffer clear();

    /**
     * Returns a copy of the entire buffer.
     *
     * @return copied buffer
     */
    Buffer copy();

    /**
     * Returns a slice of this buffer. Modifying the content of the returned buffer or this buffer affects each other's
     * content while they maintain separate indexes and marks.
     *
     * @return buffer
     */
    Buffer slice();

    /**
     * Returns a slice of this buffer. Modifying the content of the returned buffer or this buffer affects each other's
     * content while they maintain separate indexes and marks.
     *
     * @param start  start index
     * @param length length
     *
     * @return buffer
     */
    Buffer slice(int start, int length);

    /**
     * Returns the Buffer as a Netty {@code Buffer}.<p> The returned buffer is a duplicate.<p> The returned {@code
     * Buffer} might have its {@code readerIndex > 0} This method is meant for internal use only.<p>
     *
     * @return underlying netty based buffer.
     */
    ByteBuf getByteBuf();

    /**
     * Decodes this buffer's readable bytes into a string with the specified character set name.  This method is
     * identical to {@code buf.toString(buf.readerIndex(), buf.readableBytes(), charsetName)}. This method does not
     * modify {@code readerIndex} or {@code writerIndex} of this buffer.
     *
     * @throws UnsupportedCharsetException if the specified character set name is not supported by the current VM
     */
    String string(Charset charset);
}
