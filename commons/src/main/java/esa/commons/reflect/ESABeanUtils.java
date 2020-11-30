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

import java.util.Map;

/**
 * @deprecated use {@link BeanUtils} please
 */
@Deprecated
public final class ESABeanUtils {

    public static void copyProperties(Object source, Map<String, Object> target) {
        BeanUtils.copyProperties(source, target);
    }

    public static Object getFieldValue(final Object object, final String fieldName) {
        return BeanUtils.getFieldValue(object, fieldName);
    }

    public static void setFieldValue(final Object object, final String fieldName, final Object value) {
        BeanUtils.setFieldValue(object, fieldName, value);
    }

    public static Class getSuperClassGenericType(Class clazz) {
        return BeanUtils.getSuperClassGenericType(clazz);
    }

    public static Class getSuperClassGenericType(Class clazz, int index) {
        return BeanUtils.getSuperClassGenericType(clazz, index);
    }

    private ESABeanUtils() {
    }
}
