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

import esa.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InternalLoggersTest {

    @Test
    void testWriteLog() throws IOException {
        File file = createTempFile();
        testWriteLog0(InternalLoggers.logger("foo", file), file, "foo");

        file = createTempFile();
        testWriteLog0(InternalLoggers.logger(InternalLoggersTest.class, file), file,
                InternalLoggersTest.class.getName());

        file = createTempFile();
        testWriteLog0(InternalLoggers.logger("bar", file.getAbsolutePath()), file, "bar");
        file = createTempFile();
        testWriteLog0(InternalLoggers.logger(InternalLoggers.class,
                file.getAbsolutePath()),
                file,
                InternalLoggers.class.getName());
    }

    private static File createTempFile() throws IOException {
        final File target = File.createTempFile("commons-test-", ".log");
        target.deleteOnExit();
        return target;
    }

    private static void testWriteLog0(InternalLoggers.Builder builder,
                                      File target,
                                      String name) throws IOException {
        try {
            final InternalLogger logger = builder
                    .writeBuffer(16)
                    .pattern("%msg")
                    .charset(StandardCharsets.UTF_8)
                    .queue(32)
                    .build();
            assertEquals(name, logger.name());
            logger.setLevel(Level.INFO);
            final StringBuilder check = new StringBuilder(32 * 6);
            for (int i = 0; i < 30; i++) {
                logger.info("info" + i);
                check.append("info").append(i);
            }
            for (int i = 0; i < 30; i++) {
                logger.debug("debug" + i);
            }
            InternalLoggers.Manager.LOGGER_HANDLERS.remove(target).stop();

            final String v = IOUtils.readFileAsString(target, StandardCharsets.UTF_8);
            assertTrue(v.isEmpty() || check.toString().startsWith(v));
            assertFalse(v.contains("debug"));
        } finally {
            target.delete();
        }
    }

}
