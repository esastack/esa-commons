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
import java.util.function.IntConsumer;
import java.util.function.ObjIntConsumer;

import static esa.commons.ExceptionUtils.throwException;

/**
 * Represents an operation that accepts a single {@code int}-valued argument and returns no result, which allows user to
 * throw a checked exception.
 * @see IntConsumer
 */
@FunctionalInterface
public interface ThrowingIntConsumer {

    /**
     * Wraps a {@link ThrowingIntConsumer} to {@link IntConsumer} which will rethrow the error thrown by the given
     * {@link ThrowingIntConsumer}
     *
     * @param c target to wrap
     *
     * @return transferred
     */
    static IntConsumer rethrow(ThrowingIntConsumer c) {
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
     * Wraps a {@link ThrowingIntConsumer} to {@link IntConsumer} which will suppress the error thrown by the given
     * {@link ThrowingIntConsumer}
     *
     * @param c target to wrap
     *
     * @return transferred
     */
    static IntConsumer suppress(ThrowingIntConsumer c) {
        Checks.checkNotNull(c);
        return (t) -> {
            try {
                c.accept(t);
            } catch (Throwable ignored) {
            }
        };
    }

    /**
     * @see #failover(ThrowingIntConsumer, ObjIntConsumer)
     */
    static IntConsumer failover(ThrowingIntConsumer c,
                                Consumer<Throwable> onFailure) {
        return failover(c, (e, t) -> onFailure.accept(e));
    }

    /**
     * Wraps a {@link ThrowingIntConsumer} to {@link IntConsumer} which will handle the error thrown by the given {@link
     * ThrowingIntConsumer} with the given {@code onFailure} operation of {@link ObjIntConsumer}
     *
     * @param c         target to wrap
     * @param onFailure failover
     *
     * @return transferred
     */
    static IntConsumer failover(ThrowingIntConsumer c,
                                ObjIntConsumer<Throwable> onFailure) {
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
    void accept(int value) throws Throwable;
}
