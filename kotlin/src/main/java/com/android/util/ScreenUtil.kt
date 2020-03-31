package com.android.util

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager

/**
 * Created by xuzhb on 2020/3/29
 * Desc:屏幕相关工具类
 */
object ScreenUtil {

    //获取屏幕宽度，单位像素
    fun getScreenWidth(context: Context): Int = getDisplayMetrics(context).widthPixels

    //获取屏幕高度，单位像素
    fun getScreenHeight(context: Context): Int = getDisplayMetrics(context).heightPixels

    //获取屏幕参数
    fun getDisplayMetrics(context: Context): DisplayMetrics {
        val manager = context.applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val dm = DisplayMetrics()
        manager.defaultDisplay.getMetrics(dm)
        return dm
    }

}