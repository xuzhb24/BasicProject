package com.android.widget.PhotoViewer.listener

import android.graphics.RectF

/**
 * Created by xuzhb on 2021/3/30
 * Desc:View的Matrix矩阵变化监听
 */
interface OnMatrixChangedListener {

    /**
     * 当View的边界发生变化时回调
     *
     * @param rect View的新边界
     */
    fun onMatrixChanged(rectF: RectF)

}