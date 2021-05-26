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
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class InternalLoggerImplTest {

    @Test
    void testNameAndUnwrap() {
        final LogHandler handler = mock(LogHandler.class);
        InternalLoggerImpl logger = new InternalLoggerImpl("foo", handler);
        assertEquals("foo", logger.name());
        assertSame(logger, logger.unwrap());
    }

    @Test
    void testLevel() {
        final LogHandler mock = mock(LogHandler.class);
        InternalLoggerImpl logger = new InternalLoggerImpl("name", mock);
        assertTrue(logger.isInfoEnabled());
        assertTrue(logger.isWarnEnabled());
        assertTrue(logger.isErrorEnabled());
        assertTrue(logger.isLogEnabled(Level.INFO));
        assertTrue(logger.isLogEnabled(Level.WARN));
        assertTrue(logger.isLogEnabled(Level.ERROR));

        assertFalse(logger.isDebugEnabled());
        assertFalse(logger.isTraceEnabled());
        assertFalse(logger.isLogEnabled(Level.DEBUG));
        assertFalse(logger.isLogEnabled(Level.TRACE));

        logger.setLevel(Level.DEBUG);
        assertTrue(logger.isInfoEnabled());
        assertTrue(logger.isWarnEnabled());
        assertTrue(logger.isErrorEnabled());
        assertTrue(logger.isDebugEnabled());
        assertTrue(logger.isLogEnabled(Level.INFO));
        assertTrue(logger.isLogEnabled(Level.WARN));
        assertTrue(logger.isLogEnabled(Level.ERROR));
        assertTrue(logger.isLogEnabled(Level.DEBUG));

        assertFalse(logger.isTraceEnabled());
        assertFalse(logger.isLogEnabled(Level.TRACE));
    }

    @Test
    void testDebug() {
        final LogHandler mock = mock(LogHandler.class);
        InternalLoggerImpl logger = new InternalLoggerImpl("foo", mock);
        logger.setLevel(Level.DEBUG);
        assertTrue(logger.isDebugEnabled());

        verifyDebug(mock, logger, true);

        reset(mock);

        logger.setLevel(Level.OFF);
        assertFalse(logger.isDebugEnabled());
        verifyDebug(mock, logger, false);
    }

    @Test
    void testInfo() {
        final LogHandler mock = mock(LogHandler.class);
        InternalLoggerImpl logger = new InternalLoggerImpl("foo", mock);
        logger.setLevel(Level.INFO);
        assertTrue(logger.isInfoEnabled());

        verifyInfo(mock, logger, true);

        reset(mock);

        logger.setLevel(Level.OFF);
        assertFalse(logger.isInfoEnabled());
        verifyInfo(mock, logger, false);
    }

    @Test
    void testWarn() {
        final LogHandler mock = mock(LogHandler.class);
        InternalLoggerImpl logger = new InternalLoggerImpl("foo", mock);
        logger.setLevel(Level.WARN);
        assertTrue(logger.isWarnEnabled());

        verifyWarning(mock, logger, true);

        reset(mock);

        logger.setLevel(Level.OFF);
        assertFalse(logger.isWarnEnabled());
        verifyWarning(mock, logger, false);
    }

    @Test
    void testError() {
        final LogHandler mock = mock(LogHandler.class);
        InternalLoggerImpl logger = new InternalLoggerImpl("foo", mock);
        logger.setLevel(Level.ERROR);
        assertTrue(logger.isErrorEnabled());

        verifyError(mock, logger, true);

        reset(mock);

        logger.setLevel(Level.OFF);
        assertFalse(logger.isErrorEnabled());
        verifyError(mock, logger, false);
    }

    private void verifyTrace(LogHandler mock, Logger logger, boolean called) {
        logger.trace("foo");
        verifyLog(mock, Level.TRACE, "foo", null, called);
        logger.trace("foo {}", "bar");
        verifyLog(mock, Level.TRACE, "foo bar", null, called);

        logger.trace("foo {} {}", "bar", "baz");
        verifyLog(mock, Level.TRACE, "foo bar baz", null, called);

        logger.trace("foo {} {} {}", "bar", "baz", "qux");
        verifyLog(mock, Level.TRACE, "foo bar baz qux", null, called);
        final Error e = new Error();
        logger.trace("foo {} {} {}", "bar", "baz", "qux", e);
        verifyLog(mock, Level.TRACE, "foo bar baz qux", e, called);
        logger.trace("foo", e);
        verifyLog(mock, Level.TRACE, "foo", e, called);
    }

    private void verifyDebug(LogHandler mock, Logger logger, boolean called) {
        logger.debug("foo");
        verifyLog(mock, Level.DEBUG, "foo", null, called);
        logger.debug("foo {}", "bar");
        verifyLog(mock, Level.DEBUG, "foo bar", null, called);

        logger.debug("foo {} {}", "bar", "baz");
        verifyLog(mock, Level.DEBUG, "foo bar baz", null, called);

        logger.debug("foo {} {} {}", "bar", "baz", "qux");
        verifyLog(mock, Level.DEBUG, "foo bar baz qux", null, called);
        final Error e = new Error();
        logger.debug("foo {} {} {}", "bar", "baz", "qux", e);
        verifyLog(mock, Level.DEBUG, "foo bar baz qux", e, called);
        logger.debug("foo", e);
        verifyLog(mock, Level.DEBUG, "foo", e, called);
    }

    private void verifyInfo(LogHandler mock, Logger logger, boolean called) {
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

    private void verifyWarning(LogHandler mock, Logger logger, boolean called) {
        logger.warn("foo");
        verifyLog(mock, Level.WARN, "foo", null, called);
        logger.warn("foo {}", "bar");
        verifyLog(mock, Level.WARN, "foo bar", null, called);

        logger.warn("foo {} {}", "bar", "baz");
        verifyLog(mock, Level.WARN, "foo bar baz", null, called);

        logger.warn("foo {} {} {}", "bar", "baz", "qux");
        verifyLog(mock, Level.WARN, "foo bar baz qux", null, called);
        final Error e = new Error();
        logger.warn("foo {} {} {}", "bar", "baz", "qux", e);
        verifyLog(mock, Level.WARN, "foo bar baz qux", e, called);
        logger.warn("foo", e);
        verifyLog(mock, Level.WARN, "foo", e, called);
    }

    private void verifyError(LogHandler mock, Logger logger, boolean called) {
        logger.error("foo");
        verifyLog(mock, Level.ERROR, "foo", null, called);
        logger.error("foo {}", "bar");
        verifyLog(mock, Level.ERROR, "foo bar", null, called);

        logger.error("foo {} {}", "bar", "baz");
        verifyLog(mock, Level.ERROR, "foo bar baz", null, called);

        logger.error("foo {} {} {}", "bar", "baz", "qux");
        verifyLog(mock, Level.ERROR, "foo bar baz qux", null, called);
        final Error e = new Error();
        logger.error("foo {} {} {}", "bar", "baz", "qux", e);
        verifyLog(mock, Level.ERROR, "foo bar baz qux", e, called);
        logger.error("foo", e);
        verifyLog(mock, Level.ERROR, "foo", e, called);
    }

    private void verifyLog(LogHandler mock, esa.commons.logging.Level level, String msg, Throwable t, boolean called) {
        verify(mock, called ? times(1) : never())
                .handle(argThat(event -> event.loggerName().equals("foo")
                        && event.level().equals(level)
                        && event.message().equals(msg)
                        && event.thrown() == t));
    }

}
