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
package esa.commons;

import esa.commons.logging.Logger;
import esa.commons.logging.LoggerFactory;

import java.util.function.Function;

final class ConfigUtilsImpl implements ConfigUtils {
    static final Logger logger = LoggerFactory.getLogger(ConfigUtilsImpl.class);
    private final Function<String, String> getter;

    ConfigUtilsImpl(Function<String, String> getter) {
        Checks.checkNotNull(getter, "getter");
        this.getter = getter;
    }

    @Override
    public String getStr(String name) {
        Checks.checkNotEmptyArg(name, "name");
        return getter.apply(name);
    }
}
