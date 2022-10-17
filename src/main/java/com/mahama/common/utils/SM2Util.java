package com.mahama.common.utils;

import cn.hutool.crypto.ECKeyUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.SM2;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.util.encoders.Base64;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 1. @description: SM2工具类
 * 2. @author: xh
 * 3. @time: 2022/3/18
 */
@Slf4j
public class SM2Util extends SmXUtil {
    private static final byte[] DEFAULT_SM2_IV = "1234567812345678".getBytes();

    public static String encryptToBase64(String privateKey, String data) {
        return Base64.toBase64String(encrypt(privateKey, data));
    }

    public static byte[] encrypt(String privateKey, String data) {
        SM2 sm2 = new SM2(ECKeyUtil.toSm2PrivateParams(SecureUtil.decode(privateKey)), null);
        sm2.setMode(SM2Engine.Mode.C1C3C2);
        return sm2.sign(data.getBytes(StandardCharsets.UTF_8), DEFAULT_SM2_IV);
    }
}