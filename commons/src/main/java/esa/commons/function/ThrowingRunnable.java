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
package esa.commons.function;

import esa.commons.Checks;
import esa.commons.ExceptionUtils;

import java.util.function.Consumer;

/**
 * Allows user to throw a checked exception.
 * @see Runnable
 */
@FunctionalInterface
public interface ThrowingRunnable {

    /**
     * Wraps a {@link ThrowingRunnable} to {@link Runnable} which will rethrow the error thrown by the given {@link
     * ThrowingRunnable}
     *
     * @param r target to wrap
     *
     * @return transferred
     */
    static Runnable rethrow(ThrowingRunnable r) {
        Checks.checkNotNull(r);
        return () -> {
            try {
                r.run();
            } catch (Throwable e) {
                ExceptionUtils.throwException(e);
            }
        };
    }

    /**
     * Wraps a {@link ThrowingRunnable} to {@link Consumer} which will suppress the error thrown by the given {@link
     * ThrowingRunnable}
     *
     * @param r target to wrap
     *
     * @return transferred
     */
    static Runnable suppress(ThrowingRunnable r) {
        Checks.checkNotNull(r);
        return () -> {
            try {
                r.run();
            } catch (Throwable ignored) {
            }
        };
    }

    /**
     * Wraps a {@link ThrowingRunnable} to {@link Runnable} which will handle the error thrown by the given {@link
     * ThrowingRunnable} with the given {@code onFailure} operation of {@link Consumer}
     *
     * @param r         target to wrap
     * @param onFailure onFailure
     *
     * @return transferred
     */
    static Runnable failover(ThrowingRunnable r, Consumer<Throwable> onFailure) {
        Checks.checkNotNull(r);
        Checks.checkNotNull(onFailure);
        return () -> {
            try {
                r.run();
            } catch (Throwable e) {
                onFailure.accept(e);
            }
        };
    }

    /**
     * @see Runnable#run()
     */
    void run() throws Throwable;
}
