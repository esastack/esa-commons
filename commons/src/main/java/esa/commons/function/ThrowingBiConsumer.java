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
import esa.commons.ExceptionUtils;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Represents an operation that accepts two input arguments and returns no result, which allows user to throw a checked
 * exception.
 *
 * @param <T> the type of the first argument to the operation
 * @param <U> the type of the second argument to the operation
 * @see BiConsumer
 */
@FunctionalInterface
public interface ThrowingBiConsumer<T, U> {

    /**
     * Wraps a {@link ThrowingBiConsumer} to {@link BiConsumer} which will rethrow the error thrown by the given {@link
     * ThrowingBiConsumer}
     *
     * @param c   target to wrap
     * @param <T> the type of the input to the operation
     * @param <U> the type of the second argument to the operation
     *
     * @return transferred
     */
    static <T, U> BiConsumer<T, U> rethrow(ThrowingBiConsumer<T, U> c) {
        Checks.checkNotNull(c);
        return (t, u) -> {
            try {
                c.accept(t, u);
            } catch (Throwable ex) {
                ExceptionUtils.throwException(ex);
            }
        };
    }

    /**
     * Wraps a {@link ThrowingBiConsumer} to {@link BiConsumer} which will suppress the error thrown by the given {@link
     * ThrowingBiConsumer}
     *
     * @param c   target to wrap
     * @param <T> the type of the first argument to the operation
     * @param <U> the type of the second argument to the operation
     *
     * @return transferred
     */
    static <T, U> BiConsumer<T, U> suppress(ThrowingBiConsumer<T, U> c) {
        Checks.checkNotNull(c);
        return (t, u) -> {
            try {
                c.accept(t, u);
            } catch (Throwable ignored) {
            }
        };
    }

    /**
     * @see #failover(ThrowingBiConsumer, Consumer3)
     */
    static <T, U> BiConsumer<T, U> failover(ThrowingBiConsumer<T, U> c,
                                            Consumer<Throwable> onFailure) {
        return failover(c, (t, u, e) -> onFailure.accept(e));
    }

    /**
     * Wraps a {@link ThrowingBiConsumer} to {@link BiConsumer} which will handle the error thrown by the given {@link
     * ThrowingBiConsumer} with the given {@code onFailure} operation of {@link Consumer3}
     *
     * @param c         target to wrap
     * @param onFailure failover
     * @param <T>       the type of the first argument to the operation
     * @param <U>       the type of the second argument to the operation
     *
     * @return transferred
     */
    static <T, U> BiConsumer<T, U> failover(ThrowingBiConsumer<T, U> c,
                                            Consumer3<T, U, Throwable> onFailure) {
        Checks.checkNotNull(c);
        return (t, u) -> {
            try {
                c.accept(t, u);
            } catch (Throwable ex) {
                if (onFailure != null) {
                    onFailure.accept(t, u, ex);
                }
            }
        };
    }

    /**
     * @see BiConsumer#accept(Object, Object)
     */
    void accept(T t, U u) throws Throwable;
}
