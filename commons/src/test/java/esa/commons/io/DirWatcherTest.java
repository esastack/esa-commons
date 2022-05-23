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
import sun.security.action.GetPropertyAction;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.AccessController;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DirWatcherTest {

    private static final File tmpdir = new File(AccessController
            .doPrivileged(new GetPropertyAction("java.io.tmpdir")));
    private static final File dir = new File(tmpdir, "testWatch");
    private static final File zeroLevelDir = new File(dir, "zeroLevelDir");
    private static final File firstLevelDir = new File(zeroLevelDir, "firstLevelDir");
    private static final File firstLevelFile = new File(zeroLevelDir, "firstLevelFile");
    private static final File secondLevelFile = new File(firstLevelDir, "secondLevelFile");

    static {
        dir.deleteOnExit();
    }

    private final Semaphore semaphore = new Semaphore(0);
    private volatile Function<File, Boolean> semaphoreCondition = (file) -> false;

    @Test
    void testWatch() throws IOException, InterruptedException {
        testCreate();
        testModify();
        testDelete();
        delete(dir);
    }

    private void testCreate() throws InterruptedException, IOException {
        delete(dir);
        PathWatcher watcher = PathWatcher.watchDir(dir.toPath().toAbsolutePath(), 1)
                .onCreate((context) -> {
                    if (semaphoreCondition.apply(context.file())) {
                        semaphore.release();
                    }
                })
                .build();
        watcher.start();

        this.semaphoreCondition = (file) -> zeroLevelDir.getAbsolutePath().equals(file.getAbsolutePath());
        zeroLevelDir.mkdir();
        assertTrue(semaphore.tryAcquire(1000L, TimeUnit.MILLISECONDS));
        assertEquals(semaphore.drainPermits(), 0);

        this.semaphoreCondition = (file) -> firstLevelDir.getAbsolutePath().equals(file.getAbsolutePath());
        firstLevelDir.mkdir();
        assertTrue(semaphore.tryAcquire(1000L, TimeUnit.MILLISECONDS));
        assertEquals(semaphore.drainPermits(), 0);

        this.semaphoreCondition = (file) -> firstLevelFile.getAbsolutePath().equals(file.getAbsolutePath());
        firstLevelFile.createNewFile();
        assertTrue(semaphore.tryAcquire(1000L, TimeUnit.MILLISECONDS));
        assertEquals(semaphore.drainPermits(), 0);

        this.semaphoreCondition = (file) -> secondLevelFile.getAbsolutePath().equals(file.getAbsolutePath());
        secondLevelFile.createNewFile();
        assertFalse(semaphore.tryAcquire(300L, TimeUnit.MILLISECONDS));
        assertEquals(semaphore.drainPermits(), 0);
        assertTrue(watcher.stopAndWait(100L, TimeUnit.MILLISECONDS));
    }

    private void testModify() throws InterruptedException, IOException {
        zeroLevelDir.mkdir();
        firstLevelDir.mkdir();
        PathWatcher watcher = PathWatcher.watchDir(dir.toPath().toAbsolutePath(), 1)
                .onModify((context) -> {
                    if (semaphoreCondition.apply(context.file())) {
                        semaphore.release();
                    }
                })
                .build();
        watcher.start();

        this.semaphoreCondition = (file) -> firstLevelFile.getAbsolutePath().equals(file.getAbsolutePath());
        OutputStream stream = new FileOutputStream(firstLevelFile);
        stream.write(1);
        stream.close();
        assertTrue(semaphore.tryAcquire(1000L, TimeUnit.MILLISECONDS));
        assertEquals(semaphore.drainPermits(), 0);

        this.semaphoreCondition = (file) -> secondLevelFile.getAbsolutePath().equals(file.getAbsolutePath());
        stream = new FileOutputStream(secondLevelFile);
        stream.write(1);
        stream.close();
        assertFalse(semaphore.tryAcquire(300L, TimeUnit.MILLISECONDS));
        assertEquals(semaphore.drainPermits(), 0);
        assertTrue(watcher.stopAndWait(100L, TimeUnit.MILLISECONDS));
    }

    private void testDelete() throws InterruptedException, IOException {
        zeroLevelDir.mkdir();
        firstLevelDir.mkdir();
        firstLevelFile.createNewFile();
        secondLevelFile.createNewFile();
        PathWatcher watcher = PathWatcher.watchDir(dir.toPath().toAbsolutePath(), 1)
                .onDelete((context) -> {
                    if (semaphoreCondition.apply(context.file())) {
                        semaphore.release();
                    }
                })
                .build();
        watcher.start();

        this.semaphoreCondition = (file) -> firstLevelFile.getAbsolutePath().equals(file.getAbsolutePath());
        firstLevelFile.delete();
        assertTrue(semaphore.tryAcquire(1000L, TimeUnit.MILLISECONDS));
        assertEquals(semaphore.drainPermits(), 0);

        this.semaphoreCondition = (file) -> secondLevelFile.getAbsolutePath().equals(file.getAbsolutePath());
        secondLevelFile.delete();
        assertFalse(semaphore.tryAcquire(300L, TimeUnit.MILLISECONDS));
        assertEquals(semaphore.drainPermits(), 0);
        assertTrue(watcher.stopAndWait(100L, TimeUnit.MILLISECONDS));
    }

    public static boolean delete(File file) {
        // 如果dir对应的文件不存在，则退出
        if (!file.exists()) {
            return false;
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
}
