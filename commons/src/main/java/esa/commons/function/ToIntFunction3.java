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

/**
 * Represents a function that accepts two arguments and produces an int-valued result.
 *
 * @param <T1> the first function argument
 * @param <T2> the second function argument
 * @param <T3> the third function argument
 */
@FunctionalInterface
public interface ToIntFunction3<T1, T2, T3> {

    /**
     * Applies this function to the given arguments.
     *
     * @param t1 the first function argument
     * @param t2 the second function argument
     * @param t3 the third function argument
     *
     * @return the function result
     */
    int applyAsInt(T1 t1, T2 t2, T3 t3);

}
