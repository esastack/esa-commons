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

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public final class ObjectUtils {

    /**
     * Gets the default value of given target class type.
     *
     * @param clz target class type
     *
     * @return default value
     */
    public static Object defaultValue(Class<?> clz) {
        if (clz == null) {
            return null;
        }
        if (clz.equals(byte.class)) {
            return (byte) 0;
        }

        if (clz.equals(short.class)) {
            return (short) 0;
        }

        if (clz.equals(int.class)) {
            return 0;
        }

        if (clz.equals(long.class)) {
            return 0L;
        }

        if (clz.equals(float.class)) {
            return 0.0f;
        }

        if (clz.equals(double.class)) {
            return 0.0D;
        }

        if (clz.equals(char.class)) {
            return '\u0000';
        }
        if (clz.equals(boolean.class)) {
            return Boolean.FALSE;
        }

        return null;
    }

    /**
     * Determine whether the given class type is a box type of primitive type.
     * @param clz target class type
     * @return true if and only if this class represents a box type of  primitive type as follows:
     * <ul>
     * <li>{@link java.lang.Boolean}
     * <li>{@link java.lang.Character}
     * <li>{@link java.lang.Byte}
     * <li>{@link java.lang.Short}
     * <li>{@link java.lang.Integer}
     * <li>{@link java.lang.Long}
     * <li>{@link java.lang.Float}
     * <li>{@link java.lang.Double}
     * <li>{@link java.lang.Void}
     * </ul>
     */
    public static boolean isPrimitiveWrapper(Class<?> clz) {
        try {
            return ((Class<?>) clz.getField("TYPE").get(null)).isPrimitive();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Gets the default value of given target class type.
     * @param clz target class type
     * @return null only if target class type is not boxed type of primitive type
     */
    public static Object wrapperDefaultValue(Class<?> clz) {
        if (!isPrimitiveWrapper(clz)) {
            return null;
        }
        try {
            return defaultValue((Class<?>) clz.getField("TYPE").get(null));
        } catch (Exception e) {
            return null;
        }
    }

    public static Object instantiateBeanIfNecessary(Object target) {
        if (target instanceof Class) {
            Class<?> clz = (Class<?>) target;
            if (clz.isInterface()) {
                throw new IllegalArgumentException("Could not instantiate an interface, class: " + clz.getName());
            }
            try {
                return clz.getDeclaredConstructor().newInstance();
            } catch (NoSuchMethodException e) {
                throw new IllegalArgumentException("No default constructor found, class: " + clz.getName(), e);
            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException("Constructor is not accessible, class: " + clz.getName(), e);
            } catch (InvocationTargetException | InstantiationException e) {
                throw new IllegalArgumentException("Error while instantiate class: " + clz.getName(), e);
            }
        }
        return target;
    }

    public static Collection<?> instantiateBeansIfNecessary(Collection<?> objects) {
        if (objects == null || objects.isEmpty()) {
            return Collections.emptyList();
        }
        List<Object> instances = new LinkedList<>();
        for (Object obj : objects) {
            if (obj != null) {
                instances.add(instantiateBeanIfNecessary(obj));
            }
        }
        return instances;
    }

    /**
     * Indicates whether the given object {@code o1} is "equal to" the object {@code o2}.
     *
     * @return {@code true} if  {@code o1} is the same as the {@code o2}; {@code false} otherwise.
     */
    public static boolean safeEquals(Object o1, Object o2) {
        if (o1 == o2) {
            return true;
        }
        if (o1 == null || o2 == null) {
            return false;
        }
        if (o1.equals(o2)) {
            return true;
        }
        if (o1.getClass().isArray() && o2.getClass().isArray()) {
            if (o1 instanceof Object[] && o2 instanceof Object[]) {
                return Arrays.equals((Object[]) o1, (Object[]) o2);
            }
            if (o1 instanceof boolean[] && o2 instanceof boolean[]) {
                return Arrays.equals((boolean[]) o1, (boolean[]) o2);
            }
            if (o1 instanceof byte[] && o2 instanceof byte[]) {
                return Arrays.equals((byte[]) o1, (byte[]) o2);
            }
            if (o1 instanceof char[] && o2 instanceof char[]) {
                return Arrays.equals((char[]) o1, (char[]) o2);
            }
            if (o1 instanceof double[] && o2 instanceof double[]) {
                return Arrays.equals((double[]) o1, (double[]) o2);
            }
            if (o1 instanceof float[] && o2 instanceof float[]) {
                return Arrays.equals((float[]) o1, (float[]) o2);
            }
            if (o1 instanceof int[] && o2 instanceof int[]) {
                return Arrays.equals((int[]) o1, (int[]) o2);
            }
            if (o1 instanceof long[] && o2 instanceof long[]) {
                return Arrays.equals((long[]) o1, (long[]) o2);
            }
            if (o1 instanceof short[] && o2 instanceof short[]) {
                return Arrays.equals((short[]) o1, (short[]) o2);
            }
        }
        return false;
    }

    private ObjectUtils() {
    }
}
