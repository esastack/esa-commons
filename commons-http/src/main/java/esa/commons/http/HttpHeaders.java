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
package esa.commons.http;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Interface for http headers which represents a mapping of key to value. Duplicate keys may be allowed by
 * implementations.
 * @deprecated use https://github.com/esastack/esa-commons-net instead
 */
@Deprecated
public interface HttpHeaders extends Iterable<Map.Entry<String, String>> {

    /**
     * @see #get(CharSequence)
     */
    String get(String name);

    /**
     * Returns the value of a header with the specified name. If there is more than one value for the specified name,
     * the first value in insertion order is returned.
     *
     * @param name the name of the header to retrieve
     *
     * @return the first header value if the header is found. {@code null} if there's no such header
     */
    String get(CharSequence name);

    /**
     * Returns the value of a header with the specified name. If there is more than one value for the specified name,
     * the first value in insertion order is returned.
     *
     * @param name         the name of the header to retrieve
     * @param defaultValue the default value
     *
     * @return the first header value or {@code defaultValue} if there is no such header
     */
    String get(CharSequence name, String defaultValue);

    /**
     * @see #getAll(CharSequence)
     */
    List<String> getAll(String name);

    /**
     * Returns all values for the header with the specified name. The returned {@link List} can't be modified.
     *
     * @param name the name of the header to retrieve
     *
     * @return a {@link List} of header values or an empty {@link List} if no values are found.
     */
    List<String> getAll(CharSequence name);

    /**
     * Returns the {@code boolean} value of a header with the specified name. If there is more than one value for the
     * specified name, the first value in insertion order is returned.
     *
     * @param name the name of the header to retrieve
     *
     * @return the {@code boolean} value of the first value in insertion order or {@code null} if there is no such value
     * or it can't be converted to {@code boolean}.
     */
    Boolean getBoolean(CharSequence name);

    /**
     * Returns the {@code boolean} value of a header with the specified name. If there is more than one value for the
     * specified name, the first value in insertion order is returned.
     *
     * @param name         the name of the header to retrieve
     * @param defaultValue the default value
     *
     * @return the {@code boolean} value of the first value in insertion order or {@code defaultValue} if there is no
     * such value or it can't be converted to {@code boolean}.
     */
    boolean getBoolean(CharSequence name, boolean defaultValue);

    /**
     * Returns the {@code byte} value of a header with the specified name. If there is more than one value for the
     * specified name, the first value in insertion order is returned.
     *
     * @param name the name of the header to retrieve
     *
     * @return the {@code byte} value of the first value in insertion order or {@code null} if there is no such value or
     * it can't be converted to {@code byte}.
     */
    Byte getByte(CharSequence name);

    /**
     * Returns the {@code byte} value of a header with the specified name. If there is more than one value for the
     * specified name, the first value in insertion order is returned.
     *
     * @param name         the name of the header to retrieve
     * @param defaultValue the default value
     *
     * @return the {@code byte} value of the first value in insertion order or {@code defaultValue} if there is no such
     * value or it can't be converted to {@code byte}.
     */
    byte getByte(CharSequence name, byte defaultValue);

    /**
     * Returns the {@code char} value of a header with the specified name. If there is more than one value for the
     * specified name, the first value in insertion order is returned.
     *
     * @param name the name of the header to retrieve
     *
     * @return the {@code char} value of the first value in insertion order or {@code null} if there is no such value or
     * it can't be converted to {@code char}.
     */
    Character getChar(CharSequence name);

    /**
     * Returns the {@code char} value of a header with the specified name. If there is more than one value for the
     * specified name, the first value in insertion order is returned.
     *
     * @param name         the name of the header to retrieve
     * @param defaultValue the default value
     *
     * @return the {@code char} value of the first value in insertion order or {@code defaultValue} if there is no such
     * value or it can't be converted to {@code char}.
     */
    char getChar(CharSequence name, char defaultValue);

    /**
     * Returns the {@code short} value of a header with the specified name. If there is more than one value for the
     * specified name, the first value in insertion order is returned.
     *
     * @param name the name of the header to retrieve
     *
     * @return the {@code short} value of the first value in insertion order or {@code null} if there is no such value
     * or it can't be converted to {@code short}.
     */
    Short getShort(CharSequence name);

    /**
     * Returns the {@code short} value of a header with the specified name. If there is more than one value for the
     * specified name, the first value in insertion order is returned.
     *
     * @param name         the name of the header to retrieve
     * @param defaultValue the default value
     *
     * @return the {@code short} value of the first value in insertion order or {@code defaultValue} if there is no such
     * value or it can't be converted to {@code short}.
     */
    short getShort(CharSequence name, short defaultValue);

    /**
     * Returns the {@code int} value of a header with the specified name. If there is more than one value for the
     * specified name, the first value in insertion order is returned.
     *
     * @param name the name of the header to retrieve
     *
     * @return the {@code int} value of the first value in insertion order or {@code null} if there is no such value or
     * it can't be converted to {@code int}.
     */
    Integer getInt(CharSequence name);

    /**
     * Returns the {@code int} value of a header with the specified name. If there is more than one value for the
     * specified name, the first value in insertion order is returned.
     *
     * @param name         the name of the header to retrieve
     * @param defaultValue the default value
     *
     * @return the {@code int} value of the first value in insertion order or {@code defaultValue} if there is no such
     * value or it can't be converted to {@code int}.
     */
    int getInt(CharSequence name, int defaultValue);

    /**
     * Returns the {@code long} value of a header with the specified name. If there is more than one value for the
     * specified name, the first value in insertion order is returned.
     *
     * @param name the name of the header to retrieve
     *
     * @return the {@code long} value of the first value in insertion order or {@code null} if there is no such value or
     * it can't be converted to {@code long}.
     */
    Long getLong(CharSequence name);

    /**
     * Returns the {@code long} value of a header with the specified name. If there is more than one value for the
     * specified name, the first value in insertion order is returned.
     *
     * @param name         the name of the header to retrieve
     * @param defaultValue the default value
     *
     * @return the {@code long} value of the first value in insertion order or {@code defaultValue} if there is no such
     * value or it can't be converted to {@code long}.
     */
    long getLong(CharSequence name, long defaultValue);

    /**
     * Returns the {@code float} value of a header with the specified name. If there is more than one value for the
     * specified name, the first value in insertion order is returned.
     *
     * @param name the name of the header to retrieve
     *
     * @return the {@code float} value of the first value in insertion order or {@code null} if there is no such value
     * or it can't be converted to {@code float}.
     */
    Float getFloat(CharSequence name);

    /**
     * Returns the {@code float} value of a header with the specified name. If there is more than one value for the
     * specified name, the first value in insertion order is returned.
     *
     * @param name         the name of the header to retrieve
     * @param defaultValue the default value
     *
     * @return the {@code float} value of the first value in insertion order or {@code defaultValue} if there is no such
     * value or it can't be converted to {@code float}.
     */
    float getFloat(CharSequence name, float defaultValue);

    /**
     * Returns the {@code double} value of a header with the specified name. If there is more than one value for the
     * specified name, the first value in insertion order is returned.
     *
     * @param name the name of the header to retrieve
     *
     * @return the {@code double} value of the first value in insertion order or {@code null} if there is no such value
     * or it can't be converted to {@code double}.
     */
    Double getDouble(CharSequence name);

    /**
     * Returns the {@code double} value of a header with the specified name. If there is more than one value for the
     * specified name, the first value in insertion order is returned.
     *
     * @param name         the name of the header to retrieve
     * @param defaultValue the default value
     *
     * @return the {@code double} value of the first value in insertion order or {@code defaultValue} if there is no
     * such value or it can't be converted to {@code double}.
     */
    double getDouble(CharSequence name, double defaultValue);

    /**
     * @see #contains(CharSequence)
     */
    boolean contains(String name);

    /**
     * Returns {@code true} if a header with the {@code name} exists, {@code false} otherwise.
     *
     * @param name the header name
     */
    boolean contains(CharSequence name);

    /**
     * @see #contains(CharSequence, CharSequence)
     */
    boolean contains(String name, String value);

    /**
     * Returns {@code true} if a header with the {@code name} and {@code value} exists, {@code false} otherwise.
     * <p>
     * The {@link Object#equals(Object)} method is used to test for equality of {@code value}.
     * </p>
     *
     * @param name  the header name
     * @param value the header value of the header to find
     */
    boolean contains(CharSequence name, CharSequence value);

    /**
     * @see #contains(CharSequence, CharSequence, boolean)
     */
    boolean contains(String name, String value, boolean ignoreCase);

    /**
     * Returns {@code true} if a header with the {@code name} and {@code value} exists, {@code false} otherwise.
     * <p>
     * If {@code caseInsensitive} is {@code true} then a case insensitive compare is done on the value.
     *
     * @param name       the name of the header to find
     * @param value      the value of the header to find
     * @param ignoreCase {@code true} then a case insensitive compare is run to compare values. otherwise a case
     *                   sensitive compare is run to compare values.
     */
    boolean contains(CharSequence name, CharSequence value, boolean ignoreCase);

    /**
     * Returns the number of underlying in this object.
     */
    int size();

    /**
     * Returns {@code true} if {@link #size()} equals {@code 0}.
     */
    boolean isEmpty();

    /**
     * Obtains names of current {@link HttpHeaders}.
     *
     * @return names
     */
    Set<String> names();

    /**
     * @see #add(CharSequence, Object)
     */
    HttpHeaders add(String name, Object value);

    /**
     * Adds a new header with the specified {@code name} and {@code value}.
     *
     * @param name  the name of the header
     * @param value the value of the header
     *
     * @return {@code this}
     */
    HttpHeaders add(CharSequence name, Object value);

    /**
     * @see #add(CharSequence, Iterable)
     */
    HttpHeaders add(String name, Iterable<?> values);

    /**
     * Adds new underlying with the specified {@code name} and {@code values}. This method is semantically equivalent
     * to
     *
     * <pre>
     * for (T value : values) {
     *     underlying.add(name, value);
     * }
     * </pre>
     *
     * @param name   the header name
     * @param values the values of the header
     *
     * @return {@code this}
     */
    HttpHeaders add(CharSequence name, Iterable<?> values);

    /**
     * Adds a new header.
     *
     * @param name  the header name
     * @param value the value of the header
     *
     * @return {@code this}
     */
    HttpHeaders addBoolean(CharSequence name, boolean value);

    /**
     * Adds a new header.
     *
     * @param name  the header name
     * @param value the value of the header
     *
     * @return {@code this}
     */
    HttpHeaders addByte(CharSequence name, byte value);

    /**
     * Adds a new header.
     *
     * @param name  the header name
     * @param value the value of the header
     *
     * @return {@code this}
     */
    HttpHeaders addChar(CharSequence name, char value);

    /**
     * Adds a new header.
     *
     * @param name  the header name
     * @param value the value of the header
     *
     * @return {@code this}
     */
    HttpHeaders addShort(CharSequence name, short value);

    /**
     * Adds a new header.
     *
     * @param name  the header name
     * @param value the value of the header
     *
     * @return {@code this}
     */
    HttpHeaders addInt(CharSequence name, int value);

    /**
     * Adds a new header.
     *
     * @param name  the header name
     * @param value the value of the header
     *
     * @return {@code this}
     */
    HttpHeaders addLong(CharSequence name, long value);

    /**
     * Adds a new header.
     *
     * @param name  the header name
     * @param value the value of the header
     *
     * @return {@code this}
     */
    HttpHeaders addFloat(CharSequence name, float value);

    /**
     * Adds a new header.
     *
     * @param name  the header name
     * @param value the value of the header
     *
     * @return {@code this}
     */
    HttpHeaders addDouble(CharSequence name, double value);

    /**
     * Adds all header names and values of {@code underlying} to this object.
     *
     * @return {@code this}
     */
    HttpHeaders add(HttpHeaders headers);

    /**
     * @see #set(CharSequence, Object)
     */
    HttpHeaders set(String name, Object value);

    /**
     * Sets a header with the specified name and value. Any existing underlying with the same name are overwritten.
     *
     * @param name  the header name
     * @param value the value of the header
     *
     * @return {@code this}
     */
    HttpHeaders set(CharSequence name, Object value);

    /**
     * @see #set(CharSequence, Iterable)
     */
    HttpHeaders set(String name, Iterable<?> values);

    /**
     * Sets a new header with the specified name and values. This method is equivalent to
     *
     * <pre>
     * for (T v : values) {
     *     underlying.addObject(name, v);
     * }
     * </pre>
     *
     * @param name   the header name
     * @param values the value of the header
     *
     * @return {@code this}
     */
    HttpHeaders set(CharSequence name, Iterable<?> values);

    /**
     * Set the {@code name} to {@code value}. This will remove all previous values associated with {@code name}.
     *
     * @param name  The name to modify
     * @param value The value
     *
     * @return {@code this}
     */
    HttpHeaders setBoolean(CharSequence name, boolean value);

    /**
     * Set the {@code name} to {@code value}. This will remove all previous values associated with {@code name}.
     *
     * @param name  The name to modify
     * @param value The value
     *
     * @return {@code this}
     */
    HttpHeaders setByte(CharSequence name, byte value);

    /**
     * Set the {@code name} to {@code value}. This will remove all previous values associated with {@code name}.
     *
     * @param name  The name to modify
     * @param value The value
     *
     * @return {@code this}
     */
    HttpHeaders setChar(CharSequence name, char value);

    /**
     * Set the {@code name} to {@code value}. This will remove all previous values associated with {@code name}.
     *
     * @param name  The name to modify
     * @param value The value
     *
     * @return {@code this}
     */
    HttpHeaders setShort(CharSequence name, short value);

    /**
     * Set the {@code name} to {@code value}. This will remove all previous values associated with {@code name}.
     *
     * @param name  The name to modify
     * @param value The value
     *
     * @return {@code this}
     */
    HttpHeaders setInt(CharSequence name, int value);

    /**
     * Set the {@code name} to {@code value}. This will remove all previous values associated with {@code name}.
     *
     * @param name  The name to modify
     * @param value The value
     *
     * @return {@code this}
     */
    HttpHeaders setLong(CharSequence name, long value);

    /**
     * Set the {@code name} to {@code value}. This will remove all previous values associated with {@code name}.
     *
     * @param name  The name to modify
     * @param value The value
     *
     * @return {@code this}
     */
    HttpHeaders setFloat(CharSequence name, float value);

    /**
     * Set the {@code name} to {@code value}. This will remove all previous values associated with {@code name}.
     *
     * @param name  The name to modify
     * @param value The value
     *
     * @return {@code this}
     */
    HttpHeaders setDouble(CharSequence name, double value);

    /**
     * Clears the current header entries and copies all header entries of the specified {@code underlying}.
     *
     * @return {@code this}
     */
    HttpHeaders set(HttpHeaders headers);

    /**
     * Retains all current underlying but calls {@link #set(CharSequence, Object)} for each entry in {@code
     * underlying}.
     *
     * @param headers The underlying used to {@link #set(CharSequence, Object)} values in this instance
     *
     * @return {@code this}
     */
    HttpHeaders setAll(HttpHeaders headers);

    /**
     * @see #remove(CharSequence)
     */
    HttpHeaders remove(String name);

    /**
     * Removes all underlying with the specified {@code name}.
     *
     * @param name the header name
     *
     * @return {@code true} if at least one entry has been removed.
     */
    HttpHeaders remove(CharSequence name);

    /**
     * Removes all underlying. After a call to this method {@link #size()} equals {@code 0}.
     *
     * @return {@code this}
     */
    HttpHeaders clear();

    /**
     * @return Iterator over the name/value header pairs.
     */
    Iterator<Map.Entry<CharSequence, CharSequence>> iteratorCharSequence();
}
