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
package esa.commons.loadbalance;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ConsistentHasherTest {

    @Test
    void testConstructor() {
        assertThrows(NullPointerException.class, () -> ConsistentHasher.newHasher(null));
        assertThrows(IllegalArgumentException.class, () -> ConsistentHasher.newHasher(-1, n -> ""));
    }

    @Test
    void testHash() {
        final ConsistentHasher<String> hasher = ConsistentHasher.newStringHasher(1);
        assertNull(hasher.get("a"));

        hasher.addNode("a");
        assertEquals("a", hasher.get("a"));
        assertEquals("a", hasher.get("b"));
        hasher.addNodeIfAbsent("a");
        assertEquals("a", hasher.get("a"));
        assertEquals("a", hasher.get("b"));

        hasher.remove("a");
        assertNull(hasher.get("a"));

        hasher.addNodes(Arrays.asList("a", "b", "c"));

        final Random random = new Random();
        final int size = 10 + random.nextInt(64);
        final List<String> randomKey = new ArrayList<>(size);

        for (int i = 0; i < size; ) {
            String s = String.valueOf(random.nextInt(100)) + random.nextInt(1000);
            if (!randomKey.contains(s)) {
                randomKey.add(s);
                i++;
            }
        }

        final String[] firstHashResult = new String[size];

        for (int i = 0; i < size; i++) {
            firstHashResult[i] = hasher.get(randomKey.get(i));
        }

        hasher.remove("a");
        for (int i = 0; i < size; i++) {
            if (!firstHashResult[i].equals("a") && hasher.get(randomKey.get(i)).equals("a")) {
                fail();
            }
        }
        hasher.addNode("a");
        hasher.addNode("d");

        Set<String> changed = new HashSet<>();
        for (int i = 0; i < size; i++) {
            String node;
            if (!firstHashResult[i].equals((node = hasher.get(randomKey.get(i))))) {
                changed.add(node);
            }
        }
        assertTrue(changed.size() <= 2);
    }

}
