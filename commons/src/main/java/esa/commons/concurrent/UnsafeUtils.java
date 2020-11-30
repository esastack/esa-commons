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
package esa.commons.concurrent;

import esa.commons.Checks;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.security.PrivilegedExceptionAction;

/**
 * Unity class for {@link Unsafe} accessing.
 */
public final class UnsafeUtils {

    private static final Unsafe UNSAFE;

    public static boolean hasUnsafe() {
        return UNSAFE != null;
    }

    public static Unsafe getUnsafe() {
        return UNSAFE;
    }

    public static long objectFieldOffset(Class<?> target, String field) {
        try {
            return UNSAFE.objectFieldOffset(target.getDeclaredField(field));
        } catch (NoSuchFieldException e) {
            throw new Error("Could not find field named " +
                    field + " in class " + target.getName(), e);
        }
    }

    public static void throwException(Throwable t) {
        Checks.checkNotNull(t);
        UNSAFE.throwException(t);
    }

    static {
        Unsafe theUnsafe = null;
        try {
            theUnsafe = getTheUnsafe();
        } catch (Throwable ignored) {
        }
        UNSAFE = theUnsafe;
    }

    private static Unsafe getTheUnsafe() {
        try {
            return Unsafe.getUnsafe();
        } catch (SecurityException ignored) {
        }
        try {
            return java.security.AccessController.doPrivileged((PrivilegedExceptionAction<Unsafe>) () -> {
                Class<Unsafe> k = Unsafe.class;
                for (Field f : k.getDeclaredFields()) {
                    f.setAccessible(true);
                    Object x = f.get(null);
                    if (k.isInstance(x)) {
                        return k.cast(x);
                    }
                }
                throw new NoSuchFieldError("the Unsafe");
            });
        } catch (java.security.PrivilegedActionException e) {
            throw new RuntimeException("Could not initialize intrinsics",
                    e.getCause());
        }
    }

    private UnsafeUtils() {
    }
}
