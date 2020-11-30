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

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class RoundRobinLoadBalancerTest {

    @Test
    void testAll() {
        final RoundRobinLoadBalancer<Integer> lb = new RoundRobinLoadBalancer<>();
        assertNull(lb.select(Collections.emptyList()));
        assertEquals(1, lb.select(Collections.singletonList(1)));

        assertEquals(0, lb.select(Arrays.asList(0, 1, 2)));
        assertEquals(1, lb.select(Arrays.asList(0, 1, 2)));
        assertEquals(2, lb.select(Arrays.asList(0, 1, 2)));
        assertEquals(0, lb.select(Arrays.asList(0, 1, 2)));
        assertEquals(1, lb.select(Arrays.asList(0, 1, 2)));
        assertEquals(2, lb.select(Arrays.asList(0, 1, 2)));
    }

}
