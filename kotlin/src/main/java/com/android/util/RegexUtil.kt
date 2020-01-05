package com.android.util

import java.lang.StringBuilder
import java.util.regex.Pattern

/**
 * Created by xuzhb on 2020/1/4
 * Desc:正则表达式工具类
 */
object RegexUtil {

    //是否是整数，包括正数和负数
    fun isInteger(content: String): Boolean {
//        return isMatch("^[-\\+]?\\d+$", content)   //匹配"+1"格式
        return isMatch("^-?\\d+$", content)  //不匹配"+1"格式
    }

    //是否是正整数
    fun isPositiveInteger(content: String): Boolean = isMatch("\\d+", content)

    //是否是小数
    fun isDecimal(content: String): Boolean {
//        return isMatch("^[-\\+]?\\d+[.]\\d+$", content)    //匹配"+1.2"格式
        return isMatch("^-?\\d+[.]\\d+$", content)  //不匹配"+1.2"格式
    }

    //是否由数字或字母组成，包括大小写字母
    fun isLetterOrDigit(content: String): Boolean = isMatch("^[a-zA-Z\\d]+$", content)

    //是否匹配正则表达式
    fun isMatch(regex: String, content: String): Boolean = Pattern.matches(regex, content)

    //提取文本中的数字，包括整数和小数、正数和负数，如果存在多个，以指定的分隔符隔开，默认空格
    fun extractNumber(content: String, delimiter: String = " "): String {
        val sb = StringBuilder()
        val pattern = Pattern.compile("(-)?(\\d+(\\.\\d+)?)")
        val matcher = pattern.matcher(content)
        while (matcher.find()) {
            sb.append(matcher.group()).append(delimiter)
        }
        return sb.toString()
    }

    @JvmStatic
    fun main(args: Array<String>) {
        test("是否是整数", 1, "1234567890", "-1234567890", "+1234567890", "01", "-01", "1.2", "-1.2", "", "1-")
        test("是否正整数", 2, "1234567890", "-1234567890", "+1234567890", "01", "-01", "1.2", "-1.2", "", "1-")
        test(
            "是否是小数",
            3,
            "12345.67890",
            "-12345.67890",
            "+12345.67890",
            ".1",
            "-.1",
            "1.2",
            "-1.2",
            "",
            "-123",
            "-1.1.1"
        )
        test("数字或字母", 4, "1aA", "b2B", "Cc3", "4d", "dD", "D4", "567", "eee", "FFF", "su.12", "+ssdd12A")
        test("提取数字", 5, "中文中文中文")
        test("提取数字", 5, "纯整数123纯整数-456纯整数789纯整数-0012")
        test("提取数字", 5, "纯小数-1.23纯小数4.56纯小数+7.89纯小数-00.123456789纯小数0.0.0纯小数.23纯小数4.纯小数")
        test("提取数字", 5, "整数和小数12.34整数和小数-56整数和小数-78.90整数和小数12整数和小数12.8")
    }

    private fun test(tag: String, type: Int, vararg contents: String) {
        print("$tag:  ")
        for (content in contents) {
            when (type) {
                1 -> print("\"" + content + "\":" + isInteger(content) + "  ")
                2 -> print("\"" + content + "\":" + isPositiveInteger(content) + "  ")
                3 -> print("\"" + content + "\":" + isDecimal(content) + "  ")
                4 -> print("\"" + content + "\":" + isLetterOrDigit(content) + "  ")
                5 -> print("\"" + content + "\":  " + extractNumber(content, "|"))
            }
        }
        println()
    }

}