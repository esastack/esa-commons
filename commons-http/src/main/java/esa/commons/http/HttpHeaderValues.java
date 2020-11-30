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

/**
 * Lowercase standard Http header values.
 */
public final class HttpHeaderValues {
    public static final String APPLICATION_JSON = "application/json";
    public static final String APPLICATION_X_WWW_FORM_URLENCODED =
            "application/x-www-form-urlencoded";
    public static final String APPLICATION_OCTET_STREAM = "application/octet-stream";
    public static final String APPLICATION_XHTML = "application/xhtml+xml";
    public static final String APPLICATION_XML = "application/xml";
    public static final String ATTACHMENT = "attachment";
    public static final String BASE64 = "base64";
    public static final String BINARY = "binary";
    public static final String BOUNDARY = "boundary";
    public static final String BYTES = "bytes";
    public static final String CHARSET = "charset";
    public static final String CHUNKED = "chunked";
    public static final String CLOSE = "close";
    public static final String COMPRESS = "compress";
    public static final String CONTINUE = "100-continue";
    public static final String DEFLATE = "deflate";
    public static final String X_DEFLATE = "x-deflate";
    public static final String FILE = "file";
    public static final String FILENAME = "filename";
    public static final String FORM_DATA = "form-data";
    public static final String GZIP = "gzip";
    public static final String GZIP_DEFLATE = "gzip,deflate";
    public static final String X_GZIP = "x-gzip";
    public static final String IDENTITY = "identity";
    public static final String KEEP_ALIVE = "keep-alive";
    public static final String MAX_AGE = "max-age";
    public static final String MAX_STALE = "max-stale";
    public static final String MIN_FRESH = "min-fresh";
    public static final String MULTIPART_FORM_DATA = "multipart/form-data";
    public static final String MULTIPART_MIXED = "multipart/mixed";
    public static final String MUST_REVALIDATE = "must-revalidate";
    public static final String NAME = "name";
    public static final String NO_CACHE = "no-cache";
    public static final String NO_STORE = "no-store";
    public static final String NO_TRANSFORM = "no-transform";
    public static final String NONE = "none";
    public static final String ZERO = "0";
    public static final String ONLY_IF_CACHED = "only-if-cached";
    public static final String PRIVATE = "private";
    public static final String PROXY_REVALIDATE = "proxy-revalidate";
    public static final String PUBLIC = "public";
    public static final String QUOTED_PRINTABLE = "quoted-printable";
    public static final String S_MAXAGE = "s-maxage";
    public static final String TEXT_CSS = "text/css";
    public static final String TEXT_HTML = "text/html";
    public static final String TEXT_EVENT_STREAM = "text/event-stream";
    public static final String TEXT_PLAIN = "text/plain";
    public static final String TRAILERS = "trailers";
    public static final String UPGRADE = "upgrade";
    public static final String WEBSOCKET = "websocket";
    public static final String XML_HTTP_REQUEST = "XmlHttpRequest";

    private HttpHeaderValues() {
    }
}

