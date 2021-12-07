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

import esa.commons.Checks;

import java.util.Objects;

/**
 * Default implementation of {@link AttributeKey} which just wrap the given string key as the identifier.
 */
final class DefaultAttributeKey<V> implements AttributeKey<V> {

    private final String key;

    @SuppressWarnings("unchecked")
    static <V> DefaultAttributeKey<V> create(String key) {
        Checks.checkNotEmptyArg(key, "key");
        return new DefaultAttributeKey<>(key);
    }

    DefaultAttributeKey(String key) {
        this.key = key;
    }

    @Override
    public String key() {
        return key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DefaultAttributeKey<?> that = (DefaultAttributeKey<?>) o;
        return this.key.equals(that.key);
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }

    @Override
    public String toString() {
        return key();
    }
}
