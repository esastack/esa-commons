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
import sun.security.action.GetPropertyAction;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.AccessController;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileWatcherTest {

    private static final File tmpdir = new File(AccessController
            .doPrivileged(new GetPropertyAction("java.io.tmpdir")));
    private static final File file = new File(tmpdir, "testWatch");

    static {
        file.deleteOnExit();
    }

    private final Semaphore semaphore = new Semaphore(0);

    @Test
    void testStartAndStop() throws InterruptedException {
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
        testCreate();
        testModify();
        testDelete();
    }

    @Test
    void testDelayWatch() throws IOException, InterruptedException {
        testDelayCreate();
        testDelayModify();
        testDelayDelete();
    }

    private void testCreate() throws InterruptedException, IOException {
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

    private void testModify() throws InterruptedException, IOException {
        if (!file.exists()) {
            file.createNewFile();
        }
        PathWatcher watcher = PathWatcher.watchFile(file.toPath().toAbsolutePath())
                .onModify((context) -> semaphore.release())
                .build();
        watcher.start();
        OutputStream stream = new FileOutputStream(file);
        stream.write(1);
        stream.close();
        assertTrue(semaphore.tryAcquire(1000L, TimeUnit.MILLISECONDS));
        assertEquals(semaphore.drainPermits(), 0);
        assertTrue(watcher.stopAndWait(100L, TimeUnit.MILLISECONDS));
    }

    private void testDelete() throws InterruptedException, IOException {
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

    private void testDelayCreate() throws InterruptedException, IOException {
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

    private void testDelayModify() throws InterruptedException, IOException {
        if (!file.exists()) {
            file.createNewFile();
        }
        PathWatcher watcher = PathWatcher.watchFile(file.toPath().toAbsolutePath())
                .onModify((context) -> semaphore.release())
                .delay(500L)
                .build();
        watcher.start();
        OutputStream stream = new FileOutputStream(file);
        stream.write(1);
        stream.close();
        assertFalse(semaphore.tryAcquire(250L, TimeUnit.MILLISECONDS));
        assertTrue(semaphore.tryAcquire(300L, TimeUnit.MILLISECONDS));
        assertEquals(semaphore.drainPermits(), 0);
        assertTrue(watcher.stopAndWait(100L, TimeUnit.MILLISECONDS));
    }

    private void testDelayDelete() throws InterruptedException, IOException {
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
