package com.android.util;

import com.google.gson.Gson;

/**
 * Created by xuzhb on 2019/10/27
 * Desc:JSON工具类
 */
public class JsonUtil {

    private static final String TAG = "JsonUtil";

    //格式化Json字符串
    public static String formatJson(String strJson) {
        // 计数tab的个数
        int tabNum = 0;
        StringBuffer jsonFormat = new StringBuffer();
        int length = strJson.length();
        char last = 0;
        for (int i = 0; i < length; i++) {
            char c = strJson.charAt(i);
            if (c == '{') {
                tabNum++;
                jsonFormat.append(c + "\n");
                jsonFormat.append(getSpaceOrTab(tabNum));
            } else if (c == '}') {
                tabNum--;
                jsonFormat.append("\n");
                jsonFormat.append(getSpaceOrTab(tabNum));
                jsonFormat.append(c);
            } else if (c == ',') {
                jsonFormat.append(c + "\n");
                jsonFormat.append(getSpaceOrTab(tabNum));
            } else if (c == ':') {
                jsonFormat.append(c + " ");
            } else if (c == '[') {
                tabNum++;
                char next = strJson.charAt(i + 1);
                if (next == ']') {
                    jsonFormat.append(c);
                } else {
                    jsonFormat.append(c + "\n");
                    jsonFormat.append(getSpaceOrTab(tabNum));
                }
            } else if (c == ']') {
                tabNum--;
                if (last == '[') {
                    jsonFormat.append(c);
                } else {
                    jsonFormat.append("\n" + getSpaceOrTab(tabNum) + c);
                }
            } else {
                jsonFormat.append(c);
            }
            last = c;
        }
        return jsonFormat.toString();
    }

    private static String getSpaceOrTab(int tabNum) {
        StringBuffer sbTab = new StringBuffer();
        for (int i = 0; i < tabNum; i++) {
            sbTab.append('\t');
        }
        return sbTab.toString();
    }

    public static void printString(String strJson) {
        printString(TAG, strJson);
    }

    public static void printString(String tag, String strJson) {
        String space = "===============================";
        LogUtil.logLongTag(tag, " \n" + space + "\n" + formatJson(strJson) + "\n" + space);
    }

    public static void printObject(Object obj) {
        printObject(TAG, obj);
    }

    public static void printObject(String tag, Object obj) {
        printString(tag, new Gson().toJson(obj));
    }

}
