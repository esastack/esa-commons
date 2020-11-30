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

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public final class UrlUtils {

    /**
     * Prepends a {@code /} for the given patterns if necessary.
     *
     * @param patterns patterns
     * @return new patterns
     */
    public static Set<String> prependLeadingSlash(Collection<String> patterns) {
        Checks.checkNotNull(patterns, "patterns");
        Set<String> result = new LinkedHashSet<>(patterns.size());
        for (String pattern : patterns) {
            result.add(prependLeadingSlash(pattern));
        }
        return result;
    }

    /**
     * Prepends a {@code /} for the given patterns if necessary.
     *
     * @param patterns patterns
     */
    public static void prependLeadingSlash(String[] patterns) {
        Checks.checkNotNull(patterns, "patterns");
        for (int i = 0; i < patterns.length; i++) {
            patterns[i] = prependLeadingSlash(patterns[i]);
        }
    }

    /**
     * Prepends a {@code /} for the given pattern string if necessary.
     *
     * @param pattern pattern
     * @return new pattern
     */
    public static String prependLeadingSlash(String pattern) {
        if (!StringUtils.isEmpty(pattern) && !pattern.startsWith("/")) {
            pattern = "/" + pattern;
        }
        return pattern;
    }

    private UrlUtils() {
    }
}
