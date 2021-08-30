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

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@Beta
public final class SecurityUtils {

    public static String binToHex(byte[] buf) {
        StringBuilder sb = new StringBuilder(buf.length * 2);
        int i;
        for (i = 0; i < buf.length; i++) {
            if (((int) buf[i] & 0xff) < 0x10) {
                sb.append("0");
            }
            sb.append(Long.toString((int) buf[i] & 0xff, 16));
        }
        return sb.toString();
    }

    public static byte[] hexToBin(String src) {
        if (src.length() < 1) {
            return null;
        }

        byte[] encrypted = new byte[src.length() / 2];
        for (int i = 0; i < src.length() / 2; i++) {
            int high = Integer.parseInt(src.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(src.substring(i * 2 + 1, i * 2 + 2), 16);
            encrypted[i] = (byte) (high * 16 + low);
        }
        return encrypted;
    }

    public static String md5Encode(String str) {
        if (str == null) {
            return null;
        }
        MessageDigest messageDigest;

        try {
            messageDigest = MessageDigest.getInstance("MD5");

            messageDigest.reset();

            messageDigest.update(str.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("md5 encode error", e);
        }

        byte[] byteArray = messageDigest.digest();

        StringBuilder md5StrBuff = new StringBuilder();

        for (byte aByteArray : byteArray) {
            if (Integer.toHexString(0xFF & aByteArray).length() == 1) {
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & aByteArray));
            } else {
                md5StrBuff.append(Integer.toHexString(0xFF & aByteArray));
            }
        }
        return md5StrBuff.toString();
    }

    /**
     * @deprecated weak encryption algorithm
     */
    @Deprecated
    public static String getHmacSHA1(byte[] data, final String key) {
        try {
            Mac mac = Mac.getInstance("HmacSHA1");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), mac.getAlgorithm());
            mac.init(secretKeySpec);
            return binToHex(mac.doFinal(data));
        } catch (Exception e) {
            throw new RuntimeException("HMAC-SHA1 encode error", e);
        }
    }

    public static class ECDSA {
        private static final String KEY_ALGORITHM = "EC";

        public static class ECDSAKey {
            private final String publicKey;
            private final String privateKey;

            public ECDSAKey(String publicKey, String privateKey) {
                this.publicKey = publicKey;
                this.privateKey = privateKey;
            }

            public String getPublicKey() {
                return publicKey;
            }

            public String getPrivateKey() {
                return privateKey;
            }

            @Override
            public String toString() {
                return "ECDSAKey{" +
                        "publicKey='" + publicKey + '\'' +
                        ", privateKey='" + privateKey + '\'' +
                        '}';
            }
        }

        public static ECDSAKey genKey(int keySize) {
            try {
                KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
                keyPairGenerator.initialize(keySize);
                KeyPair keyPair = keyPairGenerator.generateKeyPair();
                Key publicKey = keyPair.getPublic();
                Key privateKey = keyPair.getPrivate();
                return new ECDSAKey(binToHex(publicKey.getEncoded()), binToHex(privateKey.getEncoded()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public static byte[] sign(byte[] data, String privateKey) {
            try {
                PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(hexToBin(privateKey));

                KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
                PrivateKey key = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
                Signature signature = Signature.getInstance("SHA1withECDSA");
                signature.initSign(key);
                signature.update(data);
                return signature.sign();
            } catch (Exception e) {
                throw new RuntimeException("sign with ecdsa error", e);
            }
        }

        public static boolean verify(byte[] data, byte[] sign, String publicKey) {
            try {
                X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(hexToBin(publicKey));
                KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
                PublicKey key = keyFactory.generatePublic(x509EncodedKeySpec);
                Signature signature = Signature.getInstance("SHA1withECDSA");
                signature.initVerify(key);
                signature.update(data);
                return signature.verify(sign);
            } catch (Exception e) {
                throw new RuntimeException("verify sign with ecdsa error", e);
            }
        }
    }

    public static class RSA {
        public static final String KEY_ALGORITHM = "RSA";

        public static class RSAKey {
            private String publicKey;
            private String privateKey;

            public String getPublicKey() {
                return publicKey;
            }

            public void setPublicKey(String publicKey) {
                this.publicKey = publicKey;
            }

            public String getPrivateKey() {
                return privateKey;
            }

            public void setPrivateKey(String privateKey) {
                this.privateKey = privateKey;
            }

            @Override
            public String toString() {
                return "RSAKey{" +
                        "publicKey='" + publicKey + '\'' +
                        ", privateKey='" + privateKey + '\'' +
                        '}';
            }
        }

        public static byte[] sign(byte[] data, String privateKey) {
            try {
                PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(hexToBin(privateKey));

                KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
                PrivateKey key = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
                Signature signature = Signature.getInstance("SHA1withRSA");
                signature.initSign(key);
                signature.update(data);
                return signature.sign();
            } catch (Exception e) {
                throw new RuntimeException("sign with rsa error", e);
            }
        }

        public static boolean verify(byte[] data, byte[] sign, String publicKey) {
            try {
                X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(hexToBin(publicKey));
                KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
                PublicKey key = keyFactory.generatePublic(x509EncodedKeySpec);
                Signature signature = Signature.getInstance("SHA1withRSA");
                signature.initVerify(key);
                signature.update(data);
                return signature.verify(sign);
            } catch (Exception e) {
                throw new RuntimeException("verify sign with rsa error", e);
            }
        }

        public static byte[] decryptByPrivateKey(byte[] data, byte[] key) {
            try {
                PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(key);
                KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

                PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);

                Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
                cipher.init(Cipher.DECRYPT_MODE, privateKey);
                return cipher.doFinal(data);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public static byte[] decryptByPrivateKey(byte[] data, String key) {
            byte[] keyBytes = hexToBin(key);
            return decryptByPrivateKey(data, keyBytes);
        }


        public static byte[] encryptByPublicKey(byte[] data, byte[] key) {
            try {
                X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(key);
                KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

                PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);

                Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
                cipher.init(Cipher.ENCRYPT_MODE, publicKey);
                return cipher.doFinal(data);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public static byte[] encryptByPublicKey(byte[] data, String key) {
            byte[] keyBytes = hexToBin(key);
            return encryptByPublicKey(data, keyBytes);
        }

        public static RSAKey genKey(int keySize) {
            try {
                KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);

                keyPairGenerator.initialize(keySize);

                KeyPair keyPair = keyPairGenerator.generateKeyPair();

                Key publicKey = keyPair.getPublic();

                Key privateKey = keyPair.getPrivate();

                RSAKey rsaKey = new RSAKey();
                rsaKey.setPublicKey(binToHex(publicKey.getEncoded()));
                rsaKey.setPrivateKey(binToHex(privateKey.getEncoded()));

                return rsaKey;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class AES {

        public static byte[] genKey(int size) {
            try {
                KeyGenerator kgen = KeyGenerator.getInstance("AES");
                kgen.init(size);
                SecretKey skey = kgen.generateKey();
                return skey.getEncoded();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public static class CBC {
            private static final String CBC = "AES/CBC/PKCS5Padding";

            public static byte[] encrypt(byte[] data, byte[] key, byte[] iv) {
                try {
                    SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
                    Cipher cipher = Cipher.getInstance(CBC);
                    AlgorithmParameterSpec params = new IvParameterSpec(iv);
                    cipher.init(Cipher.ENCRYPT_MODE, skeySpec, params);
                    return cipher.doFinal(data);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            public static byte[] encrypt(byte[] data, String key, byte[] iv) {
                byte[] keyBytes = hexToBin(key);
                return encrypt(data, keyBytes, iv);
            }

            public static byte[] decrypt(byte[] encryptedData, byte[] key, byte[] iv) {
                try {
                    SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
                    Cipher cipher = Cipher.getInstance(CBC);
                    AlgorithmParameterSpec params = new IvParameterSpec(iv);
                    cipher.init(Cipher.DECRYPT_MODE, skeySpec, params);
                    return cipher.doFinal(encryptedData);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            public static byte[] decrypt(byte[] encryptedData, String key, byte[] iv) {
                byte[] keyBytes = hexToBin(key);
                return decrypt(encryptedData, keyBytes, iv);
            }
        }
    }

    private SecurityUtils() {
    }
}
