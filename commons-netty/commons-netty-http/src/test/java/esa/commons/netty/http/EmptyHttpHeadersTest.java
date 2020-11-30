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
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

/**
 * EmptyHttpHeadersTest
 */
class EmptyHttpHeadersTest {

    @Test
    void testAll() {
        final EmptyHttpHeaders headers = EmptyHttpHeaders.INSTANCE;
        final String strKey = "foo";
        final CharSequence charSeqKey = "bar";
        assertNull(headers.get(strKey));
        assertNull(headers.get(charSeqKey));

        assertTrue(headers.getAll(strKey).isEmpty());
        assertTrue(headers.getAll(charSeqKey).isEmpty());

        assertNull(headers.getBoolean(strKey));
        assertTrue(headers.getBoolean(strKey, true));
        assertNull(headers.getBoolean(charSeqKey));
        assertTrue(headers.getBoolean(charSeqKey, true));

        assertNull(headers.getByte(strKey));
        assertEquals((byte) 1, headers.getByte(strKey, (byte) 1));
        assertNull(headers.getBoolean(charSeqKey));
        assertEquals((byte) 1, headers.getByte(charSeqKey, (byte) 1));

        assertNull(headers.getChar(strKey));
        assertEquals((char) 1, headers.getChar(strKey, (char) 1));
        assertNull(headers.getChar(charSeqKey));
        assertEquals((char) 1, headers.getChar(charSeqKey, (char) 1));

        assertNull(headers.getShort(strKey));
        assertEquals((short) 1, headers.getShort(strKey, (short) 1));
        assertNull(headers.getShort(charSeqKey));
        assertEquals((short) 1, headers.getShort(charSeqKey, (short) 1));

        assertNull(headers.getInt(strKey));
        assertEquals(1, headers.getInt(strKey, 1));
        assertNull(headers.getInt(charSeqKey));
        assertEquals(1, headers.getInt(charSeqKey, 1));

        assertNull(headers.getLong(strKey));
        assertEquals(1L, headers.getLong(strKey, 1L));
        assertNull(headers.getLong(charSeqKey));
        assertEquals(1L, headers.getLong(charSeqKey, 1L));

        assertNull(headers.getFloat(strKey));
        assertEquals(1.0f, headers.getFloat(strKey, 1.0f));
        assertNull(headers.getFloat(charSeqKey));
        assertEquals(1.0f, headers.getFloat(charSeqKey, 1.0f));

        assertNull(headers.getDouble(strKey));
        assertEquals(1.0D, headers.getDouble(strKey, 1.0D));
        assertNull(headers.getDouble(charSeqKey));
        assertEquals(1.0D, headers.getDouble(charSeqKey, 1.0D));

        assertFalse(headers.contains(strKey));
        assertFalse(headers.contains(charSeqKey));
        assertFalse(headers.contains(strKey, strKey));
        assertFalse(headers.contains(charSeqKey, charSeqKey));
        assertFalse(headers.contains(strKey, strKey, true));
        assertFalse(headers.contains(charSeqKey, charSeqKey, true));

        assertTrue(headers.isEmpty());
        assertEquals(0, headers.size());

        assertTrue(headers.names().isEmpty());

        assertThrows(UnsupportedOperationException.class, () -> headers.add(strKey, ""));
        assertThrows(UnsupportedOperationException.class, () -> headers.add(charSeqKey, ""));
        assertThrows(UnsupportedOperationException.class, () -> headers.add(strKey, Collections.emptyList()));
        assertThrows(UnsupportedOperationException.class, () -> headers.add(charSeqKey, Collections.emptyList()));
        assertThrows(UnsupportedOperationException.class, () -> headers.addBoolean(charSeqKey, true));
        assertThrows(UnsupportedOperationException.class, () -> headers.addByte(charSeqKey, (byte) 1));
        assertThrows(UnsupportedOperationException.class, () -> headers.addChar(charSeqKey, (char) 1));
        assertThrows(UnsupportedOperationException.class, () -> headers.addShort(charSeqKey, (short) 1));
        assertThrows(UnsupportedOperationException.class, () -> headers.addInt(charSeqKey, 1));
        assertThrows(UnsupportedOperationException.class, () -> headers.addLong(charSeqKey, 1L));
        assertThrows(UnsupportedOperationException.class, () -> headers.addFloat(charSeqKey, 1.0f));
        assertThrows(UnsupportedOperationException.class, () -> headers.addDouble(charSeqKey, 1.0D));
        assertThrows(UnsupportedOperationException.class, () -> headers.add(headers));


        assertThrows(UnsupportedOperationException.class, () -> headers.set(strKey, ""));
        assertThrows(UnsupportedOperationException.class, () -> headers.set(charSeqKey, ""));
        assertThrows(UnsupportedOperationException.class, () -> headers.set(strKey, Collections.emptyList()));
        assertThrows(UnsupportedOperationException.class, () -> headers.set(charSeqKey, Collections.emptyList()));
        assertThrows(UnsupportedOperationException.class, () -> headers.setBoolean(charSeqKey, true));
        assertThrows(UnsupportedOperationException.class, () -> headers.setByte(charSeqKey, (byte) 1));
        assertThrows(UnsupportedOperationException.class, () -> headers.setChar(charSeqKey, (char) 1));
        assertThrows(UnsupportedOperationException.class, () -> headers.setShort(charSeqKey, (short) 1));
        assertThrows(UnsupportedOperationException.class, () -> headers.setInt(charSeqKey, 1));
        assertThrows(UnsupportedOperationException.class, () -> headers.setLong(charSeqKey, 1L));
        assertThrows(UnsupportedOperationException.class, () -> headers.setFloat(charSeqKey, 1.0f));
        assertThrows(UnsupportedOperationException.class, () -> headers.setDouble(charSeqKey, 1.0D));
        assertThrows(UnsupportedOperationException.class, () -> headers.set(headers));
        assertThrows(UnsupportedOperationException.class, () -> headers.setAll(headers));

        assertThrows(UnsupportedOperationException.class, () -> headers.remove(strKey));
        assertThrows(UnsupportedOperationException.class, () -> headers.remove(charSeqKey));
        assertThrows(UnsupportedOperationException.class, headers::clear);

        assertFalse(headers.iteratorCharSequence().hasNext());
        assertFalse(headers.iterator().hasNext());
    }

    @Test
    void testToString() {
        HttpHeaders headers = EmptyHttpHeaders.INSTANCE;
        assertEquals("EmptyHttpHeaders[]", headers.toString());
    }

}
