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
package esa.commons.concurrent;

import esa.commons.Checks;

import java.util.Comparator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Unity class for build a {@link ThreadPoolExecutor}.
 */
public class ThreadPools {

    public static Builder builder() {
        return Builder.aThreadPoolExecutor();
    }

    public static final class Builder {
        private int corePoolSize;
        private int maximumPoolSize;
        private BlockingQueue<Runnable> workQueue;
        private long keepAliveTime;
        private TimeUnit timeUnit = TimeUnit.SECONDS;
        private ThreadFactory threadFactory = ThreadFactories.namedThreadFactory("pool-");
        private RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();

        private Builder() {
        }

        public static Builder aThreadPoolExecutor() {
            return new Builder();
        }

        public Builder corePoolSize(int corePoolSize) {
            this.corePoolSize = corePoolSize;
            return this;
        }

        public Builder maximumPoolSize(int maximumPoolSize) {
            this.maximumPoolSize = maximumPoolSize;
            return this;
        }

        public Builder workQueue(BlockingQueue<Runnable> workQueue) {
            Checks.checkNotNull(workQueue, "workQueue");
            this.workQueue = workQueue;
            return this;
        }

        public Builder useLinkedBlockingQueue() {
            this.workQueue = new LinkedBlockingQueue<>();
            return this;
        }

        public Builder useLinkedBlockingQueue(int capacity) {
            this.workQueue = new LinkedBlockingQueue<>(capacity);
            return this;
        }

        public Builder useArrayBlockingQueue(int capacity) {
            this.workQueue = new ArrayBlockingQueue<>(capacity);
            return this;
        }

        public Builder useArrayBlockingQueue(int capacity, boolean fair) {
            this.workQueue = new ArrayBlockingQueue<>(capacity, fair);
            return this;
        }

        public Builder useSynchronousQueue() {
            this.workQueue = new SynchronousQueue<>();
            return this;
        }

        public Builder useSynchronousQueue(boolean fair) {
            this.workQueue = new SynchronousQueue<>(fair);
            return this;
        }

        public Builder usePriorityBlockingQueue() {
            this.workQueue = new PriorityBlockingQueue<>();
            return this;
        }

        public Builder usePriorityBlockingQueue(int capacity) {
            this.workQueue = new PriorityBlockingQueue<>(capacity);
            return this;
        }

        public Builder usePriorityBlockingQueue(int capacity, Comparator<? super Runnable> comparator) {
            this.workQueue = new PriorityBlockingQueue<>(capacity, comparator);
            return this;
        }

        public Builder keepAliveTime(long keepAliveTime) {
            return keepAliveTime(keepAliveTime, TimeUnit.SECONDS);
        }

        public Builder keepAliveTime(long keepAliveTime, TimeUnit unit) {
            Checks.checkNotNull(unit, "unit");
            this.keepAliveTime = keepAliveTime;
            this.timeUnit = unit;
            return this;
        }

        public Builder threadFactory(String prefix) {
            Checks.checkNotEmptyArg(prefix, "prefix");
            this.threadFactory = ThreadFactories.namedThreadFactory(prefix);
            return this;
        }

        public Builder threadFactory(String prefix, boolean daemon) {
            Checks.checkNotEmptyArg(prefix, "prefix");
            this.threadFactory = ThreadFactories.namedThreadFactory(prefix, daemon);
            return this;
        }

        public Builder threadFactory(ThreadFactory threadFactory) {
            Checks.checkNotNull(threadFactory, "threadFactory");
            this.threadFactory = threadFactory;
            return this;
        }

        public Builder rejectPolicy(RejectedExecutionHandler handler) {
            Checks.checkNotNull(handler, "handler");
            this.handler = handler;
            return this;
        }

        public Builder useAbortPolicy() {
            this.handler = new ThreadPoolExecutor.AbortPolicy();
            return this;
        }

        public Builder useCallerRunsPolicy() {
            this.handler = new ThreadPoolExecutor.CallerRunsPolicy();
            return this;
        }

        public Builder useDiscardOldestPolicy() {
            this.handler = new ThreadPoolExecutor.DiscardOldestPolicy();
            return this;
        }

        public Builder useDiscardPolicy() {
            this.handler = new ThreadPoolExecutor.DiscardPolicy();
            return this;
        }

        public ThreadPoolExecutor build() {
            return new ThreadPoolExecutor(corePoolSize,
                    maximumPoolSize,
                    keepAliveTime,
                    timeUnit,
                    workQueue,
                    threadFactory,
                    handler);
        }
    }
}
