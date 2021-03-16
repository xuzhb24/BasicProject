package com.android.widget.FloatWindow.NeedPermission;

/**
 * Created by xuzhb on 2021/3/8
 * Desc:权限申请结果回调
 */
public interface OnPermissionListener {

    //授权成功
    void onSuccess();

    //授权失败
    void onFailure();

}
