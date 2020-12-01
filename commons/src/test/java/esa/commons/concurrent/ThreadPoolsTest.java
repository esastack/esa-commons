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

import org.junit.jupiter.api.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ThreadPoolsTest {

    @Test
    void testBuilder() {
        final ThreadFactory f = Thread::new;
        final RejectedExecutionHandler rejectHandler = (r, executor) -> {
        };
        final BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>(1);
        ThreadPoolExecutor pool = ThreadPools.builder()
                .corePoolSize(0)
                .maximumPoolSize(1)
                .keepAliveTime(10L)
                .threadFactory(f)
                .rejectPolicy(rejectHandler)
                .workQueue(queue)
                .build();

        assertEquals(0, pool.getCorePoolSize());
        assertEquals(1, pool.getMaximumPoolSize());
        assertEquals(10L, pool.getKeepAliveTime(TimeUnit.SECONDS));
        assertSame(queue, pool.getQueue());
        assertSame(f, pool.getThreadFactory());
        assertSame(rejectHandler, pool.getRejectedExecutionHandler());

        try {
            pool.shutdownNow();
        } catch (Exception ignored) {
        }

        pool = ThreadPools.builder()
                .corePoolSize(0)
                .maximumPoolSize(1)
                .useLinkedBlockingQueue()
                .threadFactory("foo#")
                .build();

        assertTrue(pool.getQueue() instanceof LinkedBlockingQueue);
        assertEquals(0L, pool.getKeepAliveTime(TimeUnit.SECONDS));
        assertEquals(Integer.MAX_VALUE, pool.getQueue().remainingCapacity());
        assertTrue(pool.getRejectedExecutionHandler() instanceof ThreadPoolExecutor.AbortPolicy);

        Thread t = pool.getThreadFactory().newThread(() -> {
        });
        assertFalse(t instanceof InternalThread);
        assertTrue(t.getName().startsWith("foo#"));
        assertFalse(t.isDaemon());
        assertEquals(t.getThreadGroup(), t.getUncaughtExceptionHandler());

        try {
            pool.shutdownNow();
        } catch (Exception ignored) {
        }

        pool = ThreadPools.builder()
                .corePoolSize(0)
                .maximumPoolSize(1)
                .useLinkedBlockingQueue(10)
                .useCallerRunsPolicy()
                .threadFactory("foo#", true)
                .build();

        assertTrue(pool.getQueue() instanceof LinkedBlockingQueue);
        assertEquals(10, pool.getQueue().remainingCapacity());
        assertTrue(pool.getRejectedExecutionHandler() instanceof ThreadPoolExecutor.CallerRunsPolicy);
        t = pool.getThreadFactory().newThread(() -> {
        });
        assertFalse(t instanceof InternalThread);
        assertTrue(t.getName().startsWith("foo#"));
        assertTrue(t.isDaemon());
        assertEquals(t.getThreadGroup(), t.getUncaughtExceptionHandler());

        try {
            pool.shutdownNow();
        } catch (Exception ignored) {
        }

        pool = ThreadPools.builder()
                .corePoolSize(0)
                .maximumPoolSize(1)
                .useArrayBlockingQueue(10)
                .useDiscardPolicy()
                .build();

        assertTrue(pool.getQueue() instanceof ArrayBlockingQueue);
        assertEquals(10, pool.getQueue().remainingCapacity());
        assertTrue(pool.getRejectedExecutionHandler() instanceof ThreadPoolExecutor.DiscardPolicy);

        try {
            pool.shutdownNow();
        } catch (Exception ignored) {
        }

        pool = ThreadPools.builder()
                .corePoolSize(0)
                .maximumPoolSize(1)
                .usePriorityBlockingQueue()
                .build();

        assertTrue(pool.getQueue() instanceof PriorityBlockingQueue);
        assertEquals(new PriorityBlockingQueue<>().remainingCapacity(), pool.getQueue().remainingCapacity());

        try {
            pool.shutdownNow();
        } catch (Exception ignored) {
        }

        pool = ThreadPools.builder()
                .corePoolSize(0)
                .maximumPoolSize(1)
                .usePriorityBlockingQueue(10)
                .build();

        assertTrue(pool.getQueue() instanceof PriorityBlockingQueue);

        try {
            pool.shutdownNow();
        } catch (Exception ignored) {
        }

        pool = ThreadPools.builder()
                .corePoolSize(0)
                .maximumPoolSize(1)
                .useSynchronousQueue()
                .build();

        assertTrue(pool.getQueue() instanceof SynchronousQueue);

        try {
            pool.shutdownNow();
        } catch (Exception ignored) {
        }
    }

}
