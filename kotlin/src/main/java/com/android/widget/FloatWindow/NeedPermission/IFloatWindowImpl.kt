package com.android.widget.FloatWindow.NeedPermission

import android.animation.*
import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.view.*
import android.view.animation.DecelerateInterpolator
import com.android.util.LogUtil
import com.android.util.ScreenUtil
import com.android.widget.FloatWindow.NeedPermission.compat.MIUI
import kotlin.math.abs

/**
 * Created by xuzhb on 2021/3/30
 * Desc:
 */
class IFloatWindowImpl constructor(val mBuilder: FloatWindow.Builder) : IFloatWindow {

    private var mScaledTouchSlop: Int = 0  //系统最小滑动距离
    private var mX: Int = 0                //当前X坐标
    private var mY: Int = 0                //当前Y坐标
    private var mOriginX: Int = 0          //原始X坐标
    private var mOriginY: Int = 0          //原始Y坐标
    private var isShow: Boolean = false    //是否显示
    private var isInit: Boolean = false    //是否已添加
    private var isRemove: Boolean = false  //是否被移除
    private var mAnimator: ValueAnimator? = null
    private var mDecelerateInterpolator: TimeInterpolator? = null
    private var mWindowManager: WindowManager? = null
    private var mLayoutParams: WindowManager.LayoutParams? = null

    init {
        mScaledTouchSlop = ViewConfiguration.get(mBuilder.mContext).scaledTouchSlop
        initPermission()  //申请悬浮窗权限
        initTouchEvent()
        isInit = true
        isShow = true
        FloatLifecycle(mBuilder.mContext, mBuilder.isShow, mBuilder.mActivities, mBuilder.mExitActivity,
            object : FloatLifecycle.OnLifecycleListener {
                override fun onShow() {
                    show()
                }

                override fun onHide() {
                    hide()
                }

                override fun onBackToDesktop() {
                    if (!mBuilder.isDesktopShow) {
                        hide()
                    }
                    mBuilder.mOnViewStateListener?.onBackToDesktop()
                }
            })
    }

    private fun initPermission() {
        mWindowManager = mBuilder.mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        mLayoutParams = WindowManager.LayoutParams().apply {
            format = PixelFormat.RGBA_8888
            flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            windowAnimations = 0
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            request()
        } else if (MIUI.isMIUI()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                request()
            } else {
                requestForMIUI()
            }
        } else {
            try {
                mLayoutParams?.type = WindowManager.LayoutParams.TYPE_TOAST
                mWindowManager?.addView(mBuilder.mView, mLayoutParams)
            } catch (e: Exception) {
                mWindowManager?.removeView(mBuilder.mView)
                LogUtil.e("FloatLayout", "TYPE_TOAST失败")
                request()
            }
        }
    }

    //申请权限
    private fun request() {
        mLayoutParams?.type =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else WindowManager.LayoutParams.TYPE_PHONE
        FloatActivity.request(mBuilder.mContext, object : OnPermissionListener {
            override fun onSuccess() {
                onSuccessCallBack()
                mBuilder.mOnPermissionListener?.onSuccess()
            }

            override fun onFailure() {
                mBuilder.mOnPermissionListener?.onFailure()
            }
        })
    }

    //申请权限(兼容小米)
    private fun requestForMIUI() {
        mLayoutParams?.type = WindowManager.LayoutParams.TYPE_PHONE
        MIUI.request(mBuilder.mContext, object : OnPermissionListener {
            override fun onSuccess() {
                onSuccessCallBack()
                mBuilder.mOnPermissionListener?.onSuccess()
            }

            override fun onFailure() {
                mBuilder.mOnPermissionListener?.onFailure()
            }
        })
    }

    private fun onSuccessCallBack() {
        mLayoutParams?.let {
            it.width = mBuilder.mWidth
            it.height = mBuilder.mHeight
            it.gravity = mBuilder.mGravity
            it.x = mBuilder.mXOffset
            it.y = mBuilder.mYOffset
            mX = mBuilder.mXOffset
            mY = mBuilder.mYOffset
            mOriginX = mBuilder.mXOffset
            mOriginY = mBuilder.mYOffset
            mWindowManager?.addView(mBuilder.mView, it)
        }
    }

    private fun initTouchEvent() {
        if (mBuilder.mMoveType == MoveType.inactive) {
            return
        }
        mBuilder.mView?.let {
            var layout = if (it is ViewGroup) it.findViewById<View>(mBuilder.mContentViewId) else it
            if (layout == null) {
                layout = it
            }
            layout.setOnTouchListener(object : View.OnTouchListener {

                var downX = 0f
                var downY = 0f
                var upX = 0f
                var upY = 0f
                var lastX = 0f
                var lastY = 0f
                var offsetX = 0f
                var offsetY = 0f
                var newX = 0f
                var newY = 0f
                var isClick = false

                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                    event?.let { it ->
                        when (it.action) {
                            MotionEvent.ACTION_DOWN -> {
                                downX = it.rawX
                                downY = it.rawY
                                lastX = it.rawX
                                lastY = it.rawY
                                cancelAnimator()
                            }
                            MotionEvent.ACTION_MOVE -> {
                                offsetX = it.rawX - lastX
                                offsetY = it.rawY - lastY
                                newX = getX() + offsetX
                                newY = getY() + offsetY
                                updateXY(newX.toInt(), newY.toInt())
                                mBuilder.mOnViewStateListener?.onPositionUpdate(newX.toInt(), newY.toInt())
                                lastX = it.rawX
                                lastY = it.rawY
                            }
                            MotionEvent.ACTION_UP -> {
                                upX = it.rawX
                                upY = it.rawY
                                isClick = abs(upX - downX) > mScaledTouchSlop || abs(upY - downY) > mScaledTouchSlop
                                when (mBuilder.mMoveType) {
                                    MoveType.slide -> {
                                        val startX = getX()
                                        val endX =
                                            if (startX * 2 + v!!.width > ScreenUtil.getScreenWidth()) ScreenUtil.getScreenWidth() - v.width - mBuilder.mSlideRightMargin
                                            else mBuilder.mSlideLeftMargin
                                        mAnimator = ObjectAnimator.ofInt(startX, endX)
                                        mAnimator?.addUpdateListener { animation ->
                                            val x = animation.animatedValue as Int
                                            updateX(x)
                                            mBuilder.mOnViewStateListener?.onPositionUpdate(x, upY.toInt())
                                        }
                                        startAnimator()
                                    }
                                    MoveType.back -> {
                                        val pvhX = PropertyValuesHolder.ofInt("x", getX(), mOriginX)
                                        val pvhY = PropertyValuesHolder.ofInt("y", getY(), mOriginY)
                                        mAnimator = ObjectAnimator.ofPropertyValuesHolder(pvhX, pvhY)
                                        mAnimator?.addUpdateListener { animation ->
                                            val x = animation.getAnimatedValue("x") as Int
                                            val y = animation.getAnimatedValue("y") as Int
                                            updateXY(x, y)
                                            mBuilder.mOnViewStateListener?.onPositionUpdate(x, y)
                                        }
                                        startAnimator()
                                    }
                                }
                            }
                        }
                    }
                    return isClick
                }
            })
        }
    }

    override fun show() {
        if (!isInit) {
            initPermission()
            isInit = true
        } else {
            if (isShow) {
                return
            }
            getView()?.visibility = View.VISIBLE
        }
        isShow = true
        mBuilder.mOnViewStateListener?.onShow()
    }

    override fun hide() {
        if (!isInit || !isShow) {
            return
        }
        getView()?.visibility = View.INVISIBLE
        isShow = false
        mBuilder.mOnViewStateListener?.onHide()
    }

    override fun isShowing(): Boolean = isShow

    override fun getX(): Int = mX

    override fun getY(): Int = mY

    override fun updateX(x: Int) {
        if (isRemove) {
            return
        }
        mLayoutParams?.x = x
        mBuilder.mXOffset = x
        mX = x
        mWindowManager?.updateViewLayout(mBuilder.mView, mLayoutParams)
    }

    override fun updateX(screenType: Int, ratio: Float) {
        val x =
            ((if (screenType == ScreenType.width) ScreenUtil.getScreenWidth() else ScreenUtil.getScreenHeight()) * ratio).toInt()
        updateX(x)
    }

    override fun updateY(y: Int) {
        if (isRemove) {
            return
        }
        mLayoutParams?.y = y
        mBuilder.mYOffset = y
        mY = y
        mWindowManager?.updateViewLayout(mBuilder.mView, mLayoutParams)
    }

    override fun updateY(screenType: Int, ratio: Float) {
        val y =
            ((if (screenType == ScreenType.width) ScreenUtil.getScreenWidth() else ScreenUtil.getScreenHeight()) * ratio).toInt()
        updateY(y)
    }

    override fun updateXY(x: Int, y: Int) {
        if (isRemove) {
            return
        }
        mLayoutParams?.x = x
        mBuilder.mXOffset = x
        mX = x
        mLayoutParams?.y = y
        mBuilder.mYOffset = y
        mY = y
        mWindowManager?.updateViewLayout(mBuilder.mView, mLayoutParams)
    }

    override fun getView(): View? = mBuilder.mView

    override fun dismiss() {
        isShow = false
        isRemove = true
        mWindowManager?.removeView(mBuilder.mView)
        mBuilder.mOnViewStateListener?.onDismiss()
    }

    private fun startAnimator() {
        if (mBuilder.mInterpolator == null) {
            if (mDecelerateInterpolator == null) {
                mDecelerateInterpolator = DecelerateInterpolator()
            }
            mBuilder.mInterpolator = mDecelerateInterpolator
        }
        mAnimator?.let {
            it.interpolator = mBuilder.mInterpolator
            it.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    it.removeAllUpdateListeners()
                    it.removeAllListeners()
                    mAnimator = null
                    mBuilder.mOnViewStateListener?.onMoveAnimEnd()
                }
            })
            it.setDuration(mBuilder.mDuration).start()
            mBuilder.mOnViewStateListener?.onMoveAnimStart()
        }
    }

    private fun cancelAnimator() {
        mAnimator?.let {
            if (it.isRunning) {
                it.cancel()
            }
        }
    }

}