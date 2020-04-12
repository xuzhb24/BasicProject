package com.android.util.regex

import java.util.regex.Pattern

/**
 * Created by xuzhb on 2020/1/4
 * Desc:正则表达式工具类
 */
object RegexUtil {

    //是否是数字
    fun isDigit(content: String): Boolean = isMatch(RegexConstant.DIGIT, content)

    //是否是整数
    fun isInteger(content: String): Boolean = isMatch(RegexConstant.INTEGER, content)

    //是否是浮点数
    fun isFloat(content: String): Boolean = isMatch(RegexConstant.FLOAT, content)

    //是否是汉字
    fun isChinese(content: String): Boolean = isMatch(RegexConstant.CHINESE, content)

    //英文字母或数字
    fun isLetterOrDigit(content: String): Boolean = isMatch(RegexConstant.LETTER_OR_DIGIT, content)

    //英文字母和数字
    fun isLetterAndDigit(content: String): Boolean =
        isMatch(RegexConstant.LETTER_AND_DIGIT, content)

    //是否是手机号
    fun isMobile(content: String): Boolean = isMatch(RegexConstant.MOBILE_EXACT, content)

    //是否是身份证号
    fun isIdCard(content: String): Boolean =
        isMatch(RegexConstant.ID_CARD_15, content) || isMatch(RegexConstant.ID_CARD_18, content)

    //是否是"yyyy-MM-dd"的日期格式
    fun isyyyyMMdd(content: String): Boolean = isMatch(RegexConstant.DATE, content)

    /**
     * 是否匹配正则表达式
     *
     * @param regex   正则表达式
     * @param content 文本
     */
    fun isMatch(regex: String, content: String): Boolean = Pattern.matches(regex, content)

    //提取文本中的数字，包括整数和小数、正数和负数，如果存在多个，以指定的分隔符隔开
    fun extractDigit(content: String, delimiter: String = " "): String =
        extract("(-)?(\\d+(\\.\\d+)?)", content, delimiter)

    /**
     * 提取文本中匹配给定正则表达式的内容，如果存在多个，以指定的分隔符隔开
     *
     * @param regex     正则表达式
     * @param content   文本
     * @param delimiter 分隔符
     */
    fun extract(regex: String, content: String, delimiter: String = " "): String {
        val sb = StringBuilder()
        val pattern = Pattern.compile(regex)
        val matcher = pattern.matcher(content)
        while (matcher.find()) {
            sb.append(matcher.group()).append(delimiter)
        }
        return sb.toString()
    }

}