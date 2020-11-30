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

import java.util.function.DoubleSupplier;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;

/**
 * Represents a supplier of {@code double}-valued results, which allows user to throw a checked exception.
 * @see DoubleSupplier
 */
@FunctionalInterface
public interface ThrowingDoubleSupplier {

    /**
     * Wraps a {@link ThrowingDoubleSupplier} to {@link DoubleSupplier} which will rethrow the error thrown by the given
     * {@link ThrowingDoubleSupplier}
     *
     * @param s target to wrap
     *
     * @return transferred
     */
    static DoubleSupplier rethrow(ThrowingDoubleSupplier s) {
        Checks.checkNotNull(s);
        return () -> {
            try {
                return s.getAsDouble();
            } catch (Throwable e) {
                ExceptionUtils.throwException(e);
                // never reach this statement
                return -1D;
            }
        };
    }

    /**
     * Wraps a {@link ThrowingDoubleSupplier} to {@link DoubleSupplier} which will use the given value of {@code
     * onFailure} as the result of the function when an error is thrown by the given {@link ThrowingDoubleSupplier}
     *
     * @param s         target to wrap
     * @param onFailure onFailure
     *
     * @return transferred
     */
    static DoubleSupplier onFailure(ThrowingDoubleSupplier s, double onFailure) {
        Checks.checkNotNull(s);
        return () -> {
            try {
                return s.getAsDouble();
            } catch (Throwable e) {
                return onFailure;
            }
        };
    }

    /**
     * Wraps a {@link ThrowingDoubleSupplier} to {@link DoubleSupplier} which will handle the error thrown by the given
     * {@link ThrowingDoubleSupplier} with the given {@code failover} operation of {@link ToIntFunction}
     *
     * @param s        target to wrap
     * @param failover failover
     *
     * @return transferred
     */
    static DoubleSupplier failover(ThrowingDoubleSupplier s, ToDoubleFunction<Throwable> failover) {
        Checks.checkNotNull(s);
        Checks.checkNotNull(failover);
        return () -> {
            try {
                return s.getAsDouble();
            } catch (Throwable e) {
                return failover.applyAsDouble(e);
            }
        };
    }

    /**
     * @see DoubleSupplier#getAsDouble()
     */
    double getAsDouble() throws Throwable;

}
