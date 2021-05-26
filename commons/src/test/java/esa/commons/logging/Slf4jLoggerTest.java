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
package esa.commons.logging;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class Slf4jLoggerTest {

    @Test
    void testEnabled() {
        final org.slf4j.Logger mock = mock(org.slf4j.Logger.class);
        when(mock.getName()).thenReturn("foo");
        final Logger logger = new Slf4cjLogger(mock);
        assertEquals("foo", logger.name());
        assertSame(mock, logger.unwrap());
        when(mock.isTraceEnabled()).thenReturn(true);
        when(mock.isDebugEnabled()).thenReturn(true);
        when(mock.isInfoEnabled()).thenReturn(true);
        when(mock.isWarnEnabled()).thenReturn(true);
        when(mock.isErrorEnabled()).thenReturn(true);
        assertTrue(logger.isTraceEnabled());
        assertTrue(logger.isDebugEnabled());
        assertTrue(logger.isInfoEnabled());
        assertTrue(logger.isWarnEnabled());
        assertTrue(logger.isErrorEnabled());

        when(mock.isTraceEnabled()).thenReturn(false);
        when(mock.isDebugEnabled()).thenReturn(false);
        when(mock.isInfoEnabled()).thenReturn(false);
        when(mock.isWarnEnabled()).thenReturn(false);
        when(mock.isErrorEnabled()).thenReturn(false);
        assertFalse(logger.isTraceEnabled());
        assertFalse(logger.isDebugEnabled());
        assertFalse(logger.isInfoEnabled());
        assertFalse(logger.isWarnEnabled());
        assertFalse(logger.isErrorEnabled());
    }

    @Test
    void testTrace() {
        final org.slf4j.Logger mock = mock(org.slf4j.Logger.class);
        when(mock.getName()).thenReturn("foo");
        final Logger logger = new Slf4cjLogger(mock);
        logger.trace("foo");
        verify(mock).trace("foo");
        logger.trace("foo {}", "bar");
        verify(mock).trace("foo {}", "bar");
        logger.trace("foo {} {}", "bar", "baz");
        verify(mock).trace("foo {} {}", "bar", "baz");
        logger.trace("foo {} {} {}", "bar", "baz", "qux");
        verify(mock).trace("foo {} {} {}", "bar", "baz", "qux");
        final Error e = new Error();
        logger.trace("foo {} {} {}", "bar", "baz", "qux", e);
        verify(mock).trace("foo {} {} {}", "bar", "baz", "qux", e);
        logger.trace("foo", e);
        verify(mock).trace("foo", e);
    }

    @Test
    void testDebug() {
        final org.slf4j.Logger mock = mock(org.slf4j.Logger.class);
        when(mock.getName()).thenReturn("foo");
        final Logger logger = new Slf4cjLogger(mock);
        logger.debug("foo");
        verify(mock).debug("foo");
        logger.debug("foo {}", "bar");
        verify(mock).debug("foo {}", "bar");
        logger.debug("foo {} {}", "bar", "baz");
        verify(mock).debug("foo {} {}", "bar", "baz");
        logger.debug("foo {} {} {}", "bar", "baz", "qux");
        verify(mock).debug("foo {} {} {}", "bar", "baz", "qux");
        final Error e = new Error();
        logger.debug("foo {} {} {}", "bar", "baz", "qux", e);
        verify(mock).debug("foo {} {} {}", "bar", "baz", "qux", e);
        logger.debug("foo", e);
        verify(mock).debug("foo", e);
    }

    @Test
    void testInfo() {
        final org.slf4j.Logger mock = mock(org.slf4j.Logger.class);
        when(mock.getName()).thenReturn("foo");
        final Logger logger = new Slf4cjLogger(mock);
        logger.info("foo");
        verify(mock).info("foo");
        logger.info("foo {}", "bar");
        verify(mock).info("foo {}", "bar");
        logger.info("foo {} {}", "bar", "baz");
        verify(mock).info("foo {} {}", "bar", "baz");
        logger.info("foo {} {} {}", "bar", "baz", "qux");
        verify(mock).info("foo {} {} {}", "bar", "baz", "qux");
        final Error e = new Error();
        logger.info("foo {} {} {}", "bar", "baz", "qux", e);
        verify(mock).info("foo {} {} {}", "bar", "baz", "qux", e);
        logger.info("foo", e);
        verify(mock).info("foo", e);
    }

    @Test
    void testWarn() {
        final org.slf4j.Logger mock = mock(org.slf4j.Logger.class);
        when(mock.getName()).thenReturn("foo");
        final Logger logger = new Slf4cjLogger(mock);
        logger.warn("foo");
        verify(mock).warn("foo");
        logger.warn("foo {}", "bar");
        verify(mock).warn("foo {}", "bar");
        logger.warn("foo {} {}", "bar", "baz");
        verify(mock).warn("foo {} {}", "bar", "baz");
        logger.warn("foo {} {} {}", "bar", "baz", "qux");
        verify(mock).warn("foo {} {} {}", "bar", "baz", "qux");
        final Error e = new Error();
        logger.warn("foo {} {} {}", "bar", "baz", "qux", e);
        verify(mock).warn("foo {} {} {}", "bar", "baz", "qux", e);
        logger.warn("foo", e);
        verify(mock).warn("foo", e);
    }

    @Test
    void testError() {
        final org.slf4j.Logger mock = mock(org.slf4j.Logger.class);
        when(mock.getName()).thenReturn("foo");
        final Logger logger = new Slf4cjLogger(mock);
        logger.error("foo");
        verify(mock).error("foo");
        logger.error("foo {}", "bar");
        verify(mock).error("foo {}", "bar");
        logger.error("foo {} {}", "bar", "baz");
        verify(mock).error("foo {} {}", "bar", "baz");
        logger.error("foo {} {} {}", "bar", "baz", "qux");
        verify(mock).error("foo {} {} {}", "bar", "baz", "qux");
        final Error e = new Error();
        logger.error("foo {} {} {}", "bar", "baz", "qux", e);
        verify(mock).error("foo {} {} {}", "bar", "baz", "qux", e);
        logger.error("foo", e);
        verify(mock).error("foo", e);
    }
}
