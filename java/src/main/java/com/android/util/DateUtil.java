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
    public static final String YMD = "yyyyMMdd";
    public static final String YMD_CH = "yyyy年MM月dd日";
    public static final String H_M_S = "HH:mm:ss";
    public static final String H_M_S_S = "HH:mm:ss.SSS";
    public static final String HMS = "HHmmss";
    public static final String HMS_CH = "HH时mm分ss秒";
    public static final String Y_M_D_H_M_S = "yyyy-MM-dd HH:mm:ss";
    public static final String Y_M_D_H_M_S_S = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String YMDHMS = "yyyyMMddHHmmss";
    public static final String YMDHMS_CH = "yyyy年MM月dd日 HH时mm分ss秒";

    //获取当前的日期，默认格式为"yyyy-MM-dd"
    public static String getCurrentDate() {
        return getCurrentDate(Y_M_D);
    }

    //获取当前的日期
    public static String getCurrentDate(String formatStr) {
        SimpleDateFormat df = new SimpleDateFormat(formatStr, Locale.getDefault());
        return df.format(new Date());
    }

    //获取当前的时间，默认格式为"HH:mm:ss"
    public static String getCurrentTime() {
        return getCurrentTime(H_M_S);
    }

    //获取当前的时间
    public static String getCurrentTime(String formatStr) {
        SimpleDateFormat df = new SimpleDateFormat(formatStr, Locale.getDefault());
        return df.format(new Date());
    }

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
        try {
            SimpleDateFormat df = new SimpleDateFormat(formatStr, Locale.getDefault());
            return df.format(currentTimeMillis);
        } catch (Exception e) {
            e.printStackTrace();
            return "时间格式化异常";
        }
    }

    //日期时间String类型转换成long类型
    public static long string2Long(String dateTime, String formatStr) {
        SimpleDateFormat df = new SimpleDateFormat(formatStr, Locale.getDefault());
        try {
            return df.parse(dateTime).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
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
            return null;
        }
    }

    //判断是否润年
    public static boolean isLeapYear(int year) {
        if ((year % 400) == 0) {
            return true;
        } else if ((year % 4) == 0) {
            if ((year % 100) == 0) {
                return false;
            } else {
                return true;
            }
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

    //获取当天n年前/后的日期,distanceYear为正数表示n年后，distanceYear为负数表示n年前
    public static String getDistanceDateByYear(int distanceYear, String formatStr) {
        Date currentDate = new Date();
        Calendar calender = Calendar.getInstance();
        calender.setTime(currentDate);
        calender.set(Calendar.YEAR, calender.get(Calendar.YEAR) + distanceYear);
        SimpleDateFormat df = new SimpleDateFormat(formatStr, Locale.getDefault());
        return df.format(calender.getTime());
    }

    //获取某一天n年前/后的日期,distanceYear为正数表示n年后，distanceYear为负数表示n年前
    public static String getDistanceDateByYear(String someDay, int distanceYear, String formatStr) {
        SimpleDateFormat df = new SimpleDateFormat(formatStr, Locale.getDefault());
        Date beginDate = null;
        try {
            beginDate = df.parse(someDay);
            Calendar calender = Calendar.getInstance();
            calender.setTime(beginDate);
            calender.set(Calendar.YEAR, calender.get(Calendar.YEAR) + distanceYear);
            return df.format(calender.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return someDay;
    }

    //获取当天n月前/后的日期，distanceMonth为负数表示n月后，distanceMonth为正数表示n月后
    public static String getDistanceDateByMonth(int distanceMonth, String formatStr) {
        Date currentDate = new Date();
        Calendar calender = Calendar.getInstance();
        calender.setTime(currentDate);
        calender.set(Calendar.MONTH, calender.get(Calendar.MONTH) + distanceMonth);
        SimpleDateFormat df = new SimpleDateFormat(formatStr, Locale.getDefault());
        return df.format(calender.getTime());
    }

    //获取某一天n月前/后的日期，distanceMonth为负数表示n月后，distanceMonth为正数表示n月后
    public static String getDistanceDateByMonth(String someDay, int distanceMonth, String formatStr) {
        SimpleDateFormat df = new SimpleDateFormat(formatStr, Locale.getDefault());
        Date beginDate = null;
        try {
            beginDate = df.parse(someDay);
            Calendar calender = Calendar.getInstance();
            calender.setTime(beginDate);
            calender.set(Calendar.MONTH, calender.get(Calendar.MONTH) + distanceMonth);
            return df.format(calender.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return someDay;
    }

    //获取当天n周前/后的日期，distanceWeek为负数表示n周前，distanceWeek为正数表示n周后
    public static String getDistanceDateByWeek(int distanceWeek, String formatStr) {
        Date currentDate = new Date();
        Calendar calender = Calendar.getInstance();
        calender.setTime(currentDate);
        calender.set(Calendar.WEEK_OF_YEAR, calender.get(Calendar.WEEK_OF_YEAR) + distanceWeek);
        SimpleDateFormat df = new SimpleDateFormat(formatStr, Locale.getDefault());
        return df.format(calender.getTime());
    }

    //获取某一天n周前/后的日期，distanceWeek为负数表示n周前，distanceWeek为正数表示n周后
    public static String getDistanceDateByWeek(String someDay, int distanceWeek, String formatStr) {
        SimpleDateFormat df = new SimpleDateFormat(formatStr, Locale.getDefault());
        Date beginDate = null;
        try {
            beginDate = df.parse(someDay);
            Calendar calender = Calendar.getInstance();
            calender.setTime(beginDate);
            calender.set(Calendar.WEEK_OF_YEAR, calender.get(Calendar.WEEK_OF_YEAR) + distanceWeek);
            return df.format(calender.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return someDay;
    }


    //获取当天n天前/后的日期，distanceDay为负数表示n天前，distanceDay为正数表示n天后
    public static String getDistanceDateByDay(int distanceDay, String formatStr) {
        Date currentDate = new Date();
        Calendar calender = Calendar.getInstance();
        calender.setTime(currentDate);
        calender.set(Calendar.DATE, calender.get(Calendar.DATE) + distanceDay);
        SimpleDateFormat df = new SimpleDateFormat(formatStr, Locale.getDefault());
        return df.format(calender.getTime());
    }

    //获取某一天n天前/后的日期，distanceDay为负数表示n天前，distanceDay为正数表示n天后
    public static String getDistanceDateByDay(String someDay, int distanceDay, String formatStr) {
        SimpleDateFormat df = new SimpleDateFormat(formatStr, Locale.getDefault());
        Date beginDate = null;
        try {
            beginDate = df.parse(someDay);
            Calendar calender = Calendar.getInstance();
            calender.setTime(beginDate);
            calender.set(Calendar.DATE, calender.get(Calendar.DATE) + distanceDay);
            return df.format(calender.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return someDay;
    }

    //获取两个日期间隔的天数
    public static int differentDays(String startDate, String endDate, String formatStr) {
        try {
            SimpleDateFormat df = new SimpleDateFormat(formatStr, Locale.getDefault());
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
                int timeDistance = 0;
                for (int i = year1; i < year2; i++) {
                    if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) { //闰年
                        timeDistance += 366;
                    } else { //不是闰年
                        timeDistance += 365;
                    }
                }
                return timeDistance + (day2 - day1);
            } else {
                return day2 - day1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    //获取两个日期间隔的秒数
    public static long differentSeconds(String startDate, String endDate, String formatStr) {
        try {
            SimpleDateFormat df = new SimpleDateFormat(formatStr, Locale.getDefault());
            long start = df.parse(startDate).getTime();
            long end = df.parse(endDate).getTime();
            return Math.abs((end - start) / 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    //某个时间点是否在某段时间内
    public static boolean isBetwennStartAndEnd(String keyDate, String startDate, String endDate, String formatStr) {
        try {
            SimpleDateFormat df = new SimpleDateFormat(formatStr, Locale.getDefault());
            long start = df.parse(startDate).getTime();
            long end = df.parse(endDate).getTime();
            long key = df.parse(keyDate).getTime();
            if (start <= key && key <= end) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //当前时间是否在某段时间内
    public static boolean isBetweenStartAndEnd(String startDate, String endDate, String formatStr) {
        try {
            SimpleDateFormat df = new SimpleDateFormat(formatStr, Locale.getDefault());
            return isBetwennStartAndEnd(df.format(new Date()), startDate, endDate, formatStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //根据日期获取星期几
    public static String getWeek(String dateTime, String formatStr) {
        String week = "";
        SimpleDateFormat df = new SimpleDateFormat(formatStr, Locale.getDefault());
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(df.parse(dateTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        switch (c.get(Calendar.DAY_OF_WEEK)) {
            case 1:
                week = "日";
                break;
            case 2:
                week = "一";
                break;
            case 3:
                week = "二";
                break;
            case 4:
                week = "三";
                break;
            case 5:
                week = "四";
                break;
            case 6:
                week = "五";
                break;
            case 7:
                week = "六";
                break;

        }
        if (TextUtils.isEmpty(week)) {
            return "未知";
        } else {
            return "星期" + week;
        }
    }

}
