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

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Represents a function that accepts one argument and produces a result, which allows user to throw a checked
 * exception.
 *
 * @param <T> the type of the input to the function
 * @param <R> the type of the result of the function
 * @see Function
 */
@FunctionalInterface
public interface ThrowingFunction<T, R> {

    /**
     * Wraps a {@link ThrowingFunction} to {@link Function} which will rethrow the error thrown by the given {@link
     * ThrowingFunction}
     *
     * @param f   target to wrap
     * @param <T> the type of the input to the operation
     * @param <R> the type of the result of the function
     *
     * @return transferred
     */
    static <T, R> Function<T, R> rethrow(ThrowingFunction<T, R> f) {
        Checks.checkNotNull(f);
        return (t) -> {
            try {
                return f.apply(t);
            } catch (Throwable e) {
                ExceptionUtils.throwException(e);
                // never reach this statement
                return null;
            }
        };
    }

    /**
     * Wraps a {@link ThrowingFunction} to {@link Function} which will use the given value of {@code onFailure} as the
     * result of the function when an error is thrown by the given {@link ThrowingFunction}
     *
     * @param f         target to wrap
     * @param onFailure onFailure
     * @param <T>       the type of the input to the operation
     * @param <R>       the type of the result of the function
     *
     * @return transferred
     */
    static <T, R> Function<T, R> onFailure(ThrowingFunction<T, R> f,
                                           R onFailure) {
        Checks.checkNotNull(f);
        return (t) -> {
            try {
                return f.apply(t);
            } catch (Throwable e) {
                return onFailure;
            }
        };
    }

    /**
     * @see #failover(ThrowingFunction, BiFunction)
     */
    static <T, R> Function<T, R> failover(ThrowingFunction<T, R> f,
                                          Function<Throwable, R> failover) {
        return failover(f, (t, e) -> failover.apply(e));
    }

    /**
     * Wraps a {@link ThrowingFunction} to {@link Function} which will handle the error thrown by the given {@link
     * ThrowingFunction} with the given {@code failover} operation of {@link BiFunction}
     *
     * @param f        target to wrap
     * @param failover failover
     * @param <T>      the type of the input to the operation
     * @param <R>      the type of the result of the function
     *
     * @return transferred
     */
    static <T, R> Function<T, R> failover(ThrowingFunction<T, R> f,
                                          BiFunction<T, Throwable, R> failover) {
        Checks.checkNotNull(f);
        Checks.checkNotNull(failover);
        return (t) -> {
            try {
                return f.apply(t);
            } catch (Throwable e) {
                return failover.apply(t, e);
            }
        };
    }

    /**
     * @see Function#apply(Object)
     */
    R apply(T t) throws Throwable;

}
