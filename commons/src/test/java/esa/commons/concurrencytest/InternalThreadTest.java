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
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InternalThreadTest {

    @Test
    void testCreateNewNettyInternalThread() {
        InternalThread t = InternalThreads.newThread();
        assertTrue(t instanceof NettyInternalThread);
        assertThreadInfo(t, null, null);

        final AtomicBoolean b = new AtomicBoolean();
        t = InternalThreads.newThread(() -> b.set(true));
        assertThreadInfo(t, null, null);
        t.thread().run();
        assertTrue(b.get());
        b.set(false);

        final ThreadGroup group = new ThreadGroup("foo");
        t = InternalThreads.newThread(group, () -> b.set(true));
        assertThreadInfo(t, group, null);
        t.thread().run();
        assertTrue(b.get());
        b.set(false);

        assertThreadInfo(InternalThreads.newThread("foo"), null, "foo");
        assertThreadInfo(InternalThreads.newThread(group, "foo"), group, "foo");

        t = InternalThreads.newThread(() -> b.set(true), "foo");
        assertThreadInfo(t, null, "foo");
        t.thread().run();
        assertTrue(b.get());
        b.set(false);

        t = InternalThreads.newThread(group, () -> b.set(true), "foo");
        assertThreadInfo(t, group, "foo");
        t.thread().run();
        assertTrue(b.get());
        b.set(false);

        t = InternalThreads.newThread(group, () -> b.set(true), "foo", 8);
        assertThreadInfo(t, group, "foo");
        t.thread().run();
        assertTrue(b.get());
        b.set(false);
    }

    @Test
    void testCreateNewInternalThread() {
        InternalThread t = new InternalThreadImpl();
        assertThreadInfo(t, null, null);

        final AtomicBoolean b = new AtomicBoolean();
        t = new InternalThreadImpl(() -> b.set(true));
        assertThreadInfo(t, null, null);
        t.thread().run();
        assertTrue(b.get());
        b.set(false);

        final ThreadGroup group = new ThreadGroup("foo");
        t = new InternalThreadImpl(group, () -> b.set(true));
        assertThreadInfo(t, group, null);
        t.thread().run();
        assertTrue(b.get());
        b.set(false);

        assertThreadInfo(new InternalThreadImpl("foo"), null, "foo");
        assertThreadInfo(new InternalThreadImpl(group, "foo"), group, "foo");

        t = new InternalThreadImpl(() -> b.set(true), "foo");
        assertThreadInfo(t, null, "foo");
        t.thread().run();
        assertTrue(b.get());
        b.set(false);

        t = new InternalThreadImpl(group, () -> b.set(true), "foo");
        assertThreadInfo(t, group, "foo");
        t.thread().run();
        assertTrue(b.get());
        b.set(false);

        t = new InternalThreadImpl(group, () -> b.set(true), "foo", 8);
        assertThreadInfo(t, group, "foo");
        t.thread().run();
        assertTrue(b.get());
        b.set(false);
    }

    private void assertThreadInfo(InternalThread t,
                                  ThreadGroup group,
                                  String name) {
        final Thread thread = t.thread();

        if (group != null) {
            assertSame(group, thread.getThreadGroup());
        }
        if (StringUtils.isNotEmpty(name)) {
            assertEquals(name, thread.getName());
        }

        assertSame(t, t.thread());
        assertNull(t.meter());
        assertNull(t.tracer());
        final Object o = new Object();
        t.meter(o);
        t.tracer(o);
        assertSame(o, t.meter());
        assertSame(o, t.tracer());
    }

}
