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

import java.util.function.BiPredicate;
import java.util.function.Predicate;

/**
 * Represents a predicate (boolean-valued function) of two arguments, which allows user to throw a checked exception.
 *
 * @param <T> the type of the first argument to the predicate
 * @param <U> the type of the second argument the predicate
 * @see BiPredicate
 */
@FunctionalInterface
public interface ThrowingBiPredicate<T, U> {

    /**
     * Wraps a {@link ThrowingBiPredicate} to {@link BiPredicate} which will rethrow the error thrown by the given
     * {@link ThrowingBiPredicate}
     *
     * @param p   target to wrap
     * @param <T> the type of the first argument to the predicate
     * @param <U> the type of the second argument the predicate
     *
     * @return transferred
     */
    static <T, U> BiPredicate<T, U> rethrow(ThrowingBiPredicate<T, U> p) {
        Checks.checkNotNull(p);
        return (t, u) -> {
            try {
                return p.test(t, u);
            } catch (Throwable e) {
                ExceptionUtils.throwException(e);
                // never reach this statement
                return true;
            }
        };
    }

    /**
     * Wraps a {@link ThrowingBiPredicate} to {@link BiPredicate} which will use the given value of {@code onFailure} as
     * the result of the function when an error is thrown by the given {@link ThrowingBiPredicate}
     *
     * @param p         target to wrap
     * @param onFailure onFailure
     * @param <T>       the type of the first argument to the predicate
     * @param <U>       the type of the second argument the predicate
     *
     * @return transferred
     */
    static <T, U> BiPredicate<T, U> onFailure(ThrowingBiPredicate<T, U> p,
                                              boolean onFailure) {
        Checks.checkNotNull(p);
        return (t, u) -> {
            try {
                return p.test(t, u);
            } catch (Throwable e) {
                return onFailure;
            }
        };
    }

    /**
     * @see #failover(ThrowingBiPredicate, Predicate3)
     */
    static <T, U> BiPredicate<T, U> failover(ThrowingBiPredicate<T, U> p,
                                             Predicate<Throwable> failover) {
        return failover(p, (t, u, e) -> failover.test(e));
    }

    /**
     * Wraps a {@link ThrowingBiPredicate} to {@link BiPredicate} which will handle the error thrown by the given {@link
     * ThrowingBiPredicate} with the given {@code failover} operation of {@link Predicate3}
     *
     * @param p        target to wrap
     * @param failover failover
     * @param <T>      the type of the first argument to the predicate
     * @param <U>      the type of the second argument the predicate
     *
     * @return transferred
     */
    static <T, U> BiPredicate<T, U> failover(ThrowingBiPredicate<T, U> p,
                                             Predicate3<T, U, Throwable> failover) {
        Checks.checkNotNull(p);
        Checks.checkNotNull(failover);
        return (t, u) -> {
            try {
                return p.test(t, u);
            } catch (Throwable e) {
                return failover.test(t, u, e);
            }
        };
    }

    /**
     * @see BiPredicate#test(Object, Object)
     */
    boolean test(T t, U u) throws Throwable;
}
