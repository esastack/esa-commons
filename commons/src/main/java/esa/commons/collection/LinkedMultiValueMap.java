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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of {@link MultiValueMap} which uses a {@link LinkedHashMap} as the underlying map.
 */
public class LinkedMultiValueMap<K, V> extends AbstractMultiValueMap<K, V> {

    public LinkedMultiValueMap() {
        super(new LinkedHashMap<>());
    }

    public LinkedMultiValueMap(int initialCapacity) {
        super(new LinkedHashMap<>(initialCapacity));
    }

    public LinkedMultiValueMap(int initialCapacity, float loadFactor) {
        super(new LinkedHashMap<>(initialCapacity, loadFactor));
    }

    public <T extends K, U extends V> LinkedMultiValueMap(MultiValueMap<T, U> map) {
        this();
        for (Map.Entry<T, List<U>> e : map.entrySet()) {
            List<V> values = newValueList();
            values.addAll(e.getValue());
            underlying.put(e.getKey(), values);
        }
    }

}
