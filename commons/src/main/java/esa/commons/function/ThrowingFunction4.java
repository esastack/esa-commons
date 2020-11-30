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
 * Represents a function that accepts four arguments and produces a result, which allows user to throw a checked
 * exception.
 *
 * @param <T1> the first function argument
 * @param <T2> the second function argument
 * @param <T3> the third function argument
 * @param <T4> the fourth function argument
 * @param <R>  the type of the result of the function
 * @see Function4
 */
@FunctionalInterface
public interface ThrowingFunction4<T1, T2, T3, T4, R> {

    /**
     * Wraps a {@link ThrowingFunction4} to {@link Function4} which will rethrow the error thrown by the given {@link
     * ThrowingFunction4}
     *
     * @param f    target to wrap
     * @param <T1> the first function argument
     * @param <T2> the second function argument
     * @param <T3> the third function argument
     * @param <T4> the fourth function argument
     * @param <R>  the type of the result of the function
     *
     * @return transferred
     */
    static <T1, T2, T3, T4, R> Function4<T1, T2, T3, T4, R> rethrow(ThrowingFunction4<T1, T2, T3, T4, R> f) {
        Checks.checkNotNull(f);
        return (t1, t2, t3, t4) -> {
            try {
                return f.apply(t1, t2, t3, t4);
            } catch (Throwable e) {
                ExceptionUtils.throwException(e);
                // never reach this statement
                return null;
            }
        };
    }

    /**
     * Wraps a {@link ThrowingFunction4} to {@link Function4} which will use the given value of {@code onFailure} as the
     * result of the function when an error is thrown by the given {@link ThrowingFunction4}
     *
     * @param f         target to wrap
     * @param onFailure failover
     * @param <T1>      the first function argument
     * @param <T2>      the second function argument
     * @param <T3>      the third function argument
     * @param <T4>      the fourth function argument
     * @param <R>       the type of the result of the function
     *
     * @return transferred
     */
    static <T1, T2, T3, T4, R> Function4<T1, T2, T3, T4, R> onFailure(ThrowingFunction4<T1, T2, T3, T4, R> f,
                                                                      R onFailure) {
        Checks.checkNotNull(f);
        return (t1, t2, t3, t4) -> {
            try {
                return f.apply(t1, t2, t3, t4);
            } catch (Throwable e) {
                return onFailure;
            }
        };
    }

    /**
     * @see #failover(ThrowingFunction4, Function5)
     */
    static <T1, T2, T3, T4, R> Function4<T1, T2, T3, T4, R> failover(ThrowingFunction4<T1, T2, T3, T4, R> f,
                                                                     Function<Throwable, R> failover) {
        return failover(f, (t1, t2, t3, t4, e) -> failover.apply(e));
    }

    /**
     * Wraps a {@link ThrowingFunction4} to {@link Function4} which will handle the error thrown by the given {@link
     * ThrowingFunction4} with the given {@code failover} operation of {@link Function5}
     *
     * @param f        target to wrap
     * @param failover failover
     * @param <T1>     the first function argument
     * @param <T2>     the second function argument
     * @param <T3>     the third function argument
     * @param <T4>     the fourth function argument
     * @param <R>      the type of the result of the function
     *
     * @return transferred
     */
    static <T1, T2, T3, T4, R> Function4<T1, T2, T3, T4, R> failover(ThrowingFunction4<T1, T2, T3, T4, R> f,
                                                                     Function5<T1, T2, T3, T4, Throwable, R> failover) {
        Checks.checkNotNull(f);
        Checks.checkNotNull(failover);
        return (t1, t2, t3, t4) -> {
            try {
                return f.apply(t1, t2, t3, t4);
            } catch (Throwable e) {
                return failover.apply(t1, t2, t3, t4, e);
            }
        };
    }

    /**
     * @see Function4#apply(Object, Object, Object, Object)
     */
    R apply(T1 t1, T2 t2, T3 t3, T4 t4) throws Throwable;
}
