package com.android.widget.FloatWindow.NeedPermission

/**
 * Created by xuzhb on 2021/3/29
 * Desc:状态改变监听
 */
interface OnViewStateListener {

    fun onPositionUpdate(x: Int, y: Int)

    fun onShow()

    fun onHide()

    fun onDismiss()

    fun onMoveAnimStart()

    fun onMoveAnimEnd()

    fun onBackToDesktop()

}