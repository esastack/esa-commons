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

import esa.commons.ExceptionUtils;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class EncoderImplTest {

    private static LogEvent logEvent =
            new LogEventImpl("logger", Level.INFO, "foo", new IllegalStateException("bar"));
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    @Test
    void testEncodeWithPattern() {
        assertArrayEquals("absent".getBytes(),
                new EncoderImpl("absent", null).encode(logEvent));
        assertArrayEquals("%absent".getBytes(),
                new EncoderImpl("%absent", null).encode(logEvent));

        // date
        assertArrayEquals(sdf.format(new Date(logEvent.timestamp())).getBytes(),
                new EncoderImpl("%d", null).encode(logEvent));
        assertArrayEquals(sdf.format(new Date(logEvent.timestamp())).getBytes(),
                new EncoderImpl("%date", null).encode(logEvent));
        assertArrayEquals(new SimpleDateFormat("yyyy-MM-dd").format(new Date(logEvent.timestamp())).getBytes(),
                new EncoderImpl("%d{yyyy-MM-dd}", null).encode(logEvent));
        assertArrayEquals(new SimpleDateFormat("yyyy-MM-dd").format(new Date(logEvent.timestamp())).getBytes(),
                new EncoderImpl("%date{yyyy-MM-dd}", null).encode(logEvent));
        assertArrayEquals("%d".getBytes(),
                new EncoderImpl("\\%d", null).encode(logEvent));
        assertArrayEquals(("\\" + sdf.format(new Date(logEvent.timestamp()))).getBytes(),
                new EncoderImpl("\\\\%d", null).encode(logEvent));

        // level
        assertArrayEquals(logEvent.level().toString().getBytes(),
                new EncoderImpl("%l", null).encode(logEvent));
        assertArrayEquals(logEvent.level().toString().getBytes(),
                new EncoderImpl("%level", null).encode(logEvent));

        // thread
        assertArrayEquals(logEvent.threadName().getBytes(),
                new EncoderImpl("%t", null).encode(logEvent));
        assertArrayEquals(logEvent.threadName().getBytes(),
                new EncoderImpl("%thread", null).encode(logEvent));

        // message
        assertArrayEquals(logEvent.message().getBytes(),
                new EncoderImpl("%m", null).encode(logEvent));
        assertArrayEquals(logEvent.message().getBytes(),
                new EncoderImpl("%msg", null).encode(logEvent));
        assertArrayEquals(logEvent.message().getBytes(),
                new EncoderImpl("%message", null).encode(logEvent));

        // line
        assertArrayEquals(System.getProperty("line.separator").getBytes(),
                new EncoderImpl("%n", null).encode(logEvent));

        // thrown
        assertArrayEquals(ExceptionUtils.getStackTrace(logEvent.thrown()).getBytes(),
                new EncoderImpl("%ex", null).encode(logEvent));
        assertArrayEquals(ExceptionUtils.getStackTrace(logEvent.thrown()).getBytes(),
                new EncoderImpl("%exception", null).encode(logEvent));
        assertArrayEquals(ExceptionUtils.getStackTrace(logEvent.thrown()).getBytes(),
                new EncoderImpl("%thrown", null).encode(logEvent));
    }

    @Test
    void testCharset() {
        assertArrayEquals("absent".getBytes(StandardCharsets.UTF_8),
                new EncoderImpl("absent", StandardCharsets.UTF_8).encode(logEvent));
    }

}
