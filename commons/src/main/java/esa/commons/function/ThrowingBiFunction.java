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
 * Represents a function that accepts two arguments and produces a result, which allows user to throw a checked
 * exception.
 *
 * @param <T> the type of the first argument to the function
 * @param <U> the type of the second argument to the function
 * @param <R> the type of the result of the function
 * @see BiFunction
 */
@FunctionalInterface
public interface ThrowingBiFunction<T, U, R> {

    /**
     * Wraps a {@link ThrowingBiFunction} to {@link BiFunction} which will rethrow the error thrown by the given {@link
     * ThrowingBiFunction}
     *
     * @param f   target to wrap
     * @param <T> the type of the first argument to the function
     * @param <U> the type of the second argument to the function
     * @param <R> the type of the result of the function
     *
     * @return transferred
     */
    static <T, U, R> BiFunction<T, U, R> rethrow(ThrowingBiFunction<T, U, R> f) {
        Checks.checkNotNull(f);
        return (t, u) -> {
            try {
                return f.apply(t, u);
            } catch (Throwable e) {
                ExceptionUtils.throwException(e);
                // never reach this statement
                return null;
            }
        };
    }

    /**
     * Wraps a {@link ThrowingBiFunction} to {@link BiFunction} which will use the given value of {@code onFailure} as
     * the result of the function when an error is thrown by the given {@link ThrowingBiFunction}
     *
     * @param f         target to wrap
     * @param onFailure onFailure
     * @param <T>       the type of the first argument to the function
     * @param <U>       the type of the second argument to the function
     * @param <R>       the type of the result of the function
     *
     * @return transferred
     */
    static <T, U, R> BiFunction<T, U, R> onFailure(ThrowingBiFunction<T, U, R> f,
                                                   R onFailure) {
        Checks.checkNotNull(f);
        return (t, u) -> {
            try {
                return f.apply(t, u);
            } catch (Throwable e) {
                return onFailure;
            }
        };
    }

    /**
     * @see #failover(ThrowingBiFunction, Function3)
     */
    static <T, U, R> BiFunction<T, U, R> failover(ThrowingBiFunction<T, U, R> f,
                                                  Function<Throwable, R> failover) {
        return failover(f, (t, u, e) -> failover.apply(e));
    }

    /**
     * Wraps a {@link ThrowingBiFunction} to {@link BiFunction} which will handle the error thrown by the given {@link
     * ThrowingBiFunction} with the given {@code failover} operation of {@link Function3}
     *
     * @param f        target to wrap
     * @param failover failover
     * @param <T>      the type of the first argument to the function
     * @param <U>      the type of the second argument to the function
     * @param <R>      the type of the result of the function
     *
     * @return transferred
     */
    static <T, U, R> BiFunction<T, U, R> failover(ThrowingBiFunction<T, U, R> f,
                                                  Function3<T, U, Throwable, R> failover) {
        Checks.checkNotNull(f);
        Checks.checkNotNull(failover);
        return (t, u) -> {
            try {
                return f.apply(t, u);
            } catch (Throwable e) {
                return failover.apply(t, u, e);
            }
        };
    }

    /**
     * @see BiFunction#apply(Object, Object)
     */
    R apply(T t, U u) throws Throwable;
}
