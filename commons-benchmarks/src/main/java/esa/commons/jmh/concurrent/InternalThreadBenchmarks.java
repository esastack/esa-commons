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
package esa.commons.jmh.concurrent;

import esa.commons.concurrent.InternalThread;
import esa.commons.concurrent.ThreadFactories;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.FastThreadLocal;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@Threads(Threads.MAX)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 5, time = 5)
@Measurement(iterations = 10, time = 5)
public class InternalThreadBenchmarks {

    @Param({"64", "128", "256"})
    private int size;
    private ThreadLocal<Object>[] jdkThreadLocals;
    private FastThreadLocal<Object>[] fastThreadLocals;

    @Setup
    public void setUp() {
        final Thread t = Thread.currentThread();
        if (t instanceof InternalThread) {
            ((InternalThread) t).tracer(new Object());
        }
        jdkThreadLocals = new ThreadLocal[size];
        for (int i = 0; i < jdkThreadLocals.length; i++) {
            jdkThreadLocals[i] = ThreadLocal.withInitial(Object::new);
        }

        fastThreadLocals = new FastThreadLocal[size];
        for (int i = 0; i < fastThreadLocals.length; i++) {
            fastThreadLocals[i] = new FastThreadLocal() {
                @Override
                protected Object initialValue() {
                    return new Object();
                }
            };
        }
    }

    @Benchmark
    @Fork(value = 1, jvmArgs = {"-Djmh.executor=CUSTOM",
            "-Djmh.executor.class=esa.commons.jmh.concurrent.InternalThreadBenchmarks$InternalThreadExecutor"})
    public void internalThread(Blackhole blackhole) {
        for (int i = 0; i < size; i++) {
            final Thread t = Thread.currentThread();
            if (t instanceof InternalThread) {
                Object o = ((InternalThread) t).tracer();
                blackhole.consume(o);
            } else {
                throw new Error();
            }
        }
    }

    @Fork(1)
    @Benchmark
    public void nativeThreadLocal(Blackhole blackhole) {
        for (ThreadLocal<Object> jdkThreadLocal : jdkThreadLocals) {
            blackhole.consume(jdkThreadLocal.get());
        }
    }

    @Benchmark
    @Fork(value = 1, jvmArgs = {"-Djmh.executor=CUSTOM",
            "-Djmh.executor.class=esa.commons.jmh.concurrent.InternalThreadBenchmarks$NettyThreadExecutor"})
    public void fastThreadLocal(Blackhole blackhole) {
        for (FastThreadLocal<Object> fastThreadLocal : fastThreadLocals) {
            blackhole.consume(fastThreadLocal.get());
        }
    }

    @TearDown
    public void clear() {
        for (ThreadLocal<Object> jdkThreadLocal1 : jdkThreadLocals) {
            jdkThreadLocal1.remove();
        }

        for (FastThreadLocal<Object> fastThreadLocal : fastThreadLocals) {
            fastThreadLocal.remove();
        }
    }

    public static final class InternalThreadExecutor extends ThreadPoolExecutor {

        public InternalThreadExecutor(int maxThreads, String prefix) {
            super(maxThreads, maxThreads, 0, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<>(), ThreadFactories.builder()
                            .groupName(prefix)
                            .useInternalThread(true)
                            .build());
        }
    }

    public static final class NettyThreadExecutor extends ThreadPoolExecutor {

        public NettyThreadExecutor(int maxThreads, String prefix) {
            super(maxThreads, maxThreads, 0, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<>(), new DefaultThreadFactory(prefix));
        }
    }

}
