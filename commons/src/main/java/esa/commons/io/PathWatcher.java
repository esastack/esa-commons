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

import java.io.File;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.util.concurrent.TimeUnit;

/**
 * The pathWatcher can watch to files or directories. If the watched path does not exist,
 * PathWatcher will create an empty directory recursively and watch to the empty directory
 */
public interface PathWatcher {

    /**
     * Create PathWatcherBuilder of file.
     *
     * @param file file
     * @return PathWatcherBuilder
     */
    static PathWatcherBuilder watchFile(Path file) {
        return new PathWatcherBuilder(file, false, 0);
    }

    /**
     * Create PathWatcherBuilder of directory with the maximum depth of recursive directory.
     *
     * @param dir      directory
     * @param maxDepth the maximum depth of recursive directory, 0 represents watch the
     *                 first level files in the directory.
     * @return PathWatcherBuilder
     */
    static PathWatcherBuilder watchDir(Path dir, int maxDepth) {
        return new PathWatcherBuilder(dir, true, maxDepth);
    }

    /**
     * Start watch. If you want to stop then call stopAndWait().
     */
    void start();

    /**
     * Stop and blocks until all tasks have completed execution after a shutdownNow
     * request, or the timeout occurs, or the current thread is interrupted, whichever
     * happens first.
     *
     * @param timeout the maximum time to wait
     * @param unit    the time unit of the timeout argument
     * @return {@code true} if this watcher stopped and
     * {@code false} if the timeout elapsed before stop
     * @throws InterruptedException if interrupted while waiting
     */
    boolean stopAndWait(long timeout, TimeUnit unit) throws InterruptedException;

    interface WatchEventContext {
        WatchEvent<?> event();

        File file();
    }

    class WatchEventContextImpl implements WatchEventContext {

        private final WatchEvent<?> event;
        private final File file;

        WatchEventContextImpl(WatchEvent<?> event, File file) {
            this.event = event;
            this.file = file;
        }

        @Override
        public WatchEvent<?> event() {
            return event;
        }

        @Override
        public File file() {
            return file;
        }

        @Override
        public String toString() {
            return "WatchEventContextImpl{" +
                    "event=" + event +
                    ", file=" + file +
                    '}';
        }
    }

}
