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
package esa.commons.reflect;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReflectionUtilsTest {

    @Test
    void testGetAllDeclaredMethods() {
        final List<String> methods = ReflectionUtils.getAllDeclaredMethods(A.class).stream()
                .map(Method::getName)
                .collect(Collectors.toList());

        // A.class
        assertTrue(methods.contains("getA"));
        assertTrue(methods.contains("setA"));
        // Base.class
        assertTrue(methods.contains("foo"));
        assertTrue(methods.contains("bar"));

        // Object.class
        assertFalse(methods.contains("equals"));
        assertFalse(methods.contains("toString"));
    }

    @Test
    void testGetAllDeclaredFields() {
        final List<Field> methods = ReflectionUtils.getAllDeclaredFields(A.class);
        final List<String> aMethods = methods.stream()
                .filter(m -> m.getDeclaringClass().equals(A.class))
                .map(Field::getName)
                .collect(Collectors.toList());
        assertTrue(aMethods.contains("a"));
        assertTrue(aMethods.contains("b"));
        assertTrue(aMethods.contains("c"));
        assertTrue(aMethods.contains("d"));

        final List<String> bMethods = methods.stream()
                .filter(m -> m.getDeclaringClass().equals(Base.class))
                .map(Field::getName)
                .collect(Collectors.toList());
        assertTrue(bMethods.contains("foo"));
        assertTrue(bMethods.contains("bar"));
        assertTrue(bMethods.contains("baz"));
    }

    @Test
    void testMakeMethodAccessible() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final Method m = Base.class.getDeclaredMethod("foo");

        final Base base = new Base();
        assertThrows(IllegalAccessException.class, () -> m.invoke(base));
        ReflectionUtils.makeMethodAccessible(m);
        assertEquals(base.foo, m.invoke(base));
    }

    @Test
    void testMakeFiledAccessible() throws NoSuchFieldException, IllegalAccessException {
        final Field f = Base.class.getDeclaredField("foo");

        final Base base = new Base();
        assertThrows(IllegalAccessException.class, () -> f.get(base));
        ReflectionUtils.makeFieldAccessible(f);
        assertEquals(base.foo, f.get(base));
    }

    @Test
    void testMakeConstructorAccessible() throws IllegalAccessException,
            NoSuchMethodException, InvocationTargetException, InstantiationException {
        final Constructor c = Base.class.getDeclaredConstructor();
        assertThrows(IllegalAccessException.class, c::newInstance);
        ReflectionUtils.makeConstructorAccessible(c);
        assertNotNull(c.newInstance());
    }

    @Test
    void testAccessibleConstructor() throws IllegalAccessException,
            NoSuchMethodException, InvocationTargetException, InstantiationException {
        assertNotNull(ReflectionUtils.accessibleConstructor(Base.class).newInstance());
    }

    @Test
    void testInvokeMethod() throws NoSuchMethodException {
        final Method m = Base.class.getDeclaredMethod("foo");

        final Base base = new Base();
        assertEquals(base.foo, ReflectionUtils.invokeMethod(m, base));
    }

    @Test
    void testIsStatic() throws NoSuchMethodException {
        assertTrue(ReflectionUtils.isStatic(ReflectionUtils.class
                .getDeclaredMethod("isStatic", Method.class)));

        assertFalse(ReflectionUtils.isStatic(Base.class
                .getDeclaredMethod("foo")));
    }

    @Test
    void testIsGetter() throws NoSuchMethodException {
        assertTrue(ReflectionUtils.isGetter(A.class
                .getDeclaredMethod("getA")));
        assertTrue(ReflectionUtils.isGetter(A.class
                .getDeclaredMethod("isB")));
        assertTrue(ReflectionUtils.isGetter(A.class
                .getDeclaredMethod("getC")));
        assertTrue(ReflectionUtils.isGetter(A.class
                .getDeclaredMethod("getD")));

        assertFalse(ReflectionUtils.isGetter(A.class
                .getDeclaredMethod("get")));
        assertFalse(ReflectionUtils.isGetter(A.class
                .getDeclaredMethod("geta")));
        assertFalse(ReflectionUtils.isGetter(A.class
                .getDeclaredMethod("getA", String.class)));
        assertFalse(ReflectionUtils.isStatic(A.class
                .getDeclaredMethod("setA", String.class)));
    }

    @Test
    void testIsSetter() throws NoSuchMethodException {
        assertTrue(ReflectionUtils.isSetter(A.class
                .getDeclaredMethod("setA", String.class)));
        assertTrue(ReflectionUtils.isSetter(A.class
                .getDeclaredMethod("setB", boolean.class)));
        assertTrue(ReflectionUtils.isSetter(A.class
                .getDeclaredMethod("setC", Boolean.class)));
        assertTrue(ReflectionUtils.isSetter(A.class
                .getDeclaredMethod("setD", int.class)));

        assertFalse(ReflectionUtils.isSetter(A.class
                .getDeclaredMethod("set")));
        assertFalse(ReflectionUtils.isSetter(A.class
                .getDeclaredMethod("seta")));
        assertFalse(ReflectionUtils.isSetter(A.class
                .getDeclaredMethod("get")));
        assertFalse(ReflectionUtils.isSetter(A.class
                .getDeclaredMethod("getA", String.class)));
    }

    @Test
    void testGetGetter() throws NoSuchFieldException {
        assertNull(ReflectionUtils.getGetter(Base.class.getDeclaredField("baz")));
        assertEquals("getA",
                ReflectionUtils.getGetter(A.class.getDeclaredField("a")).getName());
        assertEquals("getAbc",
                ReflectionUtils.getGetter(A.class.getDeclaredField("abc")).getName());
        assertEquals("getAba",
                ReflectionUtils.getGetter(A.class.getDeclaredField("aba")).getName());
        assertEquals("isB",
                ReflectionUtils.getGetter(A.class.getDeclaredField("b")).getName());
        assertEquals("getC",
                ReflectionUtils.getGetter(A.class.getDeclaredField("c")).getName());
        assertEquals("getD",
                ReflectionUtils.getGetter(A.class.getDeclaredField("d")).getName());
    }

    @Test
    void testGetSetter() throws NoSuchFieldException {
        assertNull(ReflectionUtils.getGetter(Base.class.getDeclaredField("baz")));
        assertEquals("setA",
                ReflectionUtils.getSetter(A.class.getDeclaredField("a")).getName());
        assertEquals("setAbc",
                ReflectionUtils.getSetter(A.class.getDeclaredField("abc")).getName());
        assertEquals("setAba",
                ReflectionUtils.getSetter(A.class.getDeclaredField("aba")).getName());
        assertEquals("setB",
                ReflectionUtils.getSetter(A.class.getDeclaredField("b")).getName());
        assertEquals("setC",
                ReflectionUtils.getSetter(A.class.getDeclaredField("c")).getName());
        assertEquals("setD",
                ReflectionUtils.getSetter(A.class.getDeclaredField("d")).getName());
    }

    @Test
    void testGetInterfaceMethods() {
        List<Method> methods = ReflectionUtils.getAllDeclaredMethods(ClazzB.class);
        assertTrue(methods.stream().anyMatch(m -> m.getName().equals("echoBase")));
        assertTrue(methods.stream().anyMatch(m -> m.getName().equals("echo")));
        assertTrue(methods.stream().anyMatch(m -> m.getName().equals("echoA")));
        ClazzB b = new ClazzB();
        for (Method method : methods) {
            try {
                method.invoke(b, new Object[]{null});
            } catch (Exception ignored) {
            }
        }
        methods = ReflectionUtils.getAllDeclaredMethods(ClazzC.class);
        assertTrue(methods.stream().filter(m -> m.getName().equals("echoBase")).count() > 1);
        assertTrue(methods.stream().anyMatch(m -> m.getName().equals("echoA")));
        assertTrue(methods.stream().anyMatch(m -> m.getName().equals("echo")));
        ClazzC c = new ClazzC();
        for (Method method : methods) {
            try {
                method.invoke(c, new Object[]{null});
            } catch (Exception ignored) {
            }
        }
    }

    private interface IfaceA {
        Base echoBase(Base obj);

        String echo(String hello);
    }

    private interface IfaceB extends IfaceA {
        A echoA(A a);
    }

    private interface IfaceC extends IfaceA {
        @Override
        A echoBase(Base obj);

        A echoA(A a);
    }

    private static class ClazzB implements IfaceB {
        @Override
        public Base echoBase(Base obj) {
            return null;
        }

        @Override
        public String echo(String hello) {
            return null;
        }

        @Override
        public A echoA(A a) {
            return null;
        }
    }

    private static class ClazzC implements IfaceC {
        @Override
        public String echo(String hello) {
            return null;
        }

        @Override
        public A echoBase(Base obj) {
            return null;
        }

        @Override
        public A echoA(A a) {
            return null;
        }
    }

    private static class Base {

        private final long foo = 1L;
        private final double bar = 1.0D;
        private final int baz = 1;

        private Base() {

        }

        private long foo() {
            return foo;
        }

        private double bar() {
            return bar;
        }
    }

    private static class A extends Base {

        private String a;
        private boolean b;
        private Boolean c;
        private int d;
        private String abc;
        private String aba;

        private String getAba() {
            return aba;
        }

        private void setAba(String aba) {
            this.aba = aba;
        }

        private String getAbc() {
            return abc;
        }

        private void setAbc(String a) {
            this.abc = abc;
        }

        private String getA() {
            return a;
        }

        private void setA(String a) {
            this.a = a;
        }

        private boolean isB() {
            return b;
        }

        private void setB(boolean b) {
            this.b = b;
        }

        private Boolean getC() {
            return c;
        }

        private void setC(Boolean c) {
            this.c = c;
        }

        private int getD() {
            return d;
        }

        private void setD(int d) {
            this.d = d;
        }

        private void get() {
        }

        private String getA(String a) {
            return a;
        }

        private String geta() {
            return null;
        }

        private String set() {
            return null;
        }

        private void seta() {
        }
    }

}
