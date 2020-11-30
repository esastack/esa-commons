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

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class MpscArrayTest {

    @BeforeAll
    static void before() {
        assumeTrue(UnsafeUtils.hasUnsafe());
    }

    @Test
    void testNormal() {
        final MpscArrayQueue<String> queue = new MpscArrayQueue<>(2);
        assertEquals(0, queue.size());
        assertTrue(queue.isEmpty());

        assertTrue(queue.offer("foo"));
        assertTrue(queue.offer("bar"));
        assertFalse(queue.offer("baz"));

        assertEquals("foo", queue.peek());
        assertEquals("foo", queue.poll());
        assertEquals("bar", queue.peek());
        assertEquals("bar", queue.poll());

        assertNull(queue.peek());
        assertNull(queue.poll());

        assertTrue(queue.offer("foo"));
        assertTrue(queue.offer("bar"));

        final List<String> l = new LinkedList<>();

        assertEquals(2, queue.drain(l::add));
        assertArrayEquals(new String[]{"foo", "bar"}, l.toArray());

        assertTrue(queue.offer("foo"));
        assertTrue(queue.offer("bar"));

        l.clear();
        assertEquals(1, queue.drain(l::add, 1));
        assertArrayEquals(new String[]{"foo"}, l.toArray());

        assertThrows(UnsupportedOperationException.class, queue::iterator);
    }

}
