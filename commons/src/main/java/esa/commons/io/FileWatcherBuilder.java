package esa.commons.io;

import esa.commons.Checks;

import java.io.File;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

public class FileWatcherBuilder {

    private final File file;
    private Consumer<WatchEvent> create;
    private Consumer<WatchEvent> delete;
    private Consumer<WatchEvent> modify;
    private Executor executor;
    private long delay = 0;

    FileWatcherBuilder(File file) {
        Checks.checkNotNull(file, "file");
        this.file = file;
    }

    public FileWatcherBuilder onCreate(Consumer<WatchEvent> create) {
        this.create = create;
        return this;
    }

    public FileWatcherBuilder onDelete(Consumer<WatchEvent> delete) {
        this.delete = delete;
        return this;
    }

    public FileWatcherBuilder onModify(Consumer<WatchEvent> modify) {
        this.modify = modify;
        return this;
    }

    public FileWatcherBuilder delay(long delay) {
        Checks.checkArg(delay > 0, "Delay should be greater than 0.");
        this.delay = delay;
        return this;
    }

    public FileWatcherBuilder customExecutor(Executor executor) {
        Checks.checkNotNull(executor, "executor");
        this.executor = executor;
        return this;
    }

    public FileWatcher build() {
        return new FileWatcherImpl(file, create, delete, modify, delay, executor);
    }
}
