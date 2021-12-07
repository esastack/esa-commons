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

import java.util.HashMap;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

/**
 * Implementation of {@link Attributes} which extends from the {@link HashMap}.
 * This is not thread-safe.
 */
public class AttributeMap extends HashMap<AttributeKey<?>, Attribute<?>> implements Attributes {

    public AttributeMap() {
        super();
    }

    public AttributeMap(int initCapacity) {
        super(initCapacity);
    }

    public AttributeMap(int initCapacity, float loadFactor) {
        super(initCapacity, loadFactor);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <V> Attribute<V> attr(AttributeKey<V> key) {
        return (Attribute<V>) super.computeIfAbsent(key, k -> new AttributeImpl<>(k, this));
    }

    @Override
    public boolean hasAttr(AttributeKey<?> key) {
        return super.containsKey(key);
    }

    private static class AttributeImpl<V> implements Attribute<V> {

        private final AttributeKey<V> key;
        private AttributeMap attrs;
        private V value;

        private AttributeImpl(AttributeKey<V> key, AttributeMap attrs) {
            this.key = key;
            this.attrs = attrs;
        }

        @Override
        public AttributeKey<V> key() {
            return key;
        }

        @Override
        public V get() {
            return value;
        }

        @Override
        public V getOrDefault(V def) {
            return value == null ? def : value;
        }

        @Override
        public void set(V newValue) {
            this.value = newValue;
        }

        @Override
        public void lazySet(V newValue) {
            this.value = newValue;
        }

        @Override
        public boolean compareAndSet(V expect, V update) {
            if (this.value == expect) {
                this.value = update;
                return true;
            } else {
                return false;
            }
        }

        @Override
        public V getAndSet(V newValue) {
            V old = this.value;
            this.value = newValue;
            return old;
        }

        @Override
        public V getAndUpdate(UnaryOperator<V> updateFunction) {
            V prev = this.value;
            this.value = updateFunction.apply(prev);
            return prev;
        }

        @Override
        public V updateAndGet(UnaryOperator<V> updateFunction) {
            return this.value = updateFunction.apply(this.value);
        }

        @Override
        public V getAndAccumulate(V x, BinaryOperator<V> accumulatorFunction) {
            V prev = this.value;
            this.value = accumulatorFunction.apply(prev, x);
            return prev;
        }

        @Override
        public V accumulateAndGet(V x, BinaryOperator<V> accumulatorFunction) {
            return this.value = accumulatorFunction.apply(this.value, x);
        }

        @Override
        public void remove() {
            AttributeMap currentMap = this.attrs;
            this.attrs = null;
            set(null);
            if (currentMap != null) {
                currentMap.remove(key);
            }
        }

        @Override
        public V getAndRemove() {
            AttributeMap currentMap = this.attrs;
            this.attrs = null;
            V v = getAndSet(null);
            if (currentMap != null) {
                currentMap.remove(key);
            }
            return v;
        }
    }

}
