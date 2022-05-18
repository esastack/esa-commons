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
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;
import java.util.function.Function;

public class DirWatcherImpl extends AbstractPathWatcher {

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
                throw new WatchException(e);
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
            throw new WatchException(e);
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
