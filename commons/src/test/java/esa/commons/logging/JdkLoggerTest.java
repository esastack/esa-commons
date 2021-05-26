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

import java.util.logging.Level;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JdkLoggerTest {

    @Test
    void testNameAndUnwrap() {
        final java.util.logging.Logger mock = mock(java.util.logging.Logger.class);
        when(mock.getName()).thenReturn("foo");
        final Logger logger = new JdkLogger(mock);
        assertEquals("foo", logger.name());
        assertSame(mock, logger.unwrap());
    }

    @Test
    void testTrace() {
        final java.util.logging.Logger mock = mock(java.util.logging.Logger.class);
        when(mock.getName()).thenReturn("foo");
        final Logger logger = new JdkLogger(mock);
        when(mock.isLoggable(Level.FINEST)).thenReturn(true);
        assertTrue(logger.isTraceEnabled());
        verifyTrace(mock, logger, true);

        reset(mock);

        when(mock.getName()).thenReturn("foo");
        when(mock.isLoggable(Level.FINEST)).thenReturn(false);
        assertFalse(logger.isTraceEnabled());
        verifyTrace(mock, logger, false);
    }

    @Test
    void testDebug() {
        final java.util.logging.Logger mock = mock(java.util.logging.Logger.class);
        when(mock.getName()).thenReturn("foo");
        final Logger logger = new JdkLogger(mock);
        when(mock.isLoggable(Level.FINE)).thenReturn(true);
        assertTrue(logger.isDebugEnabled());
        verifyDebug(mock, logger, true);

        reset(mock);

        when(mock.getName()).thenReturn("foo");
        when(mock.isLoggable(Level.FINE)).thenReturn(false);
        assertFalse(logger.isDebugEnabled());
        verifyDebug(mock, logger, false);
    }

    @Test
    void testInfo() {
        final java.util.logging.Logger mock = mock(java.util.logging.Logger.class);
        when(mock.getName()).thenReturn("foo");
        final Logger logger = new JdkLogger(mock);
        when(mock.isLoggable(Level.INFO)).thenReturn(true);
        assertTrue(logger.isInfoEnabled());
        verifyInfo(mock, logger, true);

        reset(mock);

        when(mock.getName()).thenReturn("foo");
        when(mock.isLoggable(Level.INFO)).thenReturn(false);
        assertFalse(logger.isInfoEnabled());
        verifyInfo(mock, logger, false);
    }

    @Test
    void testWarn() {
        final java.util.logging.Logger mock = mock(java.util.logging.Logger.class);
        when(mock.getName()).thenReturn("foo");
        final Logger logger = new JdkLogger(mock);
        when(mock.isLoggable(Level.WARNING)).thenReturn(true);
        assertTrue(logger.isWarnEnabled());
        verifyWarning(mock, logger, true);

        reset(mock);

        when(mock.getName()).thenReturn("foo");
        when(mock.isLoggable(Level.WARNING)).thenReturn(false);
        assertFalse(logger.isWarnEnabled());
        verifyWarning(mock, logger, false);
    }

    @Test
    void testError() {
        final java.util.logging.Logger mock = mock(java.util.logging.Logger.class);
        when(mock.getName()).thenReturn("foo");
        final Logger logger = new JdkLogger(mock);
        when(mock.isLoggable(Level.SEVERE)).thenReturn(true);
        assertTrue(logger.isErrorEnabled());
        verifyError(mock, logger, true);

        reset(mock);

        when(mock.getName()).thenReturn("foo");
        when(mock.isLoggable(Level.SEVERE)).thenReturn(false);
        assertFalse(logger.isErrorEnabled());
        verifyError(mock, logger, false);
    }

    private void verifyTrace(java.util.logging.Logger mock, Logger logger, boolean called) {
        logger.trace("foo");
        verifyLog(mock, Level.FINEST, "foo", null, called);
        logger.trace("foo {}", "bar");
        verifyLog(mock, Level.FINEST, "foo bar", null, called);

        logger.trace("foo {} {}", "bar", "baz");
        verifyLog(mock, Level.FINEST, "foo bar baz", null, called);

        logger.trace("foo {} {} {}", "bar", "baz", "qux");
        verifyLog(mock, Level.FINEST, "foo bar baz qux", null, called);
        final Error e = new Error();
        logger.trace("foo {} {} {}", "bar", "baz", "qux", e);
        verifyLog(mock, Level.FINEST, "foo bar baz qux", e, called);
        logger.trace("foo", e);
        verifyLog(mock, Level.FINEST, "foo", e, called);
        when(mock.isLoggable(Level.FINEST)).thenReturn(false);
    }

    private void verifyDebug(java.util.logging.Logger mock, Logger logger, boolean called) {
        logger.debug("foo");
        verifyLog(mock, Level.FINE, "foo", null, called);
        logger.debug("foo {}", "bar");
        verifyLog(mock, Level.FINE, "foo bar", null, called);

        logger.debug("foo {} {}", "bar", "baz");
        verifyLog(mock, Level.FINE, "foo bar baz", null, called);

        logger.debug("foo {} {} {}", "bar", "baz", "qux");
        verifyLog(mock, Level.FINE, "foo bar baz qux", null, called);
        final Error e = new Error();
        logger.debug("foo {} {} {}", "bar", "baz", "qux", e);
        verifyLog(mock, Level.FINE, "foo bar baz qux", e, called);
        logger.debug("foo", e);
        verifyLog(mock, Level.FINE, "foo", e, called);
    }

    private void verifyInfo(java.util.logging.Logger mock, Logger logger, boolean called) {
        logger.info("foo");
        verifyLog(mock, Level.INFO, "foo", null, called);
        logger.info("foo {}", "bar");
        verifyLog(mock, Level.INFO, "foo bar", null, called);

        logger.info("foo {} {}", "bar", "baz");
        verifyLog(mock, Level.INFO, "foo bar baz", null, called);

        logger.info("foo {} {} {}", "bar", "baz", "qux");
        verifyLog(mock, Level.INFO, "foo bar baz qux", null, called);
        final Error e = new Error();
        logger.info("foo {} {} {}", "bar", "baz", "qux", e);
        verifyLog(mock, Level.INFO, "foo bar baz qux", e, called);
        logger.info("foo", e);
        verifyLog(mock, Level.INFO, "foo", e, called);
    }

    private void verifyWarning(java.util.logging.Logger mock, Logger logger, boolean called) {
        logger.warn("foo");
        verifyLog(mock, Level.WARNING, "foo", null, called);
        logger.warn("foo {}", "bar");
        verifyLog(mock, Level.WARNING, "foo bar", null, called);

        logger.warn("foo {} {}", "bar", "baz");
        verifyLog(mock, Level.WARNING, "foo bar baz", null, called);

        logger.warn("foo {} {} {}", "bar", "baz", "qux");
        verifyLog(mock, Level.WARNING, "foo bar baz qux", null, called);
        final Error e = new Error();
        logger.warn("foo {} {} {}", "bar", "baz", "qux", e);
        verifyLog(mock, Level.WARNING, "foo bar baz qux", e, called);
        logger.warn("foo", e);
        verifyLog(mock, Level.WARNING, "foo", e, called);
    }

    private void verifyError(java.util.logging.Logger mock, Logger logger, boolean called) {
        logger.error("foo");
        verifyLog(mock, Level.SEVERE, "foo", null, called);
        logger.error("foo {}", "bar");
        verifyLog(mock, Level.SEVERE, "foo bar", null, called);

        logger.error("foo {} {}", "bar", "baz");
        verifyLog(mock, Level.SEVERE, "foo bar baz", null, called);

        logger.error("foo {} {} {}", "bar", "baz", "qux");
        verifyLog(mock, Level.SEVERE, "foo bar baz qux", null, called);
        final Error e = new Error();
        logger.error("foo {} {} {}", "bar", "baz", "qux", e);
        verifyLog(mock, Level.SEVERE, "foo bar baz qux", e, called);
        logger.error("foo", e);
        verifyLog(mock, Level.SEVERE, "foo", e, called);
    }

    private void verifyLog(java.util.logging.Logger mock, Level level, String msg, Throwable t, boolean called) {
        verify(mock, called ? times(1) : never())
                .log(argThat(logRecord -> logRecord.getLoggerName().equals("foo")
                        && logRecord.getLevel().equals(level)
                        && logRecord.getMessage().equals(msg)
                        && logRecord.getThrown() == t));
    }

}
