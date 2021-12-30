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

import esa.commons.StringUtils;
import esa.commons.logging.Logger;
import esa.commons.logging.LoggerFactory;
import esa.commons.spi.Feature;
import esa.commons.spi.SPI;
import esa.commons.spi.SpiLoader;

import java.util.Set;

/**
 * Obtain objects through commons spi
 */
@Feature(name = "spi")
public class SpiExtensionFactory implements ExtensionFactory {

    private static final Logger logger = LoggerFactory.getLogger(SpiExtensionFactory.class);

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getExtension(Class<T> type, String name, boolean required) {
        if (type.isInterface() && type.isAnnotationPresent(SPI.class)) {
            return getExtensionByNameAndType(type, name, required);
        } else if (!type.isInterface()) {
            Class<?> spiInterface = SpiLoader.getSpiInterface(type);
            if (spiInterface != null) {
                SpiLoader<T> cached = SpiLoader.cached((Class<T>) spiInterface);
                if (StringUtils.isEmpty(name)) {
                    name = cached.getExtensionNames().get(type);
                }
                if (!StringUtils.isEmpty(name)) {
                    return cached.getByName(name).orElse(null);
                } else {
                    if (required) {
                        throw new RuntimeException("The class " + type.getName() + " is not the spi definition " +
                                "of the interface, please check the spi configuration file to confirm whether " +
                                "the configuration is missing");
                    } else {
                        logger.warn("The class " + type.getName() + " is not the spi definition of " +
                                "the interface, so the injected object is null.");
                        return null;
                    }
                }
            }
        }
        return null;
    }


    private <T> T getExtensionByNameAndType(Class<T> type, String name, boolean required) {
        SpiLoader<T> cached = SpiLoader.cached(type);
        Set<String> names = cached.getExtensionClasses().keySet();
        if (StringUtils.isEmpty(name)) {
            if (names.size() > 1) {
                if (required) {
                    throw new RuntimeException("No qualifying bean of type " + type.getName() +
                            " available: expected single matching bean but found " +
                            names.size() + " : " + String.join(",", names));
                } else {
                    logger.warn("More than one is available and the name attribute of Inject is not " +
                            "configured, please configure the name attribute, otherwise we can only inject null");
                }
            } else if (names.size() == 0) {
                if (required) {
                    throw new RuntimeException("No implementation class available for SPI interface.");
                } else {
                    logger.warn("There is no available implementation class for the SPI interface, " +
                            "so the injected object is null.");
                }
            } else {
                // Here we must be able to get a unique object,
                // no need to consider the problem of array out of bounds
                return cached.getByName(names.toArray(new String[0])[0]).orElse(null);
            }
            return null;
        }
        if (!cached.getExtensionClasses().containsKey(name)) {
            if (required) {
                throw new RuntimeException("No bean named '" + name + "' available");
            } else {
                logger.warn("There is no bean named '" + name + "' available, so the injected object is null.");
                return null;
            }
        }
        // Here we must be able to get an object,
        // so there is no need to check the situation where the object cannot be obtained here
        return cached.getByName(name).orElse(null);
    }
}
