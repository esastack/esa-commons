package esa.commons.io;

import esa.commons.Checks;

import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;

public class PathWatcherBuilder {

    private final Path path;
    private final boolean watchPathIsDir;
    private final int maxDepth;
    private Consumer<WatchEventContext<?>> create;
    private Consumer<WatchEventContext<?>> delete;
    private Consumer<WatchEventContext<?>> modify;
    private Consumer<WatchEventContext<?>> overflow;
    private Executor executor;
    private ScheduledExecutorService delayScheduler;
    private WatchEvent.Modifier[] modifiers;
    private long delay = 200;

    public PathWatcherBuilder(Path path, boolean watchPathIsDir, int maxDepth) {
        Checks.checkNotNull(path, "path");
        this.path = path;
        this.watchPathIsDir = watchPathIsDir;
        this.maxDepth = maxDepth;
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

    public PathWatcherBuilder delay(long delay) {
        return delay(delay, null);
    }

    public PathWatcherBuilder delay(long delay, ScheduledExecutorService delayScheduler) {
        Checks.checkArg(delay >= 0, "delay should be >= 0.");
        this.delay = delay;
        this.delayScheduler = delayScheduler;
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
        if (watchPathIsDir) {
            return new DirWatcherImpl(path,
                    maxDepth,
                    create,
                    delete,
                    modify,
                    overflow,
                    modifiers,
                    delay,
                    executor,
                    delayScheduler);
        }
        return new FileWatcherImpl(path,
                create,
                delete,
                modify,
                overflow,
                modifiers,
                delay,
                executor,
                delayScheduler);
    }
}
