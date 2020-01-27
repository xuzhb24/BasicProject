package com.android.widget.PicGetterDialog;

import android.graphics.Bitmap;

/**
 * Create by xuzhb on 2020/1/23
 * Desc:
 */
public interface OnPicGetterListener {

    /**
     * 图片获取成功
     *
     * @param bitmap  图片
     * @param picPath 图片路径
     */
    void onSuccess(Bitmap bitmap, String picPath);

    /**
     * 图片获取失败
     *
     * @param errorMsg 错误信息
     */
    void onFailure(String errorMsg);

    /**
     * 取消图片获取
     */
    void onCancel();

}
