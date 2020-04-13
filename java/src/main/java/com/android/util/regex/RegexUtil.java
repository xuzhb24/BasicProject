package com.android.util.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xuzhb on 2020/1/5
 * Desc:正则表达式工具类
 */
public class RegexUtil {

    //是否是数字
    public static boolean isDigit(String content) {
        return isMatch(RegexConstant.DIGIT, content);
    }

    //是否是整数
    public static boolean isInteger(String content) {
        return isMatch(RegexConstant.INTEGER, content);
    }

    //是否是浮点数
    public static boolean isFloat(String content) {
        return isMatch(RegexConstant.FLOAT, content);
    }

    //是否是汉字
    public static boolean isChinese(String content) {
        return isMatch(RegexConstant.CHINESE, content);
    }

    //英文字母或数字
    public static boolean isLetterOrDigit(String content) {
        return isMatch(RegexConstant.LETTER_OR_DIGIT, content);
    }

    //英文字母和数字
    public static boolean isLetterAndDigit(String content) {
        return isMatch(RegexConstant.LETTER_AND_DIGIT, content);
    }

    //是否是手机号
    public static boolean isMobile(String phone) {
        return isMatch(RegexConstant.MOBILE_EXACT, phone);
    }

    //是否是身份证号
    public static boolean isIdCard(String number) {
        return isMatch(RegexConstant.ID_CARD_15, number) || isMatch(RegexConstant.ID_CARD_18, number);
    }

    //是否是"yyyy-MM-dd"的日期格式
    public static boolean isyyyyMMdd(String date) {
        return isMatch(RegexConstant.DATE, date);
    }

    /**
     * 是否匹配正则表达式
     *
     * @param regex   正则表达式
     * @param content 文本
     */
    public static boolean isMatch(String regex, String content) {
        return Pattern.matches(regex, content);
    }

    //提取文本中的数字，包括整数和小数、正数和负数，如果存在多个，以指定的分隔符隔开
    public static String extractDigit(String content, String delimiter) {
        return extract("(-)?(\\d+(\\.\\d+)?)", content, delimiter);
    }

    /**
     * 提取文本中匹配给定正则表达式的内容，如果存在多个，以指定的分隔符隔开
     *
     * @param regex     正则表达式
     * @param content   文本
     * @param delimiter 分隔符
     */
    public static String extract(String regex, String content, String delimiter) {
        StringBuilder sb = new StringBuilder();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            sb.append(matcher.group()).append(delimiter);
        }
        String result = sb.toString();
        return result.substring(0, result.length() - delimiter.length());
    }

}
