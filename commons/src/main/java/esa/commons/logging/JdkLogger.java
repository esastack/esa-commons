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

import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * Delegate of {@link java.util.logging.Logger}.
 */
class JdkLogger implements Logger {

    private final java.util.logging.Logger logger;

    JdkLogger(java.util.logging.Logger logger) {
        this.logger = logger;
    }

    @Override
    public String name() {
        return logger.getName();
    }

    @Override
    public boolean isTraceEnabled() {
        return logger.isLoggable(Level.FINEST);
    }

    @Override
    public boolean isDebugEnabled() {
        return logger.isLoggable(Level.FINE);
    }

    @Override
    public boolean isInfoEnabled() {
        return logger.isLoggable(Level.INFO);
    }

    @Override
    public boolean isWarnEnabled() {
        return logger.isLoggable(Level.WARNING);
    }

    @Override
    public boolean isErrorEnabled() {
        return logger.isLoggable(Level.SEVERE);
    }

    @Override
    public void trace(String msg) {
        log(Level.FINEST, msg, null);
    }

    @Override
    public void trace(String format, Object arg) {
        log(Level.FINEST, format, null, arg);
    }

    @Override
    public void trace(String format, Object arg1, Object arg2) {
        log(Level.FINEST, format, null, arg1, arg2);
    }

    @Override
    public void trace(String format, Object... arguments) {
        log(Level.FINEST, format, null, arguments);
    }

    @Override
    public void trace(String msg, Throwable t) {
        log(Level.FINEST, msg, t);
    }

    @Override
    public void debug(String msg) {
        log(Level.FINE, msg, null);
    }

    @Override
    public void debug(String format, Object arg) {
        log(Level.FINE, format, null, arg);
    }

    @Override
    public void debug(String format, Object arg1, Object arg2) {
        log(Level.FINE, format, null, arg1, arg2);
    }

    @Override
    public void debug(String format, Object... arguments) {
        log(Level.FINE, format, null, arguments);
    }

    @Override
    public void debug(String msg, Throwable t) {
        log(Level.FINE, msg, t);
    }

    @Override
    public void info(String msg) {
        log(Level.INFO, msg, null);
    }

    @Override
    public void info(String format, Object arg) {
        log(Level.INFO, format, null, arg);
    }

    @Override
    public void info(String format, Object arg1, Object arg2) {
        log(Level.INFO, format, null, arg1, arg2);
    }

    @Override
    public void info(String format, Object... arguments) {
        log(Level.INFO, format, null, arguments);
    }

    @Override
    public void info(String msg, Throwable t) {
        log(Level.INFO, msg, t);
    }

    @Override
    public void warn(String msg) {
        log(Level.WARNING, msg, null);
    }

    @Override
    public void warn(String format, Object arg) {
        log(Level.WARNING, format, null, arg);
    }

    @Override
    public void warn(String format, Object arg1, Object arg2) {
        log(Level.WARNING, format, null, arg1, arg2);
    }

    @Override
    public void warn(String format, Object... arguments) {
        log(Level.WARNING, format, null, arguments);
    }

    @Override
    public void warn(String msg, Throwable t) {
        log(Level.WARNING, msg, t);
    }

    @Override
    public void error(String msg) {
        log(Level.SEVERE, msg, null);
    }

    @Override
    public void error(String format, Object arg) {
        log(Level.SEVERE, format, null, arg);
    }

    @Override
    public void error(String format, Object arg1, Object arg2) {
        log(Level.SEVERE, format, null, arg1, arg2);
    }

    @Override
    public void error(String format, Object... arguments) {
        log(Level.SEVERE, format, null, arguments);
    }

    @Override
    public void error(String msg, Throwable t) {
        log(Level.SEVERE, msg, t);
    }

    @Override
    public Object unwrap() {
        return logger;
    }

    private void log(Level level, String message, Throwable t, Object... params) {
        if (!logger.isLoggable(level)) {
            return;
        }

        if (params != null && params.length != 0) {
            if (params[params.length - 1] instanceof Throwable) {
                if (params.length > 1) {
                    message = Formatter.format(message, params, params.length - 1);
                }
                t = (Throwable) params[params.length - 1];
            } else {
                message = Formatter.format(message, params);
            }
        }

        LogRecord record = new LogRecord(level, message == null ? "NULL" : message);
        record.setLoggerName(name());
        record.setThrown(t);
        record.setSourceClassName(null);
        logger.log(record);
    }
}
