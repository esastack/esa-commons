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
package esa.commons.netty.http;

import esa.commons.http.HttpHeaders;
import io.netty.handler.codec.DefaultHeaders;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.util.AsciiString;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Http1HeadersImplTest
 */
class Http1HeadersImplTest {


    @Test
    void testSetNullHeaderValueValidate() {
        final Http1HeadersImpl headers = new Http1HeadersImpl();
        assertThrows(IllegalArgumentException.class, () -> headers.set("\t", "foo"));
        assertThrows(NullPointerException.class, () -> headers.set("foo", null));
    }

    @Test
    void testSetNullHeaderValueNotValidate() {
        final Http1HeadersImpl headers = new Http1HeadersImpl(false);
        headers.set("\t", "foo");
        assertThrows(NullPointerException.class, () -> headers.set("foo", null));
    }

    @Test
    void testCustomValidator() {
        final Http1HeadersImpl headers = new Http1HeadersImpl(true, name -> {
            if ("foo".equals(name.toString())) {
                throw new UnsupportedOperationException();
            }
        });
        assertThrows(UnsupportedOperationException.class, () -> headers.set("foo", "1"));
        assertThrows(NullPointerException.class, () -> headers.set("bar", null));
    }

    @Test
    void testCustomUnderlyingHeaders() {
        final DefaultHeaders<CharSequence, CharSequence, ?> mock = mock(DefaultHeaders.class);
        final Http1HeadersImpl headers = new Http1HeadersImpl(mock);
        headers.set("foo", "1");
        verify(mock).setObject(eq("foo"), eq("1"));
    }

    @Test
    void testSetAndGet() {

        final Http1HeadersImpl headers = new Http1HeadersImpl();
        final String strK1 = "s1";
        final String strK2 = "s2";
        final CharSequence charSeqK1 = AsciiString.of("c1");
        final CharSequence charSeqK2 = AsciiString.of("c2");

        assertSame(headers, headers.addBoolean(strK1, true));
        assertSame(headers, headers.addBoolean(charSeqK1, false));
        assertSame(headers, headers.setBoolean(strK2, true));
        assertSame(headers, headers.setBoolean(charSeqK2, false));

        assertTrue(headers.getBoolean(strK1));
        assertTrue(headers.getBoolean(strK2));
        assertFalse(headers.getBoolean(charSeqK1));
        assertFalse(headers.getBoolean(charSeqK2));
        assertFalse(headers.getBoolean("other", false));

        assertSame(headers, headers.clear());

        assertSame(headers, headers.addByte(strK1, (byte) 1));
        assertSame(headers, headers.addByte(charSeqK1, (byte) 2));
        assertSame(headers, headers.setByte(strK2, (byte) 1));
        assertSame(headers, headers.setByte(charSeqK2, (byte) 2));
        assertEquals((byte) 1, headers.getByte(strK1));
        assertEquals((byte) 2, headers.getByte(charSeqK1));
        assertEquals((byte) 1, headers.getByte(strK2));
        assertEquals((byte) 2, headers.getByte(charSeqK2));
        assertEquals((byte) 3, headers.getByte("other", (byte) 3));

        assertSame(headers, headers.clear());

        assertSame(headers, headers.addChar(strK1, (char) 1));
        assertSame(headers, headers.addChar(charSeqK1, (char) 2));
        assertSame(headers, headers.setChar(strK2, (char) 1));
        assertSame(headers, headers.setChar(charSeqK2, (char) 2));
        assertEquals((char) 1, headers.getChar(strK1));
        assertEquals((char) 2, headers.getChar(charSeqK1));
        assertEquals((char) 1, headers.getChar(strK2));
        assertEquals((char) 2, headers.getChar(charSeqK2));
        assertEquals((char) 3, headers.getChar("other", (char) 3));

        assertSame(headers, headers.clear());

        assertSame(headers, headers.addLong(strK1, 1L));
        assertSame(headers, headers.addLong(charSeqK1, 2L));
        assertSame(headers, headers.setLong(strK2, 1L));
        assertSame(headers, headers.setLong(charSeqK2, 2L));
        assertEquals(1L, headers.getLong(strK1));
        assertEquals(2L, headers.getLong(charSeqK1));
        assertEquals(1L, headers.getLong(strK2));
        assertEquals(2L, headers.getLong(charSeqK2));
        assertEquals(3L, headers.getLong("other", 3L));

        assertSame(headers, headers.clear());

        assertSame(headers, headers.addFloat(strK1, 1f));
        assertSame(headers, headers.addFloat(charSeqK1, 2f));
        assertSame(headers, headers.setFloat(strK2, 1f));
        assertSame(headers, headers.setFloat(charSeqK2, 2f));
        assertEquals(1f, headers.getFloat(strK1));
        assertEquals(2f, headers.getFloat(charSeqK1));
        assertEquals(1f, headers.getFloat(strK2));
        assertEquals(2f, headers.getFloat(charSeqK2));
        assertEquals(3f, headers.getFloat("other", 3f));

        assertSame(headers, headers.clear());

        assertSame(headers, headers.addDouble(strK1, 1D));
        assertSame(headers, headers.addDouble(charSeqK1, 2D));
        assertSame(headers, headers.setDouble(strK2, 1D));
        assertSame(headers, headers.setDouble(charSeqK2, 2D));
        assertEquals(1D, headers.getDouble(strK1));
        assertEquals(2D, headers.getDouble(charSeqK1));
        assertEquals(1D, headers.getDouble(strK2));
        assertEquals(2D, headers.getDouble(charSeqK2));
        assertEquals(3D, headers.getDouble("other", 3D));

        assertTrue(headers.contains(strK1, "1.0"));
        assertTrue(headers.contains(charSeqK1, "2.0"));


        assertSame(headers, headers.clear());

        assertThrows(IllegalArgumentException.class, () -> headers.add((HttpHeaders) headers));

        assertSame(headers, headers.clear());

        final HttpHeaders another = new Http1HeadersImpl();
        another.add(strK1, "a");
        assertSame(headers, headers.add(another));
        assertEquals("a", headers.get(strK1));

        assertSame(headers, headers.clear());
        assertSame(headers, headers.add(EmptyHttpHeaders.INSTANCE));
        assertTrue(headers.isEmpty());


        assertSame(headers, headers.set(another));
        assertEquals("a", headers.get(strK1));

        assertSame(headers, headers.set(EmptyHttpHeaders.INSTANCE));
        assertTrue(headers.isEmpty());

        assertSame(headers, headers.setAll(another));
        assertEquals("a", headers.get(strK1));
        assertSame(headers, headers.setAll(EmptyHttpHeaders.INSTANCE));
        assertEquals("a", headers.get(strK1));

        final DefaultHttpHeaders nettyHeaders = new DefaultHttpHeaders();
        nettyHeaders.add(strK2, "c");
        assertSame(headers, headers.set(nettyHeaders));
        assertEquals("c", headers.get(strK2));

        assertSame(headers, headers.clear());

        headers.add(strK1, "a");
        assertSame(headers, headers.remove(strK1));
        assertNull(headers.get(strK1));
        headers.add(charSeqK1, "a");
        assertSame(headers, headers.remove(charSeqK1));
        assertNull(headers.get(charSeqK1));

        headers.add(strK1, "a");

        assertEquals("a", headers.copy().get(strK1));
    }

    @Test
    void testToString() {
        final Http1HeadersImpl headers = new Http1HeadersImpl();
        headers.add("a", "b");
        headers.add("c", "d");
        assertEquals("Http1HeadersImpl[a: b, c: d]", headers.toString());
    }
}
