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

import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadFactory;

import static org.junit.jupiter.api.Assertions.*;

class ThreadFactoriesTest {

    @Test
    void testNewNamedThreadFactories() {
        ThreadFactory factory = ThreadFactories.namedThreadFactory("foo#");
        Thread t = factory.newThread(() -> {
        });
        assertFalse(t instanceof InternalThread);
        assertTrue(t.getName().startsWith("foo#"));
        assertFalse(t.isDaemon());
        assertEquals(t.getThreadGroup(), t.getUncaughtExceptionHandler());

        factory = ThreadFactories.namedThreadFactory("foo#", true);
        t = factory.newThread(() -> {
        });
        assertTrue(t.isDaemon());

        final Thread.UncaughtExceptionHandler h = (t1, e) -> {
        };
        factory = ThreadFactories.namedThreadFactory("foo#", true, h);
        t = factory.newThread(() -> {
        });

        assertSame(h, t.getUncaughtExceptionHandler());

        factory = ThreadFactories.builder()
                .groupName("foo#")
                .daemon(true)
                .uncaughtExceptionHandler(h)
                .useInternalThread(true)
                .build();

        t = factory.newThread(() -> {
        });

        assertTrue(t instanceof InternalThread);
        assertTrue(t.getName().startsWith("foo#"));
        assertTrue(t.isDaemon());
        assertEquals(h, t.getUncaughtExceptionHandler());
    }

}
