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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class RollingFileAppenderTest {

    private static File dir;
    private File target;
    private String fileNamePrefix;
    private File targetWithOutExe;

    @BeforeAll
    static void before() {
        File f = null;
        try {
            f = File.createTempFile("commons-test-", ".log");
            f.deleteOnExit();
            dir = f.getParentFile();
        } catch (Exception ignored) {
            dir = null;
        } finally {
            if (f != null) {
                f.delete();
            }
        }
        assumeTrue(dir != null && dir.isDirectory() && dir.exists());

    }

    @BeforeEach
    void buildFile() throws IOException {
        target = File.createTempFile("commons-test-", ".log");
        fileNamePrefix = target.getName().substring(0, target.getName().lastIndexOf("."));
        targetWithOutExe = new File(dir, fileNamePrefix + "_log");
        boolean ok = true;
        try {
            if (target.exists()) {
                ok = target.delete();
            }
            ok &= target.createNewFile();
            if (targetWithOutExe.exists()) {
                ok &= targetWithOutExe.delete();
            }
            ok &= targetWithOutExe.createNewFile();
        } catch (IOException e) {
            ok = false;
        }
        assumeTrue(ok);
    }

    @AfterEach
    void deleteFie() {
        target.delete();
        targetWithOutExe.delete();
    }

    @Test
    void testAppend() throws IOException {
        final RollingFileAppender appender = RollingFileAppender.newInstance(target, null);
        final byte[] bytes = "foo".getBytes();
        appender.append(ByteBuffer.wrap(bytes));
        appender.close();
        assertArrayEquals(bytes, IOUtils.toByteArray(target));

        final RollingFileAppender appender1 = RollingFileAppender.newInstance(targetWithOutExe, null);
        appender1.append(ByteBuffer.wrap(bytes));
        appender1.close();
        assertArrayEquals(bytes, IOUtils.toByteArray(targetWithOutExe));
    }

    @Test
    void testSizeBasedRollingWithSuffix() throws IOException {

        final List<File> files = new LinkedList<>();

        try {
            final RollingFileAppender.SizedBasedRolling rolling =
                    new RollingFileAppender.SizedBasedRolling(target, 2, 16);
            assertNull(rolling.rolloverIfNecessary(target, 1L));
            final File r = rolling.rolloverIfNecessary(target, 17L);
            assertNotNull(r);
            files.add(r);

            assertEquals(target.getParent(), r.getParent());
            assertEquals(fileNamePrefix + ".1.log", r.getName());
            assertNull(rolling.removeOldestFuture);
            r.createNewFile();
            r.deleteOnExit();
            File[] rollingFiles = rolling.getRollingFiles();
            assertEquals(1, rollingFiles.length);
            assertEquals(r, rollingFiles[0]);

            final File r1 = rolling.rolloverIfNecessary(target, 17L);
            assertNotNull(r1);
            files.add(r1);

            assertEquals(target.getParent(), r1.getParent());
            assertEquals(fileNamePrefix + ".2.log", r1.getName());
            assertNull(rolling.removeOldestFuture);
            r1.createNewFile();
            r1.deleteOnExit();

            rollingFiles = rolling.getRollingFiles();
            assertEquals(2, rollingFiles.length);
            assertEquals(r1, rollingFiles[1]);

            final File r2 = rolling.rolloverIfNecessary(target, 17L);
            assertNotNull(r2);
            files.add(r2);

            assertEquals(target.getParent(), r2.getParent());
            assertEquals(fileNamePrefix + ".3.log", r2.getName());
            assertNotNull(rolling.removeOldestFuture);
            while (!rolling.removeOldestFuture.isDone()) {
            }

            rollingFiles = rolling.getRollingFiles();
            assertEquals(1, rollingFiles.length);
            assertEquals(r1, rollingFiles[0]);

            assertFalse(r.exists());
            assertTrue(r1.exists());
        } finally {
            files.forEach(File::delete);
        }
    }

    @Test
    void testSizeBasedRollingWithoutSuffix() throws IOException {
        final List<File> files = new LinkedList<>();

        try {
            final RollingFileAppender.SizedBasedRolling rolling1 =
                    new RollingFileAppender.SizedBasedRolling(targetWithOutExe, 2, 16);
            assertNull(rolling1.rolloverIfNecessary(target, 1L));
            final File r = rolling1.rolloverIfNecessary(target, 17L);
            assertNotNull(r);
            files.add(r);

            assertEquals(targetWithOutExe.getParent(), r.getParent());
            assertEquals(fileNamePrefix + "_log.1", r.getName());
            assertNull(rolling1.removeOldestFuture);
            r.createNewFile();
            r.deleteOnExit();

            File[] rollingFiles = rolling1.getRollingFiles();
            assertEquals(1, rollingFiles.length);
            assertEquals(r, rollingFiles[0]);

            final File r1 = rolling1.rolloverIfNecessary(targetWithOutExe, 17L);
            assertNotNull(r1);
            files.add(r1);

            assertEquals(targetWithOutExe.getParent(), r1.getParent());
            assertEquals(fileNamePrefix + "_log.2", r1.getName());
            assertNull(rolling1.removeOldestFuture);
            r1.createNewFile();
            r1.deleteOnExit();

            rollingFiles = rolling1.getRollingFiles();
            assertEquals(2, rollingFiles.length);
            assertEquals(r, rollingFiles[0]);
            assertEquals(r1, rollingFiles[1]);

            final File r2 = rolling1.rolloverIfNecessary(target, 17L);
            assertNotNull(r2);
            files.add(r2);

            assertEquals(targetWithOutExe.getParent(), r2.getParent());
            assertEquals(fileNamePrefix + "_log.3", r2.getName());
            assertNotNull(rolling1.removeOldestFuture);
            while (!rolling1.removeOldestFuture.isDone()) {
            }

            rollingFiles = rolling1.getRollingFiles();
            assertEquals(1, rollingFiles.length);
            assertEquals(r1, rollingFiles[0]);

            assertFalse(r.exists());
            assertTrue(r1.exists());
        } finally {
            files.forEach(File::delete);
        }
    }

}
