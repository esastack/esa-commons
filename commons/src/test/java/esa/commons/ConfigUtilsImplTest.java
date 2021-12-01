/*
 * Copyright 2021 OPPO ESA Stack Project
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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ConfigUtilsImplTest {

    @Test
    void testConstructor() {
        assertNotNull(new ConfigUtilsImpl(k -> k));
        assertThrows(NullPointerException.class, () -> new ConfigUtilsImpl(null));
    }

    @Test
    void testGetStr() {
        assertEquals("foo", new ConfigUtilsImpl(k -> k).getStr("foo"));
        assertThrows(IllegalArgumentException.class, () -> new ConfigUtilsImpl(k -> k).getStr(null));
        assertThrows(IllegalArgumentException.class, () -> new ConfigUtilsImpl(k -> k).getStr(""));
    }

}
