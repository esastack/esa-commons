package esa.commons.io;

import esa.commons.Checks;

import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;

public class PathWatcherBuilder {

    private final Path path;
    private final boolean isDir;
    private Consumer<WatchEventContext<?>> create;
    private Consumer<WatchEventContext<?>> delete;
    private Consumer<WatchEventContext<?>> modify;
    private Consumer<WatchEventContext<?>> overflow;
    private Executor executor;
    private ScheduledExecutorService modifyDelayScheduler;
    private WatchEvent.Modifier[] modifiers;
    private long modifyDelay = 200;

    public PathWatcherBuilder(Path path, boolean isDir) {
        Checks.checkNotNull(path, "path");
        this.path = path;
        this.isDir = isDir;
    }

    public PathWatcherBuilder onCreate(Consumer<WatchEventContext<?>> create) {
        this.create = create;
        return this;
    }

    public PathWatcherBuilder onDelete(Consumer<WatchEventContext<?>> delete) {
        this.delete = delete;
        return this;
    }

    public PathWatcherBuilder onModify(Consumer<WatchEventContext<?>> modify) {
        this.modify = modify;
        return this;
    }

    public PathWatcherBuilder onOverflow(Consumer<WatchEventContext<?>> overflow) {
        this.overflow = overflow;
        return this;
    }

    public PathWatcherBuilder modifyDelay(long modifyDelay) {
        return modifyDelay(modifyDelay, null);
    }

    public PathWatcherBuilder modifyDelay(long modifyDelay, ScheduledExecutorService modifyDelayScheduler) {
        Checks.checkArg(modifyDelay >= 0, "modifyDelay should be >= 0.");
        this.modifyDelay = modifyDelay;
        this.modifyDelayScheduler = modifyDelayScheduler;
        return this;
    }

    public PathWatcherBuilder modifiers(WatchEvent.Modifier[] modifiers) {
        Checks.checkNotNull(modifiers, "modifiers");
        this.modifiers = modifiers;
        return this;
    }

    public PathWatcherBuilder customExecutor(Executor executor) {
        Checks.checkNotNull(executor, "executor");
        this.executor = executor;
        return this;
    }

    public PathWatcher build() {
        return new PathWatcherImpl(path,
                isDir,
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
