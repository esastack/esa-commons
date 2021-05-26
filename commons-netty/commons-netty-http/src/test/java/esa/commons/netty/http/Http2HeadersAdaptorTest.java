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

import io.netty.handler.codec.http2.Http2Exception;
import io.netty.handler.codec.http2.Http2Headers;
import io.netty.util.AsciiString;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Http1HeadersAdaptorTest
 */
class Http2HeadersAdaptorTest {

    @Test
    void testSetNullHeaderValueValidate() {
        final Http2HeadersAdaptor adaptor = new Http2HeadersAdaptor();
        assertThrows(Http2Exception.class, () -> adaptor.set("A", "foo"));
        assertThrows(NullPointerException.class, () -> adaptor.set("foo", null));
    }

    @Test
    void testSetNullHeaderValueNotValidate() {
        final Http2HeadersAdaptor adaptor = new Http2HeadersAdaptor(false);
        adaptor.set("A", "foo");
        assertThrows(NullPointerException.class, () -> adaptor.set("foo", null));
    }

    @Test
    void testDelegateByOutComing() {
        final Http2Headers mock = mock(Http2Headers.class);
        final Http2HeadersAdaptor adaptor = new Http2HeadersAdaptor(mock);

        final String strKey = "k";
        final CharSequence charSeqKey = AsciiString.of(strKey);

        when(mock.get(strKey)).thenReturn("foo");
        assertEquals("foo", adaptor.get(strKey));
        verify(mock).get(same(strKey));
        when(mock.get(charSeqKey)).thenReturn("bar");
        assertEquals("bar", adaptor.get(charSeqKey));
        verify(mock).get(same(charSeqKey));

        assertEquals("bar", adaptor.get(charSeqKey, "default"));

        reset(mock);

        final List<CharSequence> list = Arrays.asList("a", "b");
        when(mock.getAll(strKey)).thenReturn(list);
        assertArrayEquals(list.toArray(), adaptor.getAll(strKey).toArray());
        verify(mock).getAll(same(strKey));

        when(mock.getAll(charSeqKey)).thenReturn(list);
        assertArrayEquals(list.toArray(), adaptor.getAll(charSeqKey).toArray());
        verify(mock).getAll(same(charSeqKey));

        reset(mock);

        when(mock.getBoolean(charSeqKey)).thenReturn(null);
        assertNull(adaptor.getBoolean(charSeqKey));
        verify(mock).getBoolean(same(charSeqKey));

        when(mock.getBoolean(charSeqKey, true)).thenReturn(true);
        assertTrue(adaptor.getBoolean(charSeqKey, true));
        verify(mock).getBoolean(same(charSeqKey), eq(true));

        when(mock.getByte(charSeqKey)).thenReturn(null);
        assertNull(adaptor.getByte(charSeqKey));
        verify(mock).getByte(same(charSeqKey));

        reset(mock);

        when(mock.getByte(charSeqKey)).thenReturn((byte) 1);
        assertEquals((byte) 1, adaptor.getByte(charSeqKey));
        verify(mock).getByte(same(charSeqKey));

        when(mock.getByte(charSeqKey, (byte) 1)).thenReturn((byte) 1);
        assertEquals((byte) 1, adaptor.getByte(charSeqKey, (byte) 1));
        verify(mock).getByte(same(charSeqKey), eq((byte) 1));


        when(mock.getChar(charSeqKey)).thenReturn(null);
        assertNull(adaptor.getChar(charSeqKey));
        verify(mock).getChar(same(charSeqKey));

        reset(mock);

        when(mock.getChar(charSeqKey)).thenReturn((char) 1);
        assertEquals((char) 1, adaptor.getChar(charSeqKey));
        verify(mock).getChar(same(charSeqKey));

        when(mock.getChar(charSeqKey, (char) 1)).thenReturn((char) 1);
        assertEquals((char) 1, adaptor.getChar(charSeqKey, (char) 1));
        verify(mock).getChar(same(charSeqKey), eq((char) 1));

        when(mock.getShort(charSeqKey)).thenReturn(null);
        assertNull(adaptor.getShort(charSeqKey));
        verify(mock).getShort(same(charSeqKey));

        reset(mock);

        when(mock.getShort(charSeqKey)).thenReturn((short) 1);
        assertEquals((short) 1, adaptor.getShort(charSeqKey));
        verify(mock).getShort(same(charSeqKey));

        when(mock.getShort(charSeqKey, (short) 1)).thenReturn((short) 1);
        assertEquals((short) 1, adaptor.getShort(charSeqKey, (short) 1));
        verify(mock).getShort(same(charSeqKey), eq((short) 1));

        when(mock.getInt(charSeqKey)).thenReturn(null);
        assertNull(adaptor.getInt(charSeqKey));
        verify(mock).getInt(same(charSeqKey));

        reset(mock);

        when(mock.getInt(charSeqKey)).thenReturn(1);
        assertEquals(1, adaptor.getInt(charSeqKey));
        verify(mock).getInt(same(charSeqKey));

        when(mock.getInt(charSeqKey, 1)).thenReturn(1);
        assertEquals(1, adaptor.getInt(charSeqKey, 1));
        verify(mock).getInt(same(charSeqKey), eq(1));

        when(mock.getLong(charSeqKey)).thenReturn(null);
        assertNull(adaptor.getLong(charSeqKey));
        verify(mock).getLong(same(charSeqKey));

        reset(mock);

        when(mock.getLong(charSeqKey)).thenReturn(1L);
        assertEquals(1L, adaptor.getLong(charSeqKey));
        verify(mock).getLong(same(charSeqKey));

        when(mock.getLong(charSeqKey, 1L)).thenReturn(1L);
        assertEquals(1L, adaptor.getLong(charSeqKey, 1L));
        verify(mock).getLong(same(charSeqKey), eq(1L));

        when(mock.getFloat(charSeqKey)).thenReturn(null);
        assertNull(adaptor.getFloat(charSeqKey));
        verify(mock).getFloat(same(charSeqKey));

        reset(mock);

        when(mock.getFloat(charSeqKey)).thenReturn(1f);
        assertEquals(1f, adaptor.getFloat(charSeqKey));
        verify(mock).getFloat(same(charSeqKey));

        when(mock.getFloat(charSeqKey, 1f)).thenReturn(1f);
        assertEquals(1f, adaptor.getFloat(charSeqKey, 1f));
        verify(mock).getFloat(same(charSeqKey), eq(1f));

        when(mock.getDouble(charSeqKey)).thenReturn(null);
        assertNull(adaptor.getDouble(charSeqKey));
        verify(mock).getFloat(same(charSeqKey));

        reset(mock);

        when(mock.getDouble(charSeqKey)).thenReturn(1D);
        assertEquals(1D, adaptor.getDouble(charSeqKey));
        verify(mock).getDouble(same(charSeqKey));

        when(mock.getDouble(charSeqKey, 1D)).thenReturn(1D);
        assertEquals(1D, adaptor.getDouble(charSeqKey, 1D));
        verify(mock).getDouble(same(charSeqKey), eq(1D));

        reset(mock);

        when(mock.contains(strKey)).thenReturn(true);
        when(mock.contains(charSeqKey)).thenReturn(true);
        when(mock.contains(strKey, strKey, true)).thenReturn(true);
        when(mock.contains(charSeqKey, charSeqKey, true)).thenReturn(true);
        assertTrue(adaptor.contains(strKey));
        assertTrue(adaptor.contains(charSeqKey));
        assertTrue(adaptor.contains(strKey, strKey, true));
        assertTrue(adaptor.contains(charSeqKey, charSeqKey, true));
        verify(mock).contains(same(strKey));
        verify(mock).contains(same(charSeqKey));
        verify(mock).contains(same(strKey), same(strKey), eq(true));
        verify(mock).contains(same(charSeqKey), same(charSeqKey), eq(true));

        when(mock.size()).thenReturn(1);
        assertEquals(1, adaptor.size());
        verify(mock).size();

        when(mock.isEmpty()).thenReturn(true);
        assertTrue(adaptor.isEmpty());
        verify(mock).isEmpty();

        final Set<CharSequence> names = new TreeSet<>(Arrays.asList("a", "b"));
        when(mock.names()).thenReturn(names);
        assertArrayEquals(names.toArray(), adaptor.names().toArray());
        verify(mock).names();

        assertSame(adaptor, adaptor.add(strKey, "a"));
        verify(mock).addObject(same(strKey), eq("a"));

        reset(mock);

        assertSame(adaptor, adaptor.add(charSeqKey, "a"));
        verify(mock).addObject(same(charSeqKey), eq("a"));

        reset(mock);

        assertSame(adaptor, adaptor.add(strKey, list));
        verify(mock).addObject(same(strKey), same(list));

        reset(mock);

        assertSame(adaptor, adaptor.add(charSeqKey, list));
        verify(mock).addObject(same(charSeqKey), same(list));

        reset(mock);

        assertSame(adaptor, adaptor.addBoolean(charSeqKey, true));
        verify(mock).addBoolean(same(charSeqKey), eq(true));

        reset(mock);

        assertSame(adaptor, adaptor.addByte(charSeqKey, (byte) 1));
        verify(mock).addByte(same(charSeqKey), eq((byte) 1));

        reset(mock);

        assertSame(adaptor, adaptor.addChar(charSeqKey, "a".charAt(0)));
        verify(mock).addChar(same(charSeqKey), eq("a".charAt(0)));

        reset(mock);

        assertSame(adaptor, adaptor.addShort(charSeqKey, (short) 1));
        verify(mock).addShort(same(charSeqKey), eq((short) 1));

        reset(mock);

        assertSame(adaptor, adaptor.addInt(charSeqKey, 1));
        verify(mock).addInt(same(charSeqKey), eq(1));

        reset(mock);

        assertSame(adaptor, adaptor.addLong(charSeqKey, 1L));
        verify(mock).addLong(same(charSeqKey), eq(1L));

        reset(mock);

        assertSame(adaptor, adaptor.addFloat(charSeqKey, 1.0f));
        verify(mock).addFloat(same(charSeqKey), eq(1.0f));

        reset(mock);

        assertSame(adaptor, adaptor.addDouble(charSeqKey, 1.0D));
        verify(mock).addDouble(same(charSeqKey), eq(1.0D));

        reset(mock);

        assertThrows(IllegalArgumentException.class, () -> adaptor.add(adaptor));

        final Http2HeadersAdaptor h2 = new Http2HeadersAdaptor();
        h2.add("a", "a1");
        h2.add("b", "b1");
        assertSame(adaptor, adaptor.add(h2));

        verify(mock).addObject(eq("a"), eq("a1"));
        verify(mock).addObject(eq("b"), eq("b1"));

        reset(mock);

        assertSame(adaptor, adaptor.set(strKey, "a"));
        verify(mock).setObject(same(strKey), eq("a"));

        reset(mock);

        assertSame(adaptor, adaptor.set(charSeqKey, "a"));
        verify(mock).setObject(same(charSeqKey), eq("a"));

        reset(mock);

        assertSame(adaptor, adaptor.set(strKey, list));
        verify(mock).setObject(same(strKey), same(list));

        reset(mock);

        assertSame(adaptor, adaptor.set(charSeqKey, list));
        verify(mock).setObject(same(charSeqKey), same(list));

        reset(mock);

        assertSame(adaptor, adaptor.setBoolean(charSeqKey, true));
        verify(mock).setBoolean(same(charSeqKey), eq(true));

        reset(mock);

        assertSame(adaptor, adaptor.setByte(charSeqKey, (byte) 1));
        verify(mock).setByte(same(charSeqKey), eq((byte) 1));

        reset(mock);

        assertSame(adaptor, adaptor.setChar(charSeqKey, "a".charAt(0)));
        verify(mock).setChar(same(charSeqKey), eq("a".charAt(0)));

        reset(mock);

        assertSame(adaptor, adaptor.setShort(charSeqKey, (short) 1));
        verify(mock).setShort(same(charSeqKey), eq((short) 1));

        reset(mock);

        assertSame(adaptor, adaptor.setInt(charSeqKey, 1));
        verify(mock).setInt(same(charSeqKey), eq(1));

        reset(mock);

        assertSame(adaptor, adaptor.setLong(charSeqKey, 1L));
        verify(mock).setLong(same(charSeqKey), eq(1L));

        reset(mock);

        assertSame(adaptor, adaptor.setFloat(charSeqKey, 1.0f));
        verify(mock).setFloat(same(charSeqKey), eq(1.0f));

        reset(mock);

        assertSame(adaptor, adaptor.setDouble(charSeqKey, 1.0D));
        verify(mock).setDouble(same(charSeqKey), eq(1.0D));

        assertSame(adaptor, adaptor.set(adaptor));

        assertSame(adaptor, adaptor.set(h2));
        verify(mock).clear();
        verify(mock).addObject(eq("a"), eq("a1"));
        verify(mock).addObject(eq("b"), eq("b1"));

        reset(mock);

        assertSame(adaptor, adaptor.setAll(h2));
        verify(mock).setObject(eq("a"), eq("a1"));
        verify(mock).setObject(eq("b"), eq("b1"));

        reset(mock);

        assertSame(adaptor, adaptor.remove(strKey));
        verify(mock).remove(same(strKey));
        assertSame(adaptor, adaptor.remove(charSeqKey));
        verify(mock).remove(same(charSeqKey));

        assertSame(adaptor, adaptor.clear());
        verify(mock).clear();

        final Iterator<Map.Entry<CharSequence, CharSequence>> it = mock(Iterator.class);

        when(mock.iterator()).thenReturn(it);

        assertSame(it, adaptor.iteratorCharSequence());
        verify(mock).iterator();

        when(it.hasNext()).thenReturn(true);

        final Map.Entry<CharSequence, CharSequence> entry = mock(Map.Entry.class);
        when(it.next()).thenReturn(entry);

        final Iterator<Map.Entry<String, String>> it2 = adaptor.iterator();

        assertTrue(it2.hasNext());
        verify(it).hasNext();

        when(entry.getKey()).thenReturn("a");
        when(entry.getValue()).thenReturn("b");
        assertEquals("a", it2.next().getKey());
        assertEquals("b", it2.next().getValue());
        it2.next().setValue("a");
        verify(entry).setValue(eq("a"));

        verify(mock, times(2)).iterator();
    }

    @Test
    void testToString() {
        final Http2HeadersAdaptor headers = new Http2HeadersAdaptor();
        headers.add("a", "b");
        headers.add("c", "d");
        assertEquals("Http2HeadersAdaptor[a: b, c: d]", headers.toString());
    }

}
