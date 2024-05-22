package com.android.widget.ExpandTextView

/**
 * Created by xuzhb on 2024/5/22
 * Desc:字体加粗索引
 */
data class BoldInfo(
    val startPosition: Int,  //加粗起始位置
    val endPosition: Int     //加粗结束位置
) {
    fun isActive(position: Int) = position in startPosition..endPosition
}