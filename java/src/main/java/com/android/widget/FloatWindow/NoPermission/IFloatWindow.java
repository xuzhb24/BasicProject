package com.android.widget.FloatWindow.NoPermission;

import android.animation.TimeInterpolator;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;


/**
 * Created by xuzhb on 2021/3/16
 * Desc:
 */
public interface IFloatWindow {

    FloatWindow setView(@NonNull View view);

    FloatWindow setView(@LayoutRes int layoutId);

    FloatWindow setLayoutParams(ViewGroup.LayoutParams params);

    FloatWindow setMoveType(@MoveType.moveType int moveType, int slideLeftMargin, int slideRightMargin);

    FloatWindow setMoveStyle(long duration, @NonNull TimeInterpolator interpolator);

    FloatWindow setX(int x);

    /**
     * 更新x坐标
     *
     * @param screenType 以屏幕宽度或屏幕高度为基准
     * @param ratio      占比
     */
    FloatWindow setX(@ScreenType.screenType int screenType, float ratio);

    FloatWindow setY(int y);

    /**
     * 更新y坐标
     *
     * @param screenType 以屏幕宽度或屏幕高度为基准
     * @param ratio      占比
     */
    FloatWindow setY(@ScreenType.screenType int screenType, float ratio);

    FloatWindow setXY(int x, int y);

    void attach(Activity activity);

    void detach(Activity activity);

    void show();

    void hide();

    float getX();

    float getY();

    boolean isShowing();

    View getView();

}
