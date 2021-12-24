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

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class ObjectUtilsTest {

    @Test
    void testDefaultValue() {

        assertNull(ObjectUtils.defaultValue(Object.class));
        assertNull(ObjectUtils.defaultValue(null));
        assertNull(ObjectUtils.defaultValue(int[].class));

        assertEquals((byte) 0, ObjectUtils.defaultValue(byte.class));
        assertEquals((short) 0, ObjectUtils.defaultValue(short.class));
        assertEquals(0, ObjectUtils.defaultValue(int.class));
        assertEquals(0L, ObjectUtils.defaultValue(long.class));
        assertEquals(0f, ObjectUtils.defaultValue(float.class));
        assertEquals(0D, ObjectUtils.defaultValue(double.class));
        assertEquals('\u0000', ObjectUtils.defaultValue(char.class));
        assertEquals(false, ObjectUtils.defaultValue(boolean.class));
    }

    @Test
    void testInstantiate() {
        assertThrows(IllegalArgumentException.class, () -> ObjectUtils.instantiateBeanIfNecessary(Function.class));
        assertNotNull(ObjectUtils.instantiateBeanIfNecessary(Object.class));
        final Object o = new Object();
        assertEquals(o, ObjectUtils.instantiateBeanIfNecessary(o));
        assertNull(ObjectUtils.instantiateBeanIfNecessary(null));
    }

    @Test
    void testInstantiateFromNoneConstructor() {
        assertThrows(Exception.class, () -> ObjectUtils.instantiateBeanIfNecessary(int.class));
    }

    @Test
    void testInstantiateFromNonPublicConstructor() {
        assertThrows(IllegalArgumentException.class, () -> ObjectUtils.instantiateBeanIfNecessary(A.class));
    }

    @Test
    void testInstantiateError() {
        assertThrows(IllegalArgumentException.class, () -> ObjectUtils.instantiateBeanIfNecessary(B.class));
    }

    @Test
    void testMultiInstantiate() {
        assertTrue(ObjectUtils.instantiateBeansIfNecessary(null).isEmpty());
        assertTrue(ObjectUtils.instantiateBeansIfNecessary(Collections.EMPTY_LIST).isEmpty());

        final Object o = new Object();
        final Collection<?> instantiates =
                ObjectUtils.instantiateBeansIfNecessary(Arrays.asList(Object.class, o, null));
        final Iterator<?> it = instantiates.iterator();
        assertEquals(2, instantiates.size());
        assertNotNull(it.next());
        assertEquals(o, it.next());
    }

    @Test
    void testSafeEquals() {
        final Object o1 = new Object();
        final Object o2 = new Object();
        assertTrue(ObjectUtils.safeEquals(o1, o1));
        assertTrue(ObjectUtils.safeEquals(null, null));
        assertFalse(ObjectUtils.safeEquals(o1, null));
        assertFalse(ObjectUtils.safeEquals(null, o2));
        assertFalse(ObjectUtils.safeEquals(o1, o2));
        assertTrue(ObjectUtils.safeEquals("foo", "foo"));
        assertTrue(ObjectUtils.safeEquals(new String("foo"), new String("foo")));

        assertFalse(ObjectUtils.safeEquals(new Object[]{o1}, o2));
        assertFalse(ObjectUtils.safeEquals(o1, new Object[]{o2}));
        assertTrue(ObjectUtils.safeEquals(new Object[]{o1, o2}, new Object[]{o1, o2}));

        assertTrue(ObjectUtils.safeEquals(new boolean[]{true, false}, new boolean[]{true, false}));
        assertFalse(ObjectUtils.safeEquals(new boolean[]{true, false}, new boolean[]{false, true}));

        assertTrue(ObjectUtils.safeEquals(new byte[]{0, 1}, new byte[]{0, 1}));
        assertFalse(ObjectUtils.safeEquals(new byte[]{0, 1}, new byte[]{1, 0}));

        assertTrue(ObjectUtils.safeEquals(new char[]{0, 1}, new char[]{0, 1}));
        assertFalse(ObjectUtils.safeEquals(new char[]{0, 1}, new char[]{1, 0}));

        assertTrue(ObjectUtils.safeEquals(new short[]{0, 1}, new short[]{0, 1}));
        assertFalse(ObjectUtils.safeEquals(new short[]{0, 1}, new short[]{1, 0}));

        assertTrue(ObjectUtils.safeEquals(new double[]{0.0D, 1.0D}, new double[]{0.0D, 1.0D}));
        assertFalse(ObjectUtils.safeEquals(new double[]{0.0D, 1.0D}, new double[]{1.0D, 0.0D}));

        assertTrue(ObjectUtils.safeEquals(new float[]{0.0f, 1.0f}, new float[]{0.0f, 1.0f}));
        assertFalse(ObjectUtils.safeEquals(new float[]{0.0f, 1.0f}, new float[]{1.0f, 0.0f}));

        assertTrue(ObjectUtils.safeEquals(new int[]{0, 1}, new int[]{0, 1}));
        assertFalse(ObjectUtils.safeEquals(new int[]{0, 1}, new int[]{1, 0}));

        assertTrue(ObjectUtils.safeEquals(new long[]{0L, 1L}, new long[]{0L, 1L}));
        assertFalse(ObjectUtils.safeEquals(new long[]{0L, 1L}, new long[]{1L, 0L}));

        assertTrue(ObjectUtils.safeEquals(new char[]{0, 1}, new char[]{0, 1}));
        assertFalse(ObjectUtils.safeEquals(new char[]{0, 1}, new char[]{1, 0}));
    }

    private static class A {
        private A() {
        }
    }

    private static class B {
        public B() {
            throw new RuntimeException();
        }
    }

}
