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
     * 加密
     */
    public static String encrypt(String data, String key) {
        return encrypt(data, key, null);
    }

    /**
     * 加密
     */
    public static String encrypt(String data, String key, String offset) {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.US_ASCII), ALGORITHM);
            if (StringUtil.isNotNullOrEmpty(offset)) {
                IvParameterSpec iv = new IvParameterSpec(offset.getBytes());//CBC模式偏移量IV
                cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            } else {
                cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            }
            byte[] encrypted = cipher.doFinal(data.getBytes(ENCODING));
            return new Base64().encodeToString(encrypted);//加密后再使用BASE64做转码
        } catch (Exception e) {
            throw new EncryptException("加密失败");
        }
    }

    /**
     * 解密
     */
    public static String decrypt(String data, String key) {
        return decrypt(data, key, null);
    }

    /**
     * 解密
     */
    public static String decrypt(String data, String key, String offset) {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.US_ASCII), ALGORITHM);
            if (StringUtil.isNotNullOrEmpty(offset)) {
                IvParameterSpec iv = new IvParameterSpec(offset.getBytes()); //CBC模式偏移量IV
                cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            } else {
                cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            }
            byte[] buffer = new Base64().decode(data);//先用base64解码
            byte[] encrypted = cipher.doFinal(buffer);
            return new String(encrypted, ENCODING);
        } catch (Exception e) {
            return null;
        }
    }
}
