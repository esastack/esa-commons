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

import java.util.function.Consumer;

/**
 * Represents an operation that accepts three input arguments and returns no result, which allows user to throw a
 * checked exception.
 *
 * @param <T1> the first function argument
 * @param <T2> the second function argument
 * @param <T3> the third function argument
 * @see Consumer3
 */
@FunctionalInterface
public interface ThrowingConsumer3<T1, T2, T3> {

    /**
     * Wraps a {@link ThrowingConsumer3} to {@link Consumer3} which will rethrow the error thrown by the given {@link
     * ThrowingConsumer3}
     *
     * @param c    target to wrap
     * @param <T1> the first function argument
     * @param <T2> the second function argument
     * @param <T3> the third function argument
     *
     * @return transferred
     */
    static <T1, T2, T3> Consumer3<T1, T2, T3> rethrow(ThrowingConsumer3<T1, T2, T3> c) {

        Checks.checkNotNull(c);
        return (t1, t2, t3) -> {
            try {
                c.accept(t1, t2, t3);
            } catch (Throwable e) {
                ExceptionUtils.throwException(e);
            }
        };
    }

    /**
     * Wraps a {@link ThrowingConsumer3} to {@link Consumer3} which will suppress the error thrown by the given {@link
     * ThrowingConsumer3}
     *
     * @param c    target to wrap
     * @param <T1> the first function argument
     * @param <T2> the second function argument
     * @param <T3> the third function argument
     *
     * @return transferred
     */
    static <T1, T2, T3> Consumer3<T1, T2, T3> suppress(ThrowingConsumer3<T1, T2, T3> c) {
        Checks.checkNotNull(c);
        return (t1, t2, t3) -> {
            try {
                c.accept(t1, t2, t3);
            } catch (Throwable ignored) {
            }
        };
    }

    /**
     * @see #failover(ThrowingConsumer3, Consumer4)
     */
    static <T1, T2, T3> Consumer3<T1, T2, T3> failover(ThrowingConsumer3<T1, T2, T3> c,
                                                       Consumer<Throwable> onFailure) {
        return failover(c, (t1, t2, t3, e) -> onFailure.accept(e));
    }

    /**
     * Wraps a {@link ThrowingConsumer3} to {@link Consumer3} which will handle the error thrown by the given {@link
     * ThrowingConsumer3} with the given {@code onFailure} operation of {@link Consumer4}
     *
     * @param c         target to wrap
     * @param onFailure failover
     * @param <T1>      the first function argument
     * @param <T2>      the second function argument
     * @param <T3>      the third function argument
     *
     * @return transferred
     */
    static <T1, T2, T3> Consumer3<T1, T2, T3> failover(ThrowingConsumer3<T1, T2, T3> c,
                                                       Consumer4<T1, T2, T3, Throwable> onFailure) {
        Checks.checkNotNull(c);
        return (t1, t2, t3) -> {
            try {
                c.accept(t1, t2, t3);
            } catch (Throwable e) {
                if (onFailure != null) {
                    onFailure.accept(t1, t2, t3, e);
                }
            }
        };
    }

    /**
     * @see Consumer3#accept(Object, Object, Object)
     */
    void accept(T1 t1, T2 t2, T3 t3) throws Throwable;
}
