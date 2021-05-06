package com.android.util

import android.content.Context
import android.os.Vibrator

/**
 * Created by xuzhb on 2021/5/6
 * Desc:震动相关工具类
 * 需要权限，<uses-permission android:name="android.permission.VIBRATE" />
 */
object VibrationUtil {

    /**
     * 震动
     *
     * @param milliseconds 震动时长 ( 毫秒 )
     */
    fun vibrate(context: Context, milliseconds: Long): Boolean {
        try {
            val vibrator = getVibratorService(context)
            vibrator.vibrate(milliseconds)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    /**
     * pattern 模式震动
     *
     * @param pattern new long[]{400, 800, 1200, 1600}, 等待400ms，震动800ms，等待1200ms，震动1600ms
     * @param repeat  指定 pattern 数组的索引, 指定 pattern 数组中从 repeat 索引开始的震动进行循环,
     *                -1 表示不重复, 非 -1 表示从 pattern 数组指定下标开始重复震动
     */
    fun vibrate(context: Context, pattern: LongArray?, repeat: Int): Boolean {
        if (pattern == null || pattern.isEmpty()) {
            return false
        }
        try {
            val vibrator = getVibratorService(context)
            vibrator.vibrate(pattern, repeat)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    /**
     * 取消震动
     */
    fun cancel(context: Context): Boolean {
        try {
            getVibratorService(context).cancel()
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    fun getVibratorService(context: Context): Vibrator {
        return context.applicationContext.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

}