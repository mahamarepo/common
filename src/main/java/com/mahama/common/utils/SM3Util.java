package com.mahama.common.utils;

import org.bouncycastle.crypto.digests.SM3Digest;

import java.nio.charset.StandardCharsets;

/**
 * @author mahama
 * @date 2022年09月20日
 */
public class SM3Util extends SmXUtil {
    public static String encryptToSafeBase64(String data) {
        return safeBase64Encode(encrypt(data));
    }

    public static byte[] encrypt(String data) {
        SM3Digest sm3Digest = new SM3Digest();
        byte[] srcData = data.getBytes(StandardCharsets.UTF_8);
        sm3Digest.update(srcData, 0, srcData.length);
        byte[] hash = new byte[sm3Digest.getDigestSize()];
        sm3Digest.doFinal(hash, 0);
        return hash;
    }
}
