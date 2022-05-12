package esa.commons.io;

import esa.commons.Checks;

import java.io.File;
import java.io.IOException;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.FileSystems;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

class FileWatcherImpl implements FileWatcher {

    private final File file;
    private final ArrayList<WatchEvent.Kind<?>> events = new ArrayList<>(4);
    private Consumer<WatchEvent<?>> create;
    private Consumer<WatchEvent<?>> delete;
    private Consumer<WatchEvent<?>> modify;
    private Consumer<WatchEvent<?>> overflow;
    private WatchService watchService;
    private WatchEvent.Modifier[] modifiers;
    private final long delay;

    private final Executor executor;
    private volatile boolean started = false;
    private volatile boolean stopped = false;

    public FileWatcherImpl(File file,
                           Consumer<WatchEvent<?>> create,
                           Consumer<WatchEvent<?>> delete,
                           Consumer<WatchEvent<?>> modify,
                           Consumer<WatchEvent<?>> overflow,
                           WatchEvent.Modifier[] modifiers,
                           long delay,
                           Executor executor) {
        Checks.checkNotNull(file, "file");
        this.file = file;
        this.modifiers = modifiers;
        if (create != null) {
            this.create = create;
            events.add(StandardWatchEventKinds.ENTRY_CREATE);
        }
        if (delete != null) {
            this.delete = delete;
            events.add(StandardWatchEventKinds.ENTRY_DELETE);
        }
        if (modify != null) {
            this.modify = modify;
            events.add(StandardWatchEventKinds.ENTRY_MODIFY);
        }
        if (overflow != null) {
            this.overflow = overflow;
            events.add(StandardWatchEventKinds.OVERFLOW);
        }
        if (events.size() == 0) {
            throw new IllegalStateException("No processors of watchEvent!" +
                    "Please add processors of watchEvent by call of on...()," +
                    "such as onCreate().");
        }
        this.delay = delay;
        if (executor != null) {
            this.executor = executor;
        } else {
            //When the command execution ends, the thread resource will be recycled automatically.
            this.executor = command ->
                    new Thread(command, "Thread-FileWatcher-" + file.getName()).start();
        }
        register();
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
                watch();
            }
        });
    }

    @Override
    public void stop() {
        IOUtils.closeQuietly(watchService);
        stopped = true;
    }

    private void watch() {
        while (!stopped) {
            doWatch();
        }
        //TODO Delay watch
    }

    private void doWatch() {
        WatchKey wk;
        try {
            wk = watchService.take();
        } catch (ClosedWatchServiceException e) {
            // 用户中断
            stop();
            return;
        } catch (InterruptedException e) {
            return;
        }

        for (WatchEvent<?> event : wk.pollEvents()) {
            final WatchEvent.Kind<?> kind = event.kind();

            if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                create.accept(event);
            } else if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                modify.accept(event);
            } else if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
                delete.accept(event);
            } else if (kind == StandardWatchEventKinds.OVERFLOW) {
                overflow.accept(event);
            }
        }
        wk.reset();
    }

    private void register() {
        try {
            watchService = FileSystems.getDefault().newWatchService();
            final WatchEvent.Kind<?>[] kinds = events.toArray(new WatchEvent.Kind[0]);

            if (modifiers == null || modifiers.length == 0) {
                file.toPath().register(watchService, kinds);
            } else {
                file.toPath().register(watchService, kinds, modifiers);
            }
        } catch (IOException e) {
            throw new WatchException(e);
        }
    }
}
