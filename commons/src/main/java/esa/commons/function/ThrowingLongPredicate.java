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

import java.util.function.LongPredicate;
import java.util.function.Predicate;

/**
 * Represents a predicate (boolean-valued function) of one {@code int}-valued argument., which allows user to throw a
 * checked exception.
 * @see LongPredicate
 */
@FunctionalInterface
public interface ThrowingLongPredicate {

    /**
     * Wraps a {@link ThrowingLongPredicate} to {@link LongPredicate} which will rethrow the error thrown by the given
     * {@link ThrowingLongPredicate}
     *
     * @param p target to wrap
     *
     * @return transferred
     */
    static LongPredicate rethrow(ThrowingLongPredicate p) {
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
     * Wraps a {@link ThrowingLongPredicate} to {@link LongPredicate} which will use the given value of {@code
     * onFailure} as the result of the function when an error is thrown by the given {@link ThrowingLongPredicate}
     *
     * @param p         target to wrap
     * @param onFailure onFailure
     *
     * @return transferred
     */
    static LongPredicate onFailure(ThrowingLongPredicate p, boolean onFailure) {
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
     * @see #failover(ThrowingLongPredicate, ObjLongPredicate)
     */
    static LongPredicate failover(ThrowingLongPredicate p,
                                  Predicate<Throwable> failover) {
        return failover(p, (e, t) -> failover.test(e));
    }

    /**
     * Wraps a {@link ThrowingLongPredicate} to {@link LongPredicate} which will handle the error thrown by the given
     * {@link ThrowingLongPredicate} with the given {@code failover} operation of {@link ObjIntPredicate}
     *
     * @param p        target to wrap
     * @param failover failover
     *
     * @return transferred
     */
    static LongPredicate failover(ThrowingLongPredicate p,
                                  ObjLongPredicate<Throwable> failover) {
        Checks.checkNotNull(p);
        Checks.checkNotNull(failover);
        return (t) -> {
            try {
                return p.test(t);
            } catch (Throwable e) {
                return failover.test(e, t);
            }
        };
    }

    /**
     * @see LongPredicate#test(long)
     */
    boolean test(long value) throws Throwable;
}
