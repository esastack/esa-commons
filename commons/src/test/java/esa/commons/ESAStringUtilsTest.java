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

class ESAStringUtilsTest {

    @Test
    void testEmptyAndBlank() {
        assertEquals("", ESAStringUtils.EMPTY_STRING);
        assertTrue(ESAStringUtils.isEmpty(""));
        assertTrue(ESAStringUtils.isEmpty((String) null));
        assertTrue(ESAStringUtils.isEmpty(ESAStringUtils.empty()));
        assertFalse(ESAStringUtils.isNotEmpty(ESAStringUtils.empty()));
        assertFalse(ESAStringUtils.isNotEmpty(""));
        assertFalse(ESAStringUtils.isNotEmpty((String) null));
        assertTrue(ESAStringUtils.isNotEmpty(("foo")));

        final CharSequence seq = "";
        final CharSequence seq1 = "foo";
        assertTrue(ESAStringUtils.isEmpty(seq));
        assertTrue(ESAStringUtils.isEmpty((CharSequence) null));
        assertFalse(ESAStringUtils.isNotEmpty(seq));
        assertFalse(ESAStringUtils.isNotEmpty((CharSequence) null));
        assertTrue(ESAStringUtils.isNotEmpty(seq1));

        assertTrue(ESAStringUtils.isBlank(""));
        assertTrue(ESAStringUtils.isBlank(" "));
        assertTrue(ESAStringUtils.isBlank("   "));
        assertTrue(ESAStringUtils.isBlank(seq));
        assertFalse(ESAStringUtils.isBlank("foo"));
        final CharSequence seq2 = " ";
        final CharSequence seq3 = "  ";
        assertTrue(ESAStringUtils.isBlank(seq2));
        assertTrue(ESAStringUtils.isBlank(seq3));

        assertFalse(ESAStringUtils.isNotBlank(""));
        assertFalse(ESAStringUtils.isNotBlank(" "));
        assertFalse(ESAStringUtils.isNotBlank("   "));
        assertTrue(ESAStringUtils.isNotBlank("foo"));

        assertFalse(ESAStringUtils.isNotBlank(seq));
        assertFalse(ESAStringUtils.isNotBlank(seq2));
        assertFalse(ESAStringUtils.isNotBlank(seq3));
        final CharSequence seq4 = "  foo ";
        assertTrue(ESAStringUtils.isNotBlank(seq4));

        assertEquals("foo", ESAStringUtils.nonNullOrElse(null, "foo"));
        assertNull(ESAStringUtils.nonNullOrElse(null, null));
        assertEquals("", ESAStringUtils.nonNullOrElse("", "foo"));
        assertEquals("bar", ESAStringUtils.nonNullOrElse("bar", "foo"));

        assertEquals("foo", ESAStringUtils.nonEmptyOrElse(null, "foo"));
        assertEquals("foo", ESAStringUtils.nonEmptyOrElse("", "foo"));
        assertNull(ESAStringUtils.nonEmptyOrElse("", null));
        assertEquals("", ESAStringUtils.emptyIfNull(null));
        assertEquals("foo", ESAStringUtils.emptyIfNull("foo"));
    }

    @Test
    void testTrim() {
        assertEquals("foo", ESAStringUtils.trim(" foo "));
        assertEquals("", ESAStringUtils.trim(""));
        assertEquals("", ESAStringUtils.trim("  "));
        assertNull(ESAStringUtils.trim(null));
    }

    @Test
    void testQuote() {
        assertTrue(ESAStringUtils.isQuoted("\"foo\""));
        assertTrue(ESAStringUtils.isQuoted("\'foo\'"));
        assertFalse(ESAStringUtils.isQuoted("\'foo"));
        assertFalse(ESAStringUtils.isQuoted("foo\'"));
        assertFalse(ESAStringUtils.isQuoted("\"foo"));
        assertFalse(ESAStringUtils.isQuoted("foo\""));
        assertFalse(ESAStringUtils.isQuoted("foo"));

        assertEquals("\'foo\'", ESAStringUtils.quote("foo"));
        assertEquals("foo", ESAStringUtils.unquote("foo"));
        assertEquals("foo\"", ESAStringUtils.unquote("foo\""));
        assertEquals("foo\'", ESAStringUtils.unquote("foo\'"));
        assertEquals("\'foo", ESAStringUtils.unquote("\'foo"));
        assertEquals("\"foo", ESAStringUtils.unquote("\"foo"));
    }

    @Test
    void testConcat() {
        assertNull(ESAStringUtils.concat(null));
        assertEquals("", ESAStringUtils.concat(""));
        assertEquals("foo", ESAStringUtils.concat("foo"));
        assertEquals("foobar", ESAStringUtils.concat("foo", "bar"));
        assertEquals("foobarbaz", ESAStringUtils.concat("foo", "bar", "baz"));
        assertEquals("foo,bar,baz", ESAStringUtils.concat("foo", ",", "bar", ",", "baz"));
    }

}
