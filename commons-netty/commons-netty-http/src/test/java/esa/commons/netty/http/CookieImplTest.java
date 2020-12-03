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

import esa.commons.http.Cookie;
import io.netty.handler.codec.http.cookie.ClientCookieEncoder;
import io.netty.handler.codec.http.cookie.ServerCookieEncoder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * CookieImplTest
 */
class CookieImplTest {

    @Test
    void testDelegate() {
        assertThrows(NullPointerException.class, () -> new CookieImpl(null));
        final CookieImpl cookie = new CookieImpl("a", "1");
        assertEquals("a", cookie.name());
        assertEquals("1", cookie.value());
        assertFalse(cookie.wrap());
        assertNull(cookie.domain());
        assertNull(cookie.path());
        assertEquals(Cookie.UNDEFINED_MAX_AGE, cookie.maxAge());
        assertFalse(cookie.isSecure());
        assertFalse(cookie.isHttpOnly());
        assertNotNull(cookie.cookie);

        cookie.setWrap(true);
        assertTrue(cookie.wrap());
        cookie.setDomain("d");
        assertEquals("d", cookie.domain());
        cookie.setPath("p");
        assertEquals("p", cookie.path());
        cookie.setMaxAge(1L);
        assertEquals(1, cookie.maxAge());
        cookie.setSecure(true);
        assertTrue(cookie.isSecure());
        cookie.setHttpOnly(true);
        assertTrue(cookie.isHttpOnly());
    }

    @Test
    void testEncode() {
        final CookieImpl cookie = new CookieImpl("a", "1");
        cookie.setWrap(true);
        cookie.setDomain("d");
        cookie.setPath("p");
        cookie.setSecure(true);
        cookie.setHttpOnly(true);

        assertEquals(ServerCookieEncoder.STRICT.encode(cookie.cookie), cookie.encode(true));
        assertEquals(ClientCookieEncoder.STRICT.encode(cookie.cookie), cookie.encode(false));
    }

}
