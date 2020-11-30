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

import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.ObjDoubleConsumer;

import static esa.commons.ExceptionUtils.throwException;

/**
 * Represents an operation that accepts a single {@code double}-valued argument and returns no result, which allows user
 * to throw a checked exception.
 * @see DoubleConsumer
 */
@FunctionalInterface
public interface ThrowingDoubleConsumer {

    /**
     * Wraps a {@link ThrowingDoubleConsumer} to {@link DoubleConsumer} which will rethrow the error thrown by the given
     * {@link ThrowingDoubleConsumer}
     *
     * @param c target to wrap
     *
     * @return transferred
     */
    static DoubleConsumer rethrow(ThrowingDoubleConsumer c) {
        Checks.checkNotNull(c);
        return (t) -> {
            try {
                c.accept(t);
            } catch (Throwable e) {
                throwException(e);
            }
        };
    }

    /**
     * Wraps a {@link ThrowingDoubleConsumer} to {@link DoubleConsumer} which will suppress the error thrown by the
     * given {@link ThrowingDoubleConsumer}
     *
     * @param c target to wrap
     *
     * @return transferred
     */
    static DoubleConsumer suppress(ThrowingDoubleConsumer c) {
        Checks.checkNotNull(c);
        return (t) -> {
            try {
                c.accept(t);
            } catch (Throwable ignored) {
            }
        };
    }

    /**
     * @see #failover(ThrowingDoubleConsumer, ObjDoubleConsumer)
     */
    static DoubleConsumer failover(ThrowingDoubleConsumer c,
                                   Consumer<Throwable> onFailure) {
        return failover(c, (e, t) -> onFailure.accept(e));
    }

    /**
     * Wraps a {@link ThrowingDoubleConsumer} to {@link DoubleConsumer} which will handle the error thrown by the given
     * {@link ThrowingDoubleConsumer} with the given {@code onFailure} operation of {@link ObjDoubleConsumer}
     *
     * @param c         target to wrap
     * @param onFailure failover
     *
     * @return transferred
     */
    static DoubleConsumer failover(ThrowingDoubleConsumer c,
                                   ObjDoubleConsumer<Throwable> onFailure) {
        Checks.checkNotNull(c);
        return (t) -> {
            try {
                c.accept(t);
            } catch (Throwable e) {
                if (onFailure != null) {
                    onFailure.accept(e, t);
                }
            }
        };
    }

    /**
     * Performs this operation on the given argument.
     *
     * @param value the input argument
     */
    void accept(double value) throws Throwable;
}
