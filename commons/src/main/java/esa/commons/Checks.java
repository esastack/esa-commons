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

import java.util.Collection;
import java.util.Map;

/**
 * Methods to be called for verifying correct arguments and states.
 */
public final class Checks {

    /**
     * Check given reference is not {@code null}.
     *
     * @param ref reference
     * @param <T> the type of the reference
     * @return given reference if not {@code null}
     * @throws NullPointerException if given reference is {@code null}
     */
    public static <T> T checkNotNull(T ref) {
        if (ref == null) {
            throw new NullPointerException();
        }
        return ref;
    }

    /**
     * Check given reference is not {@code null}.
     *
     * @param ref     reference
     * @param message error message
     * @param <T>     the type of the reference
     * @return given reference if not {@code null}
     * @throws NullPointerException if given reference is {@code null}
     */
    public static <T> T checkNotNull(T ref, String message) {
        if (ref == null) {
            throw new NullPointerException(stringValue(message));
        }
        return ref;
    }

    /**
     * Check given reference is not {@code null}.
     *
     * @param ref     reference
     * @param message error message
     * @param values  the optional values for the formatted exception message
     * @param <T>     the type of the reference
     * @return given reference if not {@code null}
     * @throws NullPointerException if given reference is {@code null}
     */
    public static <T> T checkNotNull(T ref, String message, Object... values) {
        if (ref == null) {
            throw new NullPointerException(String.format(message, values));
        }
        return ref;
    }

    /**
     * Check given predicate is {@code true}.
     *
     * @param predicate predicate
     * @throws IllegalArgumentException if given predicate is false
     */
    public static void checkArg(boolean predicate) {
        if (!predicate) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Check given predicate is {@code true}.
     *
     * @param predicate predicate
     * @param message   error message
     * @throws IllegalArgumentException if given predicate is {@code true}
     */
    public static void checkArg(boolean predicate, String message) {
        if (!predicate) {
            throw new IllegalArgumentException(stringValue(message));
        }
    }

    /**
     * Check given predicate is {@code true}.
     *
     * @param predicate predicate
     * @param message   error message
     * @param values    the optional values for the formatted exception message
     * @throws IllegalArgumentException if given predicate is {@code true}
     */
    public static void checkArg(boolean predicate, String message, Object... values) {
        if (!predicate) {
            throw new IllegalArgumentException(String.format(message, values));
        }
    }

    /**
     * Check given predicate is {@code true}
     *
     * @param predicate predicate
     * @throws IllegalStateException if given predicate is {@code false}
     */
    public static void checkState(boolean predicate) {
        if (!predicate) {
            throw new IllegalStateException();
        }
    }

    /**
     * Check given predicate is {@code true}
     *
     * @param predicate predicate
     * @param message   error message
     * @throws IllegalStateException if given predicate is {@code false}
     */
    public static void checkState(boolean predicate, String message) {
        if (!predicate) {
            throw new IllegalStateException(stringValue(message));
        }
    }

    /**
     * Check given predicate is {@code true}
     *
     * @param predicate predicate
     * @param message   error message
     * @param values    the optional values for the formatted exception message
     * @throws IllegalStateException if given predicate is {@code false}
     */
    public static void checkState(boolean predicate, String message, Object... values) {
        if (!predicate) {
            throw new IllegalStateException(String.format(message, values));
        }
    }

    /**
     * Check given string is not empty.
     *
     * @param string string
     * @return given string if it is not empty.
     * @throws IllegalArgumentException if given given string is empty
     */
    public static String checkNotEmptyArg(String string) {
        if (StringUtils.isEmpty(string)) {
            throw new IllegalArgumentException();
        }
        return string;
    }

    /**
     * Check given string is not empty.
     *
     * @param string  string
     * @param message error message
     * @return given string if it is not empty.
     * @throws IllegalArgumentException if given given string is empty
     */
    public static String checkNotEmptyArg(String string, String message) {
        if (StringUtils.isEmpty(string)) {
            throw new IllegalArgumentException(message);
        }
        return string;
    }

    /**
     * Check given string is not empty.
     *
     * @param string  string
     * @param message error message
     * @param objects the optional values for the formatted exception message
     * @return given string if it is not empty.
     * @throws IllegalArgumentException if given given string is empty
     */
    public static String checkNotEmptyArg(String string, String message, Object... objects) {
        if (StringUtils.isEmpty(string)) {
            throw new IllegalArgumentException(String.format(message, objects));
        }
        return string;
    }

    /**
     * Check given collection is not empty.
     *
     * @param elements elements
     * @param <T>      elements type
     * @throws IllegalArgumentException if given collection is empty
     */
    public static <T> void checkNotEmptyArg(Collection<T> elements) {
        if (elements == null || elements.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Check given collection is not empty.
     *
     * @param elements elements
     * @param message  error message
     * @param <T>      elements type
     * @throws IllegalArgumentException if given collection is empty
     */
    public static <T> void checkNotEmptyArg(Collection<T> elements, String message) {
        if (elements == null || elements.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Check given collection is not empty.
     *
     * @param elements elements
     * @param message  error message
     * @param values   the optional values for the formatted exception message
     * @param <T>      elements type
     * @throws IllegalArgumentException if given collection is empty
     */
    public static <T> void checkNotEmptyArg(Collection<T> elements, String message, Object... values) {
        if (elements == null || elements.isEmpty()) {
            throw new IllegalArgumentException(String.format(message, values));
        }
    }

    /**
     * Check given reference array is not empty.
     *
     * @param arr reference array
     * @param <T> the type of the array elements
     * @return given array if it is not empty
     * @throws IllegalArgumentException if given array is empty.
     */
    public static <T> T[] checkNotEmptyArg(T[] arr) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException();
        }
        return arr;
    }

    /**
     * Check given reference array is not empty.
     *
     * @param arr     reference array
     * @param message error message
     * @param <T>     the type of the array elements
     * @return given array if it is not empty
     * @throws IllegalArgumentException if given array is empty.
     */
    public static <T> T[] checkNotEmptyArg(T[] arr, String message) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException(message);
        }
        return arr;
    }

    /**
     * Check given reference array is not empty.
     *
     * @param arr     reference array
     * @param message error message
     * @param values  the optional values for the formatted exception message
     * @param <T>     the type of the array elements
     * @return given array if it is not empty
     * @throws IllegalArgumentException if given array is empty.
     */
    public static <T> T[] checkNotEmptyArg(T[] arr, String message, Object... values) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException(String.format(message, values));
        }
        return arr;
    }

    /**
     * Check given long array is not empty.
     *
     * @param arr long array
     * @return given array if it is not empty
     * @throws IllegalArgumentException if given array is empty.
     */
    public static long[] checkNotEmptyArg(long[] arr) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException();
        }
        return arr;
    }

    /**
     * Check given long array is not empty.
     *
     * @param arr     long array
     * @param message error message
     * @return given array if it is not empty
     * @throws IllegalArgumentException if given array is empty.
     */
    public static long[] checkNotEmptyArg(long[] arr, String message) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException(message);
        }
        return arr;
    }

    /**
     * Check given long array is not empty.
     *
     * @param arr     long array
     * @param message error message
     * @param values  the optional values for the formatted exception message
     * @return given array if it is not empty
     * @throws IllegalArgumentException if given array is empty.
     */
    public static long[] checkNotEmptyArg(long[] arr, String message, Object... values) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException(String.format(message, values));
        }
        return arr;
    }

    /**
     * Check given double array is not empty.
     *
     * @param arr double array
     * @return given array if it is not empty
     * @throws IllegalArgumentException if given array is empty.
     */
    public static double[] checkNotEmptyArg(double[] arr) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException();
        }
        return arr;
    }

    /**
     * Check given double array is not empty.
     *
     * @param arr     double array
     * @param message error message
     * @return given array if it is not empty
     * @throws IllegalArgumentException if given array is empty.
     */
    public static double[] checkNotEmptyArg(double[] arr, String message) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException(message);
        }
        return arr;
    }

    /**
     * Check given double array is not empty.
     *
     * @param arr     double array
     * @param message error message
     * @param values  the optional values for the formatted exception message
     * @return given array if it is not empty
     * @throws IllegalArgumentException if given array is empty.
     */
    public static double[] checkNotEmptyArg(double[] arr, String message, Object... values) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException(String.format(message, values));
        }
        return arr;
    }

    /**
     * Check given int array is not empty.
     *
     * @param arr long array
     * @return given array if it is not empty
     * @throws IllegalArgumentException if given array is empty.
     */
    public static int[] checkNotEmptyArg(int[] arr) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException();
        }
        return arr;
    }

    /**
     * Check given int array is not empty.
     *
     * @param arr     int array
     * @param message error message
     * @return given array if it is not empty
     * @throws IllegalArgumentException if given array is empty.
     */
    public static int[] checkNotEmptyArg(int[] arr, String message) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException(message);
        }
        return arr;
    }

    /**
     * Check given int array is not empty.
     *
     * @param arr     int array
     * @param message error message
     * @param values  the optional values for the formatted exception message
     * @return given array if it is not empty
     * @throws IllegalArgumentException if given array is empty.
     */
    public static int[] checkNotEmptyArg(int[] arr, String message, Object... values) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException(String.format(message, values));
        }
        return arr;
    }

    /**
     * Check given float array is not empty.
     *
     * @param arr long array
     * @return given array if it is not empty
     * @throws IllegalArgumentException if given array is empty.
     */
    public static float[] checkNotEmptyArg(float[] arr) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException();
        }
        return arr;
    }

    /**
     * Check given float array is not empty.
     *
     * @param arr     float array
     * @param message error message
     * @return given array if it is not empty
     * @throws IllegalArgumentException if given array is empty.
     */
    public static float[] checkNotEmptyArg(float[] arr, String message) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException(message);
        }
        return arr;
    }

    /**
     * Check given float array is not empty.
     *
     * @param arr     float array
     * @param message error message
     * @param values  the optional values for the formatted exception message
     * @return given array if it is not empty
     * @throws IllegalArgumentException if given array is empty.
     */
    public static float[] checkNotEmptyArg(float[] arr, String message, Object... values) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException(String.format(message, values));
        }
        return arr;
    }

    /**
     * Check given boolean array is not empty.
     *
     * @param arr boolean array
     * @return given array if it is not empty
     * @throws IllegalArgumentException if given array is empty.
     */
    public static boolean[] checkNotEmptyArg(boolean[] arr) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException();
        }
        return arr;
    }

    /**
     * Check given boolean array is not empty.
     *
     * @param arr     boolean array
     * @param message error message
     * @return given array if it is not empty
     * @throws IllegalArgumentException if given array is empty.
     */
    public static boolean[] checkNotEmptyArg(boolean[] arr, String message) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException(message);
        }
        return arr;
    }

    /**
     * Check given boolean array is not empty.
     *
     * @param arr     boolean array
     * @param message error message
     * @param values  the optional values for the formatted exception message
     * @return given array if it is not empty
     * @throws IllegalArgumentException if given array is empty.
     */
    public static boolean[] checkNotEmptyArg(boolean[] arr, String message, Object... values) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException(String.format(message, values));
        }
        return arr;
    }

    /**
     * Check given short array is not empty.
     *
     * @param arr short array
     * @return given array if it is not empty
     * @throws IllegalArgumentException if given array is empty.
     */
    public static short[] checkNotEmptyArg(short[] arr) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException();
        }
        return arr;
    }

    /**
     * Check given short array is not empty.
     *
     * @param arr     short array
     * @param message error message
     * @return given array if it is not empty
     * @throws IllegalArgumentException if given array is empty.
     */
    public static short[] checkNotEmptyArg(short[] arr, String message) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException(message);
        }
        return arr;
    }

    /**
     * Check given short array is not empty.
     *
     * @param arr     short array
     * @param message error message
     * @param values  the optional values for the formatted exception message
     * @return given array if it is not empty
     * @throws IllegalArgumentException if given array is empty.
     */
    public static short[] checkNotEmptyArg(short[] arr, String message, Object... values) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException(String.format(message, values));
        }
        return arr;
    }

    /**
     * Check given char array is not empty.
     *
     * @param arr char array
     * @return given array if it is not empty
     * @throws IllegalArgumentException if given array is empty.
     */
    public static char[] checkNotEmptyArg(char[] arr) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException();
        }
        return arr;
    }

    /**
     * Check given char array is not empty.
     *
     * @param arr     char array
     * @param message error message
     * @return given array if it is not empty
     * @throws IllegalArgumentException if given array is empty.
     */
    public static char[] checkNotEmptyArg(char[] arr, String message) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException(message);
        }
        return arr;
    }

    /**
     * Check given char array is not empty.
     *
     * @param arr     char array
     * @param message error message
     * @param values  the optional values for the formatted exception message
     * @return given array if it is not empty
     * @throws IllegalArgumentException if given array is empty.
     */
    public static char[] checkNotEmptyArg(char[] arr, String message, Object... values) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException(String.format(message, values));
        }
        return arr;
    }

    /**
     * Check given byte array is not empty.
     *
     * @param arr byte array
     * @return given array if it is not empty
     * @throws IllegalArgumentException if given array is empty.
     */
    public static byte[] checkNotEmptyArg(byte[] arr) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException();
        }
        return arr;
    }

    /**
     * Check given byte array is not empty.
     *
     * @param arr     byte array
     * @param message error message
     * @return given array if it is not empty
     * @throws IllegalArgumentException if given array is empty.
     */
    public static byte[] checkNotEmptyArg(byte[] arr, String message) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException(message);
        }
        return arr;
    }

    /**
     * Check given byte array is not empty.
     *
     * @param arr     byte array
     * @param message error message
     * @param values  the optional values for the formatted exception message
     * @return given array if it is not empty
     * @throws IllegalArgumentException if given array is empty.
     */
    public static byte[] checkNotEmptyArg(byte[] arr, String message, Object... values) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException(String.format(message, values));
        }
        return arr;
    }

    /**
     * Check given map is not empty.
     *
     * @param map map
     * @param <K> the type of the key
     * @param <V> the type of the value
     * @throws IllegalArgumentException if given map is empty.
     */
    public static <K, V> void checkNotEmptyArg(Map<K, V> map) {
        if (map == null || map.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Check given map is not empty.
     *
     * @param map     map
     * @param message error message
     * @param <K>     the type of the key
     * @param <V>     the type of the value
     * @throws IllegalArgumentException if given map is empty.
     */
    public static <K, V> void checkNotEmptyArg(Map<K, V> map, String message) {
        if (map == null || map.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Check given map is not empty.
     *
     * @param map     map
     * @param message error message
     * @param values  the optional values for the formatted exception message
     * @param <K>     the type of the key
     * @param <V>     the type of the value
     * @throws IllegalArgumentException if given map is empty.
     */
    public static <K, V> void checkNotEmptyArg(Map<K, V> map, String message, Object... values) {
        if (map == null || map.isEmpty()) {
            throw new IllegalArgumentException(String.format(message, values));
        }
    }

    /**
     * Check given string is not empty.
     *
     * @param string string
     * @return given string if it is not empty.
     * @throws IllegalStateException if given given string is empty
     */
    public static String checkNotEmptyState(String string) {
        if (StringUtils.isEmpty(string)) {
            throw new IllegalStateException();
        }
        return string;
    }

    /**
     * Check given string is not empty.
     *
     * @param string  string
     * @param message error message
     * @return given string if it is not empty.
     * @throws IllegalStateException if given given string is empty
     */
    public static String checkNotEmptyState(String string, String message) {
        if (StringUtils.isEmpty(string)) {
            throw new IllegalStateException(message);
        }
        return string;
    }

    /**
     * Check given string is not empty.
     *
     * @param string  string
     * @param message error message
     * @param values  the optional values for the formatted exception message
     * @return given string if it is not empty.
     * @throws IllegalStateException if given given string is empty
     */
    public static String checkNotEmptyState(String string, String message, Object... values) {
        if (StringUtils.isEmpty(string)) {
            throw new IllegalStateException(String.format(message, values));
        }
        return string;
    }

    /**
     * Check given collection is not empty.
     *
     * @param elements elements
     * @param <T>      elements type
     * @throws IllegalStateException if given collection is empty
     */
    public static <T> void checkNotEmptyState(Collection<T> elements) {
        if (elements == null || elements.isEmpty()) {
            throw new IllegalStateException();
        }
    }

    /**
     * Check given collection is not empty.
     *
     * @param elements elements
     * @param message  error message
     * @param <T>      elements type
     * @throws IllegalStateException if given collection is empty
     */
    public static <T> void checkNotEmptyState(Collection<T> elements, String message) {
        if (elements == null || elements.isEmpty()) {
            throw new IllegalStateException(message);
        }
    }

    /**
     * Check given collection is not empty.
     *
     * @param elements elements
     * @param message  error message
     * @param values   the optional values for the formatted exception message
     * @param <T>      elements type
     * @throws IllegalStateException if given collection is empty
     */
    public static <T> void checkNotEmptyState(Collection<T> elements, String message, Object... values) {
        if (elements == null || elements.isEmpty()) {
            throw new IllegalStateException(String.format(message, values));
        }
    }

    /**
     * Check given reference array is not empty.
     *
     * @param arr reference array
     * @param <T> the type of the array elements
     * @return given array if it is not empty
     * @throws IllegalStateException if given array is empty.
     */
    public static <T> T[] checkNotEmptyState(T[] arr) {
        if (arr == null || arr.length == 0) {
            throw new IllegalStateException();
        }
        return arr;
    }

    /**
     * Check given reference array is not empty.
     *
     * @param arr     reference array
     * @param message error message
     * @param <T>     the type of the array elements
     * @return given array if it is not empty
     * @throws IllegalStateException if given array is empty.
     */
    public static <T> T[] checkNotEmptyState(T[] arr, String message) {
        if (arr == null || arr.length == 0) {
            throw new IllegalStateException(message);
        }
        return arr;
    }

    /**
     * Check given reference array is not empty.
     *
     * @param arr     reference array
     * @param message error message
     * @param values  the optional values for the formatted exception message
     * @param <T>     the type of the array elements
     * @return given array if it is not empty
     * @throws IllegalStateException if given array is empty.
     */
    public static <T> T[] checkNotEmptyState(T[] arr, String message, Object... values) {
        if (arr == null || arr.length == 0) {
            throw new IllegalStateException(String.format(message, values));
        }
        return arr;
    }

    /**
     * Check given long array is not empty.
     *
     * @param arr long array
     * @return given array if it is not empty
     * @throws IllegalStateException if given array is empty.
     */
    public static long[] checkNotEmptyState(long[] arr) {
        if (arr == null || arr.length == 0) {
            throw new IllegalStateException();
        }
        return arr;
    }

    /**
     * Check given long array is not empty.
     *
     * @param arr     long array
     * @param message error message
     * @return given array if it is not empty
     * @throws IllegalStateException if given array is empty.
     */
    public static long[] checkNotEmptyState(long[] arr, String message) {
        if (arr == null || arr.length == 0) {
            throw new IllegalStateException(message);
        }
        return arr;
    }

    /**
     * Check given long array is not empty.
     *
     * @param arr     long array
     * @param message error message
     * @param values  the optional values for the formatted exception message
     * @return given array if it is not empty
     * @throws IllegalStateException if given array is empty.
     */
    public static long[] checkNotEmptyState(long[] arr, String message, Object... values) {
        if (arr == null || arr.length == 0) {
            throw new IllegalStateException(String.format(message, values));
        }
        return arr;
    }


    /**
     * Check given double array is not empty.
     *
     * @param arr double array
     * @return given array if it is not empty
     * @throws IllegalStateException if given array is empty.
     */
    public static double[] checkNotEmptyState(double[] arr) {
        if (arr == null || arr.length == 0) {
            throw new IllegalStateException();
        }
        return arr;
    }

    /**
     * Check given double array is not empty.
     *
     * @param arr     double array
     * @param message error message
     * @return given array if it is not empty
     * @throws IllegalStateException if given array is empty.
     */
    public static double[] checkNotEmptyState(double[] arr, String message) {
        if (arr == null || arr.length == 0) {
            throw new IllegalStateException(message);
        }
        return arr;
    }

    /**
     * Check given double array is not empty.
     *
     * @param arr     double array
     * @param message error message
     * @param values  the optional values for the formatted exception message
     * @return given array if it is not empty
     * @throws IllegalStateException if given array is empty.
     */
    public static double[] checkNotEmptyState(double[] arr, String message, Object... values) {
        if (arr == null || arr.length == 0) {
            throw new IllegalStateException(String.format(message, values));
        }
        return arr;
    }

    /**
     * Check given int array is not empty.
     *
     * @param arr long array
     * @return given array if it is not empty
     * @throws IllegalStateException if given array is empty.
     */
    public static int[] checkNotEmptyState(int[] arr) {
        if (arr == null || arr.length == 0) {
            throw new IllegalStateException();
        }
        return arr;
    }

    /**
     * Check given int array is not empty.
     *
     * @param arr     int array
     * @param message error message
     * @return given array if it is not empty
     * @throws IllegalStateException if given array is empty.
     */
    public static int[] checkNotEmptyState(int[] arr, String message) {
        if (arr == null || arr.length == 0) {
            throw new IllegalStateException(message);
        }
        return arr;
    }

    /**
     * Check given int array is not empty.
     *
     * @param arr     int array
     * @param message error message
     * @param values  the optional values for the formatted exception message
     * @return given array if it is not empty
     * @throws IllegalStateException if given array is empty.
     */
    public static int[] checkNotEmptyState(int[] arr, String message, Object... values) {
        if (arr == null || arr.length == 0) {
            throw new IllegalStateException(String.format(message, values));
        }
        return arr;
    }

    /**
     * Check given float array is not empty.
     *
     * @param arr long array
     * @return given array if it is not empty
     * @throws IllegalStateException if given array is empty.
     */
    public static float[] checkNotEmptyState(float[] arr) {
        if (arr == null || arr.length == 0) {
            throw new IllegalStateException();
        }
        return arr;
    }

    /**
     * Check given float array is not empty.
     *
     * @param arr     float array
     * @param message error message
     * @return given array if it is not empty
     * @throws IllegalStateException if given array is empty.
     */
    public static float[] checkNotEmptyState(float[] arr, String message) {
        if (arr == null || arr.length == 0) {
            throw new IllegalStateException(message);
        }
        return arr;
    }

    /**
     * Check given float array is not empty.
     *
     * @param arr     float array
     * @param message error message
     * @param values  the optional values for the formatted exception message
     * @return given array if it is not empty
     * @throws IllegalStateException if given array is empty.
     */
    public static float[] checkNotEmptyState(float[] arr, String message, Object... values) {
        if (arr == null || arr.length == 0) {
            throw new IllegalStateException(String.format(message, values));
        }
        return arr;
    }

    /**
     * Check given boolean array is not empty.
     *
     * @param arr boolean array
     * @return given array if it is not empty
     * @throws IllegalStateException if given array is empty.
     */
    public static boolean[] checkNotEmptyState(boolean[] arr) {
        if (arr == null || arr.length == 0) {
            throw new IllegalStateException();
        }
        return arr;
    }

    /**
     * Check given boolean array is not empty.
     *
     * @param arr     boolean array
     * @param message error message
     * @return given array if it is not empty
     * @throws IllegalStateException if given array is empty.
     */
    public static boolean[] checkNotEmptyState(boolean[] arr, String message) {
        if (arr == null || arr.length == 0) {
            throw new IllegalStateException(message);
        }
        return arr;
    }

    /**
     * Check given boolean array is not empty.
     *
     * @param arr     boolean array
     * @param message error message
     * @param values  the optional values for the formatted exception message
     * @return given array if it is not empty
     * @throws IllegalStateException if given array is empty.
     */
    public static boolean[] checkNotEmptyState(boolean[] arr, String message, Object... values) {
        if (arr == null || arr.length == 0) {
            throw new IllegalStateException(String.format(message, values));
        }
        return arr;
    }

    /**
     * Check given short array is not empty.
     *
     * @param arr short array
     * @return given array if it is not empty
     * @throws IllegalStateException if given array is empty.
     */
    public static short[] checkNotEmptyState(short[] arr) {
        if (arr == null || arr.length == 0) {
            throw new IllegalStateException();
        }
        return arr;
    }

    /**
     * Check given short array is not empty.
     *
     * @param arr     short array
     * @param message error message
     * @return given array if it is not empty
     * @throws IllegalStateException if given array is empty.
     */
    public static short[] checkNotEmptyState(short[] arr, String message) {
        if (arr == null || arr.length == 0) {
            throw new IllegalStateException(message);
        }
        return arr;
    }

    /**
     * Check given short array is not empty.
     *
     * @param arr     short array
     * @param message error message
     * @param values  the optional values for the formatted exception message
     * @return given array if it is not empty
     * @throws IllegalStateException if given array is empty.
     */
    public static short[] checkNotEmptyState(short[] arr, String message, Object... values) {
        if (arr == null || arr.length == 0) {
            throw new IllegalStateException(String.format(message, values));
        }
        return arr;
    }

    /**
     * Check given char array is not empty.
     *
     * @param arr char array
     * @return given array if it is not empty
     * @throws IllegalStateException if given array is empty.
     */
    public static char[] checkNotEmptyState(char[] arr) {
        if (arr == null || arr.length == 0) {
            throw new IllegalStateException();
        }
        return arr;
    }

    /**
     * Check given char array is not empty.
     *
     * @param arr     char array
     * @param message error message
     * @return given array if it is not empty
     * @throws IllegalStateException if given array is empty.
     */
    public static char[] checkNotEmptyState(char[] arr, String message) {
        if (arr == null || arr.length == 0) {
            throw new IllegalStateException(message);
        }
        return arr;
    }

    /**
     * Check given char array is not empty.
     *
     * @param arr     char array
     * @param message error message
     * @param values  the optional values for the formatted exception message
     * @return given array if it is not empty
     * @throws IllegalStateException if given array is empty.
     */
    public static char[] checkNotEmptyState(char[] arr, String message, Object... values) {
        if (arr == null || arr.length == 0) {
            throw new IllegalStateException(String.format(message, values));
        }
        return arr;
    }

    /**
     * Check given byte array is not empty.
     *
     * @param arr byte array
     * @return given array if it is not empty
     * @throws IllegalStateException if given array is empty.
     */
    public static byte[] checkNotEmptyState(byte[] arr) {
        if (arr == null || arr.length == 0) {
            throw new IllegalStateException();
        }
        return arr;
    }

    /**
     * Check given byte array is not empty.
     *
     * @param arr     byte array
     * @param message error message
     * @return given array if it is not empty
     * @throws IllegalStateException if given array is empty.
     */
    public static byte[] checkNotEmptyState(byte[] arr, String message) {
        if (arr == null || arr.length == 0) {
            throw new IllegalStateException(message);
        }
        return arr;
    }

    /**
     * Check given byte array is not empty.
     *
     * @param arr     byte array
     * @param message error message
     * @param values  the optional values for the formatted exception message
     * @return given array if it is not empty
     * @throws IllegalStateException if given array is empty.
     */
    public static byte[] checkNotEmptyState(byte[] arr, String message, Object... values) {
        if (arr == null || arr.length == 0) {
            throw new IllegalStateException(String.format(message, values));
        }
        return arr;
    }

    /**
     * Check given map is not empty.
     *
     * @param map map
     * @param <K> the type of the key
     * @param <V> the type of the value
     * @throws IllegalStateException if given map is empty.
     */
    public static <K, V> void checkNotEmptyState(Map<K, V> map) {
        if (map == null || map.isEmpty()) {
            throw new IllegalStateException();
        }
    }

    /**
     * Check given map is not empty.
     *
     * @param map     map
     * @param message error message
     * @param <K>     the type of the key
     * @param <V>     the type of the value
     * @throws IllegalStateException if given map is empty.
     */
    public static <K, V> void checkNotEmptyState(Map<K, V> map, String message) {
        if (map == null || map.isEmpty()) {
            throw new IllegalStateException(message);
        }
    }

    /**
     * Check given map is not empty.
     *
     * @param map     map
     * @param message error message
     * @param values  the optional values for the formatted exception message
     * @param <K>     the type of the key
     * @param <V>     the type of the value
     * @throws IllegalStateException if given map is empty.
     */
    public static <K, V> void checkNotEmptyState(Map<K, V> map, String message, Object... values) {
        if (map == null || map.isEmpty()) {
            throw new IllegalStateException(String.format(message, values));
        }
    }

    private static String stringValue(String message) {
        return message == null ? StringUtils.empty() : message;
    }

    private Checks() {
    }
}
