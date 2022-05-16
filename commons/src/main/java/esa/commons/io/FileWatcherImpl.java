package esa.commons.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;

class FileWatcherImpl extends AbstractPathWatcher {

    private final Path path;

    FileWatcherImpl(Path path,
                    Consumer<WatchEventContext<?>> create,
                    Consumer<WatchEventContext<?>> delete,
                    Consumer<WatchEventContext<?>> modify,
                    Consumer<WatchEventContext<?>> overflow,
                    WatchEvent.Modifier[] modifiers,
                    long modifyDelay,
                    Executor executor,
                    ScheduledExecutorService modifyDelayScheduler) {
        super(path,
                create,
                delete,
                modify,
                overflow,
                modifiers,
                modifyDelay,
                executor,
                modifyDelayScheduler);
        this.path = path;
    }

    @Override
    File getFile(WatchEvent<?> event, WatchKey wk) {
        return path.toFile();
    }

    @Override
    void initDir(Path root) {
        try {
            if (!Files.exists(path, LinkOption.NOFOLLOW_LINKS)) {
                Files.createDirectories(path.getParent());
            }
        } catch (IOException e) {
            throw new WatchException(e);
        }
    }

    @Override
    void register(Path root) {
        try {
            root.getParent().register(watchService, kinds, modifiers);
        } catch (IOException e) {
            throw new WatchException(e);
        }
    }
}
