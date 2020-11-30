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
package esa.commons.function;

import esa.commons.Checks;

import java.util.function.Consumer;
import java.util.function.LongConsumer;
import java.util.function.ObjLongConsumer;

import static esa.commons.ExceptionUtils.throwException;

/**
 * Represents an operation that accepts a single {@code long}-valued argument and returns no result, which allows user
 * to throw a checked exception.
 * @see LongConsumer
 */
@FunctionalInterface
public interface ThrowingLongConsumer {

    /**
     * Wraps a {@link ThrowingLongConsumer} to {@link LongConsumer} which will rethrow the error thrown by the given
     * {@link ThrowingLongConsumer}
     *
     * @param c target to wrap
     *
     * @return transferred
     */
    static LongConsumer rethrow(ThrowingLongConsumer c) {
        Checks.checkNotNull(c);
        return (t) -> {
            try {
                c.accept(t);
            } catch (Throwable ex) {
                throwException(ex);
            }
        };
    }

    /**
     * Wraps a {@link ThrowingLongConsumer} to {@link LongConsumer} which will suppress the error thrown by the given
     * {@link ThrowingLongConsumer}
     *
     * @param c target to wrap
     *
     * @return transferred
     */
    static LongConsumer suppress(ThrowingLongConsumer c) {
        Checks.checkNotNull(c);
        return (t) -> {
            try {
                c.accept(t);
            } catch (Throwable ignored) {
            }
        };
    }

    /**
     * @see #failover(ThrowingLongConsumer, ObjLongConsumer)
     */
    static LongConsumer failover(ThrowingLongConsumer c,
                                 Consumer<Throwable> onFailure) {
        return failover(c, (e, t) -> onFailure.accept(e));
    }

    /**
     * Wraps a {@link ThrowingLongConsumer} to {@link LongConsumer} which will handle the error thrown by the given
     * {@link ThrowingLongConsumer} with the given {@code onFailure} operation of {@link ObjLongConsumer}
     *
     * @param c         target to wrap
     * @param onFailure failover
     *
     * @return transferred
     */
    static LongConsumer failover(ThrowingLongConsumer c,
                                 ObjLongConsumer<Throwable> onFailure) {
        Checks.checkNotNull(c);
        return (t) -> {
            try {
                c.accept(t);
            } catch (Throwable ex) {
                if (onFailure != null) {
                    onFailure.accept(ex, t);
                }
            }
        };
    }

    /**
     * @see LongConsumer#accept(long)
     */
    void accept(long value) throws Throwable;
}
