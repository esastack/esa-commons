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

import java.util.function.Predicate;

/**
 * Represents a predicate (boolean-valued function) of three arguments, which allows user to throw a checked exception.
 *
 * @param <T1> the first function argument
 * @param <T2> the second function argument
 * @param <T3> the third function argument
 * @see Predicate3
 */
@FunctionalInterface
public interface ThrowingPredicate3<T1, T2, T3> {

    /**
     * Wraps a {@link ThrowingPredicate3} to {@link Predicate3} which will rethrow the error thrown by the given {@link
     * ThrowingPredicate3}
     *
     * @param p    target to wrap
     * @param <T1> the first function argument
     * @param <T2> the second function argument
     * @param <T3> the third function argument
     *
     * @return transferred
     */
    static <T1, T2, T3> Predicate3<T1, T2, T3> rethrow(ThrowingPredicate3<T1, T2, T3> p) {
        Checks.checkNotNull(p);
        return (t1, t2, t3) -> {
            try {
                return p.test(t1, t2, t3);
            } catch (Throwable e) {
                ExceptionUtils.throwException(e);
                // never reach this statement
                return false;
            }
        };
    }

    /**
     * Wraps a {@link ThrowingBiPredicate} to {@link Predicate3} which will use the given value of {@code onFailure} as
     * the result of the function when an error is thrown by the given {@link ThrowingPredicate3}
     *
     * @param p         target to wrap
     * @param onFailure onFailure
     * @param <T1>      the first function argument
     * @param <T2>      the second function argument
     * @param <T3>      the third function argument
     *
     * @return transferred
     */
    static <T1, T2, T3> Predicate3<T1, T2, T3> onFailure(ThrowingPredicate3<T1, T2, T3> p,
                                                         boolean onFailure) {
        Checks.checkNotNull(p);
        return (t1, t2, t3) -> {
            try {
                return p.test(t1, t2, t3);
            } catch (Throwable e) {
                return onFailure;
            }
        };
    }

    /**
     * @see #failover(ThrowingPredicate3, Predicate4)
     */
    static <T1, T2, T3> Predicate3<T1, T2, T3> failover(ThrowingPredicate3<T1, T2, T3> p,
                                                        Predicate<Throwable> failover) {
        return failover(p, (t1, t2, t3, e) -> failover.test(e));
    }

    /**
     * Wraps a {@link ThrowingPredicate3} to {@link Predicate3} which will handle the error thrown by the given {@link
     * ThrowingPredicate3} with the given {@code failover} operation of {@link Predicate4}
     *
     * @param p        target to wrap
     * @param failover failover
     * @param <T1>     the first function argument
     * @param <T2>     the second function argument
     * @param <T3>     the third function argument
     *
     * @return transferred
     */
    static <T1, T2, T3> Predicate3<T1, T2, T3> failover(ThrowingPredicate3<T1, T2, T3> p,
                                                        Predicate4<T1, T2, T3, Throwable> failover) {
        Checks.checkNotNull(p);
        Checks.checkNotNull(failover);
        return (t1, t2, t3) -> {
            try {
                return p.test(t1, t2, t3);
            } catch (Throwable e) {
                return failover.test(t1, t2, t3, e);
            }
        };
    }

    /**
     * @see Predicate3#test(Object, Object, Object)
     */
    boolean test(T1 t1, T2 t2, T3 t3) throws Throwable;
}
