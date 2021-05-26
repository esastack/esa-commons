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
package esa.commons.logging;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FormatterTest {

    @Test
    void testConverters() {
        final StringBuilder buf = new StringBuilder();
        Formatter.toConverter("%i - %msg",
                Collections.emptyMap()).convert(null, buf);
        assertEquals("%i - %msg", buf.toString());
        buf.setLength(0);

        Formatter.toConverter("%i - %msg", Collections.singletonMap("i",
                p -> (t, sb) -> sb.append("int"))).convert(null, buf);
        assertEquals("int - %msg", buf.toString());

        buf.setLength(0);
        Formatter.toConverter("%i - %msg", Collections.singletonMap("msg",
                p -> (t, sb) -> sb.append(
                        "message"))).convert(null, buf);
        assertEquals("%i - message", buf.toString());

        final Map<String, Function<String, Converter<Object>>> map = new LinkedHashMap<>();
        map.put("i", p -> (t, sb) -> sb.append("int"));
        map.put("msg", p -> (t, sb) -> sb.append("message"));

        buf.setLength(0);
        Formatter.toConverter("%i - %msg", map).convert(null, buf);
        assertEquals("int - message", buf.toString());

        buf.setLength(0);
        map.put("f", p -> (t, sb) -> sb.append("foo"));
        Formatter.toConverter("%i - %msg - %f", map).convert(null, buf);
        assertEquals("int - message - foo", buf.toString());


        buf.setLength(0);
        Formatter.toConverter("%i%i - %msg%msg - %f", map).convert(null, buf);
        assertEquals("intint - messagemessage - foo", buf.toString());

        buf.setLength(0);
        Formatter.toConverter(" %i%i - %msg%msg - %f ", map).convert(null, buf);
        assertEquals(" intint - messagemessage - foo ", buf.toString());


        buf.setLength(0);
        Formatter.toConverter("plain text", map).convert(null, buf);
        assertEquals("plain text", buf.toString());

        // escape
        buf.setLength(0);
        Formatter.toConverter("\\%i - \\\\%msg - \\%f", map).convert(null, buf);
        assertEquals("%i - \\message - %f", buf.toString());

        final List<String> l = Arrays.asList("foo", "bar");
        final Map<String, Function<String, Converter<List<String>>>> map1 = new LinkedHashMap<>();
        map1.put("f", p -> (plist, sb) -> sb.append(plist.get(0)));
        map1.put("b", p -> (plist, sb) -> sb.append(plist.get(1)));

        buf.setLength(0);
        Formatter.toConverter("%f - %b", map1).convert(l, buf);
        assertEquals("foo - bar", buf.toString());

        buf.setLength(0);
        Formatter.toConverter("$f - $b", '$', map1).convert(l, buf);
        assertEquals("foo - bar", buf.toString());

        // test params
        buf.setLength(0);
        Formatter.toConverter("%i{} - %msg{bar}", Collections.emptyMap()).convert(null, buf);
        assertEquals("%i{} - %msg{bar}", buf.toString());

        buf.setLength(0);
        Formatter.toConverter("%i{foo} - %msg{bar}", Collections.singletonMap("i",
                p -> (t, sb) -> sb.append(p).append("a"))).convert(null, buf);
        assertEquals("fooa - %msg{bar}", buf.toString());

        buf.setLength(0);
        Formatter.toConverter("%i{foo} - %msg{bar}", Collections.singletonMap("msg",
                p -> (t, sb) -> sb.append(p).append("b"))).convert(null, buf);
        assertEquals("%i{foo} - barb", buf.toString());

        map.clear();
        map.put("i", p -> (t, sb) -> sb.append(p).append("a"));
        map.put("msg", p -> (t, sb) -> sb.append(p).append("b"));

        buf.setLength(0);
        Formatter.toConverter("%i{foo} - %msg{bar}", map).convert(null, buf);
        assertEquals("fooa - barb", buf.toString());

        buf.setLength(0);
        Formatter.toConverter("%i - %msg{", map).convert(null, buf);
        assertEquals("nulla - nullb{", buf.toString());

        buf.setLength(0);
        Formatter.toConverter("%i} - %msg{{", map).convert(null, buf);
        assertEquals("nulla} - nullb{{", buf.toString());

        // test escape
        buf.setLength(0);
        Formatter.toConverter("%i\\{} - %msg{\\}", map).convert(null, buf);
        assertEquals("nulla\\{} - nullb{}", buf.toString());

        buf.setLength(0);
        Formatter.toConverter("%i{foo\\} - %msg{\\\\}", map).convert(null, buf);
        assertEquals("nulla{foo} - \\b", buf.toString());

        buf.setLength(0);
        Formatter.toConverter("%i{foo\\\\} - %msg{\\\\bar}", map).convert(null, buf);
        assertEquals("foo\\a - \\\\barb", buf.toString());
    }

    @Test
    void testFormatParams() {
        assertEquals("foo",
                Formatter.format("foo", new Object[0]));
        assertEquals("foo bar",
                Formatter.format("foo {}", new Object[]{"bar"}));
        assertEquals("foo 1 2",
                Formatter.format("foo {} {}", new Object[]{1L, 2L}));
        assertEquals("foo 1 2",
                Formatter.format("foo {} {}", new Object[]{(short) 1, (byte) 2}, 2));
        assertEquals("foo 1.22 {}",
                Formatter.format("foo {} {}", new Object[]{1.22D}));
        assertEquals("foo 1.11 {}",
                Formatter.format("foo {} {}", new Object[]{1.11f, 1}, 1));
        assertEquals("foo bar",
                Formatter.format("foo {}", new Object[]{"bar", 1}, 2));
        assertEquals("foo bar",
                Formatter.format("foo {}", new Object[]{"bar", 1}));
        assertEquals("foo bar",
                Formatter.format("{} bar", new Object[]{"foo"}));
        // escape
        assertEquals("foo bar {}",
                Formatter.format("foo {} \\{}", new Object[]{"bar", "baz"}));
        assertEquals("foo bar {}",
                Formatter.format("foo {} \\{}", new Object[]{"bar", "baz"}, 2));
        assertEquals("foo bar \\baz",
                Formatter.format("foo {} \\\\{}", new Object[]{"bar", "baz"}));
        assertEquals("foo bar \\baz",
                Formatter.format("foo {} \\\\{}", new Object[]{"bar", "baz"}, 2));
        assertEquals("foo bar \\\\{}",
                Formatter.format("foo {} \\\\{}", new Object[]{"bar", "baz"}, 1));
        assertEquals("{} bar",
                Formatter.format("\\{} bar", new Object[]{"foo"}));
        assertEquals("{} bar",
                Formatter.format("\\{} bar", new Object[]{"foo"}, 1));
        assertEquals("\\foo bar",
                Formatter.format("\\\\{} bar", new Object[]{"foo"}, 1));

        // error toString
        Object o = new Object() {
            @Override
            public String toString() {
                throw new IllegalStateException("a");
            }
        };
        String result = Formatter.format("Troublesome object {}", new Object[]{o});
        assertEquals("Troublesome object [FAILED toString()]", result);

        // test arrays
        assertEquals("values: foo [bar, baz]",
                Formatter.format("values: {} {}", new Object[]{"foo", new String[]{"bar", "baz"}}));
        assertEquals("values: foo [true, false]",
                Formatter.format("values: {} {}", new Object[]{"foo", new boolean[]{true, false}}));
        assertEquals("values: foo [1, 2]",
                Formatter.format("values: {} {}", new Object[]{"foo", new byte[]{1, 2}}));
        assertEquals("values: foo [1, 2]",
                Formatter.format("values: {} {}", new Object[]{"foo", new short[]{1, 2}}));
        assertEquals("values: foo [1, 2]",
                Formatter.format("values: {} {}", new Object[]{"foo", new int[]{1, 2}}));
        assertEquals("values: foo [1, 2]",
                Formatter.format("values: {} {}", new Object[]{"foo", new long[]{1, 2}}));
        assertEquals("values: foo [1.0, 2.0]",
                Formatter.format("values: {} {}", new Object[]{"foo", new double[]{1, 2}}));
        assertEquals("values: foo [1.0, 2.0]",
                Formatter.format("values: {} {}", new Object[]{"foo", new float[]{1, 2}}));

        // test  cyclic array
        final Object[] cyclicA = new Object[1];
        cyclicA[0] = cyclicA;
        assertEquals("[[...]]", Formatter.format("{}", cyclicA));

        final Object[] a = new Object[2];
        a[0] = 1;
        final Object[] c = {3, a};
        final Object[] b = {2, c};
        a[1] = b;
        assertEquals("1[2, [3, [1, [...]]]]",
                Formatter.format("{}{}", a));
    }

}
