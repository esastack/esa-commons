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

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Abstract implementation of {@link MultiValueMap} which uses a underlying map as the store of elements.
 * @see MultiValueMap
 */
public abstract class AbstractMultiValueMap<K, V> implements MultiValueMap<K, V> {

    protected final Map<K, List<V>> underlying;

    protected AbstractMultiValueMap(Map<K, List<V>> underlying) {
        if (underlying == null) {
            throw new NullPointerException("Underlying map must not be null.");
        }
        this.underlying = underlying;
    }

    protected List<V> getList(K key) {
        return underlying.computeIfAbsent(key, k -> newValueList());
    }

    protected List<V> newValueList() {
        return new LinkedList<>();
    }

    @Override
    public void add(K key, V value) {
        getList(key).add(value);
    }

    @Override
    public void addAll(K key, Iterable<? extends V> values) {
        List<V> valuesList = getList(key);
        for (V value : values) {
            valuesList.add(value);
        }
    }

    @Override
    public void addFirst(K key, V value) {
        getList(key).add(0, value);
    }

    @Override
    public V getFirst(K key) {
        List<V> values = getList(key);
        if (values != null && values.size() > 0) {
            return values.get(0);
        } else {
            return null;
        }
    }

    @Override
    public void putSingle(K key, V value) {
        List<V> values = getList(key);
        values.clear();
        values.add(value);
    }

    @Override
    public Map<K, V> toSingleValueMap() {
        LinkedHashMap<K, V> singleValueMap = new LinkedHashMap<>(this.underlying.size());
        this.underlying.forEach((key, value) -> singleValueMap.put(key, value.get(0)));
        return singleValueMap;
    }

    @Override
    public Collection<List<V>> values() {
        return underlying.values();
    }

    @Override
    public int size() {
        return underlying.size();
    }

    @Override
    public List<V> remove(Object key) {
        return underlying.remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends List<V>> m) {
        underlying.putAll(m);
    }

    @Override
    public List<V> put(K key, List<V> value) {
        return underlying.put(key, value);
    }

    @Override
    public Set<K> keySet() {
        return underlying.keySet();
    }

    @Override
    public boolean isEmpty() {
        return underlying.isEmpty();
    }

    @Override
    public List<V> get(Object key) {
        return underlying.get(key);
    }

    @Override
    public Set<Map.Entry<K, List<V>>> entrySet() {
        return underlying.entrySet();
    }

    @Override
    public boolean containsValue(Object value) {
        return underlying.containsValue(value);
    }

    @Override
    public boolean containsKey(Object key) {
        return underlying.containsKey(key);
    }

    @Override
    public void clear() {
        underlying.clear();
    }

    @Override
    public boolean equals(Object obj) {
        return this.underlying.equals(obj);
    }

    @Override
    public String toString() {
        return underlying.toString();
    }

    @Override
    public int hashCode() {
        return underlying.hashCode();
    }
}
