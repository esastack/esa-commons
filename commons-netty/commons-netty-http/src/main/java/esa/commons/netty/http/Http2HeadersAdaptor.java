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
import io.netty.handler.codec.http2.DefaultHttp2Headers;
import io.netty.handler.codec.http2.Http2Headers;

import java.util.AbstractList;
import java.util.AbstractSet;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Using a instance of {@link Http2Headers} as a proxy.
 * @deprecated use https://github.com/esastack/esa-commons-net instead
 */
@Deprecated
public class Http2HeadersAdaptor implements HttpHeaders {

    protected final Http2Headers underlying;

    public Http2HeadersAdaptor() {
        this(new DefaultHttp2Headers());
    }

    public Http2HeadersAdaptor(boolean validate) {
        this(new DefaultHttp2Headers(validate));
    }

    public Http2HeadersAdaptor(boolean validate, int arraySizeHint) {
        this(new DefaultHttp2Headers(validate, arraySizeHint));
    }

    public Http2HeadersAdaptor(Http2Headers headers) {
        this.underlying = headers;
    }

    @Override
    public String get(String name) {
        return HeadersUtils.getAsString(underlying, name);
    }

    @Override
    public String get(CharSequence name) {
        return HeadersUtils.getAsString(underlying, name);
    }

    @Override
    public String get(CharSequence name, String defaultValue) {
        CharSequence v = underlying.get(name);
        return v == null ? defaultValue : v.toString();
    }

    @Override
    public List<String> getAll(String name) {
        return getAll((CharSequence) name);
    }

    @Override
    public List<String> getAll(CharSequence name) {
        final List<CharSequence> all = underlying.getAll(name);
        if (all == null) {
            return null;
        }
        return new AbstractList<String>() {
            @Override
            public String get(int index) {
                CharSequence v = all.get(index);
                return v == null ? null : v.toString();
            }

            @Override
            public int size() {
                return all.size();
            }
        };
    }

    @Override
    public Boolean getBoolean(CharSequence name) {
        return underlying.getBoolean(name);
    }

    @Override
    public boolean getBoolean(CharSequence name, boolean defaultValue) {
        return underlying.getBoolean(name, defaultValue);
    }

    @Override
    public Byte getByte(CharSequence name) {
        return underlying.getByte(name);
    }

    @Override
    public byte getByte(CharSequence name, byte defaultValue) {
        return underlying.getByte(name, defaultValue);
    }

    @Override
    public Character getChar(CharSequence name) {
        return underlying.getChar(name);
    }

    @Override
    public char getChar(CharSequence name, char defaultValue) {
        return underlying.getChar(name, defaultValue);
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
        return underlying.getLong(name);
    }

    @Override
    public long getLong(CharSequence name, long defaultValue) {
        return underlying.getLong(name, defaultValue);
    }

    @Override
    public Float getFloat(CharSequence name) {
        return underlying.getFloat(name);
    }

    @Override
    public float getFloat(CharSequence name, float defaultValue) {
        return underlying.getFloat(name, defaultValue);
    }

    @Override
    public Double getDouble(CharSequence name) {
        return underlying.getDouble(name);
    }

    @Override
    public double getDouble(CharSequence name, double defaultValue) {
        return underlying.getDouble(name, defaultValue);
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
        return underlying.contains(name, value);
    }

    @Override
    public boolean contains(CharSequence name, CharSequence value) {
        return underlying.contains(name, value);
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
        final Set<CharSequence> names = underlying.names();
        if (names.isEmpty()) {
            return Collections.emptySet();
        }
        return new AbstractSet<String>() {
            @Override
            public Iterator<String> iterator() {
                Iterator<CharSequence> it = names.iterator();

                return new Iterator<String>() {
                    @Override
                    public boolean hasNext() {
                        return it.hasNext();
                    }

                    @Override
                    public String next() {
                        return it.next().toString();
                    }
                };
            }

            @Override
            public int size() {
                return names.size();
            }
        };
    }

    @Override
    public HttpHeaders add(String name, Object value) {
        underlying.addObject(name, value);
        return this;
    }

    @Override
    public HttpHeaders add(CharSequence name, Object value) {
        underlying.addObject(name, value);
        return this;
    }

    @Override
    public HttpHeaders add(String name, Iterable<?> values) {
        underlying.addObject(name, values);
        return this;
    }

    @Override
    public HttpHeaders add(CharSequence name, Iterable<?> values) {
        underlying.addObject(name, values);
        return this;
    }

    @Override
    public HttpHeaders addBoolean(CharSequence name, boolean value) {
        underlying.addBoolean(name, value);
        return this;
    }

    @Override
    public HttpHeaders addByte(CharSequence name, byte value) {
        underlying.addByte(name, value);
        return this;
    }

    @Override
    public HttpHeaders addChar(CharSequence name, char value) {
        underlying.addChar(name, value);
        return this;
    }

    @Override
    public HttpHeaders addShort(CharSequence name, short value) {
        underlying.addShort(name, value);
        return this;
    }

    @Override
    public HttpHeaders addInt(CharSequence name, int value) {
        underlying.addInt(name, value);
        return this;
    }

    @Override
    public HttpHeaders addLong(CharSequence name, long value) {
        underlying.addLong(name, value);
        return this;
    }

    @Override
    public HttpHeaders addFloat(CharSequence name, float value) {
        underlying.addFloat(name, value);
        return this;
    }

    @Override
    public HttpHeaders addDouble(CharSequence name, double value) {
        underlying.addDouble(name, value);
        return this;
    }

    @Override
    public HttpHeaders add(HttpHeaders headers) {
        Checks.checkNotNull(headers);
        if (headers == this) {
            throw new IllegalArgumentException("can't add to itself.");
        }

        for (Map.Entry<String, String> e : headers) {
            add(e.getKey(), e.getValue());
        }
        return this;
    }

    @Override
    public HttpHeaders set(String name, Object value) {
        underlying.setObject(name, value);
        return this;
    }

    @Override
    public HttpHeaders set(CharSequence name, Object value) {
        underlying.setObject(name, value);
        return this;
    }

    @Override
    public HttpHeaders set(String name, Iterable<?> values) {
        underlying.setObject(name, values);
        return this;
    }

    @Override
    public HttpHeaders set(CharSequence name, Iterable<?> values) {
        underlying.setObject(name, values);
        return this;
    }

    @Override
    public HttpHeaders setBoolean(CharSequence name, boolean value) {
        underlying.setBoolean(name, value);
        return this;
    }

    @Override
    public HttpHeaders setByte(CharSequence name, byte value) {
        underlying.setByte(name, value);
        return this;
    }

    @Override
    public HttpHeaders setChar(CharSequence name, char value) {
        underlying.setChar(name, value);
        return this;
    }

    @Override
    public HttpHeaders setShort(CharSequence name, short value) {
        underlying.setShort(name, value);
        return this;
    }

    @Override
    public HttpHeaders setInt(CharSequence name, int value) {
        underlying.setInt(name, value);
        return this;
    }

    @Override
    public HttpHeaders setLong(CharSequence name, long value) {
        underlying.setLong(name, value);
        return this;
    }

    @Override
    public HttpHeaders setFloat(CharSequence name, float value) {
        underlying.setFloat(name, value);
        return this;
    }

    @Override
    public HttpHeaders setDouble(CharSequence name, double value) {
        underlying.setDouble(name, value);
        return this;
    }

    @Override
    public HttpHeaders set(HttpHeaders headers) {
        Checks.checkNotNull(headers, "headers");

        if (headers != this) {
            clear();

            for (Map.Entry<String, String> entry : headers) {
                add(entry.getKey(), entry.getValue());
            }
        }
        return this;
    }

    @Override
    public HttpHeaders setAll(HttpHeaders headers) {
        Checks.checkNotNull(headers, "headers");

        if (headers.isEmpty()) {
            return this;
        }

        for (Map.Entry<String, String> entry : headers) {
            set(entry.getKey(), entry.getValue());
        }
        return this;
    }

    @Override
    public HttpHeaders remove(String name) {
        underlying.remove(name);
        return this;
    }

    @Override
    public HttpHeaders remove(CharSequence name) {
        underlying.remove(name);
        return this;
    }

    @Override
    public HttpHeaders clear() {
        underlying.clear();
        return this;
    }

    @Override
    public Iterator<Map.Entry<CharSequence, CharSequence>> iteratorCharSequence() {
        return underlying.iterator();
    }

    @Override
    public Iterator<Map.Entry<String, String>> iterator() {
        Iterator<Map.Entry<CharSequence, CharSequence>> it = underlying.iterator();
        return new Iterator<Map.Entry<String, String>>() {
            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public Map.Entry<String, String> next() {
                Map.Entry<CharSequence, CharSequence> n = it.next();
                return new Map.Entry<String, String>() {
                    @Override
                    public String getKey() {
                        return n.getKey().toString();
                    }

                    @Override
                    public String getValue() {
                        CharSequence v = n.getValue();
                        return v == null ? null : v.toString();
                    }

                    @Override
                    public String setValue(String value) {
                        CharSequence v = n.setValue(value);
                        return v == null ? null : v.toString();
                    }
                };
            }
        };
    }

    @Override
    public String toString() {
        return HeadersUtils.toString(getClass(), iteratorCharSequence(), size());
    }
}
