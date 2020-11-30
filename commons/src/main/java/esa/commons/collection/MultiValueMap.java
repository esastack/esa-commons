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
package esa.commons.collection;

import java.util.List;
import java.util.Map;

/**
 * An object that maps keys to list values.
 *
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped list values
 */
public interface MultiValueMap<K, V> extends Map<K, List<V>> {

    /**
     * Add the given single value to the current list of values for the given key.
     *
     * @param key   key
     * @param value value
     */
    void add(K key, V value);

    /**
     * Add all the values of the given list to the current list of values for the given key.
     *
     * @param key    key
     * @param values values
     */
    void addAll(K key, Iterable<? extends V> values);


    /**
     * Add a single value to the first position in the current list of values for the given key.
     *
     * @param key   key
     * @param value value
     */
    void addFirst(K key, V value);

    /**
     * Get the first value of the given key.
     *
     * @param key key
     *
     * @return value
     */
    V getFirst(K key);

    /**
     * Set the key's value to be a one item list consisting of the given value. Any existing values will be replaced.
     *
     * @param key   key
     * @param value value
     */
    void putSingle(K key, V value);

    /**
     * Return a {@code Map} with the first values contained in this {@code MultiValueMap}.
     *
     * @return a single value representation of this map
     */
    Map<K, V> toSingleValueMap();

}
