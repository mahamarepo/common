package com.mahama.common.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;


/**
 * 类型转换器
 */
public class Convert {
    private Convert() {
        // 静态类不可实例化
    }


    /**
     * 转换为字符串<br>
     * 如果给定的值为null，或者转换失败，返回默认值<br>
     * 转换失败不会报错
     *
     * @param value        被转换的值
     * @param defaultValue 转换错误时的默认值
     * @return 结果
     */
    public static String toStr(Object value, String defaultValue) {
        if (null == value) {
            return defaultValue;
        }
        if (value instanceof String) {
            return (String) value;
        } else if (CollectionKit.isArray(value)) {
            return CollectionKit.toString(value);
        }
        return value.toString();
    }


    /**
     * 转换为int<br>
     * 如果给定的值为空，或者转换失败，返回默认值<br>
     * 转换失败不会报错
     *
     * @param value        被转换的值
     * @param defaultValue 转换错误时的默认值
     * @return 结果
     */
    public static Integer toInt(Object value, Integer defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Integer) {
            return (Integer) value;
        }
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        final String valueStr = toStr(value, null);
        if (StringUtil.isEmpty(valueStr)) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(valueStr.trim());
        } catch (Exception e) {
            return defaultValue;
        }
    }


    /**
     * 转换为Integer数组<br>
     *
     * @param str 被转换的值
     * @return 结果
     */
    public static Integer[] toIntArray(String str) {
        return toIntArray(",", str);
    }

    /**
     * 转换为Integer数组<br>
     *
     * @param split 分隔符
     * @param split 被转换的值
     * @return 结果
     */
    public static Integer[] toIntArray(String split, String str) {
        if (StringUtil.isEmpty(str)) {
            return new Integer[]{};
        }
        String[] arr = str.split(split);
        final Integer[] ints = new Integer[arr.length];
        for (int i = 0; i < arr.length; i++) {
            final Integer v = toInt(arr[i], 0);
            ints[i] = v;
        }
        return ints;
    }

    public static Long[] toLongArray(String split, String str) {
        if (StringUtil.isEmpty(str)) {
            return new Long[]{};
        }
        String[] arr = str.split(split);
        final Long[] ints = new Long[arr.length];
        for (int i = 0; i < arr.length; i++) {
            final Long v = StringUtil.isNotEmpty(arr[i]) ? Long.valueOf(arr[i]) : 0L;
            ints[i] = v;
        }
        return ints;
    }


    /**
     * 转换为String数组<br>
     *
     * @param split 分隔符
     * @param split 被转换的值
     * @return 结果
     */
    public static String[] toStrArray(String split, String str) {
        if (StringUtil.isNullOrEmpty(str)) {
            return new String[]{};
        }
        return str.split(split);
    }

    public static List<String> toStrList(String split, String str) {
        return Lists.newArrayList(toStrArray(split, str));
    }

    /**
     * 转换为long<br>
     * 如果给定的值为空，或者转换失败，返回默认值<br>
     * 转换失败不会报错
     *
     * @param value        被转换的值
     * @param defaultValue 转换错误时的默认值
     * @return 结果
     */
    public static Long toLong(Object value, Long defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Long) {
            return (Long) value;
        }
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        final String valueStr = toStr(value, null);
        if (StringUtil.isEmpty(valueStr)) {
            return defaultValue;
        }
        try {
            // 支持科学计数法
            return new BigDecimal(valueStr.trim()).longValue();
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static String toSize(long size) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (size < 1024) {
            fileSizeString = df.format((double) size) + "B";
        } else if (size < 1048576) {
            fileSizeString = df.format((double) size / 1024) + "K";
        } else if (size < 1073741824) {
            fileSizeString = df.format((double) size / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) size / 1073741824) + "G";
        }
        return fileSizeString;
    }

    /**
     * 将一个int数字转换为二进制的字符串形式。
     *
     * @param num    需要转换的int类型数据
     * @param digits 要转换的二进制位数，位数不足则在前面补0
     * @return 二进制的字符串形式
     */
    public static String toBinary(int num, int digits) {
        if (num >= 0) {
            int offset = digits;
            if (digits == 32) {
                offset = 31;
            }
            int value = 1 << offset | num;
            String bs = Integer.toBinaryString(value); //0x20 | 这个是为了保证这个string长度是6位数
            return digits == 32 ? "0" + bs.substring(1) : bs.substring(1);
        } else {
            return Integer.toBinaryString(num);
        }
    }

    public static String confuse(String str) {
        char[] cc = str.toCharArray();
        int offset = cc.length * 3 % 8;
        StringBuilder stringBuilder = new StringBuilder();
        for (char c : cc) {
            int asciiCode = Character.hashCode(c);
            asciiCode += offset;
            char charTemp = (char) asciiCode;
            stringBuilder.append(charTemp);
        }
        return stringBuilder.toString();
    }

    public static String unConfuse(String str) {
        char[] cc = str.toCharArray();
        int offset = cc.length * 3 % 8;
        StringBuilder stringBuilder = new StringBuilder();
        for (char c : cc) {
            int asciiCode = Character.hashCode(c);
            asciiCode -= offset;
            char charTemp = (char) asciiCode;
            stringBuilder.append(charTemp);
        }
        return stringBuilder.toString();
    }
}
