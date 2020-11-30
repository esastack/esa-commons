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

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of {@link MultiValueMap} which uses a {@link java.util.LinkedHashMap} as the underlying map and use
 * the {@link ArrayList} as the value list.
 */
public class LinkedMultiArrayValueMap<K, V> extends LinkedMultiValueMap<K, V> {

    private final int initialValueSize;
    private static final int DEFAULT_INITIAL_VALUE_SIZE = 8;

    public LinkedMultiArrayValueMap() {
        this(DEFAULT_INITIAL_VALUE_SIZE);
    }

    public LinkedMultiArrayValueMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_INITIAL_VALUE_SIZE);
    }

    public LinkedMultiArrayValueMap(int initialCapacity, int initialValueSize) {
        super(initialCapacity);
        checkInitialValueSize(initialValueSize);
        this.initialValueSize = initialValueSize;
    }

    public LinkedMultiArrayValueMap(int initialCapacity, float loadFactor) {
        this(initialCapacity, loadFactor, DEFAULT_INITIAL_VALUE_SIZE);
    }

    public LinkedMultiArrayValueMap(int initialCapacity, float loadFactor, int initialValueSize) {
        super(initialCapacity, loadFactor);
        checkInitialValueSize(initialValueSize);
        this.initialValueSize = initialValueSize;
    }

    public <T extends K, U extends V> LinkedMultiArrayValueMap(MultiValueMap<T, U> map) {
        super(map);
        this.initialValueSize = DEFAULT_INITIAL_VALUE_SIZE;
    }


    private void checkInitialValueSize(int initialValueSize) {
        if (initialValueSize < 0) {
            throw new IllegalArgumentException("Illegal initialValueSize: " +
                    initialValueSize);
        }
    }

    @Override
    protected List<V> newValueList() {
        return new ArrayList<>(initialValueSize);
    }
}
