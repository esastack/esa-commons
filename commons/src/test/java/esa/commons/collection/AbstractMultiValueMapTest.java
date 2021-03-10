package esa.commons.collection;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AbstractMultiValueMapTest {

    @Test
    void testDelegate() {
        assertThrows(NullPointerException.class, () -> new AbstractMultiValueMap<String, String>(null) {
        });
        final Map<String, List<String>> underlying = mock(Map.class);
        AbstractMultiValueMap<String, String> map = new AbstractMultiValueMap<String, String>(underlying) {
        };

        final List<String> list = new LinkedList<>();
        when(underlying.computeIfAbsent(eq("foo"), any())).thenReturn(list);

        assertSame(list, map.getList("foo"));
        verify(underlying).computeIfAbsent(eq("foo"), any());

        map.add("foo", "1");
        assertTrue(list.contains("1"));

        map.addAll("foo", Arrays.asList("2", "3"));
        assertTrue(list.contains("2"));
        assertTrue(list.contains("3"));

        map.addFirst("foo", "0");
        assertEquals("0", list.get(0));

        assertEquals("0", map.getFirst("foo"));
        assertNull(map.getFirst("bar"));

        map.putSingle("foo", "1");
        assertTrue(list.contains("1"));
        assertEquals(1, list.size());

        final List<List<String>> values = Collections.singletonList(list);
        when(underlying.values()).thenReturn(values);
        assertSame(values, map.values());

        when(underlying.size()).thenReturn(1);
        assertEquals(1, map.size());

        map.remove("bar");
        verify(underlying).remove(eq("bar"));

        map.putAll(map);
        verify(underlying).putAll(same(map));

        map.put("baz", list);
        verify(underlying).put(eq("baz"), same(list));

        final Set<String> keySet = Collections.emptySet();
        when(underlying.keySet()).thenReturn(keySet);
        assertSame(keySet, map.keySet());

        when(underlying.isEmpty()).thenReturn(true);
        assertTrue(map.isEmpty());

        when(underlying.get(eq("foo"))).thenReturn(list);
        assertSame(list, map.get("foo"));

        final Set<Map.Entry<String, List<String>>> entrySet = Collections.emptySet();
        when(underlying.entrySet()).thenReturn(entrySet);
        assertSame(entrySet, map.entrySet());

        when(underlying.containsKey("baz")).thenReturn(true);
        assertTrue(map.containsKey("baz"));

        when(underlying.containsValue("baz")).thenReturn(true);
        assertTrue(map.containsValue("baz"));

        reset(underlying);

        map.clear();
        verify(underlying).clear();
    }

    @Test
    void testDelegate1() {
        final Map<String, List<String>> underlying = new LinkedHashMap<>();
        AbstractMultiValueMap<String, String> map = new AbstractMultiValueMap<String, String>(underlying) {
        };
        testMultiValueMap(map);
    }

    @Test
    void testToSingleValueMap() {
        final Map<String, List<String>> underlying = new LinkedHashMap<>();
        AbstractMultiValueMap<String, String> map = new AbstractMultiValueMap<String, String>(underlying) {
        };

        underlying.put("foo", Arrays.asList("1", "2"));
        underlying.put("bar", Arrays.asList("2", "3"));

        final Map<String, String> singleValueMap = map.toSingleValueMap();
        assertEquals(2, singleValueMap.size());
        assertEquals("1", singleValueMap.get("foo"));
        assertEquals("2", singleValueMap.get("bar"));
    }

    @Test
    void testEqualsAndToStringAndHashCode() {
        final Map<String, List<String>> underlying = new LinkedHashMap<>();
        AbstractMultiValueMap<String, String> map = new AbstractMultiValueMap<String, String>(underlying) {
        };

        assertEquals(map, map);
        assertEquals(underlying, map);

        assertEquals(underlying.toString(), map.toString());
        assertEquals(underlying.hashCode(), map.hashCode());
    }

    static void testMultiValueMap(MultiValueMap<String, String> map) {
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
