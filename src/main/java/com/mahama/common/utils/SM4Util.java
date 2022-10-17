package com.mahama.common.utils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.Security;

public class SM4Util extends SmXUtil {
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    private static final String ENCODING = "UTF-8";
    public static final String ALGORITHM_NAME = "SM4";
    public static final String ALGORITHM_NAME_ECB_PADDING = "SM4/ECB/PKCS7Padding";

    private static Cipher generateEcbCipher(int mode, byte[] key) throws Exception {
        Cipher cipher = Cipher.getInstance(SM4Util.ALGORITHM_NAME_ECB_PADDING, BouncyCastleProvider.PROVIDER_NAME);
        Key sm4Key = new SecretKeySpec(key, ALGORITHM_NAME);
        cipher.init(mode, sm4Key);
        return cipher;
    }

    public static String encryptToString(String key, String data) throws Exception {
        return ByteUtil.bytesToHexString(encrypt(key, data));
    }

    public static String encryptToSafeBase64(String key, String data) throws Exception {
        return safeBase64Encode(encrypt(key, data));
    }

    /**
     * sm4加密
     */
    public static byte[] encrypt(String key, String data) throws Exception {
        byte[] keyData = key.getBytes(ENCODING);
        byte[] srcData = data.getBytes(ENCODING);
        return encrypt_Ecb_Padding(keyData, srcData);
    }

    private static byte[] encrypt_Ecb_Padding(byte[] key, byte[] data) throws Exception {
        Cipher cipher = generateEcbCipher(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(data);
    }

    public static String decryptWithSafeBase64(String key, String cipherText) {
        return decrypt(key, safeBase64Decrypt(cipherText));
    }

    public static String decrypt(String key, String cipherText) {
        byte[] cipherData = ByteUtil.hexToBytes(cipherText);
        return decrypt(key, cipherData);
    }

    /**
     * sm4解密
     */
    public static String decrypt(String key, byte[] cipherData) {
        String decryptStr = "";
        byte[] keyData = key.getBytes();
        byte[] srcData = new byte[0];
        try {
            srcData = decrypt_Ecb_Padding(keyData, cipherData);
            decryptStr = new String(srcData, ENCODING);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decryptStr;
    }

    private static byte[] decrypt_Ecb_Padding(byte[] key, byte[] cipherText) throws Exception {
        Cipher cipher = generateEcbCipher(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(cipherText);
    }
}