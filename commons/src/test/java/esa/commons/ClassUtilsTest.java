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

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class ClassUtilsTest {

    @Test
    void testHasClass() {
        assertTrue(ClassUtils.hasClass("java.lang.String"));
        assertFalse(ClassUtils.hasClass("absent.Clz"));
    }

    @Test
    void testForName() {
        assertSame(String.class, ClassUtils.forName("java.lang.String"));
        assertSame(String.class, ClassUtils.forName("java.lang.String", false));
        assertNull(ClassUtils.forName("absent.Clz"));
    }

    @Test
    void testGetUserType() {
        assertEquals(Object.class, ClassUtils.getUserType(new Object()));
        assertEquals(Object.class, ClassUtils.getUserType(Object.class));
    }

    @Test
    void testGetRawType() throws NoSuchMethodException {
        assertNull(ClassUtils.getRawType(null));
        assertEquals(Object.class, ClassUtils.getRawType(Object.class));
        assertEquals(Map.class,
                ClassUtils.getRawType(ClassUtilsTest.class
                        .getDeclaredMethod("parameters")
                        .getReturnType()));
        assertEquals(Map.class,
                ClassUtils.getRawType(ClassUtilsTest.class
                        .getDeclaredMethod("parameters")
                        .getGenericReturnType()));
        assertEquals(Map.class,
                ClassUtils.getRawType(ClassUtilsTest.class
                        .getDeclaredMethod("parameters1")
                        .getReturnType()));
        assertEquals(Map.class,
                ClassUtils.getRawType(ClassUtilsTest.class
                        .getDeclaredMethod("parameters1")
                        .getGenericReturnType()));

        assertNull(ClassUtils.getRawType(new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return new Type[0];
            }

            @Override
            public Type getRawType() {
                try {
                    return ClassUtilsTest.class
                            .getDeclaredMethod("parameters")
                            .getGenericReturnType();
                } catch (NoSuchMethodException e) {
                    fail();
                    return null;
                }
            }

            @Override
            public Type getOwnerType() {
                return null;
            }
        }));
    }

    private List<String> parameters(String a, int b) {
        return null;
    }

    private Map<String, Integer> parameters() {
        return null;
    }

    private Map parameters1() {
        return null;
    }

    @Test
    void retrieveGenericTypes() throws NoSuchMethodException {
        assertEquals(0, ClassUtils.retrieveGenericTypes(Object.class).length);
        assertEquals(0, ClassUtils.retrieveGenericTypes(ArrayList.class).length);
        assertEquals(1, ClassUtils.retrieveGenericTypes(ClassUtilsTest.class
                .getDeclaredMethod("parameters", String.class, int.class).getGenericReturnType())
                .length);
        assertEquals(2, ClassUtils.retrieveGenericTypes(ClassUtilsTest.class
                .getDeclaredMethod("parameters").getGenericReturnType()).length);

        assertEquals(String.class, ClassUtils.retrieveGenericTypes(ClassUtilsTest.class
                .getDeclaredMethod("parameters", String.class, int.class).getGenericReturnType())[0]);
        assertEquals(String.class, ClassUtils.retrieveGenericTypes(ClassUtilsTest.class
                .getDeclaredMethod("parameters").getGenericReturnType())[0]);
        assertEquals(Integer.class, ClassUtils.retrieveGenericTypes(ClassUtilsTest.class
                .getDeclaredMethod("parameters").getGenericReturnType())[1]);

        assertTrue(ClassUtils.retrieveFirstGenericType(ClassUtilsTest.class
                .getDeclaredMethod("parameters", String.class, int.class).getGenericReturnType()).isPresent());
        assertEquals(String.class, ClassUtils.retrieveFirstGenericType(ClassUtilsTest.class
                .getDeclaredMethod("parameters", String.class, int.class).getGenericReturnType()).get());
    }

    @Test
    void retrieveTargetGenericTypes() throws NoSuchMethodException {
        assertEquals(0, ClassUtils.findGenericTypes(Object.class).length);
        assertEquals(0, ClassUtils.retrieveGenericTypes(ArrayList.class).length);
        assertEquals(1, ClassUtils.retrieveGenericTypes(ClassUtilsTest.class
                .getDeclaredMethod("parameters", String.class, int.class).getGenericReturnType())
                .length);

        assertEquals(String.class, ClassUtils.retrieveGenericTypes(ClassUtilsTest.class
                .getDeclaredMethod("parameters", String.class, int.class).getGenericReturnType())[0]);

        assertTrue(ClassUtils.retrieveFirstGenericType(ClassUtilsTest.class
                .getDeclaredMethod("parameters", String.class, int.class).getGenericReturnType()).isPresent());
        assertEquals(String.class, ClassUtils.retrieveFirstGenericType(ClassUtilsTest.class
                .getDeclaredMethod("parameters", String.class, int.class).getGenericReturnType()).get());
    }


    @Test
    void findGenericTypes() {
        assertEquals(0, ClassUtils.findGenericTypes(Object.class).length);
        assertEquals(1, ClassUtils.findGenericTypes(ArrayList.class).length);

        assertTrue(ClassUtils.findFirstGenericType(ArrayList.class).isPresent());
        assertEquals(Object.class, ClassUtils.findFirstGenericType(ArrayList.class).get());

        assertEquals(1, ClassUtils.findGenericTypes(new ArrayList<String>() {
        }.getClass())
                .length);
        assertEquals(String.class, ClassUtils.findGenericTypes(new ArrayList<String>() {
        }.getClass())[0]);
        assertTrue(ClassUtils.findFirstGenericType(new ArrayList<String>() {
        }.getClass()).isPresent());
        assertEquals(String.class, ClassUtils.findFirstGenericType(new ArrayList<String>() {
        }.getClass()).get());

        assertEquals(2, ClassUtils.findGenericTypes(new HashMap<String, Integer>() {
        }.getClass()).length);
        assertEquals(String.class, ClassUtils.findGenericTypes(new HashMap<String, Integer>() {
        }.getClass())[0]);
        assertEquals(Integer.class, ClassUtils.findGenericTypes(new HashMap<String, Integer>() {
        }.getClass())[1]);

        assertTrue(ClassUtils.findFirstGenericType(ForTest0.class).isPresent());
        assertEquals(String.class, ClassUtils.findFirstGenericType(ForTest0.class).get());

        assertTrue(ClassUtils.findFirstGenericType(ForTest1.class).isPresent());
        assertEquals(String.class, ClassUtils.findFirstGenericType(ForTest1.class).get());

        assertTrue(ClassUtils.findFirstGenericType(ForTest2.class).isPresent());
        assertEquals(String.class, ClassUtils.findFirstGenericType(ForTest2.class).get());

        assertTrue(ClassUtils.findFirstGenericType(ForTest3.class).isPresent());
        assertEquals(String.class, ClassUtils.findFirstGenericType(ForTest3.class).get());

        assertTrue(ClassUtils.findFirstGenericType(ForTest4.class).isPresent());
        assertEquals(String.class, ClassUtils.findFirstGenericType(ForTest4.class).get());

        assertTrue(ClassUtils.findFirstGenericType(ForTest5.class).isPresent());
        assertEquals(String.class, ClassUtils.findFirstGenericType(ForTest5.class).get());

        assertTrue(ClassUtils.findFirstGenericType(ForTest6.class).isPresent());
        assertEquals(String.class, ClassUtils.findFirstGenericType(ForTest6.class).get());
        assertTrue(ClassUtils.findFirstGenericType(ForTest6.class, Comparable.class).isPresent());
        assertEquals(String.class, ClassUtils.findFirstGenericType(ForTest6.class, Comparable.class).get());
        assertTrue(ClassUtils.findFirstGenericType(ForTest6.class, Iterator.class).isPresent());
        assertEquals(Integer.class, ClassUtils.findFirstGenericType(ForTest6.class, Iterator.class).get());

        assertTrue(ClassUtils.findFirstGenericType(ForTest7.class).isPresent());
        assertEquals(String.class, ClassUtils.findFirstGenericType(ForTest7.class).get());

        assertTrue(ClassUtils.findFirstGenericType(ForTest8.class).isPresent());
        assertEquals(String.class, ClassUtils.findFirstGenericType(ForTest8.class).get());
    }

    @Test
    void testFindGenericTypes1() {
        final Type[] types0 = ClassUtils.findGenericTypes(AVImpl.class, V.class);
        assertNotNull(types0);
        assertEquals(1, types0.length);
        assertEquals(String.class, types0[0]);

        final Type[] types1 = ClassUtils.findGenericTypes(AVImpl.class, AV.class);
        assertNotNull(types1);
        assertEquals(1, types1.length);
        assertEquals(String.class, types1[0]);

        final Type[] types2 = ClassUtils.findGenericTypes(AVVImpl.class, V.class);
        assertNotNull(types2);
        assertEquals(1, types2.length);
        assertEquals(Integer.class, types2[0]);

        final Type[] types3 = ClassUtils.findGenericTypes(SubAVVImpl.class, V.class);
        assertNotNull(types3);
        assertEquals(1, types3.length);
        assertEquals(Integer.class, types3[0]);

        final Type[] types4 = ClassUtils.findGenericTypes(AVV.class, VV.class);
        assertNotNull(types4);
        assertEquals(2, types4.length);
        assertEquals(String.class, types4[0]);
        assertEquals(Integer.class, types4[1]);

        final Type[] types5 = ClassUtils.findGenericTypes(AVVImpl.class, VV.class);
        assertNotNull(types5);
        assertEquals(2, types5.length);
        assertEquals(String.class, types5[0]);
        assertEquals(Integer.class, types5[1]);

        final Type[] types6 = ClassUtils.findGenericTypes(SubAVVImpl.class, VV.class);
        assertNotNull(types6);
        assertEquals(2, types6.length);
        assertEquals(String.class, types6[0]);
        assertEquals(Integer.class, types6[1]);

        final Type[] types7 = ClassUtils.findGenericTypes(SubAVVImpl.class, VEmpty.class);
        assertNotNull(types7);
        assertEquals(0, types7.length);

        final Type[] types8 = ClassUtils.findGenericTypes(SubAVVImpl.class, AVV.class);
        assertNotNull(types8);
        assertEquals(0, types8.length);

        final Type[] types9 = ClassUtils.findGenericTypes(SS.class, Empty.class);
        assertNotNull(types9);
        assertEquals(0, types9.length);
    }

    @Test
    void testFindGenericTypes2() {
        final Type[] types0 = ClassUtils.findGenericTypes(SImpl.class, S.class);
        assertNotNull(types0);
        assertEquals(1, types0.length);
        assertEquals(String.class, types0[0]);

        final Type[] types1 = ClassUtils.findGenericTypes(SubSSImpl.class, S.class);
        assertNotNull(types1);
        assertEquals(1, types1.length);
        assertEquals(String.class, types1[0]);

        final Type[] types2 = ClassUtils.findGenericTypes(SSImpl.class, SS.class);
        assertNotNull(types2);
        assertEquals(2, types2.length);
        assertEquals(Float.class, types2[0]);
        assertEquals(String.class, types2[1]);

        final Type[] types3 = ClassUtils.findGenericTypes(SubSSImpl.class, SS.class);
        assertNotNull(types3);
        assertEquals(2, types3.length);
        assertEquals(Float.class, types3[0]);
        assertEquals(String.class, types3[1]);

        final Type[] types4 = ClassUtils.findGenericTypes(ASImpl.class, S.class);
        assertNotNull(types4);
        assertEquals(1, types4.length);
        assertEquals(String.class, types4[0]);

        final Type[] types5 = ClassUtils.findGenericTypes(ASSImpl.class, SS.class);
        assertNotNull(types5);
        assertEquals(2, types5.length);
        assertEquals(String.class, types5[0]);
        assertEquals(Integer.class, types5[1]);

        final Type[] types6 = ClassUtils.findGenericTypes(ASS.class, SS.class);
        assertNotNull(types6);
        assertEquals(2, types6.length);
        assertEquals(String.class, types6[0]);
        assertEquals(Integer.class, types6[1]);

        final Type[] types7 = ClassUtils.findGenericTypes(ASS.class, Empty.class);
        assertNotNull(types7);
        assertEquals(0, types7.length);

        final Type[] types8 = ClassUtils.findGenericTypes(SSIImpl.class, SSI.class);
        assertNotNull(types8);
        assertEquals(0, types8.length);
    }

    @Test
    void testDoWithMethods() {
        final List<String> methods = new LinkedList<>();
        ClassUtils.doWithMethods(Son.class, m -> methods.add(m.getName()), m -> !m.getName().equals("son1"));
        assertTrue(methods.contains("son"));
        assertTrue(methods.contains("father"));
        assertTrue(methods.contains("grandFather"));
        assertTrue(methods.contains("toString"));
        assertFalse(methods.contains("son1"));
    }

    @Test
    void testDoWithUserDeclaredMethods() {
        final List<String> methods = new LinkedList<>();
        ClassUtils.doWithUserDeclaredMethods(Son.class,
                m -> methods.add(m.getName()), m -> !m.getName().equals("son1"));
        assertTrue(methods.contains("son"));
        assertTrue(methods.contains("father"));
        assertTrue(methods.contains("grandFather"));
        assertFalse(methods.contains("toString"));
        assertFalse(methods.contains("son1"));
    }

    @Test
    void testUserDeclaredMethods() {
        final List<String> methods = ClassUtils.userDeclaredMethods(Son.class)
                .stream()
                .map(Method::getName)
                .collect(Collectors.toList());
        assertTrue(methods.contains("son"));
        assertTrue(methods.contains("father"));
        assertTrue(methods.contains("grandFather"));
        assertTrue(methods.contains("son1"));
        assertFalse(methods.contains("toString"));
    }

    @Test
    void testFindOverriddenMethods() throws Throwable {
        final Method m1 = SubAVVImpl.class.getMethod("toString");
        List<Method> founds1 = ClassUtils.findOverriddenMethods(m1);
        assertEquals(6, founds1.size());
        assertEquals("toString", founds1.get(0).getName());
        assertEquals(AVVImpl.class, founds1.get(0).getDeclaringClass());
        assertEquals("toString", founds1.get(1).getName());
        assertEquals(AVV.class, founds1.get(1).getDeclaringClass());
        assertEquals("toString", founds1.get(2).getName());
        assertEquals(VV.class, founds1.get(2).getDeclaringClass());

        assertEquals("toString", founds1.get(3).getName());
        assertEquals(V.class, founds1.get(3).getDeclaringClass());
        assertEquals("toString", founds1.get(4).getName());
        assertEquals(VEmpty.class, founds1.get(4).getDeclaringClass());
        assertEquals("toString", founds1.get(5).getName());
        assertEquals(Object.class, founds1.get(5).getDeclaringClass());

        final Method m2 = ASImpl.class.getMethod("getE");
        List<Method> founds2 = ClassUtils.findOverriddenMethods(m2);
        assertEquals(2, founds2.size());
        assertEquals("getE", founds2.get(0).getName());
        assertEquals(AS.class, founds2.get(0).getDeclaringClass());
        assertEquals("getE", founds2.get(1).getName());
        assertEquals(S.class, founds2.get(1).getDeclaringClass());
    }

    @Test
    void testFindOverriddenMethod() throws Throwable {
        final Method m1 = AS.class.getMethod("getE");
        Optional<Method> target1 = ClassUtils.findOverriddenMethod(m1);
        assertTrue(target1.isPresent());
        assertEquals("getE", target1.get().getName());
        assertEquals(S.class, target1.get().getDeclaringClass());

        final Method m2 = ASImpl.class.getMethod("getE");
        Optional<Method> target2 = ClassUtils.findOverriddenMethod(m2);
        assertTrue(target2.isPresent());
        assertEquals("getE", target2.get().getName());
        assertEquals(AS.class, target2.get().getDeclaringClass());

        final Method m3 = SSIImpl.class.getMethod("saveAll", Float.class, String.class);
        Optional<Method> target3 = ClassUtils.findOverriddenMethod(m3);
        assertTrue(target3.isPresent());
        assertEquals("saveAll", target3.get().getName());
        assertEquals(SS.class, target3.get().getDeclaringClass());

        final Method m4 = SS.class.getMethod("saveE", Object.class);
        Optional<Method> target4 = ClassUtils.findOverriddenMethod(m4);
        assertTrue(target4.isPresent());
        assertEquals("saveE", target4.get().getName());
        assertEquals(S.class, target4.get().getDeclaringClass());

        final Method m5 = SubAVVImpl.class.getMethod("saveE", Integer.class);
        Optional<Method> target5 = ClassUtils.findOverriddenMethod(m5);
        assertTrue(target5.isPresent());
        assertEquals("saveE", target5.get().getName());
        assertEquals(V.class, target5.get().getDeclaringClass());

        final Method m6 = SubAVVImpl.class.getMethod("saveT", String.class);
        Optional<Method> target6 = ClassUtils.findOverriddenMethod(m6);
        assertTrue(target6.isPresent());
        assertEquals("saveT", target6.get().getName());
        assertEquals(VV.class, target6.get().getDeclaringClass());
    }

    private static class GrandFa {

        public void grandFather() {

        }
    }

    private static class Fa extends GrandFa {

        public void father() {

        }
    }

    private static class Son extends Fa {

        public void son() {

        }

        public void son1() {

        }
    }

    private static class ForTest0 implements Comparable<String> {

        @Override
        public int compareTo(String o) {
            return 0;
        }
    }

    private static class ForTest1 extends ForTest0 {
    }

    private static class ForTest2 extends ForTest1 {
    }

    private interface C extends Comparable<String> {
    }

    private static class ForTest3 implements C {
        @Override
        public int compareTo(String o) {
            return 0;
        }
    }

    private static class ForTest4 extends ForTest3 {
    }

    private static class ForTest5 extends ForTest3 implements Comparable<String> {
    }

    private static class ForTest6 extends ForTest3 implements Comparable<String>,
            Iterator<Integer> {
        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public Integer next() {
            return null;
        }
    }

    private static class T<E> {
    }

    private static class ForTest7 extends T<String> {
    }

    private static class ForTest8 extends ForTest7 {
    }

    private interface Empty {

    }

    private interface S<E> extends Empty {

        E getE();

        void saveE(E e);

    }

    private abstract static class AS<E> implements S<E> {

        @Override
        public E getE() {
            return null;
        }

    }

    private static class ASImpl extends AS<String> {

        @Override
        public String getE() {
            return null;
        }

        @Override
        public void saveE(String s) {

        }
    }

    private static class SImpl implements S<String> {

        @Override
        public String getE() {
            return null;
        }

        @Override
        public void saveE(String s) {

        }
    }

    private interface SS<T, E> extends S<E>, Empty {

        T getT();

        void saveAll(T t, E e);

        void saveT(T t);

        @Override
        default void saveE(E e) {

        }
    }

    private abstract static class ASS implements SS<String, Integer> {

    }

    private static class ASSImpl extends ASS {

        @Override
        public Integer getE() {
            return null;
        }

        @Override
        public void saveE(Integer integer) {

        }

        @Override
        public String getT() {
            return null;
        }

        @Override
        public void saveAll(String s, Integer integer) {

        }

        @Override
        public void saveT(String s) {

        }
    }

    private interface SSI extends SS<Float, String> {

    }

    private static class SSIImpl implements SSI {

        @Override
        public String getE() {
            return null;
        }

        @Override
        public void saveE(String s) {

        }

        @Override
        public Float getT() {
            return null;
        }

        @Override
        public void saveAll(Float aFloat, String s) {

        }

        @Override
        public void saveT(Float aFloat) {

        }
    }

    private static class SSImpl implements SS<Float, String> {

        @Override
        public String getE() {
            return null;
        }

        @Override
        public void saveE(String s) {

        }

        @Override
        public Float getT() {
            return null;
        }

        @Override
        public void saveAll(Float aFloat, String s) {

        }

        @Override
        public void saveT(Float aFloat) {

        }
    }

    private static class SubSSImpl extends SSImpl {

    }


    private abstract static class VEmpty {

        @Override
        public String toString() {
            return super.toString();
        }
    }

    private abstract static class V<E> extends VEmpty {

        public E getE() {
            return null;
        }

        public void saveE(E e) {

        }

        @Override
        public String toString() {
            return super.toString();
        }
    }

    private abstract static class AV<E> extends V<E> {

    }

    private static class AVImpl extends AV<String> {

        @Override
        public String getE() {
            return null;
        }

        @Override
        public void saveE(String s) {

        }
    }

    private abstract static class VV<T, E> extends V<E> {

        public T getT() {
            return null;
        }

        public void saveAll(T t, E e) {

        }

        public void saveT(T t) {

        }

        @Override
        public String toString() {
            return super.toString();
        }
    }

    private abstract static class AVV extends VV<String, Integer> {

        @Override
        public String toString() {
            return super.toString();
        }
    }

    private static class AVVImpl extends AVV {

        @Override
        public Integer getE() {
            return null;
        }

        @Override
        public String getT() {
            return null;
        }

        @Override
        public void saveAll(String s, Integer integer) {

        }

        @Override
        public String toString() {
            return super.toString();
        }
    }

    private static class SubAVVImpl extends AVVImpl {

        @Override
        public void saveE(Integer integer) {
            super.saveE(integer);
        }

        @Override
        public void saveT(String s) {
            super.saveT(s);
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }

}
