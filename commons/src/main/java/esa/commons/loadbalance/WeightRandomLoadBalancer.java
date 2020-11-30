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

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public abstract class WeightRandomLoadBalancer<T> implements LoadBalancer<T> {

    @Override
    public T select(List<T> elements) {
        if (elements == null || elements.isEmpty()) {
            return null;
        }
        int size = elements.size();
        if (size == 1) {
            return elements.get(0);
        }

        int totalWeight = 0;
        for (T e : elements) {
            totalWeight += getWeight(e);
        }

        int pos = ThreadLocalRandom.current().nextInt(totalWeight);
        for (int p = 0, i = 0; i < size; i++) {
            T e = elements.get(i);
            if (pos >= p && pos < (p = getWeight(e) + p)) {
                return e;
            }
        }
        return null;
    }

    protected abstract int getWeight(T e);
}
