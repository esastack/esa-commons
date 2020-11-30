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
package esa.commons.reflect;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BeanUtilsTest {

    @Test
    void testCopyProperties() {
        final Map<String, Object> values = new HashMap<>();
        BeanUtils.copyProperties(null, values);
        assertTrue(values.isEmpty());
        BeanUtils.copyProperties(new Subject(), values);
        assertEquals(2, values.size());
        assertEquals("1", values.get("foo"));
        assertEquals(2, values.get("bar"));
    }

    @Test
    void testGetAndSetFieldValue() {
        assertThrows(NullPointerException.class, () -> BeanUtils.getFieldValue(null, "foo"));
        final Subject subject = new Subject();

        assertThrows(IllegalArgumentException.class, () -> BeanUtils.getFieldValue(subject, "absent"));
        assertEquals("1", BeanUtils.getFieldValue(subject, "foo"));
        assertEquals(2, BeanUtils.getFieldValue(subject, "bar"));

        assertThrows(IllegalArgumentException.class,
                () -> BeanUtils.setFieldValue(subject, "absent", 1));
        assertThrows(IllegalArgumentException.class,
                () -> BeanUtils.setFieldValue(subject, "foo", 1));
        assertThrows(IllegalArgumentException.class,
                () -> BeanUtils.setFieldValue(subject, "bar", "mismatch"));

        BeanUtils.setFieldValue(subject, "foo", "2");
        assertEquals("2", BeanUtils.getFieldValue(subject, "foo"));
        BeanUtils.setFieldValue(subject, "bar", 3);
        assertEquals(3, BeanUtils.getFieldValue(subject, "bar"));
    }

    private static class Subject {

        private static String a = "3";

        private String foo = "1";
        private int bar = 2;

    }
}
