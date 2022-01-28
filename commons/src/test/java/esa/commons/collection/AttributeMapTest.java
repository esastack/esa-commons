/*
 * Copyright 2021 OPPO ESA Stack Project
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

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AttributeMapTest {

    @Test
    void testCrud() {
        final AttributeMap attrs = new AttributeMap();
        final AttributeKey<String> key = AttributeKey.stringKey("foo");
        assertEquals(0, attrs.size());
        assertTrue(attrs.isEmpty());
        assertFalse(attrs.hasAttr(key));
        assertSame(attrs, attrs.asMap());

        final Attribute<String> attr = attrs.attr(key);
        assertNotNull(attr);
        assertEquals(1, attrs.size());
        assertFalse(attrs.isEmpty());
        assertTrue(attrs.hasAttr(key));

        final Map<AttributeKey<?>, Attribute<?>> values = new LinkedHashMap<>();
        attrs.forEach(values::put);
        assertEquals(1, values.size());
        assertSame(key, values.entrySet().iterator().next().getKey());
        assertSame(attr, values.entrySet().iterator().next().getValue());


    }

    @Test
    void testGetAndSet() {
        final AttributeMap attrs = new AttributeMap();
        final Attribute<String> attr = attrs.attr(AttributeKey.stringKey("foo"));
        assertNull(attr.get());
        assertEquals("foo", attr.key().name());
        assertEquals("def", attr.getOrDefault("def"));

        attrs.attr(AttributeKey.stringKey("foo")).set("test");
        assertEquals("test", attr.get());
        assertEquals("test", attr.getOrDefault("def"));

        attrs.attr(AttributeKey.stringKey("foo")).lazySet("test1");
        assertEquals("test1", attr.get());

        assertFalse(attrs.attr(AttributeKey.stringKey("foo")).compareAndSet("absent", "test2"));
        assertEquals("test1", attr.get());

        assertTrue(attrs.attr(AttributeKey.stringKey("foo")).compareAndSet("test1", "test2"));
        assertEquals("test2", attr.get());

        assertEquals("test2",  attrs.attr(AttributeKey.stringKey("foo")).getAndSet("test3"));
        assertEquals("test3", attr.get());

        assertEquals("test3",  attrs.attr(AttributeKey.stringKey("foo")).getAndUpdate(v -> v + "!"));
        assertEquals("test3!", attr.get());

        assertEquals("test3!!",  attrs.attr(AttributeKey.stringKey("foo")).updateAndGet(v -> v + "!"));
        assertEquals("test3!!", attr.get());

        assertEquals("test3!!",  attrs.attr(AttributeKey.stringKey("foo"))
                .getAndAccumulate("x", (prev, x) -> prev + x));
        assertEquals("test3!!x", attr.get());

        assertEquals("test3!!xx",  attrs.attr(AttributeKey.stringKey("foo"))
                .accumulateAndGet("x", (prev, x) -> prev + x));
        assertEquals("test3!!xx", attr.get());
    }

    @Test
    void testRemove() {
        final AttributeMap attrs = new AttributeMap();
        final Attribute<String> attr = attrs.attr(AttributeKey.stringKey("foo"));
        assertNull(attr.get());
        attr.set("test");

        assertTrue(attrs.hasAttr(AttributeKey.stringKey("foo")));

        attr.remove();

        assertFalse(attrs.hasAttr(AttributeKey.stringKey("foo")));
        assertDoesNotThrow(attr::remove);

        final Attribute<String> attr1 = attrs.attr(AttributeKey.stringKey("bar"));
        attr1.set("test");
        assertEquals("test", attr1.getAndRemove());
        assertFalse(attrs.hasAttr(AttributeKey.stringKey("bar")));
        assertNull(attr1.get());

        assertNull(attr1.getAndRemove());
    }

    @Test
    void testInitCapacity() {
        final AttributeMap attrs = new AttributeMap(4);
        attrs.attr(AttributeKey.stringKey("1"));
        attrs.attr(AttributeKey.stringKey("2"));
        attrs.attr(AttributeKey.stringKey("3"));
        attrs.attr(AttributeKey.stringKey("4"));
        assertDoesNotThrow(() -> attrs.attr(AttributeKey.stringKey("5")));
        assertDoesNotThrow(() -> attrs.attr(AttributeKey.stringKey("6")));

        assertEquals(6, attrs.size());
    }

    @Test
    void testInitCapacityAndLoadFactor() {
        final AttributeMap attrs = new AttributeMap(4, 0.5f);
        attrs.attr(AttributeKey.stringKey("1"));
        attrs.attr(AttributeKey.stringKey("2"));
        assertDoesNotThrow(() -> attrs.attr(AttributeKey.stringKey("3")));
        assertDoesNotThrow(() -> attrs.attr(AttributeKey.stringKey("4")));
        assertDoesNotThrow(() -> attrs.attr(AttributeKey.stringKey("5")));
        assertDoesNotThrow(() -> attrs.attr(AttributeKey.stringKey("6")));

        assertEquals(6, attrs.size());
    }

    @Test
    void testCreateFromAnotherAttributes() {
        final AttributeMap attrs = new AttributeMap();
        attrs.attr(AttributeKey.stringKey("foo")).set("1");
        attrs.attr(AttributeKey.stringKey("bar")).set("2");

        final AttributeMap attrs1 = new AttributeMap(attrs);
        assertEquals(2, attrs1.size());
        assertTrue(attrs1.hasAttr(AttributeKey.stringKey("foo")));
        assertTrue(attrs1.hasAttr(AttributeKey.stringKey("bar")));
        assertEquals("1", attrs1.attr(AttributeKey.stringKey("foo")).get());
        assertEquals("2", attrs1.attr(AttributeKey.stringKey("bar")).get());
    }

}
