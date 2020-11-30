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
 * Represents a function that accepts two arguments and produces an double-valued result., which allows user to throw a
 * checked exception.
 *
 * @param <T> the type of the first argument to the function
 * @param <U> the type of the second argument to the function
 * @see ToDoubleBiFunction
 */
@FunctionalInterface
public interface ThrowingToDoubleBiFunction<T, U> {

    /**
     * Wraps a {@link ThrowingToDoubleBiFunction} to {@link ToDoubleBiFunction} which will rethrow the error thrown by
     * the given {@link ThrowingToDoubleBiFunction}
     *
     * @param f   target to wrap
     * @param <T> the type of the first argument to the function
     * @param <U> the type of the second argument to the function
     *
     * @return transferred
     */
    static <T, U> ToDoubleBiFunction<T, U> rethrow(ThrowingToDoubleBiFunction<T, U> f) {
        Checks.checkNotNull(f);
        return (t, u) -> {
            try {
                return f.applyAsDouble(t, u);
            } catch (Throwable e) {
                ExceptionUtils.throwException(e);
                // never reach this statement
                return -1D;
            }
        };
    }

    /**
     * Wraps a {@link ThrowingToDoubleBiFunction} to {@link ToDoubleBiFunction} which will use the given value of {@code
     * onFailure} as the result of the function when an error is thrown by the given {@link ThrowingToDoubleBiFunction}
     *
     * @param f         target to wrap
     * @param onFailure onFailure
     * @param <T>       the type of the first argument to the function
     * @param <U>       the type of the second argument to the function
     *
     * @return transferred
     */
    static <T, U> ToDoubleBiFunction<T, U> onFailure(ThrowingToDoubleBiFunction<T, U> f,
                                                     double onFailure) {
        Checks.checkNotNull(f);
        return (t, u) -> {
            try {
                return f.applyAsDouble(t, u);
            } catch (Throwable e) {
                return onFailure;
            }
        };
    }

    /**
     * @see #failover(ThrowingToDoubleBiFunction, ToDoubleFunction3)
     */
    static <T, U> ToDoubleBiFunction<T, U> failover(ThrowingToDoubleBiFunction<T, U> f,
                                                    ToDoubleFunction<Throwable> failover) {
        return failover(f, (t, u, e) -> failover.applyAsDouble(e));
    }

    /**
     * Wraps a {@link ThrowingToDoubleBiFunction} to {@link ToDoubleBiFunction} which will handle the error thrown by
     * the given {@link ThrowingToDoubleBiFunction} with the given {@code failover} operation of {@link ToIntFunction3}
     *
     * @param f        target to wrap
     * @param failover failover
     * @param <T>      the type of the first argument to the function
     * @param <U>      the type of the second argument to the function
     *
     * @return transferred
     */
    static <T, U> ToDoubleBiFunction<T, U> failover(ThrowingToDoubleBiFunction<T, U> f,
                                                    ToDoubleFunction3<T, U, Throwable> failover) {
        Checks.checkNotNull(f);
        Checks.checkNotNull(failover);
        return (t, u) -> {
            try {
                return f.applyAsDouble(t, u);
            } catch (Throwable e) {
                return failover.applyAsDouble(t, u, e);
            }
        };
    }

    /**
     * @see ToDoubleBiFunction#applyAsDouble(Object, Object)
     */
    double applyAsDouble(T t, U u) throws Throwable;
}
