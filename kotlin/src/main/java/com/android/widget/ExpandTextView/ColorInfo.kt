package com.android.widget.ExpandTextView

import androidx.annotation.ColorInt

/**
 * Created by xuzhb on 2024/5/22
 * Desc:字体变色
 */
data class ColorInfo(
    val startPosition: Int,   //变色起始位置
    val endPosition: Int,     //变色结束位置
    @ColorInt val color: Int  //字体颜色
) {
    fun isActive(position: Int) = position in startPosition..endPosition
}