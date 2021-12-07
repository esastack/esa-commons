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

import java.util.function.BiConsumer;

/**
 * An container for attributes.
 * <p>
 * Note!: Whether this attribute is thread-safe depends on its implementation.
 */
public interface Attributes {

    /**
     * Returns the attribute value for the given {@code key}.
     * It will create a new {@link Attribute} if attribute of the given {@code key} is absent.
     *
     * @param key attr key
     * @param <V> attr
     * @return none-null attribute
     */
    <V> Attribute<V> attr(AttributeKey<V> key);

    /**
     * Returns {@code true} if this attributes contains a mapping for the specified key.
     *
     * @param key The key whose presence in this attributes is to be tested
     * @return {@code true} if this attributes contains a mapping for the specified
     * key.
     */
    boolean hasAttr(AttributeKey<?> key);

    /**
     * Iterates over all the key-value pairs of attributes contained by this instance.
     */
    void forEach(BiConsumer<? super AttributeKey<?>, ? super Attribute<?>> consumer);

    /**
     * The number of attributes contained in this.
     */
    int size();

    /**
     * Whether there are any attributes contained in this.
     */
    boolean isEmpty();

}
