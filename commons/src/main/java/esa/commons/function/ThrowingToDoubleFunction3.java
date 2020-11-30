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

import java.util.function.ToDoubleFunction;

/**
 * Represents a function that produces an double-valued result, which allows user to throw a checked exception.
 *
 * @param <T1> the first function argument
 * @param <T2> the second function argument
 * @param <T3> the third function argument
 * @see ToDoubleFunction3
 */
@FunctionalInterface
public interface ThrowingToDoubleFunction3<T1, T2, T3> {

    /**
     * Wraps a {@link ThrowingToDoubleFunction3} to {@link ToDoubleFunction3} which will rethrow the error thrown by the
     * given {@link ThrowingConsumer4}
     *
     * @param f    target to wrap
     * @param <T1> the first function argument
     * @param <T2> the second function argument
     * @param <T3> the third function argument
     *
     * @return transferred
     */
    static <T1, T2, T3> ToDoubleFunction3<T1, T2, T3> rethrow(ThrowingToDoubleFunction3<T1, T2, T3> f) {
        Checks.checkNotNull(f);
        return (t1, t2, t3) -> {
            try {
                return f.applyAsDouble(t1, t2, t3);
            } catch (Throwable e) {
                ExceptionUtils.throwException(e);
                // never reach this statement
                return -1D;
            }
        };
    }

    /**
     * Wraps a {@link ThrowingToDoubleFunction3} to {@link ToDoubleFunction3} which will use the given value of {@code
     * onFailure} as the result of the function when an error is thrown by the given {@link ThrowingToDoubleFunction3}
     *
     * @param f         target to wrap
     * @param onFailure onFailure
     * @param <T1>      the first function argument
     * @param <T2>      the second function argument
     * @param <T3>      the third function argument
     *
     * @return transferred
     */
    static <T1, T2, T3> ToDoubleFunction3<T1, T2, T3> onFailure(ThrowingToDoubleFunction3<T1, T2, T3> f,
                                                                double onFailure) {
        Checks.checkNotNull(f);
        return (t1, t2, t3) -> {
            try {
                return f.applyAsDouble(t1, t2, t3);
            } catch (Throwable e) {
                return onFailure;
            }
        };
    }

    /**
     * @see #failover(ThrowingToDoubleFunction3, ToDoubleFunction4)
     */
    static <T1, T2, T3> ToDoubleFunction3<T1, T2, T3> failover(ThrowingToDoubleFunction3<T1, T2, T3> f,
                                                               ToDoubleFunction<Throwable> failover) {
        return failover(f, (t1, t2, t3, e) -> failover.applyAsDouble(e));
    }

    /**
     * Wraps a {@link ThrowingToDoubleFunction3} to {@link ToDoubleFunction3} which will handle the error thrown by the
     * given {@link ThrowingToDoubleFunction3} with the given {@code failover} operation of {@link ToDoubleFunction4}
     *
     * @param f        target to wrap
     * @param failover failover
     * @param <T1>     the first function argument
     * @param <T2>     the second function argument
     * @param <T3>     the third function argument
     *
     * @return transferred
     */
    static <T1, T2, T3> ToDoubleFunction3<T1, T2, T3> failover(ThrowingToDoubleFunction3<T1, T2, T3> f,
                                                               ToDoubleFunction4<T1, T2, T3, Throwable> failover) {
        Checks.checkNotNull(f);
        Checks.checkNotNull(failover);
        return (t1, t2, t3) -> {
            try {
                return f.applyAsDouble(t1, t2, t3);
            } catch (Throwable e) {
                return failover.applyAsDouble(t1, t2, t3, e);
            }
        };
    }

    /**
     * @see ToDoubleFunction3#applyAsDouble(Object, Object, Object)
     */
    double applyAsDouble(T1 t1, T2 t2, T3 t3) throws Throwable;
}
