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

import esa.commons.Checks;
import esa.commons.http.HttpHeaders;
import io.netty.handler.codec.HeadersUtils;
import io.netty.handler.codec.http.DefaultHttpHeaders;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static esa.commons.netty.http.Utils.VALUE_CONVERTER;

/**
 * Using a instance of {@link io.netty.handler.codec.http.HttpHeaders} as a proxy.
 * @deprecated use https://github.com/esastack/esa-commons-net instead
 */
@Deprecated
public class Http1HeadersAdaptor implements HttpHeaders {

    protected final io.netty.handler.codec.http.HttpHeaders underlying;

    public Http1HeadersAdaptor() {
        this(new DefaultHttpHeaders());
    }

    public Http1HeadersAdaptor(boolean validate) {
        this(new DefaultHttpHeaders(validate));
    }

    public Http1HeadersAdaptor(io.netty.handler.codec.http.HttpHeaders underlying) {
        this.underlying = underlying;
    }

    @Override
    public String get(String name) {
        return underlying.get(name);
    }

    @Override
    public String get(CharSequence name) {
        return underlying.get(name);
    }

    @Override
    public String get(CharSequence name, String defaultValue) {
        return underlying.get(name, defaultValue);
    }

    @Override
    public List<String> getAll(String name) {
        return underlying.getAll(name);
    }

    @Override
    public List<String> getAll(CharSequence name) {
        return underlying.getAll(name);
    }

    @Override
    public Boolean getBoolean(CharSequence name) {
        String v = get(name);
        try {
            return v != null ? VALUE_CONVERTER.convertToBoolean(v) : null;
        } catch (RuntimeException ignore) {
            return null;
        }
    }

    @Override
    public boolean getBoolean(CharSequence name, boolean defaultValue) {
        Boolean v = getBoolean(name);
        return v != null ? v : defaultValue;
    }

    @Override
    public Byte getByte(CharSequence name) {
        String v = get(name);
        try {
            return v != null ? VALUE_CONVERTER.convertToByte(v) : null;
        } catch (RuntimeException ignore) {
            return null;
        }
    }

    @Override
    public byte getByte(CharSequence name, byte defaultValue) {
        Byte v = getByte(name);
        return v != null ? v : defaultValue;
    }

    @Override
    public Character getChar(CharSequence name) {
        String v = get(name);
        try {
            return v != null ? VALUE_CONVERTER.convertToChar(v) : null;
        } catch (RuntimeException ignore) {
            return null;
        }
    }

    @Override
    public char getChar(CharSequence name, char defaultValue) {
        Character v = getChar(name);
        return v != null ? v : defaultValue;
    }

    @Override
    public Short getShort(CharSequence name) {
        return underlying.getShort(name);
    }

    @Override
    public short getShort(CharSequence name, short defaultValue) {
        return underlying.getShort(name, defaultValue);
    }

    @Override
    public Integer getInt(CharSequence name) {
        return underlying.getInt(name);
    }

    @Override
    public int getInt(CharSequence name, int defaultValue) {
        return underlying.getInt(name, defaultValue);
    }

    @Override
    public Long getLong(CharSequence name) {
        String v = get(name);
        try {
            return v != null ? VALUE_CONVERTER.convertToLong(v) : null;
        } catch (RuntimeException ignore) {
            return null;
        }
    }

    @Override
    public long getLong(CharSequence name, long defaultValue) {
        Long v = getLong(name);
        return v != null ? v : defaultValue;
    }

    @Override
    public Float getFloat(CharSequence name) {
        String v = get(name);
        try {
            return v != null ? VALUE_CONVERTER.convertToFloat(v) : null;
        } catch (RuntimeException ignore) {
            return null;
        }
    }

    @Override
    public float getFloat(CharSequence name, float defaultValue) {
        Float v = getFloat(name);
        return v != null ? v : defaultValue;
    }

    @Override
    public Double getDouble(CharSequence name) {
        String v = get(name);
        try {
            return v != null ? VALUE_CONVERTER.convertToDouble(v) : null;
        } catch (RuntimeException ignore) {
            return null;
        }
    }

    @Override
    public double getDouble(CharSequence name, double defaultValue) {
        Double v = getDouble(name);
        return v != null ? v : defaultValue;
    }

    @Override
    public boolean contains(String name) {
        return underlying.contains(name);
    }

    @Override
    public boolean contains(CharSequence name) {
        return underlying.contains(name);
    }

    @Override
    public boolean contains(String name, String value) {
        return underlying.contains(name, value, false);
    }

    @Override
    public boolean contains(CharSequence name, CharSequence value) {
        return underlying.contains(name, value, false);
    }

    @Override
    public boolean contains(String name, String value, boolean ignoreCase) {
        return underlying.contains(name, value, ignoreCase);
    }

    @Override
    public boolean contains(CharSequence name, CharSequence value, boolean ignoreCase) {
        return underlying.contains(name, value, ignoreCase);
    }

    @Override
    public int size() {
        return underlying.size();
    }

    @Override
    public boolean isEmpty() {
        return underlying.isEmpty();
    }

    @Override
    public Set<String> names() {
        return underlying.names();
    }

    @Override
    public Http1HeadersAdaptor add(String name, Object value) {
        underlying.add(name, value);
        return this;
    }

    @Override
    public Http1HeadersAdaptor add(CharSequence name, Object value) {
        underlying.add(name, value);
        return this;
    }

    @Override
    public Http1HeadersAdaptor add(String name, Iterable<?> values) {
        underlying.add(name, values);
        return this;
    }

    @Override
    public Http1HeadersAdaptor add(CharSequence name, Iterable<?> values) {
        underlying.add(name, values);
        return this;
    }

    @Override
    public Http1HeadersAdaptor addBoolean(CharSequence name, boolean value) {
        underlying.add(name, VALUE_CONVERTER.convertBoolean(value));
        return this;
    }

    @Override
    public Http1HeadersAdaptor addByte(CharSequence name, byte value) {
        underlying.add(name, VALUE_CONVERTER.convertByte(value));
        return this;
    }

    @Override
    public Http1HeadersAdaptor addChar(CharSequence name, char value) {
        underlying.add(name, VALUE_CONVERTER.convertChar(value));
        return this;
    }

    @Override
    public Http1HeadersAdaptor addShort(CharSequence name, short value) {
        underlying.addShort(name, value);
        return this;
    }

    @Override
    public Http1HeadersAdaptor addInt(CharSequence name, int value) {
        underlying.addInt(name, value);
        return this;
    }

    @Override
    public Http1HeadersAdaptor addLong(CharSequence name, long value) {
        underlying.add(name, VALUE_CONVERTER.convertLong(value));
        return this;
    }

    @Override
    public Http1HeadersAdaptor addFloat(CharSequence name, float value) {
        underlying.add(name, VALUE_CONVERTER.convertFloat(value));
        return this;
    }

    @Override
    public Http1HeadersAdaptor addDouble(CharSequence name, double value) {
        underlying.add(name, VALUE_CONVERTER.convertDouble(value));
        return this;
    }

    @Override
    public Http1HeadersAdaptor add(HttpHeaders headers) {
        Checks.checkNotNull(headers, "underlying");
        if (headers == this) {
            throw new IllegalArgumentException("can't add to itself.");
        }
        if (headers instanceof io.netty.handler.codec.http.HttpHeaders) {
            this.underlying.add((io.netty.handler.codec.http.HttpHeaders) headers);
        } else {
            for (Map.Entry<String, String> e : headers) {
                add(e.getKey(), e.getValue());
            }
        }
        return this;
    }

    @Override
    public Http1HeadersAdaptor set(String name, Object value) {
        underlying.set(name, value);
        return this;
    }

    @Override
    public Http1HeadersAdaptor set(CharSequence name, Object value) {
        underlying.set(name, value);
        return this;
    }

    @Override
    public Http1HeadersAdaptor set(String name, Iterable<?> values) {
        underlying.set(name, values);
        return this;
    }

    @Override
    public Http1HeadersAdaptor set(CharSequence name, Iterable<?> values) {
        underlying.set(name, values);
        return this;
    }

    @Override
    public Http1HeadersAdaptor setBoolean(CharSequence name, boolean value) {
        underlying.set(name, VALUE_CONVERTER.convertBoolean(value));
        return this;
    }

    @Override
    public Http1HeadersAdaptor setByte(CharSequence name, byte value) {
        underlying.set(name, VALUE_CONVERTER.convertByte(value));
        return this;
    }

    @Override
    public Http1HeadersAdaptor setChar(CharSequence name, char value) {
        underlying.set(name, VALUE_CONVERTER.convertChar(value));
        return this;
    }

    @Override
    public Http1HeadersAdaptor setShort(CharSequence name, short value) {
        underlying.setShort(name, value);
        return this;
    }

    @Override
    public Http1HeadersAdaptor setInt(CharSequence name, int value) {
        underlying.setInt(name, value);
        return this;
    }

    @Override
    public Http1HeadersAdaptor setLong(CharSequence name, long value) {
        underlying.set(name, VALUE_CONVERTER.convertLong(value));
        return this;
    }

    @Override
    public Http1HeadersAdaptor setFloat(CharSequence name, float value) {
        underlying.set(name, VALUE_CONVERTER.convertFloat(value));
        return this;
    }

    @Override
    public Http1HeadersAdaptor setDouble(CharSequence name, double value) {
        underlying.set(name, VALUE_CONVERTER.convertDouble(value));
        return this;
    }

    @Override
    public Http1HeadersAdaptor set(HttpHeaders headers) {
        Checks.checkNotNull(headers, "headers");
        if (headers == this) {
            return this;
        }

        if (headers instanceof io.netty.handler.codec.http.HttpHeaders) {
            this.underlying.set((io.netty.handler.codec.http.HttpHeaders) headers);
        } else {
            clear();
            for (Map.Entry<String, String> entry : headers) {
                add(entry.getKey(), entry.getValue());
            }
        }
        return this;
    }

    @Override
    public Http1HeadersAdaptor setAll(HttpHeaders headers) {
        Checks.checkNotNull(headers, "headers");
        if (headers.isEmpty()) {
            return this;
        }

        if (headers instanceof io.netty.handler.codec.http.HttpHeaders) {
            this.underlying.setAll((io.netty.handler.codec.http.HttpHeaders) headers);
        } else {
            for (Map.Entry<String, String> entry : headers) {
                set(entry.getKey(), entry.getValue());
            }
        }
        return this;
    }

    @Override
    public Http1HeadersAdaptor remove(String name) {
        underlying.remove(name);
        return this;
    }

    @Override
    public Http1HeadersAdaptor remove(CharSequence name) {
        underlying.remove(name);
        return this;
    }

    @Override
    public Http1HeadersAdaptor clear() {
        underlying.clear();
        return this;
    }

    @Override
    public Iterator<Map.Entry<CharSequence, CharSequence>> iteratorCharSequence() {
        return underlying.iteratorCharSequence();
    }

    @Override
    public Iterator<Map.Entry<String, String>> iterator() {
        return underlying.iterator();
    }

    @Override
    public String toString() {
        return HeadersUtils.toString(getClass(), iteratorCharSequence(), size());
    }
}
