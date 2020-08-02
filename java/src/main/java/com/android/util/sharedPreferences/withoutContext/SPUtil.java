package com.android.util.sharedPreferences.withoutContext;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;

import com.android.base.BaseApplication;
import com.android.util.DataDueUtil;

import java.util.Map;
import java.util.Set;

/**
 * Created by xuzhb on 2019/10/15
 * Desc:SharedPreferences工具类，不带Context
 */
public class SPUtil {

    //文件名
    private static final String SP_NAME = "share_data";

    //保存字符串数据
    public static void putString(String key, String value) {
        getSharedPref().edit().putString(key, value).apply();
    }

    //保存字符串数据，saveTime：保存的时间，以秒为单位
    public static void putString(String key, String value, int saveTime) {
        getSharedPref().edit().putString(key, DataDueUtil.newStringWithTimeInfo(value, saveTime)).apply();
    }

    //读取字符串数据
    public static String getString(String key, String defValue) {
        String value = getSharedPref().getString(key, defValue);
        if (!DataDueUtil.isDue(value)) {  //对于无时效性的数据和尚未过期的数据，isDue返回false
            return DataDueUtil.clearTimeInfo(value);
        } else {
            remove(key);  //数据过期后移除原来的数据内容
            return null;
        }
    }

    //保存字符串集合
    public static void putStringSet(String key, @Nullable Set<String> value) {
        getSharedPref().edit().putStringSet(key, value).apply();
    }

    //读取字符串集合
    public static Set<String> getStringSet(String key, @Nullable Set<String> defValue) {
        return getSharedPref().getStringSet(key, defValue);
    }

    //保存Int值
    public static void putInt(String key, int value) {
        getSharedPref().edit().putInt(key, value).apply();
    }

    //读取Int值
    public static int getInt(String key, int defValue) {
        return getSharedPref().getInt(key, defValue);
    }

    //保存Long值
    public static void putLong(String key, long value) {
        getSharedPref().edit().putLong(key, value).apply();
    }

    //读取Long值
    public static long getLong(String key, long defValue) {
        return getSharedPref().getLong(key, defValue);
    }

    //保存Boolean值
    public static void putBoolean(String key, boolean value) {
        getSharedPref().edit().putBoolean(key, value).apply();
    }

    //读取Boolean值
    public static boolean getBoolean(String key, boolean defValue) {
        return getSharedPref().getBoolean(key, defValue);
    }

    //保存Float值
    public static void putFloat(String key, float value) {
        getSharedPref().edit().putFloat(key, value).apply();
    }

    //读取Float值
    public static float getFloat(String key, float defValue) {
        return getSharedPref().getFloat(key, defValue);
    }

    //是否存在某个key
    public static boolean contains(String key) {
        return getSharedPref().contains(key);
    }

    //获取所有键值对
    public static Map<String, ?> getAll() {
        return getSharedPref().getAll();
    }

    //移除某个key值对应的值
    public static void remove(String key) {
        getSharedPref().edit().remove(key).apply();
    }

    //清除所有数据
    public static void clear() {
        getSharedPref().edit().clear().apply();
    }

    //获取SharedPreferences的Editor对象
    private static SharedPreferences getSharedPref() {
        return BaseApplication.getInstance().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

}

