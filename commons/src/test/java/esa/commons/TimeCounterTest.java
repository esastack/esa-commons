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
package esa.commons;

import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TimeCounterTest {

    @Test
    void testCountOnce() throws InterruptedException {
        assertEquals(0L, TimeCounter.countMillis());
        assertEquals(0L, TimeCounter.countSeconds());
        assertEquals(0L, TimeCounter.count(TimeUnit.NANOSECONDS));

        final long start = System.nanoTime();
        TimeCounter.start();
        Thread.sleep(20L);
        long cost;
        assertTrue((cost = TimeCounter.countMillis()) <= TimeUnit.NANOSECONDS
                .toMillis(System.nanoTime() - start) && cost > 0L);

        Thread.sleep(20L);
        assertEquals(0L, TimeCounter.countMillis());
        assertEquals(0L, TimeCounter.countSeconds());
        assertEquals(0L, TimeCounter.count(TimeUnit.NANOSECONDS));
    }

    @Test
    void testCountTwice() throws InterruptedException {
        assertEquals(0L, TimeCounter.countMillis());
        assertEquals(0L, TimeCounter.countSeconds());
        assertEquals(0L, TimeCounter.count(TimeUnit.NANOSECONDS));
        final long start = System.nanoTime();
        TimeCounter.start();
        TimeCounter.start();
        Thread.sleep(20L);
        long cost;
        assertTrue((cost = TimeCounter.countMillis()) <= TimeUnit.NANOSECONDS
                .toMillis(System.nanoTime() - start) && cost > 0L);
        assertTrue((cost = TimeCounter.countMillis()) <= TimeUnit.NANOSECONDS
                .toMillis(System.nanoTime() - start) && cost > 0L);

        Thread.sleep(20L);
        assertEquals(0L, TimeCounter.countMillis());
        assertEquals(0L, TimeCounter.countSeconds());
        assertEquals(0L, TimeCounter.count(TimeUnit.NANOSECONDS));
    }

    @Test
    void testCountManyTimes() throws InterruptedException {
        assertEquals(0L, TimeCounter.countMillis());
        assertEquals(0L, TimeCounter.countSeconds());
        assertEquals(0L, TimeCounter.count(TimeUnit.NANOSECONDS));
        final long start = System.nanoTime();
        TimeCounter.start();
        TimeCounter.start();
        TimeCounter.start();
        TimeCounter.start();
        Thread.sleep(20L);
        long cost;
        assertTrue((cost = TimeCounter.countMillis()) <= TimeUnit.NANOSECONDS
                .toMillis(System.nanoTime() - start) && cost > 0L);
        assertTrue((cost = TimeCounter.countMillis()) <= TimeUnit.NANOSECONDS
                .toMillis(System.nanoTime() - start) && cost > 0L);
        assertTrue((cost = TimeCounter.countMillis()) <= TimeUnit.NANOSECONDS
                .toMillis(System.nanoTime() - start) && cost > 0L);
        assertTrue((cost = TimeCounter.countMillis()) <= TimeUnit.NANOSECONDS
                .toMillis(System.nanoTime() - start) && cost > 0L);

        Thread.sleep(20L);
        assertEquals(0L, TimeCounter.countMillis());
        assertEquals(0L, TimeCounter.countSeconds());
        assertEquals(0L, TimeCounter.count(TimeUnit.NANOSECONDS));
    }

}
