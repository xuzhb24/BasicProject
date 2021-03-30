package com.android.widget.FloatWindow.NeedPermission

import androidx.annotation.IntDef

/**
 * Created by xuzhb on 2021/3/29
 * Desc:
 */
object MoveType {
    const val inactive = 1  //不可移动
    const val active = 2    //可移动
    const val slide = 3     //移动松手后贴边
    const val back = 4      //移动松手后回到原来的位置

    @IntDef(inactive, active, slide, back)
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    internal annotation class moveType
}