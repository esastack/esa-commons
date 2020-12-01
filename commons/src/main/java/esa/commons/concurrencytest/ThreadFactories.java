/*
 * Copyright 2020 OPPO ESA Stack Project
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
package esa.commons.concurrencytest;

import esa.commons.StringUtils;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Unity class for building a {@link ThreadFactory}.
 */
public final class ThreadFactories {

    private ThreadFactories() {
    }

    public static Builder builder() {
        return new Builder();
    }

    public static ThreadFactory namedThreadFactory(final String groupName) {
        return namedThreadFactory(groupName, false);
    }

    public static ThreadFactory namedThreadFactory(final String groupName, final boolean daemon) {
        return namedThreadFactory(groupName, daemon, null);
    }

    public static ThreadFactory namedThreadFactory(final String groupName,
                                                   final boolean daemon,
                                                   final Thread.UncaughtExceptionHandler exceptionHandler) {
        if (StringUtils.isEmpty(groupName)) {
            throw new IllegalArgumentException("Group name must not be null or empty!");
        }
        return builder().groupName(groupName)
                .daemon(daemon)
                .uncaughtExceptionHandler(exceptionHandler)
                .build();
    }

    private static class TF implements ThreadFactory {

        private static final AtomicInteger POOL_NUM = new AtomicInteger(1);
        private final AtomicInteger num = new AtomicInteger(0);

        final String prefix;
        final boolean daemon;
        final Thread.UncaughtExceptionHandler exceptionHandler;
        final boolean useInternalThread;

        private TF(String prefix,
                   boolean daemon,
                   Thread.UncaughtExceptionHandler exceptionHandler,
                   boolean useInternalThread) {

            this.prefix = StringUtils.nonEmptyOrElse(prefix, "pool-") + POOL_NUM.getAndIncrement() + "-";
            this.daemon = daemon;
            this.exceptionHandler = exceptionHandler;
            this.useInternalThread = useInternalThread;
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread thread;
            if (useInternalThread) {
                thread = InternalThreads.newThread(r).thread();
            } else {
                thread = new Thread(r);
            }
            try {
                if (thread.isDaemon() != daemon) {
                    thread.setDaemon(daemon);
                }
            } catch (Exception ignored) {
                // Doesn't matter even if failed to set.
            }
            thread.setName(prefix + num.getAndIncrement());
            if (exceptionHandler != null) {
                thread.setUncaughtExceptionHandler(exceptionHandler);
            }
            return thread;
        }
    }

    public static class Builder {

        public Builder() {
        }

        private String groupName;
        private boolean daemon;
        private Thread.UncaughtExceptionHandler exceptionHandler;
        private boolean useInternalThread = false;

        public Builder groupName(String groupName) {
            this.groupName = groupName;
            return this;
        }

        public Builder daemon(boolean daemon) {
            this.daemon = daemon;
            return this;
        }

        public Builder uncaughtExceptionHandler(Thread.UncaughtExceptionHandler exceptionHandler) {
            this.exceptionHandler = exceptionHandler;
            return this;
        }

        public Builder useInternalThread(boolean useInternalThread) {
            this.useInternalThread = useInternalThread;
            return this;
        }

        public ThreadFactory build() {
            return new TF(groupName, daemon, exceptionHandler, useInternalThread);
        }
    }

}
