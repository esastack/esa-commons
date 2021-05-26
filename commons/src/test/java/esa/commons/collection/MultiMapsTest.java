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
package esa.commons.collection;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MultiMapsTest {

    @Test
    void testEmptyMap() {
        final MultiValueMap<String, String> m = MultiMaps.emptyMultiMap();
        assertSame(m, MultiMaps.emptyMultiMap());
        assertTrue(m.isEmpty());
        assertEquals(0, m.size());
        assertTrue(m.entrySet().isEmpty());
        assertTrue(m.keySet().isEmpty());
        assertTrue(m.values().isEmpty());
        assertEquals(0, m.hashCode());
        assertEquals(m, MultiMaps.emptyMultiMap());
        assertNull(m.get(""));
        assertNull(m.getFirst(""));
        assertEquals(Collections.emptyList(), m.getOrDefault("", Collections.emptyList()));

        final List<String> list = Arrays.asList("a", "b");

        assertThrows(UnsupportedOperationException.class, () -> m.add("", ""));
        assertThrows(UnsupportedOperationException.class, () -> m.addFirst("", ""));
        assertThrows(UnsupportedOperationException.class, () -> m.addAll("", list));

        assertThrows(UnsupportedOperationException.class, () -> m.put("a", list));
        assertThrows(UnsupportedOperationException.class, () -> m.putSingle("a", "a"));
        assertThrows(UnsupportedOperationException.class, () -> m.putIfAbsent("a", list));
        assertThrows(UnsupportedOperationException.class,
                () -> m.putAll(Collections.singletonMap("", list)));

        assertThrows(UnsupportedOperationException.class, () -> m.replace("a", list));
        assertThrows(UnsupportedOperationException.class, () -> m.replace("a", list, list));
        assertThrows(UnsupportedOperationException.class, () -> m.remove(""));
        assertThrows(UnsupportedOperationException.class, () -> m.remove("", list));
        assertThrows(UnsupportedOperationException.class, () -> m.compute("", (k, v) -> list));
        assertThrows(UnsupportedOperationException.class, () -> m.computeIfAbsent("", k -> list));
        assertThrows(UnsupportedOperationException.class, () -> m.computeIfPresent("", (k, v) -> list));
        assertThrows(UnsupportedOperationException.class, () -> m.merge("", null, null));

        assertFalse(m.containsKey(""));
        assertFalse(m.containsValue(""));

        assertThrows(NullPointerException.class, () -> m.replaceAll(null));
        assertThrows(NullPointerException.class, () -> m.forEach(null));


    }

}
