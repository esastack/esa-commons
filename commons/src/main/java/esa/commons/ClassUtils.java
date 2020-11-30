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

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

public final class ClassUtils {

    private static final Class<?>[] EMPTY_CLZ_ARR = new Class<?>[0];

    /**
     * Indicates the given class exists or not.
     *
     * @param clz class
     *
     * @return {@code true} if given class exists, otherwise {@code false}
     */
    public static boolean hasClass(String clz) {
        return forName(clz, false) != null;
    }

    /**
     * @see #forName(String, boolean)
     */
    public static Class<?> forName(String clz) {
        return forName(clz, true);
    }

    /**
     * Returns the Class object associated with the class or interface with the given string name
     *
     * @param clz        name oInternalLoggersf class
     * @param initialize if {@code true} the class will be initialized.
     *
     * @return class object or {@code null} if given class does not exists
     */
    public static Class<?> forName(String clz, boolean initialize) {
        if (StringUtils.isEmpty(clz)) {
            return null;
        }
        ClassLoader cl = getClassLoader();
        try {
            return Class.forName(clz, initialize, cl);
        } catch (ClassNotFoundException ignored) {
        }
        return null;
    }

    /**
     * Returns the {@link ClassLoader} associates with current thread or {@link ClassLoader} associates with current
     * class({@link ClassUtils}) if we could not get {@link ClassLoader} from current thread.
     *
     * @return class loader
     */
    public static ClassLoader getClassLoader() {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable ignored) {
        }
        if (cl == null) {
            cl = ClassUtils.class.getClassLoader();
        }
        return cl;
    }

    /**
     * Gets the raw type of give {@code type}.
     *
     * @param type target type
     *
     * @return raw type of {@link Class}
     */
    public static Class<?> getRawType(Type type) {
        if (type == null) {
            return null;
        }
        if (type instanceof Class<?>) {
            return (Class<?>) type;
        }
        if (type instanceof ParameterizedType) {
            ParameterizedType t = (ParameterizedType) type;
            Type raw = t.getRawType();
            if (raw instanceof Class<?>) {
                return (Class<?>) raw;
            }
        }
        return null;
    }

    /**
     * Retrieve the generic types of the given {@link Type}.
     *
     * @param requiredType requiredType
     *
     * @return {@link Type} of the first type, {@link Object} if could not find a actual {@link Type} such as {@code T}.
     */
    public static Class<?>[] retrieveGenericTypes(Type requiredType) {
        return doRetrieveGenericTypes(requiredType, null);
    }

    /**
     * Retrieve the first generic type of the given {@link Type}. For instance, this method will return {@link Integer}
     * if passed a {@link Type} of list. Aso, this method will return {@link String} if passed a {@link Type} of map.
     *
     * @param requiredType requiredType
     *
     * @return {@link Type} of the first type, {@link Object} if could not find a actual {@link Type} such as {@code T}.
     */
    public static Optional<Class<?>> retrieveFirstGenericType(Type requiredType) {
        Class<?>[] elementTypes = retrieveGenericTypes(requiredType);
        return elementTypes != null && elementTypes.length > 0 ? Optional.of(elementTypes[0]) : Optional.empty();
    }

    /**
     * @see #findFirstGenericType(Class, Class)
     */
    public static Optional<Class<?>> findFirstGenericType(Class<?> concrete) {
        return findFirstGenericType(concrete, null);
    }

    /**
     * @see #findGenericTypes(Class, Class)
     */
    public static Optional<Class<?>> findFirstGenericType(Class<?> concrete, Class<?> interfaceType) {
        Class<?>[] elementTypes = findGenericTypes(concrete, interfaceType);
        return elementTypes != null && elementTypes.length > 0 ? Optional.of(elementTypes[0]) : Optional.empty();
    }

    /**
     * @see #findGenericTypes(Class, Class)
     */
    public static Class<?>[] findGenericTypes(Class<?> concrete) {
        return findGenericTypes(concrete, null);
    }

    /**
     * Finds generic type declared in given target raw type of the given {@link Class} with find semantic which will try
     * to find the generic types from the supper class and the interfaces of super class recursively.
     *
     * @param concrete      real type
     * @param targetRawType target raw type, {@code null} if no required target raw type.
     *
     * @return generic types found
     */
    public static Class<?>[] findGenericTypes(Class<?> concrete,
                                              Class<?> targetRawType) {
        Checks.checkNotNull(concrete, "concrete");
        if (Object.class.equals(concrete)) {
            return EMPTY_CLZ_ARR;
        }
        // from interface
        Class<?>[] generics = doRetrieveGenericInterfaceTypes(concrete, targetRawType);
        if (generics != null && generics.length > 0) {
            return generics;
        }

        // from super class
        Type genericSuperclass = concrete.getGenericSuperclass();
        generics = doRetrieveGenericTypes(genericSuperclass, targetRawType);
        if (generics != null && generics.length > 0) {
            return generics;
        }

        // from interface of super class
        Class<?> supperClass = concrete.getSuperclass();
        generics = findGenericTypes(supperClass, targetRawType);

        if (generics == null || generics.length == 0) {
            return EMPTY_CLZ_ARR;
        }
        return generics;
    }

    private static Class<?>[] doRetrieveGenericInterfaceTypes(Class<?> concrete, Class<?> interfaceType) {
        if (Object.class.equals(concrete)) {
            return null;
        }
        Type[] types = concrete.getGenericInterfaces();
        if (types != null && types.length > 0) {
            for (Type type : types) {
                Class<?>[] generics = doRetrieveGenericTypes(type, interfaceType);
                if (generics.length > 0) {
                    return generics;
                }
            }

            // retrieve from interface
            Class<?>[] superInterfaces = concrete.getInterfaces();
            if (superInterfaces != null && superInterfaces.length > 0) {
                for (Class<?> type : superInterfaces) {
                    Class<?>[] generics = doRetrieveGenericInterfaceTypes(type, interfaceType);
                    if (generics != null) {
                        return generics;
                    }
                }
            }
        }
        return null;
    }

    private static Class<?>[] doRetrieveGenericTypes(Type requiredType, Class<?> rawType) {
        Checks.checkNotNull(requiredType);
        Class<?>[] elementTypes = null;
        if (requiredType instanceof ParameterizedType) {
            ParameterizedType type = (ParameterizedType) requiredType;
            if (rawType == null || rawType.equals(type.getRawType())) {
                Type[] maybeTypeVariable = type.getActualTypeArguments();
                if (maybeTypeVariable != null && maybeTypeVariable.length > 0) {
                    elementTypes = new Class<?>[maybeTypeVariable.length];
                    for (int i = 0; i < maybeTypeVariable.length; i++) {
                        if (maybeTypeVariable[i] instanceof Class<?>) {
                            elementTypes[i] = (Class<?>) maybeTypeVariable[i];
                        } else if (maybeTypeVariable[i] instanceof ParameterizedType) {
                            Type raw = ((ParameterizedType) maybeTypeVariable[i]).getRawType();
                            if (raw instanceof Class<?>) {
                                elementTypes[i] = (Class<?>) raw;
                            } else {
                                elementTypes[i] = Object.class;
                            }
                        } else {
                            elementTypes[i] = Object.class;
                        }
                    }
                }
            }
        }
        return elementTypes == null ? EMPTY_CLZ_ARR : elementTypes;
    }

    public static Class<?> getUserType(Object target) {
        if (target == null) {
            return null;
        }
        if (target instanceof Class<?>) {
            return (Class<?>) target;
        }
        return getUserType(target.getClass());
    }

    public static Class<?> getUserType(Class<?> clz) {
        if (clz.getName().contains("$$")) {
            Class<?> superclass = clz.getSuperclass();
            if (superclass != null && superclass != Object.class) {
                return superclass;
            }
        }
        return clz;
    }

    public static void doWithUserDeclaredMethods(Class<?> clz,
                                                 Consumer<Method> c,
                                                 Predicate<Method> p) {
        Predicate<Method> predicate = userDeclared();
        if (p != null) {
            predicate = predicate.and(p);
        }
        doWithMethods(clz, c, predicate);
    }

    public static Set<Method> userDeclaredMethods(Class<?> clz) {
        return userDeclaredMethods(clz, null);
    }

    public static Set<Method> userDeclaredMethods(Class<?> clz, Predicate<Method> p) {
        Predicate<Method> predicate = userDeclared();
        if (p != null) {
            predicate = predicate.and(p);
        }
        Set<Method> methods = new LinkedHashSet<>();
        doWithMethods(clz, methods::add, predicate);
        return methods;
    }

    private static Predicate<Method> userDeclared() {
        return method -> (!method.isBridge()
                && !method.isSynthetic()
                && method.getDeclaringClass() != Object.class);
    }

    public static void doWithMethods(Class<?> clz,
                                     Consumer<Method> c,
                                     Predicate<Method> p) {
        clz = getUserType(clz);
        Method[] methods = clz.getDeclaredMethods();
        for (Method method : methods) {
            if (p != null && !p.test(method)) {
                continue;
            }
            c.accept(method);
        }
        if (clz.getSuperclass() != null) {
            doWithMethods(clz.getSuperclass(), c, p);
        } else if (clz.isInterface()) {
            for (Class<?> superIfc : clz.getInterfaces()) {
                doWithMethods(superIfc, c, p);
            }
        }
    }

    /**
     * @deprecated use {@link #doWithUserDeclaredMethods(Class, Consumer, Predicate)}
     */
    @Deprecated
    public static void doWithUserDeclaredMethodsMethods(Class<?> clz,
                                                        Consumer<Method> c,
                                                        Predicate<Method> p) {
        doWithUserDeclaredMethods(clz, c, p);
    }

    private ClassUtils() {
    }
}
