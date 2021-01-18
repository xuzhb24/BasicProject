package com.android.widget.PhotoViewer.listener;

import android.widget.ImageView;

/**
 * Created by xuzhb on 2021/1/6
 * Desc:图片单击事件监听
 */
public interface OnPhotoTapListener {

    /**
     * 点击图片区域以内时回调，图片区域以外不会回调
     *
     * @param imageView 点击的图片
     * @param x         点击的位置x坐标
     * @param y         点击的位置y坐标
     * @param xOffset   点击的位置相对于左上角的横向偏移量，百分比形式
     * @param yOffset   点击的位置相对于左上角的纵向偏移量，百分比形式
     */
    void onPhotoTap(ImageView imageView, float x, float y, float xOffset, float yOffset);

}
