package esa.commons.io;

import esa.commons.Checks;
import esa.commons.concurrent.ConcurrentHashSet;
import esa.commons.logging.Logger;
import esa.commons.logging.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.FileSystems;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

class FileWatcherImpl implements FileWatcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileWatcherImpl.class);
    private final File file;
    private final ArrayList<WatchEvent.Kind<?>> events = new ArrayList<>(4);
    private Consumer<WatchEvent<?>> create;
    private Consumer<WatchEvent<?>> delete;
    private Consumer<WatchEvent<?>> modify;
    private Consumer<WatchEvent<?>> overflow;
    private WatchService watchService;
    private final WatchEvent.Modifier[] modifiers;
    private final long modifyDelay;

    private final Set<String> eventSet;
    private final Executor executor;
    private final ScheduledExecutorService modifyDelayScheduler;
    private volatile boolean started = false;
    private volatile boolean stopped = false;

    public FileWatcherImpl(File file,
                           Consumer<WatchEvent<?>> create,
                           Consumer<WatchEvent<?>> delete,
                           Consumer<WatchEvent<?>> modify,
                           Consumer<WatchEvent<?>> overflow,
                           WatchEvent.Modifier[] modifiers,
                           long modifyDelay,
                           Executor executor,
                           ScheduledExecutorService modifyDelayScheduler) {
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

        if (executor == null) {
            this.executor = defaultExecutor("FileWatcher-Executor-" + file.getName());
        } else {
            this.executor = executor;
        }

        this.modifyDelay = modifyDelay;
        if (modifyDelay > 0) {
            this.eventSet = new ConcurrentHashSet<>();
            if (modifyDelayScheduler == null) {
                this.modifyDelayScheduler =
                        defaultScheduler("FileWatcher-ModifyDelayScheduler-" + file.getName());
            } else {
                this.modifyDelayScheduler = modifyDelayScheduler;
            }
        } else {
            this.eventSet = null;
            this.modifyDelayScheduler = null;
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
        if (stopped) {
            return;
        }
        stopped = true;
        IOUtils.closeQuietly(watchService);

        //Use atomic operations to avoid unnecessary exceptions caused by adding tasks to
        //modifyDelayScheduler after modifyDelayScheduler is shutdown after stop
        atomicSchedulerOperation(modifyDelayScheduler::shutdown);
    }

    private void watch() {
        while (!stopped) {
            try {
                doWatch();
            } catch (Throwable e) {
                LOGGER.error("Error occur when watch file({}).", file.getName(), e);
            }
        }
    }

    private void doWatch() {
        WatchKey wk;
        try {
            wk = watchService.take();
        } catch (ClosedWatchServiceException e) {
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
                if (modifyDelay > 0) {
                    delayPushModifyEvent(event);
                } else {
                    modify.accept(event);
                }
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

    private void delayPushModifyEvent(WatchEvent<?> event) {
        final String path = event.context().toString();
        if (eventSet.contains(path)) {
            return;
        }
        eventSet.add(path);

        //Use atomic operations to avoid unnecessary exceptions caused by adding tasks to
        //modifyDelayScheduler when modifyDelayScheduler had shutdown after stop
        atomicSchedulerOperation(() -> {
            if (!stopped) {
                modifyDelayScheduler.schedule(() -> {
                    eventSet.remove(path);
                    modify.accept(event);
                }, modifyDelay, TimeUnit.MILLISECONDS);
            }
        });

    }

    private void atomicSchedulerOperation(Runnable runnable) {
        Checks.checkNotNull(modifyDelayScheduler,"modifyDelayScheduler");
        synchronized (modifyDelayScheduler) {
            runnable.run();
        }
    }

    /**
     * The default executor does not share threads to avoid the bad influence of FileWatchers among
     * multiple components.
     * <p>
     * When the command execution ends, the thread resource will be recycled automatically.This means
     * when the user stops FileWatcher, the thread resources will be automatically recycled without
     * other processing.
     *
     * @param name executorName
     * @return executor
     */
    private static Executor defaultExecutor(String name) {
        return command -> new Thread(command, name).start();
    }

    /**
     * The default modifyDelayScheduler does not share threads to avoid the bad influence of FileWatchers
     * among multiple components.
     *
     * @param name schedulerName
     * @return ScheduledExecutorService
     */
    private static ScheduledExecutorService defaultScheduler(String name) {
        return Executors.newSingleThreadScheduledExecutor(r ->
                new Thread(r, name));
    }
}
