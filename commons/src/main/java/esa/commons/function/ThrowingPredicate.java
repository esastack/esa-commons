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
 * Represents a predicate (boolean-valued function) of one argument, which allows user to throw a checked exception.
 *
 * @param <T> the type of the input to the predicate
 * @see Predicate
 */
@FunctionalInterface
public interface ThrowingPredicate<T> {

    /**
     * Wraps a {@link ThrowingPredicate} to {@link Predicate} which will rethrow the error thrown by the given {@link
     * ThrowingPredicate}
     *
     * @param p   target to wrap
     * @param <T> the type of the input to the predicate
     *
     * @return transferred
     */
    static <T> Predicate<T> rethrow(ThrowingPredicate<T> p) {
        Checks.checkNotNull(p);
        return (t) -> {
            try {
                return p.test(t);
            } catch (Throwable e) {
                ExceptionUtils.throwException(e);
                // never reach this statement
                return false;
            }
        };
    }

    /**
     * Wraps a {@link ThrowingPredicate} to {@link Predicate} which will use the given value of {@code onFailure} as the
     * result of the function when an error is thrown by the given {@link ThrowingPredicate}
     *
     * @param p         target to wrap
     * @param onFailure onFailure
     * @param <T>       the type of the input to the predicate
     *
     * @return transferred
     */
    static <T> Predicate<T> onFailure(ThrowingPredicate<T> p,
                                      boolean onFailure) {
        Checks.checkNotNull(p);
        return (t) -> {
            try {
                return p.test(t);
            } catch (Throwable e) {
                return onFailure;
            }
        };
    }

    /**
     * @see #failover(ThrowingPredicate, BiPredicate)
     */
    static <T> Predicate<T> failover(ThrowingPredicate<T> p,
                                     Predicate<Throwable> failover) {
        return failover(p, (t, e) -> failover.test(e));
    }

    /**
     * Wraps a {@link ThrowingPredicate} to {@link Predicate} which will handle the error thrown by the given {@link
     * ThrowingPredicate} with the given {@code failover} operation of {@link BiPredicate}
     *
     * @param p        target to wrap
     * @param failover failover
     * @param <T>      the type of the input to the predicate
     *
     * @return transferred
     */
    static <T> Predicate<T> failover(ThrowingPredicate<T> p,
                                     BiPredicate<T, Throwable> failover) {
        Checks.checkNotNull(p);
        Checks.checkNotNull(failover);
        return (t) -> {
            try {
                return p.test(t);
            } catch (Throwable e) {
                return failover.test(t, e);
            }
        };
    }

    /**
     * @see Predicate#test(Object)
     */
    boolean test(T t) throws Throwable;
}
