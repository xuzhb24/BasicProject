package com.android.util

/**
 * Created by xuzhb on 2020/7/14
 * Desc:快速点击检测工具
 */
object CheckFastClickUtil {

    private var mLastClickTime: Long = 0

    fun isFastClick(interval: Long = 400): Boolean {
        val currentTime = System.currentTimeMillis()
        return if (currentTime - mLastClickTime > interval) {
            mLastClickTime = currentTime;
            false
        } else {
            true
        }
    }

}