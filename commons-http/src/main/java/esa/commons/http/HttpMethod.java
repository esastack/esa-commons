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

import esa.commons.Checks;
import esa.commons.MathUtils;
import esa.commons.StringUtils;

/**
 * Method of the HTTP protocol.
 */
public enum HttpMethod {

    /**
     * Methods
     */
    GET,
    HEAD,
    POST,
    PUT,
    PATCH,
    DELETE,
    OPTIONS,
    TRACE,
    CONNECT;

    private static final HttpMethod[] METHODS
            = new HttpMethod[MathUtils.nextPowerOfTwo(HttpMethod.values().length)];

    private static final int MASK = METHODS.length - 1;

    static {
        for (HttpMethod method : HttpMethod.values()) {
            int index = hash(method.name()) & MASK;
            Checks.checkState(index >= 0 && index < METHODS.length && METHODS[index] == null,
                    "Unexpected hash");
            METHODS[index] = method;
        }
    }

    /**
     * Matches {@link HttpMethod}.
     * <p>
     * !Note: uppercase name only.
     *
     * @param name method name
     *
     * @return {@link HttpMethod} matched or null
     */
    public static HttpMethod fastValueOf(String name) {
        if (StringUtils.isEmpty(name)) {
            return null;
        }

        HttpMethod value = METHODS[hash(name) & MASK];
        if (value != null && value.name().equals(name)) {
            return value;
        }
        return null;
    }

    private static int hash(String name) {
        // This hash code needs to produce a unique index in the "values" array for each HttpMethod. If new
        // HttpMethods are added this algorithm will need to be adjusted.
        // For example with the current set of HttpMethods it just so happens that the String hash code value
        // shifted right by 6 bits modulo 16 is unique relative to all other HttpMethod values.
        return name.hashCode() >>> 6;
    }
}
