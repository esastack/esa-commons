package esa.commons.io;

import esa.commons.Checks;

import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;

//TODO 增加注释
public class PathWatcherBuilder {

    private final Path path;
    private final boolean watchPathIsDir;
    private final int maxDepth;
    private Consumer<WatchEventContext<?>> create;
    private Consumer<WatchEventContext<?>> delete;
    private Consumer<WatchEventContext<?>> modify;
    private Consumer<WatchEventContext<?>> overflow;
    private ScheduledExecutorService delayScheduler;
    private WatchEvent.Modifier[] modifiers;
    private long delay = 200;

    PathWatcherBuilder(Path path, boolean watchPathIsDir, int maxDepth) {
        Checks.checkNotNull(path, "path");
        this.path = path;
        this.watchPathIsDir = watchPathIsDir;
        this.maxDepth = maxDepth;
    }

    //TODO 增加注释
    public PathWatcherBuilder onCreate(Consumer<WatchEventContext<?>> create) {
        this.create = create;
        return this;
    }

    //TODO 增加注释
    public PathWatcherBuilder onDelete(Consumer<WatchEventContext<?>> delete) {
        this.delete = delete;
        return this;
    }

    //TODO 增加注释
    public PathWatcherBuilder onModify(Consumer<WatchEventContext<?>> modify) {
        this.modify = modify;
        return this;
    }

    //TODO 增加注释
    public PathWatcherBuilder onOverflow(Consumer<WatchEventContext<?>> overflow) {
        this.overflow = overflow;
        return this;
    }

    //TODO 增加注释
    public PathWatcherBuilder delay(long delay) {
        return delay(delay, null);
    }

    //TODO 增加注释
    public PathWatcherBuilder delay(long delay, ScheduledExecutorService delayScheduler) {
        Checks.checkArg(delay >= 0, "delay should be >= 0.");
        this.delay = delay;
        this.delayScheduler = delayScheduler;
        return this;
    }

    //TODO 增加注释
    public PathWatcherBuilder modifiers(WatchEvent.Modifier[] modifiers) {
        Checks.checkNotNull(modifiers, "modifiers");
        this.modifiers = modifiers;
        return this;
    }

    //TODO 增加注释
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
                    delayScheduler);
        }
        return new FileWatcherImpl(path,
                create,
                delete,
                modify,
                overflow,
                modifiers,
                delay,
                delayScheduler);
    }
}
