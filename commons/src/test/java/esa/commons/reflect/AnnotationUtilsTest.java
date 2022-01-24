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

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AnnotationUtilsTest {

    @Test
    void testHasAnnotation() {
        assertTrue(AnnotationUtils.hasAnnotation(S.class, A.class));
        assertTrue(AnnotationUtils.hasAnnotation(S.class, B.class));
        assertTrue(AnnotationUtils.hasAnnotation(S.class, C.class));
        assertFalse(AnnotationUtils.hasAnnotation(S.class, D.class));
        assertFalse(AnnotationUtils.hasAnnotation(null, D.class));
        assertFalse(AnnotationUtils.hasAnnotation(S.class, null));
    }

    @Test
    void testHasAnnotationOfMethod() throws Throwable {
        final Method method = M2.class.getMethod("sayHello", Object.class);

        assertFalse(AnnotationUtils.hasAnnotation(method, null, true));
        assertFalse(AnnotationUtils.hasAnnotation(method, null, false));
        assertFalse(AnnotationUtils.hasAnnotation((Method) null, A.class, true));
        assertFalse(AnnotationUtils.hasAnnotation((Method) null, A.class, false));

        assertTrue(AnnotationUtils.hasAnnotation(method, A.class, false));
        assertTrue(AnnotationUtils.hasAnnotation(method, B.class, false));
        assertTrue(AnnotationUtils.hasAnnotation(method, C.class, false));
        assertFalse(AnnotationUtils.hasAnnotation(method, D.class, false));

        assertTrue(AnnotationUtils.hasAnnotation(method, A.class, true));
        assertTrue(AnnotationUtils.hasAnnotation(method, B.class, true));
        assertTrue(AnnotationUtils.hasAnnotation(method, C.class, true));
        assertTrue(AnnotationUtils.hasAnnotation(method, D.class, true));
    }

    @Test
    void testHasAnnotationOfClass() {
        assertFalse(AnnotationUtils.hasAnnotation(S.class, null, true));
        assertFalse(AnnotationUtils.hasAnnotation(S.class, null, false));
        assertFalse(AnnotationUtils.hasAnnotation((Class<?>) null, A.class, true));
        assertFalse(AnnotationUtils.hasAnnotation((Class<?>) null, A.class, false));

        assertFalse(AnnotationUtils.hasAnnotation(Y.class, A.class, false));
        assertFalse(AnnotationUtils.hasAnnotation(Y.class, B.class, false));
        assertFalse(AnnotationUtils.hasAnnotation(Y.class, C.class, false));
        assertFalse(AnnotationUtils.hasAnnotation(Y.class, D.class, false));

        assertTrue(AnnotationUtils.hasAnnotation(Y.class, A.class, true));
        assertTrue(AnnotationUtils.hasAnnotation(Y.class, B.class, true));
        assertTrue(AnnotationUtils.hasAnnotation(Y.class, C.class, true));
        assertTrue(AnnotationUtils.hasAnnotation(Y.class, D.class, true));
    }

    @Test
    void testFindAnyAnnotation() {
        assertNull(AnnotationUtils.findAnyAnnotation(null, new Class[]{A.class, D.class}));
        assertNull(AnnotationUtils.findAnyAnnotation(S.class, null));
        assertNull(AnnotationUtils.findAnyAnnotation(S.class, new Class[0]));
        final Annotation ann = AnnotationUtils.findAnyAnnotation(S.class, new Class[]{A.class, D.class});
        assertNotNull(ann);
        assertTrue(ann instanceof A);
        assertEquals(1, ((A) ann).value());
    }

    @Test
    void testFindAnyAnnotationOfMethod() throws Throwable {
        final Method method1 = M3.class.getMethod("sayHello", String.class);
        final Annotation ann1 = AnnotationUtils.findAnyAnnotation(method1,
                new Class[]{A.class, B.class, C.class, D.class}, true);
        assertTrue(ann1 instanceof A);
        assertEquals(1, ((A) ann1).value());

        final Annotation ann2 = AnnotationUtils.findAnyAnnotation(method1,
                new Class[]{A.class, B.class, C.class, D.class}, false);
        assertNull(ann2);

        final Annotation ann3 = AnnotationUtils.findAnyAnnotation(method1,
                new Class[]{C.class, D.class}, true);
        assertTrue(ann3 instanceof C);
        assertEquals("m2", ((C) ann3).value());

        final Annotation ann4 = AnnotationUtils.findAnyAnnotation(method1,
                new Class[]{D.class}, true);
        assertTrue(ann4 instanceof D);
        assertEquals("m1", ((D) ann4).value());

        final Method method2 = M2.class.getMethod("sayHello", Object.class);
        final Annotation ann5 = AnnotationUtils.findAnyAnnotation(method2,
                new Class[]{A.class, B.class, C.class, D.class}, true);
        assertTrue(ann5 instanceof A);
        assertEquals(1, ((A) ann5).value());
    }

    @Test
    void testFindAnyAnnotationOfClass() {
        assertNull(AnnotationUtils.findAnyAnnotation(Y.class,
                new Class[]{A.class, B.class, C.class, D.class}, false));

        final Annotation ann1 = AnnotationUtils.findAnyAnnotation(Y.class,
                new Class[]{A.class, B.class, C.class, D.class}, true);
        assertTrue(ann1 instanceof A);
        assertEquals(1, ((A) ann1).value());


        final Annotation ann2 = AnnotationUtils.findAnyAnnotation(Y.class,
                new Class[]{B.class, C.class, D.class}, true);
        assertTrue(ann2 instanceof B);

        final Annotation ann3 = AnnotationUtils.findAnyAnnotation(Y.class,
                new Class[]{C.class, D.class}, true);
        assertTrue(ann3 instanceof C);

        final Annotation ann4 = AnnotationUtils.findAnyAnnotation(Y.class,
                new Class[]{D.class}, true);
        assertTrue(ann4 instanceof D);
    }

    @Test
    void testFindAnnotation() {
        assertNull(AnnotationUtils.findAnnotation(null, A.class));
        assertNull(AnnotationUtils.findAnnotation(S.class, null));
        final A ann = AnnotationUtils.findAnnotation(S.class, A.class);
        assertNotNull(ann);
        assertEquals(1, ann.value());
    }

    @Test
    void testFindAnnotationOfMethod() throws Exception {
        final Method method1 = M1.class.getMethod("sayHello", Object.class);
        assertNull(AnnotationUtils.findAnnotation(method1, A.class, false));
        assertNull(AnnotationUtils.findAnnotation(method1, B.class, false));
        assertNull(AnnotationUtils.findAnnotation(method1, C.class, false));
        assertNotNull(AnnotationUtils.findAnnotation(method1, D.class, false));

        assertNull(AnnotationUtils.findAnnotation(method1, A.class, true));
        assertNull(AnnotationUtils.findAnnotation(method1, B.class, true));
        assertNull(AnnotationUtils.findAnnotation(method1, C.class, true));
        assertNotNull(AnnotationUtils.findAnnotation(method1, D.class, true));


        final Method method2 = M2.class.getMethod("sayHello", Object.class);
        assertNotNull(AnnotationUtils.findAnnotation(method2, A.class, false));
        assertNotNull(AnnotationUtils.findAnnotation(method2, B.class, false));
        assertNotNull(AnnotationUtils.findAnnotation(method2, C.class, false));
        assertNull(AnnotationUtils.findAnnotation(method2, D.class, false));

        assertNotNull(AnnotationUtils.findAnnotation(method2, A.class, true));
        assertNotNull(AnnotationUtils.findAnnotation(method2, B.class, true));
        assertNotNull(AnnotationUtils.findAnnotation(method2, C.class, true));
        assertNotNull(AnnotationUtils.findAnnotation(method2, D.class, true));


        final Method method3 = M3.class.getMethod("sayHello", String.class);
        assertNull(AnnotationUtils.findAnnotation(method3, A.class, false));
        assertNull(AnnotationUtils.findAnnotation(method3, B.class, false));
        assertNull(AnnotationUtils.findAnnotation(method3, C.class, false));
        assertNull(AnnotationUtils.findAnnotation(method3, D.class, false));

        assertNotNull(AnnotationUtils.findAnnotation(method3, A.class, true));
        assertNotNull(AnnotationUtils.findAnnotation(method3, B.class, true));
        assertNotNull(AnnotationUtils.findAnnotation(method3, C.class, true));
        assertNotNull(AnnotationUtils.findAnnotation(method3, D.class, true));
    }

    @Test
    void testFindAnnotationOfClass() {
        assertNull(AnnotationUtils.findAnnotation(X.class, A.class, false));
        assertNull(AnnotationUtils.findAnnotation(X.class, B.class, false));
        assertNull(AnnotationUtils.findAnnotation(X.class, C.class, false));
        assertNull(AnnotationUtils.findAnnotation(X.class, D.class, false));

        assertNotNull(AnnotationUtils.findAnnotation(X.class, A.class, true));
        assertNotNull(AnnotationUtils.findAnnotation(X.class, B.class, true));
        assertNotNull(AnnotationUtils.findAnnotation(X.class, C.class, true));
        assertNotNull(AnnotationUtils.findAnnotation(X.class, D.class, true));


        assertNull(AnnotationUtils.findAnnotation(Y.class, A.class, false));
        assertNull(AnnotationUtils.findAnnotation(Y.class, B.class, false));
        assertNull(AnnotationUtils.findAnnotation(Y.class, C.class, false));
        assertNull(AnnotationUtils.findAnnotation(Y.class, D.class, false));

        assertNotNull(AnnotationUtils.findAnnotation(Y.class, A.class, true));
        assertNotNull(AnnotationUtils.findAnnotation(Y.class, B.class, true));
        assertNotNull(AnnotationUtils.findAnnotation(Y.class, C.class, true));
        assertNotNull(AnnotationUtils.findAnnotation(Y.class, D.class, true));
    }

    @C
    private static class S {

    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE, ElementType.METHOD})
    @Documented
    @interface A {
        int value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE, ElementType.METHOD})
    @Documented
    @interface B {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE, ElementType.METHOD})
    @Documented
    @A(1)
    @B
    @interface C {
        String value() default "";
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE, ElementType.METHOD})
    @Documented
    @interface D {
        String value() default "";
    }

    @D
    private interface XX {

    }

    private static class X extends S implements XX {

    }

    private static class Y extends X {

    }

    private interface M1<R> {

        @D("m1")
        void sayHello(R r);

    }

    private abstract static class M2<R> implements M1<R> {

        @C(value = "m2")
        @Override
        public void sayHello(R r) {

        }
    }

    private static class M3 extends M2<String> {

        @Override
        public void sayHello(String s) {

        }
    }

}
