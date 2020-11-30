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
package esa.commons.http;

import esa.commons.Checks;
import esa.commons.StringUtils;
import esa.commons.function.Function3;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public class MimeType {

    private static final Function3<String, String, Map<String, String>, MimeType> GENERATOR
            = MimeType::new;

    private static final char[] SEPARATORS = {'(', ')', '<', '>', '@',
            ',', ';', ':', '\\', '\"',
            '/', '[', ']', '?', '=',
            '{', '}', ' ', '\t'};

    private static final boolean[] TOKEN = new boolean[128];
    private static final String CHARSET_KEY = "charset";

    static {
        // see RFC 2616, section 2.2
        // token = 1*<any CHAR except CTLs or separators>
        for (int i = 0; i < TOKEN.length; i++) {
            boolean token = true;
            // exclude CTLs(0-31, 127)
            if (i < 32 || i == 127) {
                token = false;
            } else {
                for (char s : SEPARATORS) {
                    if (i == s) {
                        // exclude separators
                        token = false;
                        break;
                    }
                }
            }
            TOKEN[i] = token;
        }
    }

    public static final String WILDCARD_TYPE = "*";
    private static final char PARAMETER_SEPARATOR = ';';
    private static final char SUB_TYPE_SEPARATOR = '/';

    public static boolean isValidToken(CharSequence token) {
        if (token != null) {
            for (int i = 0; i < token.length(); i++) {
                if (isInvalidToken(token.charAt(i))) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void checkToken(CharSequence token) {
        if (token != null) {
            for (int i = 0; i < token.length(); i++) {
                if (isInvalidToken(token.charAt(i))) {
                    throw new IllegalArgumentException("Invalid token character '"
                            + token.charAt(i) + "' in \"" + token + "\"");
                }
            }
        }
    }

    private static boolean isInvalidToken(char ch) {
        return ch <= 0 || ch >= 127 || !TOKEN[ch];
    }

    private final String type;
    private final String subtype;
    private final Map<String, String> parameters;

    private String value;

    public MimeType(String type) {
        this(type, WILDCARD_TYPE);
    }

    public MimeType(String type, String subtype) {
        this(type, subtype, Collections.emptyMap());
    }

    public MimeType(String type, String subtype, Charset charset) {
        this(type, subtype, Collections.singletonMap(CHARSET_KEY, charset.name()));
    }

    public MimeType(String type, String subtype, String charset) {
        this(type, subtype, Collections.singletonMap(CHARSET_KEY, charset));
    }

    public MimeType(String type, String subtype, Map<String, String> parameters) {
        this(type, subtype, parameters, true);
    }

    public MimeType(String type, String subtype, Map<String, String> parameters, boolean checkToken) {
        Checks.checkNotEmptyArg(type, "type must not be null or empty.");
        Checks.checkNotEmptyArg(subtype, "subtype must not be null or empty.");
        if (checkToken) {
            checkToken(type);
            checkToken(subtype);
        }

        type = type.toLowerCase(Locale.ENGLISH);
        this.type = WILDCARD_TYPE.equals(type) ? WILDCARD_TYPE : type;

        subtype = subtype.toLowerCase(Locale.ENGLISH);
        this.subtype = WILDCARD_TYPE.equals(subtype) ? WILDCARD_TYPE : subtype;

        if (parameters == null || parameters.isEmpty()) {
            this.parameters = Collections.emptyMap();
        } else {
            Map<String, String> paramsMap = new LinkedHashMap<>(parameters.size());
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                Checks.checkNotEmptyArg(entry.getKey(),
                        "attribute must not be null or empty.");
                Checks.checkNotEmptyArg(entry.getValue(),
                        "parameter value must not be null or empty.");
                if (checkToken) {
                    checkToken(entry.getKey());
                }

                String value = entry.getValue();

                if (StringUtils.isQuoted(value)) {
                    value = StringUtils.unquote(value);
                } else {
                    if (checkToken) {
                        checkToken(value);
                    }
                }

                paramsMap.put(entry.getKey().toLowerCase(Locale.ENGLISH),
                        StringUtils.unquote(value));
            }
            this.parameters = Collections.unmodifiableMap(paramsMap);
        }
    }

    public String type() {
        return type;
    }

    public String subtype() {
        return subtype;
    }

    public Charset charset() {
        String charset = getParameter(CHARSET_KEY);
        return (charset != null ? Charset.forName(StringUtils.unquote(charset)) : null);
    }

    public Map<String, String> parameters() {
        return parameters;
    }

    public String getParameter(String name) {
        return this.parameters.get(name);
    }

    public boolean isWildcardType() {
        return WILDCARD_TYPE.equals(type());
    }

    public boolean isWildcardSubtype() {
        return WILDCARD_TYPE.equals(subtype()) || subtype().startsWith("*+");
    }

    @Deprecated
    public boolean isWildcardSubType() {
        return isWildcardSubtype();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(type).append(SUB_TYPE_SEPARATOR).append(subtype);
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            sb.append(PARAMETER_SEPARATOR)
                    .append(entry.getKey())
                    .append('=')
                    .append(entry.getValue());
        }
        return sb.toString();
    }

    public String value() {
        if (value == null) {
            value = toString();
        }
        return value;
    }

    public static MimeType of(String type) {
        return new MimeType(type);
    }

    public static MimeType of(String type, String subType) {
        return new MimeType(type, subType);
    }

    public static MimeType of(String type, String subType, Charset charset) {
        return new MimeType(type, subType, charset);
    }

    public static MimeType of(String type, String subType, String charset) {
        return new MimeType(type, subType, charset);
    }

    public static MimeType of(String type, String subType, Map<String, String> parameters) {
        return new MimeType(type, subType, parameters);
    }

    public static MimeType of(String type, String subType, Map<String, String> parameters, boolean checkToken) {
        return new MimeType(type, subType, parameters, checkToken);
    }

    public static MimeType parseMimeType(String mimeType) {
        return parseMimeType(mimeType, GENERATOR);
    }

    public static <T extends MimeType> T parseMimeType(String mimeType,
                                                       Function3<String, String, Map<String, String>, T> generator) {
        if (StringUtils.isEmpty(mimeType)) {
            throw new IllegalArgumentException("Invalid mime type string '" + "null'");
        }

        int separatorIdx = mimeType.indexOf(PARAMETER_SEPARATOR);
        int st = 0;
        int len = separatorIdx < 0 ? mimeType.length() : separatorIdx;
        // trim
        while ((st < len) && (mimeType.charAt(st) <= ' ')) {
            st++;
        }
        while ((st < len) && (mimeType.charAt(len - 1) <= ' ')) {
            len--;
        }

        if (len - st <= 0) {
            throw new IllegalArgumentException("MimeType must not be empty");
        }
        String fullType = ((st > 0) || (len < mimeType.length())) ? mimeType.substring(st, len) : mimeType;

        int subTypeIndex;
        if (WILDCARD_TYPE.equals(fullType)) {
            fullType = "*/*";
            subTypeIndex = 1;
        } else {
            subTypeIndex = fullType.indexOf(SUB_TYPE_SEPARATOR);
            if (subTypeIndex < 0) {
                throw new IllegalArgumentException("MimeType('" + mimeType + "') must contains '/'");
            }

            if (subTypeIndex == fullType.length() - 1) {
                throw new IllegalArgumentException("MimeType('" + mimeType + "') must contains a subtype");
            }
        }

        String type = fullType.substring(0, subTypeIndex);
        String subType = fullType.substring(subTypeIndex + 1);

        Map<String, String> parameters = null;
        if (separatorIdx >= 0 && separatorIdx < mimeType.length()) {

            while (separatorIdx < mimeType.length()) {
                int cursor = separatorIdx + 1;
                boolean quotePresent = false;
                while (cursor < mimeType.length()) {
                    char ch = mimeType.charAt(cursor);
                    if (ch == ';') {
                        if (!quotePresent) {
                            break;
                        }
                    } else if (ch == '"') {
                        quotePresent = !quotePresent;
                    }
                    cursor++;
                }
                String parameter = mimeType.substring(separatorIdx + 1, cursor).trim();
                if (parameter.length() > 0) {
                    if (parameters == null) {
                        parameters = new LinkedHashMap<>(4);
                    }
                    int eqIndex = parameter.indexOf('=');
                    if (eqIndex >= 0) {
                        String key = parameter.substring(0, eqIndex).trim();
                        String value = parameter.substring(eqIndex + 1).trim();
                        parameters.put(key, value);
                    }
                }
                separatorIdx = cursor;
            }
        }

        return generator.apply(type, subType, parameters);
    }

    public static List<MimeType> parseMimeTypes(String mimeTypes) {
        return parseMimeTypes(mimeTypes, GENERATOR);
    }

    public static <T extends MimeType> List<T> parseMimeTypes(String mimeTypes,
                                                              Function3<String, String, Map<String, String>, T> g) {
        return parseMimeTypes(mimeTypes, s -> parseMimeType(s, g));
    }

    public static <T extends MimeType> List<T> parseMimeTypes(String mimeTypes,
                                                              Function<String, T> parser) {
        List<T> parsed = new LinkedList<>();
        parseMimeTypes(mimeTypes, parser, parsed);
        return parsed;
    }

    public static <T extends MimeType> void parseMimeTypes(String mimeTypes,
                                                           Function<String, T> parser,
                                                           List<? super T> target) {
        if (StringUtils.isEmpty(mimeTypes)) {
            return;
        }
        int start = 0;
        boolean quotePresent = false;
        char ch;
        for (int i = start; i < mimeTypes.length(); i++) {
            ch = mimeTypes.charAt(i);
            if (ch == '"') {
                quotePresent = !quotePresent;
            } else if (ch == ',') {
                if (!quotePresent) {
                    target.add(parser.apply(mimeTypes.substring(start, i)));
                    start = i + 1;
                }
            }
        }
        target.add(parser.apply(mimeTypes.substring(start)));
    }

    /**
     * Indicate whether this type includes the given type.
     * <p>
     * eg: {@code text/*} includes {@code text/plain}, and {@code application/*+xml} includes {@code
     * application/soap+xml}
     *
     * @return {@code true} if this type includes the given type; {@code false} otherwise
     */
    public boolean includes(MimeType other) {
        if (other == null) {
            return false;
        }
        if (isWildcardType()) {
            return true;
        } else if (type().equals(other.type())) {
            if (subtype().equals(other.subtype())) {
                return true;
            }
            if (isWildcardSubtype()) {
                int plusIdx = subtype().lastIndexOf('+');
                if (plusIdx == -1) {
                    return true;
                } else {
                    int plusIdx1 = other.subtype().lastIndexOf('+');
                    if (plusIdx1 != -1) {
                        return subtype().substring(plusIdx + 1).equals(other.subtype().substring(plusIdx1 + 1))
                                && WILDCARD_TYPE.equals(subtype().substring(0, plusIdx));
                    }
                }
            }
        }
        return false;
    }

    /**
     * Indicate whether this {@code MediaType} is compatible with the given media type.
     * <p>
     * eg: {@code text/*} is compatible with {@code text/plain}
     *
     * @return {@code true} if this media type is compatible with the given media type; {@code false} otherwise
     */
    public boolean isCompatibleWith(MimeType other) {
        if (other == null) {
            return false;
        }
        if (isWildcardType() || other.isWildcardType()) {
            return true;
        } else if (type().equals(other.type())) {
            if (subtype().equals(other.subtype())) {
                return true;
            }
            if (isWildcardSubtype() || other.isWildcardSubtype()) {
                int plusIdx = subtype().lastIndexOf('+');
                int plusIdx1 = other.subtype().lastIndexOf('+');
                if (plusIdx == -1 && plusIdx1 == -1) {
                    return true;
                } else if (plusIdx != -1 && plusIdx1 != -1) {
                    return subtype().substring(plusIdx + 1).equals(other.subtype().substring(plusIdx1 + 1))
                            && (WILDCARD_TYPE.equals(subtype().substring(0, plusIdx))
                            || WILDCARD_TYPE.equals(other.subtype().substring(0, plusIdx1)));
                }
            }
        }
        return false;
    }

    public static class SpecificityComparator<T extends MimeType> implements Comparator<T> {

        @Override
        public int compare(T mimeType1, T mimeType2) {
            if (mimeType1.isWildcardType() && !mimeType2.isWildcardType()) {
                return 1;
            } else if (!mimeType1.isWildcardType() && mimeType2.isWildcardType()) {
                return -1;
            } else if (!mimeType1.type().equals(mimeType2.type())) {
                return 0;
            } else {
                if (mimeType1.isWildcardSubtype() && !mimeType2.isWildcardSubtype()) {
                    return 1;
                } else if (!mimeType1.isWildcardSubtype() && mimeType2.isWildcardSubtype()) {
                    return -1;
                } else if (!mimeType1.subtype().equals(mimeType2.subtype())) {
                    return 0;
                } else {
                    return Integer.compare(mimeType2.parameters().size(), mimeType1.parameters().size());
                }
            }
        }
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof MimeType)) {
            return false;
        }
        MimeType otherType = (MimeType) other;
        if (this.type.equalsIgnoreCase(otherType.type) &&
                this.subtype.equalsIgnoreCase(otherType.subtype)) {

            if (this.parameters.size() != otherType.parameters.size()) {
                return false;
            }

            for (Map.Entry<String, String> entry : this.parameters.entrySet()) {
                String key = entry.getKey();
                if (!otherType.parameters.containsKey(key)) {
                    return false;
                }
                if (CHARSET_KEY.equals(key)) {
                    if (!Objects.equals(charset(), otherType.charset())) {
                        return false;
                    }
                } else if (!Objects.equals(entry.getValue(), otherType.parameters.get(key))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
