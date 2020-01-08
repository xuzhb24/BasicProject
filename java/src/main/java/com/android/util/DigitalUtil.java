package com.android.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by xuzhb on 2019/12/10
 * Desc:一些浮点数格式化的工具类
 */
public class DigitalUtil {

    //保留2位小数，四舍五入
    public static String formatNumberByRounding1(double number) {
        return String.format("%.2f", number);
    }

    //保留2位小数，四舍五入
    public static String formatNumberByRounding2(double number) {
        return new DecimalFormat("#0.00").format(number);
    }

    //保留2位小数，四舍五入
    public static String formatNumberByRounding3(double number) {
        return formatNumber(number, 2, BigDecimal.ROUND_HALF_UP);
    }

    //保留2位小数，五舍六入，舍弃的部分如果大于5才进位，小于或等于5直接舍弃
    public static String formatNumberByRounding4(double number) {
        return formatNumber(number, 2, BigDecimal.ROUND_HALF_DOWN);
    }

    //保留2位小数，舍弃的部分非零，则直接进位；舍弃的部分为零，不进位
    public static String formatNumberByRounding5(double number) {
        return formatNumber(number, 2, BigDecimal.ROUND_UP);
    }

    //保留2位小数，直接截取
    public static String formatNumberNoRounding(double number) {
        return formatNumber(number, 2, BigDecimal.ROUND_DOWN);
    }

    public static String formatNumber(double number, int length, int roundingMode) {
        BigDecimal bigDecimal = new BigDecimal(String.valueOf(number));
        return bigDecimal.setScale(length, roundingMode).toString();
    }

    //去除小数点尾部的0
    public static String trimEndZero(String numberStr) {
        if (numberStr != null && numberStr.indexOf(".") > 0) {
            numberStr = numberStr.replaceAll("0+?$", "");  //去掉后面无用的零
            numberStr = numberStr.replaceAll("[.]$", "");  //若小数点后面全是零则去掉小数点
        }
        return numberStr;
    }

    //去除小数点尾部的0
    public static String trimEndZero(double number) {
        NumberFormat format = NumberFormat.getInstance();
        format.setGroupingUsed(false);
        return format.format(number);
    }

    public static void main(String[] args) {
        test("四舍五入(String.format)：", 1, 1.23499, 1.23999, 0, 1.1);
        test("四舍五入(DecimalFormat)：", 2, 1.23499, 1.23999, 0.0, 1.1);
        test("四舍五入(BigDecimal)：", 3, 1.23499, 1.23999, 0.0, 1.1);
        test("五舍六入(BigDecimal)：", 4, 1.235, 1.23500001, 1.236);
        test("非零进位(BigDecimal)：", 5, 1.231, 1.23000, 0.0, 1.000001);
        test("直接截取(BigDecimal)：", 6, 0.0, 0.1, 1.0912344, 1.23999999999);
        test("去除尾部的零(正则表达式)：", 7, 1000.0, 2.1, 1.00, 1.2345);
        test("去除尾部的零(NumberFormat)：", 8, 10000.0, 2.1, 1.00, 1.2345, 0.0);
    }

    private static void test(String tag, int type, double... numbers) {
        System.out.print(tag + "\t");
        for (int i = 0; i < numbers.length; i++) {
            switch (type) {
                case 1:
                    System.out.print(formatNumberByRounding1(numbers[i]) + "\t");
                    break;
                case 2:
                    System.out.print(formatNumberByRounding2(numbers[i]) + "\t");
                    break;
                case 3:
                    System.out.print(formatNumberByRounding3(numbers[i]) + "\t");
                    break;
                case 4:
                    System.out.print(formatNumberByRounding4(numbers[i]) + "\t");
                    break;
                case 5:
                    System.out.print(formatNumberByRounding5(numbers[i]) + "\t");
                    break;
                case 6:
                    System.out.print(formatNumberNoRounding(numbers[i]) + "\t");
                    break;
                case 7:
                    System.out.print(trimEndZero(formatNumberByRounding1(numbers[i])) + "\t");
                    break;
                case 8:
                    System.out.print(trimEndZero(numbers[i]) + "\t");
                    break;

            }
        }
        System.out.println();
    }


}
