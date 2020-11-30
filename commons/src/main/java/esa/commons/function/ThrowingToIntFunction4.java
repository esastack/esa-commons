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

import java.util.function.ToIntFunction;

/**
 * Represents a function that produces an int-valued result, which allows user to throw a checked exception.
 *
 * @param <T1> the first function argument
 * @param <T2> the second function argument
 * @param <T3> the third function argument
 * @param <T4> the fourth function argument
 * @see ToIntFunction4
 */
@FunctionalInterface
public interface ThrowingToIntFunction4<T1, T2, T3, T4> {

    /**
     * Wraps a {@link ThrowingToIntFunction4} to {@link ToIntFunction4} which will rethrow the error thrown by the given
     * {@link ThrowingToIntFunction4}
     *
     * @param f    target to wrap
     * @param <T1> the first function argument
     * @param <T2> the second function argument
     * @param <T3> the third function argument
     * @param <T4> the fourth function argument
     *
     * @return transferred
     */
    static <T1, T2, T3, T4> ToIntFunction4<T1, T2, T3, T4> rethrow(ThrowingToIntFunction4<T1, T2, T3, T4> f) {
        Checks.checkNotNull(f);
        return (t1, t2, t3, t4) -> {
            try {
                return f.applyAsInt(t1, t2, t3, t4);
            } catch (Throwable e) {
                ExceptionUtils.throwException(e);
                // never reach this statement
                return -1;
            }
        };
    }

    /**
     * Wraps a {@link ThrowingToIntFunction4} to {@link ToIntFunction4} which will use the given value of {@code
     * onFailure} as the result of the function when an error is thrown by the given {@link ThrowingToIntFunction4}
     *
     * @param f         target to wrap
     * @param onFailure failover
     * @param <T1>      the first function argument
     * @param <T2>      the second function argument
     * @param <T3>      the third function argument
     * @param <T4>      the fourth function argument
     *
     * @return transferred
     */
    static <T1, T2, T3, T4> ToIntFunction4<T1, T2, T3, T4> onFailure(ThrowingToIntFunction4<T1, T2, T3, T4> f,
                                                                     int onFailure) {
        Checks.checkNotNull(f);
        return (t1, t2, t3, t4) -> {
            try {
                return f.applyAsInt(t1, t2, t3, t4);
            } catch (Throwable e) {
                return onFailure;
            }
        };
    }

    /**
     * @see #failover(ThrowingToIntFunction4, ToIntFunction5)
     */
    static <T1, T2, T3, T4> ToIntFunction4<T1, T2, T3, T4> failover(ThrowingToIntFunction4<T1, T2, T3, T4> f,
                                                                    ToIntFunction<Throwable> failover) {
        return failover(f, (t1, t2, t3, t4, e) -> failover.applyAsInt(e));
    }

    /**
     * Wraps a {@link ThrowingToIntFunction4} to {@link ToIntFunction4} which will handle the error thrown by the given
     * {@link ThrowingToIntFunction4} with the given {@code failover} operation of {@link Function5}
     *
     * @param fn        target to wrap
     * @param f failover
     * @param <T1>     the first function argument
     * @param <T2>     the second function argument
     * @param <T3>     the third function argument
     * @param <T4>     the fourth function argument
     *
     * @return transferred
     */
    static <T1, T2, T3, T4> ToIntFunction4<T1, T2, T3, T4> failover(ThrowingToIntFunction4<T1, T2, T3, T4> fn,
                                                                    ToIntFunction5<T1, T2, T3, T4, Throwable> f) {
        Checks.checkNotNull(fn);
        Checks.checkNotNull(f);
        return (t1, t2, t3, t4) -> {
            try {
                return fn.applyAsInt(t1, t2, t3, t4);
            } catch (Throwable e) {
                return f.applyAsInt(t1, t2, t3, t4, e);
            }
        };
    }

    /**
     * @see ToIntFunction4#applyAsInt(Object, Object, Object, Object)
     */
    int applyAsInt(T1 t1, T2 t2, T3 t3, T4 t4) throws Throwable;
}
