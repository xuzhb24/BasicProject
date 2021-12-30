package com.android.util

import android.view.View

/**
 * Created by xuzhb on 2021/12/30
 * Desc:View扩展函数
 */
//显示View
fun View?.visible() {
    this?.visibility = View.VISIBLE
}

//隐藏View
fun View?.invisible() {
    this?.visibility = View.INVISIBLE
}

//隐藏View
fun View?.gone() {
    this?.visibility = View.GONE
}