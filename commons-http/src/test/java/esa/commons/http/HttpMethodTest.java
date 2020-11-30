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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * HttpMethodTest
 */
class HttpMethodTest {

    @Test
    void testFastValueOf() {
        assertSame(HttpMethod.GET, HttpMethod.fastValueOf("GET"));
        assertSame(HttpMethod.HEAD, HttpMethod.fastValueOf("HEAD"));
        assertSame(HttpMethod.POST, HttpMethod.fastValueOf("POST"));
        assertSame(HttpMethod.PUT, HttpMethod.fastValueOf("PUT"));
        assertSame(HttpMethod.PATCH, HttpMethod.fastValueOf("PATCH"));
        assertSame(HttpMethod.DELETE, HttpMethod.fastValueOf("DELETE"));
        assertSame(HttpMethod.OPTIONS, HttpMethod.fastValueOf("OPTIONS"));
        assertSame(HttpMethod.TRACE, HttpMethod.fastValueOf("TRACE"));
        assertSame(HttpMethod.CONNECT, HttpMethod.fastValueOf("CONNECT"));

        assertNull(HttpMethod.fastValueOf("get"));
        assertNull(HttpMethod.fastValueOf("head"));
        assertNull(HttpMethod.fastValueOf("post"));
        assertNull(HttpMethod.fastValueOf("put"));
        assertNull(HttpMethod.fastValueOf("patch"));
        assertNull(HttpMethod.fastValueOf("delete"));
        assertNull(HttpMethod.fastValueOf("options"));
        assertNull(HttpMethod.fastValueOf("trace"));
        assertNull(HttpMethod.fastValueOf("connect"));

        assertNull(HttpMethod.fastValueOf("1"));
    }

}
