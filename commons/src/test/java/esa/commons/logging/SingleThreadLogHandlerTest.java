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

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SingleThreadLogHandlerTest {

    @Test
    void testHandle() throws InterruptedException {
        final AtomicReference<byte[]> appended = new AtomicReference<>();
        final AtomicBoolean closed = new AtomicBoolean(false);
        final Appender appender = new Appender() {
            @Override
            public void append(ByteBuffer data) {
                byte[] bytes = new byte[data.remaining()];
                data.get(bytes);
                appended.set(bytes);
                throw new Error();
            }

            @Override
            public void close() {
                closed.set(true);
            }
        };
        final Encoder encoder = mock(Encoder.class);

        final SingleThreadLogHandler handler = new SingleThreadLogHandler(appender, encoder, 0, 0);
        final LogEvent event = new LogEventImpl("foo", Level.ERROR, "hello", null);
        final byte[] bytes = "test".getBytes();
        when(encoder.encode(event)).thenReturn(bytes);
        handler.handle(event);
        handler.worker.join();
        verify(encoder).encode(event);
        assertArrayEquals(bytes, appended.get());
        assertTrue(closed.get());
    }

}
