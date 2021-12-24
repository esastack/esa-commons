/*
 * Copyright 2021 OPPO ESA Stack Project
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
package esa.commons.spi.factory;

import esa.commons.spi.SPI;
import esa.commons.spi.SpiLoader;

import java.util.Optional;

/**
 * Obtain objects through commons spi
 */
public class SpiExtensionFactory implements ExtensionFactory {

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getExtension(Class<T> type, String name) {
        Optional<T> extension = Optional.empty();
        if (type.isInterface() && type.isAnnotationPresent(SPI.class)) {
            extension = SpiLoader.getByName(type, name);
        } else {
            Class<?>[] interfaces = type.getInterfaces();
            for (Class<?> in : interfaces) {
                if (in.isAnnotationPresent(SPI.class)) {
                    extension = SpiLoader.getByName((Class<T>) in, name);
                    break;
                }
            }
        }
        return extension.orElse(null);
    }
}
