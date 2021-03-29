package com.android.widget.FloatWindow.NoPermission

import android.animation.TimeInterpolator
import android.app.Activity
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.annotation.NonNull

/**
 * Created by xuzhb on 2021/3/25
 * Desc:
 */
interface IFloatWindow {

    fun setView(@NonNull view: View): FloatWindow

    fun setView(@LayoutRes layoutId: Int): FloatWindow

    fun setContentViewId(@IdRes contentViewId: Int): FloatWindow

    fun setLayoutParams(params: ViewGroup.LayoutParams): FloatWindow

    fun setMoveType(@MoveType.moveType moveType: Int, slideLeftMargin: Float, slideRightMargin: Float): FloatWindow

    fun setMoveStyle(duration: Long, @NonNull interpolator: TimeInterpolator): FloatWindow

    fun attach(activity: Activity)

    fun detach(activity: Activity)

    fun show()

    fun hide()

    fun setX(x: Float)

    /**
     * 更新x坐标
     *
     * @param screenType 以屏幕宽度或屏幕高度为基准
     * @param ratio      占比
     */
    fun setX(@ScreenType.screenType screenType: Int, ratio: Float)

    fun setY(y: Float)

    /**
     * 更新y坐标
     *
     * @param screenType 以屏幕宽度或屏幕高度为基准
     * @param ratio      占比
     */
    fun setY(@ScreenType.screenType screenType: Int, ratio: Float)

    fun setXY(x: Float, y: Float)

    fun getX(): Float

    fun getY(): Float

    fun isShowing(): Boolean

    fun getView(): View?

}