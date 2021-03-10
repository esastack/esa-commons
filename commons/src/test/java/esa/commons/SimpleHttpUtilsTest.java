package esa.commons;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import esa.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SimpleHttpUtilsTest {

    @Test
    void testPostUnavailableServers() throws IOException {
        final int unboundPort = NetworkUtils.selectRandomPort();
        final List<String> urls = Collections.singletonList("http://127.0.0.1:" + unboundPort);
        final Map<String, String> headers = Collections.singletonMap("foo", "1");
        final byte[] data = "hello".getBytes();
        assertNull(SimpleHttpUtils.sendPost(null, headers, data));
        assertNull(SimpleHttpUtils.sendPost(Collections.emptyList(), headers, data));
        assertNull(SimpleHttpUtils.sendPost(urls, headers, null));
        assertNull(SimpleHttpUtils.sendPost(urls, headers, new byte[0]));

        assertThrows(IOException.class, () -> SimpleHttpUtils.sendPost(urls, headers, data));
    }

    @Test
    void testPost() throws IOException {
        final int port = NetworkUtils.selectRandomPort();
        final HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        try {
            server.createContext("/foo", new HttpHandler() {
                @Override
                public void handle(HttpExchange exchange) throws IOException {
                    exchange.sendResponseHeaders(200, 0);
                    final InputStream in = exchange.getRequestBody();
                    final OutputStream out = exchange.getResponseBody();
                    try {
                        IOUtils.copy(in, out);
                    } finally {
                        IOUtils.closeQuietly(in);
                        IOUtils.closeQuietly(out);
                    }
                }
            });
            server.start();
            final List<String> urls = Collections.singletonList("http://127.0.0.1:" + port + "/foo");
            final Map<String, String> headers = Collections.singletonMap("foo", "1");
            final byte[] data = "hello".getBytes();
            assertArrayEquals(data, SimpleHttpUtils.sendPost(urls, headers, data));
        } finally {
            server.stop(0);
        }
    }

}
