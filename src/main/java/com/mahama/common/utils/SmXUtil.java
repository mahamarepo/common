package com.mahama.common.utils;

import org.bouncycastle.util.encoders.Base64;

/**
 * @author mahama
 * @date 2022年09月20日
 */
public class SmXUtil {
    public static String safeBase64Encode(byte[] bytes) {
        String base64Str = Base64.toBase64String(bytes);
        return base64Str.replace("+", "-").replace("/", "_");
    }

    public static byte[] safeBase64Decrypt(String data) {
        data = data.replace("-", "+").replace("_", "/");
        return Base64.decode(data);
    }
}
