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

import esa.commons.Checks;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Utils of MultiMaps
 */
public final class MultiMaps {

    private static final EmptyMultiValueMap EMPTY = new EmptyMultiValueMap();

    @SuppressWarnings("unchecked")
    public static <K, V> MultiValueMap<K, V> emptyMultiMap() {
        return (MultiValueMap<K, V>) EMPTY;
    }

    private static class EmptyMultiValueMap<K, V>
            extends AbstractMap<K, List<V>> implements MultiValueMap<K, V>, Serializable {

        private static final long serialVersionUID = -1508943510398766323L;

        @Override
        public void add(K key, V value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void addAll(K key, Iterable<? extends V> values) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void addFirst(K key, V value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public V getFirst(K key) {
            return null;
        }

        @Override
        public void putSingle(K key, V value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Map<K, V> toSingleValueMap() {
            return Collections.emptyMap();
        }

        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public boolean containsKey(Object key) {
            return false;
        }

        @Override
        public boolean containsValue(Object value) {
            return false;
        }

        @Override
        public List<V> get(Object key) {
            return null;
        }

        @Override
        public List<V> getOrDefault(Object key, List<V> defaultValue) {
            return defaultValue;
        }


        @Override
        public void forEach(BiConsumer<? super K, ? super List<V>> action) {
            Checks.checkNotNull(action);
        }

        @Override
        public List<V> putIfAbsent(K key, List<V> value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean replace(K key, List<V> oldValue, List<V> newValue) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<V> replace(K key, List<V> value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void replaceAll(BiFunction<? super K, ? super List<V>, ? extends List<V>> function) {
            Checks.checkNotNull(function);
        }

        @Override
        public List<V> remove(Object key) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean remove(Object key, Object value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<V> computeIfAbsent(K key, Function<? super K, ? extends List<V>> mappingFunction) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<V> computeIfPresent(K key,
                                        BiFunction<? super K, ? super List<V>, ? extends List<V>> remappingFunction) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<V> compute(K key, BiFunction<? super K, ? super List<V>, ? extends List<V>> remappingFunction) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<V> merge(K key, List<V> value,
                             BiFunction<? super List<V>, ? super List<V>, ? extends List<V>> remappingFunction) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Set<K> keySet() {
            return Collections.emptySet();
        }

        @Override
        public Collection<List<V>> values() {
            return Collections.emptySet();
        }

        @Override
        public Set<Entry<K, List<V>>> entrySet() {
            return Collections.emptySet();
        }

        @Override
        public boolean equals(Object o) {
            return (o instanceof MultiValueMap) && ((MultiValueMap<?, ?>) o).isEmpty();
        }

        @Override
        public int hashCode() {
            return 0;
        }
    }

    private MultiMaps() {
    }

}
