package esa.commons.io;

import esa.commons.Checks;

import java.io.File;
import java.nio.file.WatchEvent;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;

public class FileWatcherBuilder {

    private final File file;
    private Consumer<WatchEvent<?>> create;
    private Consumer<WatchEvent<?>> delete;
    private Consumer<WatchEvent<?>> modify;
    private Consumer<WatchEvent<?>> overflow;
    private Executor executor;
    private ScheduledExecutorService modifyDelayScheduler;
    private WatchEvent.Modifier[] modifiers;
    private long modifyDelay = 0;

    FileWatcherBuilder(File file) {
        Checks.checkNotNull(file, "file");
        this.file = file;
    }

    public FileWatcherBuilder onCreate(Consumer<WatchEvent<?>> create) {
        this.create = create;
        return this;
    }

    public FileWatcherBuilder onDelete(Consumer<WatchEvent<?>> delete) {
        this.delete = delete;
        return this;
    }

    public FileWatcherBuilder onModify(Consumer<WatchEvent<?>> modify) {
        this.modify = modify;
        return this;
    }

    public FileWatcherBuilder onOverflow(Consumer<WatchEvent<?>> overflow) {
        this.overflow = overflow;
        return this;
    }

    public FileWatcherBuilder modifyDelay(long modifyDelay) {
        return modifyDelay(modifyDelay, null);
    }

    public FileWatcherBuilder modifyDelay(long modifyDelay, ScheduledExecutorService modifyDelayScheduler) {
        Checks.checkArg(modifyDelay >= 0, "modifyDelay should be >= 0.");
        this.modifyDelay = modifyDelay;
        this.modifyDelayScheduler = modifyDelayScheduler;
        return this;
    }

    public FileWatcherBuilder modifiers(WatchEvent.Modifier[] modifiers) {
        Checks.checkNotNull(modifiers, "modifiers");
        this.modifiers = modifiers;
        return this;
    }

    public FileWatcherBuilder customExecutor(Executor executor) {
        Checks.checkNotNull(executor, "executor");
        this.executor = executor;
        return this;
    }

    public FileWatcher build() {
        return new FileWatcherImpl(file,
                create,
                delete,
                modify,
                overflow,
                modifiers,
                modifyDelay,
                executor,
                modifyDelayScheduler);
    }
}
