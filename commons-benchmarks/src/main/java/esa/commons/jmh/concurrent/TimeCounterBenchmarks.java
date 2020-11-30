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

import esa.commons.TimeCounter;
import esa.commons.http.HttpMethod;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@Threads(Threads.MAX)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 1, time = 5)
@Measurement(iterations = 1, time = 5)
public class TimeCounterBenchmarks {

    @Benchmark
    public void countOrigin(Blackhole blackhole) {
        long start = System.nanoTime();
        blackhole.consume(TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start));
    }

    @Benchmark
    public void countByTimeCounter(Blackhole blackhole) {
        TimeCounter.start();
        blackhole.consume(TimeCounter.countMillis());
    }

}
