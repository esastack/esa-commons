/*
 * Copyright 2022 OPPO ESA Stack Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this dir except in compliance with the License.
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
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DirWatcherTest {

    private static final long TIMEOUT = 5000L;

    @Test
    void testWatch() throws IOException, InterruptedException {
        final File dir = FileTestUtils.newTemp("dir-watcher-test", "test-watch");
        final File zeroLevelDir = new File(dir, "zeroLevelDir");
        final File firstLevelDir = new File(zeroLevelDir, "firstLevelDir");
        final File firstLevelFile = new File(zeroLevelDir, "firstLevelFile");
        final File secondLevelFile = new File(firstLevelDir, "secondLevelFile");
        final FileSemaphore semaphore = new FileSemaphore();
        try {
            testCreate(dir,
                    zeroLevelDir,
                    firstLevelDir,
                    firstLevelFile,
                    secondLevelFile,
                    semaphore);

            testModify(dir,
                    zeroLevelDir,
                    firstLevelDir,
                    firstLevelFile,
                    secondLevelFile,
                    semaphore);

            testDelete(dir,
                    zeroLevelDir,
                    firstLevelDir,
                    firstLevelFile,
                    secondLevelFile,
                    semaphore);
        } finally {
            delete(dir);
        }
    }

    private void testCreate(File dir,
                            File zeroLevelDir,
                            File firstLevelDir,
                            File firstLevelFile,
                            File secondLevelFile,
                            FileSemaphore semaphore) throws InterruptedException, IOException {
        delete(dir);
        PathWatcher watcher = PathWatcher.watchDir(dir.toPath().toAbsolutePath(), 1)
                .onCreate((context) -> semaphore.conditionalRelease(context.file()))
                .build();
        watcher.start();

        semaphore.condition((file) -> zeroLevelDir.getAbsolutePath().equals(file.getAbsolutePath()));
        zeroLevelDir.mkdir();
        assertTrue(semaphore.unWrap().tryAcquire(TIMEOUT, TimeUnit.MILLISECONDS));
        assertEquals(semaphore.unWrap().drainPermits(), 0);

        semaphore.condition((file) -> firstLevelDir.getAbsolutePath().equals(file.getAbsolutePath()));
        firstLevelDir.mkdir();
        assertTrue(semaphore.unWrap().tryAcquire(TIMEOUT, TimeUnit.MILLISECONDS));
        assertEquals(semaphore.unWrap().drainPermits(), 0);

        semaphore.condition((file) -> firstLevelFile.getAbsolutePath().equals(file.getAbsolutePath()));
        firstLevelFile.createNewFile();
        assertTrue(semaphore.unWrap().tryAcquire(TIMEOUT, TimeUnit.MILLISECONDS));
        assertEquals(semaphore.unWrap().drainPermits(), 0);

        semaphore.condition((file) -> secondLevelFile.getAbsolutePath().equals(file.getAbsolutePath()));
        secondLevelFile.createNewFile();
        assertFalse(semaphore.unWrap().tryAcquire(500L, TimeUnit.MILLISECONDS));
        assertEquals(semaphore.unWrap().drainPermits(), 0);
        assertTrue(watcher.stopAndWait(TIMEOUT, TimeUnit.MILLISECONDS));
    }

    private void testModify(File dir,
                            File zeroLevelDir,
                            File firstLevelDir,
                            File firstLevelFile,
                            File secondLevelFile,
                            FileSemaphore semaphore) throws InterruptedException, IOException {
        zeroLevelDir.mkdir();
        firstLevelDir.mkdir();
        PathWatcher watcher = PathWatcher.watchDir(dir.toPath().toAbsolutePath(), 1)
                .onModify((context) -> semaphore.conditionalRelease(context.file()))
                .build();
        watcher.start();

        semaphore.condition((file) -> firstLevelFile.getAbsolutePath().equals(file.getAbsolutePath()));
        try (OutputStream stream = new FileOutputStream(firstLevelFile)) {
            stream.write(1);
        }
        assertTrue(semaphore.unWrap().tryAcquire(TIMEOUT, TimeUnit.MILLISECONDS));
        assertEquals(semaphore.unWrap().drainPermits(), 0);

        semaphore.condition((file) -> secondLevelFile.getAbsolutePath().equals(file.getAbsolutePath()));
        try (OutputStream stream = new FileOutputStream(secondLevelFile)) {
            stream.write(1);
        }
        assertFalse(semaphore.unWrap().tryAcquire(500L, TimeUnit.MILLISECONDS));
        assertEquals(semaphore.unWrap().drainPermits(), 0);
        assertTrue(watcher.stopAndWait(TIMEOUT, TimeUnit.MILLISECONDS));
    }

    private void testDelete(File dir,
                            File zeroLevelDir,
                            File firstLevelDir,
                            File firstLevelFile,
                            File secondLevelFile,
                            FileSemaphore semaphore) throws InterruptedException, IOException {
        zeroLevelDir.mkdir();
        firstLevelDir.mkdir();
        firstLevelFile.createNewFile();
        secondLevelFile.createNewFile();
        PathWatcher watcher = PathWatcher.watchDir(dir.toPath().toAbsolutePath(), 1)
                .onDelete((context) -> semaphore.conditionalRelease(context.file()))
                .build();
        watcher.start();

        semaphore.condition((file) -> firstLevelFile.getAbsolutePath().equals(file.getAbsolutePath()));
        firstLevelFile.delete();
        assertTrue(semaphore.unWrap().tryAcquire(TIMEOUT, TimeUnit.MILLISECONDS));
        assertEquals(semaphore.unWrap().drainPermits(), 0);

        semaphore.condition((file) -> secondLevelFile.getAbsolutePath().equals(file.getAbsolutePath()));
        secondLevelFile.delete();
        assertFalse(semaphore.unWrap().tryAcquire(500L, TimeUnit.MILLISECONDS));
        assertEquals(semaphore.unWrap().drainPermits(), 0);
        assertTrue(watcher.stopAndWait(TIMEOUT, TimeUnit.MILLISECONDS));
    }

    private static boolean delete(File file) {
        //If file not exist,then exit directly
        if (!file.exists()) {
            return true;
        }

        if (file.isFile()) {
            return file.delete();
        } else {
            for (File tem : file.listFiles()) {
                delete(tem);
            }
        }

        return file.delete();
    }

    private static final class FileSemaphore {
        private final Semaphore semaphore = new Semaphore(0);
        private volatile Function<File, Boolean> releaseCondition = (file) -> false;

        private void condition(Function<File, Boolean> releaseCondition) {
            this.releaseCondition = releaseCondition;
        }

        private Semaphore unWrap() {
            return semaphore;
        }

        private void conditionalRelease(File file) {
            if (releaseCondition.apply(file)) {
                semaphore.release();
            }
        }
    }

}
