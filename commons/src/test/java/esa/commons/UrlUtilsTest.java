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

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UrlUtilsTest {

    @Test
    void testPrependLeadingSlash() {
        assertEquals("/foo", UrlUtils.prependLeadingSlash("foo"));
        assertEquals("/foo", UrlUtils.prependLeadingSlash("/foo"));
        assertEquals("", UrlUtils.prependLeadingSlash(""));

        assertArrayEquals(new String[]{"/foo", "/bar"},
                UrlUtils.prependLeadingSlash(Arrays.asList("foo", "/bar")).toArray());

        final String[] arr = new String[]{"foo", "/bar"};
        UrlUtils.prependLeadingSlash(arr);
        assertArrayEquals(new String[]{"/foo", "/bar"}, arr);
    }

}
