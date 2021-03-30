package com.android.widget.FloatWindow.NeedPermission

/**
 * Created by xuzhb on 2021/3/29
 * Desc:权限申请结果回调
 */
interface OnPermissionListener {

    //授权成功
    fun onSuccess()

    //授权失败
    fun onFailure()

}