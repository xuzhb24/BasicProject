package com.android.widget.PhotoViewer.listener;

import android.widget.ImageView;

/**
 * Created by xuzhb on 2021/1/6
 * Desc:图片外部区域点击事件监听
 */
public interface OnOutsidePhotoTapListener {

    /**
     * 点击图片外部区域时回调
     */
    void onOutsidePhotoTap(ImageView imageView);

}
