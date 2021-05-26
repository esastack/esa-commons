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
package esa.commons.spi;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates the target annotated by {@link SPI} is a SPI interface.
 * @see Feature
 * @see SpiLoader
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface SPI {

    /**
     * Default extension name for SPI.
     * <p>It's used in two cases:</p>
     * <ul>
     *     <li>1. Get the default extension of SPI. {@link SpiLoader#getDefault()}  </li>
     *     <li>2. The default extension can be a back-up choice to return, when getting an extension by a given name
     *     which is null or empty string. {@link SpiLoader#getByName(String name)}
     *     </li>
     * </ul>
     *
     */
    String value() default "";
}
