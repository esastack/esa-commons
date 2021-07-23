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
import io.netty.handler.codec.DefaultHeaders;
import io.netty.handler.codec.http.DefaultHttpHeaders;

import java.util.Map;

import static esa.commons.netty.http.Utils.VALUE_CONVERTER;

/**
 * Implementing {@link HttpHeaders} by extending {@link DefaultHttpHeaders}.
 * @deprecated use https://github.com/esastack/esa-commons-net instead
 */
@Deprecated
public class Http1HeadersImpl extends DefaultHttpHeaders implements HttpHeaders {

    public Http1HeadersImpl() {
    }

    public Http1HeadersImpl(boolean validate) {
        super(validate);
    }

    protected Http1HeadersImpl(boolean validate, DefaultHeaders.NameValidator<CharSequence> nameValidator) {
        super(validate, nameValidator);
    }

    protected Http1HeadersImpl(DefaultHeaders<CharSequence, CharSequence, ?> headers) {
        super(headers);
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
    public boolean contains(String name, String value) {
        return super.contains(name, value, false);
    }

    @Override
    public boolean contains(CharSequence name, CharSequence value) {
        return super.contains(name, value, false);
    }

    @Override
    public Http1HeadersImpl add(String name, Object value) {
        super.add(name, value);
        return this;
    }

    @Override
    public Http1HeadersImpl add(CharSequence name, Object value) {
        super.add(name, value);
        return this;
    }

    @Override
    public Http1HeadersImpl add(String name, Iterable<?> values) {
        super.add(name, values);
        return this;
    }

    @Override
    public Http1HeadersImpl add(CharSequence name, Iterable<?> values) {
        super.add(name, values);
        return this;
    }

    @Override
    public Http1HeadersImpl addBoolean(CharSequence name, boolean value) {
        super.add(name, VALUE_CONVERTER.convertBoolean(value));
        return this;
    }

    @Override
    public Http1HeadersImpl addByte(CharSequence name, byte value) {
        super.add(name, VALUE_CONVERTER.convertByte(value));
        return this;
    }

    @Override
    public Http1HeadersImpl addChar(CharSequence name, char value) {
        super.add(name, VALUE_CONVERTER.convertChar(value));
        return this;
    }

    @Override
    public Http1HeadersImpl addShort(CharSequence name, short value) {
        super.addShort(name, value);
        return this;
    }

    @Override
    public Http1HeadersImpl addInt(CharSequence name, int value) {
        super.addInt(name, value);
        return this;
    }

    @Override
    public Http1HeadersImpl addLong(CharSequence name, long value) {
        super.add(name, VALUE_CONVERTER.convertLong(value));
        return this;
    }

    @Override
    public Http1HeadersImpl addFloat(CharSequence name, float value) {
        super.add(name, VALUE_CONVERTER.convertFloat(value));
        return this;
    }

    @Override
    public Http1HeadersImpl addDouble(CharSequence name, double value) {
        super.add(name, VALUE_CONVERTER.convertDouble(value));
        return this;
    }

    @Override
    public Http1HeadersImpl add(HttpHeaders headers) {
        Checks.checkNotNull(headers, "headers");
        if (headers == this) {
            throw new IllegalArgumentException("can't add to itself.");
        }
        if (headers instanceof Http1HeadersImpl) {
            super.add((Http1HeadersImpl) headers);
        } else {
            for (Map.Entry<String, String> e : headers) {
                add(e.getKey(), e.getValue());
            }
        }
        return this;
    }

    @Override
    public Http1HeadersImpl add(io.netty.handler.codec.http.HttpHeaders headers) {
        super.add(headers);
        return this;
    }

    @Override
    public Http1HeadersImpl set(String name, Object value) {
        super.set(name, value);
        return this;
    }

    @Override
    public Http1HeadersImpl set(CharSequence name, Object value) {
        super.set(name, value);
        return this;
    }

    @Override
    public Http1HeadersImpl set(String name, Iterable<?> values) {
        super.set(name, values);
        return this;
    }

    @Override
    public Http1HeadersImpl set(CharSequence name, Iterable<?> values) {
        super.set(name, values);
        return this;
    }

    @Override
    public Http1HeadersImpl setBoolean(CharSequence name, boolean value) {
        set(name, VALUE_CONVERTER.convertBoolean(value));
        return this;
    }

    @Override
    public Http1HeadersImpl setByte(CharSequence name, byte value) {
        set(name, VALUE_CONVERTER.convertByte(value));
        return this;
    }

    @Override
    public Http1HeadersImpl setChar(CharSequence name, char value) {
        set(name, VALUE_CONVERTER.convertChar(value));
        return this;
    }

    @Override
    public Http1HeadersImpl setShort(CharSequence name, short value) {
        super.setShort(name, value);
        return this;
    }

    @Override
    public Http1HeadersImpl setInt(CharSequence name, int value) {
        super.setInt(name, value);
        return this;
    }

    @Override
    public Http1HeadersImpl setLong(CharSequence name, long value) {
        set(name, VALUE_CONVERTER.convertLong(value));
        return this;
    }

    @Override
    public Http1HeadersImpl setFloat(CharSequence name, float value) {
        set(name, VALUE_CONVERTER.convertFloat(value));
        return this;
    }

    @Override
    public Http1HeadersImpl setDouble(CharSequence name, double value) {
        set(name, VALUE_CONVERTER.convertDouble(value));
        return this;
    }

    @Override
    public Http1HeadersImpl set(HttpHeaders headers) {
        Checks.checkNotNull(headers, "headers");
        if (headers == this) {
            return this;
        }

        if (headers instanceof Http1HeadersImpl) {
            super.set((Http1HeadersImpl) headers);
        } else {
            clear();

            for (Map.Entry<String, String> entry : headers) {
                add(entry.getKey(), entry.getValue());
            }
        }
        return this;
    }

    @Override
    public Http1HeadersImpl setAll(HttpHeaders headers) {
        Checks.checkNotNull(headers, "headers");

        if (headers.isEmpty()) {
            return this;
        }

        if (headers instanceof Http1HeadersImpl) {
            super.setAll((Http1HeadersImpl) headers);
        } else {
            for (Map.Entry<String, String> entry : headers) {
                set(entry.getKey(), entry.getValue());
            }
        }
        return this;
    }

    @Override
    public Http1HeadersImpl set(io.netty.handler.codec.http.HttpHeaders headers) {
        super.set(headers);
        return this;
    }

    @Override
    public Http1HeadersImpl remove(String name) {
        super.remove(name);
        return this;
    }

    @Override
    public Http1HeadersImpl remove(CharSequence name) {
        super.remove(name);
        return this;
    }

    @Override
    public Http1HeadersImpl clear() {
        super.clear();
        return this;
    }

    @Override
    public Http1HeadersImpl copy() {
        return new Http1HeadersImpl().set((HttpHeaders) this);
    }
}
