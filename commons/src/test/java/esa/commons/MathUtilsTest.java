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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MathUtilsTest {

    @Test
    void testIsPowerOfTwo() {
        assertTrue(MathUtils.isPowerOfTwo(0));
        assertTrue(MathUtils.isPowerOfTwo(1));
        assertTrue(MathUtils.isPowerOfTwo(2));
        assertTrue(MathUtils.isPowerOfTwo(4));
        assertFalse(MathUtils.isPowerOfTwo(-2));
        assertFalse(MathUtils.isPowerOfTwo(-1));
        assertFalse(MathUtils.isPowerOfTwo(3));
        assertFalse(MathUtils.isPowerOfTwo(6));
    }

    @Test
    void testNextPowerOfTwo() {
        assertEquals(1, MathUtils.nextPowerOfTwo(0));
        assertEquals(1, MathUtils.nextPowerOfTwo(1));
        assertEquals(2, MathUtils.nextPowerOfTwo(2));
        assertEquals(4, MathUtils.nextPowerOfTwo(3));
        assertEquals(8, MathUtils.nextPowerOfTwo(7));
        assertEquals(1, MathUtils.nextPowerOfTwo(-1));
        assertEquals(1 << 30, MathUtils.nextPowerOfTwo(1 << 30));
        assertEquals(1 << 30, MathUtils.nextPowerOfTwo(Integer.MAX_VALUE));
    }

    @Test
    void testRound() {
        assertEquals(10.12D, MathUtils.round(10.119D));
        assertEquals(10.12D, MathUtils.round(10.124D));

        final Double d1 = 10.119D;
        final Double d2 = 10.124D;
        assertEquals(10.12D, MathUtils.round(d1));
        assertEquals(10.12D, MathUtils.round(d2));
        assertNull(MathUtils.round(null));
    }
}
