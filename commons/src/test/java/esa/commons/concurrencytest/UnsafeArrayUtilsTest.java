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
package esa.commons.concurrencytest;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class UnsafeArrayUtilsTest {

    private String[] arr;

    @BeforeAll
    static void before() {
        assumeTrue(UnsafeUtils.hasUnsafe());
    }

    @BeforeEach
    void setUp() {
        arr = new String[2];
    }

    @Test
    void testStaticFields() {
        assertTrue(UnsafeArrayUtils.ARRAY_INDEX_SCALE > 0);
        assertTrue(UnsafeArrayUtils.REF_ARRAY_ELEMENT_SHIFT > 0);
        assertTrue(UnsafeArrayUtils.REF_ARRAY_BASE > 0);
    }

    @Test
    void testGetAndSet() {
        UnsafeArrayUtils.setElement(arr, UnsafeArrayUtils.calcElementOffset(0), "foo");
        assertEquals("foo", arr[0]);
        assertEquals("foo", UnsafeArrayUtils.getElement(arr, UnsafeArrayUtils.calcElementOffset(0)));
        assertEquals("foo", UnsafeArrayUtils.getElementAcquire(arr, UnsafeArrayUtils.calcElementOffset(0)));

        UnsafeArrayUtils.setElementRelease(arr, UnsafeArrayUtils.calcElementOffset(3, 1), "bar");
        assertEquals("bar", arr[1]);
        assertEquals("bar", UnsafeArrayUtils.getElement(arr, UnsafeArrayUtils.calcElementOffset(1)));
        assertEquals("bar", UnsafeArrayUtils.getElementAcquire(arr, UnsafeArrayUtils.calcElementOffset(1)));

        UnsafeArrayUtils.lazySetElement(arr, UnsafeArrayUtils.calcElementOffset(0), "baz");
        assertEquals("baz", arr[0]);
        assertEquals("baz", UnsafeArrayUtils.getElement(arr, UnsafeArrayUtils.calcElementOffset(0)));
        assertEquals("baz", UnsafeArrayUtils.getElementAcquire(arr, UnsafeArrayUtils.calcElementOffset(0)));
    }

}
