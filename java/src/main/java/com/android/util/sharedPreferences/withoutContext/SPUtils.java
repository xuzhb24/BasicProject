package com.android.util.sharedPreferences.withoutContext;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;

import com.android.base.BaseApplication;
import com.android.util.DataDueUtil;

import java.util.Map;
import java.util.Set;

/**
 * Created by xuzhb on 2020/3/23
 * Desc:SharedPreferences工具类，可以通过构造函数创建多个SharedPreferences文件
 */
public class SPUtils {

    private SharedPreferences mSp;
    private SharedPreferences.Editor mEditor;

    public SPUtils(String spName) {
        mSp = BaseApplication.getInstance().getSharedPreferences(spName, Context.MODE_PRIVATE);
        mEditor = mSp.edit();
        mEditor.apply();
    }

    //保存字符串数据
    public void putString(String key, String value) {
        mEditor.putString(key, value).apply();
    }

    //保存字符串数据，saveTime：保存的时间，以秒为单位
    public void putString(String key, String value, int saveTime) {
        mEditor.putString(key, DataDueUtil.newStringWithTimeInfo(value, saveTime)).apply();
    }

    //读取字符串数据
    public String getString(String key, String defValue) {
        String value = mSp.getString(key, defValue);
        if (!DataDueUtil.isDue(value)) {  //对于无时效性的数据和尚未过期的数据，isDue返回false
            return DataDueUtil.clearTimeInfo(value);
        } else {
            remove(key);  //数据过期后移除原来的数据内容
            return null;
        }
    }

    //保存字符串集合
    public void putStringSet(String key, @Nullable Set<String> value) {
        mEditor.putStringSet(key, value).apply();
    }

    //读取字符串集合
    public Set<String> getStringSet(String key, @Nullable Set<String> defValue) {
        return mSp.getStringSet(key, defValue);
    }

    //保存Int值
    public void putInt(String key, int value) {
        mEditor.putInt(key, value).apply();
    }

    //读取Int值
    public int getInt(String key, int defValue) {
        return mSp.getInt(key, defValue);
    }

    //保存Long值
    public void putLong(String key, long value) {
        mEditor.putLong(key, value).apply();
    }

    //读取Long值
    public long getLong(String key, long defValue) {
        return mSp.getLong(key, defValue);
    }

    //保存Boolean值
    public void putBoolean(String key, boolean value) {
        mEditor.putBoolean(key, value).apply();
    }

    //读取Boolean值
    public boolean getBoolean(String key, boolean defValue) {
        return mSp.getBoolean(key, defValue);
    }

    //保存Float值
    public void putFloat(String key, float value) {
        mEditor.putFloat(key, value).apply();
    }

    //读取Float值
    public float getFloat(String key, float defValue) {
        return mSp.getFloat(key, defValue);
    }

    //是否存在某个key
    public boolean contains(String key) {
        return mSp.contains(key);
    }

    //获取所有键值对
    public Map<String, ?> getAll() {
        return mSp.getAll();
    }

    //移除某个key值对应的值
    public void remove(String key) {
        mEditor.remove(key).apply();
    }

    //清除所有数据
    public void clear() {
        mEditor.clear().apply();
    }

}
