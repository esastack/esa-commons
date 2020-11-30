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
package esa.commons;

import esa.commons.concurrent.UnsafeUtils;
import esa.commons.io.IOUtils;
import esa.commons.io.StringBuilderWriter;

import java.io.PrintWriter;

public final class ExceptionUtils {

    /**
     * Converts give {@link Throwable}'s stack to {@link String}.
     *
     * @param t throwable
     *
     * @return string value of stack trace
     */
    public static String getStackTrace(Throwable t) {
        StringBuilderWriter sw = new StringBuilderWriter();
        PrintWriter pw = new PrintWriter(sw);
        try {
            t.printStackTrace(pw);
            return sw.toString();
        } finally {
            IOUtils.closeQuietly(pw);
        }
    }

    /**
     * Converts give {@link Throwable}'s stack to {@link String}.
     *
     * @param t throwable
     */
    public static void getStackTrace(Throwable t, StringBuilder sb) {
        PrintWriter pw = new PrintWriter(new StringBuilderWriter(sb));
        try {
            t.printStackTrace(pw);
        } finally {
            IOUtils.closeQuietly(pw);
        }
    }

    /**
     * Converts given {@link Throwable} to {@link RuntimeException}
     *
     * @return itself if it is an instance of {@link RuntimeException}, new instance of {@link RuntimeException}
     * otherwise.
     */
    public static RuntimeException asRuntime(Throwable t) {
        if (t == null) {
            return null;
        }
        if (t instanceof RuntimeException) {
            return (RuntimeException) t;
        }
        return new RuntimeException(t);
    }

    /**
     * Throws the given {@link Throwable} without checking.
     */
    public static void throwException(Throwable t) {
        if (UnsafeUtils.hasUnsafe()) {
            UnsafeUtils.throwException(t);
        } else {
            ExceptionUtils.<RuntimeException>throwException0(t);
        }
    }

    @SuppressWarnings("unchecked")
    private static <E extends Throwable> void throwException0(Throwable t) throws E {
        throw (E) t;
    }

    private ExceptionUtils() {
    }
}
