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

import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

/**
 * The pathWatcher can watch to files or directories. If the watched path does not exist,
 * PathWatcher will create an empty directory recursively and watch to the empty directory
 */
public interface PathWatcher {

    void start();

    boolean stopAndWait(long timeout, TimeUnit unit) throws InterruptedException;

    static PathWatcherBuilder watchFile(Path file) {
        return new PathWatcherBuilder(file, false, 0);
    }

    static PathWatcherBuilder watchDir(Path dir, int maxDepth) {
        return new PathWatcherBuilder(dir, true, maxDepth);
    }
}
