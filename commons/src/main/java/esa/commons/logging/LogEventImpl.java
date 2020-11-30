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

class LogEventImpl implements LogEvent {

    private final String loggerName;
    private final Level level;
    private final String msg;
    private final Throwable t;
    private final String threadName;
    private final long timestamp;

    LogEventImpl(String loggerName, Level level, String msg, Throwable t) {
        this.loggerName = loggerName;
        this.level = level;
        this.msg = msg;
        this.t = t;
        this.threadName = Thread.currentThread().getName();
        this.timestamp = System.currentTimeMillis();
    }

    @Override
    public String threadName() {
        return threadName;
    }

    @Override
    public Level level() {
        return level;
    }

    @Override
    public String message() {
        return msg;
    }

    @Override
    public String loggerName() {
        return loggerName;
    }

    @Override
    public Throwable thrown() {
        return t;
    }

    @Override
    public long timestamp() {
        return timestamp;
    }
}
