package esa.commons.http;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HttpVersionTest {

    @Test
    void testHttpVersions() {
        assertEquals("HTTP_1_0", HttpVersion.HTTP_1_0.name());
        assertEquals("HTTP_1_1", HttpVersion.HTTP_1_1.name());
        assertEquals("HTTP_2", HttpVersion.HTTP_2.name());
    }

}
