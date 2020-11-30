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
import esa.commons.ClassUtils;
import esa.commons.ExceptionUtils;
import esa.commons.Platforms;
import esa.commons.StringUtils;
import esa.commons.concurrent.UnsafeUtils;
import esa.commons.reflect.ReflectionUtils;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

class EncoderImpl implements Encoder {

    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private static final Map<String, Function<String, Converter<LogEvent>>> CONVERTERS;
    private static final Function<StringBuilder, byte[]> ENCODER;
    private static final BiFunction<StringBuilder, Charset, byte[]> ENCODER_WITH_CHARSET;

    private final Converter<LogEvent> converter;
    private final Function<StringBuilder, byte[]> strEncoder;

    EncoderImpl(String pattern, Charset charset) {
        Checks.checkNotNull(pattern, "pattern");
        this.converter = Formatter.toConverter(pattern, CONVERTERS);
        if (charset == null) {
            this.strEncoder = ENCODER;
        } else {
            this.strEncoder = sb -> ENCODER_WITH_CHARSET.apply(sb, charset);
        }
    }

    @Override
    public byte[] encode(LogEvent event) {
        StringBuilder sb = InternalLoggers.Manager.localSb(event.message().length());
        converter.convert(event, sb);
        return strEncoder.apply(sb);
    }

    @Override
    public void stop() {
        converter.stop();
    }

    static {
        // initialize converters
        CONVERTERS = new LinkedHashMap<>();
        final Function<String, Converter<LogEvent>> dateConverter =
                param -> {

                    if (StringUtils.isEmpty(param) || DEFAULT_DATE_FORMAT.equals(param)) {
                        return new Converter<LogEvent>() {

                            private long lastTimestamp = -1L;
                            private String cacheTime;

                            @Override
                            public void convert(LogEvent event, StringBuilder stringBuilder) {
                                long t = event.timestamp();
                                if (t != lastTimestamp) {
                                    lastTimestamp = t;
                                    cacheTime = InternalLoggers.Manager.localSdf().format(new Date(t));
                                }
                                stringBuilder.append(cacheTime);
                            }
                        };
                    } else {
                        return new Converter<LogEvent>() {

                            private final ThreadLocal<SimpleDateFormat> sdf =
                                    ThreadLocal.withInitial(() -> new SimpleDateFormat(param));
                            private long lastTimestamp = -1L;
                            private String cacheTime;

                            @Override
                            public void convert(LogEvent event, StringBuilder stringBuilder) {
                                long t = event.timestamp();
                                if (t != lastTimestamp) {
                                    lastTimestamp = t;
                                    cacheTime = sdf.get().format(new Date(t));
                                }
                                stringBuilder.append(cacheTime);
                            }

                            @Override
                            public void stop() {
                                sdf.remove();
                            }
                        };
                    }
                };
        CONVERTERS.put("d", dateConverter);
        CONVERTERS.put("date", dateConverter);

        final Function<String, Converter<LogEvent>> levelConverter =
                param -> (event, sbBuf) -> sbBuf.append(event.level().toString());
        CONVERTERS.put("l", levelConverter);
        CONVERTERS.put("level", levelConverter);

        final Function<String, Converter<LogEvent>> threadConverter =
                param -> (event, sbBuf) -> sbBuf.append(event.threadName());
        CONVERTERS.put("t", threadConverter);
        CONVERTERS.put("thread", threadConverter);

        final Function<String, Converter<LogEvent>> loggerConverter =
                param -> (event, sbBuf) -> sbBuf.append(event.loggerName());
        CONVERTERS.put("logger", loggerConverter);

        final Function<String, Converter<LogEvent>> messageConverter =
                param -> (event, sbBuf) -> sbBuf.append(event.message());
        CONVERTERS.put("m", messageConverter);
        CONVERTERS.put("msg", messageConverter);
        CONVERTERS.put("message", messageConverter);

        final Function<String, Converter<LogEvent>> lineConverter =
                param -> (event, sbBuf) -> sbBuf.append(LINE_SEPARATOR);
        CONVERTERS.put("n", lineConverter);

        // TODO: performance up
        final Function<String, Converter<LogEvent>> throwableConverter =
                param -> (event, sbBuf) -> {
                    Throwable t = event.thrown();
                    if (t != null) {
                        ExceptionUtils.getStackTrace(event.thrown(), sbBuf);
                    }
                };
        CONVERTERS.put("ex", throwableConverter);
        CONVERTERS.put("exception", throwableConverter);
        CONVERTERS.put("thrown", throwableConverter);

        // detect StringBuilder encoder
        Function<StringBuilder, byte[]> en = null;
        BiFunction<StringBuilder, Charset, byte[]> en1 = null;

        try {
            if (Platforms.javaVersion() < 9) {
                // hack for jdk8, avoid byte array copy
                long valueFieldOffset = -1;
                Field valueField = null;
                Method encodeMethod;
                Method encodeMethodWithCharset;

                final ClassLoader cl = ClassUtils.getClassLoader();
                Class<?> abstractStringBuilder =
                        Class.forName("java.lang.AbstractStringBuilder", true, cl);

                if (UnsafeUtils.hasUnsafe()) {
                    valueFieldOffset =
                            UnsafeUtils.objectFieldOffset(abstractStringBuilder, "value");
                } else {
                    valueField = abstractStringBuilder.getDeclaredField("value");
                    ReflectionUtils.makeFieldAccessible(valueField);
                }

                Class<?> stringEncoding = Class.forName("java.lang.StringCoding", true, cl);
                encodeMethod =
                        stringEncoding.getDeclaredMethod("encode",
                                char[].class, int.class, int.class);
                ReflectionUtils.makeMethodAccessible(encodeMethod);
                encodeMethodWithCharset =
                        stringEncoding.getDeclaredMethod("encode",
                                Charset.class, char[].class, int.class, int.class);
                ReflectionUtils.makeMethodAccessible(encodeMethodWithCharset);

                if (UnsafeUtils.hasUnsafe() && valueFieldOffset != -1) {
                    final Unsafe u = UnsafeUtils.getUnsafe();
                    final long offset = valueFieldOffset;
                    final Method m = encodeMethod;
                    en = sb -> {
                        try {
                            Object value = u.getObject(sb, offset);
                            return (byte[]) m.invoke(null, value, 0, sb.length());
                        } catch (Throwable t) {
                            ExceptionUtils.throwException(t);
                            // never reach
                            return null;
                        }
                    };

                    try {
                        final StringBuilder forTest = new StringBuilder("foo");
                        Checks.checkState(Arrays.equals(forTest.toString().getBytes(), en.apply(forTest)));
                    } catch (Throwable t) {
                        en = null;
                    }

                    final Method m1 = encodeMethodWithCharset;
                    en1 = (sb, cs) -> {
                        try {
                            Object value = u.getObject(sb, offset);
                            return (byte[]) m1.invoke(null, cs, value, 0, sb.length());
                        } catch (Throwable t) {
                            ExceptionUtils.throwException(t);
                            // never reach
                            return null;
                        }
                    };
                    try {
                        final StringBuilder forTest = new StringBuilder("foo");
                        Checks.checkState(Arrays.equals(forTest.toString().getBytes(StandardCharsets.UTF_8),
                                en1.apply(forTest, StandardCharsets.UTF_8)));
                    } catch (Throwable t) {
                        en1 = null;
                    }
                }

                if (en == null && valueField != null) {
                    final Field f = valueField;
                    final Method m = encodeMethod;
                    en = sb -> {
                        try {
                            Object value = f.get(sb);
                            return (byte[]) m.invoke(null, value, 0, sb.length());
                        } catch (Throwable t) {
                            ExceptionUtils.throwException(t);
                            // never reach
                            return null;
                        }
                    };

                    try {
                        final StringBuilder forTest = new StringBuilder("foo");
                        Checks.checkState(Arrays.equals(forTest.toString().getBytes(), en.apply(forTest)));
                    } catch (Throwable t) {
                        en = null;
                    }
                }
                if (en1 == null && valueField != null) {
                    final Field f = valueField;
                    final Method m1 = encodeMethodWithCharset;
                    en1 = (sb, cs) -> {
                        try {
                            Object value = f.get(sb);
                            return (byte[]) m1.invoke(null, cs, value, 0, sb.length());
                        } catch (Throwable t) {
                            ExceptionUtils.throwException(t);
                            // never reach
                            return null;
                        }
                    };

                    try {
                        final StringBuilder forTest = new StringBuilder("foo");
                        Checks.checkState(Arrays.equals(forTest.toString().getBytes(StandardCharsets.UTF_8),
                                en1.apply(forTest, StandardCharsets.UTF_8)));
                    } catch (Throwable t) {
                        en1 = null;
                    }
                }
            }
        } catch (Exception ignored) {
        }

        if (en == null) {
            ENCODER = sb -> sb.toString().getBytes();
        } else {
            ENCODER = en;
        }
        if (en1 == null) {
            ENCODER_WITH_CHARSET = (sb, cs) -> sb.toString().getBytes(cs);
        } else {
            ENCODER_WITH_CHARSET = en1;
        }
    }
}
