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

import java.lang.annotation.*;

import static org.junit.jupiter.api.Assertions.*;

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
    void testFindAnnotation() {
        assertNull(AnnotationUtils.findAnnotation(null, A.class));
        assertNull(AnnotationUtils.findAnnotation(S.class, null));
        final A ann = AnnotationUtils.findAnnotation(S.class, A.class);
        assertNotNull(ann);
        assertEquals(1, ann.value());
    }

    @C
    private static class S {

    }


    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE})
    @Documented
    @interface A {
        int value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE})
    @Documented
    @interface B {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE})
    @Documented
    @A(1)
    @B
    @interface C {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE})
    @Documented
    @interface D {
    }

}
