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
import java.util.function.Predicate;

/**
 * Represents a predicate (boolean-valued function) of four arguments, which allows user to throw a checked exception.
 *
 * @param <T1> the first function argument
 * @param <T2> the second function argument
 * @param <T3> the third function argument
 * @param <T4> the fourth function argument
 *
 * @see Predicate4
 */
@FunctionalInterface
public interface ThrowingPredicate4<T1, T2, T3, T4> {

    /**
     * Wraps a {@link ThrowingPredicate4} to {@link Predicate4} which will rethrow the error thrown by the given {@link
     * ThrowingPredicate4}
     *
     * @param p    target to wrap
     * @param <T1> the first function argument
     * @param <T2> the second function argument
     * @param <T3> the third function argument
     * @param <T4> the fourth function argument
     *
     * @return transferred
     */
    static <T1, T2, T3, T4> Predicate4<T1, T2, T3, T4> rethrow(ThrowingPredicate4<T1, T2, T3, T4> p) {
        Checks.checkNotNull(p);
        return (t1, t2, t3, t4) -> {
            try {
                return p.test(t1, t2, t3, t4);
            } catch (Throwable e) {
                ExceptionUtils.throwException(e);
                // never reach this statement
                return false;
            }
        };
    }

    /**
     * Wraps a {@link ThrowingPredicate4} to {@link Predicate4} which will use the given value of {@code onFailure} as
     * the result of the function when an error is thrown by the given {@link ThrowingPredicate4}
     *
     * @param p         target to wrap
     * @param onFailure onFailure
     * @param <T1>      the first function argument
     * @param <T2>      the second function argument
     * @param <T3>      the third function argument
     * @param <T4>      the fourth function argument
     *
     * @return transferred
     */
    static <T1, T2, T3, T4> Predicate4<T1, T2, T3, T4> onFailure(ThrowingPredicate4<T1, T2, T3, T4> p,
                                                                 boolean onFailure) {
        Checks.checkNotNull(p);
        return (t1, t2, t3, t4) -> {
            try {
                return p.test(t1, t2, t3, t4);
            } catch (Throwable e) {
                return onFailure;
            }
        };
    }

    /**
     * @see #failover(ThrowingPredicate4, Predicate5)
     */
    static <T1, T2, T3, T4> Predicate4<T1, T2, T3, T4> failover(ThrowingPredicate4<T1, T2, T3, T4> p,
                                                                Predicate<Throwable> failover) {
        return failover(p, (t1, t2, t3, t4, e) -> failover.test(e));
    }

    /**
     * Wraps a {@link ThrowingPredicate4} to {@link Predicate4} which will handle the error thrown by the given {@link
     * ThrowingPredicate4} with the given {@code failover} operation of {@link Function}
     *
     * @param p        target to wrap
     * @param failover failover
     * @param <T1>     the first function argument
     * @param <T2>     the second function argument
     * @param <T3>     the third function argument
     * @param <T4>     the fourth function argument
     *
     * @return transferred
     */
    static <T1, T2, T3, T4> Predicate4<T1, T2, T3, T4> failover(ThrowingPredicate4<T1, T2, T3, T4> p,
                                                                Predicate5<T1, T2, T3, T4, Throwable> failover) {
        Checks.checkNotNull(p);
        Checks.checkNotNull(failover);
        return (t1, t2, t3, t4) -> {
            try {
                return p.test(t1, t2, t3, t4);
            } catch (Throwable e) {
                return failover.test(t1, t2, t3, t4, e);
            }
        };
    }


    /**
     * @see Predicate4#test(Object, Object, Object, Object)
     */
    boolean test(T1 t1, T2 t2, T3 t3, T4 t4) throws Throwable;
}
