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

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public final class IOUtils {

    public static void write(byte[] data, OutputStream output) throws IOException {
        if (data != null) {
            output.write(data);
        }
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException ignored) {
            }
        }
    }

    public static byte[] toByteArray(File f) throws IOException {
        byte[] bytes;
        InputStream in = null;
        try {
            in = new FileInputStream(f);
            bytes = toByteArray(in);
        } finally {
            closeQuietly(in);
        }
        return bytes;
    }

    public static byte[] toByteArray(InputStream in) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        int n;
        byte[] data = new byte[4096];
        while ((n = in.read(data)) != -1) {
            output.write(data, 0, n);
        }
        return output.toByteArray();
    }

    public static void copy(InputStream in, OutputStream out) throws IOException {
        copy(in, out, new byte[1024]);
    }

    public static void copy(InputStream in, OutputStream out, byte[] buffer) throws IOException {
        int bytesRead;
        while ((bytesRead = in.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
        }
    }

    public static void copy(InputStream in, Writer out, Charset charset) throws IOException {
        copy(in, out, charset, new char[1024]);
    }

    public static void copy(InputStream in, Writer out, Charset charset, char[] buffer) throws IOException {
        copy(new InputStreamReader(in, charset), out, buffer);
    }

    public static void copy(Reader in, Writer out) throws IOException {
        copy(in, out, new char[1024]);
    }

    public static void copy(Reader in, Writer out, char[] buffer) throws IOException {
        int bytesRead;
        while ((bytesRead = in.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
        }
    }

    public static String readFileAsString(File f) throws IOException {
        return readFileAsString(f, StandardCharsets.UTF_8);
    }

    public static String readFileAsString(File f, Charset charset) throws IOException {
        return readFileAsString(f, charset, new char[1024]);
    }

    public static String readFileAsString(File f, Charset charset, char[] buffer) throws IOException {
        InputStream in = null;
        try {
            in = new FileInputStream(f);
            return toString(in, charset, buffer);
        } finally {
            closeQuietly(in);
        }
    }

    public static String toString(InputStream in) throws IOException {
        return toString(in, StandardCharsets.UTF_8);
    }

    public static String toString(InputStream in, Charset charset) throws IOException {
        return toString(in, charset, new char[1024]);
    }

    public static String toString(InputStream in, Charset charset, char[] buffer) throws IOException {
        StringBuilderWriter writer = null;
        try {
            writer = new StringBuilderWriter();
            copy(new InputStreamReader(in, charset), writer, buffer);
        } finally {
            closeQuietly(writer);
        }
        return writer.toString();
    }

    private IOUtils() {
    }
}
