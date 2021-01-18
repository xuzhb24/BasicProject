package com.android.widget.PhotoViewer.listener;

import android.graphics.RectF;

/**
 * Created by xuzhb on 2021/1/6
 * Desc:View的Matrix矩阵变化监听
 */
public interface OnMatrixChangedListener {

    /**
     * 当View的边界发生变化时回调
     *
     * @param rect View的新边界
     */
    void onMatrixChanged(RectF rect);

}
