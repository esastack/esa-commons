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

import java.io.Closeable;
import java.nio.ByteBuffer;

/**
 * An appender appends the encoded data into the target(probably a file or console)
 */
interface Appender extends Closeable {

    /**
     * Appends the given data.
     *
     * @param data data
     */
    void append(ByteBuffer data);

}
