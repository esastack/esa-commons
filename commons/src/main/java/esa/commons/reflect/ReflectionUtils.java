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
import esa.commons.ExceptionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * ReflectionUtils
 */
public final class ReflectionUtils {

    /**
     * Get all declared methods of this target and all the methods of its superclasses and implemented interfaces;
     * Methods of Object are excluded.
     *
     * @param target the target Class
     */
    public static List<Method> getAllDeclaredMethods(Class<?> target) {
        List<Method> allMethods = new LinkedList<>();
        ClassUtils.doWithMethods(target, allMethods::add, method -> !method.getDeclaringClass().equals(Object.class));
        return allMethods;
    }

    public static List<Field> getAllDeclaredFields(Class<?> target) {
        List<Field> allFields = new LinkedList<>();

        for (Class<?> clazz = target; clazz != null; clazz = clazz.getSuperclass()) {
            Field[] methods = clazz.getDeclaredFields();
            allFields.addAll(Arrays.asList(methods));
        }
        return allFields;
    }

    public static void makeMethodAccessible(Method method) {
        if (isNotAccessible(method) && !method.isAccessible()) {
            method.setAccessible(true);
        }
    }

    public static void makeFieldAccessible(Field field) {
        if (isNotAccessible(field) && !field.isAccessible()) {
            field.setAccessible(true);
        }
    }

    public static void makeConstructorAccessible(Constructor<?> ctor) {
        if (isNotAccessible(ctor) && !ctor.isAccessible()) {
            ctor.setAccessible(true);
        }
    }

    private static boolean isNotAccessible(Member member) {
        return (!Modifier.isPublic(member.getModifiers()) ||
                !Modifier.isPublic(member.getDeclaringClass().getModifiers()));
    }

    public static <T> Constructor<T> accessibleConstructor(Class<T> clazz,
                                                           Class<?>... parameterTypes)
            throws NoSuchMethodException {
        Constructor<T> ctor = clazz.getDeclaredConstructor(parameterTypes);
        makeConstructorAccessible(ctor);
        return ctor;
    }

    public static Object invokeMethod(Method method, Object target, Object... args) {
        return invokeMethod(method, target, true, args);
    }

    @Deprecated
    public static Object invokeMethod(Method method, Object target, boolean force, Object... args) {
        try {
            if (force) {
                makeMethodAccessible(method);
            }
            return method.invoke(target, args);
        } catch (Exception e) {
            ExceptionUtils.throwException(e);
            // never reach
            return null;
        }
    }

    public static boolean isStatic(Method method) {
        return Modifier.isStatic(method.getModifiers());
    }

    public static boolean isGetter(Method method) {
        if (method == null) {
            return false;
        }
        String name = method.getName();
        return name.length() > 2
                && !isStatic(method)
                && method.getParameterTypes().length == 0
                && ((name.length() > 3
                && name.startsWith("get")
                && Character.isUpperCase(name.charAt(3)))
                || (name.startsWith("is")
                && Character.isUpperCase(name.charAt(2))
                && method.getReturnType().equals(boolean.class)));
    }

    public static boolean isSetter(Method method) {
        if (method == null) {
            return false;
        }
        String name = method.getName();
        return name.length() > 3
                && !isStatic(method)
                && method.getParameterTypes().length == 1
                && name.startsWith("set")
                && Character.isUpperCase(name.charAt(3));
    }

    public static Method getSetter(Field field) {
        if (field == null) {
            return null;
        }
        List<Method> methods = getAllDeclaredMethods(field.getDeclaringClass());
        for (Method method : methods) {
            String fieldName = field.getName();
            String methodName = method.getName();
            if (isSetter(method)
                    && fieldName.toUpperCase().charAt(0) == methodName.charAt(3)
                    && (fieldName.length() == 1
                    || methodName.endsWith(fieldName.substring(1)))
                    && method.getParameterTypes()[0].equals(field.getType())) {
                return method;
            }
        }
        return null;
    }

    public static Method getGetter(Field field) {
        if (field == null) {
            return null;
        }
        List<Method> methods = getAllDeclaredMethods(field.getDeclaringClass());
        for (Method method : methods) {
            String fieldName = field.getName();
            String methodName = method.getName();
            if (isGetter(method)
                    && (fieldName.toUpperCase().charAt(0)
                    == methodName.charAt((methodName.length() > 3 && !field.getType().equals(boolean.class)) ? 3 : 2))
                    && (fieldName.length() == 1
                    || methodName.endsWith(fieldName.substring(1)))
                    && method.getReturnType().equals(field.getType())) {
                return method;
            }
        }
        return null;
    }

    private ReflectionUtils() {
    }
}
