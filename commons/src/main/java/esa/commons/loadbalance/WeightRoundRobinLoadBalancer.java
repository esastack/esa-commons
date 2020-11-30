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
package esa.commons.loadbalance;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

public abstract class WeightRoundRobinLoadBalancer<T> implements LoadBalancer<T> {

    private volatile int cursor = 0;

    private static final AtomicIntegerFieldUpdater CURSOR_UPDATER =
            AtomicIntegerFieldUpdater.newUpdater(WeightRoundRobinLoadBalancer.class, "cursor");

    @SuppressWarnings("unchecked")
    @Override
    public T select(List<T> elements) {
        if (elements == null || elements.isEmpty()) {
            return null;
        }
        final int size = elements.size();
        if (size == 1) {
            return elements.get(0);
        }

        int totalWeight = 0;
        for (T e : elements) {
            totalWeight += getWeight(e);
        }

        List<T> elementsList = new ArrayList<>(totalWeight);
        for (T e : elements) {
            int weight = getWeight(e);
            for (int i = 0; i < weight; i++) {
                elementsList.add(e);
            }
        }

        return elementsList.get((CURSOR_UPDATER.getAndIncrement(this) & 0x7fffffff) % totalWeight);
    }

    protected abstract int getWeight(T e);

}
