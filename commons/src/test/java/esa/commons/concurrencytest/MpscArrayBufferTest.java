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

import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MpscArrayBufferTest {

    @Test
    void testOffer() {
        assertThrows(IllegalArgumentException.class, () -> new MpscArrayBuffer<>(1));

        final MpscArrayBuffer<Integer> buffer = new MpscArrayBuffer<>(2);

        assertTrue(buffer.offer(1));
        assertTrue(buffer.offer(2));
        assertFalse(buffer.offer(3));
        final List<Integer> ret = new LinkedList<>();
        assertEquals(2, buffer.drain(ret::add));

        assertEquals(1, ret.get(0));
        assertEquals(2, ret.get(1));

        assertEquals(0, buffer.relaxedOffer(1));
        assertEquals(0, buffer.relaxedOffer(2));
        assertEquals(1, buffer.relaxedOffer(3));
        ret.clear();

        assertEquals(2, buffer.drain(ret::add));

        assertEquals(1, ret.get(0));
        assertEquals(2, ret.get(1));
    }

}
