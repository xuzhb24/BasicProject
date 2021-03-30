package com.android.widget.FloatWindow.NeedPermission;

import android.view.View;

/**
 * Created by xuzhb on 2021/3/8
 * Desc:
 */
public interface IFloatWindow {

    void show();

    void hide();

    boolean isShowing();

    int getX();

    int getY();

    void updateX(int x);

    /**
     * 更新x坐标
     *
     * @param screenType 以屏幕宽度或屏幕高度为基准
     * @param ratio      占比
     */
    void updateX(@ScreenType.screenType int screenType, float ratio);

    void updateY(int y);

    /**
     * 更新y坐标
     *
     * @param screenType 以屏幕宽度或屏幕高度为基准
     * @param ratio      占比
     */
    void updateY(@ScreenType.screenType int screenType, float ratio);

    void updateXY(int x, int y);

    View getView();

    //使用FloatWindow.destroy()来取消弹窗，不要手动调用下面的方法
    @Deprecated
    void dismiss();

}
