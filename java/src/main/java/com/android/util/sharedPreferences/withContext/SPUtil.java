package com.android.util.sharedPreferences.withContext;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.android.util.DataDueUtil;

import java.util.Map;
import java.util.Set;

/**
 * Created by xuzhb on 2019/10/15
 * Desc:SharedPreferences工具类，带Context
 */
public class SPUtil {

    //文件名
    private static final String SP_NAME = "share_data";

    //保存字符串数据
    public static void putString(Context context, String key, String value) {
        getSharedPref(context).edit().putString(key, value).apply();
    }

    //保存字符串数据，saveTime：保存的时间，以秒为单位
    public static void putString(Context context, String key, String value, int saveTime) {
        getSharedPref(context).edit().putString(key, DataDueUtil.newStringWithTimeInfo(value, saveTime)).apply();
    }

    //读取字符串数据
    public static String getString(Context context, String key, String defValue) {
        String value = getSharedPref(context).getString(key, defValue);
        if (!DataDueUtil.isDue(value)) {  //对于无时效性的数据和尚未过期的数据，isDue返回false
            return DataDueUtil.clearTimeInfo(value);
        } else {
            remove(context, key);  //数据过期后移除原来的数据内容
            return null;
        }
    }

    //保存字符串集合
    public static void putStringSet(Context context, String key, @Nullable Set<String> value) {
        getSharedPref(context).edit().putStringSet(key, value).apply();
    }

    //读取字符串集合
    public static Set<String> getStringSet(Context context, String key, @Nullable Set<String> defValue) {
        return getSharedPref(context).getStringSet(key, defValue);
    }

    //保存Int值
    public static void putInt(Context context, String key, int value) {
        getSharedPref(context).edit().putInt(key, value).apply();
    }

    //读取Int值
    public static int getInt(Context context, String key, int defValue) {
        return getSharedPref(context).getInt(key, defValue);
    }

    //保存Long值
    public static void putLong(Context context, String key, long value) {
        getSharedPref(context).edit().putLong(key, value).apply();
    }

    //读取Long值
    public static long getLong(Context context, String key, long defValue) {
        return getSharedPref(context).getLong(key, defValue);
    }

    //保存Boolean值
    public static void putBoolean(Context context, String key, boolean value) {
        getSharedPref(context).edit().putBoolean(key, value).apply();
    }

    //读取Boolean值
    public static boolean getBoolean(Context context, String key, boolean defValue) {
        return getSharedPref(context).getBoolean(key, defValue);
    }

    //保存Float值
    public static void putFloat(Context context, String key, float value) {
        getSharedPref(context).edit().putFloat(key, value).apply();
    }

    //读取Float值
    public static float getFloat(Context context, String key, float defValue) {
        return getSharedPref(context).getFloat(key, defValue);
    }

    //是否存在某个key
    public static boolean contains(Context context, String key) {
        return getSharedPref(context).contains(key);
    }

    //获取所有键值对
    public static Map<String, ?> getAll(Context context) {
        return getSharedPref(context).getAll();
    }

    //移除某个key值对应的值
    public static void remove(Context context, String key) {
        getSharedPref(context).edit().remove(key).apply();
    }

    //清除所有数据
    public static void clear(Context context) {
        getSharedPref(context).edit().clear().apply();
    }

    //获取SharedPreferences的Editor对象
    private static SharedPreferences getSharedPref(Context context) {
        return context.getApplicationContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

}

