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
package esa.commons.io;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IOUtilsTest {

    @Test
    void testWrite() throws IOException {
        final List<Integer> l = new LinkedList<>();
        final OutputStream out = new OutputStream() {
            @Override
            public void write(int b) {
                l.add(b);
            }
        };

        IOUtils.write(new byte[]{(byte) 1, (byte) 2}, out);

        out.flush();
        out.close();

        assertEquals(2, l.size());
        assertEquals(1, l.get(0));
        assertEquals(2, l.get(1));
    }

    @Test
    void testCloseQuietly() {
        IOUtils.closeQuietly(() -> {
            throw new IOException();
        });

        final AtomicBoolean closed = new AtomicBoolean(false);
        IOUtils.closeQuietly(() -> closed.set(true));
        assertTrue(closed.get());
    }

    @Test
    void testFile() throws IOException {
        final File target = File.createTempFile("commons-test-", ".tmp");
        target.deleteOnExit();

        try {
            try (OutputStream out = new FileOutputStream(target)) {
                IOUtils.write("foo".getBytes(StandardCharsets.UTF_8), out);
                out.flush();
            }
            assertArrayEquals("foo".getBytes(StandardCharsets.UTF_8), IOUtils.toByteArray(target));
            try (InputStream in = new FileInputStream(target)) {
                assertEquals("foo", IOUtils.toString(in));
            }
            assertEquals("foo", IOUtils.readFileAsString(target));
        } finally {
            target.delete();
        }
    }

    @Test
    void testCopyStreamToStream() throws IOException {
        final InputStream in = new InputStream() {

            int i = 3;

            @Override
            public int read() {
                return i-- > 0 ? i : -1;
            }
        };

        final List<Integer> l = new LinkedList<>();
        final OutputStream out = new OutputStream() {
            @Override
            public void write(int b) {
                l.add(b);
            }
        };
        IOUtils.copy(in, out);

        assertEquals(3, l.size());
        assertEquals(2, l.get(0));
        assertEquals(1, l.get(1));
        assertEquals(0, l.get(2));

        in.close();
        out.close();
    }

    @Test
    void testCopyStreamToWriter() throws IOException {
        final InputStream in = new InputStream() {

            final byte[] bytes = "foo".getBytes(StandardCharsets.UTF_8);
            int index = 0;

            @Override
            public int read() {
                return index < bytes.length ? bytes[index++] : -1;
            }
        };

        final Writer writer = new StringBuilderWriter(16);
        IOUtils.copy(in, writer, StandardCharsets.UTF_8);
        writer.flush();
        assertEquals("foo", writer.toString());

        in.close();
        writer.close();
    }

    @Test
    void testCopyReaderToWriter() throws IOException {
        final Reader reader = new InputStreamReader(new InputStream() {

            final byte[] bytes = "foo".getBytes(StandardCharsets.UTF_8);
            int index = 0;

            @Override
            public int read() {
                return index < bytes.length ? bytes[index++] : -1;
            }
        }, StandardCharsets.UTF_8);

        final Writer writer = new StringBuilderWriter(16);
        IOUtils.copy(reader, writer);
        writer.flush();
        assertEquals("foo", writer.toString());

        reader.close();
        writer.close();
    }

}
