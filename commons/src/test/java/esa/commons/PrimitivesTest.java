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
package esa.commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PrimitivesTest {

    @Test
    void testIsWrapperType() {
        assertTrue(Primitives.isWrapperType(Boolean.class));
        assertTrue(Primitives.isWrapperType(Byte.class));
        assertTrue(Primitives.isWrapperType(Character.class));
        assertTrue(Primitives.isWrapperType(Short.class));
        assertTrue(Primitives.isWrapperType(Integer.class));
        assertTrue(Primitives.isWrapperType(Long.class));
        assertTrue(Primitives.isWrapperType(Float.class));
        assertTrue(Primitives.isWrapperType(Double.class));
        assertTrue(Primitives.isWrapperType(Void.class));

        assertFalse(Primitives.isWrapperType(null));
        assertFalse(Primitives.isWrapperType(Object.class));
        assertFalse(Primitives.isWrapperType(boolean.class));
        assertFalse(Primitives.isWrapperType(byte.class));
        assertFalse(Primitives.isWrapperType(char.class));
        assertFalse(Primitives.isWrapperType(int.class));
        assertFalse(Primitives.isWrapperType(long.class));
        assertFalse(Primitives.isWrapperType(double.class));
        assertFalse(Primitives.isWrapperType(float.class));
        assertFalse(Primitives.isWrapperType(void.class));
    }

    @Test
    void testIsPrimitiveType() {
        assertTrue(Primitives.isPrimitiveType(boolean.class));
        assertTrue(Primitives.isPrimitiveType(byte.class));
        assertTrue(Primitives.isPrimitiveType(char.class));
        assertTrue(Primitives.isPrimitiveType(int.class));
        assertTrue(Primitives.isPrimitiveType(long.class));
        assertTrue(Primitives.isPrimitiveType(double.class));
        assertTrue(Primitives.isPrimitiveType(float.class));
        assertTrue(Primitives.isPrimitiveType(void.class));

        assertFalse(Primitives.isPrimitiveType(null));
        assertFalse(Primitives.isPrimitiveType(Object.class));
        assertFalse(Primitives.isPrimitiveType(Boolean.class));
        assertFalse(Primitives.isPrimitiveType(Byte.class));
        assertFalse(Primitives.isPrimitiveType(Character.class));
        assertFalse(Primitives.isPrimitiveType(Short.class));
        assertFalse(Primitives.isPrimitiveType(Integer.class));
        assertFalse(Primitives.isPrimitiveType(Long.class));
        assertFalse(Primitives.isPrimitiveType(Float.class));
        assertFalse(Primitives.isPrimitiveType(Double.class));
        assertFalse(Primitives.isPrimitiveType(Void.class));
    }

    @Test
    void testIsPrimitiveOrWrapperType() {
        assertTrue(Primitives.isPrimitiveOrWraperType(boolean.class));
        assertTrue(Primitives.isPrimitiveOrWraperType(byte.class));
        assertTrue(Primitives.isPrimitiveOrWraperType(char.class));
        assertTrue(Primitives.isPrimitiveOrWraperType(int.class));
        assertTrue(Primitives.isPrimitiveOrWraperType(long.class));
        assertTrue(Primitives.isPrimitiveOrWraperType(double.class));
        assertTrue(Primitives.isPrimitiveOrWraperType(float.class));
        assertTrue(Primitives.isPrimitiveOrWraperType(void.class));
        assertTrue(Primitives.isPrimitiveOrWraperType(Boolean.class));
        assertTrue(Primitives.isPrimitiveOrWraperType(Byte.class));
        assertTrue(Primitives.isPrimitiveOrWraperType(Character.class));
        assertTrue(Primitives.isPrimitiveOrWraperType(Short.class));
        assertTrue(Primitives.isPrimitiveOrWraperType(Integer.class));
        assertTrue(Primitives.isPrimitiveOrWraperType(Long.class));
        assertTrue(Primitives.isPrimitiveOrWraperType(Float.class));
        assertTrue(Primitives.isPrimitiveOrWraperType(Double.class));
        assertTrue(Primitives.isPrimitiveOrWraperType(Void.class));

        assertFalse(Primitives.isPrimitiveOrWraperType(null));
        assertFalse(Primitives.isPrimitiveOrWraperType(Object.class));
    }

    @Test
    void testDefaultValue() {
        assertEquals((byte) 0, Primitives.defaultValue(byte.class));
        assertEquals((byte) 0, Primitives.defaultValue(Byte.class));
        assertEquals((short) 0, Primitives.defaultValue(short.class));
        assertEquals((short) 0, Primitives.defaultValue(Short.class));
        assertEquals(0, Primitives.defaultValue(int.class));
        assertEquals(0, Primitives.defaultValue(Integer.class));
        assertEquals(0L, Primitives.defaultValue(long.class));
        assertEquals(0L, Primitives.defaultValue(Long.class));
        assertEquals(0f, Primitives.defaultValue(float.class));
        assertEquals(0f, Primitives.defaultValue(Float.class));
        assertEquals(0D, Primitives.defaultValue(double.class));
        assertEquals(0D, Primitives.defaultValue(Double.class));
        assertEquals('\u0000', Primitives.defaultValue(char.class));
        assertEquals('\u0000', Primitives.defaultValue(Character.class));
        assertEquals(false, Primitives.defaultValue(boolean.class));
        assertEquals(false, Primitives.defaultValue(Boolean.class));
        assertNull(Primitives.defaultValue(Void.class));
        assertNull(Primitives.defaultValue(void.class));

        assertNull(Primitives.defaultValue(null));
        assertNull(Primitives.defaultValue(String.class));
    }

}
