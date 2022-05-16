package esa.commons.io;

import esa.commons.Checks;

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
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;

public class DirWatcherImpl extends AbstractPathWatcher {

    private final Map<WatchKey, DirInfo> watchKeyPathMap = new HashMap<>();
    private final int maxDepth;

    DirWatcherImpl(Path path,
                   int maxDepth,
                   Consumer<WatchEventContext<?>> create,
                   Consumer<WatchEventContext<?>> delete,
                   Consumer<WatchEventContext<?>> modify,
                   Consumer<WatchEventContext<?>> overflow,
                   WatchEvent.Modifier[] modifiers,
                   long delay,
                   Executor executor,
                   ScheduledExecutorService delayScheduler) {
        super(path,
                create,
                delete,
                modify,
                overflow,
                modifiers,
                delay,
                executor,
                delayScheduler);
        Checks.checkArg(maxDepth >= 0, "MaxDepth should be >= 0!");
        this.maxDepth = maxDepth;
    }

    @Override
    File getFile(WatchEvent<?> event, WatchKey wk) {
        DirInfo dirInfo = watchKeyPathMap.get(wk);
        File file = new File(dirInfo.dir, event.context().toString());

        if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
            if (file.isDirectory()) {
                register(file, dirInfo.currentDepth + 1);
            }
        } else if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
            //Folder modification events are not notified
            if (file.isDirectory()) {
                return null;
            }
        }
        return file;
    }

    @Override
    void initDir(Path root) {
        try {
            if (!Files.exists(root, LinkOption.NOFOLLOW_LINKS)) {
                Files.createDirectories(root);
            }
        } catch (IOException e) {
            throw new WatchException(e);
        }
    }

    @Override
    void register(Path root) {
        File file = root.toFile();
        if (!file.exists()) {
            throw new IllegalStateException("File(" + file + ") doesn't exist!");
        }
        if (!file.isDirectory()) {
            throw new IllegalStateException("File(" + file + ") should be a directory!");
        }
        register(file, 0);
    }

    private void register(File file,
                          int currentDepth) {

        if (currentDepth > maxDepth) {
            return;
        }

        try {
            final WatchKey key;

            key = file.toPath().register(watchService, kinds, modifiers);
            watchKeyPathMap.put(key, new DirInfo(file, currentDepth));
            for (File childFile : Objects.requireNonNull(file.listFiles(File::isDirectory))) {
                if (childFile.isDirectory()) {
                    register(childFile, currentDepth + 1);
                }
            }
        } catch (IOException e) {
            throw new WatchException(e);
        }
    }

    private static final class DirInfo {
        private final File dir;
        private final int currentDepth;

        private DirInfo(File dir, int currentDepth) {
            this.dir = dir;
            this.currentDepth = currentDepth;
        }
    }

}
