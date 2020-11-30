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
import esa.commons.annotation.Beta;
import esa.commons.annotation.Internal;

import java.io.File;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * Bootstraps an {@link InternalLogger}, which allows to write logs to the specified {@link File} without depending on
 * an underlying log framework(such as log4j, logback...).
 */
@Beta
@Internal
public class InternalLoggers {

    /**
     * Creates a builder of {@link InternalLogger} by {@code name}.
     *
     * @param name   logger name
     * @param target target log file
     *
     * @return builder
     */
    public static Builder logger(String name, File target) {
        return new Builder(name, target);
    }

    /**
     * Creates a builder of {@link InternalLogger} by name of {@code clz}.
     *
     * @param clz    class
     * @param target target log file
     *
     * @return builder
     */
    public static Builder logger(Class<?> clz, File target) {
        return new Builder(clz.getName(), target);
    }

    /**
     * Creates a builder of {@link InternalLogger} by {@code name}.
     *
     * @param name   logger name
     * @param target target log file
     *
     * @return builder
     */
    public static Builder logger(String name, String target) {
        return new Builder(name, new File(target));
    }

    /**
     * Creates a builder of {@link InternalLogger} by name of {@code clz}.
     *
     * @param clz    class
     * @param target target log file
     *
     * @return builder
     */
    public static Builder logger(Class<?> clz, String target) {
        return new Builder(clz.getName(), new File(target));
    }

    @Beta
    @Internal
    public static class Builder {
        private static int DEFAULT_MAX_HISTORY = 10;
        private final String name;
        private final File target;
        private String pattern = "%date %level [%thread] %logger : %msg%n%thrown";
        private int queue;
        private int writeBuffer;
        private Charset charset;

        private Supplier<RollingFileAppender.Rolling> rolling = () -> RollingFileAppender.Rolling.NOOP;

        private Builder(String name, File file) {
            Checks.checkNotNull(name, "loggerName");
            Checks.checkNotNull(file, "file");
            this.name = name;
            this.target = file;
        }

        /**
         * Sets the patter of log message.
         *
         * <ul>
         * <li>{@code %d}, {@code %date} : date format of yyyy-MM-dd HH:mm:ss.SSS</li>
         * <li>{@code %d{yyy-MM-dd}}, {@code %{yyy-MM-dd}} : date format of yyy-MM-DD</li>
         * <li>{@code %l}, {@code %level} : Log Level, such as 'INFO', 'DEBUG'</li>
         * <li>{@code %t}, {@code %thread} : thread name</li>
         * <li>{@code %logger} : logger name</li>
         * <li>{@code %m}, {@code %msg}, {@code %message} : message body</li>
         * <li>{@code %n} : new line</li>
         * <li>{@code %ex}, {@code %exception}, {@code %thrown} : exception stack</li>
         * </ul>
         * <p>
         * eg. A logger(named: foo) message with pattern: {@code %date{yyyy-MM-dd} %level [%thread] %logger : %msg%n}
         * <pre>
         * logger.info("hello {}", "world!");
         * </pre>
         * out put: 1970-01-01 INFO [main] foo : hello world!\n
         *
         * @param pattern patter of message
         *
         * @return builder
         */
        public Builder pattern(String pattern) {
            Checks.checkNotEmptyArg(pattern, "pattern");
            this.pattern = pattern;
            return this;
        }

        /**
         * Sets the charset of message.
         *
         * @param charset charset
         *
         * @return builder
         */
        public Builder charset(Charset charset) {
            this.charset = charset;
            return this;
        }

        /**
         * Sets the size of queue which will be used in {@link SingleThreadLogHandler}.
         *
         * @param queue size of queue
         *
         * @return builder
         */
        public Builder queue(int queue) {
            Checks.checkArg(queue > 0, "queue size must over than zero: " + queue);
            this.queue = queue;
            return this;
        }

        /**
         * Sets the size of buffer before writing data to the target file. All the log events is expected to be buffered
         * as a block of bytes in {@link SingleThreadLogHandler} and then passed to {@link Appender}.
         *
         * @param writeBuffer write buffer
         *
         * @return builder
         */
        public Builder writeBuffer(int writeBuffer) {
            Checks.checkArg(writeBuffer > 0, "write buffer must over than zero: " + writeBuffer);
            this.writeBuffer = writeBuffer;
            return this;
        }

        /**
         * @see #useSizeBasedRolling(File, long, int)
         */
        public Builder useSizeBasedRolling(long maxSize) {
            return useSizeBasedRolling(maxSize, DEFAULT_MAX_HISTORY);
        }

        /**
         * @see #useSizeBasedRolling(File, long, int)
         */
        public Builder useSizeBasedRolling(long maxSize, int maxHistory) {
            return useSizeBasedRolling(target, maxSize, maxHistory);
        }

        /**
         * Uses size based rolling policy, a new log file will be created when the length current log file is over than
         * this value(Inaccurate). The directory of history file will be same with the given {@code file}, and the name
         * of the history file will also be similar with the the given {@code file}. An oldest log file will be deleted
         * if there're too many history files who's number is over than the value of {@code maxHistory}.
         *
         * @param file       template file of history file
         * @param maxSize    max size per-file
         * @param maxHistory max num of history files
         *
         * @return builder
         */
        public Builder useSizeBasedRolling(File file, long maxSize, int maxHistory) {
            this.rolling = () -> new RollingFileAppender.SizedBasedRolling(file, maxHistory, maxSize);
            return this;
        }

        /**
         * @see #useTimeBasedRolling(File, String, int)
         */
        public Builder useTimeBasedRolling(String datePattern) {
            return useTimeBasedRolling(datePattern, DEFAULT_MAX_HISTORY);
        }

        /**
         * @see #useTimeBasedRolling(File, String, int)
         */
        public Builder useTimeBasedRolling(String datePattern, int maxHistory) {
            return useTimeBasedRolling(target, datePattern, maxHistory);
        }

        /**
         * Uses time based rolling policy, a new log file will be created when time is not in period. The directory of
         * history file will be same with the given {@code file}, and the name of the  history file will also be similar
         * with the the given {@code file}. An oldest log file will be deleted if there're  too many history files who's
         * number is over than the value of {@code maxHistory}.
         *
         * @param file        template file of history file
         * @param datePattern patter of date only support: {@code Hour} and {@code Day} level,if you want your log files
         *                    be be created every day, this value should be {@code yyyy-MM-dd} or {@code yyyyMMdd}(and
         *                    so on..) , and if you want your log files be be created every hour, this value should be
         *                    {@code yyyy-MM-dd_HH} or {@code yyyyMMddHH} (and so on..)
         *                    <p>
         *                    Note: the pattern will be used to compose the history file's name, so please be care of
         *                    that do not use any pattern that contains any illegal character of a name of file.
         * @param maxHistory  max num of history files
         *
         * @return builder
         */
        public Builder useTimeBasedRolling(File file, String datePattern, int maxHistory) {
            this.rolling = () -> new RollingFileAppender.TimeBasedRolling(file, maxHistory, datePattern);
            return this;
        }

        /**
         * @see #useTimeAndSizeBasedRolling(File, String, long, int)
         */
        public Builder useTimeAndSizeBasedRolling(String datePattern, long maxSize) {
            return useTimeAndSizeBasedRolling(datePattern, maxSize, DEFAULT_MAX_HISTORY);
        }

        /**
         * @see #useTimeAndSizeBasedRolling(File, String, long, int)
         */
        public Builder useTimeAndSizeBasedRolling(String datePattern, long maxSize, int maxHistory) {
            return useTimeAndSizeBasedRolling(target, datePattern, maxSize, maxHistory);
        }


        /**
         * @see #useTimeBasedRolling(File, String, int)
         * @see #useSizeBasedRolling(File, long, int)
         */
        public Builder useTimeAndSizeBasedRolling(File file, String datePattern, long maxSize,
                                                  int maxHistory) {
            this.rolling = () -> new RollingFileAppender.TimeAndSizeBasedRolling(file, maxHistory, datePattern,
                    maxSize);
            return this;
        }

        /**
         * Builds a new instance of {@link InternalLogger}.
         *
         * @return logger
         * @throws IllegalStateException if the target file has been opened by any other logger.
         */
        public InternalLogger build() {
            return build0(false);
        }

        /**
         * Builds a new instance of {@link InternalLogger}, and try to use a same file with other logger instance.
         *
         * @return logger
         */
        public InternalLogger getOrBuild() {
            return build0(true);
        }

        private InternalLogger build0(boolean reuseHandler) {
            Checks.checkNotNull(target, "target file");
            synchronized (InternalLoggers.class) {
                LogHandler logHandler = Manager.LOGGER_HANDLERS.get(target);
                if (logHandler == null) {
                    final Appender appender = RollingFileAppender.newInstance(target, rolling.get());
                    logHandler = new SingleThreadLogHandler(appender,
                            new EncoderImpl(pattern, charset), queue, writeBuffer);
                } else if (!reuseHandler) {
                    throw new IllegalStateException(
                            "Could not build logger because file '" +
                                    target.getAbsolutePath()
                                    + "' has been opened by other loggers.");
                }
                Manager.LOGGER_HANDLERS.put(target, logHandler);
                return new InternalLoggerImpl(name, logHandler);
            }
        }
    }

    static class Manager {
        private static final int LOCAL_STRING_BUILDER_SIZE = 2048;
        static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
        static final ConcurrentHashMap<File, LogHandler> LOGGER_HANDLERS = new ConcurrentHashMap<>();
        private static final ThreadLocal<StringBuilder> SBUF =
                ThreadLocal.withInitial(() -> new StringBuilder(LOCAL_STRING_BUILDER_SIZE));
        private static final ThreadLocal<SimpleDateFormat> SDF =
                ThreadLocal.withInitial(() -> new SimpleDateFormat(DEFAULT_DATE_FORMAT));

        static {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> LOGGER_HANDLERS.forEach((f, h) -> {
                try {
                    SBUF.remove();
                    SDF.remove();
                    h.stop();
                } catch (Throwable t) {
                    System.err.println("Failed to stop handler '" + f.getAbsolutePath() + "'");
                }
            }), "esa-logging-hook"));
        }

        static StringBuilder localSb(int len) {
            if (len < LOCAL_STRING_BUILDER_SIZE) {
                StringBuilder sb = SBUF.get();
                sb.setLength(0);
                return sb;
            } else {
                return new StringBuilder(len + 128);
            }
        }

        static SimpleDateFormat localSdf() {
            return SDF.get();
        }
    }

}
