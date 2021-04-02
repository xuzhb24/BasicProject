package com.android.widget.PhotoViewer.listener

/**
 * Created by xuzhb on 2021/3/30
 * Desc:手势监听
 */
interface OnGestureListener {

    /**
     * 拖动
     *
     * @param dx 水平方向上拖动的距离
     * @param dy 竖直方向上拖动的距离
     */
    fun onDrag(dx: Float, dy: Float)

    /**
     * 快速滑动
     *
     * @param startX    滑动起点的x坐标
     * @param startY    滑动起点的y坐标
     * @param velocityX 水平方向上滑动的速度
     * @param velocityY 竖直方向上滑动的速度
     */
    fun onFling(startX: Float, startY: Float, velocityX: Float, velocityY: Float)

    /**
     * 缩放
     *
     * @param scaleFactor 缩放因子
     * @param focusX      组成该手势的两个触点的中点的x坐标，单位是像素
     * @param focusY      组成该手势的两个触点的中点的y坐标，单位是像素
     */
    fun onScale(scaleFactor: Float, focusX: Float, focusY: Float)

}