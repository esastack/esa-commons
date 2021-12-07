/*
 * Copyright 2021 OPPO ESA Stack Project
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
package esa.commons.collection;

import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

/**
 * Interface provides a handle for setting the value reference.
 * <p>
 * Note!: Whether this attribute is thread-safe depends on its implementation.
 *
 * @param <V> The type of value that can be set with the key.
 */
public interface Attribute<V> {

    /**
     * Returns the key of this attribute.
     */
    AttributeKey<V> key();

    /**
     * Returns the value, which may be {@code null}.
     */
    V get();

    /**
     * Returns the value or given {@code def} if the value is {@code null}.
     */
    V getOrDefault(V def);

    /**
     * Sets the value to given {@code newValue}.
     */
    void set(V newValue);

    /**
     * Sets the value to given {@code newValue} eventually.
     */
    void lazySet(V newValue);

    /**
     * Sets the value to the given {@code update} if the current value {@code ==} the {@code expect}.
     */
    boolean compareAndSet(V expect, V update);

    /**
     * Sets to the given value and returns the old value.
     */
    V getAndSet(V newValue);

    /**
     * Updates the current value with the results of applying the given function, returning the previous value.
     */
    V getAndUpdate(UnaryOperator<V> updateFunction);

    /**
     * Updates the current value with the results of applying the given function, returning the updated value.
     */
    V updateAndGet(UnaryOperator<V> updateFunction);

    /**
     * Updates the current value with the results of applying the given function to the current and given values,
     * returning the previous value.
     */
    V getAndAccumulate(V x, BinaryOperator<V> accumulatorFunction);

    /**
     * Updates the current value with the results of applying the given function to the current and given values,
     * returning the updated value.
     */
    V accumulateAndGet(V x, BinaryOperator<V> accumulatorFunction);

    /**
     * Removes current {@link Attribute} from {@link Attributes}.
     */
    void remove();

    /**
     * Removes current {@link Attribute} from {@link Attributes}, returning the current value.
     */
    V getAndRemove();

}
