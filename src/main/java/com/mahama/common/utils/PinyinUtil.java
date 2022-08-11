package com.mahama.common.utils;

import net.sourceforge.pinyin4j.PinyinHelper;

import java.util.Arrays;

public class PinyinUtil {
    public static String getPinYin(String str) {
        StringBuilder convert = new StringBuilder();
        for (int j = 0; j < str.length(); j++) {
            char word = str.charAt(j);
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
            if (pinyinArray != null) {
                convert.append(Arrays.toString(pinyinArray));
            } else {
                convert.append(word);
            }
        }
        return convert.toString().toUpperCase();
    }

    public static String getPinYinHeadChar(String str) {
        StringBuilder convert = new StringBuilder();
        for (int j = 0; j < str.length(); j++) {
            char word = str.charAt(j);
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
            if (pinyinArray != null) {
                convert.append(pinyinArray[0].charAt(0));
            } else {
                convert.append(word);
            }
        }
        return convert.toString().toUpperCase();
    }

    public static String getPinYinFirstChar(String str) {
        char word = str.charAt(0);
        String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
        if (pinyinArray != null) {
            return String.valueOf(pinyinArray[0].charAt(0)).toUpperCase();
        } else {
            return String.valueOf(word).toUpperCase();
        }
    }
}
