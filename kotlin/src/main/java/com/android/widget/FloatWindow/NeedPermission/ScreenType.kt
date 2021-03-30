package com.android.widget.FloatWindow.NeedPermission

import androidx.annotation.IntDef

/**
 * Created by xuzhb on 2021/3/29
 * Desc:选择以屏幕宽度或屏幕高度为基准
 */
object ScreenType {
    const val width = 0
    const val height = 1

    @IntDef(width, height)
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    internal annotation class screenType
}