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

import java.util.HashMap;
import java.util.Map;

/**
 * Utils of primitive types and their wrapper types.
 */
public final class Primitives {

    private static final Map<Class<?>, Class<?>> WRAPPERS
            = new HashMap<>(16);
    private static final Map<Class<?>, Object> DEFAULT_VALUES
            = new HashMap<>(32);

    static {
        WRAPPERS.put(Boolean.class, boolean.class);
        WRAPPERS.put(Byte.class, byte.class);
        WRAPPERS.put(Character.class, char.class);
        WRAPPERS.put(Short.class, short.class);
        WRAPPERS.put(Integer.class, int.class);
        WRAPPERS.put(Long.class, long.class);
        WRAPPERS.put(Float.class, float.class);
        WRAPPERS.put(Double.class, double.class);
        WRAPPERS.put(Void.class, void.class);

        DEFAULT_VALUES.put(Boolean.class, false);
        DEFAULT_VALUES.put(boolean.class, false);
        DEFAULT_VALUES.put(Byte.class, (byte) 0);
        DEFAULT_VALUES.put(byte.class, (byte) 0);
        DEFAULT_VALUES.put(Character.class, '\u0000');
        DEFAULT_VALUES.put(char.class, '\u0000');
        DEFAULT_VALUES.put(Short.class, (short) 0);
        DEFAULT_VALUES.put(short.class, (short) 0);
        DEFAULT_VALUES.put(Integer.class, 0);
        DEFAULT_VALUES.put(int.class, 0);
        DEFAULT_VALUES.put(Long.class, 0L);
        DEFAULT_VALUES.put(long.class, 0L);
        DEFAULT_VALUES.put(Float.class, 0.0F);
        DEFAULT_VALUES.put(float.class, 0.0F);
        DEFAULT_VALUES.put(Double.class, 0.0D);
        DEFAULT_VALUES.put(double.class, 0.0D);
        DEFAULT_VALUES.put(Void.class, null);
        DEFAULT_VALUES.put(void.class, null);
    }

    /**
     * Whether the give given {@code type} is a wrapper type or not.
     *
     * @param type type
     *
     * @return {@code true} if the given {@code type} is a wrapper type, otherwise {@code false}
     */
    public static boolean isWrapperType(Class<?> type) {
        return type != null && WRAPPERS.containsKey(type);
    }

    /**
     * Whether the give given {@code type} is a primitive type or not.
     *
     * @param type type
     *
     * @return {@code true} if the given {@code type} is a primitive type, otherwise {@code false}
     */
    public static boolean isPrimitiveType(Class<?> type) {
        return type != null && type.isPrimitive();
    }

    /**
     * Whether the give given {@code type} is a primitive or wrapper type.
     *
     * @param type type
     *
     * @return {@code true} if the given {@code type} is a primitive or wrapper type, otherwise {@code false}
     */
    public static boolean isPrimitiveOrWraperType(Class<?> type) {
        return DEFAULT_VALUES.containsKey(type);
    }

    /**
     * Gets the default value of given primitive type or wrapper type.
     *
     * @param type type
     * @param <T>  type
     *
     * @return default value of given primitive type or wrapper type, or else {@code null} if given  {@code type} is not
     * a primitive type or wrapper type
     */
    @SuppressWarnings("unchecked")
    public static <T> T defaultValue(Class<T> type) {
        return (T) DEFAULT_VALUES.get(type);
    }

    private Primitives() {
    }

}
