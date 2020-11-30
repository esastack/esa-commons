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

import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

/**
 * Represents a supplier of {@code boolean}-valued results, which allows user to throw a checked exception.
 * @see BooleanSupplier
 */
@FunctionalInterface
public interface ThrowingBooleanSupplier {

    /**
     * Wraps a {@link ThrowingBooleanSupplier} to {@link BooleanSupplier} which will rethrow the error thrown by the
     * given {@link ThrowingBooleanSupplier}
     *
     * @param s target to wrap
     *
     * @return transferred
     */
    static BooleanSupplier rethrow(ThrowingBooleanSupplier s) {
        Checks.checkNotNull(s);
        return () -> {
            try {
                return s.getAsBoolean();
            } catch (Throwable e) {
                ExceptionUtils.throwException(e);
                // never reach this statement
                return false;
            }
        };
    }

    /**
     * Wraps a {@link ThrowingBooleanSupplier} to {@link BooleanSupplier} which will use the given value of {@code
     * onFailure} as the result of the function when an error is thrown by the given {@link ThrowingBooleanSupplier}
     *
     * @param s         target to wrap
     * @param onFailure onFailure
     *
     * @return transferred
     */
    static BooleanSupplier onFailure(ThrowingBooleanSupplier s, boolean onFailure) {
        Checks.checkNotNull(s);
        return () -> {
            try {
                return s.getAsBoolean();
            } catch (Throwable e) {
                return onFailure;
            }
        };
    }

    /**
     * Wraps a {@link ThrowingBooleanSupplier} to {@link BooleanSupplier} which will handle the error thrown by the
     * given {@link ThrowingBooleanSupplier} with the given {@code failover} operation of {@link Predicate}
     *
     * @param s        target to wrap
     * @param failover failover
     *
     * @return transferred
     */
    static BooleanSupplier failover(ThrowingBooleanSupplier s, Predicate<Throwable> failover) {
        Checks.checkNotNull(s);
        Checks.checkNotNull(failover);
        return () -> {
            try {
                return s.getAsBoolean();
            } catch (Throwable e) {
                return failover.test(e);
            }
        };
    }

    /**
     * @see BooleanSupplier#getAsBoolean()
     */
    boolean getAsBoolean() throws Throwable;

}
