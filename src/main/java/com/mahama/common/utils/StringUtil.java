package com.mahama.common.utils;


import org.laziji.commons.rereg.model.Node;
import org.laziji.commons.rereg.model.OrdinaryNode;

import java.nio.charset.Charset;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 *
 * @author enilu
 */
public class StringUtil {
    public static final String EMPTY = "";

    /**
     * 是否为空字符
     */
    public static boolean isEmpty(String str) {
        if (str == null || str.trim().length() == 0) {
            return true;
        }
        if ("null".equalsIgnoreCase(str) || "undefined".equalsIgnoreCase(str)) {
            return true;
        }
        return false;
    }

    /**
     * 是否包含空字符串
     *
     * @param strs 字符串列表
     * @return 是否包含空字符串
     */
    public static boolean hasBlank(String... strs) {
        if (CollectionKit.isEmpty(strs)) {
            return true;
        }
        for (String str : strs) {
            if (isEmpty(str)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否为非空字符
     */
    public static boolean isNotEmpty(String str) {
        return (!isEmpty(str));
    }

    /**
     * 判断是否为null或空字符
     */
    public static boolean isNullOrEmpty(Object o) {
        if (o == null) {
            return true;
        }
        if (String.valueOf(o).replace((char) 12288, ' ').trim().length() == 0) {
            return true;
        }
        return "null".equals(o);
    }

    /**
     * 判断是否不为null或非空字符
     */
    public static boolean isNotNullOrEmpty(Object o) {
        return !isNullOrEmpty(o);
    }

    // 根据Unicode编码完美的判断中文汉字和符号
    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }


    public static String sNull(Object obj) {
        return obj == null ? null : String.valueOf(obj);
    }

    public static String sNull(Object obj, String defaultValue) {
        return obj == null ? defaultValue : String.valueOf(obj);
    }

    public static boolean contains(String sec, String str){
        return str.contains(sec);
    }

    /**
     * 改进JDK subString<br>
     * index从0开始计算，最后一个字符为-1<br>
     * 如果from和to位置一样，返回 "" <br>
     * 如果from或to为负数，则按照length从后向前数位置，如果绝对值大于字符串长度，则from归到0，to归到length<br>
     * 如果经过修正的index中from大于to，则互换from和to
     * example: <br>
     * abcdefgh 2 3 -> c <br>
     * abcdefgh 2 -3 -> cde <br>
     *
     * @param str    String
     * @param fromIndex 开始的index（包括）
     * @param toIndex   结束的index（不包括）
     * @return 字串
     */
    public static String sub(String str, int fromIndex, int toIndex) {
        int len = str.length();
        if (fromIndex < 0) {
            fromIndex = len + fromIndex;
            if (fromIndex < 0) {
                fromIndex = 0;
            }
        } else if (fromIndex > len) {
            fromIndex = len;
        }
        if (toIndex < 0) {
            toIndex = len + toIndex;
            if (toIndex < 0) {
                toIndex = len;
            }
        } else if (toIndex > len || toIndex == 0) {
            toIndex = len;
        }
        if (toIndex < fromIndex) {
            int tmp = fromIndex;
            fromIndex = toIndex;
            toIndex = tmp;
        }
        if (fromIndex == toIndex) {
            return EMPTY;
        }
        char[] strArray = str.toCharArray();
        char[] newStrArray = Arrays.copyOfRange(strArray, fromIndex, toIndex);
        return new String(newStrArray);
    }


    /**
     * 首字母变小写
     */
    public static String firstCharToLowerCase(String str) {
        char firstChar = str.charAt(0);
        if (firstChar >= 'A' && firstChar <= 'Z') {
            char[] arr = str.toCharArray();
            arr[0] += ('a' - 'A');
            return new String(arr);
        }
        return str;
    }

    /**
     * 首字母变大写
     */
    public static String firstCharToUpperCase(String str) {
        char firstChar = str.charAt(0);
        if (firstChar >= 'a' && firstChar <= 'z') {
            char[] arr = str.toCharArray();
            arr[0] -= ('a' - 'A');
            return new String(arr);
        }
        return str;
    }

    /**
     * 驼峰格式字符串转换为下划线格式字符串
     */
    public static String camelToUnderline(String str) {
        if (isNullOrEmpty(str)) {
            return str;
        }
        int len = str.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = str.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append('_');
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }


    /**
     * 去掉指定前缀
     *
     * @param str    字符串
     * @param prefix 前缀
     * @return 切掉后的字符串，若前缀不是 preffix， 返回原字符串
     */
    public static String removePrefix(String str, String prefix) {
        if (isEmpty(str) || isEmpty(prefix)) {
            return str;
        }

        if (str.startsWith(prefix)) {
            return str.substring(prefix.length());
        }
        return str;
    }


    /**
     * 去掉指定后缀
     *
     * @param str    字符串
     * @param suffix 后缀
     * @return 切掉后的字符串，若后缀不是 suffix， 返回原字符串
     */
    public static String removeSuffix(String str, String suffix) {
        if (isEmpty(str) || isEmpty(suffix)) {
            return str;
        }

        if (str.endsWith(suffix)) {
            return str.substring(0, str.length() - suffix.length());
        }
        return str;
    }

    /**
     * 切分字符串<br>
     * from jodd
     *
     * @param str       被切分的字符串
     * @param delimiter 分隔符
     * @return 字符串
     */
    public static String[] split(String str, String delimiter) {
        if (str == null) {
            return null;
        }
        if (str.trim().length() == 0) {
            return new String[]{str};
        }

        int dellen = delimiter.length(); // del length
        int maxparts = (str.length() / dellen) + 2; // one more for the last
        int[] positions = new int[maxparts];

        int i, j = 0;
        int count = 0;
        positions[0] = -dellen;
        while ((i = str.indexOf(delimiter, j)) != -1) {
            count++;
            positions[count] = i;
            j = i + dellen;
        }
        count++;
        positions[count] = str.length();

        String[] result = new String[count];

        for (i = 0; i < count; i++) {
            result[i] = str.substring(positions[i] + dellen, positions[i + 1]);
        }
        return result;
    }

    /**
     * 解码字节码
     *
     * @param data    字符串
     * @param charset 字符集，如果此字段为空，则解码的结果取决于平台
     * @return 解码后的字符串
     */
    public static String str(byte[] data, Charset charset) {
        if (data == null) {
            return null;
        }

        if (null == charset) {
            return new String(data);
        }
        return new String(data, charset);
    }

    /**
     * unicode 转字符串
     *
     * @param unicode 全为 Unicode 的字符串
     * @return
     */
    public static String unicode2String(String unicode) {
        StringBuilder string = new StringBuilder();
        String[] hex = unicode.split("\\\\u");

        for (int i = 1; i < hex.length; i++) {
            // 转换出每一个代码点
            int data = Integer.parseInt(hex[i], 16);
            // 追加成string
            string.append((char) data);
        }

        return string.toString();
    }



    /**
     * 格式化文本, {} 表示占位符<br>
     * 例如：format("aaa {} ccc", "bbb")   ---->    aaa bbb ccc
     *
     * @param template 文本模板，被替换的部分用 {} 表示
     * @param values   参数值
     * @return 格式化后的文本
     */
    public static String format(String template, Object... values) {
        if (CollectionKit.isEmpty(values) || isEmpty(template)) {
            return template;
        }

        final StringBuilder sb = new StringBuilder();
        final int length = template.length();

        int valueIndex = 0;
        char currentChar;
        for (int i = 0; i < length; i++) {
            if (valueIndex >= values.length) {
                sb.append(sub(template, i, length));
                break;
            }

            currentChar = template.charAt(i);
            if (currentChar == '{') {
                final char nextChar = template.charAt(++i);
                if (nextChar == '}') {
                    sb.append(values[valueIndex++]);
                } else {
                    sb.append('{').append(nextChar);
                }
            } else {
                sb.append(currentChar);
            }

        }

        return sb.toString();
    }

    /**
     * 格式化文本，使用 {varName} 占位<br>
     * map = {a: "aValue", b: "bValue"}
     * format("{a} and {b}", map)    ---->    aValue and bValue
     *
     * @param template 文本模板，被替换的部分用 {key} 表示
     * @param map      参数值对
     * @return 格式化后的文本
     */
    public static String format(String template, Map<?, ?> map) {
        if (null == map || map.isEmpty()) {
            return template;
        }

        for (Map.Entry<?, ?> entry : map.entrySet()) {
            template = template.replace("{" + entry.getKey() + "}", entry.getValue().toString());
        }
        return template;
    }

    /**
     * 格式化文本,使用正则表达式随机字符替换, {} 表示占位符<br>
     * 例如：format("aaa {} ccc", "\\d{3}")   ---->    aaa 123 ccc
     *
     * @param template   文本模板，被替换的部分用 {} 表示
     * @param expression 正则表达式
     * @return 格式化后的文本
     */
    public static String formatByRegex(String template, String expression) {
        if (isEmpty(template)) {
            return template;
        }

        final StringBuilder sb = new StringBuilder();
        final int length = template.length();

        char currentChar;
        for (int i = 0; i < length; i++) {
            currentChar = template.charAt(i);
            if (currentChar == '{') {
                final char nextChar = template.charAt(++i);
                if (nextChar == '}') {
                    sb.append(randomByRegex(expression));
                } else {
                    sb.append('{').append(nextChar);
                }
            } else {
                sb.append(currentChar);
            }
        }
        return sb.toString();
    }

    /**
     * 验证正则
     */
    public static boolean checkRegex(String str, String regex) {
        return checkRegex(str, regex, false);
    }

    public static boolean checkRegex(String str, String regex, boolean ignoreCase) {
        if (str == null) return false;
        int flags = 0;
        if (ignoreCase) {
            flags = Pattern.CASE_INSENSITIVE;
        }
        Pattern p = Pattern.compile(regex, flags);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 正则匹配
     */
    public static List<String> regex(String str, String regex) {
        List<String> list = new ArrayList<>();
        if (str != null) {
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(str);
            while (m.find()) {
                list.add(m.group());
            }
        }
        return list;
    }

    /**
     * 正则匹配第一个结果
     */
    public static String regexFirst(String str, String regex) {
        List<String> list = regex(str, regex);
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    /**
     * 根据正则生成随机字符
     *
     * @param expression 正则表达式
     * @see java.util.regex.Pattern
     */
    public static String randomByRegex(String expression) {
        try {
            Node node = new OrdinaryNode(expression);
            return node.random();
        } catch (Exception err) {
            return "";
        }
    }

    public static String replaceByRegex(String str, String regex, ReplaceHandler handler) {
        return replaceByRegex(str, Pattern.compile(regex), handler);
    }

    public static String replaceByRegex(String str, Pattern pattern, ReplaceHandler handler) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        Matcher matcher = pattern.matcher(str);

        boolean result = matcher.find();
        if (!result) {
            // normal char sequence
            return str;
        }

        int start = 0;
        StringBuilder sb = new StringBuilder();
        do {
            // append before
            sb.append(str, start, matcher.start());
            // append replacement
            sb.append(handler.onReplace(str.substring(matcher.start(), matcher.end())));
            start = matcher.end();
            result = matcher.find();
        } while (result);

        // append tail
        if (start < str.length()) {
            sb.append(str, start, str.length());
        }

        return sb.toString();
    }

    public interface ReplaceHandler {
        Object onReplace(String str);
    }
}
