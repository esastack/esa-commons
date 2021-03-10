package esa.commons.io;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class CompressUtilsTest {

    @Test
    void testGzipCompress() throws IOException {
        final byte[] forTest = "hello world!".getBytes(StandardCharsets.UTF_8);
        assertArrayEquals(forTest, CompressUtils.gzipDecompress(CompressUtils.gzipCompress(forTest)));
    }

}
