package com.android.widget.PhotoViewer.listener

import android.widget.ImageView

/**
 * Created by xuzhb on 2021/3/30
 * Desc:图片外部区域点击事件监听
 */
interface OnOutsidePhotoTapListener {

    /**
     * 点击图片外部区域时回调
     */
    fun onOutsidePhotoTap(imageView: ImageView)

}