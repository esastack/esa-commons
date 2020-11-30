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

import esa.commons.Checks;

import java.io.Writer;

/**
 * Implementation of {@link Writer} that writes data into the {@link #sb} which could be convert to {@link String} by
 * {@link #toString()}.
 */
public class StringBuilderWriter extends Writer {

    private final StringBuilder sb;

    public StringBuilderWriter() {
        this(new StringBuilder());
    }

    public StringBuilderWriter(int capacity) {
        this(new StringBuilder(capacity));
    }

    public StringBuilderWriter(StringBuilder sb) {
        Checks.checkNotNull(sb, "sb");
        this.sb = sb;
    }

    @Override
    public void write(char[] cbuf, int off, int len) {
        this.sb.append(cbuf, off, len);
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() {
    }

    @Override
    public String toString() {
        return this.sb.toString();
    }

}
