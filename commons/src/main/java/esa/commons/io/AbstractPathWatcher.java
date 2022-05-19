/*
 * Copyright 2022 OPPO ESA Stack Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package esa.commons.io;

import esa.commons.Checks;
import esa.commons.ExceptionUtils;
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
    private final Path path;
    private final ArrayList<WatchEvent.Kind<?>> events = new ArrayList<>(4);
    private final Consumer<WatchEventContext> create;
    private final Consumer<WatchEventContext> delete;
    private final Consumer<WatchEventContext> modify;
    private final Consumer<WatchEventContext> overflow;
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
                        Consumer<WatchEventContext> create,
                        Consumer<WatchEventContext> delete,
                        Consumer<WatchEventContext> modify,
                        Consumer<WatchEventContext> overflow,
                        WatchEvent.Modifier[] modifiers,
                        long delay,
                        ScheduledExecutorService delayScheduler) {
        Checks.checkNotNull(path, "path");
        this.path = path;
        this.modifiers = modifiers == null ? new WatchEvent.Modifier[0] : modifiers;

        this.create = doIfConsumerNotNull(create, () -> events.add(StandardWatchEventKinds.ENTRY_CREATE));
        this.delete = doIfConsumerNotNull(delete, () -> events.add(StandardWatchEventKinds.ENTRY_DELETE));
        this.modify = doIfConsumerNotNull(modify, () -> events.add(StandardWatchEventKinds.ENTRY_MODIFY));
        this.overflow = doIfConsumerNotNull(overflow, () -> events.add(StandardWatchEventKinds.OVERFLOW));
        Checks.checkArg(events.size() > 0, "No processors of WatchEventContext!" +
                "Please add processors of WatchEventContext by call of on...(),such as onCreate().");
        this.kinds = events.toArray(new WatchEvent.Kind[0]);

        this.executor = executor(path);
        this.delay = delay;
        this.eventSet = delay > 0 ? new ConcurrentHashSet<>() : null;
        this.delayScheduler = delayScheduler(delayScheduler, path);

        WatchService watchServiceTem = null;
        try {
            watchServiceTem = FileSystems.getDefault().newWatchService();
        } catch (IOException e) {
            ExceptionUtils.throwException(e);
        }
        this.watchService = watchServiceTem;
        initDir(path);
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
        register(path);
        executor.execute(() -> {
            //If stop is executed firstly, it will end directly at start()
            if (!stopped) {
                watch();
            }
        });
    }

    @Override
    public boolean stopAndWait(long timeout, TimeUnit unit) throws InterruptedException {
        if (stopped) {
            if (delayScheduler == null) {
                return true;
            } else {
                return delayScheduler.awaitTermination(timeout, unit);
            }
        }
        LOGGER.info("PathWatcher of {} is stopping!", path);

        //Set stop to true firstly , so that the watch() method will be terminated at the
        //first time when the watchService is closed,
        stopped = true;
        IOUtils.closeQuietly(watchService);

        //Use atomic operations to avoid unnecessary exceptions caused by adding tasks to
        //delayScheduler after delayScheduler is shutdown after stop
        if (delayScheduler == null) {
            return true;
        }
        atomicSchedulerOperation(() -> {
            if (!delayScheduler.isShutdown()) {
                delayScheduler.shutdownNow();
            }
        });
        return delayScheduler.awaitTermination(timeout, unit);
    }

    private void watch() {
        while (!stopped) {
            try {
                doWatch();
            } catch (Throwable e) {
                LOGGER.error("Error occur when watch {}.", path, e);
            }
        }
    }

    private void doWatch() {
        WatchKey wk;
        try {
            wk = watchService.take();
        } catch (ClosedWatchServiceException | InterruptedException e) {
            return;
        }

        for (WatchEvent<?> event : wk.pollEvents()) {
            File file = getFile(event, wk);
            if (file == null) {
                continue;
            }
            final WatchEvent.Kind<?> kind = event.kind();
            if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                pushEvent(new WatchEventContextImpl(event, file), create);
            } else if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                pushEvent(new WatchEventContextImpl(event, file), modify);
            } else if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
                pushEvent(new WatchEventContextImpl(event, file), delete);
            } else if (kind == StandardWatchEventKinds.OVERFLOW) {
                pushEvent(new WatchEventContextImpl(event, file), overflow);
            }
        }
        wk.reset();
    }

    abstract void initDir(Path path);

    abstract void register(Path path);

    abstract File getFile(WatchEvent<?> event, WatchKey wk);

    private void pushEvent(WatchEventContext ctx, Consumer<WatchEventContext> consumer) {
        if (delay <= 0L) {
            consumer.accept(ctx);
            return;
        }

        final String eventKey = ctx.file().getAbsolutePath() + ctx.event().kind().name();
        if (eventSet.contains(eventKey)) {
            return;
        }
        eventSet.add(eventKey);

        //Use atomic operations to avoid unnecessary exceptions caused by adding tasks to
        //delayScheduler when delayScheduler had shutdown after stop
        atomicSchedulerOperation(() -> {
            if (!stopped) {
                delayScheduler.schedule(() -> {
                    //Remove first and then call consumer.accept() to avoid the new event is removed
                    //before it has been executed when the new event appears in the process of
                    //consumer.accept().
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

    private static Consumer<WatchEventContext> doIfConsumerNotNull(Consumer<WatchEventContext> consumer,
                                                                      Runnable doIfConsumerNotNull) {
        if (consumer == null) {
            return null;
        } else {
            doIfConsumerNotNull.run();
            return consumer;
        }
    }

    private static ScheduledExecutorService delayScheduler(ScheduledExecutorService scheduler, Path path) {
        if (scheduler == null) {
            return defaultScheduler("FileWatcher-delayScheduler-" + path);
        } else {
            return scheduler;
        }
    }

    /**
     * The executor does not share threads to avoid the bad influence of FileWatchers among
     * multiple components.
     * <p>
     * When the command execution ends, the thread resource will be recycled automatically.This means
     * when the user stops FileWatcher, the thread resources will be automatically recycled without
     * other processing.
     *
     * @param path path
     * @return executor
     */
    private static Executor executor(Path path) {
        return command -> new Thread(command, "FileWatcher-Executor-" + path).start();
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
