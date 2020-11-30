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

import java.util.function.DoublePredicate;
import java.util.function.Predicate;

/**
 * Represents a predicate (boolean-valued function) of one {@code double}-valued argument., which allows user to throw a
 * checked exception.
 * @see DoublePredicate
 */
@FunctionalInterface
public interface ThrowingDoublePredicate {

    /**
     * Wraps a {@link ThrowingDoublePredicate} to {@link DoublePredicate} which will rethrow the error thrown by the
     * given {@link ThrowingDoublePredicate}
     *
     * @param p target to wrap
     *
     * @return transferred
     */
    static DoublePredicate rethrow(ThrowingDoublePredicate p) {
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
     * Wraps a {@link ThrowingDoublePredicate} to {@link DoublePredicate} which will use the given value of {@code
     * onFailure} as the result of the function when an error is thrown by the given {@link ThrowingDoublePredicate}
     *
     * @param p         target to wrap
     * @param onFailure onFailure
     *
     * @return transferred
     */
    static DoublePredicate onFailure(ThrowingDoublePredicate p, boolean onFailure) {
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
     * @see #failover(ThrowingDoublePredicate, ObjDoublePredicate)
     */
    static DoublePredicate failover(ThrowingDoublePredicate p,
                                    Predicate<Throwable> failover) {
        return failover(p, (e, t) -> failover.test(e));
    }

    /**
     * Wraps a {@link ThrowingDoublePredicate} to {@link DoublePredicate} which will handle the error thrown by the
     * given {@link ThrowingDoublePredicate} with the given {@code failover} operation of {@link ObjDoublePredicate}
     *
     * @param p        target to wrap
     * @param failover failover
     *
     * @return transferred
     */
    static DoublePredicate failover(ThrowingDoublePredicate p,
                                    ObjDoublePredicate<Throwable> failover) {
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
     * @see DoublePredicate#test(double)
     */
    boolean test(double value) throws Throwable;
}
