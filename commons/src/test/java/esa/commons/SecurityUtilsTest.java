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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SecurityUtilsTest {

    @Test
    void testHexConversion() {
        final byte[] bin = new byte[255];
        for (int i = 0; i < bin.length; i++) {
            bin[i] = (byte) (i - 128);
        }
        final String hex = SecurityUtils.binToHex(bin);
        for (int i = 0; i < bin.length; i++) {
            assertEquals(bin[i], SecurityUtils.hexToBin(hex.substring(i * 2, i * 2 + 2))[0]);
        }
    }

    @Test
    void testMd5Encode() {
        assertNull(SecurityUtils.md5Encode(null));
        final String encoded = SecurityUtils.md5Encode("foo");
        assertNotNull(encoded);
        assertEquals(encoded, SecurityUtils.md5Encode("foo"));
    }

    @Test
    void testHmacSHA1() {
        assertEquals("d373670db3c99ebfa96060e993c340ccf6dd079e",
                SecurityUtils.getHmacSHA1("hello".getBytes(), "abc"));
    }

    @Test
    void testECSDA() {
        final SecurityUtils.ECDSA.ECDSAKey key = SecurityUtils.ECDSA.genKey(256);
        assertNotNull(key);
        assertTrue(key.toString().contains(key.getPrivateKey()));
        assertTrue(key.toString().contains(key.getPublicKey()));
        final byte[] data = "hello".getBytes();

        assertTrue(SecurityUtils.ECDSA.verify(data,
                SecurityUtils.ECDSA.sign(data, key.getPrivateKey()),
                key.getPublicKey()));
    }

    @Test
    void testRSA() {
        final SecurityUtils.RSA.RSAKey key = SecurityUtils.RSA.genKey(1024);
        assertNotNull(key);
        assertTrue(key.toString().contains(key.getPrivateKey()));
        assertTrue(key.toString().contains(key.getPublicKey()));
        final byte[] data = "hello".getBytes();
        assertArrayEquals(data,
                SecurityUtils.RSA.decryptByPrivateKey(SecurityUtils.RSA.encryptByPublicKey(data, key.getPublicKey()),
                        key.getPrivateKey()));

        assertTrue(SecurityUtils.RSA.verify(data,
                SecurityUtils.RSA.sign(data, key.getPrivateKey()),
                key.getPublicKey()));
    }

    @Test
    void testAES() {
        final String key = SecurityUtils.binToHex(SecurityUtils.AES.genKey(128));
        assertNotNull(key);
        final byte[] data = "hello".getBytes();
        final byte[] iv = "abcdefghijklmnop".getBytes();
        assertArrayEquals(data,
                SecurityUtils.AES.CBC.decrypt(SecurityUtils.AES.CBC.encrypt(data, key, iv), key, iv));
    }


}
