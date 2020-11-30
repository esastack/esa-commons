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
 * Represents an operation that accepts four input arguments and returns no result.
 *
 * @param <T1> the first function argument
 * @param <T2> the second function argument
 * @param <T3> the third function argument
 * @param <T4> the fourth function argument
 * @param <T5> the fifth function argument
 */
@FunctionalInterface
public interface Consumer5<T1, T2, T3, T4, T5> {

    /**
     * Performs this operation on the given arguments.
     *
     * @param t1 the first function argument
     * @param t2 the second function argument
     * @param t3 the third function argument
     * @param t4 the fourth function argument
     * @param t5 the fifth function argument
     */
    void accept(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5);

    /**
     * Returns a composed {@code Consumer5} that performs, in sequence, this operation followed by the {@code after}
     * operation. If performing either operation throws an exception, it is relayed to the caller of the composed
     * operation.  If performing this operation throws an exception, the {@code after} operation will not be performed.
     *
     * @param after the operation to perform after this operation
     *
     * @return a composed {@code Consumer5} that performs in sequence this operation followed by the {@code after}
     * operation
     * @throws NullPointerException if {@code after} is null
     */
    default Consumer5<T1, T2, T3, T4, T5> andThen(Consumer5<? super T1, ? super T2, ? super T3, ? super T4, ?
            super T5> after) {
        Checks.checkNotNull(after);

        return (t1, t2, t3, t4, t5) -> {
            accept(t1, t2, t3, t4, t5);
            after.accept(t1, t2, t3, t4, t5);
        };
    }
}
