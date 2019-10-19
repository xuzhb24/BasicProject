package com.android.util.sharedPreferences.withContext;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by xuzhb on 2019/10/15
 * Desc:SharedPreferences工具类，带Context
 */
public class SPUtil {

    //文件名
    private static final String FILE_NAME = "shared_preferences";

    //保存数据
    public static void put(Context context, String key, Object object) {
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        }
        editor.apply();
    }

    //读取数据
    public static Object get(Context context, String key, Object object) {
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        if (object instanceof String) {
            return sp.getString(key, (String) object);
        } else if (object instanceof Integer) {
            return sp.getInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            return sp.getFloat(key, (Float) object);
        } else if (object instanceof Long) {
            return sp.getLong(key, (Long) object);
        }
        return null;
    }

    //移除某个key值对应的值
    public static void remove(Context context, String key) {
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.clear().apply();
    }

    //清除所有数据
    public static void clearAll(Context context) {
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear().apply();
    }

}

