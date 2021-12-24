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
 * Indicates that the target annotated by {@link Feature} has some features which would be used in {@link SpiLoader}.
 * @see SPI
 * @see SpiLoader
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Feature {

    /**
     * <p>"name" is used to identify a spi object.</p>
     * We can identify spi objects through spi configuration file and {@link esa.commons.spi.Feature} annotation,
     * and {@link esa.commons.spi.Feature} annotation has a higher priority than spi configuration file.
     */
    String name() default "";

    /**
     * <p>"groups" is used for filtering the extensions which belong to the same group.</p>
     * When getting extensions annotated by @Feature for specific expectation, "group" can be a choice to get a series
     * of extensions annotated by the same group name.
     */
    String[] groups() default {};

    /**
     * <p>"tags" is used for filtering the extensions which match with the same key and the same value.</p>
     * The values of "tags" should be pairs of key-value which joined with ":", eg: "key1:value1, key2:value2, ...,
     * keyN:valueN".
     * <p>
     * Notice: the "key" is required in each tag, while the "value" is optional.
     * <p>
     * When getting extensions annotated by @Feature for specific expectation, once any pair of tag annotated on an
     * extension matches with the input tags for filtering, this extension will be added to result list.
     */
    String[] tags() default {};

    /**
     * <p>"excludeTags" is used for excluding the extensions.</p>
     * The values of "excludeTags" should be pairs of key-value, they are just like the key-value pairs of the "tags".
     * <p>
     * Also, the "key" is required and the "value" is optional. (same with "tags")
     * <p>
     * The difference between "tags" and "excludeTags" is:
     * <ul>
     * <li> An extension which matches with the "tags" would be ADDED in the featured extension list. </li>
     * <li> An extension which matches with the "excludeTags" would be SKIPPED. </li>
     * <li> "excludeTags" has a HIGHER PRIORITY than "tags". </li>
     * </ul>
     * <p>
     * Notice: When an extension matches with the "excludeTags", the matching of "tags" would NOT be processed, even if
     * the extension matches with the "excludeTags" and the "tags" at the same time.
     */
    String[] excludeTags() default {};

    /**
     * "order" is used for two purposes:
     * <ul>
     * <li> Sort the extensions. </li>
     * <li> Sort the wrapper classes. </li>
     * </ul>
     *
     * <p> Once need to sort the extensions or wrapper classes,
     * each class should be annotated by @Feature with the member "order".</p>
     *
     * <p> Extensions will be sorted by ASCEND order. </p>
     *
     * <p> When NO order configured in @Feature or NO @Feature annotation, "0" would be the default order value. </p>
     */
    int order() default 0;
}
