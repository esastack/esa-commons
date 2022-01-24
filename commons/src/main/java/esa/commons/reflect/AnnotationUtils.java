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

import esa.commons.ClassUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * Unity class of java annotations.
 */
public final class AnnotationUtils {

    /**
     * Checks whether the given annotation is present on the given {@code element}. This method will search annotations
     * that is annotated on the presenting annotations except the annotations that declared as {@code
     * java.lang.annotation.Xxx}.
     *
     * @param element element to check
     * @param target  target annotation type
     *
     * @return {@code true} if given annotation is present, {@code false} otherwise.
     */
    public static boolean hasAnnotation(AnnotatedElement element,
                                        Class<? extends Annotation> target) {
        if (element == null || target == null) {
            return false;
        }
        return findAnnotation(element, target) != null;
    }

    /**
     * Checks whether the given annotation is present on the given {@code element}.
     *
     * @param element   element to check
     * @param target    target annotation type
     * @param recursive whether support to find on {@code target}'s super class or interfaces recursively.
     * @return  {@code true} if given annotation is present, {@code false} otherwise.
     */
    public static boolean hasAnnotation(Class<?> element,
                                        Class<? extends Annotation> target,
                                        boolean recursive) {
        if (element == null || target == null) {
            return false;
        }
        return findAnnotation(element, target, recursive) != null;
    }

    /**
     * Checks whether the given annotation is present on the given {@code element}.
     *
     * @param element   element to check
     * @param target    target annotation type
     * @param recursive whether support to find on {@code target}'s super class or interfaces recursively.
     * @return  {@code true} if given annotation is present, {@code false} otherwise.
     */
    public static boolean hasAnnotation(Method element,
                                        Class<? extends Annotation> target,
                                        boolean recursive) {
        if (element == null || target == null) {
            return false;
        }
        return findAnnotation(element, target, recursive) != null;
    }

    /**
     * Finds an annotation from given {@code element} if any type of {@code targets} matches.This method will search
     * annotations that is annotated on the presenting annotations except the annotations that declared as {@code
     * java.lang.annotation.Xxx}.
     *
     * @param element element to find from
     * @param targets target annotations types
     * @param <A>     annotation type
     *
     * @return first matched annotation or {@code null} if mismatched.
     */
    public static <A extends Annotation> A findAnyAnnotation(AnnotatedElement element,
                                                             Class<A>[] targets) {
        if (element == null || targets == null || targets.length == 0) {
            return null;
        }
        for (Class<A> target : targets) {
            A found = findAnnotationRecursively(element, target, new HashSet<>());
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    /**
     * Finds an annotation from given {@code element} if any type of {@code targets} matches.This method will search
     * annotations that is annotated on the presenting annotations except the annotations that declared as {@code
     * java.lang.annotation.Xxx}.
     *
     * @param element   element to find from
     * @param targets   target annotations types
     * @param recursive recursive whether support to find on {@code target}'s super class or interfaces recursively.
     * @param <A>     annotation type
     *
     * @return first matched annotation or {@code null} if mismatched.
     */
    public static <A extends Annotation> A findAnyAnnotation(Class<?> element,
                                                             Class<A>[] targets,
                                                             boolean recursive) {
        if (element == null || targets == null || targets.length == 0 || Object.class.equals(element)) {
            return null;
        }
        A found;
        if ((found = findAnyAnnotation(element, targets)) != null) {
            return found;
        }
        if (!recursive) {
            return null;
        }
        if ((found = findAnyAnnotation(element.getSuperclass(), targets, true)) != null) {
            return found;
        }
        for (Class<?> interface0 : element.getInterfaces()) {
            if ((found = findAnyAnnotation(interface0, targets, true)) != null) {
                return found;
            }
        }

        return null;
    }

    /**
     * Finds an annotation from given {@code element} if any type of {@code targets} matches.This method will search
     * annotations that is annotated on the presenting annotations except the annotations that declared as {@code
     * java.lang.annotation.Xxx}.
     *
     * @param element   element to find from
     * @param targets   target annotations types
     * @param recursive recursive whether support to find on {@code target}'s super class or interfaces recursively.
     * @param <A>     annotation type
     *
     * @return first matched annotation or {@code null} if mismatched.
     */
    public static <A extends Annotation> A findAnyAnnotation(Method element,
                                                             Class<A>[] targets,
                                                             boolean recursive) {
        if (element == null || targets == null || targets.length == 0) {
            return null;
        }
        A found;
        if ((found = findAnyAnnotation(element, targets)) != null) {
            return found;
        }

        if (!recursive) {
            return null;
        }

        for (Method method : ClassUtils.findImplementedMethods(element)) {
            if ((found = findAnyAnnotation(method, targets)) != null) {
                return found;
            }
        }

        return null;
    }


    /**
     * Finds the annotation from given {@code element} if type of {@code target} matches.This method will search
     * annotations that is annotated on the presenting annotations except the annotations that declared as {@code
     * java.lang.annotation.Xxx}.
     *
     * @param element element to find from
     * @param target  target annotation type
     * @param <A>     annotation type
     *
     * @return first matched annotation or {@code null} if mismatched.
     */
    public static <A extends Annotation> A findAnnotation(AnnotatedElement element,
                                                          Class<A> target) {
        if (element == null || target == null) {
            return null;
        }
        return findAnnotationRecursively(element, target, new HashSet<>());
    }

    /**
     * Finds the annotation from given {@code element} if type of {@code target} matches.This method will search
     * annotations that is annotated on the presenting annotations except the annotations that declared as {@code
     * java.lang.annotation.Xxx}.
     *
     * @param element    element to find from
     * @param target     target annotation type
     * @param recursive  whether support to find on {@code target}'s super class or interfaces recursively.
     * @param <A>        annotation type
     *
     * @return first matched annotation or {@code null} if mismatched.
     */
    public static <A extends Annotation> A findAnnotation(Class<?> element,
                                                          Class<A> target,
                                                          boolean recursive) {
        if (target == null || element == null || Object.class.equals(element)) {
            return null;
        }

        A found;
        if ((found = findAnnotation(element, target)) != null) {
            return found;
        }
        if (!recursive) {
            return null;
        }
        if ((found = findAnnotation(element.getSuperclass(), target, true)) != null) {
            return found;
        }
        for (Class<?> interface0 : element.getInterfaces()) {
            if ((found = findAnnotation(interface0, target, true)) != null) {
                return found;
            }
        }
        return null;
    }

    /**
     * Finds the annotation from given {@code element} if type of {@code target} matches.This method will search
     * annotations that is annotated on the presenting annotations except the annotations that declared as {@code
     * java.lang.annotation.Xxx}.
     *
     * @param element    element to find from
     * @param target     target annotation type
     * @param recursive  whether support to find on {@code target}'s super class or interfaces recursively.
     * @param <A>        annotation type
     *
     * @return first matched annotation or {@code null} if mismatched.
     */
    public static <A extends Annotation> A findAnnotation(Method element,
                                                          Class<A> target,
                                                          boolean recursive) {
        if (element == null || target == null) {
            return null;
        }

        A found;
        if ((found = findAnnotation(element, target)) != null) {
            return found;
        }
        if (!recursive) {
            return null;
        }
        for (Method method : ClassUtils.findImplementedMethods(element)) {
            if ((found = findAnnotation(method, target)) != null) {
                return found;
            }
        }
        return null;
    }

    private static <A extends Annotation> A findAnnotationRecursively(AnnotatedElement element,
                                                                      Class<A> target,
                                                                      Set<Annotation> filtered) {

        A annotation = element.getDeclaredAnnotation(target);
        if (annotation != null) {
            return annotation;
        }

        for (Annotation declaredAnn : element.getDeclaredAnnotations()) {
            Class<? extends Annotation> type = declaredAnn.annotationType();
            if (filtered.add(declaredAnn) && !type.getName().startsWith("java.lang.annotation")) {
                annotation = findAnnotationRecursively(type, target, filtered);
                if (annotation != null) {
                    return annotation;
                }
            }
        }
        return null;
    }

    private AnnotationUtils() {
    }
}
