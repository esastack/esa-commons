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
package esa.commons.netty.http;

import esa.commons.http.HttpHeaders;
import io.netty.handler.codec.HeadersUtils;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Empty implements of {@link HttpHeaders}
 * @deprecated use https://github.com/esastack/esa-commons-net instead
 */
@Deprecated
public final class EmptyHttpHeaders implements HttpHeaders {

    public static final EmptyHttpHeaders INSTANCE = new EmptyHttpHeaders();

    private EmptyHttpHeaders() {
    }

    @Override
    public String get(String name) {
        return null;
    }

    @Override
    public String get(CharSequence name) {
        return null;
    }

    @Override
    public String get(CharSequence name, String defaultValue) {
        return defaultValue;
    }

    @Override
    public List<String> getAll(String name) {
        return Collections.emptyList();
    }

    @Override
    public List<String> getAll(CharSequence name) {
        return Collections.emptyList();
    }

    @Override
    public Boolean getBoolean(CharSequence name) {
        return null;
    }

    @Override
    public boolean getBoolean(CharSequence name, boolean defaultValue) {
        return defaultValue;
    }

    @Override
    public Byte getByte(CharSequence name) {
        return null;
    }

    @Override
    public byte getByte(CharSequence name, byte defaultValue) {
        return defaultValue;
    }

    @Override
    public Character getChar(CharSequence name) {
        return null;
    }

    @Override
    public char getChar(CharSequence name, char defaultValue) {
        return defaultValue;
    }

    @Override
    public Short getShort(CharSequence name) {
        return null;
    }

    @Override
    public short getShort(CharSequence name, short defaultValue) {
        return defaultValue;
    }

    @Override
    public Integer getInt(CharSequence name) {
        return null;
    }

    @Override
    public int getInt(CharSequence name, int defaultValue) {
        return defaultValue;
    }

    @Override
    public Long getLong(CharSequence name) {
        return null;
    }

    @Override
    public long getLong(CharSequence name, long defaultValue) {
        return defaultValue;
    }

    @Override
    public Float getFloat(CharSequence name) {
        return null;
    }

    @Override
    public float getFloat(CharSequence name, float defaultValue) {
        return defaultValue;
    }

    @Override
    public Double getDouble(CharSequence name) {
        return null;
    }

    @Override
    public double getDouble(CharSequence name, double defaultValue) {
        return defaultValue;
    }

    @Override
    public boolean contains(String name) {
        return false;
    }

    @Override
    public boolean contains(CharSequence name) {
        return false;
    }

    @Override
    public boolean contains(String name, String value) {
        return false;
    }

    @Override
    public boolean contains(CharSequence name, CharSequence value) {
        return false;
    }

    @Override
    public boolean contains(String name, String value, boolean ignoreCase) {
        return false;
    }

    @Override
    public boolean contains(CharSequence name, CharSequence value, boolean ignoreCase) {
        return false;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public Set<String> names() {
        return Collections.emptySet();
    }

    @Override
    public EmptyHttpHeaders add(String name, Object value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override
    public EmptyHttpHeaders add(CharSequence name, Object value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override
    public EmptyHttpHeaders add(String name, Iterable<?> values) {
        throw new UnsupportedOperationException("read only");
    }

    @Override
    public EmptyHttpHeaders add(CharSequence name, Iterable<?> values) {
        throw new UnsupportedOperationException("read only");
    }

    @Override
    public EmptyHttpHeaders addBoolean(CharSequence name, boolean value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override
    public EmptyHttpHeaders addByte(CharSequence name, byte value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override
    public EmptyHttpHeaders addChar(CharSequence name, char value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override
    public EmptyHttpHeaders addShort(CharSequence name, short value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override
    public EmptyHttpHeaders addInt(CharSequence name, int value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override
    public EmptyHttpHeaders addLong(CharSequence name, long value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override
    public EmptyHttpHeaders addFloat(CharSequence name, float value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override
    public EmptyHttpHeaders addDouble(CharSequence name, double value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override
    public EmptyHttpHeaders add(HttpHeaders headers) {
        throw new UnsupportedOperationException("read only");
    }

    @Override
    public EmptyHttpHeaders set(String name, Object value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override
    public EmptyHttpHeaders set(CharSequence name, Object value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override
    public EmptyHttpHeaders set(String name, Iterable<?> values) {
        throw new UnsupportedOperationException("read only");
    }

    @Override
    public EmptyHttpHeaders set(CharSequence name, Iterable<?> values) {
        throw new UnsupportedOperationException("read only");
    }

    @Override
    public EmptyHttpHeaders setBoolean(CharSequence name, boolean value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override
    public EmptyHttpHeaders setByte(CharSequence name, byte value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override
    public EmptyHttpHeaders setChar(CharSequence name, char value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override
    public EmptyHttpHeaders setShort(CharSequence name, short value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override
    public EmptyHttpHeaders setInt(CharSequence name, int value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override
    public EmptyHttpHeaders setLong(CharSequence name, long value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override
    public EmptyHttpHeaders setFloat(CharSequence name, float value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override
    public EmptyHttpHeaders setDouble(CharSequence name, double value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override
    public EmptyHttpHeaders set(HttpHeaders headers) {
        throw new UnsupportedOperationException("read only");
    }

    @Override
    public EmptyHttpHeaders setAll(HttpHeaders headers) {
        throw new UnsupportedOperationException("read only");
    }

    @Override
    public EmptyHttpHeaders remove(String name) {
        throw new UnsupportedOperationException("read only");
    }

    @Override
    public EmptyHttpHeaders remove(CharSequence name) {
        throw new UnsupportedOperationException("read only");
    }

    @Override
    public EmptyHttpHeaders clear() {
        throw new UnsupportedOperationException("read only");
    }

    @Override
    public Iterator<Map.Entry<CharSequence, CharSequence>> iteratorCharSequence() {
        List<Map.Entry<CharSequence, CharSequence>> empty = Collections.emptyList();
        return empty.iterator();
    }

    @Override
    public Iterator<Map.Entry<String, String>> iterator() {
        List<Map.Entry<String, String>> empty = Collections.emptyList();
        return empty.iterator();
    }

    @Override
    public String toString() {
        return HeadersUtils.toString(getClass(), iteratorCharSequence(), size());
    }
}
