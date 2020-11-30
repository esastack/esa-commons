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
package esa.commons.logging;

import esa.commons.annotation.Beta;
import esa.commons.annotation.Internal;

/**
 * Extension of {@link Logger}, which would be created by {@link InternalLoggers}.
 */
@Beta
@Internal
public interface InternalLogger extends Logger {

    /**
     * Level of current logger.
     *
     * @return level
     */
    Level level();

    /**
     * Sets the level of current logger to given value.
     *
     * @param level level to set
     */
    void setLevel(Level level);

    /**
     * Whether the given {@code level} is enabled.
     *
     * @param level level
     *
     * @return {@code true} if given level is enabled, otherwise {@code false}
     */
    boolean isLogEnabled(Level level);
}
