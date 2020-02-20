package com.android.util;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by xuzhb on 2019/9/8
 * Desc:时间相关工具类
 */
public class DateUtil {

    public static final String Y_M_D = "yyyy-MM-dd";
    public static final String Y_M = "yyyy-MM";
    public static final String M_D = "MM-dd";
    public static final String YMD = "yyyyMMdd";
    public static final String YM = "yyyyMM";
    public static final String MD = "MMdd";
    public static final String YMD_CH = "yyyy年MM月dd日";
    public static final String YM_CH = "yyyy年MM月";
    public static final String MD_CH = "MM月dd日";
    public static final String H_M_S_S = "HH:mm:ss.SSS";
    public static final String H_M_S = "HH:mm:ss";
    public static final String H_M = "HH:mm";
    public static final String M_S = "mm:ss";
    public static final String HMSS = "HHmmssSSS";
    public static final String HMS = "HHmmss";
    public static final String HM = "HHmm";
    public static final String MS = "mmss";
    public static final String HMSS_CH = "HH时mm分ss秒SSS毫秒";
    public static final String HMS_CH = "HH时mm分ss秒";
    public static final String HM_CH = "HH时mm分";
    public static final String MS_CH = "mm分ss秒";
    public static final String Y_M_D_H_M_S_S = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String Y_M_D_H_M_S = "yyyy-MM-dd HH:mm:ss";
    public static final String YMDHMSS_CH = "yyyy年MM月dd日 HH时mm分ss秒SSS毫秒";
    public static final String YMDHMS_CH = "yyyy年MM月dd日 HH时mm分ss秒";
    public static final String YMDHMSS = "yyyyMMddHHmmssSSS";
    public static final String YMDHMS = "yyyyMMddHHmmss";

    //获取当前的日期时间，默认格式为"yyyy-MM-dd HH:mm:ss"
    public static String getCurrentDateTime() {
        return getCurrentDateTime(Y_M_D_H_M_S);
    }

    //获取当前的日期时间
    public static String getCurrentDateTime(String formatStr) {
        SimpleDateFormat df = new SimpleDateFormat(formatStr, Locale.getDefault());
        return df.format(new Date());
    }

    //日期时间Date类型转换成String类型
    public static String date2String(Date date, String formatStr) {
        SimpleDateFormat df = new SimpleDateFormat(formatStr, Locale.getDefault());
        return df.format(date);
    }

    //日期时间String类型转换成Date类型
    public static Date string2Date(String dateTime, String formatStr) {
        SimpleDateFormat df = new SimpleDateFormat(formatStr, Locale.getDefault());
        try {
            return df.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    //日期时间long类型转换成String类型
    public static String long2String(long currentTimeMillis, String formatStr) {
        SimpleDateFormat df = new SimpleDateFormat(formatStr, Locale.getDefault());
        try {
            return df.format(currentTimeMillis);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //日期时间String类型转换成long类型
    public static long string2Long(String dateTime, String formatStr) {
        SimpleDateFormat df = new SimpleDateFormat(formatStr, Locale.getDefault());
        try {
            return df.parse(dateTime).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    //日期时间Date类型转换成long类型
    public static long date2Long(Date date) {
        return date.getTime();
    }

    //日期时间long类型转换成Date类型
    public static Date long2Date(long currentTimeMillis) {
        try {
            return new Date(currentTimeMillis);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //判断是否润年
    public static boolean isLeapYear(int year) {
        if ((year % 400) == 0) {
            return true;
        } else if (year % 4 == 0) {
            return year % 100 != 0;
        } else {
            return false;
        }
    }

    //转换时间格式
    public static String convertOtherFormat(String dateTime, String curentFormatStr, String targetFormatStr) {
        SimpleDateFormat curentDf = new SimpleDateFormat(curentFormatStr, Locale.getDefault());
        SimpleDateFormat targetDf = new SimpleDateFormat(targetFormatStr, Locale.getDefault());
        try {
            Date date = curentDf.parse(dateTime);
            return targetDf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateTime;
    }

    //比较两个日期大小，正数表示第一个日期在后，负数表示第一个日期在前，0表示相等
    public static int compareDate(String dateTime1, String dateTime2, String formatStr) {
        SimpleDateFormat df = new SimpleDateFormat(formatStr, Locale.getDefault());
        try {
            Date date1 = df.parse(dateTime1);
            Date date2 = df.parse(dateTime2);
            if (date1.getTime() > date2.getTime()) {
                return 1;
            } else if (date1.getTime() < date2.getTime()) {
                return -1;
            } else {
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    //获取当天n年前/后的日期，distance为负数表示n年前，distance为正数表示n年后
    public static String getDistanceDateByYear(int distance, String formatStr) {
        return getDistanceDateByYear(distance, formatStr, "");
    }

    //获取指定日期n年前/后的日期，distance为负数表示n年前，distance为正数表示n年后，dateTime传空时表示当天
    public static String getDistanceDateByYear(int distance, String formatStr, String dateTime) {
        SimpleDateFormat df = new SimpleDateFormat(formatStr, Locale.getDefault());
        try {
            Date date = TextUtils.isEmpty(dateTime) ? new Date() : df.parse(dateTime);
            Calendar calender = Calendar.getInstance();
            calender.setTime(date);
            calender.set(Calendar.YEAR, calender.get(Calendar.YEAR) + distance);
            return df.format(calender.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateTime;
    }

    //获取当天n月前/后的日期，distance为负数表示n月前，distance为正数表示n月后
    public static String getDistanceDateByMonth(int distance, String formatStr) {
        return getDistanceDateByMonth(distance, formatStr, "");
    }

    //获取指定日期n月前/后的日期，distance为负数表示n月前，distance为正数表示n月后，dateTime传空时表示当天
    public static String getDistanceDateByMonth(int distance, String formatStr, String dateTime) {
        SimpleDateFormat df = new SimpleDateFormat(formatStr, Locale.getDefault());
        try {
            Date date = TextUtils.isEmpty(dateTime) ? new Date() : df.parse(dateTime);
            Calendar calender = Calendar.getInstance();
            calender.setTime(date);
            calender.set(Calendar.MONTH, calender.get(Calendar.MONTH) + distance);
            return df.format(calender.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateTime;
    }

    //获取当天n周前/后的日期，distance为负数表示n周前，distance为正数表示n周后，dateTime传空时表示当天
    public static String getDistanceDateByWeek(int distance, String formatStr) {
        return getDistanceDateByWeek(distance, formatStr, "");
    }

    //获取指定日期n周前/后的日期，distance为负数表示n周前，distance为正数表示n周后，dateTime传空时表示当天
    public static String getDistanceDateByWeek(int distance, String formatStr, String dateTime) {
        SimpleDateFormat df = new SimpleDateFormat(formatStr, Locale.getDefault());
        try {
            Date date = TextUtils.isEmpty(dateTime) ? new Date() : df.parse(dateTime);
            Calendar calender = Calendar.getInstance();
            calender.setTime(date);
            calender.set(Calendar.WEEK_OF_YEAR, calender.get(Calendar.WEEK_OF_YEAR) + distance);
            return df.format(calender.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateTime;
    }

    //获取当天n天前/后的日期，distance为负数表示n天前，distance为正数表示n天后
    public static String getDistanceDateByDay(int distance, String formatStr) {
        return getDistanceDateByDay(distance, formatStr, "");
    }

    //获取指定日期n天前/后的日期，distance为负数表示n天前，distance为正数表示n天后，dateTime传空时表示当天
    public static String getDistanceDateByDay(int distance, String formatStr, String dateTime) {
        SimpleDateFormat df = new SimpleDateFormat(formatStr, Locale.getDefault());
        try {
            Date date = TextUtils.isEmpty(dateTime) ? new Date() : df.parse(dateTime);
            Calendar calender = Calendar.getInstance();
            calender.setTime(date);
            calender.set(Calendar.DATE, calender.get(Calendar.DATE) + distance);
            return df.format(calender.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateTime;
    }

    //获取两个时间点间隔的天数
    public static int getDistanceDay(String startDate, String endDate, String formatStr) {
        SimpleDateFormat df = new SimpleDateFormat(formatStr, Locale.getDefault());
        try {
            Date date1 = df.parse(startDate);
            Date date2 = df.parse(endDate);
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(date1);
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(date2);
            int day1 = cal1.get(Calendar.DAY_OF_YEAR);
            int day2 = cal2.get(Calendar.DAY_OF_YEAR);
            int year1 = cal1.get(Calendar.YEAR);
            int year2 = cal2.get(Calendar.YEAR);
            if (year1 != year2) { //同一年
                int distance = 0;
                for (int i = year1; i < year2; i++) {
                    if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) { //闰年
                        distance += 366;
                    } else { //不是闰年
                        distance += 365;
                    }
                }
                return distance + (day2 - day1);
            } else {
                return day2 - day1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    //获取两个时间点间隔的秒数
    public static long getDistanceSecond(String startDate, String endDate, String formatStr) {
        SimpleDateFormat df = new SimpleDateFormat(formatStr, Locale.getDefault());
        try {
            long start = df.parse(startDate).getTime();
            long end = df.parse(endDate).getTime();
            return Math.abs((end - start) / 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    //当前时间是否在某个时间段内
    public static boolean isInThePeriod(String startDate, String endDate, String formatStr) {
        return isInThePeriod(startDate, endDate, formatStr, "");
    }

    //某个时间点是否在某个时间段内，dateTime传空时表示当前时间
    public static boolean isInThePeriod(String startDate, String endDate, String formatStr, String dateTime) {
        SimpleDateFormat df = new SimpleDateFormat(formatStr, Locale.getDefault());
        try {
            long key = TextUtils.isEmpty(dateTime) ? new Date().getTime() : df.parse(dateTime).getTime();
            long start = df.parse(startDate).getTime();
            long end = df.parse(endDate).getTime();
            return start <= key && key <= end;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //获取当天的零点时间，即"00:00:00.000"
    public static long getStartTimeOfToday() {
        return getStartTimeOfDay("", "");
    }

    //获取某一天的零点时间，即"00:00:00.000"，dateTime传空时表示当天
    public static long getStartTimeOfDay(String dateTime, String formatStr) {
        try {
            Calendar c = Calendar.getInstance();
            if (!TextUtils.isEmpty(dateTime)) {
                SimpleDateFormat df = new SimpleDateFormat(formatStr, Locale.getDefault());
                c.setTime(df.parse(dateTime));
            }
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.MILLISECOND, 0);
            return c.getTimeInMillis();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    //获取当天的最后时间，即"23:59:59.999"
    public static long getEndTimeOfToday() {
        return getEndTimeOfDay("", "");
    }

    //获取某一天的最后时间，即"23:59:59.999"，dateTime传空时表示当天
    public static long getEndTimeOfDay(String dateTime, String formatStr) {
        try {
            Calendar c = Calendar.getInstance();
            if (!TextUtils.isEmpty(dateTime)) {
                SimpleDateFormat df = new SimpleDateFormat(formatStr, Locale.getDefault());
                c.setTime(df.parse(dateTime));
            }
            c.set(Calendar.HOUR_OF_DAY, 23);
            c.set(Calendar.SECOND, 59);
            c.set(Calendar.MINUTE, 59);
            c.set(Calendar.MILLISECOND, 999);
            return c.getTimeInMillis();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    //获得本月第一天的零点时间，即"00:00:00.000"
    public static long getStartTimeOfCurrentMonth() {
        return getStartTimeOfMonth("", "");
    }

    //获取某月第一天的零点时间，即"00:00:00.000"，dateTime传空时表示本月
    public static long getStartTimeOfMonth(String dateTime, String formatStr) {
        try {
            Calendar c = Calendar.getInstance();
            if (!TextUtils.isEmpty(dateTime)) {
                SimpleDateFormat df = new SimpleDateFormat(formatStr, Locale.getDefault());
                c.setTime(df.parse(dateTime));
            }
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.MILLISECOND, 0);
            c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
            return c.getTimeInMillis();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    //获得本月最后一天的最后时间，即"23:59:59.999"
    public static long getEndTimeOfCurrentMonth() {
        return getEndTimeOfMonth("", "");
    }

    //获取某月最后一天的最后时间，即"23:59:59.999"，dateTime传空时表示本月
    public static long getEndTimeOfMonth(String dateTime, String formatStr) {
        try {
            Calendar c = Calendar.getInstance();
            if (!TextUtils.isEmpty(dateTime)) {
                SimpleDateFormat df = new SimpleDateFormat(formatStr, Locale.getDefault());
                c.setTime(df.parse(dateTime));
            }
            c.set(Calendar.HOUR_OF_DAY, 23);
            c.set(Calendar.SECOND, 59);
            c.set(Calendar.MINUTE, 59);
            c.set(Calendar.MILLISECOND, 999);
            c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
            return c.getTimeInMillis();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    //当天是星期几
    public static String getCurrentDayOfWeekCH() {
        return getDayOfWeekCH("", "");
    }

    //指定日期是星期几，dateTime传空时表示当天
    public static String getDayOfWeekCH(String dateTime, String formatStr) {
        String dayOfWeek = "";
        try {
            Calendar c = Calendar.getInstance();
            if (!TextUtils.isEmpty(dateTime)) {
                SimpleDateFormat df = new SimpleDateFormat(formatStr, Locale.getDefault());
                c.setTime(df.parse(dateTime));
            }
            switch (c.get(Calendar.DAY_OF_WEEK)) {
                case 1:
                    dayOfWeek = "日";
                    break;
                case 2:
                    dayOfWeek = "一";
                    break;
                case 3:
                    dayOfWeek = "二";
                    break;
                case 4:
                    dayOfWeek = "三";
                    break;
                case 5:
                    dayOfWeek = "四";
                    break;
                case 6:
                    dayOfWeek = "五";
                    break;
                case 7:
                    dayOfWeek = "六";
                    break;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(dayOfWeek)) {
            return "未知";
        } else {
            return "星期" + dayOfWeek;
        }
    }

}
