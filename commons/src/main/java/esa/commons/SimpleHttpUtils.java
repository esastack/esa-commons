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

import esa.commons.annotation.Beta;
import esa.commons.io.IOUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Beta
public final class SimpleHttpUtils {

    public static byte[] sendPost(List<String> urls,
                                  Map<String, String> headers,
                                  byte[] bodyBytes) throws IOException {
        if (urls == null || urls.size() == 0 || bodyBytes == null || bodyBytes.length == 0) {
            return null;
        }

        return sendPost(urls, headers, bodyBytes, new ArrayList<>());
    }

    private static byte[] sendPost(final List<String> urls,
                                   Map<String, String> headers,
                                   byte[] bodyBytes,
                                   final List<String> failUrls) throws IOException {
        List<String> urlsCopy = new ArrayList<>(urls);

        urlsCopy.removeAll(failUrls);

        int size = urlsCopy.size();
        if (size == 0) {
            throw new IOException("no available server.");
        }

        int index = ThreadLocalRandom.current().nextInt(size);
        String url = urlsCopy.get(index);

        DataOutputStream outputStream = null;
        InputStream inputStream = null;
        InputStream errStream = null;
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) new URL(url).openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setConnectTimeout(3000);
            urlConnection.setReadTimeout(10000);
            urlConnection.setUseCaches(false);
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            if (headers != null) {
                for (Map.Entry<String, String> row : headers.entrySet()) {
                    urlConnection.setRequestProperty(row.getKey(), row.getValue());
                }
            }

            urlConnection.setRequestProperty("Connection", "Keep-Alive");

            outputStream = new DataOutputStream(urlConnection.getOutputStream());

            IOUtils.write(bodyBytes, outputStream);
            IOUtils.closeQuietly(outputStream);

            int respCode = urlConnection.getResponseCode();

            if (200 == respCode) {
                inputStream = urlConnection.getInputStream();
                return IOUtils.toByteArray(inputStream);
            } else if (408 == respCode) {
                failUrls.add(url);
                return sendPost(urls, headers, bodyBytes, failUrls);
            } else {
                throw new IOException("sendPost error,url=" +
                        url + ",Unexpected HTTP response: " + respCode + " " + respCode);
            }
        } catch (IOException e) {
            if (urlConnection != null) {
                errStream = urlConnection.getErrorStream();
            }

            if (e instanceof java.net.ConnectException
                    || (e instanceof java.net.SocketTimeoutException)) {
                failUrls.add(url);
                return sendPost(urls, headers, bodyBytes, failUrls);
            }

            throw e;
        } finally {
            IOUtils.closeQuietly(outputStream);
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(errStream);
        }
    }

    private SimpleHttpUtils() {
    }
}
