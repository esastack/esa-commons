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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of {@link MultiValueMap} which uses a {@link HashMap} as the underlying map.
 */
public class HashMultiValueMap<K, V> extends AbstractMultiValueMap<K, V> {

    public HashMultiValueMap() {
        super(new HashMap<>());
    }

    public HashMultiValueMap(int initialCapacity) {
        super(new HashMap<>(initialCapacity));
    }

    public HashMultiValueMap(int initialCapacity, float loadFactor) {
        super(new HashMap<>(initialCapacity, loadFactor));
    }

    public <T extends K, U extends V> HashMultiValueMap(MultiValueMap<T, U> map) {
        this();
        for (Map.Entry<T, List<U>> e : map.entrySet()) {
            List<V> values = newValueList();
            values.addAll(e.getValue());
            underlying.put(e.getKey(), values);
        }
    }

    @Override
    public Map<K, V> toSingleValueMap() {
        HashMap<K, V> singleValueMap = new HashMap<>(this.underlying.size());
        this.underlying.forEach((key, value) -> singleValueMap.put(key, value.get(0)));
        return singleValueMap;
    }
}
