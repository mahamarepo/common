package com.mahama.common.utils;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.ByteBuffer;
import java.util.*;

public class ByteUtil {

    /**
     * byte[]转java对象
     *
     * @param bytes byte[]
     * @param <T>   T
     * @return T
     */
    public static <T> T bytesToObject(byte[] bytes) {
        T t = null;
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        ObjectInputStream sIn;
        try {
            sIn = new ObjectInputStream(in);
            t = (T) sIn.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    /**********
     * 字节数组转16进制字符串
     */
    public static String bytesToHexString(byte[] src) {
        return bytesToHexString(src, false);
    }

    public static String bytesToHexString(byte[] src, boolean isInt) {
        String resultString = "";
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0)
            return null;
        if (isInt) {
            for (byte b : src) {
                String hv = Integer.toHexString(b);
//                stringBuilder.append("0".repeat(8 - hv.length()));
                stringBuilder.append(new String(new char[8 - hv.length()]).replace("\0", "0"));
                stringBuilder.append(hv);
            }
        } else {
            for (byte b : src) {
                int v = b & 255;
                String hv = Integer.toHexString(v);
                if (hv.length() < 2)
                    stringBuilder.append(0);
                stringBuilder.append(hv);
            }
        }

        resultString = stringBuilder.toString();
        return resultString.toUpperCase(Locale.getDefault());
    }

    public static byte hexToByte(String hex) {
        return hexToBytes(hex, 16)[0];
    }

    /**
     * 十六进制字符串转byte[]
     *
     * @param hex 十六进制字符串
     * @return byte[]
     */
    public static byte[] hexToBytes(String hex) {
        return hexToBytes(hex, 16);
    }

    /**
     * 字符串转byte[]
     *
     * @param hex   字符串
     * @param radix 使用进制
     * @return byte[]
     */
    public static byte[] hexToBytes(String hex, int radix) {
        if (hex == null) {
            return new byte[]{};
        }

        // 奇数位补0
        if (hex.length() % 2 != 0) {
            hex = "0" + hex;
        }

        int length = hex.length();
        ByteBuffer buffer = ByteBuffer.allocate(length / 2);
        for (int i = 0; i < length; i++) {
            String hexStr = hex.charAt(i) + "";
            i++;
            hexStr += hex.charAt(i);
            byte b = (byte) Integer.parseInt(hexStr, radix);
            buffer.put(b);
        }
        return buffer.array();
    }

    public static int hexToInt(String hex) {
        return bytesToInt(hexToBytes(hex, 16));
    }


    /**
     * byte转十六进制字符
     *
     * @param b byte
     * @return 十六进制字符
     */
    public static String intToByteHex(Integer b) {
        String hex = Integer.toHexString(b & 0xFF);
        if (hex.length() == 1) {
            hex = '0' + hex;
        }
        return hex.toUpperCase(Locale.getDefault());
    }

    /**
     * 合并两个byte[]为一个byte数组
     */
    public static byte[] concat(byte[] array1, byte[] array2) {
        byte[] result = Arrays.copyOf(array1, array1.length + array2.length);
        System.arraycopy(array2, 0, result, array1.length, array2.length);
        return result;
    }

    /**
     * 合并多个byte[]为一个byte数组
     */
    public static byte[] concatAll(byte[]... arrays) {
        if (arrays.length == 0) {
            return new byte[]{};
        }
        if (arrays.length == 1) {
            return arrays[0];
        }

        byte[] cur = concat(arrays[0], arrays[1]);
        for (int i = 2; i < arrays.length; i++) {
            cur = concat(cur, arrays[i]);
        }
        return cur;
    }

    /**
     * 将long数值转换为占八个字节的byte数组
     * 高位在前，低位在后
     */
    public static byte[] longToBytes(long lg) {
        byte[] bytes = new byte[8];
        bytes[0] = (byte) (lg >> 56);
        bytes[1] = (byte) (lg >> 48);
        bytes[2] = (byte) (lg >> 40);
        bytes[3] = (byte) (lg >> 32);
        bytes[4] = (byte) (lg >> 24);
        bytes[5] = (byte) (lg >> 16);
        bytes[6] = (byte) (lg >> 8);
        bytes[7] = (byte) lg;
        return bytes;
    }

    /**
     * 浮点转换为字节
     */
    public static byte[] floatToBytes(float f) {
        return intToBytes(Float.floatToIntBits(f));
    }

    /**
     * 字节转换为浮点
     *
     * @param src 数据源
     */
    public static float bytesToFloat(byte[] src) {
        BigDecimal dst = bytesToFloat(src, 6);
        if (dst == null) {
            return 0.0f;
        }
        return dst.floatValue();
    }

    /**
     * 字节转换为浮点
     *
     * @param src   数据源
     * @param scale 数据精度（有效位）
     */
    public static BigDecimal bytesToFloat(byte[] src, int scale) {
        ByteBuffer buf = ByteBuffer.allocateDirect(4); //无额外内存的直接缓存
        //buf=buf.order(ByteOrder.LITTLE_ENDIAN);//默认大端，小端用这行
        buf.put(src);
        buf.rewind();
        float f = buf.getFloat();
        if (Float.isNaN(f)) {
            return null;
        }
        return BigDecimal.valueOf(f).setScale(scale, RoundingMode.HALF_UP);
    }

    /**
     * 将int数值转换为占四个字节的byte数组
     * 高位在前，低位在后
     */
    public static byte[] intToBytes(int integer) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) (integer >> 24);
        bytes[1] = (byte) (integer >> 16);
        bytes[2] = (byte) (integer >> 8);
        bytes[3] = (byte) integer;
        return bytes;
    }

    /**
     * 将int数值转换为占四个字节的byte数组
     * 低位在前，高位在后
     */
    public static byte[] intToReverseBytes(int integer) {
        byte[] bytes = new byte[4];
        bytes[3] = (byte) (integer >> 24);
        bytes[2] = (byte) (integer >> 16);
        bytes[1] = (byte) (integer >> 8);
        bytes[0] = (byte) integer;
        return bytes;
    }

    /**
     * 将int数值转换为占两个字节的byte数组
     * 高位在前，低位在后
     */
    public static byte[] intToShortBytes(int integer) {
        byte[] bytes = new byte[2];
        bytes[0] = (byte) (integer >> 8);
        bytes[1] = (byte) integer;
        return bytes;
    }

    /**
     * 将int数值转换为占两个字节的byte数组
     * 高位在前，低位在后
     */
    public static byte[] intToByte(int integer) {
        byte[] bytes = new byte[1];
        bytes[0] = (byte) integer;
        return bytes;
    }

    /**
     * 高位在前，低位在后
     */
    public static int bytesToInt(byte[] src) {
        if (src.length > 4) {
            return -1;
        }
        int result = 0;
        for (int i = 0; i < src.length; i++) {
            result = result | (src[i] & 0xFF) << (8 * (src.length - 1 - i));
        }
        return result;
    }

    /**
     * 根据字节转换INT
     */
    public static int bytesToIntByByte(byte[] src) {
        int result = 0;
        for (byte b : src) {
            result = result * 100 + (b & 0xFF);
        }
        return result;
    }

    /**
     * 根据字节转换数字字符串
     */
    public static String bytesToNumStrByByte(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : src) {
            stringBuilder.append((b & 0xFF) < 10 ? "0" + (b & 0xFF) : (b & 0xFF));
        }
        return stringBuilder.toString();
    }

    /**
     * 将单字节的byte转换为无符号int数值
     * 高位在前，低位在后
     */
    public static int byteToInt(byte src) {
        return (src & 0xFF);
    }

    /**
     * 将两个字节的byte数组转换为int数值
     * 低位在前，高位在后
     */
    public static int shortReverseBytesToInt(byte[] src) {
        if (src.length != 2) {
            return -1;
        }
        return (src[0] & 0xFF) | ((src[1] & 0xFF) << 8);
    }

    public static List<Integer> shortBytesToIntList(byte[] src) {
        List<Integer> list = Lists.newArrayList();
        for (int i = 0; i < src.length; i = i + 2) {
            list.add(bytesToInt(new byte[]{src[i], src[i + 1]}));
        }
        return list;
    }

    public static byte[] subBytes(byte[] src, int begin) {
        int count = src.length - begin;
        return subBytes(src, begin, count);
    }

    public static byte[] subBytes(byte[] src, int begin, int count) {
        byte[] bs = new byte[count];
        System.arraycopy(src, begin, bs, 0, count);
        return bs;
    }

    /**
     * 分割byte数组
     *
     * @param src  源数组
     * @param size 分割长度
     */
    public static List<byte[]> splitBytes(byte[] src, int size) {
        List<byte[]> result = new ArrayList<>();
        for (int i = 0; i < src.length; i = i + size) {
            byte[] bytes = new byte[size];
            System.arraycopy(src, i, bytes, 0, size);
            result.add(bytes);
        }
        return result;
    }

    /**
     * Byte[] to byte[]
     */
    public static byte[] toPrimitives(Byte[] oBytes) {
        byte[] bytes = new byte[oBytes.length];
        for (int i = 0; i < oBytes.length; i++) {
            bytes[i] = oBytes[i];
        }
        return bytes;
    }

    /**
     * byte[] to Byte[]
     */
    public static Byte[] toObjects(byte[] bytesPrim) {
        Byte[] bytes = new Byte[bytesPrim.length];
        int i = 0;
        for (byte b : bytesPrim) bytes[i++] = b; // Autoboxing
        return bytes;
    }

    public static void main(String[] args) {
        byte[] src = new byte[]{0x1, 0x2, 0x3};
        int result = 0;
        for (int i = 0; i < src.length; i++) {
            result = result | (src[i] & 0xFF) << (8 * (src.length - 1 - i));
        }
        System.out.println(result);
    }
}
