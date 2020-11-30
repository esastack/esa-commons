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

/**
 * Represents a predicate (boolean-valued function) of four arguments.
 *
 * @param <T1> the first function argument
 * @param <T2> the second function argument
 * @param <T3> the third function argument
 * @param <T4> the fourth function argument
 */
@FunctionalInterface
public interface Predicate4<T1, T2, T3, T4> {

    /**
     * Evaluates this predicate on the given arguments.
     *
     * @param t1 the first function argument
     * @param t2 the second function argument
     * @param t3 the third function argument
     * @param t4 the fourth function argument
     *
     * @return {@code true} if the input arguments match the predicate, otherwise {@code false}
     */
    boolean test(T1 t1, T2 t2, T3 t3, T4 t4);

    /**
     * Returns a composed predicate that represents a short-circuiting logical AND of this predicate and another.  When
     * evaluating the composed predicate, if this predicate is {@code false}, then the {@code other} predicate is not
     * evaluated.
     *
     * <p>Any exceptions thrown during evaluation of either predicate are relayed
     * to the caller; if evaluation of this predicate throws an exception, the {@code other} predicate will not be
     * evaluated.
     *
     * @param other a predicate that will be logically-ANDed with this predicate
     *
     * @return a composed predicate that represents the short-circuiting logical AND of this predicate and the {@code
     * other} predicate
     * @throws NullPointerException if other is null
     */
    default Predicate4<T1, T2, T3, T4> and(Predicate4<? super T1, ? super T2, ? super T3, ? super T4> other) {
        Checks.checkNotNull(other);
        return (T1 t1, T2 t2, T3 t3, T4 t4) -> test(t1, t2, t3, t4) && other.test(t1, t2, t3, t4);
    }

    /**
     * Returns a predicate that represents the logical negation of this predicate.
     *
     * @return a predicate that represents the logical negation of this predicate
     */
    default Predicate4<T1, T2, T3, T4> negate() {
        return (T1 t1, T2 t2, T3 t3, T4 t4) -> !test(t1, t2, t3, t4);
    }

    /**
     * Returns a composed predicate that represents a short-circuiting logical OR of this predicate and another.  When
     * evaluating the composed predicate, if this predicate is {@code true}, then the {@code other} predicate is not
     * evaluated.
     *
     * <p>Any exceptions thrown during evaluation of either predicate are relayed
     * to the caller; if evaluation of this predicate throws an exception, the {@code other} predicate will not be
     * evaluated.
     *
     * @param other a predicate that will be logically-ORed with this predicate
     *
     * @return a composed predicate that represents the short-circuiting logical OR of this predicate and the {@code
     * other} predicate
     * @throws NullPointerException if other is null
     */
    default Predicate4<T1, T2, T3, T4> or(Predicate4<? super T1, ? super T2, ? super T3, ? super T4> other) {
        Checks.checkNotNull(other);
        return (T1 t1, T2 t2, T3 t3, T4 t4) -> test(t1, t2, t3, t4) || other.test(t1, t2, t3, t4);
    }

}
