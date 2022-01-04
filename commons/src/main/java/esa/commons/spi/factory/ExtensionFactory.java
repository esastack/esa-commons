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

/**
 * Used to obtain the objects that need to be injected, which can be extended to obtain objects from Java spi,
 * esa commons spi or spring, etc.
 */
@SPI
public interface ExtensionFactory {
    /**
     * Get extension object based on name and type. When the object cannot be obtained, we must decide whether
     * to throw an exception according to the required value of {@link esa.commons.spi.factory.Inject}.
     *
     * @param type type of extension
     * @param name name of extension
     * @param required default true. Means that whether to throw an exception when the extension cannot be found
     *                 or find more than one
     * @return extension object
     */
    <T> T getExtension(Class<T> type, String name, boolean required);
}
