package com.mahama.common.utils;

import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.UndeclaredThrowableException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * 验证码生成工具类
 */
public class TOTP {
    private static final int[] DIGITS_POWER = {1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000};

    /**
     * 加密方式，HmacSHA1、HmacSHA256、HmacSHA512
     */
    private static final String CRYPTO = "HmacSHA1";

    /**
     * 生成密钥，每个用户独享一份密钥
     */
    public static String getSecretKey() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        Base32 base32 = new Base32();
        String secretKey = base32.encodeToString(bytes);
        return secretKey.toUpperCase();
    }

    /**
     * 生成二维码内容
     *
     * @param secretKey 密钥
     * @param account   账户名
     * @param issuer    网站（可不写）
     */
    public static String getQrCodeText(String secretKey, String account, String issuer) {
        String normalizedBase32Key = secretKey.replace(" ", "").toUpperCase();
        try {
            return "otpauth://totp/"
                    + URLEncoder.encode((StringUtil.isNotNullOrEmpty(issuer) ? (issuer + ":") : "") + account, "UTF-8").replace("+", "%20")
                    + "?secret=" + URLEncoder.encode(normalizedBase32Key, "UTF-8").replace("+", "%20")
                    + (StringUtil.isNotNullOrEmpty(issuer) ? ("&issuer=" + URLEncoder.encode(issuer, "UTF-8").replace("+", "%20")) : "");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * 获取验证码
     *
     * @param secretKey 密钥
     */
    public static String getCode(String secretKey) {
        String normalizedBase32Key = secretKey.replace(" ", "").toUpperCase();
        Base32 base32 = new Base32();
        byte[] bytes = base32.decode(normalizedBase32Key);
        String hexKey = Hex.encodeHexString(bytes);
        long time = (System.currentTimeMillis() / 1000) / 30;
        String hexTime = Long.toHexString(time);
        return generateTOTP(hexKey, hexTime, "6", CRYPTO);
    }

    public static boolean checkCode(String secret, long code) {
        return checkCode(secret, code, 0);
    }

    /**
     * 检验 code 是否正确
     *
     * @param secret     密钥
     * @param code       code
     * @param windowSize 时间前后偏移量
     *                   用于防止客户端时间不精确导致生成的TOTP与服务器端的TOTP一直不一致
     *                   如果为0,当前时间为 10:10:15
     *                   则表明在 10:10:00-10:10:30 之间生成的TOTP 能校验通过
     *                   如果为1,则表明在
     *                   10:09:30-10:10:00
     *                   10:10:00-10:10:30
     *                   10:10:30-10:11:00 之间生成的TOTP 能校验通过
     *                   以此类推
     */
    public static boolean checkCode(String secret, long code, int windowSize) {
        Base32 codec = new Base32();
        byte[] decodedKey = codec.decode(secret);
        long t = (System.currentTimeMillis() / 1000L) / 30L;
        long hash;
        for (int i = -windowSize; i <= windowSize; ++i) {
            try {
                hash = verifyCode(decodedKey, t + i);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
            if (hash == code) {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据时间偏移量计算
     */
    private static long verifyCode(byte[] key, long t) throws NoSuchAlgorithmException, InvalidKeyException {
        byte[] data = new byte[8];
        long value = t;
        for (int i = 8; i-- > 0; value >>>= 8) {
            data[i] = (byte) value;
        }
        SecretKeySpec signKey = new SecretKeySpec(key, CRYPTO);
        Mac mac = Mac.getInstance(CRYPTO);
        mac.init(signKey);
        byte[] hash = mac.doFinal(data);
        int offset = hash[20 - 1] & 0xF;
        long truncatedHash = 0;
        for (int i = 0; i < 4; ++i) {
            truncatedHash <<= 8;
            truncatedHash |= (hash[offset + i] & 0xFF);
        }
        truncatedHash &= 0x7FFFFFFF;
        truncatedHash %= 1000000;
        return truncatedHash;
    }

    private static byte[] hmac_sha(String crypto, byte[] keyBytes, byte[] text) {
        try {
            Mac hmac;
            hmac = Mac.getInstance(crypto);
            SecretKeySpec macKey = new SecretKeySpec(keyBytes, "RAW");
            hmac.init(macKey);
            return hmac.doFinal(text);
        } catch (GeneralSecurityException gse) {
            throw new UndeclaredThrowableException(gse);
        }
    }

    private static byte[] hexStr2Bytes(String hex) {
        byte[] bArray = new BigInteger("10" + hex, 16).toByteArray();

        byte[] ret = new byte[bArray.length - 1];
        System.arraycopy(bArray, 1, ret, 0, ret.length);
        return ret;
    }

    public static String generateTOTP(String key, String time, String returnDigits, String crypto) {
        int codeDigits = Integer.decode(returnDigits);
        StringBuilder result = null;

        StringBuilder timeBuilder = new StringBuilder(time);
        while (timeBuilder.length() < 16) {
            timeBuilder.insert(0, "0");
        }
        time = timeBuilder.toString();

        byte[] msg = hexStr2Bytes(time);
        byte[] k = hexStr2Bytes(key);
        byte[] hash = hmac_sha(crypto, k, msg);

        int offset = hash[hash.length - 1] & 0xf;

        int binary = ((hash[offset] & 0x7f) << 24)
                | ((hash[offset + 1] & 0xff) << 16)
                | ((hash[offset + 2] & 0xff) << 8) | (hash[offset + 3] & 0xff);

        int otp = binary % DIGITS_POWER[codeDigits];

        result = new StringBuilder(Integer.toString(otp));
        while (result.length() < codeDigits) {
            result.insert(0, "0");
        }
        return result.toString();
    }
}
