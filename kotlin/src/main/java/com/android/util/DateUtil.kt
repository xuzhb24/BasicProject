package com.android.util

import android.text.TextUtils
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by xuzhb on 2019/9/8
 * Desc:时间相关工具类
 */
object DateUtil {

    const val Y_M_D = "yyyy-MM-dd"
    const val Y_M = "yyyy-MM"
    const val M_D = "MM-dd"
    const val YMD = "yyyyMMdd"
    const val YM = "yyyyMM"
    const val MD = "MMdd"
    const val YMD_CH = "yyyy年MM月dd日"
    const val YM_CH = "yyyy年MM月"
    const val MD_CH = "MM月dd日"
    const val H_M_S = "HH:mm:ss"
    const val H_M = "HH:mm"
    const val M_S = "mm:ss"
    const val H_M_S_S = "HH:mm:ss.SSS"
    const val HMS = "HHmmss"
    const val HM = "HHmm"
    const val MS = "mmss"
    const val HMS_CH = "HH时mm分ss秒"
    const val HM_CH = "HH时mm分"
    const val MS_CH = "mm分ss秒"
    const val Y_M_D_H_M_S = "yyyy-MM-dd HH:mm:ss"
    const val Y_M_D_H_M_S_S = "yyyy-MM-dd HH:mm:ss.SSS"
    const val YMDHMS = "yyyyMMddHHmmss"
    const val YMDHMS_CH = "yyyy年MM月dd日 HH时mm分ss秒"

    //获取当前的日期
    fun getCurrentDateTime(formatStr: String = Y_M_D): String {
        val df = SimpleDateFormat(formatStr, Locale.getDefault())
        return df.format(Date())
    }

    //日期时间Date类型转换成String类型
    fun date2String(date: Date, formatStr: String): String {
        val df = SimpleDateFormat(formatStr, Locale.getDefault())
        return df.format(date)
    }

    //日期时间String类型转换成Date类型
    fun string2Date(dateTime: String, formatStr: String): Date? {
        val df = SimpleDateFormat(formatStr, Locale.getDefault())
        try {
            return df.parse(dateTime)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    //日期时间long类型转换成String类型
    fun long2String(currentTimeMillis: Long, formatStr: String): String {
        val df = SimpleDateFormat(formatStr, Locale.getDefault())
        return df.format(currentTimeMillis)
    }

    //日期时间String类型转换成long类型
    fun string2Long(dateTime: String, formatStr: String): Long? {
        val df = SimpleDateFormat(formatStr, Locale.getDefault())
        try {
            return df.parse(dateTime).time
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    //日期时间Date类型转换成long类型
    fun date2Long(date: Date): Long = date.time

    //日期时间long类型转换成Date类型
    fun long2Date(currentTimeMillis: Long): Date = Date(currentTimeMillis)

    //判断是否润年
    fun isLeapYear(year: Int): Boolean {
        if (year % 400 == 0) {
            return true
        } else if (year % 4 == 0) {
            return year % 100 != 0
        } else {
            return false
        }
    }

    //转换时间格式
    fun convertOtherFormat(dateTime: String, curentFormatStr: String, targetFormatStr: String): String {
        val curentDf = SimpleDateFormat(curentFormatStr, Locale.getDefault())
        val targetDf = SimpleDateFormat(targetFormatStr, Locale.getDefault())
        try {
            val date = curentDf.parse(dateTime)
            return targetDf.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return dateTime
    }

    //比较两个日期大小，正数表示第一个日期在后，负数表示第一个日期在前，0表示相等
    fun compareDate(dateTime1: String, dateTime2: String, formatStr: String): Int {
        val df = SimpleDateFormat(formatStr, Locale.getDefault())
        try {
            val date1 = df.parse(dateTime1)
            val date2 = df.parse(dateTime2)
            return if (date1.time > date2.time) {
                1
            } else if (date1.time < date2.time) {
                -1
            } else {
                0
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return 0
    }

    //获取两个日期间隔的天数
    fun getDistanceDay(startDate: String, endDate: String, formatStr: String): Int {
        val df = SimpleDateFormat(formatStr, Locale.getDefault())
        try {
            val date1 = df.parse(startDate)
            val date2 = df.parse(endDate)
            val calendar1 = Calendar.getInstance()
            val calendar2 = Calendar.getInstance()
            calendar1.time = date1
            calendar2.time = date2
            val day1 = calendar1.get(Calendar.DAY_OF_YEAR)
            val day2 = calendar2.get(Calendar.DAY_OF_YEAR)
            val year1 = calendar1.get(Calendar.YEAR)
            val year2 = calendar2.get(Calendar.YEAR)
            if (year1 != year2) {  //不是同一年
                var distance = 0
                for (i in year1 until year2) {
                    if ((i % 4 == 0 && i % 100 != 0) || i % 400 == 0) {  //闰年
                        distance += 366
                    } else {
                        distance += 365
                    }
                }
                return distance + day2 - day1
            } else {
                return day2 - day1
            }
        } catch (e: Exception) {
        }
        return 0
    }

    //获取两个日期间隔的秒数
    fun getDistanceSecond(startDate: String, endDate: String, formatStr: String): Long {
        val df = SimpleDateFormat(formatStr, Locale.getDefault())
        try {
            val start = df.parse(startDate).time
            val end = df.parse(endDate).time
            return Math.abs((end - start) / 1000)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return 0
    }

    //获取指定日期的当月第一天
    fun getFirstDayOfMonth(dateTime: String, formatStr: String): Date {
        val calendar = Calendar.getInstance()
        with(calendar) {
            time = string2Date(dateTime, formatStr)
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }
        return calendar.time
    }

    //获取指定日期的当月最后一天
    fun getLastDayOfMonth(dateTime: String, formatStr: String): Date {
        val calendar = Calendar.getInstance()
        with(calendar) {
            time = string2Date(dateTime, formatStr)
            set(Calendar.DAY_OF_MONTH, getActualMaximum(Calendar.DAY_OF_MONTH))
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
        }
        return calendar.time
    }

    //获取指定日期n天前/后的日期，distance为负数表示n天前，distance为正数表示n天后，dateTime传空时表示当天
    fun getDistanceDateByDay(distance: Int, formatStr: String, dateTime: String = ""): String {
        val df = SimpleDateFormat(formatStr, Locale.getDefault())
        val calendar = Calendar.getInstance()
        try {
            val date = if (TextUtils.isEmpty(dateTime)) Date() else df.parse(dateTime)
            calendar.time = date
            calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + distance)
            return df.format(calendar.time)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return dateTime
    }

    //获取指定日期n周前/后的日期，distance为负数表示n周前，distance为正数表示n周后，dateTime传空时表示当天
    fun getDistanceDateByWeek(distance: Int, formatStr: String, dateTime: String = ""): String {
        val df = SimpleDateFormat(formatStr, Locale.getDefault())
        val calendar = Calendar.getInstance()
        try {
            val date = if (TextUtils.isEmpty(dateTime)) Date() else df.parse(dateTime)
            calendar.time = date
            calendar.set(Calendar.WEEK_OF_YEAR, calendar.get(Calendar.WEEK_OF_YEAR) + distance)
            return df.format(calendar.time)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return dateTime
    }

    //获取指定日期n月前/后的日期，distance为负数表示n月前，distance为正数表示n月后，dateTime传空时表示当天
    fun getDistanceDateByMonth(distance: Int, formatStr: String, dateTime: String = ""): String {
        val df = SimpleDateFormat(formatStr, Locale.getDefault())
        val calendar = Calendar.getInstance()
        try {
            val date = if (TextUtils.isEmpty(dateTime)) Date() else df.parse(dateTime)
            calendar.time = date
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + distance)
            return df.format(calendar.time)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return dateTime
    }

    //获取指定日期n年前/后的日期，distance为负数表示n年前，distance为正数表示n年后，dateTime传空时表示当天
    fun getDistanceDateByYear(distance: Int, formatStr: String, dateTime: String = ""): String {
        val df = SimpleDateFormat(formatStr, Locale.getDefault())
        val calendar = Calendar.getInstance()
        try {
            val date = if (TextUtils.isEmpty(dateTime)) Date() else df.parse(dateTime)
            calendar.time = date
            calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + distance)
            return df.format(calendar.time)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return dateTime
    }

    //某个时间点是否在某段时间内，dateTime传空时表示当天
    fun isInThePeriod(startDate: String, endDate: String, formatStr: String, dateTime: String = ""): Boolean? {
        val df = SimpleDateFormat(formatStr, Locale.getDefault())
        try {
            val key = if (TextUtils.isEmpty(dateTime)) Date().time else df.parse(dateTime).time
            val start = df.parse(startDate).time
            val end = df.parse(endDate).time
            return if (start <= key && key <= end) true else false
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    //根据日期获取星期几
    fun getWeek(dateTime: String, formatStr: String): String {
        val df = SimpleDateFormat(formatStr, Locale.getDefault())
        val calendar = Calendar.getInstance()
        var week = ""
        try {
            calendar.time = df.parse(dateTime)
            when (calendar.get(Calendar.DAY_OF_WEEK)) {
                1 -> week = "日"
                2 -> week = "一"
                3 -> week = "二"
                4 -> week = "三"
                5 -> week = "四"
                6 -> week = "五"
                7 -> week = "六"
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return if (TextUtils.isEmpty(week)) "未知" else "星期$week"
    }

}