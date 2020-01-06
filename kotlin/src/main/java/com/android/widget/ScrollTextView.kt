package com.android.widget

import android.content.Context
import android.support.annotation.AttrRes
import android.support.v7.widget.AppCompatTextView
import android.text.TextUtils
import android.util.AttributeSet

/**
 * Created by xuzhb on 2020/1/7
 * Desc:TextView文字滚动显示，即跑马灯效果（不需要获取焦点）
 */
class ScrollTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, @AttrRes defStyleAttrs: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttrs) {

    init {
        setSingleLine(true)
        ellipsize = TextUtils.TruncateAt.MARQUEE  //设置跑马灯效果
        marqueeRepeatLimit = -1  //设置循环滚动为无限循环
    }

    override fun isFocused(): Boolean = true

}