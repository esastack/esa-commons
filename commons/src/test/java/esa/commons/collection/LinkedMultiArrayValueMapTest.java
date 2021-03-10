package esa.commons.collection;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static esa.commons.collection.AbstractMultiValueMapTest.testMultiValueMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LinkedMultiArrayValueMapTest {

    @Test
    void testToSingleValueMap() {
        testMultiValueMap(new LinkedMultiArrayValueMap<>());
        testMultiValueMap(new LinkedMultiArrayValueMap<>(4));
        testMultiValueMap(new LinkedMultiArrayValueMap<>(4, 0.5f));
        testMultiValueMap(new LinkedMultiArrayValueMap<>(4, 4));
        testMultiValueMap(new LinkedMultiArrayValueMap<>(4, 0.5f, 4));

        final LinkedMultiValueMap<String, String> other = new LinkedMultiValueMap<>();
        other.put("foo", Arrays.asList("1", "2"));
        other.put("bar", Arrays.asList("2", "3"));

        final LinkedMultiArrayValueMap<String, String> map = new LinkedMultiArrayValueMap<>(other);
        assertEquals("1", map.getFirst("foo"));
        assertEquals("2", map.getFirst("bar"));

        map.clear();
        testMultiValueMap(map);

        assertThrows(IllegalArgumentException.class,
                () -> new LinkedMultiArrayValueMap<>(4, -1));
    }

}
