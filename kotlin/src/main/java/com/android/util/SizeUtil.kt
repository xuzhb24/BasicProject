package com.android.util

import com.android.base.BaseApplication

/**
 * Created by xuzhb on 2019/8/1
 * Desc:尺寸信息单位转换
 */
object SizeUtil {

    fun px2dp(pxValue: Float): Float {
        val density: Float = BaseApplication.instance.resources.displayMetrics.density
        return pxValue / density + 0.5f
    }

    fun dp2px(dpValue: Float): Float {
        val density: Float = BaseApplication.instance.resources.displayMetrics.density
        return dpValue * density + 0.5f
    }

    fun px2sp(pxValue: Float): Float {
        val scaledDensity: Float = BaseApplication.instance.resources.displayMetrics.scaledDensity
        return pxValue / scaledDensity + 0.5f
    }

    fun sp2px(spValue: Float): Float {
        val scaledDensity: Float = BaseApplication.instance.resources.displayMetrics.scaledDensity
        return spValue * scaledDensity + 0.5f
    }

}