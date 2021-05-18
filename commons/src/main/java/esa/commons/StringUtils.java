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

public final class StringUtils {

    public static final String EMPTY_STRING = "";

    /**
     * @return empty string
     */
    public static String empty() {
        return EMPTY_STRING;
    }

    /**
     * Whether the given {@code charSeq} is empty.
     *
     * @param charSeq {@link CharSequence} to check
     *
     * @return {@code true} if the given {@code charSeq} is not {@code null} and has a positive length.
     */
    public static boolean isEmpty(CharSequence charSeq) {
        return charSeq == null || charSeq.length() == 0;
    }

    /**
     * Whether the given {@code str} is empty.
     *
     * @param str {@link String} to check
     *
     * @return {@code true} if the given {@code str} is not {@code null} and has a positive length.
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    /**
     * Whether the given {@code charSeq} is not empty.
     *
     * @param charSeq {@link CharSequence} to check
     *
     * @return {@code true} if the given {@code charSeq} is {@code null} or dose not have a positive length.
     */
    public static boolean isNotEmpty(CharSequence charSeq) {
        return !isEmpty(charSeq);
    }

    /**
     * Whether the given {@code str} is not empty.
     *
     * @param str {@link String} to check
     *
     * @return {@code true} if the given {@code Str} is {@code null} or dose not have a positive length.
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * Returns the given {@code other} value if the given {@code str} is {@code null}.
     *
     * @param str   {@link String} to check
     * @param other fallback value
     *
     * @return {@code str} if the given {@code str} is not {@code null} or else {@code other}.
     */
    public static String nonNullOrElse(String str, String other) {
        return str == null ? other : str;
    }

    /**
     * Returns the given {@code other} value if the given {@code str} is empty.
     *
     * @param str   {@link String} to check
     * @param other fallback value
     *
     * @return {@code str} if the given {@code str} is not empty or else {@code other}.
     */
    public static String nonEmptyOrElse(String str, String other) {
        return isEmpty(str) ? other : str;
    }

    /**
     * Returns {@link #empty()} if the given {@code str} is {@code null}.
     *
     * @param str {@link String} to check
     *
     * @return {@code str} itself if the given {@code str} is not {@code null}, or else {@link #empty()} .
     */
    public static String emptyIfNull(String str) {
        return str == null ? EMPTY_STRING : str;
    }

    /**
     * Trims the given {@link String} value.
     *
     * @param str could be {@code null}
     *
     * @return trimmed value or {@code null}.
     */
    public static String trim(String str) {
        return str == null ? null : str.trim();
    }

    /**
     * Whether the given {@code seq} is blank.
     *
     * @param seq {@link String} to check
     *
     * @return {@code true} if the given {@code seq} is blank.
     */
    public static boolean isBlank(CharSequence seq) {
        int strLen;
        if (seq == null || (strLen = seq.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(seq.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Whether the given {@code seq} is not blank.
     *
     * @param seq {@link String} to check
     *
     * @return {@code true} if the given {@code seq} is not blank.
     */
    public static boolean isNotBlank(CharSequence seq) {
        return !isBlank(seq);
    }

    /**
     * Whether the given {@code seq} is quoted by '"' or "'".
     *
     * @param seq {@link CharSequence} to check
     *
     * @return {@code true} if the given {@code seq} is quoted.
     */
    public static boolean isQuoted(CharSequence seq) {
        return seq != null
                && seq.length() > 1
                && (seq.charAt(0) == '\"' && seq.charAt(seq.length() - 1) == '\"'
                || seq.charAt(0) == '\'' && seq.charAt(seq.length() - 1) == '\'');
    }

    /**
     * Quotes the given {@code seq} by "'".
     *
     * @param seq seq to quote
     *
     * @return quoted {@link String} value or {@code null} if the given {@code seq} is {@code null}.
     */
    public static String quote(CharSequence seq) {
        return seq == null ? null : "'" + seq + "'";
    }

    /**
     * Unquotes the given {@code seq} is quoted by '"' or "'".
     *
     * @param seq seq
     *
     * @return unquoted {@link String} value or itself if the given {@code seq} is not quoted by '"' or "'".
     */
    public static String unquote(String seq) {
        return (isQuoted(seq) ? unsafeUnquote(seq) : seq);
    }

    private static String unsafeUnquote(String s) {
        return s.substring(1, s.length() - 1);
    }

    /**
     * Concat the given {@code params} to a single value of {@link String}.
     *
     * @param params a series of {@link String}s to concat.
     *
     * @return concat
     */
    public static String concat(String... params) {
        if (params == null) {
            return null;
        }
        if (params.length == 0) {
            return EMPTY_STRING;
        }
        if (params.length < 5) {
            String ret = null;
            for (String s : params) {
                if (s != null) {
                    if (ret == null) {
                        ret = s;
                    } else {
                        ret += s;
                    }
                }
            }

            return ret;
        }
        int len = 0;
        for (String param : params) {
            if (param != null) {
                len += param.length();
            }
        }

        StringBuilder sb = new StringBuilder(len);
        for (String param : params) {
            if (param != null) {
                sb.append(param);
            }
        }
        return sb.toString();
    }

    private StringUtils() {
    }
}
