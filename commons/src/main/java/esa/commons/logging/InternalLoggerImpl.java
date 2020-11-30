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

import esa.commons.Checks;

class InternalLoggerImpl implements InternalLogger {
    private final String name;
    private final LogHandler logHandler;
    private volatile Level level = Level.INFO;

    InternalLoggerImpl(String name, LogHandler logHandler) {
        Checks.checkNotEmptyArg(name, "name");
        Checks.checkNotNull(logHandler);
        this.name = name;
        this.logHandler = logHandler;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public boolean isTraceEnabled() {
        return isLogEnabled(Level.TRACE);
    }

    @Override
    public boolean isDebugEnabled() {
        return isLogEnabled(Level.DEBUG);
    }

    @Override
    public boolean isInfoEnabled() {
        return isLogEnabled(Level.INFO);
    }

    @Override
    public boolean isWarnEnabled() {
        return isLogEnabled(Level.WARN);
    }

    @Override
    public boolean isErrorEnabled() {
        return isLogEnabled(Level.ERROR);
    }

    @Override
    public void trace(String msg) {
        log(Level.TRACE, msg, null);
    }

    @Override
    public void trace(String format, Object arg) {
        log(Level.TRACE, format, null, arg);
    }

    @Override
    public void trace(String format, Object arg1, Object arg2) {
        log(Level.TRACE, format, null, arg1, arg2);
    }

    @Override
    public void trace(String format, Object... arguments) {
        log(Level.TRACE, format, null, arguments);
    }

    @Override
    public void trace(String msg, Throwable t) {
        log(Level.TRACE, msg, t);
    }

    @Override
    public void debug(String msg) {
        log(Level.DEBUG, msg, null);
    }

    @Override
    public void debug(String format, Object arg) {
        log(Level.DEBUG, format, null, arg);
    }

    @Override
    public void debug(String format, Object arg1, Object arg2) {
        log(Level.DEBUG, format, null, arg1, arg2);
    }

    @Override
    public void debug(String format, Object... arguments) {
        log(Level.DEBUG, format, null, arguments);
    }

    @Override
    public void debug(String msg, Throwable t) {
        log(Level.DEBUG, msg, t);
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
        log(Level.WARN, msg, null);
    }

    @Override
    public void warn(String format, Object arg) {
        log(Level.WARN, format, null, arg);
    }

    @Override
    public void warn(String format, Object arg1, Object arg2) {
        log(Level.WARN, format, null, arg1, arg2);
    }

    @Override
    public void warn(String format, Object... arguments) {
        log(Level.WARN, format, null, arguments);
    }

    @Override
    public void warn(String msg, Throwable t) {
        log(Level.WARN, msg, t);
    }

    @Override
    public void error(String msg) {
        log(Level.ERROR, msg, null);
    }

    @Override
    public void error(String format, Object arg) {
        log(Level.ERROR, format, null, arg);
    }

    @Override
    public void error(String format, Object arg1, Object arg2) {
        log(Level.ERROR, format, null, arg1, arg2);
    }

    @Override
    public void error(String format, Object... arguments) {
        log(Level.ERROR, format, null, arguments);
    }

    @Override
    public void error(String msg, Throwable t) {
        log(Level.ERROR, msg, t);
    }

    @Override
    public Object unwrap() {
        return this;
    }

    @Override
    public Level level() {
        return level;
    }

    @Override
    public void setLevel(Level level) {
        this.level = level;
    }

    @Override
    public boolean isLogEnabled(Level level) {
        final Level current = this.level;
        return current.val() <= level.val();
    }

    private void log(Level level, String message, Throwable t, Object... params) {
        if (!isLogEnabled(level)) {
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
        if (message == null) {
            message = "NULL";
        }

        logHandler.handle(new LogEventImpl(name(), level, message, t));
    }
}
