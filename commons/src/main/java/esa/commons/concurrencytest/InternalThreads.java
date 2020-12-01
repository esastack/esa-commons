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

import esa.commons.ClassUtils;

/**
 * Unity class of {@link InternalThread}.
 */
public final class InternalThreads {

    private static final boolean NETTY_THREAD_AVAILABLE;

    /**
     * Creates a new {@link Thread} that is the implementation of {@link InternalThread}.
     *
     * @return instance of {@link NettyInternalThread}, {@link InternalThreadImpl} otherwise
     */
    public static InternalThread newThread() {
        return NETTY_THREAD_AVAILABLE
                ? new NettyInternalThread()
                : new InternalThreadImpl();
    }

    /**
     * Creates a new {@link Thread} that is an implementation of {@link InternalThread}.
     *
     * @param target target to be invoked when this thread is tarted
     *
     * @return instance of {@link NettyInternalThread}, {@link InternalThreadImpl} otherwise
     */
    public static InternalThread newThread(Runnable target) {
        return NETTY_THREAD_AVAILABLE
                ? new NettyInternalThread(target)
                : new InternalThreadImpl(target);
    }

    /**
     * Creates a new {@link Thread} that is an implementation of {@link InternalThread}.
     *
     * @param group  thread group that the new thread belongs to
     * @param target target to be invoked when this thread is tarted
     *
     * @return instance of {@link NettyInternalThread}, {@link InternalThreadImpl} otherwise
     */
    public static InternalThread newThread(ThreadGroup group, Runnable target) {
        return NETTY_THREAD_AVAILABLE
                ? new NettyInternalThread(group, target)
                : new InternalThreadImpl(group, target);
    }

    /**
     * Creates a new {@link Thread} that is an implementation of {@link InternalThread}.
     *
     * @param name name of this thread
     *
     * @return instance of {@link NettyInternalThread}, {@link InternalThreadImpl} otherwise
     */
    public static InternalThread newThread(String name) {
        return NETTY_THREAD_AVAILABLE
                ? new NettyInternalThread(name)
                : new InternalThreadImpl(name);
    }

    /**
     * Creates a new {@link Thread} that is an implementation of {@link InternalThread}.
     *
     * @param group thread group that the new thread belongs to
     * @param name  name of this thread
     *
     * @return instance of {@link NettyInternalThread}, {@link InternalThreadImpl} otherwise
     */
    public static InternalThread newThread(ThreadGroup group, String name) {
        return NETTY_THREAD_AVAILABLE
                ? new NettyInternalThread(group, name)
                : new InternalThreadImpl(group, name);
    }

    /**
     * Creates a new {@link Thread} that is an implementation of {@link InternalThread}.
     *
     * @param target target to be invoked when this thread is tarted
     * @param name   name of this thread
     *
     * @return instance of {@link NettyInternalThread}, {@link InternalThreadImpl} otherwise
     */
    public static InternalThread newThread(Runnable target, String name) {
        return NETTY_THREAD_AVAILABLE
                ? new NettyInternalThread(target, name)
                : new InternalThreadImpl(target, name);
    }

    /**
     * Creates a new {@link Thread} that is an implementation of {@link InternalThread}.
     *
     * @param group  thread group that the new thread belongs to
     * @param target target to be invoked when this thread is tarted
     * @param name   name of this thread
     *
     * @return instance of {@link NettyInternalThread}, {@link InternalThreadImpl} otherwise
     */
    public static InternalThread newThread(ThreadGroup group, Runnable target, String name) {
        return NETTY_THREAD_AVAILABLE
                ? new NettyInternalThread(group, target, name)
                : new InternalThreadImpl(group, target, name);
    }

    /**
     * Creates a new {@link Thread} that is an implementation of {@link InternalThread}.
     *
     * @param group     thread group that the new thread belongs to
     * @param target    target to be invoked when this thread is tarted
     * @param name      name of this thread
     * @param stackSize the desired stack size for the new thread, or zero to indicate that this parameter is to be
     *                  ignored
     *
     * @return instance of {@link NettyInternalThread}, {@link InternalThreadImpl} otherwise
     */
    public static InternalThread newThread(ThreadGroup group, Runnable target, String name, long stackSize) {
        return NETTY_THREAD_AVAILABLE
                ? new NettyInternalThread(group, target, name, stackSize)
                : new InternalThreadImpl(group, target, name, stackSize);
    }

    static {
        boolean available = false;
        try {
            if (ClassUtils.hasClass("io.netty.util.concurrent.FastThreadLocalThread")) {
                NettyInternalThread t = new NettyInternalThread("instantly");
                if (t.threadLocalMap() == null) {
                    available = true;
                }
            }
        } catch (Throwable ignored) {
        }

        NETTY_THREAD_AVAILABLE = available;
    }

    private InternalThreads() {
    }
}
