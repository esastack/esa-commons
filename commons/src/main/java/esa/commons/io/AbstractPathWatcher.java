package esa.commons.io;

import esa.commons.Checks;
import esa.commons.concurrent.ConcurrentHashSet;
import esa.commons.logging.Logger;
import esa.commons.logging.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
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

abstract class AbstractPathWatcher implements PathWatcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractPathWatcher.class);
    private final Path root;
    private final ArrayList<WatchEvent.Kind<?>> events = new ArrayList<>(4);
    private final Consumer<WatchEventContext<?>> create;
    private final Consumer<WatchEventContext<?>> delete;
    private final Consumer<WatchEventContext<?>> modify;
    private final Consumer<WatchEventContext<?>> overflow;
    final WatchService watchService;
    final WatchEvent.Modifier[] modifiers;
    final WatchEvent.Kind<?>[] kinds;

    private final Executor executor;
    private final long delay;
    private final ScheduledExecutorService delayScheduler;
    private final Set<String> eventSet;
    private volatile boolean started = false;
    private volatile boolean stopped = false;

    AbstractPathWatcher(Path path,
                        Consumer<WatchEventContext<?>> create,
                        Consumer<WatchEventContext<?>> delete,
                        Consumer<WatchEventContext<?>> modify,
                        Consumer<WatchEventContext<?>> overflow,
                        WatchEvent.Modifier[] modifiers,
                        long delay,
                        Executor executor,
                        ScheduledExecutorService delayScheduler) {
        Checks.checkNotNull(path, "path");
        this.root = path;
        this.modifiers = modifiers == null ? new WatchEvent.Modifier[0] : modifiers;

        this.create = doIfConsumerNotNull(create, () -> events.add(StandardWatchEventKinds.ENTRY_CREATE));
        this.delete = doIfConsumerNotNull(delete, () -> events.add(StandardWatchEventKinds.ENTRY_DELETE));
        this.modify = doIfConsumerNotNull(modify, () -> events.add(StandardWatchEventKinds.ENTRY_MODIFY));
        this.overflow = doIfConsumerNotNull(overflow, () -> events.add(StandardWatchEventKinds.OVERFLOW));
        Checks.checkArg(events.size() > 0, "No processors of WatchEventContext!" +
                "Please add processors of WatchEventContext by call of on...(),such as onCreate().");
        this.kinds = events.toArray(new WatchEvent.Kind[0]);

        this.executor = getExecutor(executor, path);
        this.delay = delay;
        this.eventSet = delay > 0 ? new ConcurrentHashSet<>() : null;
        this.delayScheduler = getdelayScheduler(delayScheduler, path);

        try {
            this.watchService = FileSystems.getDefault().newWatchService();
        } catch (IOException e) {
            throw new WatchException(e);
        }

        initDir(root);
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
        register(root);
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
        LOGGER.info("PathWatcher of {} is stopping!", root);
        stopped = true;
        IOUtils.closeQuietly(watchService);

        //Use atomic operations to avoid unnecessary exceptions caused by adding tasks to
        //delayScheduler after delayScheduler is shutdown after stop
        atomicSchedulerOperation(delayScheduler::shutdown);
    }

    private void watch() {
        while (!stopped) {
            try {
                doWatch();
            } catch (Throwable e) {
                LOGGER.error("Error occur when watch {}.", root, e);
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
            File file = getFile(event, wk);
            if (file == null) {
                continue;
            }
            final WatchEvent.Kind<?> kind = event.kind();
            if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                pushEvent(new WatchEventContextImpl<>(event, file), create);
            } else if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                pushEvent(new WatchEventContextImpl<>(event, file), modify);
            } else if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
                pushEvent(new WatchEventContextImpl<>(event, file), delete);
            } else if (kind == StandardWatchEventKinds.OVERFLOW) {
                pushEvent(new WatchEventContextImpl<>(event, file), overflow);
            }
        }
        wk.reset();
    }

    abstract void initDir(Path root);

    abstract void register(Path root);

    abstract File getFile(WatchEvent<?> event, WatchKey wk);

    private void pushEvent(WatchEventContext<?> ctx, Consumer<WatchEventContext<?>> consumer) {
        if (delay <= 0) {
            consumer.accept(ctx);
            return;
        }

        final String eventKey = ctx.event().context().toString() + ctx.event().kind().name();
        if (eventSet.contains(eventKey)) {
            return;
        }
        eventSet.add(eventKey);

        //Use atomic operations to avoid unnecessary exceptions caused by adding tasks to
        //delayScheduler when delayScheduler had shutdown after stop
        atomicSchedulerOperation(() -> {
            if (!stopped) {
                delayScheduler.schedule(() -> {
                    eventSet.remove(eventKey);
                    consumer.accept(ctx);
                }, delay, TimeUnit.MILLISECONDS);
            }
        });

    }

    private void atomicSchedulerOperation(Runnable runnable) {
        Checks.checkNotNull(delayScheduler, "delayScheduler");
        synchronized (delayScheduler) {
            runnable.run();
        }
    }

    private static Consumer<WatchEventContext<?>> doIfConsumerNotNull(Consumer<WatchEventContext<?>> consumer,
                                                                      Runnable doIfConsumerNotNull) {
        if (consumer == null) {
            return null;
        } else {
            doIfConsumerNotNull.run();
            return consumer;
        }
    }

    private static ScheduledExecutorService getdelayScheduler(ScheduledExecutorService scheduler, Path path) {
        if (scheduler == null) {
            return defaultScheduler("FileWatcher-delayScheduler-" + path);
        } else {
            return scheduler;
        }
    }

    private static Executor getExecutor(Executor executor, Path path) {
        if (executor == null) {
            return defaultExecutor("FileWatcher-Executor-" + path);
        } else {
            return executor;
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
     * The default delayScheduler does not share threads to avoid the bad influence of FileWatchers
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
