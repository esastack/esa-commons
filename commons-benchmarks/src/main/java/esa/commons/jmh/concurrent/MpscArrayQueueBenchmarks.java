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

import esa.commons.concurrencytest.MpscArrayBuffer;
import esa.commons.concurrencytest.MpscArrayQueue;
import org.openjdk.jmh.annotations.*;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.IntSupplier;
import java.util.function.Predicate;

@State(Scope.Group)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 5, time = 5)
@Measurement(iterations = 10, time = 5)
public class MpscArrayQueueBenchmarks {

    @Param({"Mpsc", "LinkedBlockingQueue", "Mpsc_Buffer"})
    private String type;

    private Predicate<Integer> offerFunc;
    private IntSupplier pollFunc;
    private Queue<Integer> q;

    @Setup
    public void setQ() {
        if ("Mpsc".equals(type)) {
            final MpscArrayQueue<Integer> q = new MpscArrayQueue<>(16384);
            offerFunc = q::offer;
            pollFunc = () -> q.poll() == null ? 0 : 1;
            this.q = q;
        } else if ("LinkedBlockingQueue".equals(type)) {
            final LinkedBlockingQueue<Integer> q = new LinkedBlockingQueue<>(16384);
            offerFunc = q::offer;
            pollFunc = () -> q.poll() == null ? 0 : 1;
            this.q = q;
        } else if ("Mpsc_Buffer".equals(type)) {
            final MpscArrayBuffer<Integer> q = new MpscArrayBuffer<>(16384);
            offerFunc = q::offer;
            pollFunc = () -> q.drain(e -> {
            });
        } else {
            throw new Error();
        }
    }

    @AuxCounters
    @State(Scope.Thread)
    public static class Offer {
        public long offerFailed;
        public long offerOk;
    }

    @AuxCounters
    @State(Scope.Thread)
    public static class Poll {
        public long pollOk;
        public long pollFailed;

    }

    @Benchmark
    @Group("g")
    @GroupThreads(4)
    public void offer(Offer metrics) {
        if (offerFunc.test(1)) {
            metrics.offerOk++;
        } else {
            metrics.offerFailed++;
        }
    }

    @Benchmark
    @Group("g")
    public void poll(Poll metrics) {
        int p = pollFunc.getAsInt();
        if (p == 0) {
            metrics.pollFailed++;
        } else {
            metrics.pollOk += p;
        }
    }

    @TearDown(Level.Iteration)
    public void emptyQ() {
        if (q != null) {
            synchronized (q) {
                q.clear();
            }
        }
    }
}
