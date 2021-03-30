package com.android.widget.FloatWindow.NeedPermission

import android.view.View

/**
 * Created by xuzhb on 2021/3/29
 * Desc:
 */
interface IFloatWindow {

    fun show()

    fun hide()

    fun isShowing(): Boolean

    fun getX(): Int

    fun getY(): Int

    fun updateX(x: Int)

    /**
     * 更新x坐标
     *
     * @param screenType 以屏幕宽度或屏幕高度为基准
     * @param ratio      占比
     */
    fun updateX(@ScreenType.screenType screenType: Int, ratio: Float)

    fun updateY(y: Int)

    /**
     * 更新y坐标
     *
     * @param screenType 以屏幕宽度或屏幕高度为基准
     * @param ratio      占比
     */
    fun updateY(@ScreenType.screenType screenType: Int, ratio: Float)

    fun updateXY(x: Int, y: Int)

    fun getView(): View?

    //使用FloatWindow.destroy()来取消弹窗，不要手动调用下面的方法
    @Deprecated("Please use destroy")
    fun dismiss()

}