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
package esa.commons;

public final class MathUtils {

    private static final int MAX_POWER2 = 1 << 30;

    /**
     * Whether the given value is power of 2.
     *
     * @param value value
     *
     * @return {@code true} if it is power of 2, or else {@code false}
     */
    public static boolean isPowerOfTwo(int value) {
        return (value & value - 1) == 0;
    }

    /**
     * Finds the next power of 2 greater than or equal to the supplied value.
     *
     * @param value value
     *
     * @return the next positive power of 2. 1 if is a negative value or
     */
    public static int nextPowerOfTwo(final int value) {
        if (value > MAX_POWER2) {
            return MAX_POWER2;
        }
        if (value < 0) {
            return 1;
        }
        return 1 << (32 - Integer.numberOfLeadingZeros(value - 1));
    }

    public static double round(double value) {
        return Math.round(value * 100D) / 100D;
    }

    /**
     * @deprecated use {@link #round(double)}
     */
    @Deprecated
    public static Double round(Double value) {
        if (value == null) {
            return null;
        }
        return Math.round(value * 100D) / 100D;
    }

    private MathUtils() {
    }
}
