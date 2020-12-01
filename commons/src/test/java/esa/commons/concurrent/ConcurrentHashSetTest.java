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
package esa.commons.concurrent;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConcurrentHashSetTest {

    @Test
    void testNormal() {
        final ConcurrentHashSet<Integer> set = new ConcurrentHashSet<>();
        assertTrue(set.isEmpty());
        assertEquals(0, set.size());
        assertFalse(set.iterator().hasNext());

        assertTrue(set.add(1));
        assertTrue(set.add(2));
        assertFalse(set.add(2));

        assertFalse(set.isEmpty());
        assertEquals(2, set.size());
        assertTrue(set.iterator().hasNext());
        assertTrue(set.contains(1));
        assertTrue(set.contains(2));
        assertFalse(set.contains(3));

        assertTrue(set.remove(1));
        assertFalse(set.remove(3));

        assertEquals(1, set.size());
        assertTrue(set.iterator().hasNext());
        assertFalse(set.contains(1));
        assertTrue(set.contains(2));

        set.clear();
        assertTrue(set.isEmpty());
        assertEquals(0, set.size());
        assertFalse(set.iterator().hasNext());

        assertFalse(set.contains(1));
        assertFalse(set.contains(2));

        final ConcurrentHashSet<Integer> set1 = new ConcurrentHashSet<>(2);
        assertTrue(set1.add(1));
        assertTrue(set1.add(2));
        assertTrue(set1.add(3));
        assertFalse(set1.isEmpty());
        assertEquals(3, set1.size());
        assertTrue(set1.iterator().hasNext());
        assertTrue(set1.contains(1));
        assertTrue(set1.contains(2));
        assertTrue(set1.contains(3));
    }

}
