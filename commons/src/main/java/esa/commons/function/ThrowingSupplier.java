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
import java.util.function.Supplier;

/**
 * Represents a supplier of results, which allows user to throw a checked exception.
 *
 * @param <T> the type of results supplied by this supplier
 * @see Supplier
 */
@FunctionalInterface
public interface ThrowingSupplier<T> {

    /**
     * Wraps a {@link ThrowingSupplier} to {@link Supplier} which will rethrow the error thrown by the given {@link
     * ThrowingSupplier}
     *
     * @param s   target to wrap
     * @param <T> the type of results supplied by this supplier
     *
     * @return transferred
     */
    static <T> Supplier<T> rethrow(ThrowingSupplier<T> s) {
        Checks.checkNotNull(s);
        return () -> {
            try {
                return s.get();
            } catch (Throwable e) {
                ExceptionUtils.throwException(e);
                // never reach this statement
                return null;
            }
        };
    }

    /**
     * Wraps a {@link ThrowingSupplier} to {@link Supplier} which will use the given value of {@code onFailure} as the
     * result of the function when an error is thrown by the given {@link ThrowingSupplier}
     *
     * @param s         target to wrap
     * @param onFailure onFailure
     * @param <T>       the type of results supplied by this supplier
     *
     * @return transferred
     */
    static <T> Supplier<T> onFailure(ThrowingSupplier<T> s, T onFailure) {
        Checks.checkNotNull(s);
        return () -> {
            try {
                return s.get();
            } catch (Throwable e) {
                return onFailure;
            }
        };
    }

    /**
     * Wraps a {@link ThrowingSupplier} to {@link Supplier} which will handle the error thrown by the given {@link
     * ThrowingSupplier} with the given {@code failover} operation of {@link Function}
     *
     * @param s        target to wrap
     * @param failover failover
     * @param <T>      the type of results supplied by this supplier
     *
     * @return transferred
     */
    static <T> Supplier<T> failover(ThrowingSupplier<T> s, Function<Throwable, T> failover) {
        Checks.checkNotNull(s);
        Checks.checkNotNull(failover);
        return () -> {
            try {
                return s.get();
            } catch (Throwable e) {
                return failover.apply(e);
            }
        };
    }

    /**
     * @see Supplier#get()
     */
    T get() throws Throwable;

}
