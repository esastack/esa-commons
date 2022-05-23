/*
 * Copyright 2022 OPPO ESA Stack Project
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
package esa.commons.io;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileWatcherTest {

    private static final String FILE_PREFIX = "file-watcher-test";

    @Test
    void testStartAndStop() throws InterruptedException, IOException {
        final Semaphore semaphore = new Semaphore(0);
        final File file = FileTestUtils.newTemp(FILE_PREFIX, "test-start-and-Stop");
        PathWatcher watcher = PathWatcher.watchFile(file.toPath().toAbsolutePath())
                .onCreate((context) -> semaphore.release())
                .build();
        watcher.start();
        assertThrows(IllegalStateException.class, () -> watcher.start());
        assertTrue(watcher.stopAndWait(100L, TimeUnit.MILLISECONDS));
        assertTrue(watcher.stopAndWait(100L, TimeUnit.MILLISECONDS));
        assertThrows(IllegalStateException.class, () -> watcher.start());

        PathWatcher watcher1 = PathWatcher.watchFile(file.toPath().toAbsolutePath())
                .onCreate((context) -> semaphore.release())
                .build();
        assertTrue(watcher1.stopAndWait(100L, TimeUnit.MILLISECONDS));
        assertThrows(IllegalStateException.class, () -> watcher1.start());
    }

    @Test
    void testWatch() throws IOException, InterruptedException {
        final File file = FileTestUtils.newTemp(FILE_PREFIX, "test-watch");
        testCreate(file);
        testModify(file);
        testDelete(file);
    }

    @Test
    void testDelayWatch() throws IOException, InterruptedException {
        final File file = FileTestUtils.newTemp(FILE_PREFIX, "test-delay-watch");
        testDelayCreate(file);
        testDelayModify(file);
        testDelayDelete(file);
    }

    private void testCreate(File file) throws InterruptedException, IOException {
        final Semaphore semaphore = new Semaphore(0);
        if (file.exists()) {
            file.delete();
        }
        PathWatcher watcher = PathWatcher.watchFile(file.toPath().toAbsolutePath())
                .onCreate((context) -> semaphore.release())
                .build();
        watcher.start();
        file.createNewFile();
        assertTrue(semaphore.tryAcquire(1000L, TimeUnit.MILLISECONDS));
        assertEquals(semaphore.drainPermits(), 0);
        assertTrue(watcher.stopAndWait(100L, TimeUnit.MILLISECONDS));
    }

    private void testModify(File file) throws InterruptedException, IOException {
        final Semaphore semaphore = new Semaphore(0);
        if (!file.exists()) {
            file.createNewFile();
        }
        PathWatcher watcher = PathWatcher.watchFile(file.toPath().toAbsolutePath())
                .onModify((context) -> semaphore.release())
                .build();
        watcher.start();
        try (OutputStream stream = new FileOutputStream(file)) {
            stream.write(1);
        }
        assertTrue(semaphore.tryAcquire(1000L, TimeUnit.MILLISECONDS));
        assertEquals(semaphore.drainPermits(), 0);
        assertTrue(watcher.stopAndWait(100L, TimeUnit.MILLISECONDS));
    }

    private void testDelete(File file) throws InterruptedException, IOException {
        final Semaphore semaphore = new Semaphore(0);
        if (!file.exists()) {
            file.createNewFile();
        }
        PathWatcher watcher = PathWatcher.watchFile(file.toPath().toAbsolutePath())
                .onDelete((context) -> semaphore.release())
                .build();
        watcher.start();
        file.delete();
        assertTrue(semaphore.tryAcquire(1000L, TimeUnit.MILLISECONDS));
        assertEquals(semaphore.drainPermits(), 0);
        assertTrue(watcher.stopAndWait(100L, TimeUnit.MILLISECONDS));
    }

    private void testDelayCreate(File file) throws InterruptedException, IOException {
        final Semaphore semaphore = new Semaphore(0);
        if (file.exists()) {
            file.delete();
        }
        PathWatcher watcher = PathWatcher.watchFile(file.toPath().toAbsolutePath())
                .onCreate((context) -> semaphore.release())
                .delay(500L)
                .build();
        watcher.start();
        file.createNewFile();
        assertFalse(semaphore.tryAcquire(250L, TimeUnit.MILLISECONDS));
        assertTrue(semaphore.tryAcquire(300L, TimeUnit.MILLISECONDS));
        assertEquals(semaphore.drainPermits(), 0);
        assertTrue(watcher.stopAndWait(100L, TimeUnit.MILLISECONDS));
    }

    private void testDelayModify(File file) throws InterruptedException, IOException {
        final Semaphore semaphore = new Semaphore(0);
        if (!file.exists()) {
            file.createNewFile();
        }
        PathWatcher watcher = PathWatcher.watchFile(file.toPath().toAbsolutePath())
                .onModify((context) -> semaphore.release())
                .delay(500L)
                .build();
        watcher.start();
        try (OutputStream stream = new FileOutputStream(file)) {
            stream.write(1);
        }
        assertFalse(semaphore.tryAcquire(250L, TimeUnit.MILLISECONDS));
        assertTrue(semaphore.tryAcquire(300L, TimeUnit.MILLISECONDS));
        assertEquals(semaphore.drainPermits(), 0);
        assertTrue(watcher.stopAndWait(100L, TimeUnit.MILLISECONDS));
    }

    private void testDelayDelete(File file) throws InterruptedException, IOException {
        final Semaphore semaphore = new Semaphore(0);
        if (!file.exists()) {
            file.createNewFile();
        }
        PathWatcher watcher = PathWatcher.watchFile(file.toPath().toAbsolutePath())
                .onDelete((context) -> semaphore.release())
                .delay(500L)
                .build();
        watcher.start();
        file.delete();
        assertFalse(semaphore.tryAcquire(250L, TimeUnit.MILLISECONDS));
        assertTrue(semaphore.tryAcquire(300L, TimeUnit.MILLISECONDS));
        assertEquals(semaphore.drainPermits(), 0);
        assertTrue(watcher.stopAndWait(100L, TimeUnit.MILLISECONDS));
    }
}
