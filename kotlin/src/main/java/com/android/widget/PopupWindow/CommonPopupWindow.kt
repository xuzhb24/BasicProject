package com.android.widget.PopupWindow

import android.app.Activity
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.support.annotation.FloatRange
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupWindow
import com.android.widget.ViewHolder

/**
 * Created by xuzhb on 2019/9/3
 * Desc:通用的PopupWindow
 */
class CommonPopupWindow private constructor(context: Context) : PopupWindow() {

    private var mWindowHelper: WindowHelper? = null

    init {
        if (context is Activity) {
            mWindowHelper = WindowHelper(context)
        }
    }

    override fun dismiss() {
        super.dismiss()
        mWindowHelper?.setBackGroundAlpha(1.0f)
    }

    //默认静态内部类，如果加上inner，即inner class则是非静态内部类
    class Builder(private var mContext: Context) {

        private var mLayoutId: Int = -1          //弹窗的布局id
        private var mWidth: Int = 0              //弹窗的宽度
        private var mHeight: Int = 0             //弹窗的高度
        private var mAlpha: Float = 1.0f        //背景透明度
        private var mAnimationStyle: Int = -1   //动画
        private var mTouchable: Boolean = true  //是否可点击
        private var mBackgroundDrawable: Drawable = ColorDrawable(0x00000000)  //背景drawable
        private var mOnViewListener: ((holder: ViewHolder, popupWindow: PopupWindow) -> Unit)? = null

        //通过布局id设置弹窗布局的View
        fun setContentView(layoutId: Int): Builder {
            mLayoutId = layoutId
            return this
        }

        //设置宽高
        fun setViewParams(width: Int, height: Int): Builder {
            mWidth = width
            mHeight = height
            return this
        }

        //设置背景灰色程度
        fun setBackGroundAlpha(@FloatRange(from = 0.0, to = 1.0) alpha: Float): Builder {
            mAlpha = alpha
            return this
        }

        //设置动画
        fun setAnimationStyle(animationStyle: Int): Builder {
            mAnimationStyle = animationStyle
            return this
        }

        //设置外部区域是否可点击取消对话框
        fun setOutsideTouchable(touchable: Boolean): Builder {
            mTouchable = touchable
            return this
        }

        //设置弹窗背景
        fun setBackgroundDrawable(drawable: Drawable): Builder {
            mBackgroundDrawable = drawable
            return this
        }

        //设置事件监听
        fun setOnViewListener(listener: (holder: ViewHolder, popupWindow: PopupWindow) -> Unit): Builder {
            mOnViewListener = listener
            return this
        }

        fun build(): CommonPopupWindow {
            val popupWindow = CommonPopupWindow(mContext)
            with(popupWindow) {
                if (mLayoutId != -1) {
                    contentView = LayoutInflater.from(mContext).inflate(mLayoutId, null)
                } else {
                    throw IllegalArgumentException("The contentView of PopupWindow is null")
                }

                if (mWidth == 0) {
                    width = ViewGroup.LayoutParams.WRAP_CONTENT
                } else {
                    width = mWidth
                }
                if (mHeight == 0) {
                    height = ViewGroup.LayoutParams.WRAP_CONTENT
                } else {
                    height = mHeight
                }
                mWindowHelper?.setBackGroundAlpha(mAlpha)
                if (mAnimationStyle != -1) {
                    animationStyle = mAnimationStyle
                }
                setBackgroundDrawable(mBackgroundDrawable)
                isOutsideTouchable = mTouchable
                isFocusable = mTouchable

                mOnViewListener?.invoke(ViewHolder(contentView), this)
            }
            return popupWindow
        }

    }

}