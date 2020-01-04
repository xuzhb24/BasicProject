package com.android.util

import java.util.regex.Pattern

/**
 * Created by xuzhb on 2020/1/4
 * Desc:正则表达式工具类
 */
object RegexUtil {

    //是否是数字
    fun isDight(content: String): Boolean = isMatch("^[0-9]+\$", content)

    //是否只由数字、大小写字母组成
    fun isLetterDigit(content: String): Boolean = isMatch("^[a-z0-9A-Z]+\$", content)

    //是否匹配正则表达式
    fun isMatch(regex: String, content: String): Boolean = Pattern.matches(regex, content)

    //提取文本中的数字，如果存在多个，以空格分隔
    fun extractDigit(content: String): String {
        var result = ""
        val pattern = Pattern.compile("\\d+")
        val matcher = pattern.matcher(content)
        while (matcher.find()) {
            result += matcher.group() + " "
        }
        return result.trim()
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println(isDight("1234"))
        println(isDight("asd1234"))
        println(isLetterDigit("1asd1234A12"))
        println(isLetterDigit("1asd1234A12=-="))
        println(extractDigit("1asd1234A12=-="))
        println(extractDigit("一教楼101室"))
    }

}