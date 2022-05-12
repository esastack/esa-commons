package esa.commons.io;

import java.io.File;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

class FileWatcherImpl implements FileWatcher {

    private final File file;
    private final Consumer<WatchEvent> create;
    private final Consumer<WatchEvent> delete;
    private final Consumer<WatchEvent> modify;
    private final long delay;
    private final Executor executor;
    private volatile boolean started = false;
    private volatile boolean stopped = false;

    public FileWatcherImpl(File file,
                           Consumer<WatchEvent> create,
                           Consumer<WatchEvent> delete,
                           Consumer<WatchEvent> modify,
                           long delay,
                           Executor executor) {
        this.file = file;
        this.create = create;
        this.delete = delete;
        this.modify = modify;
        this.delay = delay;
        if (executor != null) {
            this.executor = executor;
        } else {
            //When the command execution ends, the thread resource will be recycled automatically.
            this.executor = command ->
                    new Thread(command, "Thread-FileWatcher-" + file.getName()).start();
        }
    }

    @Override
    public void start() {
        if (started) {
            throw new IllegalStateException("FileWatcher had started!");
        }
        synchronized (this) {
            if (started) {
                throw new IllegalStateException("FileWatcher had started!");
            }
            started = true;
        }
        executor.execute(() -> {
            //If stop is executed firstly, it will end directly at start()
            if (!stopped) {
                init();
                watch();
            }
        });
    }

    @Override
    public void stop() {
        stopped = true;
    }

    private void init() {
        //TODO
    }

    private void watch() {
        //TODO
    }
}
