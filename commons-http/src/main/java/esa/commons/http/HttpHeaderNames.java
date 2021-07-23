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
 * Lowercase standard Http header names.
 * @deprecated use https://github.com/esastack/esa-commons-net instead
 */
@Deprecated
public final class HttpHeaderNames {

    public static final String ACCEPT = "accept";
    public static final String ACCEPT_CHARSET = "accept-charset";
    public static final String ACCEPT_ENCODING = "accept-encoding";
    public static final String ACCEPT_LANGUAGE = "accept-language";
    public static final String ACCEPT_RANGES = "accept-ranges";
    public static final String ACCEPT_PATCH = "accept-patch";
    public static final String ACCESS_CONTROL_ALLOW_CREDENTIALS =
            "access-control-allow-credentials";
    public static final String ACCESS_CONTROL_ALLOW_HEADERS =
            "access-control-allow-headers";
    public static final String ACCESS_CONTROL_ALLOW_METHODS =
            "access-control-allow-methods";
    public static final String ACCESS_CONTROL_ALLOW_ORIGIN =
            "access-control-allow-origin";
    public static final String ACCESS_CONTROL_EXPOSE_HEADERS =
            "access-control-expose-headers";
    public static final String ACCESS_CONTROL_MAX_AGE = "access-control-max-age";
    public static final String ACCESS_CONTROL_REQUEST_HEADERS =
            "access-control-request-headers";
    public static final String ACCESS_CONTROL_REQUEST_METHOD =
            "access-control-request-method";
    public static final String AGE = "age";
    public static final String ALLOW = "allow";
    public static final String AUTHORIZATION = "authorization";
    public static final String CACHE_CONTROL = "cache-control";
    public static final String CONNECTION = "connection";
    public static final String CONTENT_BASE = "content-base";
    public static final String CONTENT_ENCODING = "content-encoding";
    public static final String CONTENT_LANGUAGE = "content-language";
    public static final String CONTENT_LENGTH = "content-length";
    public static final String CONTENT_LOCATION = "content-location";
    public static final String CONTENT_TRANSFER_ENCODING = "content-transfer-encoding";
    public static final String CONTENT_DISPOSITION = "content-disposition";
    public static final String CONTENT_MD5 = "content-md5";
    public static final String CONTENT_RANGE = "content-range";
    public static final String CONTENT_SECURITY_POLICY = "content-security-policy";
    public static final String CONTENT_TYPE = "content-type";
    public static final String COOKIE = "cookie";
    public static final String DATE = "date";
    public static final String DNT = "dnt";
    public static final String ETAG = "etag";
    public static final String EXPECT = "expect";
    public static final String EXPIRES = "expires";
    public static final String FROM = "from";
    public static final String HOST = "host";
    public static final String IF_MATCH = "if-match";
    public static final String IF_MODIFIED_SINCE = "if-modified-since";
    public static final String IF_NONE_MATCH = "if-none-match";
    public static final String IF_RANGE = "if-range";
    public static final String IF_UNMODIFIED_SINCE = "if-unmodified-since";
    public static final String LAST_MODIFIED = "last-modified";
    public static final String LOCATION = "location";
    public static final String MAX_FORWARDS = "max-forwards";
    public static final String ORIGIN = "origin";
    public static final String PRAGMA = "pragma";
    public static final String PROXY_AUTHENTICATE = "proxy-authenticate";
    public static final String PROXY_AUTHORIZATION = "proxy-authorization";
    public static final String RANGE = "range";
    public static final String REFERER = "referer";
    public static final String RETRY_AFTER = "retry-after";
    public static final String SEC_WEBSOCKET_KEY1 = "sec-websocket-key1";
    public static final String SEC_WEBSOCKET_KEY2 = "sec-websocket-key2";
    public static final String SEC_WEBSOCKET_LOCATION = "sec-websocket-location";
    public static final String SEC_WEBSOCKET_ORIGIN = "sec-websocket-origin";
    public static final String SEC_WEBSOCKET_PROTOCOL = "sec-websocket-protocol";
    public static final String SEC_WEBSOCKET_VERSION = "sec-websocket-version";
    public static final String SEC_WEBSOCKET_KEY = "sec-websocket-key";
    public static final String SEC_WEBSOCKET_ACCEPT = "sec-websocket-accept";
    public static final String SEC_WEBSOCKET_EXTENSIONS = "sec-websocket-extensions";
    public static final String SERVER = "server";
    public static final String SET_COOKIE = "set-cookie";
    public static final String SET_COOKIE2 = "set-cookie2";
    public static final String TE = "te";
    public static final String TRAILER = "trailer";
    public static final String TRANSFER_ENCODING = "transfer-encoding";
    public static final String UPGRADE = "upgrade";
    public static final String UPGRADE_INSECURE_REQUESTS = "upgrade-insecure-requests";
    public static final String USER_AGENT = "user-agent";
    public static final String VARY = "vary";
    public static final String VIA = "via";
    public static final String WARNING = "warning";
    public static final String WEBSOCKET_LOCATION = "websocket-location";
    public static final String WEBSOCKET_ORIGIN = "websocket-origin";
    public static final String WEBSOCKET_PROTOCOL = "websocket-protocol";
    public static final String WWW_AUTHENTICATE = "www-authenticate";
    public static final String X_FRAME_OPTIONS = "x-frame-options";
    public static final String X_REQUESTED_WITH = "x-requested-with";

    private HttpHeaderNames() {
    }
}
