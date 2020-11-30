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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ThrowingsTest {

    @Test
    void testThrowingConsumers() {
        // Consumer
        ThrowingConsumer.rethrow(t -> doNothing()).accept(null);
        ThrowingConsumer.suppress(t -> doNothing()).accept(null);
        ThrowingConsumer
                .failover(t -> doNothing(), throwable -> doNothing())
                .accept(null);
        assertThrows(Exception.class, () -> ThrowingConsumer.rethrow(this::throwEx).accept(null));
        ThrowingConsumer.suppress(this::throwEx).accept(null);
        assertThrows(IllegalStateException.class,
                () -> ThrowingConsumer.failover(this::throwEx, t -> {
                    throw new IllegalStateException();
                }).accept(null));

        // BiConsumer
        ThrowingBiConsumer.rethrow((t1, t2) -> doNothing()).accept(null, null);
        ThrowingBiConsumer.suppress((t1, t2) -> doNothing()).accept(null, null);
        ThrowingBiConsumer.failover((t1, t2) -> doNothing(), throwable -> doNothing()).accept(null, null);
        assertThrows(Exception.class, () -> ThrowingBiConsumer.rethrow((t1, t2) -> throwEx())
                .accept(null, null));
        ThrowingBiConsumer.suppress((t1, t2) -> throwEx()).accept(null, null);
        assertThrows(IllegalStateException.class,
                () -> ThrowingBiConsumer.failover((t1, t2) -> throwEx(), t -> {
                    throw new IllegalStateException();
                }).accept(null, null));

        // Consumer3
        ThrowingConsumer3.rethrow((t1, t2, t3) -> doNothing()).accept(null, null, null);
        ThrowingConsumer3.suppress((t1, t2, t3) -> doNothing()).accept(null, null, null);
        ThrowingConsumer3.failover((t1, t2, t3) -> doNothing(), throwable -> {
        }).accept(null, null, null);
        assertThrows(Exception.class, () -> ThrowingConsumer3.rethrow((t1, t2, t3) -> throwEx())
                .accept(null, null, null));
        ThrowingConsumer3.suppress((t1, t2, t3) -> throwEx()).accept(null, null, null);
        assertThrows(IllegalStateException.class,
                () -> ThrowingConsumer3.failover((t1, t2, t3) -> throwEx(), t -> {
                    throw new IllegalStateException();
                }).accept(null, null, null));

        // Consumer4
        ThrowingConsumer4.rethrow((t1, t2, t3, t4) -> doNothing()).accept(null, null, null, null);
        ThrowingConsumer4.suppress((t1, t2, t3, t4) -> {
            doNothing();
        }).accept(null, null, null, null);
        ThrowingConsumer4.failover((t1, t2, t3, t4) -> {
        }, throwable -> doNothing()).accept(null, null, null, null);
        assertThrows(Exception.class, () -> ThrowingConsumer4.rethrow((t1, t2, t3, t4) -> throwEx()).accept(null,
                null, null, null));
        ThrowingConsumer4.suppress((t1, t2, t3, t4) -> throwEx()).accept(null, null, null, null);
        assertThrows(IllegalStateException.class,
                () -> ThrowingConsumer4.failover((t1, t2, t3, t4) -> throwEx(), t -> {
                    throw new IllegalStateException();
                }).accept(null, null, null, null));

        // IntConsumer
        ThrowingIntConsumer.rethrow(t -> doNothing()).accept(1);
        ThrowingIntConsumer.suppress(t -> doNothing()).accept(1);
        ThrowingIntConsumer.failover(t -> doNothing(), throwable -> doNothing()).accept(1);
        assertThrows(Exception.class, () -> ThrowingIntConsumer.rethrow(this::throwEx).accept(1));
        ThrowingIntConsumer.suppress(this::throwEx).accept(1);
        assertThrows(IllegalStateException.class,
                () -> ThrowingIntConsumer.failover(this::throwEx, t -> {
                    throw new IllegalStateException();
                }).accept(1));

        // LongConsumer
        ThrowingLongConsumer.rethrow(t -> doNothing()).accept(1L);
        ThrowingLongConsumer.suppress(t -> doNothing()).accept(1L);
        ThrowingLongConsumer.failover(t -> doNothing(), throwable -> {
        }).accept(1L);
        assertThrows(Exception.class, () -> ThrowingLongConsumer.rethrow(this::throwEx).accept(1L));
        ThrowingLongConsumer.suppress(this::throwEx).accept(1);
        assertThrows(IllegalStateException.class,
                () -> ThrowingLongConsumer.failover(this::throwEx, t -> {
                    throw new IllegalStateException();
                }).accept(1L));

        // DoubleConsumer
        ThrowingDoubleConsumer.rethrow(t -> doNothing()).accept(1D);
        ThrowingDoubleConsumer.suppress(t -> doNothing()).accept(1D);
        ThrowingDoubleConsumer.failover(t -> doNothing(), throwable -> {
        }).accept(1D);
        assertThrows(Exception.class, () -> ThrowingDoubleConsumer.rethrow(this::throwEx).accept(1D));
        ThrowingDoubleConsumer.suppress(this::throwEx).accept(1);
        assertThrows(IllegalStateException.class,
                () -> ThrowingDoubleConsumer.failover(this::throwEx, t -> {
                    throw new IllegalStateException();
                }).accept(1D));
    }

    @Test
    void testThrowingFunctions() {
        // Function
        assertNull(ThrowingFunction.rethrow(t -> t).apply(null));
        assertNull(ThrowingFunction.onFailure(t -> t, null).apply(null));
        assertNull(ThrowingFunction.failover(t -> t, t -> null).apply(null));
        assertThrows(Exception.class, () -> ThrowingFunction.rethrow(this::throwEx).apply(null));
        assertNull(ThrowingFunction.onFailure(this::throwEx, null).apply(null));
        assertThrows(IllegalStateException.class,
                () -> ThrowingFunction.failover(this::throwEx, t -> {
                    throw new IllegalStateException();
                }).apply(null));

        // BiFunction
        assertNull(ThrowingBiFunction.rethrow((t1, t2) -> t1).apply(null, null));
        assertNull(ThrowingBiFunction.onFailure((t1, t2) -> t1, null).apply(null, null));
        assertNull(ThrowingBiFunction.failover((t1, t2) -> t1, t -> null).apply(null, null));
        assertThrows(Exception.class, () -> ThrowingBiFunction.rethrow(this::throwEx).apply(null, null));
        assertNull(ThrowingBiFunction.onFailure(this::throwEx, null).apply(null, null));
        assertThrows(IllegalStateException.class,
                () -> ThrowingBiFunction.failover(this::throwEx, t -> {
                    throw new IllegalStateException();
                }).apply(null, null));

        // Function3
        assertNull(ThrowingFunction3.rethrow((t1, t2, t3) -> t1).apply(null, null, null));
        assertNull(ThrowingFunction3.onFailure((t1, t2, t3) -> t1, null).apply(null, null, null));
        assertNull(ThrowingFunction3.failover((t1, t2, t3) -> t1, t -> null).apply(null, null, null));
        assertThrows(Exception.class, () -> ThrowingFunction3.rethrow(this::throwEx).apply(null, null, null));
        assertNull(ThrowingFunction3.onFailure(this::throwEx, null).apply(null, null, null));
        assertThrows(IllegalStateException.class,
                () -> ThrowingFunction3.failover(this::throwEx, t -> {
                    throw new IllegalStateException();
                }).apply(null, null, null));


        // Function4
        assertNull(ThrowingFunction4.rethrow((t1, t2, t3, t4) -> t1).apply(null, null, null, null));
        assertNull(ThrowingFunction4.onFailure((t1, t2, t3, t4) -> t1, null).apply(null, null, null, null));
        assertNull(ThrowingFunction4.failover((t1, t2, t3, t4) -> t1, t -> null).apply(null, null, null, null));
        assertThrows(Exception.class, () -> ThrowingFunction4.rethrow(this::throwEx).apply(null, null, null, null));
        assertNull(ThrowingFunction4.onFailure(this::throwEx, null).apply(null, null, null, null));
        assertThrows(IllegalStateException.class,
                () -> ThrowingFunction4.failover(this::throwEx, t -> {
                    throw new IllegalStateException();
                }).apply(null, null, null, null));

        // IntFunction
        assertNull(ThrowingIntFunction.rethrow(t -> null).apply(1));
        assertNull(ThrowingIntFunction.onFailure(t -> null, 1).apply(1));
        assertNull(ThrowingIntFunction.failover(t -> null, t -> 1).apply(1));
        assertThrows(Exception.class, () -> ThrowingIntFunction.rethrow(this::throwEx).apply(1));
        assertNull(ThrowingIntFunction.onFailure(this::throwEx, null).apply(1));
        assertThrows(IllegalStateException.class,
                () -> ThrowingIntFunction.failover(this::throwEx, t -> {
                    throw new IllegalStateException();
                }).apply(1));

        // LongFunction
        assertNull(ThrowingLongFunction.rethrow(t -> null).apply(1L));
        assertNull(ThrowingLongFunction.onFailure(t -> null, 1L).apply(1L));
        assertNull(ThrowingLongFunction.failover(t -> null, t -> 1L).apply(1L));
        assertThrows(Exception.class, () -> ThrowingLongFunction.rethrow(this::throwEx).apply(1L));
        assertNull(ThrowingLongFunction.onFailure(this::throwEx, null).apply(1L));
        assertThrows(IllegalStateException.class,
                () -> ThrowingLongFunction.failover(this::throwEx, t -> {
                    throw new IllegalStateException();
                }).apply(1L));

        // DoubleFunction
        assertNull(ThrowingDoubleFunction.rethrow(t -> null).apply(1D));
        assertNull(ThrowingDoubleFunction.onFailure(t -> null, 1D).apply(1D));
        assertNull(ThrowingDoubleFunction.failover(t -> null, t -> 1D).apply(1D));
        assertThrows(Exception.class, () -> ThrowingDoubleFunction.rethrow(this::throwEx).apply(1D));
        assertNull(ThrowingDoubleFunction.onFailure(this::throwEx, null).apply(1D));
        assertThrows(IllegalStateException.class,
                () -> ThrowingDoubleFunction.failover(this::throwEx, t -> {
                    throw new IllegalStateException();
                }).apply(1D));

        // ToIntFunction
        assertEquals(1, ThrowingToIntFunction.rethrow(t -> 1).applyAsInt(null));
        assertEquals(1, ThrowingToIntFunction.onFailure(t -> 1, 2).applyAsInt(null));
        assertEquals(1, ThrowingToIntFunction.failover(t -> 1, t -> 2).applyAsInt(null));
        assertThrows(Exception.class, () -> ThrowingToIntFunction.rethrow(this::throwExF).applyAsInt(null));
        assertEquals(2, ThrowingToIntFunction.onFailure(this::throwExF, 2).applyAsInt(null));
        assertThrows(IllegalStateException.class,
                () -> ThrowingToIntFunction.failover(this::throwExF, t -> {
                    throw new IllegalStateException();
                }).applyAsInt(null));

        // ToLongFunction
        assertEquals(1L, ThrowingToLongFunction.rethrow(t -> 1L)
                .applyAsLong(null));
        assertEquals(1L, ThrowingToLongFunction.onFailure(t -> 1L, 2L)
                .applyAsLong(null));
        assertEquals(1L, ThrowingToLongFunction.failover(t -> 1L, t -> 2L)
                .applyAsLong(null));
        assertThrows(Exception.class, () -> ThrowingToLongFunction.rethrow(this::throwExF)
                .applyAsLong(null));
        assertEquals(2L, ThrowingToLongFunction.onFailure(this::throwExF, 2L)
                .applyAsLong(null));
        assertThrows(IllegalStateException.class,
                () -> ThrowingToLongFunction.failover(this::throwExF, t -> {
                    throw new IllegalStateException();
                }).applyAsLong(null));

        // ToDoubleFunction
        assertEquals(1D, ThrowingToDoubleFunction.rethrow(t -> 1D)
                .applyAsDouble(null));
        assertEquals(1D, ThrowingToDoubleFunction.onFailure(t -> 1D, 2D)
                .applyAsDouble(null));
        assertEquals(1D, ThrowingToDoubleFunction.failover(t -> 1D, t -> 2D)
                .applyAsDouble(null));
        assertThrows(Exception.class, () -> ThrowingToDoubleFunction.rethrow(this::throwExF)
                .applyAsDouble(null));
        assertEquals(2D, ThrowingToDoubleFunction.onFailure(this::throwExF, 2D)
                .applyAsDouble(null));
        assertThrows(IllegalStateException.class,
                () -> ThrowingToDoubleFunction.failover(this::throwExF, t -> {
                    throw new IllegalStateException();
                }).applyAsDouble(null));

        // ToIntBiFunction
        assertEquals(1, ThrowingToIntBiFunction.rethrow((t, u) -> 1)
                .applyAsInt(null, null));
        assertEquals(1, ThrowingToIntBiFunction.onFailure((t, u) -> 1, 2)
                .applyAsInt(null, null));
        assertEquals(1, ThrowingToIntBiFunction.failover((t, u) -> 1, t -> 2)
                .applyAsInt(null, null));
        assertThrows(Exception.class, () -> ThrowingToIntBiFunction.rethrow(this::throwExF)
                .applyAsInt(null, null));
        assertEquals(2, ThrowingToIntBiFunction.onFailure(this::throwExF, 2)
                .applyAsInt(null, null));
        assertThrows(IllegalStateException.class,
                () -> ThrowingToIntBiFunction.failover(this::throwExF, t -> {
                    throw new IllegalStateException();
                }).applyAsInt(null, null));

        // ToLongBiFunction
        assertEquals(1L, ThrowingToLongBiFunction.rethrow((t, u) -> 1L)
                .applyAsLong(null, null));
        assertEquals(1L, ThrowingToLongBiFunction.onFailure((t, u) -> 1L, 2L)
                .applyAsLong(null, null));
        assertEquals(1L, ThrowingToLongBiFunction.failover((t, u) -> 1L, t -> 2L)
                .applyAsLong(null, null));
        assertThrows(Exception.class, () -> ThrowingToLongBiFunction.rethrow(this::throwExF)
                .applyAsLong(null, null));
        assertEquals(2L, ThrowingToLongBiFunction.onFailure(this::throwExF, 2L)
                .applyAsLong(null, null));
        assertThrows(IllegalStateException.class,
                () -> ThrowingToLongBiFunction.failover(this::throwExF, t -> {
                    throw new IllegalStateException();
                }).applyAsLong(null, null));

        // ToDoubleBiFunction
        assertEquals(1D, ThrowingToDoubleBiFunction.rethrow((t, u) -> 1D)
                .applyAsDouble(null, null));
        assertEquals(1D, ThrowingToDoubleBiFunction.onFailure((t, u) -> 1D, 2D)
                .applyAsDouble(null, null));
        assertEquals(1D, ThrowingToDoubleBiFunction.failover((t, u) -> 1D, t -> 2D)
                .applyAsDouble(null, null));
        assertThrows(Exception.class, () -> ThrowingToDoubleBiFunction.rethrow(this::throwExF)
                .applyAsDouble(null, null));
        assertEquals(2D, ThrowingToDoubleBiFunction.onFailure(this::throwExF, 2D)
                .applyAsDouble(null, null));
        assertThrows(IllegalStateException.class,
                () -> ThrowingToDoubleBiFunction.failover(this::throwExF, t -> {
                    throw new IllegalStateException();
                }).applyAsDouble(null, null));

        // ToIntFunction3
        assertEquals(1, ThrowingToIntFunction3.rethrow((t1, t2, t3) -> 1)
                .applyAsInt(null, null, null));
        assertEquals(1, ThrowingToIntFunction3.onFailure((t1, t2, t3) -> 1, 2)
                .applyAsInt(null, null, null));
        assertEquals(1, ThrowingToIntFunction3.failover((t1, t2, t3) -> 1, t -> 2)
                .applyAsInt(null, null, null));
        assertThrows(Exception.class, () -> ThrowingToIntFunction3.rethrow(this::throwExF)
                .applyAsInt(null, null, null));
        assertEquals(2, ThrowingToIntFunction3.onFailure(this::throwExF, 2)
                .applyAsInt(null, null, null));
        assertThrows(IllegalStateException.class,
                () -> ThrowingToIntFunction3.failover(this::throwExF, t -> {
                    throw new IllegalStateException();
                }).applyAsInt(null, null, null));

        // ToLongFunction3
        assertEquals(1L, ThrowingToLongFunction3.rethrow((t1, t2, t3) -> 1L)
                .applyAsLong(null, null, null));
        assertEquals(1L, ThrowingToLongFunction3.onFailure((t1, t2, t3) -> 1L, 2L)
                .applyAsLong(null, null, null));
        assertEquals(1L, ThrowingToLongFunction3.failover((t1, t2, t3) -> 1L, t -> 2L)
                .applyAsLong(null, null, null));
        assertThrows(Exception.class, () -> ThrowingToLongFunction3.rethrow(this::throwExF)
                .applyAsLong(null, null, null));
        assertEquals(2L, ThrowingToLongFunction3.onFailure(this::throwExF, 2L)
                .applyAsLong(null, null, null));
        assertThrows(IllegalStateException.class,
                () -> ThrowingToLongFunction3.failover(this::throwExF, t -> {
                    throw new IllegalStateException();
                }).applyAsLong(null, null, null));

        // ToDoubleFunction3
        assertEquals(1D, ThrowingToDoubleFunction3.rethrow((t1, t2, t3) -> 1D)
                .applyAsDouble(null, null, null));
        assertEquals(1D, ThrowingToDoubleFunction3.onFailure((t1, t2, t3) -> 1D, 2D)
                .applyAsDouble(null, null, null));
        assertEquals(1D, ThrowingToDoubleFunction3.failover((t1, t2, t3) -> 1D, t -> 2D)
                .applyAsDouble(null, null, null));
        assertThrows(Exception.class, () -> ThrowingToDoubleFunction3.rethrow(this::throwExF)
                .applyAsDouble(null, null, null));
        assertEquals(2D, ThrowingToDoubleFunction3.onFailure(this::throwExF, 2D)
                .applyAsDouble(null, null, null));
        assertThrows(IllegalStateException.class,
                () -> ThrowingToDoubleFunction3.failover(this::throwExF, t -> {
                    throw new IllegalStateException();
                }).applyAsDouble(null, null, null));

        // ToIntFunction4
        assertEquals(1, ThrowingToIntFunction4.rethrow((t1, t2, t3, t4) -> 1)
                .applyAsInt(null, null, null, null));
        assertEquals(1, ThrowingToIntFunction4.onFailure((t1, t2, t3, t4) -> 1, 2)
                .applyAsInt(null, null, null, null));
        assertEquals(1, ThrowingToIntFunction4.failover((t1, t2, t3, t4) -> 1, t -> 2)
                .applyAsInt(null, null, null,
                        null));
        assertThrows(Exception.class, () -> ThrowingToIntFunction4.rethrow(this::throwExF)
                .applyAsInt(null, null, null, null));
        assertEquals(2, ThrowingToIntFunction4.onFailure(this::throwExF, 2)
                .applyAsInt(null, null, null, null));
        assertThrows(IllegalStateException.class,
                () -> ThrowingToIntFunction4.failover(this::throwExF, t -> {
                    throw new IllegalStateException();
                }).applyAsInt(null, null, null, null));

        // ToLongFunction4
        assertEquals(1L, ThrowingToLongFunction4.rethrow((t1, t2, t3, t4) -> 1L)
                .applyAsLong(null, null, null, null));
        assertEquals(1L, ThrowingToLongFunction4.onFailure((t1, t2, t3, t4) -> 1L, 2L)
                .applyAsLong(null, null, null,
                        null));
        assertEquals(1L, ThrowingToLongFunction4.failover((t1, t2, t3, t4) -> 1L, t -> 2L)
                .applyAsLong(null, null,
                        null, null));
        assertThrows(Exception.class, () -> ThrowingToLongFunction4.rethrow(this::throwExF)
                .applyAsLong(null, null,
                        null, null));
        assertEquals(2L, ThrowingToLongFunction4.onFailure(this::throwExF, 2L)
                .applyAsLong(null, null, null, null));
        assertThrows(IllegalStateException.class,
                () -> ThrowingToLongFunction4.failover(this::throwExF, t -> {
                    throw new IllegalStateException();
                }).applyAsLong(null, null, null, null));

        // ToDoubleFunction4
        assertEquals(1D, ThrowingToDoubleFunction4.rethrow((t1, t2, t3, t4) -> 1D)
                .applyAsDouble(null, null, null,
                        null));
        assertEquals(1D, ThrowingToDoubleFunction4.onFailure((t1, t2, t3, t4) -> 1D, 2D)
                .applyAsDouble(null, null,
                        null, null));
        assertEquals(1D, ThrowingToDoubleFunction4.failover((t1, t2, t3, t4) -> 1D, t -> 2D)
                .applyAsDouble(null, null, null, null));
        assertThrows(Exception.class, () -> ThrowingToDoubleFunction4.rethrow(this::throwExF).applyAsDouble(null,
                null, null, null));
        assertEquals(2D, ThrowingToDoubleFunction4.onFailure(this::throwExF, 2D)
                .applyAsDouble(null, null, null, null));
        assertThrows(IllegalStateException.class,
                () -> ThrowingToDoubleFunction4.failover(this::throwExF, t -> {
                    throw new IllegalStateException();
                }).applyAsDouble(null, null, null, null));
    }


    @Test
    void testThrowingPredicates() {
        // Predicate
        assertTrue(ThrowingPredicate.rethrow(t -> true).test(null));
        assertTrue(ThrowingPredicate.onFailure(t -> true, true).test(null));
        assertTrue(ThrowingPredicate.failover(t -> true, throwable -> true).test(null));
        assertThrows(Exception.class, () -> ThrowingPredicate.rethrow(this::throwEx).test(null));
        assertTrue(ThrowingPredicate.onFailure(this::throwEx, true).test(null));
        assertThrows(IllegalStateException.class,
                () -> ThrowingPredicate.failover(this::throwEx, t -> {
                    throw new IllegalStateException();
                }).test(null));

        // BiPredicate
        assertTrue(ThrowingBiPredicate.rethrow((t1, t2) -> true).test(null, null));
        assertTrue(ThrowingBiPredicate.onFailure((t1, t2) -> true, true).test(null, null));
        assertTrue(ThrowingBiPredicate.failover((t1, t2) -> true, throwable -> true).test(null, null));
        assertThrows(Exception.class, () -> ThrowingBiPredicate.rethrow(this::throwEx).test(null, null));
        assertTrue(ThrowingBiPredicate.onFailure(this::throwEx, true).test(null, null));
        assertThrows(IllegalStateException.class,
                () -> ThrowingBiPredicate.failover(this::throwEx, t -> {
                    throw new IllegalStateException();
                }).test(null, null));

        // Predicate3
        assertTrue(ThrowingPredicate3.rethrow((t1, t2, t3) -> true).test(null, null, null));
        assertTrue(ThrowingPredicate3.onFailure((t1, t2, t3) -> true, true).test(null, null, null));
        assertTrue(ThrowingPredicate3.failover((t1, t2, t3) -> true, throwable -> true).test(null, null, null));
        assertThrows(Exception.class, () -> ThrowingPredicate3.rethrow(this::throwEx).test(null, null, null));
        assertTrue(ThrowingPredicate3.onFailure(this::throwEx, true).test(null, null, null));
        assertThrows(IllegalStateException.class,
                () -> ThrowingPredicate3.failover(this::throwEx, t -> {
                    throw new IllegalStateException();
                }).test(null, null, null));


        // Predicate4
        assertTrue(ThrowingPredicate4.rethrow((t1, t2, t3, t4) -> true).test(null, null, null, null));
        assertTrue(ThrowingPredicate4.onFailure((t1, t2, t3, t4) -> true, true).test(null, null, null, null));
        assertTrue(ThrowingPredicate4.failover((t1, t2, t3, t4) -> true, throwable -> true).test(null, null, null,
                null));
        assertThrows(Exception.class, () -> ThrowingPredicate4.rethrow(this::throwEx).test(null, null, null, null));
        assertTrue(ThrowingPredicate4.onFailure(this::throwEx, true).test(null, null, null, null));
        assertThrows(IllegalStateException.class,
                () -> ThrowingPredicate4.failover(this::throwEx, t -> {
                    throw new IllegalStateException();
                }).test(null, null, null, null));

        // IntPredicate
        assertTrue(ThrowingIntPredicate.rethrow(t -> true).test(1));
        assertTrue(ThrowingIntPredicate.onFailure(t -> true, true).test(1));
        assertTrue(ThrowingIntPredicate.failover(t -> true, throwable -> true).test(1));
        assertThrows(Exception.class, () -> ThrowingIntPredicate.rethrow(this::throwEx).test(1));
        assertTrue(ThrowingIntPredicate.onFailure(this::throwEx, true).test(1));
        assertThrows(IllegalStateException.class,
                () -> ThrowingIntPredicate.failover(this::throwEx, t -> {
                    throw new IllegalStateException();
                }).test(1));

        // LongPredicate
        assertTrue(ThrowingLongPredicate.rethrow(t -> true).test(1L));
        assertTrue(ThrowingLongPredicate.onFailure(t -> true, true).test(1L));
        assertTrue(ThrowingLongPredicate.failover(t -> true, throwable -> true).test(1L));
        assertThrows(Exception.class, () -> ThrowingLongPredicate.rethrow(this::throwEx).test(1L));
        assertTrue(ThrowingLongPredicate.onFailure(this::throwEx, true).test(1L));
        assertThrows(IllegalStateException.class,
                () -> ThrowingLongPredicate.failover(this::throwEx, t -> {
                    throw new IllegalStateException();
                }).test(1L));

        // DoublePredicate
        assertTrue(ThrowingDoublePredicate.rethrow(t -> true).test(1D));
        assertTrue(ThrowingDoublePredicate.onFailure(t -> true, true).test(1D));
        assertTrue(ThrowingDoublePredicate.failover(t -> true, throwable -> true).test(1D));
        assertThrows(Exception.class, () -> ThrowingDoublePredicate.rethrow(this::throwEx).test(1D));
        assertTrue(ThrowingDoublePredicate.onFailure(this::throwEx, true).test(1D));
        assertThrows(IllegalStateException.class,
                () -> ThrowingDoublePredicate.failover(this::throwEx, t -> {
                    throw new IllegalStateException();
                }).test(1D));

    }

    @Test
    void testThrowingSuppliers() {
        // Supplier
        assertNull(ThrowingSupplier.rethrow(() -> null).get());
        assertNull(ThrowingSupplier.onFailure(() -> null, "").get());
        assertNull(ThrowingSupplier.failover(() -> null, throwable -> "").get());
        assertThrows(Exception.class, () -> ThrowingSupplier.rethrow(this::throwExS).get());
        assertEquals(1, ThrowingSupplier.onFailure(this::throwExS, 1).get());
        assertThrows(IllegalStateException.class,
                () -> ThrowingSupplier.failover(this::throwExS, t -> {
                    throw new IllegalStateException();
                }).get());

        // IntSupplier
        assertEquals(1, ThrowingIntSupplier.rethrow(() -> 1).getAsInt());
        assertEquals(1, ThrowingIntSupplier.onFailure(() -> 1, 2).getAsInt());
        assertEquals(1, ThrowingIntSupplier.failover(() -> 1, throwable -> 2).getAsInt());
        assertThrows(Exception.class, () -> ThrowingIntSupplier.rethrow(this::throwExS).getAsInt());
        assertEquals(1, ThrowingIntSupplier.onFailure(this::throwExS, 1).getAsInt());
        assertThrows(IllegalStateException.class,
                () -> ThrowingIntSupplier.failover(this::throwExS, t -> {
                    throw new IllegalStateException();
                }).getAsInt());

        // LongSupplier
        assertEquals(1L, ThrowingLongSupplier.rethrow(() -> 1L).getAsLong());
        assertEquals(1L, ThrowingLongSupplier.onFailure(() -> 1L, 2L).getAsLong());
        assertEquals(1L, ThrowingLongSupplier.failover(() -> 1L, throwable -> 2L).getAsLong());
        assertThrows(Exception.class, () -> ThrowingLongSupplier.rethrow(this::throwExS).getAsLong());
        assertEquals(1L, ThrowingLongSupplier.onFailure(this::throwExS, 1L).getAsLong());
        assertThrows(IllegalStateException.class,
                () -> ThrowingLongSupplier.failover(this::throwExS, t -> {
                    throw new IllegalStateException();
                }).getAsLong());

        // DoubleSupplier
        assertEquals(1D, ThrowingDoubleSupplier.rethrow(() -> 1D).getAsDouble());
        assertEquals(1D, ThrowingDoubleSupplier.onFailure(() -> 1D, 2D).getAsDouble());
        assertEquals(1D, ThrowingDoubleSupplier.failover(() -> 1D, throwable -> 2D).getAsDouble());
        assertThrows(Exception.class, () -> ThrowingDoubleSupplier.rethrow(this::throwExS).getAsDouble());
        assertEquals(1D, ThrowingDoubleSupplier.onFailure(this::throwExS, 1D).getAsDouble());
        assertThrows(IllegalStateException.class,
                () -> ThrowingDoubleSupplier.failover(this::throwExS, t -> {
                    throw new IllegalStateException();
                }).getAsDouble());

        // BooleanSupplier
        assertTrue(ThrowingBooleanSupplier.rethrow(() -> true).getAsBoolean());
        assertTrue(ThrowingBooleanSupplier.onFailure(() -> true, false).getAsBoolean());
        assertTrue(ThrowingBooleanSupplier.failover(() -> true, throwable -> false).getAsBoolean());
        assertThrows(Exception.class, () -> ThrowingBooleanSupplier.rethrow(this::throwEx).getAsBoolean());
        assertTrue(ThrowingBooleanSupplier.onFailure(this::throwEx, true).getAsBoolean());
        assertThrows(IllegalStateException.class,
                () -> ThrowingBooleanSupplier.failover(this::throwEx, t -> {
                    throw new IllegalStateException();
                }).getAsBoolean());

    }

    @Test
    void testThrowingRunnable() {
        // Function
        ThrowingRunnable.rethrow(ThrowingsTest::doNothing).run();
        ThrowingRunnable.suppress(ThrowingsTest::doNothing).run();
        ThrowingRunnable.failover(ThrowingsTest::doNothing, throwable -> doNothing()).run();
        assertThrows(Exception.class, () -> ThrowingRunnable.rethrow(this::throwEx).run());
        ThrowingRunnable.suppress(this::throwEx).run();
        assertThrows(IllegalStateException.class,
                () -> ThrowingRunnable.failover(this::throwEx, t -> {
                    throw new IllegalStateException();
                }).run());
    }

    private static void doNothing() {

    }

    private int throwExS() throws Exception {
        throw new Exception();
    }

    private int throwExF(Object... o) throws Exception {
        throw new Exception();
    }

    private boolean throwEx(Object... o) throws Exception {
        throw new Exception();
    }
}
