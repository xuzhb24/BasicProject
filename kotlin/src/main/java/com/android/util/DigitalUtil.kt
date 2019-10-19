package com.android.util

import java.math.BigDecimal
import java.text.DecimalFormat

/**
 * Created by xuzhb on 2019/10/16
 * Desc:一些浮点数格式化的工具类
 */
object DigitalUtil {

    //保留2位小数，四舍五入
    fun formatNumberByRounding1(number: Double, length: Int = 2): String = String.format("%.${length}f", number)

    //保留2位小数，四舍五入
    fun formatNumberByRounding2(number: Double, length: Int = 2): String =
        BigDecimal(number.toString()).setScale(length, BigDecimal.ROUND_HALF_UP).toString()

    //保留2位小数，四舍五入
    fun formatNumberByRounding3(number: Double): String = DecimalFormat("#0.00").format(number)

    //保留2位小数，五舍六入，舍弃的部分如果大于5才进位，小于或等于5直接舍弃
    fun formatNumberByRounding4(number: Double, length: Int = 2): String =
        BigDecimal(number.toString()).setScale(length, BigDecimal.ROUND_HALF_DOWN).toString()

    //保留2位小数，舍弃的部分非零，则直接进位；舍弃的部分为零，不进位
    fun formatNumberByRounding5(number: Double, length: Int = 2): String =
        BigDecimal(number.toString()).setScale(length, BigDecimal.ROUND_UP).toString()

    //保留2位小数，直接截取
    fun formatNumberNoRounding(number: Double, length: Int = 2): String =
        BigDecimal(number.toString()).setScale(length, BigDecimal.ROUND_DOWN).toString()


    @JvmStatic
    fun main(args: Array<String>) {
        println(
            "四舍五入(String.format):\t"
                    + formatNumberByRounding1(1.23499) + "  "
                    + formatNumberByRounding1(1.23999) + "  "
                    + formatNumberByRounding1(0.0) + "  "
                    + formatNumberByRounding1(1.1)
        )
        println(
            "四舍五入(BigDecimal):\t"
                    + formatNumberByRounding2(1.23499) + "  "
                    + formatNumberByRounding2(1.23999) + "  "
                    + formatNumberByRounding2(0.0) + "  "
                    + formatNumberByRounding2(1.1)
        )
        println(
            "四舍五入(DecimalFormat):\t"
                    + formatNumberByRounding3(1.23499) + "  "
                    + formatNumberByRounding3(1.23999) + "  "
                    + formatNumberByRounding3(0.0) + "  "
                    + formatNumberByRounding3(1.1)
        )
        println(
            "五舍六入(BigDecimal):\t"
                    + formatNumberByRounding4(1.235) + "  "
                    + formatNumberByRounding4(1.235001)
        )
        println(
            "非零进位(BigDecimal):\t"
                    + formatNumberByRounding5(1.231) + "  "
                    + formatNumberByRounding5(1.23000) + "  "
                    + formatNumberByRounding5(0.0) + "  "
                    + formatNumberByRounding5(1.00001)
        )
        println(
            "直接截取(BigDecimal):\t"
                    + formatNumberNoRounding(0.0) + "  "
                    + formatNumberNoRounding(0.1) + "  "
                    + formatNumberNoRounding(1.0912344) + "  "
                    + formatNumberNoRounding(1.23999999999)
        )
    }

}