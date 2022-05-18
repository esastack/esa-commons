package esa.commons.io;

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
        this.path = path;
    }

    @Override
    void initDir(Path path) {
        if (!Files.exists(path, LinkOption.NOFOLLOW_LINKS)) {
            try {
                Files.createDirectories(path.getParent());
            } catch (IOException e) {
                throw new WatchException(e);
            }
        }
    }

    @Override
    void register(Path path) {
        try {
            path.getParent().register(watchService, kinds, modifiers);
        } catch (IOException e) {
            throw new WatchException(e);
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
