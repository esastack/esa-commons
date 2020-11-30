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

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static esa.commons.ExceptionUtils.throwException;

/**
 * Represents an operation that accepts a single input argument and returns no result, which allows user to throw a
 * checked exception.
 *
 * @param <T> the type of the input to the operation
 * @see Consumer
 */
@FunctionalInterface
public interface ThrowingConsumer<T> {

    /**
     * Wraps a {@link ThrowingConsumer} to {@link Consumer} which will rethrow the error thrown by the given {@link
     * ThrowingConsumer}
     *
     * @param c   target to wrap
     * @param <T> the type of the input to the operation
     *
     * @return transferred
     */
    static <T> Consumer<T> rethrow(ThrowingConsumer<T> c) {
        Checks.checkNotNull(c);
        return (t) -> {
            try {
                c.accept(t);
            } catch (Throwable e) {
                throwException(e);
            }
        };
    }

    /**
     * Wraps a {@link ThrowingConsumer} to {@link Consumer} which will suppress the error thrown by the given {@link
     * ThrowingConsumer}
     *
     * @param c   target to wrap
     * @param <T> the type of the input to the operation
     *
     * @return transferred
     */
    static <T> Consumer<T> suppress(ThrowingConsumer<T> c) {
        Checks.checkNotNull(c);
        return (t) -> {
            try {
                c.accept(t);
            } catch (Throwable ignored) {
            }
        };
    }

    /**
     * @see #failover(ThrowingConsumer, BiConsumer)
     */
    static <T> Consumer<T> failover(ThrowingConsumer<T> c,
                                    Consumer<Throwable> onFailure) {
        return failover(c, (t, e) -> onFailure.accept(e));
    }

    /**
     * Wraps a {@link ThrowingConsumer} to {@link Consumer} which will handle the error thrown by the given {@link
     * ThrowingConsumer} with the given {@code onFailure} operation of {@link BiConsumer}
     *
     * @param c         target to wrap
     * @param onFailure failover
     * @param <T>       the type of the input to the operation
     *
     * @return transferred
     */
    static <T> Consumer<T> failover(ThrowingConsumer<T> c,
                                    BiConsumer<T, Throwable> onFailure) {
        Checks.checkNotNull(c);
        return (t) -> {
            try {
                c.accept(t);
            } catch (Throwable e) {
                if (onFailure != null) {
                    onFailure.accept(t, e);
                }
            }
        };
    }

    /**
     * @see Consumer#accept(Object)
     */
    void accept(T t) throws Throwable;
}
