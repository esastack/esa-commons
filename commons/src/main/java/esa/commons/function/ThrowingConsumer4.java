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
 * Represents an operation that accepts four input arguments and returns no result, which allows user to throw a checked
 * exception.
 *
 * @param <T1> the first function argument
 * @param <T2> the second function argument
 * @param <T3> the third function argument
 * @param <T4> the fourth function argument
 * @see Consumer4
 */
@FunctionalInterface
public interface ThrowingConsumer4<T1, T2, T3, T4> {

    /**
     * Wraps a {@link ThrowingConsumer4} to {@link Consumer4} which will rethrow the error thrown by the given {@link
     * ThrowingConsumer4}
     *
     * @param c    target to wrap
     * @param <T1> the first function argument
     * @param <T2> the second function argument
     * @param <T3> the third function argument
     * @param <T4> the fourth function argument
     *
     * @return transferred
     */
    static <T1, T2, T3, T4> Consumer4<T1, T2, T3, T4> rethrow(ThrowingConsumer4<T1, T2, T3, T4> c) {
        Checks.checkNotNull(c);
        return (t1, t2, t3, t4) -> {
            try {
                c.accept(t1, t2, t3, t4);
            } catch (Throwable t) {
                ExceptionUtils.throwException(t);
            }
        };
    }

    /**
     * Wraps a {@link ThrowingConsumer4} to {@link Consumer4} which will suppress the error thrown by the given {@link
     * ThrowingConsumer4}
     *
     * @param c    target to wrap
     * @param <T1> the first function argument
     * @param <T2> the second function argument
     * @param <T3> the third function argument
     * @param <T4> the fourth function argument
     *
     * @return transferred
     */
    static <T1, T2, T3, T4> Consumer4<T1, T2, T3, T4> suppress(ThrowingConsumer4<T1, T2, T3, T4> c) {
        Checks.checkNotNull(c);
        return (t1, t2, t3, t4) -> {
            try {
                c.accept(t1, t2, t3, t4);
            } catch (Throwable ignored) {
            }
        };
    }

    /**
     * @see #failover(ThrowingConsumer4, Consumer5)
     */
    static <T1, T2, T3, T4> Consumer4<T1, T2, T3, T4> failover(ThrowingConsumer4<T1, T2, T3, T4> c,
                                                               Consumer<Throwable> onFailure) {
        return failover(c, (t1, t2, t3, t4, e) -> onFailure.accept(e));
    }

    /**
     * Wraps a {@link ThrowingConsumer4} to {@link Consumer4} which will handle the error thrown by the given {@link
     * ThrowingConsumer4} with the given {@code onFailure} operation of {@link Consumer5}
     *
     * @param c         target to wrap
     * @param onFailure failover
     * @param <T1>      the first function argument
     * @param <T2>      the second function argument
     * @param <T3>      the third function argument
     * @param <T4>      the fourth function argument
     *
     * @return transferred
     */
    static <T1, T2, T3, T4> Consumer4<T1, T2, T3, T4> failover(ThrowingConsumer4<T1, T2, T3, T4> c,
                                                               Consumer5<T1, T2, T3, T4, Throwable> onFailure) {
        Checks.checkNotNull(c);
        return (t1, t2, t3, t4) -> {
            try {
                c.accept(t1, t2, t3, t4);
            } catch (Throwable e) {
                if (onFailure != null) {
                    onFailure.accept(t1, t2, t3, t4, e);
                }
            }
        };
    }

    /**
     * @see Consumer4#accept(Object, Object, Object, Object)
     */
    void accept(T1 t1, T2 t2, T3 t3, T4 t4) throws Throwable;
}
