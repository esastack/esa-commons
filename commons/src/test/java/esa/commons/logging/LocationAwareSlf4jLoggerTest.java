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
import org.slf4j.spi.LocationAwareLogger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LocationAwareSlf4jLoggerTest {

    @Test
    void testNameAndUnwrap() {
        final org.slf4j.spi.LocationAwareLogger mock = mock(org.slf4j.spi.LocationAwareLogger.class);
        when(mock.getName()).thenReturn("foo");
        final Logger logger = new LocationAwareSlf4Logger(mock);
        assertEquals("foo", logger.name());
        assertSame(mock, logger.unwrap());
    }

    @Test
    void testTrace() {
        final org.slf4j.spi.LocationAwareLogger mock = mock(org.slf4j.spi.LocationAwareLogger.class);
        final Logger logger = new LocationAwareSlf4Logger(mock);
        when(mock.isTraceEnabled()).thenReturn(true);
        assertTrue(logger.isTraceEnabled());
        verifyTrace(mock, logger, true);

        reset(mock);

        when(mock.isTraceEnabled()).thenReturn(false);
        assertFalse(logger.isTraceEnabled());
        verifyTrace(mock, logger, false);
    }

    @Test
    void testDebug() {
        final org.slf4j.spi.LocationAwareLogger mock = mock(org.slf4j.spi.LocationAwareLogger.class);
        final Logger logger = new LocationAwareSlf4Logger(mock);
        when(mock.isDebugEnabled()).thenReturn(true);
        assertTrue(logger.isDebugEnabled());
        verifyDebug(mock, logger, true);

        reset(mock);

        when(mock.isDebugEnabled()).thenReturn(false);
        assertFalse(logger.isDebugEnabled());
        verifyDebug(mock, logger, false);
    }

    @Test
    void testInfo() {
        final org.slf4j.spi.LocationAwareLogger mock = mock(org.slf4j.spi.LocationAwareLogger.class);
        final Logger logger = new LocationAwareSlf4Logger(mock);
        when(mock.isInfoEnabled()).thenReturn(true);
        assertTrue(logger.isInfoEnabled());
        verifyInfo(mock, logger, true);

        reset(mock);

        when(mock.isInfoEnabled()).thenReturn(false);
        assertFalse(logger.isInfoEnabled());
        verifyInfo(mock, logger, false);
    }

    @Test
    void testWarning() {
        final org.slf4j.spi.LocationAwareLogger mock = mock(org.slf4j.spi.LocationAwareLogger.class);
        final Logger logger = new LocationAwareSlf4Logger(mock);
        when(mock.isWarnEnabled()).thenReturn(true);
        assertTrue(logger.isWarnEnabled());
        verifyWarn(mock, logger, true);

        reset(mock);

        when(mock.isWarnEnabled()).thenReturn(false);
        assertFalse(logger.isWarnEnabled());
        verifyWarn(mock, logger, false);
    }

    @Test
    void testError() {
        final org.slf4j.spi.LocationAwareLogger mock = mock(org.slf4j.spi.LocationAwareLogger.class);
        final Logger logger = new LocationAwareSlf4Logger(mock);
        when(mock.isErrorEnabled()).thenReturn(true);
        assertTrue(logger.isErrorEnabled());
        verifyError(mock, logger, true);

        reset(mock);

        when(mock.isErrorEnabled()).thenReturn(false);
        assertFalse(logger.isErrorEnabled());
        verifyError(mock, logger, false);
    }

    private void verifyTrace(LocationAwareLogger mock, Logger logger, boolean called) {
        logger.trace("foo");
        verify(mock, called ? times(1) : never()).trace("foo");
        logger.trace("foo {}", "bar");
        verify(mock, called ? times(1) : never())
                .log(isNull(),
                        eq(LocationAwareSlf4Logger.FQCN),
                        eq(LocationAwareLogger.TRACE_INT),
                        eq("foo bar"),
                        any(),
                        isNull());

        logger.trace("foo {} {}", "bar", "baz");
        verify(mock, called ? times(1) : never())
                .log(isNull(),
                        eq(LocationAwareSlf4Logger.FQCN),
                        eq(LocationAwareLogger.TRACE_INT),
                        eq("foo bar baz"),
                        any(),
                        isNull());
        logger.trace("foo {} {} {}", "bar", "baz", "qux");
        verify(mock, called ? times(1) : never())
                .log(isNull(),
                        eq(LocationAwareSlf4Logger.FQCN),
                        eq(LocationAwareLogger.TRACE_INT),
                        eq("foo bar baz qux"),
                        any(),
                        isNull());
        final Error e = new Error();
        logger.trace("foo {} {} {}", "bar", "baz", "qux", e);
        verify(mock, called ? times(1) : never())
                .log(isNull(),
                        eq(LocationAwareSlf4Logger.FQCN),
                        eq(LocationAwareLogger.TRACE_INT),
                        eq("foo bar baz qux"),
                        any(),
                        eq(e));
        logger.trace("foo", e);
        verify(mock, called ? times(1) : never())
                .log(isNull(),
                        eq(LocationAwareSlf4Logger.FQCN),
                        eq(LocationAwareLogger.TRACE_INT),
                        eq("foo"),
                        any(),
                        eq(e));
    }

    private void verifyDebug(LocationAwareLogger mock, Logger logger, boolean called) {
        logger.debug("foo");
        verify(mock, called ? times(1) : never())
                .debug("foo");
        logger.debug("foo {}", "bar");
        verify(mock, called ? times(1) : never())
                .log(isNull(),
                        eq(LocationAwareSlf4Logger.FQCN),
                        eq(LocationAwareLogger.DEBUG_INT),
                        eq("foo bar"),
                        any(),
                        isNull());

        logger.debug("foo {} {}", "bar", "baz");
        verify(mock, called ? times(1) : never())
                .log(isNull(),
                        eq(LocationAwareSlf4Logger.FQCN),
                        eq(LocationAwareLogger.DEBUG_INT),
                        eq("foo bar baz"),
                        any(),
                        isNull());
        logger.debug("foo {} {} {}", "bar", "baz", "qux");
        verify(mock, called ? times(1) : never())
                .log(isNull(),
                        eq(LocationAwareSlf4Logger.FQCN),
                        eq(LocationAwareLogger.DEBUG_INT),
                        eq("foo bar baz qux"),
                        any(),
                        isNull());
        final Error e = new Error();
        logger.debug("foo {} {} {}", "bar", "baz", "qux", e);
        verify(mock, called ? times(1) : never())
                .log(isNull(),
                        eq(LocationAwareSlf4Logger.FQCN),
                        eq(LocationAwareLogger.DEBUG_INT),
                        eq("foo bar baz qux"),
                        any(),
                        eq(e));
        logger.debug("foo", e);
        verify(mock, called ? times(1) : never())
                .log(isNull(),
                        eq(LocationAwareSlf4Logger.FQCN),
                        eq(LocationAwareLogger.DEBUG_INT),
                        eq("foo"),
                        any(),
                        eq(e));
    }

    private void verifyInfo(LocationAwareLogger mock, Logger logger, boolean called) {
        logger.info("foo");
        verify(mock, called ? times(1) : never())
                .info("foo");
        logger.info("foo {}", "bar");
        verify(mock, called ? times(1) : never())
                .log(isNull(),
                        eq(LocationAwareSlf4Logger.FQCN),
                        eq(LocationAwareLogger.INFO_INT),
                        eq("foo bar"),
                        any(),
                        isNull());

        logger.info("foo {} {}", "bar", "baz");
        verify(mock, called ? times(1) : never())
                .log(isNull(),
                        eq(LocationAwareSlf4Logger.FQCN),
                        eq(LocationAwareLogger.INFO_INT),
                        eq("foo bar baz"),
                        any(),
                        isNull());
        logger.info("foo {} {} {}", "bar", "baz", "qux");
        verify(mock, called ? times(1) : never())
                .log(isNull(),
                        eq(LocationAwareSlf4Logger.FQCN),
                        eq(LocationAwareLogger.INFO_INT),
                        eq("foo bar baz qux"),
                        any(),
                        isNull());
        final Error e = new Error();
        logger.info("foo {} {} {}", "bar", "baz", "qux", e);
        verify(mock, called ? times(1) : never())
                .log(isNull(),
                        eq(LocationAwareSlf4Logger.FQCN),
                        eq(LocationAwareLogger.INFO_INT),
                        eq("foo bar baz qux"),
                        any(),
                        eq(e));
        logger.info("foo", e);
        verify(mock, called ? times(1) : never())
                .log(isNull(),
                        eq(LocationAwareSlf4Logger.FQCN),
                        eq(LocationAwareLogger.INFO_INT),
                        eq("foo"),
                        any(),
                        eq(e));
    }

    private void verifyWarn(LocationAwareLogger mock, Logger logger, boolean called) {
        logger.warn("foo");
        verify(mock, called ? times(1) : never())
                .warn("foo");
        logger.warn("foo {}", "bar");
        verify(mock, called ? times(1) : never())
                .log(isNull(),
                        eq(LocationAwareSlf4Logger.FQCN),
                        eq(LocationAwareLogger.WARN_INT),
                        eq("foo bar"),
                        any(),
                        isNull());

        logger.warn("foo {} {}", "bar", "baz");
        verify(mock, called ? times(1) : never())
                .log(isNull(),
                        eq(LocationAwareSlf4Logger.FQCN),
                        eq(LocationAwareLogger.WARN_INT),
                        eq("foo bar baz"),
                        any(),
                        isNull());
        logger.warn("foo {} {} {}", "bar", "baz", "qux");
        verify(mock, called ? times(1) : never())
                .log(isNull(),
                        eq(LocationAwareSlf4Logger.FQCN),
                        eq(LocationAwareLogger.WARN_INT),
                        eq("foo bar baz qux"),
                        any(),
                        isNull());
        final Error e = new Error();
        logger.warn("foo {} {} {}", "bar", "baz", "qux", e);
        verify(mock, called ? times(1) : never())
                .log(isNull(),
                        eq(LocationAwareSlf4Logger.FQCN),
                        eq(LocationAwareLogger.WARN_INT),
                        eq("foo bar baz qux"),
                        any(),
                        eq(e));
        logger.warn("foo", e);
        verify(mock, called ? times(1) : never())
                .log(isNull(),
                        eq(LocationAwareSlf4Logger.FQCN),
                        eq(LocationAwareLogger.WARN_INT),
                        eq("foo"),
                        any(),
                        eq(e));
    }

    private void verifyError(LocationAwareLogger mock, Logger logger, boolean called) {
        logger.error("foo");
        verify(mock, called ? times(1) : never())
                .error("foo");
        logger.error("foo {}", "bar");
        verify(mock, called ? times(1) : never())
                .log(isNull(),
                        eq(LocationAwareSlf4Logger.FQCN),
                        eq(LocationAwareLogger.ERROR_INT),
                        eq("foo bar"),
                        any(),
                        isNull());

        logger.error("foo {} {}", "bar", "baz");
        verify(mock, called ? times(1) : never())
                .log(isNull(),
                        eq(LocationAwareSlf4Logger.FQCN),
                        eq(LocationAwareLogger.ERROR_INT),
                        eq("foo bar baz"),
                        any(),
                        isNull());
        logger.error("foo {} {} {}", "bar", "baz", "qux");
        verify(mock, called ? times(1) : never())
                .log(isNull(),
                        eq(LocationAwareSlf4Logger.FQCN),
                        eq(LocationAwareLogger.ERROR_INT),
                        eq("foo bar baz qux"),
                        any(),
                        isNull());
        final Error e = new Error();
        logger.error("foo {} {} {}", "bar", "baz", "qux", e);
        verify(mock, called ? times(1) : never())
                .log(isNull(),
                        eq(LocationAwareSlf4Logger.FQCN),
                        eq(LocationAwareLogger.ERROR_INT),
                        eq("foo bar baz qux"),
                        any(),
                        eq(e));
        logger.error("foo", e);
        verify(mock, called ? times(1) : never())
                .log(isNull(),
                        eq(LocationAwareSlf4Logger.FQCN),
                        eq(LocationAwareLogger.ERROR_INT),
                        eq("foo"),
                        any(),
                        eq(e));
    }
}
