package com.mahama.common.utils;

import com.mahama.common.exception.EncryptException;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

public class AESUtil {
    private static final String ENCODING = "UTF-8"; // 编码
    private static final String ALGORITHM = "AES"; //算法
    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding"; // 默认的加密算法，CBC模式

    /**
     * 随机密钥
     */
    private static String generateRandomKey() {
        return StringUtil.randomByRegex("[A-Za-z0-9]{16}");
    }

    private static int randomLength(String key) {
        return Integer.parseUnsignedInt(String.valueOf(key.charAt(0)), 36);
    }

    public static String randomEncrypt(int length) {
        return StringUtil.randomByRegex("\\w{" + length + "}");
    }

    /**
     * 加密
     */
    public static String encrypt(String data) {
        String key = generateRandomKey();
        int length = randomLength(key);
        String enStr = encrypt(data, key);
        return key + randomEncrypt(length) + enStr;
    }

    /**
     * 加密
     */
    public static String encrypt(String data, String key) {
        String offset = generateRandomKey();
        int length = randomLength(offset);
        String enStr = encrypt(data, key, offset);
        return offset + randomEncrypt(length) + enStr;
    }

    /**
     * 加密
     */
    public static String encrypt(String data, String key, String offset) {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.US_ASCII), ALGORITHM);
            IvParameterSpec iv = new IvParameterSpec(offset.getBytes());//CBC模式偏移量IV
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] encrypted = cipher.doFinal(data.getBytes(ENCODING));
            return new Base64().encodeToString(encrypted);//加密后再使用BASE64做转码
        } catch (Exception e) {
            throw new EncryptException("加密失败");
        }
    }

    /**
     * 解密
     */
    public static String decrypt(String data) {
        String key = StringUtil.sub(data, 0, 16);
        int length = randomLength(key);
        String enStr = StringUtil.sub(data, 16 + length, 0);
        return decrypt(enStr, key);
    }

    /**
     * 解密
     */
    public static String decrypt(String data, String key) {
        String offset = StringUtil.sub(data, 0, 16);
        int length = randomLength(offset);
        String enStr = StringUtil.sub(data, 16 + length, 0);
        return decrypt(enStr, key, offset);
    }

    /**
     * 解密
     */
    public static String decrypt(String data, String key, String offset) {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.US_ASCII), ALGORITHM);
            IvParameterSpec iv = new IvParameterSpec(offset.getBytes()); //CBC模式偏移量IV
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] buffer = new Base64().decode(data);//先用base64解码
            byte[] encrypted = cipher.doFinal(buffer);
            return new String(encrypted, ENCODING);
        } catch (Exception e) {
            return null;
        }
    }
}
