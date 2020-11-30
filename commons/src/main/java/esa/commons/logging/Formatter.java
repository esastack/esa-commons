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

import esa.commons.StringUtils;

import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;

class Formatter {

    private static final String DELIM = "{}";
    private static final char DELIM_PREFIX = '%';
    private static final char ESCAPE_CHAR = '\\';

    static <T> Converter<T> toConverter(String pattern,
                                        Map<String, Function<String, Converter<T>>> map) {
        return toConverter(pattern, DELIM_PREFIX, map);
    }

    @SuppressWarnings("unchecked")
    static <T> Converter<T> toConverter(String pattern,
                                        char delimPrefix,
                                        Map<String, Function<String, Converter<T>>> map) {

        int d;
        if (StringUtils.isEmpty(pattern)
                || map == null
                || map.isEmpty()
                || (d = pattern.indexOf(delimPrefix)) == -1) {
            return (t, s) -> s.append(pattern);
        }

        Map<String, Function<String, Converter<T>>> sorted =
                new TreeMap<>(Comparator.reverseOrder());
        sorted.putAll(map);
        map = sorted;

        final List<Converter> slices = new LinkedList<>();
        int i = 0;
        StringBuilder sbBuf = new StringBuilder();
        out:
        do {
            boolean notEscaped = d == 0 || pattern.charAt(d - 1) != ESCAPE_CHAR;
            if (notEscaped) {
                // normal text
                sbBuf.append(pattern, i, d);
            } else {
                sbBuf.append(pattern, i, d - 1);
                // check that escape char is not is escaped: "\\%f"
                notEscaped = d >= 2 && pattern.charAt(d - 2) == ESCAPE_CHAR;
            }

            i = d + 1;
            if (notEscaped && d < pattern.length() - 1) {
                // match marker
                for (Map.Entry<String, Function<String, Converter<T>>> entry : map.entrySet()) {
                    String marker = entry.getKey();
                    if (d + marker.length() < pattern.length()
                            && pattern.substring(i, i + marker.length()).equals(marker)) {

                        i += marker.length();
                        String param = null;
                        boolean pNotEscaped = false;
                        if (i < pattern.length() - 1 && pattern.charAt(i) == '{') {
                            // maybe: foo-%d{yyyyMMdd}
                            int pIndex = pattern.indexOf('}', i + 1);
                            if (pIndex != -1) {
                                pNotEscaped = pIndex == i + 1 || pattern.charAt(pIndex - 1) != ESCAPE_CHAR;
                                if (pNotEscaped) {
                                    // foo-%d{yyyyMMdd} or foo-%d{}
                                    param = pattern.substring(i + 1, pIndex);
                                    i = pIndex + 1;
                                } else {
                                    if (pIndex >= i + 3 && pattern.charAt(pIndex - 2) == ESCAPE_CHAR) {
                                        // foo-%d{yyyyMMdd\\}
                                        param = pattern.substring(i + 1, pIndex - 1);
                                        i = pIndex + 1;
                                        pNotEscaped = true;
                                    } else {
                                        // foo-%d{\} or foo-%d{yyyyMMdd\}
                                        param = pattern.substring(i, pIndex - 1) + '}';
                                        i = pIndex + 1;
                                    }
                                }
                            }
                        }
                        if (sbBuf.length() > 0) {
                            // add plain text
                            final String plainText = sbBuf.toString();
                            slices.add((t, sb) -> sb.append(plainText));
                            sbBuf.setLength(0);
                        }

                        // add marker converter
                        slices.add(pNotEscaped ? entry.getValue().apply(param) : entry.getValue().apply(null));
                        if (!pNotEscaped && param != null) {
                            sbBuf.append(param);
                        }

                        d = pattern.indexOf(delimPrefix, i);
                        continue out;
                    }
                }
            }
            // not escape, but failed to match given marker
            sbBuf.append(delimPrefix);
            d = pattern.indexOf(delimPrefix, i);
        } while (d != -1);
        // append left
        final int i0 = i;
        sbBuf.append(pattern, i0, pattern.length());
        final String last = sbBuf.toString();
        slices.add((t, sb) -> sb.append(last));

        // transfer to array for random access
        final Converter[] slicesArr =
                slices.toArray(new Converter[0]);

        return new Converter<T>() {
            @Override
            public void convert(T t, StringBuilder sb) {
                for (Converter consumer : slicesArr) {
                    consumer.convert(t, sb);
                }
            }

            @Override
            public void stop() {
                for (Converter consumer : slicesArr) {
                    try {
                        consumer.stop();
                    } catch (Throwable ignored) {
                    }
                }
            }
        };
    }

    static String format(String msg, Object[] arr) {
        return format(msg, arr, arr.length);
    }

    static String format(String msg, Object[] arr, int len) {
        // unsafe operation(we do not validate arr and len)
        if (msg == null) {
            return null;
        }

        int d = msg.indexOf(DELIM);
        if (d == -1) {
            return msg;
        }

        final StringBuilder sb = new StringBuilder(msg.length() + 50);
        int i = 0;
        int j = 0;
        do {
            // plain text of marker \{}
            // or
            // plain text of marker \\{}
            boolean mayEscaped = d != 0 && msg.charAt(d - 1) == ESCAPE_CHAR;
            if (mayEscaped) {
                // append "plain text of marker"
                if (d - i > 1) {
                    // only append if chars present.
                    sb.append(msg, i, d - 1);
                }
                if (d < 2 || msg.charAt(d - 2) != ESCAPE_CHAR) {
                    // "/" escaped
                    // plain text of marker \\{}
                    sb.append(DELIM);
                } else {
                    deeplyAppendParameter(sb, arr[j++], null);
                    if (j >= len) {
                        i = d + 2;
                        break;
                    }
                }
            } else {
                if (d - i > 0) {
                    // only append if chars present.
                    sb.append(msg, i, d);
                }
                deeplyAppendParameter(sb, arr[j++], null);
                if (j >= len) {
                    i = d + 2;
                    break;
                }
            }
            d = msg.indexOf(DELIM, (i = d + 2));
        } while (d != -1);
        // append left
        sb.append(msg, i, msg.length());
        return sb.toString();
    }

    // special treatment of array values was suggested by 'lizongbo'
    private static void deeplyAppendParameter(StringBuilder sb, Object o, Set<Object[]> seenSet) {
        if (o == null) {
            sb.append("null");
            return;
        }
        Class<?> clz = o.getClass();
        if (clz.isArray()) {
            // check for primitive array types because they
            // unfortunately cannot be cast to Object[]
            sb.append('[');
            if (clz == boolean[].class) {
                booleanArrayAppend(sb, (boolean[]) o);
            } else if (clz == byte[].class) {
                byteArrayAppend(sb, (byte[]) o);
            } else if (clz == char[].class) {
                charArrayAppend(sb, (char[]) o);
            } else if (clz == short[].class) {
                shortArrayAppend(sb, (short[]) o);
            } else if (clz == int[].class) {
                intArrayAppend(sb, (int[]) o);
            } else if (clz == long[].class) {
                longArrayAppend(sb, (long[]) o);
            } else if (clz == float[].class) {
                floatArrayAppend(sb, (float[]) o);
            } else if (clz == double[].class) {
                doubleArrayAppend(sb, (double[]) o);
            } else {
                objectArrayAppend(sb, (Object[]) o, seenSet);
            }
            sb.append(']');
        } else {
            if (Number.class.isAssignableFrom(clz)) {
                // Prevent String instantiation for some number types
                if (clz == Long.class) {
                    sb.append(((Long) o).longValue());
                } else if (clz == Integer.class || clz == Short.class || clz == Byte.class) {
                    sb.append(((Number) o).intValue());
                } else if (clz == Double.class) {
                    sb.append(((Double) o).doubleValue());
                } else if (clz == Float.class) {
                    sb.append(((Float) o).floatValue());
                } else {
                    safeObjectAppend(sb, o);
                }
            } else {
                safeObjectAppend(sb, o);
            }
        }
    }

    private static void safeObjectAppend(StringBuilder sb, Object o) {
        try {
            String oAsString = o.toString();
            sb.append(oAsString);
        } catch (Throwable t) {
            System.err.println("SLF4J: Failed toString() invocation on an object of type ["
                    + o.getClass().getName()
                    + "]");
            t.printStackTrace();
            sb.append("[FAILED toString()]");
        }
    }

    private static void objectArrayAppend(StringBuilder sb, Object[] a, Set<Object[]> seenSet) {
        if (a.length == 0) {
            return;
        }
        if (seenSet == null) {
            seenSet = new HashSet<>(a.length);
        }
        if (seenSet.add(a)) {
            deeplyAppendParameter(sb, a[0], seenSet);
            for (int i = 1; i < a.length; i++) {
                sb.append(", ");
                deeplyAppendParameter(sb, a[i], seenSet);
            }
            // allow repeats in siblings
            seenSet.remove(a);
        } else {
            sb.append("...");
        }
    }

    private static void booleanArrayAppend(StringBuilder sb, boolean[] a) {
        final int len = a.length;
        for (int i = 0; i < len; i++) {
            sb.append(a[i]);
            if (i != len - 1) {
                sb.append(", ");
            }
        }
    }

    private static void byteArrayAppend(StringBuilder sb, byte[] a) {
        final int len = a.length;
        for (int i = 0; i < len; i++) {
            sb.append(a[i]);
            if (i != len - 1) {
                sb.append(", ");
            }
        }
    }

    private static void charArrayAppend(StringBuilder sb, char[] a) {
        final int len = a.length;
        for (int i = 0; i < len; i++) {
            sb.append(a[i]);
            if (i != len - 1) {
                sb.append(", ");
            }
        }
    }

    private static void shortArrayAppend(StringBuilder sb, short[] a) {
        final int len = a.length;
        for (int i = 0; i < len; i++) {
            sb.append(a[i]);
            if (i != len - 1) {
                sb.append(", ");
            }
        }
    }

    private static void intArrayAppend(StringBuilder sb, int[] a) {
        final int len = a.length;
        for (int i = 0; i < len; i++) {
            sb.append(a[i]);
            if (i != len - 1) {
                sb.append(", ");
            }
        }
    }

    private static void longArrayAppend(StringBuilder sb, long[] a) {
        final int len = a.length;
        for (int i = 0; i < len; i++) {
            sb.append(a[i]);
            if (i != len - 1) {
                sb.append(", ");
            }
        }
    }

    private static void floatArrayAppend(StringBuilder sb, float[] a) {
        final int len = a.length;
        for (int i = 0; i < len; i++) {
            sb.append(a[i]);
            if (i != len - 1) {
                sb.append(", ");
            }
        }
    }

    private static void doubleArrayAppend(StringBuilder sb, double[] a) {
        final int len = a.length;
        for (int i = 0; i < len; i++) {
            sb.append(a[i]);
            if (i != len - 1) {
                sb.append(", ");
            }
        }
    }

}
