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

import static esa.commons.ExceptionUtils.asRuntime;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ExceptionUtilsTest {

    @Test
    void testAsRuntime() {
        assertNull(asRuntime(null));
        final RuntimeException r = new RuntimeException();
        assertEquals(r, asRuntime(r));
        final Exception e = new Exception();
        assertEquals(RuntimeException.class, asRuntime(e).getClass());
        assertEquals(e, asRuntime(e).getCause());
    }

    @Test
    void testThrowAsUnchecked() {
        assertThrows(Exception.class, () -> ExceptionUtils.throwException(new Exception()));
        assertThrows(RuntimeException.class, () -> ExceptionUtils.throwException(new RuntimeException()));
    }

}
