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
package esa.commons.concurrencytest;

import org.openjdk.jcstress.annotations.Actor;
import org.openjdk.jcstress.annotations.Arbiter;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.annotations.State;
import org.openjdk.jcstress.infra.results.ZZI_Result;

import static org.openjdk.jcstress.annotations.Expect.ACCEPTABLE;
import static org.openjdk.jcstress.annotations.Expect.FORBIDDEN;

/**
 * SpscArrayQueuePollTest
 */
@JCStressTest
@Outcome(id = "true, true, 2", expect = ACCEPTABLE, desc = "Pretty good")
@Outcome(expect = FORBIDDEN)
@State
public class MpscArrayQueueOfferTest {

    private final MpscArrayQueue<Integer> q = new MpscArrayQueue<>(2);

    @Actor
    public void offer1(ZZI_Result r) {
        r.r1 = q.offer(1);
    }

    @Actor
    public void offer2(ZZI_Result r) {
        r.r2 = q.offer(2);
    }

    @Arbiter
    public void arbiter(ZZI_Result r) {
        r.r3 = q.size();
    }

}
