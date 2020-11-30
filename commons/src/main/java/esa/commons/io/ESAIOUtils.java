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

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;

/**
 * @deprecated use {@link IOUtils} please
 */
@Deprecated
public final class ESAIOUtils {

    public static void write(byte[] data, OutputStream output) throws IOException {
        IOUtils.write(data, output);
    }

    public static void closeQuietly(Closeable closeable) {
        IOUtils.closeQuietly(closeable);
    }

    public static byte[] toByteArray(File f) throws IOException {
        return IOUtils.toByteArray(f);
    }

    public static byte[] toByteArray(InputStream in) throws IOException {
        return IOUtils.toByteArray(in);
    }

    public static void copy(InputStream in, OutputStream out) throws IOException {
        IOUtils.copy(in, out);
    }

    public static void copy(InputStream in, OutputStream out, byte[] buffer) throws IOException {
        IOUtils.copy(in, out, buffer);
    }

    public static String readFileAsString(File f) throws IOException {
        return IOUtils.readFileAsString(f);
    }

    public static String readFileAsString(File f, Charset charset) throws IOException {
        return IOUtils.readFileAsString(f, charset);
    }

    public static String readFileAsString(File f, Charset charset, char[] buffer) throws IOException {
        return IOUtils.readFileAsString(f, charset, buffer);
    }

    public static String toString(InputStream in) throws IOException {
        return IOUtils.toString(in);
    }

    public static String toString(InputStream in, Charset charset) throws IOException {
        return IOUtils.toString(in, charset);
    }

    public static String toString(InputStream in, Charset charset, char[] buffer) throws IOException {
        return IOUtils.toString(in, charset, buffer);
    }

    public static void copy(InputStream in, Writer out, Charset charset) throws IOException {
        IOUtils.copy(in, out, charset);
    }

    public static void copy(InputStream in, Writer out, Charset charset, char[] buffer) throws IOException {
        IOUtils.copy(in, out, charset, buffer);
    }

    public static void copy(Reader in, Writer out) throws IOException {
        IOUtils.copy(in, out);
    }

    public static void copy(Reader in, Writer out, char[] buffer) throws IOException {
        IOUtils.copy(in, out, buffer);
    }

    private ESAIOUtils() {
    }
}
