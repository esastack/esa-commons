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

import java.util.function.ToIntBiFunction;
import java.util.function.ToIntFunction;

/**
 * Represents a function that produces an int-valued result, which allows user to throw a checked exception.
 *
 * @param <T> the type of the result of the function
 * @see ToIntFunction
 */
@FunctionalInterface
public interface ThrowingToIntFunction<T> {

    /**
     * Wraps a {@link ThrowingToIntFunction} to {@link ToIntFunction} which will rethrow the error thrown by the given
     * {@link ThrowingToIntFunction}
     *
     * @param f   target to wrap
     * @param <T> the type of the result of the function
     *
     * @return transferred
     */
    static <T> ToIntFunction<T> rethrow(ThrowingToIntFunction<T> f) {
        Checks.checkNotNull(f);
        return (t) -> {
            try {
                return f.applyAsInt(t);
            } catch (Throwable e) {
                ExceptionUtils.throwException(e);
                // never reach this statement
                return -1;
            }
        };
    }

    /**
     * Wraps a {@link ThrowingToIntFunction} to {@link ToIntFunction} which will use the given value of {@code
     * onFailure} as the result of the function when an error is thrown by the given {@link ThrowingToIntFunction}
     *
     * @param f         target to wrap
     * @param onFailure onFailure
     * @param <T>       the type of the result of the function
     *
     * @return transferred
     */
    static <T> ToIntFunction<T> onFailure(ThrowingToIntFunction<T> f,
                                          int onFailure) {
        Checks.checkNotNull(f);
        return (t) -> {
            try {
                return f.applyAsInt(t);
            } catch (Throwable e) {
                return onFailure;
            }
        };
    }

    /**
     * @see #failover(ThrowingToIntFunction, ToIntBiFunction)
     */
    static <T> ToIntFunction<T> failover(ThrowingToIntFunction<T> f,
                                         ToIntFunction<Throwable> failover) {
        return failover(f, (t, e) -> failover.applyAsInt(e));
    }

    /**
     * Wraps a {@link ThrowingToIntFunction} to {@link ToIntFunction} which will handle the error thrown by the given
     * {@link ThrowingToIntFunction} with the given {@code failover} operation of {@link ToIntBiFunction}
     *
     * @param f        target to wrap
     * @param failover failover
     * @param <T>      the type of the result of the function
     *
     * @return transferred
     */
    static <T> ToIntFunction<T> failover(ThrowingToIntFunction<T> f,
                                         ToIntBiFunction<T, Throwable> failover) {
        Checks.checkNotNull(f);
        Checks.checkNotNull(failover);
        return (t) -> {
            try {
                return f.applyAsInt(t);
            } catch (Throwable e) {
                return failover.applyAsInt(t, e);
            }
        };
    }

    /**
     * @see ToIntFunction#applyAsInt(Object)
     */
    int applyAsInt(T value) throws Throwable;

}
