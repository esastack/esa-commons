/*
 * Copyright 2022 OPPO ESA Stack Project
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

import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ResultTest {

    @Test
    void testNullOk() {
        final Result<Integer, String> r = Result.ok();
        assertTrue(r.isOk());
        assertFalse(r.isErr());
        final AtomicReference<Integer> ifOk = new AtomicReference<>(0);
        r.ifOk(ifOk::set);
        assertNull(ifOk.get());

        final AtomicReference<String> ifErr = new AtomicReference<>("");
        r.ifError(ifErr::set);
        assertEquals("", ifErr.get());

        assertNull(r.get());
        assertNull(r.get("foo"));
        assertNull(r.getOrElse(1));

        assertThrows(NoSuchElementException.class, r::getErr);
        assertThrows(NoSuchElementException.class, () -> r.getErr("foo"));
        assertEquals("foo", r.getErrOrElse("foo"));

        assertSame(Result.ok(), r.map(v -> null));
        assertEquals(1, r.map(v -> 1).get());

        assertSame(r, r.mapErr(v -> null));
        assertSame(r, r.mapErr(v -> ""));

        assertTrue(r.contains(null));
        assertFalse(r.contains(1));

        assertFalse(r.containsErr(null));
        assertFalse(r.containsErr(""));

        assertEquals(r, Result.ok());
        assertEquals(r.hashCode(), Result.ok().hashCode());
        assertEquals("Result::Ok[null]", r.toString());
    }

    @Test
    void testNullErr() {
        assertSame(Result.ok(), Result.ok(null));
        final Result<Integer, String> r = Result.err();
        assertFalse(r.isOk());
        assertTrue(r.isErr());
        final AtomicReference<Integer> ifOk = new AtomicReference<>(0);
        r.ifOk(ifOk::set);
        assertEquals(0, ifOk.get());

        final AtomicReference<String> ifErr = new AtomicReference<>("");
        r.ifError(ifErr::set);
        assertNull(ifErr.get());

        assertThrows(NoSuchElementException.class, r::get);
        assertThrows(NoSuchElementException.class, () -> r.get("foo"));
        assertEquals(1, r.getOrElse(1));

        assertNull(r.getErr());
        assertNull(r.getErr("foo"));
        assertNull(r.getErrOrElse("foo"));

        assertSame(r, r.map(v -> null));
        assertSame(r, r.map(v -> 1));

        assertSame(Result.err(), r.mapErr(v -> null));
        assertEquals("foo", r.mapErr(v -> "foo").getErr());

        assertFalse(r.contains(null));
        assertFalse(r.contains(1));

        assertTrue(r.containsErr(null));
        assertFalse(r.containsErr(""));

        assertEquals(r, Result.err());
        assertEquals(r.hashCode(), Result.err().hashCode());
        assertEquals("Result::Err[null]", r.toString());
    }

    @Test
    void testOk() {
        assertSame(Result.err(), Result.err(null));
        final Result<Integer, String> r = Result.ok(10);
        assertTrue(r.isOk());
        assertFalse(r.isErr());
        final AtomicReference<Integer> ifOk = new AtomicReference<>(0);
        r.ifOk(ifOk::set);
        assertEquals(10, ifOk.get());

        final AtomicReference<String> ifErr = new AtomicReference<>("");
        r.ifError(ifErr::set);
        assertEquals("", ifErr.get());

        assertEquals(10, r.get());
        assertEquals(10, r.get("foo"));
        assertEquals(10, r.getOrElse(1));

        assertThrows(NoSuchElementException.class, r::getErr);
        assertThrows(NoSuchElementException.class, () -> r.getErr("foo"));
        assertEquals("foo", r.getErrOrElse("foo"));

        assertSame(Result.ok(), r.map(v -> null));
        assertEquals(11, r.map(v -> v + 1).get());

        assertSame(r, r.mapErr(v -> null));
        assertSame(r, r.mapErr(v -> ""));

        assertFalse(r.contains(null));
        assertFalse(r.contains(1));
        assertTrue(r.contains(10));

        assertFalse(r.containsErr(null));
        assertFalse(r.containsErr(""));

        assertEquals(r, r);
        assertNotEquals(null, r);
        assertNotEquals(new Object(), r);
        assertEquals(Result.ok(10), r);
        assertEquals(r.hashCode(), Result.ok(10).hashCode());
        assertEquals("Result::Ok[10]", r.toString());
    }

    @Test
    void testErr() {
        final Result<Integer, String> r = Result.err("yoo");
        assertFalse(r.isOk());
        assertTrue(r.isErr());
        final AtomicReference<Integer> ifOk = new AtomicReference<>(0);
        r.ifOk(ifOk::set);
        assertEquals(0, ifOk.get());

        final AtomicReference<String> ifErr = new AtomicReference<>("");
        r.ifError(ifErr::set);
        assertEquals("yoo", ifErr.get());

        assertThrows(NoSuchElementException.class, r::get);
        assertThrows(NoSuchElementException.class, () -> r.get("foo"));
        assertEquals(1, r.getOrElse(1));

        assertEquals("yoo", r.getErr());
        assertEquals("yoo", r.getErr("foo"));
        assertEquals("yoo", r.getErrOrElse("foo"));

        assertSame(r, r.map(v -> null));
        assertSame(r, r.map(v -> 1));

        assertSame(Result.err(), r.mapErr(v -> null));
        assertEquals("foo", r.mapErr(v -> "foo").getErr());

        assertFalse(r.contains(null));
        assertFalse(r.contains(1));

        assertFalse(r.containsErr(null));
        assertFalse(r.containsErr(""));
        assertTrue(r.containsErr("yoo"));

        assertEquals(r, r);
        assertNotEquals(null, r);
        assertNotEquals(new Object(), r);
        assertEquals(r, Result.err("yoo"));
        assertEquals(r.hashCode(), Result.err("yoo").hashCode());
        assertEquals("Result::Err[yoo]", r.toString());
    }

}
