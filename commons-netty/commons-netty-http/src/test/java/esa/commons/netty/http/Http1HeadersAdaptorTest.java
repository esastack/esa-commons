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

import io.netty.handler.codec.http.HttpHeaders;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Http1HeadersAdaptorTest
 */
class Http1HeadersAdaptorTest {

    @Test
    void testSetNullHeaderValueValidate() {
        final Http1HeadersAdaptor adaptor = new Http1HeadersAdaptor();
        assertThrows(IllegalArgumentException.class, () -> adaptor.set("\t", "foo"));
        assertThrows(NullPointerException.class, () -> adaptor.set("foo", null));
    }

    @Test
    void testSetNullHeaderValueNotValidate() {
        final Http1HeadersAdaptor adaptor = new Http1HeadersAdaptor(false);
        adaptor.set("\t", "foo");
        assertThrows(NullPointerException.class, () -> adaptor.set("foo", null));
    }

    @Test
    void testDelegateByOutComing() {
        final HttpHeaders mock = mock(HttpHeaders.class);
        final Http1HeadersAdaptor adaptor = new Http1HeadersAdaptor(mock);

        final String strKey = "k1";

        when(mock.get(strKey)).thenReturn("foo");
        assertEquals("foo", adaptor.get(strKey));
        verify(mock).get(same(strKey));

        final CharSequence charSeqKey = "k2";

        when(mock.get(charSeqKey)).thenReturn("bar");
        assertEquals("bar", adaptor.get(charSeqKey));
        verify(mock).get(same(charSeqKey));

        when(mock.get(charSeqKey, "default")).thenReturn("default");
        assertEquals("default", adaptor.get(charSeqKey, "default"));
        verify(mock).get(same(charSeqKey), eq("default"));

        final List<String> list = Arrays.asList("a", "b");
        when(mock.getAll(strKey)).thenReturn(list);
        assertSame(list, adaptor.getAll(strKey));
        verify(mock).getAll(same(strKey));

        when(mock.getAll(charSeqKey)).thenReturn(list);
        assertSame(list, adaptor.getAll(charSeqKey));
        verify(mock).getAll(same(charSeqKey));

        reset(mock);

        assertNull(adaptor.getBoolean(charSeqKey));
        assertTrue(adaptor.getBoolean(charSeqKey, true));
        when(mock.get(charSeqKey)).thenReturn("true");
        assertTrue(adaptor.getBoolean(charSeqKey));
        when(mock.get(charSeqKey)).thenReturn("false");
        assertFalse(adaptor.getBoolean(charSeqKey));
        assertFalse(adaptor.getBoolean(charSeqKey, true));
        when(mock.get(charSeqKey)).thenReturn("?");
        assertFalse(adaptor.getBoolean(charSeqKey));

        reset(mock);

        assertNull(adaptor.getByte(charSeqKey));
        assertEquals((byte) 1, adaptor.getByte(charSeqKey, (byte) 1));
        when(mock.get(charSeqKey)).thenReturn("2");
        assertEquals((byte) 2, adaptor.getByte(charSeqKey));
        when(mock.get(charSeqKey)).thenReturn("?");
        assertNull(adaptor.getByte(charSeqKey));

        reset(mock);

        assertNull(adaptor.getChar(charSeqKey));
        assertEquals((char) 1, adaptor.getChar(charSeqKey, (char) 1));
        when(mock.get(charSeqKey)).thenReturn("2");
        assertEquals("2".charAt(0), adaptor.getChar(charSeqKey));
        when(mock.get(charSeqKey)).thenReturn("?");

        reset(mock);
        when(mock.getShort(charSeqKey)).thenReturn((short) 1);
        when(mock.getShort(charSeqKey, (short) 2)).thenReturn((short) 1);
        assertEquals((short) 1, adaptor.getShort(charSeqKey));
        assertEquals((short) 1, adaptor.getShort(charSeqKey, (short) 2));
        verify(mock).getShort(same(charSeqKey));
        verify(mock).getShort(same(charSeqKey), eq((short) 2));

        when(mock.getInt(charSeqKey)).thenReturn(1);
        when(mock.getInt(charSeqKey, 2)).thenReturn(1);
        assertEquals(1, adaptor.getInt(charSeqKey));
        assertEquals(1, adaptor.getInt(charSeqKey, 2));
        verify(mock).getInt(same(charSeqKey));
        verify(mock).getInt(same(charSeqKey), eq(2));

        reset(mock);

        assertNull(adaptor.getLong(charSeqKey));
        assertEquals(1L, adaptor.getLong(charSeqKey, 1L));
        when(mock.get(charSeqKey)).thenReturn("2");
        assertEquals(2L, adaptor.getLong(charSeqKey));
        when(mock.get(charSeqKey)).thenReturn("?");
        assertNull(adaptor.getLong(charSeqKey));

        reset(mock);

        assertNull(adaptor.getFloat(charSeqKey));
        assertEquals(1.0f, adaptor.getFloat(charSeqKey, 1.0f));
        when(mock.get(charSeqKey)).thenReturn("2.0");
        assertEquals(2.0f, adaptor.getFloat(charSeqKey));
        when(mock.get(charSeqKey)).thenReturn("?");
        assertNull(adaptor.getFloat(charSeqKey));

        reset(mock);

        assertNull(adaptor.getDouble(charSeqKey));
        assertEquals(1.0D, adaptor.getDouble(charSeqKey, 1.0D));
        when(mock.get(charSeqKey)).thenReturn("2.0");
        assertEquals(2.0D, adaptor.getDouble(charSeqKey));
        when(mock.get(charSeqKey)).thenReturn("?");
        assertNull(adaptor.getDouble(charSeqKey));

        reset(mock);

        when(mock.contains(strKey)).thenReturn(true);
        when(mock.contains(charSeqKey)).thenReturn(true);
        when(mock.contains(strKey, strKey, true)).thenReturn(true);
        when(mock.contains(strKey, strKey, false)).thenReturn(true);
        when(mock.contains(charSeqKey, charSeqKey, true)).thenReturn(true);
        when(mock.contains(charSeqKey, charSeqKey, false)).thenReturn(true);
        assertTrue(adaptor.contains(strKey));
        assertTrue(adaptor.contains(charSeqKey));
        assertTrue(adaptor.contains(strKey, strKey));
        assertTrue(adaptor.contains(charSeqKey, charSeqKey));
        assertTrue(adaptor.contains(strKey, strKey, true));
        assertTrue(adaptor.contains(charSeqKey, charSeqKey, true));
        verify(mock).contains(same(strKey));
        verify(mock).contains(same(charSeqKey));
        verify(mock).contains(same(strKey), same(strKey), eq(true));
        verify(mock).contains(same(strKey), same(strKey), eq(false));
        verify(mock).contains(same(charSeqKey), same(charSeqKey), eq(true));
        verify(mock).contains(same(charSeqKey), same(charSeqKey), eq(false));

        when(mock.size()).thenReturn(1);
        assertEquals(1, adaptor.size());
        verify(mock).size();

        when(mock.isEmpty()).thenReturn(true);
        assertTrue(adaptor.isEmpty());
        verify(mock).isEmpty();

        final Set<String> names = new TreeSet<>();
        when(mock.names()).thenReturn(names);
        assertSame(names, adaptor.names());
        verify(mock).names();

        assertSame(adaptor, adaptor.add(strKey, "a"));
        verify(mock).add(same(strKey), eq("a"));

        reset(mock);

        assertSame(adaptor, adaptor.add(charSeqKey, "a"));
        verify(mock).add(same(charSeqKey), eq("a"));

        reset(mock);

        assertSame(adaptor, adaptor.add(strKey, list));
        verify(mock).add(same(strKey), same(list));

        reset(mock);

        assertSame(adaptor, adaptor.add(charSeqKey, list));
        verify(mock).add(same(charSeqKey), same(list));

        reset(mock);

        assertSame(adaptor, adaptor.addBoolean(charSeqKey, true));
        verify(mock).add(same(charSeqKey), eq("true"));

        reset(mock);

        assertSame(adaptor, adaptor.addByte(charSeqKey, (byte) 1));
        verify(mock).add(same(charSeqKey), eq("1"));

        reset(mock);

        assertSame(adaptor, adaptor.addChar(charSeqKey, "a".charAt(0)));
        verify(mock).add(same(charSeqKey), eq("a"));

        reset(mock);

        assertSame(adaptor, adaptor.addShort(charSeqKey, (short) 1));
        verify(mock).addShort(same(charSeqKey), eq((short) 1));

        reset(mock);

        assertSame(adaptor, adaptor.addInt(charSeqKey, 1));
        verify(mock).addInt(same(charSeqKey), eq(1));

        reset(mock);

        assertSame(adaptor, adaptor.addLong(charSeqKey, 1L));
        verify(mock).add(same(charSeqKey), eq("1"));

        reset(mock);

        assertSame(adaptor, adaptor.addFloat(charSeqKey, 1.0f));
        verify(mock).add(same(charSeqKey), eq("1.0"));

        reset(mock);

        assertSame(adaptor, adaptor.addDouble(charSeqKey, 1.0D));
        verify(mock).add(same(charSeqKey), eq("1.0"));

        reset(mock);

        final Http1HeadersImpl headers = mock(Http1HeadersImpl.class);
        assertThrows(IllegalArgumentException.class, () -> adaptor.add(adaptor));
        assertSame(adaptor, adaptor.add(headers));
        verify(mock).add(same(headers));

        final Http2HeadersAdaptor h2 = new Http2HeadersAdaptor();
        h2.add("a", "a1");
        h2.add("b", "b1");
        assertSame(adaptor, adaptor.add(h2));

        verify(mock).add(eq("a"), eq("a1"));
        verify(mock).add(eq("b"), eq("b1"));

        reset(mock);

        assertSame(adaptor, adaptor.set(strKey, "a"));
        verify(mock).set(same(strKey), eq("a"));

        reset(mock);

        assertSame(adaptor, adaptor.set(charSeqKey, "a"));
        verify(mock).set(same(charSeqKey), eq("a"));

        reset(mock);

        assertSame(adaptor, adaptor.set(strKey, list));
        verify(mock).set(same(strKey), same(list));

        reset(mock);

        assertSame(adaptor, adaptor.set(charSeqKey, list));
        verify(mock).set(same(charSeqKey), same(list));

        reset(mock);

        assertSame(adaptor, adaptor.setBoolean(charSeqKey, true));
        verify(mock).set(same(charSeqKey), eq("true"));

        reset(mock);

        assertSame(adaptor, adaptor.setByte(charSeqKey, (byte) 1));
        verify(mock).set(same(charSeqKey), eq("1"));

        reset(mock);

        assertSame(adaptor, adaptor.setChar(charSeqKey, "a".charAt(0)));
        verify(mock).set(same(charSeqKey), eq("a"));

        reset(mock);

        assertSame(adaptor, adaptor.setShort(charSeqKey, (short) 1));
        verify(mock).setShort(same(charSeqKey), eq((short) 1));

        reset(mock);

        assertSame(adaptor, adaptor.setInt(charSeqKey, 1));
        verify(mock).setInt(same(charSeqKey), eq(1));

        reset(mock);

        assertSame(adaptor, adaptor.setLong(charSeqKey, 1L));
        verify(mock).set(same(charSeqKey), eq("1"));

        reset(mock);

        assertSame(adaptor, adaptor.setFloat(charSeqKey, 1.0f));
        verify(mock).set(same(charSeqKey), eq("1.0"));

        reset(mock);

        assertSame(adaptor, adaptor.setDouble(charSeqKey, 1.0D));
        verify(mock).set(same(charSeqKey), eq("1.0"));

        assertSame(adaptor, adaptor.set(adaptor));
        assertSame(adaptor, adaptor.set(headers));
        verify(mock).set(same(headers));

        assertSame(adaptor, adaptor.set(h2));
        verify(mock).clear();
        verify(mock).add(eq("a"), eq("a1"));
        verify(mock).add(eq("b"), eq("b1"));

        reset(mock);

        when(headers.isEmpty()).thenReturn(true);
        assertSame(adaptor, adaptor.setAll(headers));

        reset(headers);
        when(headers.isEmpty()).thenReturn(false);
        assertSame(adaptor, adaptor.setAll(headers));
        verify(mock).setAll(same(headers));

        assertSame(adaptor, adaptor.setAll(h2));
        verify(mock).set(eq("a"), eq("a1"));
        verify(mock).set(eq("b"), eq("b1"));

        reset(mock);

        assertSame(adaptor, adaptor.remove(strKey));
        verify(mock).remove(same(strKey));
        assertSame(adaptor, adaptor.remove(charSeqKey));
        verify(mock).remove(same(charSeqKey));

        assertSame(adaptor, adaptor.clear());
        verify(mock).clear();


        final Map<CharSequence, CharSequence> m = Collections.emptyMap();
        final Iterator<Map.Entry<CharSequence, CharSequence>> it = m.entrySet().iterator();
        when(mock.iteratorCharSequence()).thenReturn(it);

        assertSame(it, adaptor.iteratorCharSequence());
        verify(mock).iteratorCharSequence();

        final Map<String, String> m1 = Collections.emptyMap();
        final Iterator<Map.Entry<String, String>> it1 = m1.entrySet().iterator();

        when(mock.iterator()).thenReturn(it1);
        assertSame(it1, adaptor.iterator());
        verify(mock).iterator();
    }

    @Test
    void testToString() {
        final Http1HeadersAdaptor headers = new Http1HeadersAdaptor();
        headers.add("a", "b");
        headers.add("c", "d");
        assertEquals("Http1HeadersAdaptor[a: b, c: d]", headers.toString());
    }

}
