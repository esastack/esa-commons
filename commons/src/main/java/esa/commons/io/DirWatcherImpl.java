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

import esa.commons.Checks;
import esa.commons.ExceptionUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;
import java.util.function.Function;

class DirWatcherImpl extends AbstractPathWatcher {

    private final Map<WatchKey, DirInfo> watchKeyPathMap = new HashMap<>();
    private final Function<DirInfo, Boolean> recursionEndCondition;

    DirWatcherImpl(Path path,
                   int maxDepth,
                   Consumer<WatchEventContext<?>> create,
                   Consumer<WatchEventContext<?>> delete,
                   Consumer<WatchEventContext<?>> modify,
                   Consumer<WatchEventContext<?>> overflow,
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
        Checks.checkArg(maxDepth >= 0, "MaxDepth should be >= 0!");
        this.recursionEndCondition =
                dirInfo -> dirInfo.depth > maxDepth || !dirInfo.dir.isDirectory();
    }

    @Override
    void initDir(Path root) {
        if (!Files.exists(root, LinkOption.NOFOLLOW_LINKS)) {
            try {
                Files.createDirectories(root);
            } catch (IOException e) {
                ExceptionUtils.throwException(e);
            }
        } else if (!root.toFile().isDirectory()) {
            throw new IllegalStateException("Path(" + root + ") is not a directory!");
        }
    }

    @Override
    void register(Path root) {
        recursiveRegister(new DirInfo(root.toFile(), 0));
    }

    @Override
    File getFile(WatchEvent<?> event, WatchKey wk) {
        DirInfo dirInfo = watchKeyPathMap.get(wk);
        File file = new File(dirInfo.dir, event.context().toString());

        if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
            recursiveRegister(new DirInfo(file, dirInfo.depth + 1));
        }
        return file;
    }

    private void recursiveRegister(DirInfo dirInfo) {
        if (recursionEndCondition.apply(dirInfo)) {
            return;
        }

        File dir = dirInfo.dir;
        try {
            watchKeyPathMap.put(dir.toPath().register(watchService, kinds, modifiers), dirInfo);
        } catch (IOException e) {
            ExceptionUtils.throwException(e);
        }
        for (File childFile : Objects.requireNonNull(dir.listFiles(File::isDirectory))) {
            recursiveRegister(new DirInfo(childFile, dirInfo.depth + 1));
        }
    }

    private static final class DirInfo {
        private final File dir;
        private final int depth;

        private DirInfo(File dir, int depth) {
            this.dir = dir;
            this.depth = depth;
        }
    }

}
