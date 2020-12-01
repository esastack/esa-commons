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

import sun.misc.Unsafe;

/**
 * Unity class for array operation by {@link Unsafe}.
 */
public final class UnsafeArrayUtils {

    public static final int ARRAY_INDEX_SCALE;
    public static final long REF_ARRAY_BASE;
    public static final int REF_ARRAY_ELEMENT_SHIFT;
    private static final Unsafe UNSAFE;

    /**
     * Fetches a value from given array.
     *
     * @param arr    array in which the variable resides
     * @param offset indication of where the variable resides in the given array
     * @param <E>    element type of array
     *
     * @return the value fetched from the given array
     */
    @SuppressWarnings("unchecked")
    public static <E> E getElement(E[] arr, long offset) {
        return (E) UNSAFE.getObject(arr, offset);
    }

    /**
     * Acquire version of {@link #getElement(Object[], long)}
     */
    @SuppressWarnings("unchecked")
    public static <E> E getElementAcquire(E[] arr, long offset) {
        return (E) UNSAFE.getObjectVolatile(arr, offset);
    }

    /**
     * Stores a value into a given array.
     *
     * @param arr    array in which the variable resides
     * @param offset indication of where the variable resides in the given array
     * @param e      the value to store into the given array
     * @param <E>    element type of array
     */
    public static <E> void setElement(E[] arr, long offset, E e) {
        UNSAFE.putObject(arr, offset, e);
    }

    /**
     * Release version of {@link #setElement(Object[], long, Object)}
     */
    public static <E> void setElementRelease(E[] arr, long offset, E e) {
        UNSAFE.putObjectVolatile(arr, offset, e);
    }

    /**
     * Eventually stores a value into a given array.
     *
     * @see #setElement(Object[], long, Object)
     */
    public static <E> void lazySetElement(E[] arr, long offset, E e) {
        UNSAFE.putOrderedObject(arr, offset, e);
    }

    /**
     * Calculates the offset that indicates where the variable resides in.
     *
     * @param index index
     *
     * @return offset that indicates where the variable resides in.
     */
    public static long calcElementOffset(long index) {
        return REF_ARRAY_BASE + (index << REF_ARRAY_ELEMENT_SHIFT);
    }

    /**
     * Calculates the masked offset that indicates where the variable resides in.
     *
     * @param index index
     *
     * @return offset that indicates where the variable resides in.
     */
    public static long calcElementOffset(long index, long mask) {
        return calcElementOffset(index & mask);
    }

    private UnsafeArrayUtils() {
    }

    static {
        if (UnsafeUtils.hasUnsafe()) {
            UNSAFE = UnsafeUtils.getUnsafe();
            final int scale = UNSAFE.arrayIndexScale(Object[].class);
            if (4 == scale) {
                REF_ARRAY_ELEMENT_SHIFT = 2;
            } else if (8 == scale) {
                REF_ARRAY_ELEMENT_SHIFT = 3;
            } else {
                throw new IllegalStateException("Unknown pointer size");
            }
            ARRAY_INDEX_SCALE = scale;
            REF_ARRAY_BASE = UNSAFE.arrayBaseOffset(Object[].class);
        } else {
            UNSAFE = null;
            ARRAY_INDEX_SCALE = -1;
            REF_ARRAY_BASE = -1L;
            REF_ARRAY_ELEMENT_SHIFT = -1;
        }
    }
}
