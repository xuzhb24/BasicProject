package com.android.widget.PhotoViewer.listener

import android.view.MotionEvent

/**
 * Created by xuzhb on 2021/3/30
 * Desc:快速滑动监听
 */
interface OnSingleFlingListener {

    /**
     * 快速滑动时回调
     *
     * @param event1    首次接触时的MotionEvent
     * @param event2    最后一次接触时的MotionEvent
     * @param velocityX 水平滑动的速度
     * @param velocityY 竖直滑动的速度
     */
    fun onFling(event1: MotionEvent, event2: MotionEvent, velocityX: Float, velocityY: Float): Boolean

}