package com.android.widget.PhotoViewer.listener

import android.view.View

/**
 * Created by xuzhb on 2021/3/30
 * Desc:ImageView点击事件监听
 */
interface OnViewTapListener {

    /**
     * ImageView点击时回调
     *
     * @param view 点击的ImageView
     * @param x    点击的X坐标
     * @param y    点击的Y坐标
     */
    fun onViewTap(view: View, x: Float, y: Float)

}