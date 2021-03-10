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
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ChecksTest {

    @Test
    void testCheckNotNull() {
        final Object o = new Object();
        assertSame(o, Checks.checkNotNull(o));
        assertThrows(NullPointerException.class, () -> Checks.checkNotNull(null));

        assertSame(o, Checks.checkNotNull(o, "foo"));
        assertEquals("o",
                assertThrows(NullPointerException.class,
                        () -> Checks.checkNotNull(null, "o")).getMessage());
        assertEquals("",
                assertThrows(NullPointerException.class,
                        () -> Checks.checkNotNull(null, null)).getMessage());

        assertSame(o, Checks.checkNotNull(o, "foo %s", "bar"));
        assertEquals("foo bar",
                assertThrows(NullPointerException.class,
                        () -> Checks.checkNotNull(null, "foo %s", "bar")).getMessage());
    }

    @Test
    void testCheckArg() {
        Checks.checkArg(true);
        Checks.checkArg(true, "o");
        Checks.checkArg(true, "foo %s", "bar");
        assertThrows(IllegalArgumentException.class, () -> Checks.checkArg(false));
        assertEquals("o",
                assertThrows(IllegalArgumentException.class,
                        () -> Checks.checkArg(false, "o")).getMessage());
        assertEquals("foo bar",
                assertThrows(IllegalArgumentException.class,
                        () -> Checks.checkArg(false, "foo %s", "bar")).getMessage());
    }

    @Test
    void testCheckState() {
        Checks.checkState(true);
        Checks.checkState(true, "o");
        Checks.checkState(true, "foo %s", "bar");
        assertThrows(IllegalStateException.class, () -> Checks.checkState(false));
        assertEquals("o",
                assertThrows(IllegalStateException.class,
                        () -> Checks.checkState(false, "o")).getMessage());
        assertEquals("foo bar",
                assertThrows(IllegalStateException.class,
                        () -> Checks.checkState(false, "foo %s", "bar")).getMessage());
    }

    @Test
    void testCheckNotEmptyArg() {
        final String str = "foo";
        assertSame(str, Checks.checkNotEmptyArg(str));
        assertSame(str, Checks.checkNotEmptyArg(str, "str"));
        assertSame(str, Checks.checkNotEmptyArg(str, "foo %s", "bar"));
        assertThrows(IllegalArgumentException.class, () -> Checks.checkNotEmptyArg(StringUtils.empty()));
        assertEquals("str",
                assertThrows(IllegalArgumentException.class,
                        () -> Checks.checkNotEmptyArg(StringUtils.empty(), "str")).getMessage());
        assertEquals("foo bar",
                assertThrows(IllegalArgumentException.class,
                        () -> Checks.checkNotEmptyArg(StringUtils.empty(), "foo %s", "bar"))
                        .getMessage());

        final List<String> list = Arrays.asList("f", "b");
        Checks.checkNotEmptyArg(list);
        Checks.checkNotEmptyArg(list, "list");
        Checks.checkNotEmptyArg(list, "foo %s", "bar");
        assertThrows(IllegalArgumentException.class, () -> Checks.checkNotEmptyArg((List<String>) null));
        assertThrows(IllegalArgumentException.class, () -> Checks.checkNotEmptyArg(Collections.emptyList()));
        assertEquals("list",
                assertThrows(IllegalArgumentException.class,
                        () -> Checks.checkNotEmptyArg((List<String>) null, "list")).getMessage());
        assertEquals("list",
                assertThrows(IllegalArgumentException.class,
                        () -> Checks.checkNotEmptyArg(Collections.emptyList(), "list")).getMessage());
        assertEquals("foo bar",
                assertThrows(IllegalArgumentException.class,
                        () -> Checks.checkNotEmptyArg((List<String>) null, "foo %s", "bar"))
                        .getMessage());
        assertEquals("foo bar",
                assertThrows(IllegalArgumentException.class,
                        () -> Checks.checkNotEmptyArg(Collections.emptyList(), "foo %s", "bar"))
                        .getMessage());

        final String[] refArr = new String[]{"f", "b"};
        assertSame(refArr, Checks.checkNotEmptyArg(refArr));
        assertSame(refArr, Checks.checkNotEmptyArg(refArr, "str"));
        assertSame(refArr, Checks.checkNotEmptyArg(refArr, "foo %s", "bar"));
        assertThrows(IllegalArgumentException.class, () -> Checks.checkNotEmptyArg((String[]) null));
        assertThrows(IllegalArgumentException.class, () -> Checks.checkNotEmptyArg(new String[0]));

        assertEquals("arr",
                assertThrows(IllegalArgumentException.class,
                        () -> Checks.checkNotEmptyArg((String[]) null, "arr")).getMessage());
        assertEquals("arr",
                assertThrows(IllegalArgumentException.class,
                        () -> Checks.checkNotEmptyArg(new String[0], "arr")).getMessage());
        assertEquals("foo bar",
                assertThrows(IllegalArgumentException.class,
                        () -> Checks.checkNotEmptyArg((String[]) null, "foo %s", "bar"))
                        .getMessage());
        assertEquals("foo bar",
                assertThrows(IllegalArgumentException.class,
                        () -> Checks.checkNotEmptyArg(new String[0], "foo %s", "bar"))
                        .getMessage());

        final long[] longArr = new long[]{0L, 1L};
        assertSame(longArr, Checks.checkNotEmptyArg(longArr));
        assertSame(longArr, Checks.checkNotEmptyArg(longArr, "arr"));
        assertSame(longArr, Checks.checkNotEmptyArg(longArr, "foo %s", "bar"));
        assertThrows(IllegalArgumentException.class, () -> Checks.checkNotEmptyArg((long[]) null));
        assertThrows(IllegalArgumentException.class, () -> Checks.checkNotEmptyArg(new long[0]));
        assertEquals("arr",
                assertThrows(IllegalArgumentException.class,
                        () -> Checks.checkNotEmptyArg((long[]) null, "arr")).getMessage());
        assertEquals("arr",
                assertThrows(IllegalArgumentException.class,
                        () -> Checks.checkNotEmptyArg(new long[0], "arr")).getMessage());
        assertEquals("foo bar",
                assertThrows(IllegalArgumentException.class,
                        () -> Checks.checkNotEmptyArg((long[]) null, "foo %s", "bar"))
                        .getMessage());
        assertEquals("foo bar",
                assertThrows(IllegalArgumentException.class,
                        () -> Checks.checkNotEmptyArg(new long[0], "foo %s", "bar"))
                        .getMessage());

        final double[] doubleArr = new double[]{0.0D, 1.0D};
        assertSame(doubleArr, Checks.checkNotEmptyArg(doubleArr));
        assertSame(doubleArr, Checks.checkNotEmptyArg(doubleArr, "arr"));
        assertSame(doubleArr, Checks.checkNotEmptyArg(doubleArr, "foo %s", "bar"));
        assertThrows(IllegalArgumentException.class, () -> Checks.checkNotEmptyArg((double[]) null));
        assertThrows(IllegalArgumentException.class, () -> Checks.checkNotEmptyArg(new double[0]));
        assertEquals("arr",
                assertThrows(IllegalArgumentException.class,
                        () -> Checks.checkNotEmptyArg((double[]) null, "arr")).getMessage());
        assertEquals("arr",
                assertThrows(IllegalArgumentException.class,
                        () -> Checks.checkNotEmptyArg(new double[0], "arr")).getMessage());
        assertEquals("foo bar",
                assertThrows(IllegalArgumentException.class,
                        () -> Checks.checkNotEmptyArg((double[]) null, "foo %s", "bar"))
                        .getMessage());
        assertEquals("foo bar",
                assertThrows(IllegalArgumentException.class,
                        () -> Checks.checkNotEmptyArg(new double[0], "foo %s", "bar"))
                        .getMessage());

        final int[] intArr = new int[]{0, 1};
        assertSame(intArr, Checks.checkNotEmptyArg(intArr));
        assertSame(intArr, Checks.checkNotEmptyArg(intArr, "arr"));
        assertSame(intArr, Checks.checkNotEmptyArg(intArr, "foo %s", "bar"));
        assertThrows(IllegalArgumentException.class, () -> Checks.checkNotEmptyArg((int[]) null));
        assertThrows(IllegalArgumentException.class, () -> Checks.checkNotEmptyArg(new int[0]));
        assertEquals("arr",
                assertThrows(IllegalArgumentException.class,
                        () -> Checks.checkNotEmptyArg((int[]) null, "arr")).getMessage());
        assertEquals("arr",
                assertThrows(IllegalArgumentException.class,
                        () -> Checks.checkNotEmptyArg(new int[0], "arr")).getMessage());
        assertEquals("foo bar",
                assertThrows(IllegalArgumentException.class,
                        () -> Checks.checkNotEmptyArg((int[]) null, "foo %s", "bar"))
                        .getMessage());
        assertEquals("foo bar",
                assertThrows(IllegalArgumentException.class,
                        () -> Checks.checkNotEmptyArg(new int[0], "foo %s", "bar"))
                        .getMessage());

        final float[] floatArr = new float[]{0.0f, 1.0f};
        assertSame(floatArr, Checks.checkNotEmptyArg(floatArr));
        assertSame(floatArr, Checks.checkNotEmptyArg(floatArr, "arr"));
        assertSame(floatArr, Checks.checkNotEmptyArg(floatArr, "foo %s", "bar"));
        assertThrows(IllegalArgumentException.class, () -> Checks.checkNotEmptyArg((float[]) null));
        assertThrows(IllegalArgumentException.class, () -> Checks.checkNotEmptyArg(new float[0]));
        assertEquals("arr",
                assertThrows(IllegalArgumentException.class,
                        () -> Checks.checkNotEmptyArg((float[]) null, "arr")).getMessage());
        assertEquals("arr",
                assertThrows(IllegalArgumentException.class,
                        () -> Checks.checkNotEmptyArg(new float[0], "arr")).getMessage());
        assertEquals("foo bar",
                assertThrows(IllegalArgumentException.class,
                        () -> Checks.checkNotEmptyArg((float[]) null, "foo %s", "bar"))
                        .getMessage());
        assertEquals("foo bar",
                assertThrows(IllegalArgumentException.class,
                        () -> Checks.checkNotEmptyArg(new float[0], "foo %s", "bar"))
                        .getMessage());

        final boolean[] booleanArr = new boolean[]{true, false};
        assertSame(booleanArr, Checks.checkNotEmptyArg(booleanArr));
        assertSame(booleanArr, Checks.checkNotEmptyArg(booleanArr, "arr"));
        assertSame(booleanArr, Checks.checkNotEmptyArg(booleanArr, "foo %s", "bar"));
        assertThrows(IllegalArgumentException.class, () -> Checks.checkNotEmptyArg((boolean[]) null));
        assertThrows(IllegalArgumentException.class, () -> Checks.checkNotEmptyArg(new boolean[0]));
        assertEquals("arr",
                assertThrows(IllegalArgumentException.class,
                        () -> Checks.checkNotEmptyArg((boolean[]) null, "arr")).getMessage());
        assertEquals("arr",
                assertThrows(IllegalArgumentException.class,
                        () -> Checks.checkNotEmptyArg(new boolean[0], "arr")).getMessage());
        assertEquals("foo bar",
                assertThrows(IllegalArgumentException.class,
                        () -> Checks.checkNotEmptyArg((boolean[]) null, "foo %s", "bar"))
                        .getMessage());
        assertEquals("foo bar",
                assertThrows(IllegalArgumentException.class,
                        () -> Checks.checkNotEmptyArg(new boolean[0], "foo %s", "bar"))
                        .getMessage());

        final short[] shortArr = new short[]{0, 1};
        assertSame(shortArr, Checks.checkNotEmptyArg(shortArr));
        assertSame(shortArr, Checks.checkNotEmptyArg(shortArr, "arr"));
        assertSame(shortArr, Checks.checkNotEmptyArg(shortArr, "foo %s", "bar"));
        assertThrows(IllegalArgumentException.class, () -> Checks.checkNotEmptyArg((short[]) null));
        assertThrows(IllegalArgumentException.class, () -> Checks.checkNotEmptyArg(new short[0]));
        assertEquals("arr",
                assertThrows(IllegalArgumentException.class,
                        () -> Checks.checkNotEmptyArg((short[]) null, "arr")).getMessage());
        assertEquals("arr",
                assertThrows(IllegalArgumentException.class,
                        () -> Checks.checkNotEmptyArg(new short[0], "arr")).getMessage());
        assertEquals("foo bar",
                assertThrows(IllegalArgumentException.class,
                        () -> Checks.checkNotEmptyArg((short[]) null, "foo %s", "bar"))
                        .getMessage());
        assertEquals("foo bar",
                assertThrows(IllegalArgumentException.class,
                        () -> Checks.checkNotEmptyArg(new short[0], "foo %s", "bar"))
                        .getMessage());

        final char[] charArr = new char[]{0, 1};
        assertSame(charArr, Checks.checkNotEmptyArg(charArr));
        assertSame(charArr, Checks.checkNotEmptyArg(charArr, "arr"));
        assertSame(charArr, Checks.checkNotEmptyArg(charArr, "foo %s", "bar"));
        assertThrows(IllegalArgumentException.class, () -> Checks.checkNotEmptyArg((char[]) null));
        assertThrows(IllegalArgumentException.class, () -> Checks.checkNotEmptyArg(new char[0]));
        assertEquals("arr",
                assertThrows(IllegalArgumentException.class,
                        () -> Checks.checkNotEmptyArg((char[]) null, "arr")).getMessage());
        assertEquals("arr",
                assertThrows(IllegalArgumentException.class,
                        () -> Checks.checkNotEmptyArg(new char[0], "arr")).getMessage());
        assertEquals("foo bar",
                assertThrows(IllegalArgumentException.class,
                        () -> Checks.checkNotEmptyArg((char[]) null, "foo %s", "bar"))
                        .getMessage());
        assertEquals("foo bar",
                assertThrows(IllegalArgumentException.class,
                        () -> Checks.checkNotEmptyArg(new char[0], "foo %s", "bar"))
                        .getMessage());

        final byte[] byteArr = new byte[]{0, 1};
        assertSame(byteArr, Checks.checkNotEmptyArg(byteArr));
        assertSame(byteArr, Checks.checkNotEmptyArg(byteArr, "arr"));
        assertSame(byteArr, Checks.checkNotEmptyArg(byteArr, "foo %s", "bar"));
        assertThrows(IllegalArgumentException.class, () -> Checks.checkNotEmptyArg((byte[]) null));
        assertThrows(IllegalArgumentException.class, () -> Checks.checkNotEmptyArg(new byte[0]));
        assertEquals("arr",
                assertThrows(IllegalArgumentException.class,
                        () -> Checks.checkNotEmptyArg((byte[]) null, "arr")).getMessage());
        assertEquals("arr",
                assertThrows(IllegalArgumentException.class,
                        () -> Checks.checkNotEmptyArg(new byte[0], "arr")).getMessage());
        assertEquals("foo bar",
                assertThrows(IllegalArgumentException.class,
                        () -> Checks.checkNotEmptyArg((byte[]) null, "foo %s", "bar"))
                        .getMessage());
        assertEquals("foo bar",
                assertThrows(IllegalArgumentException.class,
                        () -> Checks.checkNotEmptyArg(new byte[0], "foo %s", "bar"))
                        .getMessage());

        final Map<String, String> map = Collections.singletonMap("f", "b");
        Checks.checkNotEmptyArg(map);
        Checks.checkNotEmptyArg(map, "map");
        Checks.checkNotEmptyArg(map, "foo %s", "bar");
        assertThrows(IllegalArgumentException.class, () -> Checks.checkNotEmptyArg((Map<String, String>) null));
        assertThrows(IllegalArgumentException.class, () -> Checks.checkNotEmptyArg(Collections.emptyMap()));
        assertEquals("map",
                assertThrows(IllegalArgumentException.class,
                        () -> Checks.checkNotEmptyArg((Map<String, String>) null, "map")).getMessage());
        assertEquals("map",
                assertThrows(IllegalArgumentException.class,
                        () -> Checks.checkNotEmptyArg(Collections.emptyMap(), "map")).getMessage());
        assertEquals("foo bar",
                assertThrows(IllegalArgumentException.class,
                        () -> Checks.checkNotEmptyArg((Map<String, String>) null, "foo %s", "bar"))
                        .getMessage());
        assertEquals("foo bar",
                assertThrows(IllegalArgumentException.class,
                        () -> Checks.checkNotEmptyArg(Collections.emptyMap(), "foo %s", "bar"))
                        .getMessage());
    }

    @Test
    void testCheckNotEmptyState() {
        final String str = "foo";
        assertSame(str, Checks.checkNotEmptyState(str));
        assertSame(str, Checks.checkNotEmptyState(str, "str"));
        assertSame(str, Checks.checkNotEmptyState(str, "foo %s", "bar"));
        assertThrows(IllegalStateException.class, () -> Checks.checkNotEmptyState(StringUtils.empty()));
        assertEquals("str",
                assertThrows(IllegalStateException.class,
                        () -> Checks.checkNotEmptyState(StringUtils.empty(), "str")).getMessage());
        assertEquals("foo bar",
                assertThrows(IllegalStateException.class,
                        () -> Checks.checkNotEmptyState(StringUtils.empty(), "foo %s", "bar"))
                        .getMessage());

        final List<String> list = Arrays.asList("f", "b");
        Checks.checkNotEmptyState(list);
        Checks.checkNotEmptyState(list, "list");
        Checks.checkNotEmptyState(list, "foo %s", "bar");
        assertThrows(IllegalStateException.class, () -> Checks.checkNotEmptyState((List<String>) null));
        assertThrows(IllegalStateException.class, () -> Checks.checkNotEmptyState(Collections.emptyList()));
        assertEquals("list",
                assertThrows(IllegalStateException.class,
                        () -> Checks.checkNotEmptyState((List<String>) null, "list")).getMessage());
        assertEquals("list",
                assertThrows(IllegalStateException.class,
                        () -> Checks.checkNotEmptyState(Collections.emptyList(), "list")).getMessage());
        assertEquals("foo bar",
                assertThrows(IllegalStateException.class,
                        () -> Checks.checkNotEmptyState((List<String>) null, "foo %s", "bar"))
                        .getMessage());
        assertEquals("foo bar",
                assertThrows(IllegalStateException.class,
                        () -> Checks.checkNotEmptyState(Collections.emptyList(), "foo %s", "bar"))
                        .getMessage());

        final String[] refArr = new String[]{"f", "b"};
        assertSame(refArr, Checks.checkNotEmptyState(refArr));
        assertSame(refArr, Checks.checkNotEmptyState(refArr, "str"));
        assertSame(refArr, Checks.checkNotEmptyState(refArr, "foo %s", "bar"));
        assertThrows(IllegalStateException.class, () -> Checks.checkNotEmptyState((String[]) null));
        assertThrows(IllegalStateException.class, () -> Checks.checkNotEmptyState(new String[0]));
        assertEquals("arr",
                assertThrows(IllegalStateException.class,
                        () -> Checks.checkNotEmptyState((String[]) null, "arr")).getMessage());
        assertEquals("arr",
                assertThrows(IllegalStateException.class,
                        () -> Checks.checkNotEmptyState(new String[0], "arr")).getMessage());
        assertEquals("foo bar",
                assertThrows(IllegalStateException.class,
                        () -> Checks.checkNotEmptyState((String[]) null, "foo %s", "bar"))
                        .getMessage());
        assertEquals("foo bar",
                assertThrows(IllegalStateException.class,
                        () -> Checks.checkNotEmptyState(new String[0], "foo %s", "bar"))
                        .getMessage());

        final long[] longArr = new long[]{0L, 1L};
        assertSame(longArr, Checks.checkNotEmptyState(longArr));
        assertSame(longArr, Checks.checkNotEmptyState(longArr, "arr"));
        assertSame(longArr, Checks.checkNotEmptyState(longArr, "foo %s", "bar"));
        assertThrows(IllegalStateException.class, () -> Checks.checkNotEmptyState((long[]) null));
        assertThrows(IllegalStateException.class, () -> Checks.checkNotEmptyState(new long[0]));
        assertEquals("arr",
                assertThrows(IllegalStateException.class,
                        () -> Checks.checkNotEmptyState((long[]) null, "arr")).getMessage());
        assertEquals("arr",
                assertThrows(IllegalStateException.class,
                        () -> Checks.checkNotEmptyState(new long[0], "arr")).getMessage());
        assertEquals("foo bar",
                assertThrows(IllegalStateException.class,
                        () -> Checks.checkNotEmptyState((long[]) null, "foo %s", "bar"))
                        .getMessage());
        assertEquals("foo bar",
                assertThrows(IllegalStateException.class,
                        () -> Checks.checkNotEmptyState(new long[0], "foo %s", "bar"))
                        .getMessage());

        final double[] doubleArr = new double[]{0.0D, 1.0D};
        assertSame(doubleArr, Checks.checkNotEmptyState(doubleArr));
        assertSame(doubleArr, Checks.checkNotEmptyState(doubleArr, "arr"));
        assertSame(doubleArr, Checks.checkNotEmptyState(doubleArr, "foo %s", "bar"));
        assertThrows(IllegalStateException.class, () -> Checks.checkNotEmptyState((double[]) null));
        assertThrows(IllegalStateException.class, () -> Checks.checkNotEmptyState(new double[0]));
        assertEquals("arr",
                assertThrows(IllegalStateException.class,
                        () -> Checks.checkNotEmptyState((double[]) null, "arr")).getMessage());
        assertEquals("arr",
                assertThrows(IllegalStateException.class,
                        () -> Checks.checkNotEmptyState(new double[0], "arr")).getMessage());
        assertEquals("foo bar",
                assertThrows(IllegalStateException.class,
                        () -> Checks.checkNotEmptyState((double[]) null, "foo %s", "bar"))
                        .getMessage());
        assertEquals("foo bar",
                assertThrows(IllegalStateException.class,
                        () -> Checks.checkNotEmptyState(new double[0], "foo %s", "bar"))
                        .getMessage());

        final int[] intArr = new int[]{0, 1};
        assertSame(intArr, Checks.checkNotEmptyState(intArr));
        assertSame(intArr, Checks.checkNotEmptyState(intArr, "arr"));
        assertSame(intArr, Checks.checkNotEmptyState(intArr, "foo %s", "bar"));
        assertThrows(IllegalStateException.class, () -> Checks.checkNotEmptyState((int[]) null));
        assertThrows(IllegalStateException.class, () -> Checks.checkNotEmptyState(new int[0]));
        assertEquals("arr",
                assertThrows(IllegalStateException.class,
                        () -> Checks.checkNotEmptyState((int[]) null, "arr")).getMessage());
        assertEquals("arr",
                assertThrows(IllegalStateException.class,
                        () -> Checks.checkNotEmptyState(new int[0], "arr")).getMessage());
        assertEquals("foo bar",
                assertThrows(IllegalStateException.class,
                        () -> Checks.checkNotEmptyState((int[]) null, "foo %s", "bar"))
                        .getMessage());
        assertEquals("foo bar",
                assertThrows(IllegalStateException.class,
                        () -> Checks.checkNotEmptyState(new int[0], "foo %s", "bar"))
                        .getMessage());

        final float[] floatArr = new float[]{0.0f, 1.0f};
        assertSame(floatArr, Checks.checkNotEmptyState(floatArr));
        assertSame(floatArr, Checks.checkNotEmptyState(floatArr, "arr"));
        assertSame(floatArr, Checks.checkNotEmptyState(floatArr, "foo %s", "bar"));
        assertThrows(IllegalStateException.class, () -> Checks.checkNotEmptyState((float[]) null));
        assertThrows(IllegalStateException.class, () -> Checks.checkNotEmptyState(new float[0]));
        assertEquals("arr",
                assertThrows(IllegalStateException.class,
                        () -> Checks.checkNotEmptyState((float[]) null, "arr")).getMessage());
        assertEquals("arr",
                assertThrows(IllegalStateException.class,
                        () -> Checks.checkNotEmptyState(new float[0], "arr")).getMessage());
        assertEquals("foo bar",
                assertThrows(IllegalStateException.class,
                        () -> Checks.checkNotEmptyState((float[]) null, "foo %s", "bar"))
                        .getMessage());
        assertEquals("foo bar",
                assertThrows(IllegalStateException.class,
                        () -> Checks.checkNotEmptyState(new float[0], "foo %s", "bar"))
                        .getMessage());

        final boolean[] booleanArr = new boolean[]{true, false};
        assertSame(booleanArr, Checks.checkNotEmptyState(booleanArr));
        assertSame(booleanArr, Checks.checkNotEmptyState(booleanArr, "arr"));
        assertSame(booleanArr, Checks.checkNotEmptyState(booleanArr, "foo %s", "bar"));
        assertThrows(IllegalStateException.class, () -> Checks.checkNotEmptyState((boolean[]) null));
        assertThrows(IllegalStateException.class, () -> Checks.checkNotEmptyState(new boolean[0]));
        assertEquals("arr",
                assertThrows(IllegalStateException.class,
                        () -> Checks.checkNotEmptyState((boolean[]) null, "arr")).getMessage());
        assertEquals("arr",
                assertThrows(IllegalStateException.class,
                        () -> Checks.checkNotEmptyState(new boolean[0], "arr")).getMessage());
        assertEquals("foo bar",
                assertThrows(IllegalStateException.class,
                        () -> Checks.checkNotEmptyState((boolean[]) null, "foo %s", "bar"))
                        .getMessage());
        assertEquals("foo bar",
                assertThrows(IllegalStateException.class,
                        () -> Checks.checkNotEmptyState(new boolean[0], "foo %s", "bar"))
                        .getMessage());

        final short[] shortArr = new short[]{0, 1};
        assertSame(shortArr, Checks.checkNotEmptyState(shortArr));
        assertSame(shortArr, Checks.checkNotEmptyState(shortArr, "arr"));
        assertSame(shortArr, Checks.checkNotEmptyState(shortArr, "foo %s", "bar"));
        assertThrows(IllegalStateException.class, () -> Checks.checkNotEmptyState((short[]) null));
        assertThrows(IllegalStateException.class, () -> Checks.checkNotEmptyState(new short[0]));
        assertEquals("arr",
                assertThrows(IllegalStateException.class,
                        () -> Checks.checkNotEmptyState((short[]) null, "arr")).getMessage());
        assertEquals("arr",
                assertThrows(IllegalStateException.class,
                        () -> Checks.checkNotEmptyState(new short[0], "arr")).getMessage());
        assertEquals("foo bar",
                assertThrows(IllegalStateException.class,
                        () -> Checks.checkNotEmptyState((short[]) null, "foo %s", "bar"))
                        .getMessage());
        assertEquals("foo bar",
                assertThrows(IllegalStateException.class,
                        () -> Checks.checkNotEmptyState(new short[0], "foo %s", "bar"))
                        .getMessage());

        final char[] charArr = new char[]{0, 1};
        assertSame(charArr, Checks.checkNotEmptyState(charArr));
        assertSame(charArr, Checks.checkNotEmptyState(charArr, "arr"));
        assertSame(charArr, Checks.checkNotEmptyState(charArr, "foo %s", "bar"));
        assertThrows(IllegalStateException.class, () -> Checks.checkNotEmptyState((char[]) null));
        assertThrows(IllegalStateException.class, () -> Checks.checkNotEmptyState(new char[0]));
        assertEquals("arr",
                assertThrows(IllegalStateException.class,
                        () -> Checks.checkNotEmptyState((char[]) null, "arr")).getMessage());
        assertEquals("arr",
                assertThrows(IllegalStateException.class,
                        () -> Checks.checkNotEmptyState(new char[0], "arr")).getMessage());
        assertEquals("foo bar",
                assertThrows(IllegalStateException.class,
                        () -> Checks.checkNotEmptyState((char[]) null, "foo %s", "bar"))
                        .getMessage());
        assertEquals("foo bar",
                assertThrows(IllegalStateException.class,
                        () -> Checks.checkNotEmptyState(new char[0], "foo %s", "bar"))
                        .getMessage());

        final byte[] byteArr = new byte[]{0, 1};
        assertSame(byteArr, Checks.checkNotEmptyState(byteArr));
        assertSame(byteArr, Checks.checkNotEmptyState(byteArr, "arr"));
        assertSame(byteArr, Checks.checkNotEmptyState(byteArr, "foo %s", "bar"));
        assertThrows(IllegalStateException.class, () -> Checks.checkNotEmptyState((byte[]) null));
        assertThrows(IllegalStateException.class, () -> Checks.checkNotEmptyState(new byte[0]));
        assertEquals("arr",
                assertThrows(IllegalStateException.class,
                        () -> Checks.checkNotEmptyState((byte[]) null, "arr")).getMessage());
        assertEquals("arr",
                assertThrows(IllegalStateException.class,
                        () -> Checks.checkNotEmptyState(new byte[0], "arr")).getMessage());
        assertEquals("foo bar",
                assertThrows(IllegalStateException.class,
                        () -> Checks.checkNotEmptyState((byte[]) null, "foo %s", "bar"))
                        .getMessage());
        assertEquals("foo bar",
                assertThrows(IllegalStateException.class,
                        () -> Checks.checkNotEmptyState(new byte[0], "foo %s", "bar"))
                        .getMessage());

        final Map<String, String> map = Collections.singletonMap("f", "b");
        Checks.checkNotEmptyState(map);
        Checks.checkNotEmptyState(map, "map");
        Checks.checkNotEmptyState(map, "foo %s", "bar");
        assertThrows(IllegalStateException.class, () -> Checks.checkNotEmptyState((Map<String, String>) null));
        assertThrows(IllegalStateException.class, () -> Checks.checkNotEmptyState(Collections.emptyMap()));
        assertEquals("map",
                assertThrows(IllegalStateException.class,
                        () -> Checks.checkNotEmptyState((Map<String, String>) null, "map")).getMessage());
        assertEquals("map",
                assertThrows(IllegalStateException.class,
                        () -> Checks.checkNotEmptyState(Collections.emptyMap(), "map")).getMessage());
        assertEquals("foo bar",
                assertThrows(IllegalStateException.class,
                        () -> Checks.checkNotEmptyState((Map<String, String>) null, "foo %s", "bar"))
                        .getMessage());
        assertEquals("foo bar",
                assertThrows(IllegalStateException.class,
                        () -> Checks.checkNotEmptyState(Collections.emptyMap(), "foo %s", "bar"))
                        .getMessage());
    }
}
