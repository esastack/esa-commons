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

import esa.commons.Checks;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;

import static esa.commons.reflect.ReflectionUtils.makeFieldAccessible;

/**
 * Unity class of beans.
 */
public final class BeanUtils {

    public static void copyProperties(Object source, Map<String, Object> target) {
        if (source == null || target == null) {
            return;
        }

        Class<?> clz = (source instanceof Class) ? (Class<?>) source : source.getClass();
        Field[] declaredFields = clz.getDeclaredFields();
        for (Field field : declaredFields) {
            if (!Modifier.isStatic(field.getModifiers())) {
                String name = field.getName();
                Object value = getFieldValue(source, name);
                target.put(name, value);
            }
        }
    }

    public static Object getFieldValue(Object object, String fieldName) {
        Checks.checkNotNull(object, "object");
        Field field = findField(object, fieldName);

        makeFieldAccessible(field);

        try {
            return field.get(object);
        } catch (IllegalAccessException ignored) {
        }
        return null;
    }

    public static void setFieldValue(Object object, String fieldName, Object value) {
        Checks.checkNotNull(object, "object");
        Field field = findField(object, fieldName);
        makeFieldAccessible(field);

        try {
            field.set(object, value);
        } catch (IllegalAccessException ignored) {
        }
    }

    private static Field findField(Object object, final String fieldName) {
        Class<?> clz = object.getClass();
        for (; clz != Object.class; clz = clz.getSuperclass()) {
            try {
                return clz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException ignored) {
            }
        }
        throw new IllegalArgumentException("Could not find field '" + fieldName +
                "' in '" + object.getClass().getName() + "'");
    }

    private BeanUtils() {
    }
}
