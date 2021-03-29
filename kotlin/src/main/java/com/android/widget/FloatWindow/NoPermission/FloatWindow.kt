package com.android.widget.FloatWindow.NoPermission

import android.animation.*
import android.app.Activity
import android.content.Context
import android.view.*
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.core.view.ViewCompat
import com.android.util.LogUtil
import com.android.util.ScreenUtil
import com.android.widget.ViewHolder
import kotlin.math.abs

/**
 * Created by xuzhb on 2021/3/25
 * Desc:
 */
class FloatWindow private constructor() : IFloatWindow {

    companion object {
        private const val TAG = "FloatWindow"

        //单例对象
        fun get() = SingleTonHolder.holder
    }

    //静态内部类单例模式
    private object SingleTonHolder {
        val holder = FloatWindow()
    }

    private var mScaledTouchSlop: Int = 0  //系统最小滑动距离
    private var mX: Float = 0f
    private var mY: Float = 0f
    private var mOriginX: Float = 0f
    private var mOriginY: Float = 0f
    private var mMoveType: Int = MoveType.slide
    private var mSlideLeftMargin: Float = 0f
    private var mSlideRightMargin: Float = 0f
    private var isShowing: Boolean = true
    private var isFirstTouchDown: Boolean = true
    private var mContext: Context? = null
    private var mView: View? = null

    @LayoutRes
    private var mLayoutId: Int = 0

    @IdRes
    private var mContentViewId: Int = -1
    private var mLayoutParams: ViewGroup.LayoutParams? = null
    private var mAnimator: ValueAnimator? = null
    private var mInterpolator: TimeInterpolator? = null
    private var mDuration: Long = 300
    private var mOnViewListener: ((holder: ViewHolder, view: View) -> Unit)? = null

    override fun setView(view: View): FloatWindow {
        mView = view
        return this
    }

    override fun setView(layoutId: Int): FloatWindow {
        mLayoutId = layoutId
        return this
    }

    //设置主要操作的控件Id，提供这个方法主要是为了避免整个布局最大的控件设置了点击事件后无法拖动的现象
    override fun setContentViewId(contentViewId: Int): FloatWindow {
        mContentViewId = contentViewId
        return this
    }

    override fun setLayoutParams(params: ViewGroup.LayoutParams): FloatWindow {
        mLayoutParams = params
        return this
    }

    override fun setMoveType(moveType: Int, slideLeftMargin: Float, slideRightMargin: Float): FloatWindow {
        mMoveType = moveType
        mSlideLeftMargin = slideLeftMargin
        mSlideRightMargin = slideRightMargin
        return this
    }

    override fun setMoveStyle(duration: Long, interpolator: TimeInterpolator): FloatWindow {
        mDuration = duration
        mInterpolator = interpolator
        return this
    }

    fun setOnViewListener(listener: (holder: ViewHolder, view: View) -> Unit): FloatWindow {
        mOnViewListener = listener
        return this
    }

    override fun attach(activity: Activity) {
        LogUtil.i(TAG, "attach activity:${activity.javaClass.name}")
        mContext = activity.applicationContext
        mScaledTouchSlop = ViewConfiguration.get(mContext).scaledTouchSlop
        if (isAttachedToWindow()) {
            return
        }
        if (mView == null && mLayoutId == 0) {
            throw IllegalArgumentException("View has not been initialize!")
        }
        if (mView == null) {
            mView = LayoutInflater.from(mContext).inflate(mLayoutId, null)
        }
        val container = getActivityContainer(activity)
        mView?.let {
            if (it.parent == container) {
                LogUtil.i(TAG, "View has setted")
                return
            }
            if (it.parent != null) {
                LogUtil.i(TAG, "remove view")
                (it.parent as ViewGroup).removeView(it)
            }
            if (isFirstTouchDown) {  //第一次添加
                if (mLayoutParams == null) {
                    mLayoutParams = getDefaultParams()
                }
                it.layoutParams = mLayoutParams
            } else {  //移动过
                if (mMoveType == MoveType.inactive || mMoveType == MoveType.back) {
                    if (mLayoutParams == null) {
                        mLayoutParams = getDefaultParams()
                    }
                    it.layoutParams = mLayoutParams
                } else {
                    it.layoutParams = getDefaultParams()
                    it.x = mX
                    it.y = mY
                }
            }
            mOnViewListener?.invoke(ViewHolder(it), it)
            initTouchEvent()
            container.addView(it)
            it.visibility = if (isShowing) View.VISIBLE else View.GONE
        }
    }

    private fun initTouchEvent() {
        if (mMoveType == MoveType.inactive) {
            return
        }
        mView?.let {
            var layout = if (it is ViewGroup) it.findViewById<View>(mContentViewId) else it
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
                var isClick = true

                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                    event?.let { it ->
                        when (it.action) {
                            MotionEvent.ACTION_DOWN -> {
                                LogUtil.i(TAG, "ACTION_DOWN,isClick:$isClick")
                                if (isFirstTouchDown) {
                                    mView?.let { view ->
                                        mOriginX = view.x
                                        mOriginY = view.y
                                        mX = view.x
                                        mY = view.y
                                    }
                                    isFirstTouchDown = false
                                }
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
                                setXY(newX, newY)
                                lastX = it.rawX
                                lastY = it.rawY
                                LogUtil.i(TAG, "ACTION_MOVE,mX:$mX mY:$mY")
                            }
                            MotionEvent.ACTION_UP -> {
                                upX = it.rawX
                                upY = it.rawY
                                isClick = abs(upX - downX) > mScaledTouchSlop || abs(upY - downY) > mScaledTouchSlop
                                when (mMoveType) {
                                    MoveType.slide -> {
                                        val startX = getX()
                                        val endX = if (startX * 2 + v!!.width > ScreenUtil.getScreenWidth())
                                            ScreenUtil.getScreenWidth() - v.width - mSlideRightMargin else mSlideLeftMargin
                                        mAnimator = ObjectAnimator.ofFloat(startX, endX)
                                        mAnimator?.addUpdateListener {
                                            val x = it.animatedValue as Float
                                            setX(x)
                                        }
                                        startAnimator()
                                    }
                                    MoveType.back -> {
                                        val pvhX = PropertyValuesHolder.ofFloat("x", getX(), mOriginX)
                                        val pvhY = PropertyValuesHolder.ofFloat("y", getY(), mOriginY)
                                        mAnimator = ObjectAnimator.ofPropertyValuesHolder(pvhX, pvhY)
                                        mAnimator?.addUpdateListener {
                                            val x = it.getAnimatedValue("x") as Float
                                            val y = it.getAnimatedValue("y") as Float
                                            setXY(x, y)
                                        }
                                        startAnimator()
                                    }
                                    else -> {
                                    }
                                }
                                LogUtil.i(TAG, "ACTION_UP,isClick:$isClick,mMoveType:$mMoveType,mX:$mX,mY:$mY")
                            }
                        }
                    }
                    return isClick
                }
            })
        }
    }

    override fun detach(activity: Activity) {
        LogUtil.i(TAG, "detach activity:${activity.javaClass.name}")
        if (isAttachedToWindow()) {
            getActivityContainer(activity).removeView(mView)
            mOnViewListener = null
            mView = null
        }
    }

    override fun show() {
        isShowing = true
        mView?.visibility = View.VISIBLE
    }

    override fun hide() {
        isShowing = false
        mView?.visibility = View.GONE
    }

    override fun setX(x: Float) {
        mX = x
        mView?.x = x
    }

    override fun setX(screenType: Int, ratio: Float) {
        val x = (if (screenType == ScreenType.width) ScreenUtil.getScreenWidth() else ScreenUtil.getScreenHeight()) * ratio
        setX(x)
    }

    override fun setY(y: Float) {
        mY = y
        mView?.y = y
    }

    override fun setY(screenType: Int, ratio: Float) {
        val y = (if (screenType == ScreenType.width) ScreenUtil.getScreenWidth() else ScreenUtil.getScreenHeight()) * ratio
        setY(y)
    }

    override fun setXY(x: Float, y: Float) {
        mX = x
        mY = y
        mView?.let {
            it.x = x
            it.y = y
        }
    }

    override fun getX() = mX

    override fun getY() = mY

    override fun isShowing(): Boolean {
        return mView?.visibility == View.VISIBLE
    }

    override fun getView(): View? = mView

    private fun getActivityContainer(activity: Activity): FrameLayout {
        return activity.findViewById(android.R.id.content)
    }

    private fun getDefaultParams() =
        FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)

    private fun isAttachedToWindow(): Boolean {
        if (mView != null && ViewCompat.isAttachedToWindow(mView!!)) {
            LogUtil.i(TAG, "View has attached to window")
            return true
        }
        LogUtil.i(TAG, "View doesn't attached to window")
        return false
    }

    private fun startAnimator() {
        if (mInterpolator == null) {
            mInterpolator = DecelerateInterpolator()
        }
        mAnimator?.let {
            it.interpolator = mInterpolator
            it.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    it.removeAllUpdateListeners()
                    it.removeAllListeners()
                    mAnimator = null
                }
            })
            it.setDuration(mDuration).start()
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