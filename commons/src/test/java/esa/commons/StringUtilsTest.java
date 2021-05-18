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
package esa.commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StringUtilsTest {

    @Test
    void testEmptyAndBlank() {
        assertEquals("", StringUtils.EMPTY_STRING);
        assertTrue(StringUtils.isEmpty(""));
        assertTrue(StringUtils.isEmpty((String) null));
        assertTrue(StringUtils.isEmpty(StringUtils.empty()));
        assertFalse(StringUtils.isNotEmpty(StringUtils.empty()));
        assertFalse(StringUtils.isNotEmpty(""));
        assertFalse(StringUtils.isNotEmpty((String) null));
        assertTrue(StringUtils.isNotEmpty(("foo")));

        final CharSequence seq = "";
        final CharSequence seq1 = "foo";
        assertTrue(StringUtils.isEmpty(seq));
        assertTrue(StringUtils.isEmpty((CharSequence) null));
        assertFalse(StringUtils.isNotEmpty(seq));
        assertFalse(StringUtils.isNotEmpty((CharSequence) null));
        assertTrue(StringUtils.isNotEmpty(seq1));

        assertTrue(StringUtils.isBlank(""));
        assertTrue(StringUtils.isBlank(" "));
        assertTrue(StringUtils.isBlank("   "));
        assertTrue(StringUtils.isBlank(seq));
        assertFalse(StringUtils.isBlank("foo"));
        final CharSequence seq2 = " ";
        final CharSequence seq3 = "  ";
        assertTrue(StringUtils.isBlank(seq2));
        assertTrue(StringUtils.isBlank(seq3));

        assertFalse(StringUtils.isNotBlank(""));
        assertFalse(StringUtils.isNotBlank(" "));
        assertFalse(StringUtils.isNotBlank("   "));
        assertTrue(StringUtils.isNotBlank("foo"));

        assertFalse(StringUtils.isNotBlank(seq));
        assertFalse(StringUtils.isNotBlank(seq2));
        assertFalse(StringUtils.isNotBlank(seq3));
        final CharSequence seq4 = "  foo ";
        assertTrue(StringUtils.isNotBlank(seq4));

        assertEquals("foo", StringUtils.nonNullOrElse(null, "foo"));
        assertNull(StringUtils.nonNullOrElse(null, null));
        assertEquals("", StringUtils.nonNullOrElse("", "foo"));
        assertEquals("bar", StringUtils.nonNullOrElse("bar", "foo"));

        assertEquals("foo", StringUtils.nonEmptyOrElse(null, "foo"));
        assertEquals("foo", StringUtils.nonEmptyOrElse("", "foo"));
        assertNull(StringUtils.nonEmptyOrElse("", null));
        assertEquals("", StringUtils.emptyIfNull(null));
        assertEquals("foo", StringUtils.emptyIfNull("foo"));
    }

    @Test
    void testTrim() {
        assertEquals("foo", StringUtils.trim(" foo "));
        assertEquals("", StringUtils.trim(""));
        assertEquals("", StringUtils.trim("  "));
        assertNull(StringUtils.trim(null));
    }

    @Test
    void testQuote() {
        assertTrue(StringUtils.isQuoted("\"foo\""));
        assertTrue(StringUtils.isQuoted("\'foo\'"));
        assertFalse(StringUtils.isQuoted("\'foo"));
        assertFalse(StringUtils.isQuoted("foo\'"));
        assertFalse(StringUtils.isQuoted("\"foo"));
        assertFalse(StringUtils.isQuoted("foo\""));
        assertFalse(StringUtils.isQuoted("foo"));

        assertEquals("\'foo\'", StringUtils.quote("foo"));
        assertEquals("foo", StringUtils.unquote("foo"));
        assertEquals("foo\"", StringUtils.unquote("foo\""));
        assertEquals("foo\'", StringUtils.unquote("foo\'"));
        assertEquals("\'foo", StringUtils.unquote("\'foo"));
        assertEquals("\"foo", StringUtils.unquote("\"foo"));
    }

    @Test
    void testConcat() {
        assertNull(StringUtils.concat(null));
        assertEquals("", StringUtils.concat(""));
        assertEquals("foo", StringUtils.concat("foo"));
        assertEquals("foobar", StringUtils.concat("foo", "bar"));
        assertEquals("foobar", StringUtils.concat("foo", null, "bar"));
        assertEquals("foobar", StringUtils.concat(null, "foo", null, "bar"));
        assertEquals("foobarbaz", StringUtils.concat("foo", "bar", "baz"));
        assertEquals("foo,bar,baz", StringUtils.concat("foo", ",", "bar", ",", "baz"));
        assertEquals("foo,bar,baz", StringUtils.concat("foo", ",", null,
                "bar", ",", null, "baz", null));
    }

}
