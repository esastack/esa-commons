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

import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;
import org.slf4j.spi.LocationAwareLogger;

class LocationAwareSlf4Logger implements Logger {

    static final String FQCN = LocationAwareSlf4Logger.class.getName();

    private final LocationAwareLogger logger;

    LocationAwareSlf4Logger(LocationAwareLogger logger) {
        this.logger = logger;
    }

    @Override
    public String name() {
        return logger.getName();
    }

    @Override
    public boolean isTraceEnabled() {
        return logger.isTraceEnabled();
    }

    @Override
    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    @Override
    public boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }

    @Override
    public boolean isWarnEnabled() {
        return logger.isWarnEnabled();
    }

    @Override
    public boolean isErrorEnabled() {
        return logger.isErrorEnabled();
    }

    @Override
    public void trace(String msg) {
        if (isTraceEnabled()) {
            logger.trace(msg);
        }
    }

    @Override
    public void trace(String format, Object arg) {
        if (isTraceEnabled()) {
            final FormattingTuple tuple = MessageFormatter.format(format, arg);
            logger.log(null,
                    FQCN,
                    LocationAwareLogger.TRACE_INT,
                    tuple.getMessage(),
                    tuple.getArgArray(),
                    tuple.getThrowable());
        }
    }

    @Override
    public void trace(String format, Object arg1, Object arg2) {
        if (isTraceEnabled()) {
            final FormattingTuple tuple = MessageFormatter.format(format, arg1, arg2);
            logger.log(null,
                    FQCN,
                    LocationAwareLogger.TRACE_INT,
                    tuple.getMessage(),
                    tuple.getArgArray(),
                    tuple.getThrowable());
        }
    }

    @Override
    public void trace(String format, Object... arguments) {
        if (isTraceEnabled()) {
            final FormattingTuple tuple = MessageFormatter.arrayFormat(format, arguments);
            logger.log(null,
                    FQCN,
                    LocationAwareLogger.TRACE_INT,
                    tuple.getMessage(),
                    tuple.getArgArray(),
                    tuple.getThrowable());
        }
    }

    @Override
    public void trace(String msg, Throwable t) {
        if (isTraceEnabled()) {
            logger.log(null, FQCN, LocationAwareLogger.TRACE_INT, msg, null, t);
        }
    }

    @Override
    public void debug(String msg) {
        if (isDebugEnabled()) {
            logger.debug(msg);
        }
    }

    @Override
    public void debug(String format, Object arg) {
        if (isDebugEnabled()) {
            final FormattingTuple tuple = MessageFormatter.format(format, arg);
            logger.log(null,
                    FQCN,
                    LocationAwareLogger.DEBUG_INT,
                    tuple.getMessage(),
                    tuple.getArgArray(),
                    tuple.getThrowable());
        }
    }

    @Override
    public void debug(String format, Object arg1, Object arg2) {
        if (isDebugEnabled()) {
            final FormattingTuple tuple = MessageFormatter.format(format, arg1, arg2);
            logger.log(null,
                    FQCN,
                    LocationAwareLogger.DEBUG_INT,
                    tuple.getMessage(),
                    tuple.getArgArray(),
                    tuple.getThrowable());
        }
    }

    @Override
    public void debug(String format, Object... arguments) {
        if (isDebugEnabled()) {
            final FormattingTuple tuple = MessageFormatter.arrayFormat(format, arguments);
            logger.log(null,
                    FQCN,
                    LocationAwareLogger.DEBUG_INT,
                    tuple.getMessage(),
                    tuple.getArgArray(),
                    tuple.getThrowable());
        }
    }

    @Override
    public void debug(String msg, Throwable t) {
        if (isDebugEnabled()) {
            logger.log(null, FQCN, LocationAwareLogger.DEBUG_INT, msg, null, t);
        }
    }

    @Override
    public void info(String msg) {
        if (isInfoEnabled()) {
            logger.info(msg);
        }
    }

    @Override
    public void info(String format, Object arg) {
        if (isInfoEnabled()) {
            final FormattingTuple tuple = MessageFormatter.format(format, arg);
            logger.log(null,
                    FQCN,
                    LocationAwareLogger.INFO_INT,
                    tuple.getMessage(),
                    tuple.getArgArray(),
                    tuple.getThrowable());
        }
    }

    @Override
    public void info(String format, Object arg1, Object arg2) {
        if (isInfoEnabled()) {
            final FormattingTuple tuple = MessageFormatter.format(format, arg1, arg2);
            logger.log(null,
                    FQCN,
                    LocationAwareLogger.INFO_INT,
                    tuple.getMessage(),
                    tuple.getArgArray(),
                    tuple.getThrowable());
        }
    }

    @Override
    public void info(String format, Object... arguments) {
        if (isInfoEnabled()) {
            final FormattingTuple tuple = MessageFormatter.arrayFormat(format, arguments);
            logger.log(null,
                    FQCN,
                    LocationAwareLogger.INFO_INT,
                    tuple.getMessage(),
                    tuple.getArgArray(),
                    tuple.getThrowable());
        }
    }

    @Override
    public void info(String msg, Throwable t) {
        if (isInfoEnabled()) {
            logger.log(null, FQCN, LocationAwareLogger.INFO_INT, msg, null, t);
        }
    }

    @Override
    public void warn(String msg) {
        if (isWarnEnabled()) {
            logger.warn(msg);
        }
    }

    @Override
    public void warn(String format, Object arg) {
        if (isWarnEnabled()) {
            final FormattingTuple tuple = MessageFormatter.format(format, arg);
            logger.log(null,
                    FQCN,
                    LocationAwareLogger.WARN_INT,
                    tuple.getMessage(),
                    tuple.getArgArray(),
                    tuple.getThrowable());
        }
    }

    @Override
    public void warn(String format, Object arg1, Object arg2) {
        if (isWarnEnabled()) {
            final FormattingTuple tuple = MessageFormatter.format(format, arg1, arg2);
            logger.log(null,
                    FQCN,
                    LocationAwareLogger.WARN_INT,
                    tuple.getMessage(),
                    tuple.getArgArray(),
                    tuple.getThrowable());
        }
    }

    @Override
    public void warn(String format, Object... arguments) {
        if (isWarnEnabled()) {
            final FormattingTuple tuple = MessageFormatter.arrayFormat(format, arguments);
            logger.log(null,
                    FQCN,
                    LocationAwareLogger.WARN_INT,
                    tuple.getMessage(),
                    tuple.getArgArray(),
                    tuple.getThrowable());
        }
    }

    @Override
    public void warn(String msg, Throwable t) {
        if (isWarnEnabled()) {
            logger.log(null, FQCN, LocationAwareLogger.WARN_INT, msg, null, t);
        }
    }

    @Override
    public void error(String msg) {
        if (isErrorEnabled()) {
            logger.error(msg);
        }
    }

    @Override
    public void error(String format, Object arg) {
        if (isErrorEnabled()) {
            final FormattingTuple tuple = MessageFormatter.format(format, arg);
            logger.log(null,
                    FQCN,
                    LocationAwareLogger.ERROR_INT,
                    tuple.getMessage(),
                    tuple.getArgArray(),
                    tuple.getThrowable());
        }
    }

    @Override
    public void error(String format, Object arg1, Object arg2) {
        if (isErrorEnabled()) {
            final FormattingTuple tuple = MessageFormatter.format(format, arg1, arg2);
            logger.log(null,
                    FQCN,
                    LocationAwareLogger.ERROR_INT,
                    tuple.getMessage(),
                    tuple.getArgArray(),
                    tuple.getThrowable());
        }
    }

    @Override
    public void error(String format, Object... arguments) {
        if (isErrorEnabled()) {
            final FormattingTuple tuple = MessageFormatter.arrayFormat(format, arguments);
            logger.log(null,
                    FQCN,
                    LocationAwareLogger.ERROR_INT,
                    tuple.getMessage(),
                    tuple.getArgArray(),
                    tuple.getThrowable());
        }
    }

    @Override
    public void error(String msg, Throwable t) {
        if (isErrorEnabled()) {
            logger.log(null, FQCN, LocationAwareLogger.ERROR_INT, msg, null, t);
        }
    }

    @Override
    public Object unwrap() {
        return logger;
    }
}
