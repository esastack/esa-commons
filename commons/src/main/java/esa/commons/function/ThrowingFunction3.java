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

import java.util.function.Function;

/**
 * Represents a function that accepts three arguments and produces a result, which allows user to throw a checked
 * exception.
 *
 * @param <T1> the first function argument
 * @param <T2> the second function argument
 * @param <T3> the third function argument
 * @param <R>  the type of the result of the function
 * @see Function3
 */
@FunctionalInterface
public interface ThrowingFunction3<T1, T2, T3, R> {

    /**
     * Wraps a {@link ThrowingFunction3} to {@link Function3} which will rethrow the error thrown by the given {@link
     * ThrowingConsumer4}
     *
     * @param f    target to wrap
     * @param <T1> the first function argument
     * @param <T2> the second function argument
     * @param <T3> the third function argument
     * @param <R>  the type of the result of the function
     *
     * @return transferred
     */
    static <T1, T2, T3, R> Function3<T1, T2, T3, R> rethrow(ThrowingFunction3<T1, T2, T3, R> f) {
        Checks.checkNotNull(f);
        return (t1, t2, t3) -> {
            try {
                return f.apply(t1, t2, t3);
            } catch (Throwable e) {
                ExceptionUtils.throwException(e);
                // never reach this statement
                return null;
            }
        };
    }

    /**
     * Wraps a {@link ThrowingFunction3} to {@link Function3} which will use the given value of {@code onFailure} as the
     * result of the function when an error is thrown by the given {@link ThrowingFunction3}
     *
     * @param f         target to wrap
     * @param onFailure onFailure
     * @param <T1>      the first function argument
     * @param <T2>      the second function argument
     * @param <T3>      the third function argument
     * @param <R>       the type of the result of the function
     *
     * @return transferred
     */
    static <T1, T2, T3, R> Function3<T1, T2, T3, R> onFailure(ThrowingFunction3<T1, T2, T3, R> f,
                                                              R onFailure) {
        Checks.checkNotNull(f);
        return (t1, t2, t3) -> {
            try {
                return f.apply(t1, t2, t3);
            } catch (Throwable e) {
                return onFailure;
            }
        };
    }

    /**
     * @see #failover(ThrowingFunction3, Function4)
     */
    static <T1, T2, T3, R> Function3<T1, T2, T3, R> failover(ThrowingFunction3<T1, T2, T3, R> f,
                                                             Function<Throwable, R> failover) {
        return failover(f, (t1, t2, t3, e) -> failover.apply(e));
    }

    /**
     * Wraps a {@link ThrowingFunction3} to {@link Function3} which will handle the error thrown by the given {@link
     * ThrowingFunction3} with the given {@code failover} operation of {@link Function4}
     *
     * @param f        target to wrap
     * @param failover failover
     * @param <T1>     the first function argument
     * @param <T2>     the second function argument
     * @param <T3>     the third function argument
     * @param <R>      the type of the result of the function
     *
     * @return transferred
     */
    static <T1, T2, T3, R> Function3<T1, T2, T3, R> failover(ThrowingFunction3<T1, T2, T3, R> f,
                                                             Function4<T1, T2, T3, Throwable, R> failover) {
        Checks.checkNotNull(f);
        Checks.checkNotNull(failover);
        return (t1, t2, t3) -> {
            try {
                return f.apply(t1, t2, t3);
            } catch (Throwable e) {
                return failover.apply(t1, t2, t3, e);
            }
        };
    }

    /**
     * @see Function3#apply(Object, Object, Object)
     */
    R apply(T1 t1, T2 t2, T3 t3) throws Throwable;
}
