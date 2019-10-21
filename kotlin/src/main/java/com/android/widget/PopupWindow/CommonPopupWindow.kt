package com.android.widget.PopupWindow

import android.app.Activity
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.support.annotation.FloatRange
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow

/**
 * Created by xuzhb on 2019/9/3
 * Desc:通用的PopupWindow
 */
class CommonPopupWindow private constructor(context: Context) : PopupWindow() {

    private val mWindowHelper: WindowHelper

    init {
        mWindowHelper = WindowHelper(context as Activity)
    }

    override fun dismiss() {
        super.dismiss()
        mWindowHelper.setBackGroundAlpha(1.0f)
    }

    //通过下方的接口获取相应的子View并设置事件监听
    interface OnChildViewListener {
        //layoutId：区分不同布局
        fun onChildView(popupWindow: PopupWindow, view: View, layoutId: Int)
    }

    class Builder(private var mContext: Context) {

        private var mLayoutId: Int = -1          //弹窗的布局id
        private var mView: View? = null          //弹窗的布局
        private var mWidth: Int = 0              //弹窗的宽度
        private var mHeight: Int = 0             //弹窗的高度
        private var mAlpha: Float = 1.0f        //背景透明度
        private var mAnimationStyle: Int = -1   //动画
        private var mTouchable: Boolean = true  //是否可点击
        private var mBackgroundDrawable: Drawable = ColorDrawable(0x00000000)  //背景drawable
        private var mOnViewListener: OnChildViewListener? = null

        //通过布局id设置弹窗布局的View
        fun setContentView(layoutId: Int): Builder {
            mLayoutId = layoutId
            mView = null
            return this
        }

        //设置弹窗布局的View
        fun setContentView(view: View): Builder {
            mLayoutId = -1
            mView = view
            return this
        }

        //设置宽高
        fun setWidthAndHeight(width: Int, height: Int): Builder {
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

        //设置Outside是否可点击
        fun setOutsideTouchable(touchable: Boolean): Builder {
            mTouchable = touchable
            return this
        }

        //设置弹窗背景
        fun setBackgroundDrawable(drawable: Drawable): Builder {
            mBackgroundDrawable = drawable
            return this
        }

        fun setOnViewListener(listener: OnChildViewListener): Builder {
            mOnViewListener = listener
            return this
        }

        fun build(): CommonPopupWindow {
            val popupWindow = CommonPopupWindow(mContext)
            with(popupWindow) {
                if (mLayoutId != -1) {
                    contentView = LayoutInflater.from(mContext).inflate(mLayoutId, null)
                } else if (mView != null) {
                    contentView = mView
                } else {
                    throw IllegalArgumentException("The contentView of PopupWindow is null")
                }

                mOnViewListener?.onChildView(this, contentView, mLayoutId)

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
                mWindowHelper.setBackGroundAlpha(mAlpha)
                if (mAnimationStyle != -1) {
                    animationStyle = mAnimationStyle
                }
                setBackgroundDrawable(mBackgroundDrawable)
                isOutsideTouchable = mTouchable
                isFocusable = mTouchable
            }
            return popupWindow
        }

    }

}