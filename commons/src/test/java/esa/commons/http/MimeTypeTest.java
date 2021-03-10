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
package esa.commons.http;


import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MimeTypeTest {

    private static final char[] SEPARATORS = {'(', ')', '<', '>', '@',
            ',', ';', ':', '\\', '\"',
            '/', '[', ']', '?', '=',
            '{', '}', ' ', '\t'};

    @Test
    void testNormal() {
        final String type = "text";
        final String subType = "plain";
        final MimeType mimeType = new MimeType(type, subType);
        assertEquals(type, mimeType.type());
        assertEquals(subType, mimeType.subtype());
        assertNull(mimeType.charset());
        assertTrue(mimeType.parameters().isEmpty());
        assertEquals(type + "/" + subType, mimeType.toString());
        assertEquals("text/plain", mimeType.value());
        assertFalse(mimeType.isWildcardType());
        assertFalse(mimeType.isWildcardSubtype());
        assertTrue(MimeType.of(MimeType.WILDCARD_TYPE).isWildcardType());
        assertTrue(MimeType.of(MimeType.WILDCARD_TYPE).isWildcardSubtype());
        assertTrue(MimeType.of("application", "*+xml").isWildcardSubtype());
        assertEquals("application/json;a=1",
                new MimeType("application", "json", Collections.singletonMap("a", "1")).toString());
    }

    @Test
    void testCheckToken() {
        assertThrows(IllegalArgumentException.class, () -> new MimeType("???", "json"));
        assertDoesNotThrow(() -> new MimeType("???", "json", Collections.emptyMap(), false));
    }

    @Test
    void testTypeOnly() {
        final String type = "text";
        final MimeType mimeType = new MimeType(type);
        assertEquals(type, mimeType.type());
        assertEquals("*", mimeType.subtype());
        assertNull(mimeType.charset());
        assertTrue(mimeType.parameters().isEmpty());
    }

    @Test
    void testCharset() {
        final String type = "text";
        final String subType = "plain";
        final String charset = "utf-8";
        MimeType mimeType = new MimeType(type, subType, charset);
        assertEquals(type, mimeType.type());
        assertEquals(subType, mimeType.subtype());
        assertEquals(StandardCharsets.UTF_8, mimeType.charset());
        assertEquals(1, mimeType.parameters().size());

        mimeType = new MimeType(type, subType, StandardCharsets.UTF_8);
        assertEquals(type, mimeType.type());
        assertEquals(subType, mimeType.subtype());
        assertEquals(StandardCharsets.UTF_8, mimeType.charset());
        assertEquals(1, mimeType.parameters().size());
    }

    @Test
    void testParameter() {
        final String type = "text";
        final String subType = "plain";
        final Map<String, String> params = new LinkedHashMap<>(2);
        params.put("foo", "foo");
        params.put("bar", "bar");
        // case insensitive
        params.put("FOO", "foo");
        params.put("Bar", "bar");
        params.put("quote1", "\"?:\"");
        params.put("quote2", "\'?:\'");
        final MimeType mimeType = new MimeType(type, subType, params);
        assertEquals("foo", mimeType.getParameter("foo"));
        assertEquals("bar", mimeType.getParameter("bar"));
        assertEquals("?:", mimeType.getParameter("quote1"));
        assertEquals("?:", mimeType.getParameter("quote2"));
        assertEquals(4, mimeType.parameters().size());
    }

    @Test
    void testControlMarksInType() {
        final String type = "text";
        final char[] forTest = new char[33];
        for (int i = 0; i < 32; i++) {
            forTest[i] = (char) i;
        }
        forTest[32] = (char) 127;

        for (final char ch : forTest) {
            assertThrows(IllegalArgumentException.class,
                    () -> new MimeType(type + ch));
        }
    }

    @Test
    void testControlMarksInSubType() {
        final String type = "text";
        final String subType = "plain";
        final char[] forTest = new char[33];
        for (int i = 0; i < 32; i++) {
            forTest[i] = (char) i;
        }
        forTest[32] = (char) 127;

        for (final char ch : forTest) {
            assertThrows(IllegalArgumentException.class,
                    () -> new MimeType(type, subType + ch));
        }
    }

    @Test
    void testControlMarksInParameters() {
        final String type = "text";
        final String subType = "plain";
        final char[] forTest = new char[33];
        for (int i = 0; i < 32; i++) {
            forTest[i] = (char) i;
        }
        forTest[32] = (char) 127;

        for (final char ch : forTest) {
            assertThrows(IllegalArgumentException.class,
                    () -> new MimeType(type, subType, Collections.singletonMap("foo", "foo" + ch)));
        }
    }

    @Test
    void testSeparatorMarksInType() {
        final String type = "text";
        for (final char ch : SEPARATORS) {
            assertThrows(IllegalArgumentException.class, () -> new MimeType(type + ch));
        }
    }

    @Test
    void testSeparatorMarksInSubType() {
        final String type = "text";
        final String subType = "plain";

        for (final char ch : SEPARATORS) {
            assertThrows(IllegalArgumentException.class, () -> new MimeType(type, subType + ch));
        }
    }

    @Test
    void testSeparatorMarksInParameters() {
        final String type = "text";
        final String subType = "plain";

        for (final char ch : SEPARATORS) {
            assertThrows(IllegalArgumentException.class,
                    () -> new MimeType(type, subType, Collections.singletonMap("foo", "foo" + ch)));
        }
    }

    @Test
    void testParseMimeType() {
        assertThrows(IllegalArgumentException.class, () -> MimeType.parseMimeType(null));
        assertThrows(IllegalArgumentException.class, () -> MimeType.parseMimeType(""));
        assertThrows(IllegalArgumentException.class, () -> MimeType.parseMimeType("  "));
        assertThrows(IllegalArgumentException.class, () -> MimeType.parseMimeType("abc"));
        assertThrows(IllegalArgumentException.class, () -> MimeType.parseMimeType("abc/"));

        final MimeType wildcard = MimeType.parseMimeType("*");
        assertTrue(wildcard.isWildcardType());
        assertTrue(wildcard.isWildcardSubtype());

        final String type = "text";
        final String subtype = "plain";

        final MimeType parsed = MimeType.parseMimeType(type + "/" + subtype);
        assertEquals(type, parsed.type());
        assertEquals(subtype, parsed.subtype());
        assertTrue(parsed.parameters().isEmpty());
    }

    @Test
    void testParseMimeTypeWithBlank() {
        final String type = " text";
        final String subtype = "plain ";

        final MimeType parsed = MimeType.parseMimeType(type + "/" + subtype);
        assertEquals(type.trim(), parsed.type());
        assertEquals(subtype.trim(), parsed.subtype());
        assertTrue(parsed.parameters().isEmpty());
    }

    @Test
    void testParseMimeTypeWithCharset() {
        final String type = "text";
        final String subtype = "plain";
        final String charset = ";charset=utf-8";

        final MimeType parsed = MimeType.parseMimeType(type + "/" + subtype + charset);
        assertEquals(type.trim(), parsed.type());
        assertEquals(subtype.trim(), parsed.subtype());
        assertFalse(parsed.parameters().isEmpty());
        assertEquals(StandardCharsets.UTF_8, parsed.charset());
    }

    @Test
    void testParseMimeTypeWithParameters() {
        final String type = "text";
        final String subtype = "plain";
        final String parameter = "; a=1 ; b= \"2\"";

        final MimeType parsed = MimeType.parseMimeType(type + "/" + subtype + parameter);
        assertEquals(type.trim(), parsed.type());
        assertEquals(subtype.trim(), parsed.subtype());
        assertFalse(parsed.parameters().isEmpty());
        assertEquals("1", parsed.getParameter("a"));
        assertEquals("2", parsed.getParameter("b"));
    }

    @Test
    void testParseMimeTypes() {

        assertTrue(MimeType.parseMimeTypes("").isEmpty());

        final List<MimeType> mimeTypes =
                MimeType.parseMimeTypes("application/json;a=1;b=\"2\", text/plain;charset=utf-8,application/*+xml, " +
                        "*/*");
        assertEquals(4, mimeTypes.size());
        MimeType type = mimeTypes.get(0);
        assertEquals("application", type.type());
        assertEquals("json", type.subtype());
        assertEquals("1", type.getParameter("a"));
        assertEquals("2", type.getParameter("b"));

        type = mimeTypes.get(1);
        assertEquals("text", type.type());
        assertEquals("plain", type.subtype());
        assertEquals(StandardCharsets.UTF_8, type.charset());

        type = mimeTypes.get(2);
        assertEquals("application", type.type());
        assertEquals("*+xml", type.subtype());

        type = mimeTypes.get(3);
        assertEquals(MimeType.WILDCARD_TYPE, type.type());
        assertEquals(MimeType.WILDCARD_TYPE, type.subtype());
    }

    @Test
    void testIsValidToken() {
        assertTrue(MimeType.isValidToken(null));
        assertTrue(MimeType.isValidToken("abc"));
        assertFalse(MimeType.isValidToken("abc?"));
    }

    @Test
    void testOf() {
        MimeType type = MimeType.of("application");
        assertEquals("application", type.type());
        assertEquals("*", type.subtype());
        assertTrue(type.parameters().isEmpty());
        assertNull(type.charset());

        type = MimeType.of("application", "foo");
        assertEquals("application", type.type());
        assertEquals("foo", type.subtype());
        assertTrue(type.parameters().isEmpty());
        assertNull(type.charset());

        type = MimeType.of("application", "foo", StandardCharsets.UTF_8);
        assertEquals("application", type.type());
        assertEquals("foo", type.subtype());
        assertTrue(type.parameters().containsKey("charset"));
        assertEquals(StandardCharsets.UTF_8, type.charset());

        type = MimeType.of("application", "foo", StandardCharsets.UTF_8.toString());
        assertEquals("application", type.type());
        assertEquals("foo", type.subtype());
        assertTrue(type.parameters().containsKey("charset"));
        assertEquals(StandardCharsets.UTF_8, type.charset());

        type = MimeType.of("application", "foo", Collections.singletonMap("foo", "bar"));
        assertEquals("application", type.type());
        assertEquals("foo", type.subtype());
        assertEquals("bar", type.getParameter("foo"));

        assertThrows(IllegalArgumentException.class,
                () -> MimeType.of("application", "foo",
                        Collections.singletonMap("foo?", "bar"), true));
    }

    @Test
    void testIncludes() {
        assertFalse(MimeType.of(MimeType.WILDCARD_TYPE)
                .includes(null));
        assertTrue(MimeType.of(MimeType.WILDCARD_TYPE)
                .includes(MimeType.of("text", "plain")));
        assertFalse(MimeType.of("text", "plain")
                .includes(MimeType.of(MimeType.WILDCARD_TYPE)));

        assertTrue(MimeType.of("text", "plain")
                .includes(MimeType.of("text", "plain", StandardCharsets.UTF_8)));

        assertTrue(MimeType.of("text", MimeType.WILDCARD_TYPE)
                .includes(MimeType.of("text", "plain")));

        assertFalse(MimeType.of("text", "plain")
                .includes(MimeType.of("text", MimeType.WILDCARD_TYPE)));

        assertTrue(MimeType.of("application", "*+xml")
                .includes(MimeType.of("application", "soap+xml")));
        assertFalse(MimeType.of("application", "soap+xml")
                .includes(MimeType.of("application", "*+xml")));

        assertFalse(MimeType.of("application", "*+html")
                .includes(MimeType.of("application", "soap+xml")));
    }

    @Test
    void testIsCompatibleWith() {
        assertFalse(MimeType.of(MimeType.WILDCARD_TYPE)
                .isCompatibleWith(null));

        assertTrue(MimeType.of(MimeType.WILDCARD_TYPE)
                .isCompatibleWith(MimeType.of("text", "plain")));

        assertTrue(MimeType.of("text", "plain")
                .isCompatibleWith(MimeType.of(MimeType.WILDCARD_TYPE)));

        assertTrue(MimeType.of("text", "plain")
                .isCompatibleWith(MimeType.of("text", "plain", StandardCharsets.UTF_8)));

        assertTrue(MimeType.of("text", MimeType.WILDCARD_TYPE)
                .isCompatibleWith(MimeType.of("text", "plain")));

        assertTrue(MimeType.of("text", "plain")
                .isCompatibleWith(MimeType.of("text", MimeType.WILDCARD_TYPE)));

        assertTrue(MimeType.of("application", "*+xml")
                .isCompatibleWith(MimeType.of("application", "soap+xml")));
        assertTrue(MimeType.of("application", "soap+xml")
                .isCompatibleWith(MimeType.of("application", "*+xml")));

        assertFalse(MimeType.of("application", "*+html")
                .isCompatibleWith(MimeType.of("application", "soap+xml")));

        assertFalse(MimeType.of("application", "json")
                .isCompatibleWith(MimeType.of("text", "plain")));
    }

    @Test
    void testSpecificityComparator() {
        final MimeType.SpecificityComparator<MimeType> c = new MimeType.SpecificityComparator<>();

        assertTrue(c.compare(MimeType.of(MimeType.WILDCARD_TYPE),
                MimeType.of("text", "plain")) > 0);
        assertTrue(c.compare(MimeType.of("text", "plain"),
                MimeType.of(MimeType.WILDCARD_TYPE)) < 0);

        assertTrue(c.compare(MimeType.of(MimeType.WILDCARD_TYPE),
                MimeType.of("text", "*")) > 0);
        assertTrue(c.compare(MimeType.of("text", "*"),
                MimeType.of(MimeType.WILDCARD_TYPE)) < 0);

        assertEquals(0, c.compare(MimeType.of("text", "plain"),
                MimeType.of("application", "json")));
        assertEquals(0, c.compare(MimeType.of("application", "json"),
                MimeType.of("text", "plain")));

        assertTrue(c.compare(MimeType.of("text", "plain"),
                MimeType.of("text", "*")) < 0);
        assertTrue(c.compare(MimeType.of("text", "*"),
                MimeType.of("text", "plain")) > 0);

        assertEquals(0, c.compare(MimeType.of("application", "xml"),
                MimeType.of("application", "json")));

        assertTrue(c.compare(MimeType.of("text", "plain"),
                MimeType.of("text", "plain", Collections.singletonMap("a", "1"))) > 0);

        assertTrue(c.compare(MimeType.of("text", "plain", Collections.singletonMap("a", "1")),
                MimeType.of("text", "plain")) < 0);
    }

    @Test
    void testEquals() {
        final MimeType m = MimeType.of("text", "plain");
        assertEquals(m, m);
        assertEquals(m, MimeType.of("text", "plain"));
        assertEquals(m, MimeType.of("TEXT", "PLAIN"));
        assertEquals(MimeType.of("text", "plain", StandardCharsets.UTF_8),
                MimeType.of("text", "plain", StandardCharsets.UTF_8));

        assertEquals(MimeType.of("text", "plain", Collections.singletonMap("a", "1")),
                MimeType.of("text", "plain", Collections.singletonMap("a", "1")));

        assertNotEquals(m, null);
        assertNotEquals(m, "other type");
        assertNotEquals(m, MimeType.of("text", "json"));
        assertNotEquals(m, MimeType.of("application", "plain"));
        assertNotEquals(MimeType.of("text", "plain", StandardCharsets.UTF_8),
                MimeType.of("text", "plain", StandardCharsets.US_ASCII));
        assertNotEquals(MimeType.of("text", "plain"),
                MimeType.of("text", "plain", StandardCharsets.US_ASCII));
        assertNotEquals(MimeType.of("text", "plain"),
                MimeType.of("text", "plain", Collections.singletonMap("a", "1")));
        assertNotEquals(MimeType.of("text", "plain", Collections.singletonMap("a", "1")),
                MimeType.of("text", "plain", Collections.singletonMap("a", "2")));
        assertNotEquals(MimeType.of("text", "plain", Collections.singletonMap("a", "1")),
                MimeType.of("text", "plain", Collections.singletonMap("b", "2")));

        final Map<String, String> params = new HashMap<>();
        params.put("a", "1");
        params.put("b", "2");
        assertNotEquals(MimeType.of("text", "plain", Collections.singletonMap("a", "1")), params);
    }

}
