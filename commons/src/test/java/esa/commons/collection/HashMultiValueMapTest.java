package esa.commons.collection;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Map;

import static esa.commons.collection.AbstractMultiValueMapTest.testMultiValueMap;
import static org.junit.jupiter.api.Assertions.assertEquals;

class HashMultiValueMapTest {

    @Test
    void testToSingleValueMap() {
        testMap(new HashMultiValueMap<>(), true);
        testMap(new HashMultiValueMap<>(4), true);
        testMap(new HashMultiValueMap<>(4, 0.5f), true);

        final HashMultiValueMap<String, String> other = new HashMultiValueMap<>();
        other.put("foo", Arrays.asList("1", "2"));
        other.put("bar", Arrays.asList("2", "3"));
        testMap(new HashMultiValueMap<>(other), false);
    }

    private static void testMap(HashMultiValueMap<String, String> map, boolean put) {
        if (put) {
            map.put("foo", Arrays.asList("1", "2"));
            map.put("bar", Arrays.asList("2", "3"));
        }

        final Map<String, String> singleValueMap = map.toSingleValueMap();
        assertEquals(2, singleValueMap.size());
        assertEquals("1", singleValueMap.get("foo"));
        assertEquals("2", singleValueMap.get("bar"));

        map.clear();

        testMultiValueMap(map);
    }


}
