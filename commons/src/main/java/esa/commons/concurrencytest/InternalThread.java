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
package esa.commons.concurrencytest;

import esa.commons.annotation.Beta;
import esa.commons.annotation.Internal;

/**
 * Indicates that a {@link Thread} is an internal thread witch maintains a series of objects for metering or tracing
 * such as {@link #meter()} and {@link #tracer()}. Note that this class is for internal use only and is subject to
 * change at any time. use {@link Thread} unless you know what you are doing.
 */
@Beta
@Internal
public interface InternalThread {


    /**
     * Returns current thread(probably {@code this}) or the underlying thread.
     *
     * @return thread
     */
    Thread thread();

    /**
     * Returns the meter object associated to current thread.
     * <p>
     * Note that this object would be set by any one else, so check the object returned is exactly what you want.
     *
     * @return meter
     */
    Object meter();

    /**
     * Sets the meter object associated to current thread.
     *
     * @param meter meter
     */
    void meter(Object meter);

    /**
     * Returns the tracer object associated to current thread.
     * <p>
     * Note that this object would be set by any one else, so check the object returned is exactly what you want.
     *
     * @return tracer
     */
    Object tracer();

    /**
     * Sets the tracer object associated to current thread.
     *
     * @param tracer tracer
     */
    void tracer(Object tracer);
}
