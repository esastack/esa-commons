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

import esa.commons.concurrencytest.SpscArrayQueue;
import org.openjdk.jmh.annotations.*;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@State(Scope.Group)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 5, time = 5)
@Measurement(iterations = 10, time = 5)
public class SpscArrayQueueBenchmarks {

    @Param({"Spsc", "LinkedBlockingQueue"})
    private String type;

    private Queue<Integer> q;

    @Setup
    public void setQ() {
        if ("Spsc".equals(type)) {
            q = new SpscArrayQueue<>(16384);
        } else if ("LinkedBlockingQueue".equals(type)) {
            q = new LinkedBlockingQueue<>(16384);
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
    public void offer(Offer metrics) {
        if (q.offer(1)) {
            metrics.offerOk++;
        } else {
            metrics.offerFailed++;
        }
    }

    @Benchmark
    @Group("g")
    public void poll(Poll metrics) {
        Integer e = q.poll();
        if (e == null) {
            metrics.pollFailed++;
        } else {
            metrics.pollOk++;
        }
    }

    @TearDown(Level.Iteration)
    public void emptyQ() {
        synchronized (q) {
            q.clear();
        }
    }
}
