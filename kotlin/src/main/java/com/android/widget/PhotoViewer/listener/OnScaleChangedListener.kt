package com.android.widget.PhotoViewer.listener

/**
 * Created by xuzhb on 2021/3/30
 * Desc:图片缩放监听
 */
interface OnScaleChangedListener {

    /**
     * 图片缩放时回调
     *
     * @param scaleFactor 缩放因子（小于1时缩小，大于1时放大）
     * @param focusX      缩放焦点的x坐标
     * @param focusY      缩放焦点的y坐标
     */
    fun onScaleChange(scaleFactor: Float, focusX: Float, focusY: Float)

}