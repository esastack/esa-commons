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

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MultiValueMapTest {

    @Test
    void testAll() {
        AbstractMultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        testMultiValueMap(map);
        assertEquals(LinkedHashMap.class, map.underlying.getClass());
        assertEquals(LinkedList.class, map.values().iterator().next().getClass());

        int size = map.size();
        map = new HashMultiValueMap<>(map);
        assertEquals(size, map.size());

        map.clear();
        testMultiValueMap(map);
        assertEquals(HashMap.class, map.underlying.getClass());
        assertEquals(LinkedList.class, map.values().iterator().next().getClass());

        size = map.size();
        map = new LinkedMultiArrayValueMap<>(map);
        assertEquals(size, map.size());

        map.clear();
        testMultiValueMap(map);
        assertEquals(LinkedHashMap.class, map.underlying.getClass());
        assertEquals(ArrayList.class, map.values().iterator().next().getClass());
    }

    void testMultiValueMap(MultiValueMap<String, String> map) {
        map.add("foo", "foo");
        assertEquals(1, map.size());
        assertEquals("foo", map.getFirst("foo"));
        final List<String> values = map.get("foo");
        assertEquals(1, values.size());
        assertEquals("foo", values.get(0));

        map.addFirst("foo", "bar");
        assertEquals(1, map.size());
        assertEquals("bar", map.getFirst("foo"));
        assertEquals(2, map.get("foo").size());
        assertEquals("bar", map.get("foo").get(0));
        assertEquals("foo", map.get("foo").get(1));

        map.putSingle("foo", "baz");
        assertEquals(1, map.size());
        assertEquals("baz", map.getFirst("foo"));
        assertEquals(1, map.get("foo").size());
        assertEquals("baz", map.get("foo").get(0));

        map.addAll("bar", Arrays.asList("a", "b", "c"));
        assertEquals(2, map.size());
        assertEquals("a", map.getFirst("bar"));
        assertEquals(3, map.get("bar").size());
        assertEquals("a", map.get("bar").get(0));
        assertEquals("b", map.get("bar").get(1));
        assertEquals("c", map.get("bar").get(2));

        final Map<String, String> singleValueMap = map.toSingleValueMap();
        assertEquals(2, singleValueMap.size());
        assertEquals("baz", singleValueMap.get("foo"));
        assertEquals("a", singleValueMap.get("bar"));
    }

}
