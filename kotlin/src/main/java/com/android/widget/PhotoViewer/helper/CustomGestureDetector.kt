package com.android.widget.PhotoViewer.helper

import android.content.Context
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.VelocityTracker
import android.view.ViewConfiguration
import com.android.widget.PhotoViewer.listener.OnGestureListener
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.sqrt

/**
 * Created by xuzhb on 2021/3/30
 * Desc:自定义手势检测
 */
class CustomGestureDetector constructor(
    val mContext: Context,
    val mListener: OnGestureListener  //手势检测
) {

    companion object {
        private const val INVALID_POINTER_ID = -1
    }

    private var mActivePointerId: Int = INVALID_POINTER_ID
    private var mActivePointerIndex: Int = 0
    private var mIsDragging: Boolean = false
    private var mLastTouchX: Float = 0f
    private var mLastTouchY: Float = 0f
    private var mVelocityTracker: VelocityTracker? = null  //速度追踪，追踪手指在滑动过程中的速度
    private var mDetector: ScaleGestureDetector  //缩放的手势检测
    private var mTouchSlop: Int = 0        //系统所能识别的被认为是滑动的最小距离
    private var mMinimumVelocity: Int = 0  //执行一个fling的最小速度，以每秒像素数为单位

    init {
        val configuration = ViewConfiguration.get(mContext)
        mMinimumVelocity = configuration.scaledMinimumFlingVelocity
        mTouchSlop = configuration.scaledTouchSlop
        val scaleListener = object : ScaleGestureDetector.OnScaleGestureListener {

            //缩放开始，返回结果表示是否处理后继的缩放事件，false表示不会执行onScale()
            override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean {
                return true
            }

            //缩放结束
            override fun onScaleEnd(detector: ScaleGestureDetector?) {
            }

            override fun onScale(detector: ScaleGestureDetector?): Boolean {
                detector?.let {
                    val scaleFactor = it.scaleFactor
                    if (java.lang.Float.isNaN(scaleFactor) || java.lang.Float.isInfinite(scaleFactor)) {
                        return false
                    }
                    if (scaleFactor >= 0) {
                        mListener.onScale(scaleFactor, it.focusX, it.focusY)
                    }
                }
                return true
            }
        }
        mDetector = ScaleGestureDetector(mContext, scaleListener)
    }

    //是否正在缩放
    fun isScaling(): Boolean = mDetector.isInProgress

    //是否正在拖动
    fun isDragging(): Boolean = mIsDragging

    fun onTouchEvent(ev: MotionEvent): Boolean {
        return try {
            mDetector.onTouchEvent(ev)
            processTouchEvent(ev)
        } catch (e: Exception) {
            // Fix for support lib bug, happening when onDestroy is called
            true
        }
    }

    private fun processTouchEvent(ev: MotionEvent): Boolean {
        val action = ev.action
        when (action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                mActivePointerId = ev.getPointerId(0)
                mVelocityTracker = VelocityTracker.obtain()
                mVelocityTracker?.addMovement(ev)
                mLastTouchX = getActiveX(ev)
                mLastTouchY = getActiveY(ev)
                mIsDragging = false
            }
            MotionEvent.ACTION_MOVE -> {
                val x = getActiveX(ev)
                val y = getActiveY(ev)
                val dx = x - mLastTouchX
                val dy = y - mLastTouchY
                if (!mIsDragging) {
                    mIsDragging = sqrt((dx * dx) + (dy * dy)) >= mTouchSlop  //判断是否正在滑动
                }
                if (mIsDragging) {
                    mListener.onDrag(dx, dy)
                    mLastTouchX = x
                    mLastTouchY = y
                    mVelocityTracker?.addMovement(ev)
                }
            }
            MotionEvent.ACTION_CANCEL -> {
                mActivePointerId = INVALID_POINTER_ID
                //释放VelocityTracker
                mVelocityTracker?.recycle()
                mVelocityTracker = null
            }
            MotionEvent.ACTION_UP -> {
                mActivePointerId = INVALID_POINTER_ID
                if (mIsDragging) {
                    mVelocityTracker?.let {
                        mLastTouchX = getActiveX(ev)
                        mLastTouchY = getActiveY(ev)
                        it.addMovement(ev)
                        it.computeCurrentVelocity(1000)
                        //计算水平方向和竖直方向上手指滑动的速度
                        val vX = it.xVelocity
                        val vY = it.yVelocity
                        if (max(abs(vX), abs(vY)) >= mMinimumVelocity) {
                            //如果超过了快速滑动的最小距离，则定性为一次快速滑动
                            mListener.onFling(mLastTouchX, mLastTouchY, -vX, -vY)
                        }
                    }
                }
                //释放VelocityTracker
                mVelocityTracker?.recycle()
                mVelocityTracker = null
            }
            //当屏幕上有多个点被按住，松开其中一个点时触发（即非最后一个点被放开时）触发
            MotionEvent.ACTION_POINTER_UP -> {
                val pointerIndex = getPointerIndex(ev.action)
                val pointerId = ev.getPointerId(pointerIndex)
                if (pointerId == mActivePointerId) {
                    //重新设置一个新的位置
                    val newPointerIndex = if (pointerIndex == 0) 1 else 0
                    mActivePointerId = ev.getPointerId(newPointerIndex)
                    mLastTouchX = ev.getX(newPointerIndex)
                    mLastTouchY = ev.getX(newPointerIndex)
                }
            }
        }
        mActivePointerIndex = ev.findPointerIndex(if (mActivePointerId != INVALID_POINTER_ID) mActivePointerId else 0)
        return true
    }

    private fun getActiveX(ev: MotionEvent): Float {
        return try {
            ev.getX(mActivePointerIndex)
        } catch (e: Exception) {
            ev.x
        }
    }

    private fun getActiveY(ev: MotionEvent): Float {
        return try {
            ev.getY(mActivePointerIndex)
        } catch (e: Exception) {
            ev.y
        }
    }

    private fun getPointerIndex(action: Int): Int {
        return action and MotionEvent.ACTION_POINTER_INDEX_MASK shr MotionEvent.ACTION_POINTER_INDEX_SHIFT
    }

}