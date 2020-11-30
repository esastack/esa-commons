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

import java.util.concurrent.TimeUnit;

/**
 * Time counter (reentrant)
 * <p>
 * Usage:
 *
 * <pre>
 *
 * TimeCounter.start();
 * ...
 * TimeCounter.start();
 * try {
 *   //...
 * } finally {
 *     long cost1 = TimeCounter.count(unit);
 *     long cost2 = TimeCounter.count(unit);
 * }
 * </pre>
 */
public final class TimeCounter {

    private static final ThreadLocal<Stack> PIVOT = new ThreadLocal<>();

    public static void start() {
        Stack s = PIVOT.get();
        if (s == null) {
            s = new Stack(System.nanoTime());
            PIVOT.set(s);
        } else {
            s.push(System.nanoTime());
        }
    }

    public static long count(TimeUnit unit) {
        Stack s;
        if ((s = PIVOT.get()) == null) {
            return 0L;
        }
        long cost = System.nanoTime() - s.pop();
        // avoid leak
        if (s.isEmpty()) {
            remove();
        }
        return unit.convert(cost, TimeUnit.NANOSECONDS);
    }

    public static void remove() {
        PIVOT.remove();
    }

    public static long countMillis() {
        return count(TimeUnit.MILLISECONDS);
    }

    public static long countSeconds() {
        return count(TimeUnit.SECONDS);
    }

    private static class Stack {

        private Node top;

        private Stack(long base) {
            this.top = new Node(null, base);
        }

        private void push(long e) {
            top = new Node(top, e);
        }

        private long pop() {
            final Node l = top;
            top = l.prev;
            return l.item;
        }

        private boolean isEmpty() {
            return top == null;
        }

        private static class Node {
            long item;
            Node prev;

            Node(Node prev, long element) {
                this.item = element;
                this.prev = prev;
            }
        }
    }

    private TimeCounter() {
    }
}
