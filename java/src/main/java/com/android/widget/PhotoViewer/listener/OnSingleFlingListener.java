package com.android.widget.PhotoViewer.listener;

import android.view.MotionEvent;

/**
 * Created by xuzhb on 2021/1/6
 * Desc:快速滑动监听
 */
public interface OnSingleFlingListener {

    /**
     * 快速滑动时回调
     *
     * @param event1    首次接触时的MotionEvent
     * @param event2    最后一次接触时的MotionEvent
     * @param velocityX 水平滑动的速度
     * @param velocityY 竖直滑动的速度
     */
    boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY);

}
