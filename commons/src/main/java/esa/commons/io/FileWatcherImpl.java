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

import esa.commons.ExceptionUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;

class FileWatcherImpl extends AbstractPathWatcher {

    private final Path path;

    FileWatcherImpl(Path path,
                    Consumer<WatchEventContext> create,
                    Consumer<WatchEventContext> delete,
                    Consumer<WatchEventContext> modify,
                    Consumer<WatchEventContext> overflow,
                    WatchEvent.Modifier[] modifiers,
                    long delay,
                    ScheduledExecutorService delayScheduler) {
        super(path,
                create,
                delete,
                modify,
                overflow,
                modifiers,
                delay,
                delayScheduler);
        this.path = path;
    }

    @Override
    void initDir(Path path) {
        if (!Files.exists(path, LinkOption.NOFOLLOW_LINKS)) {
            try {
                Files.createDirectories(path.getParent());
            } catch (IOException e) {
                ExceptionUtils.throwException(e);
            }
        }
    }

    @Override
    void register(Path path) {
        try {
            path.getParent().register(watchService, kinds, modifiers);
        } catch (IOException e) {
            ExceptionUtils.throwException(e);
        }
    }

    @Override
    File getFile(WatchEvent<?> event, WatchKey wk) {
        if (path.endsWith(event.context().toString())) {
            return path.toFile();
        } else {
            return null;
        }
    }
}
