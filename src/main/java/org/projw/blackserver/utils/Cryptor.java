package org.projw.blackserver.utils;

import org.apache.commons.codec.binary.Hex;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.projw.blackserver.controllers.exceptions.SystemException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.Base64;

public class Cryptor {
    static final Logger logger = LoggerFactory.getLogger(Cryptor.class);
    private static Cipher cipher;
    private static SecretKey secretKey;
    private static MessageDigest md5;

    static {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128);
            secretKey = keyGenerator.generateKey();
            cipher = Cipher.getInstance("AES");

            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            throw new SystemException("无法初始化 Cryptor 类", e);
        }
    }

    /**
     * @return 当出现异常的时候返回空字符串
     */
    @Nullable
    public static String encrypt(byte[] data) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedByte = cipher.doFinal(data);
            Base64.Encoder encoder = Base64.getEncoder();
            return encoder.encodeToString(encryptedByte);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @return 当出现异常的时候返回 null
     */
    @Nullable
    public static byte[] decrypt(String encryptedText) {
        try {
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] encryptedTextByte = decoder.decode(encryptedText);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return cipher.doFinal(encryptedTextByte);
        } catch (Exception e) {
            return null;
        }
    }

    private static String secretKeyToString(SecretKey key) {
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    private static SecretKey stringToSecretKey(String str) {
        byte[] decodedKey = Base64.getDecoder().decode(str);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
    }

    public static String md5(byte[] data) {
        md5.reset();
        md5.update(data);
        byte[] digest = md5.digest();
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(digest);
    }

    public static String md5_hex(byte[] data) {
        md5.reset();
        md5.update(data);
        byte[] digest = md5.digest(); // 16 bytes
        return Hex.encodeHexString(digest); // 32 character
    }
}
