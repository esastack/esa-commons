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

import java.util.function.ToDoubleBiFunction;
import java.util.function.ToDoubleFunction;

/**
 * Represents a function that produces an double-valued result, which allows user to throw a checked exception.
 *
 * @param <T> the type of the result of the function
 * @see ToDoubleFunction
 */
@FunctionalInterface
public interface ThrowingToDoubleFunction<T> {

    /**
     * Wraps a {@link ThrowingToDoubleFunction} to {@link ToDoubleFunction} which will rethrow the error thrown by the
     * given {@link ThrowingToDoubleFunction}
     *
     * @param f   target to wrap
     * @param <T> the type of the result of the function
     *
     * @return transferred
     */
    static <T> ToDoubleFunction<T> rethrow(ThrowingToDoubleFunction<T> f) {
        Checks.checkNotNull(f);
        return (t) -> {
            try {
                return f.applyAsDouble(t);
            } catch (Throwable e) {
                ExceptionUtils.throwException(e);
                // never reach this statement
                return -1D;
            }
        };
    }

    /**
     * Wraps a {@link ThrowingToDoubleFunction} to {@link ToDoubleFunction} which will use the given value of {@code
     * onFailure} as the result of the function when an error is thrown by the given {@link ThrowingToDoubleFunction}
     *
     * @param f         target to wrap
     * @param onFailure onFailure
     * @param <T>       the type of the result of the function
     *
     * @return transferred
     */
    static <T> ToDoubleFunction<T> onFailure(ThrowingToDoubleFunction<T> f,
                                             double onFailure) {
        Checks.checkNotNull(f);
        return (t) -> {
            try {
                return f.applyAsDouble(t);
            } catch (Throwable e) {
                return onFailure;
            }
        };
    }

    /**
     * @see #failover(ThrowingToDoubleFunction, ToDoubleBiFunction)
     */
    static <T> ToDoubleFunction<T> failover(ThrowingToDoubleFunction<T> f,
                                            ToDoubleFunction<Throwable> failover) {
        return failover(f, (t, e) -> failover.applyAsDouble(e));
    }

    /**
     * Wraps a {@link ThrowingToDoubleFunction} to {@link ToDoubleFunction} which will handle the error thrown by the
     * given {@link ThrowingToDoubleFunction} with the given {@code failover} operation of {@link ToDoubleBiFunction}
     *
     * @param f        target to wrap
     * @param failover failover
     * @param <T>      the type of the result of the function
     *
     * @return transferred
     */
    static <T> ToDoubleFunction<T> failover(ThrowingToDoubleFunction<T> f,
                                            ToDoubleBiFunction<T, Throwable> failover) {
        Checks.checkNotNull(f);
        Checks.checkNotNull(failover);
        return (t) -> {
            try {
                return f.applyAsDouble(t);
            } catch (Throwable e) {
                return failover.applyAsDouble(t, e);
            }
        };
    }

    /**
     * @see ToDoubleFunction#applyAsDouble(Object)
     */
    double applyAsDouble(T value) throws Throwable;

}
