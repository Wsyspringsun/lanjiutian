package com.wyw.ljtds.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Formatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/3/6 0006.
 */

public class StringUtils {
    /**
     * 是否为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(CharSequence str) {

        return (str == null || str.length() == 0);
    }

    /**
     * 字符串长度
     *
     * @param str
     * @return
     */
    public static int length(CharSequence str) {

        return str == null ? 0 : str.length();
    }


    /**
     * 去除@和它前面的一个字符
     *
     * @param line
     * @return
     */
    public static String getNewStringLeft(String line) {

        if (line.contains("@")) {
            String newString;
            char[] strChar = line.toCharArray();
            int index = line.indexOf("@");
            if (strChar[0] == '@') {
                if (line.length() == 1) {// 将$开头的直接去掉
                    return null;
                } else {
                    String str = line.substring(1, line.length());
                    return getNewStringLeft(str);// 递归
                }
            }
            String str = line.substring(index - 1, index + 1);
            newString = line.replace(str, "");
            String getString = getNewStringLeft(newString);
            if (getString.length() < 1) {
                return "空字符";
            }
            return getString;
        }
        return line;
    }


    /**
     * 去除#和它后面的一个字符
     *
     * @param line
     * @return
     */
    public static String getNewStringRight(String line) {

        if (line.contains("#")) {
            String newString;
            char[] strChar = line.toCharArray();
            int index = line.indexOf("#");
            if (strChar[0] == '#') {
                if (line.length() == 1) {// 将$开头的直接去掉
                    return null;
                } else {
                    String str = line.substring(1, line.length());
                    return getNewStringLeft(str);// 递归
                }
            }
            String str = line.substring(index, index + 2);
            newString = line.replace(str, "");
            String getString = getNewStringLeft(newString);
            if (getString.length() < 1) {
                return "空字符";
            }
            return getString;
        }
        return line;
    }


    /**
     * 在给定的字符串中，用新的字符替换所有旧的字符
     *
     * @param string  给定的字符串
     * @param oldchar 旧的字符
     * @param newchar 新的字符
     * @return 替换后的字符串
     */
    public static String replace(String string, char oldchar, char newchar) {
        char chars[] = string.toCharArray();
        for (int w = 0; w < chars.length; w++) {
            if (chars[w] == oldchar) {
                chars[w] = newchar;
                break;
            }
        }
        return new String(chars);
    }

    /**
     * @param source 字符串
     * @return 返回htmL到字符串
     */
    public static String htmlEscapeCharsToString(String source) {

        return StringUtils.isEmpty(source)
                ? source
                : source.replaceAll("&lt;", "<")
                .replaceAll("&gt;", ">")
                .replaceAll("&amp;", "&")
                .replaceAll("&quot;", "\"");
    }


    /**
     * 判断是否第一个字符为Y   是则去掉
     *
     * @param str
     * @return
     */
    public static String deletaFirst(String str) {
        if (StringUtils.isEmpty(str)) return "";
        if (str.startsWith("Y")) {
            return str.substring(1, str.length());
//            return str;
        } else {
            return str;
        }
    }


    /**
     * 提取字符串中的数字
     *
     * @param args
     * @return
     */
    public static String getNumber(String args) {
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(args);
        return m.replaceAll("").trim();
    }

    public static String formatLatlng(double latlng) {
        DecimalFormat df = new DecimalFormat("0.00000000000000");
        BigDecimal bd = new BigDecimal(latlng);
        return df.format(bd);
    }
}
