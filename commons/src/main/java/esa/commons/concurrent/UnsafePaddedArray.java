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
package esa.commons.concurrent;

import esa.commons.Checks;
import esa.commons.MathUtils;
import sun.misc.Unsafe;

import java.util.AbstractQueue;

import static esa.commons.concurrent.UnsafeArrayUtils.ARRAY_INDEX_SCALE;
import static esa.commons.concurrent.UnsafeArrayUtils.REF_ARRAY_BASE;
import static esa.commons.concurrent.UnsafeArrayUtils.REF_ARRAY_ELEMENT_SHIFT;

abstract class UnsafePaddedArray<E> extends LhsArrayPad<E> {
    private static final int ELEMENTS_PAD = 128 / ARRAY_INDEX_SCALE;
    private static final long ARRAY_BASE_OFFSET =
            REF_ARRAY_BASE + (ELEMENTS_PAD << REF_ARRAY_ELEMENT_SHIFT);
    final E[] elements;
    private final int capacity;
    private final long mask;

    UnsafePaddedArray(int capacity) {
        Checks.checkArg(capacity > 0, "Expect");
        int c = MathUtils.nextPowerOfTwo(capacity);
        this.capacity = capacity;
        this.mask = c - 1;
        //noinspection unchecked
        this.elements = (E[]) new Object[c + ELEMENTS_PAD * 2];
    }

    static long calcElementOffset(long index, long mask) {
        return ARRAY_BASE_OFFSET + ((index & mask) << REF_ARRAY_ELEMENT_SHIFT);
    }

    long mask() {
        return mask;
    }

    protected int capacity() {
        return capacity;
    }
}

abstract class LhsArrayPad<E> extends AbstractQueue<E> {
    static final Unsafe U = UnsafeUtils.getUnsafe();
    long p1, p2, p3, p4, p5, p6, p7;
}
