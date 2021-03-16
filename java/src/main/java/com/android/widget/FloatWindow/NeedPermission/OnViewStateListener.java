package com.android.widget.FloatWindow.NeedPermission;

/**
 * Created by xuzhb on 2021/3/8
 * Desc:状态改变监听
 */
public interface OnViewStateListener {

    void onPositionUpdate(int x, int y);

    void onShow();

    void onHide();

    void onDismiss();

    void onMoveAnimStart();

    void onMoveAnimEnd();

    void onBackToDesktop();

}
