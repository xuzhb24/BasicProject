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

    void dismiss();

}
