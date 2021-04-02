package com.android.widget.PhotoViewer.listener

/**
 * Created by xuzhb on 2021/3/30
 * Desc:图片拖动监听
 */
interface OnViewDragListener {

    /**
     * 拖动图片时回调，当缩放图片时不会触发次回调
     *
     * @param dx 水平方向上拖动的距离
     * @param dy 竖直方向上拖动的距离
     */
    fun onDrag(dx: Float, dy: Float)

}