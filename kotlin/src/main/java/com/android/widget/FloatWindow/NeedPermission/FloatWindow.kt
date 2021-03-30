package com.android.widget.FloatWindow.NeedPermission

import android.animation.TimeInterpolator
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.annotation.MainThread
import androidx.annotation.NonNull
import com.android.util.ScreenUtil
import com.android.widget.ViewHolder

/**
 * Created by xuzhb on 2021/3/30
 * Desc:悬浮窗
 */
object FloatWindow {

    private const val mDefaultTag = "default_float_window_tag"
    private var mFloatWindowMap: HashMap<String, IFloatWindow>? = null

    @MainThread
    fun with(context: Context): Builder = Builder(context.applicationContext)

    fun get(tag: String = mDefaultTag): IFloatWindow? {
        return mFloatWindowMap?.get(tag)
    }

    fun destroy(tag: String = mDefaultTag) {
        mFloatWindowMap?.let {
            if (it.containsKey(tag)) {
                it[tag]?.dismiss()
                it.remove(tag)
            }
        }
    }

    class Builder constructor(val mContext: Context) {
        var mView: View? = null

        @LayoutRes
        var mLayoutId: Int = 0

        @IdRes
        var mContentViewId: Int = -1
        var mWidth: Int = ViewGroup.LayoutParams.WRAP_CONTENT
        var mHeight: Int = ViewGroup.LayoutParams.WRAP_CONTENT
        var mGravity: Int = Gravity.TOP or Gravity.START
        var mXOffset: Int = 0
        var mYOffset: Int = 0
        var mMoveType: Int = MoveType.slide
        var mSlideLeftMargin: Int = 0
        var mSlideRightMargin: Int = 0
        var mDuration: Long = 300
        var isShow: Boolean = true
        var isDesktopShow: Boolean = false
        var mTag: String = mDefaultTag
        var mExitActivity: Class<*>? = null  //应用退出回到桌面的最后一个Activity
        var mActivities: Array<out Class<*>>? = null
        var mInterpolator: TimeInterpolator? = null
        var mOnViewStateListener: OnViewStateListener? = null
        var mOnPermissionListener: OnPermissionListener? = null
        private var mOnViewListener: ((holder: ViewHolder, view: View) -> Unit)? = null

        fun setView(@NonNull view: View): Builder {
            mView = view
            return this
        }

        fun setView(@LayoutRes layoutId: Int): Builder {
            mLayoutId = layoutId
            return this
        }

        //设置主要操作的控件Id，提供这个方法主要是为了避免整个布局最大的控件设置了点击事件后无法拖动的现象
        fun setContentViewId(@IdRes contentViewId: Int): Builder {
            mContentViewId = contentViewId
            return this
        }

        fun setWidth(width: Int): Builder {
            mWidth = width
            return this
        }

        fun setHeight(height: Int): Builder {
            mHeight = height
            return this
        }

        fun setWidth(@ScreenType.screenType screenType: Int, ratio: Float): Builder {
            mWidth =
                ((if (screenType == ScreenType.width) ScreenUtil.getScreenWidth() else ScreenUtil.getScreenHeight()) * ratio).toInt()
            return this
        }

        fun setHeight(@ScreenType.screenType screenType: Int, ratio: Float): Builder {
            mHeight =
                ((if (screenType == ScreenType.width) ScreenUtil.getScreenWidth() else ScreenUtil.getScreenHeight()) * ratio).toInt()
            return this
        }

        fun setX(x: Int): Builder {
            mXOffset = x
            return this
        }

        fun setY(y: Int): Builder {
            mYOffset = y
            return this
        }

        fun setX(@ScreenType.screenType screenType: Int, ratio: Float): Builder {
            mXOffset =
                ((if (screenType == ScreenType.width) ScreenUtil.getScreenWidth() else ScreenUtil.getScreenHeight()) * ratio).toInt()
            return this
        }

        fun setY(@ScreenType.screenType screenType: Int, ratio: Float): Builder {
            mYOffset =
                ((if (screenType == ScreenType.width) ScreenUtil.getScreenWidth() else ScreenUtil.getScreenHeight()) * ratio).toInt()
            return this
        }

        /**
         * 设置Activity过滤器，用于指定在哪些界面显示悬浮窗，默认全部界面都显示
         *
         * @param show       是否显示
         * @param activities 进行过滤的Activity
         */
        fun setFilter(show: Boolean, @NonNull vararg activities: Class<*>): Builder {
            isShow = show
            mActivities = activities
            return this
        }

        /**
         * 设置带边距的贴边动画，只有moveType为MoveType.slide，设置边距才有意义
         *
         * @param moveType         移动方式
         * @param slideLeftMargin  贴边动画的左边距，默认为0
         * @param slideRightMargin 贴边动画的右边距，默认为0
         */
        fun setMoveType(@MoveType.moveType moveType: Int, slideLeftMargin: Int, slideRightMargin: Int): Builder {
            mMoveType = moveType
            mSlideLeftMargin = slideLeftMargin
            mSlideRightMargin = slideRightMargin
            return this
        }

        fun setMoveStyle(duration: Long, @NonNull interpolator: TimeInterpolator): Builder {
            mDuration = duration
            mInterpolator = interpolator
            return this
        }

        fun setTag(@NonNull tag: String): Builder {
            mTag = tag
            return this
        }

        fun setDesktopShow(show: Boolean, @NonNull exitActivity: Class<*>): Builder {
            isDesktopShow = show
            mExitActivity = exitActivity
            return this
        }

        fun setOnViewStateListener(listener: OnViewStateListener): Builder {
            mOnViewStateListener = listener
            return this
        }

        fun setOnPermissionListener(listener: OnPermissionListener): Builder {
            mOnPermissionListener = listener
            return this
        }

        fun setOnViewListener(listener: (holder: ViewHolder, view: View) -> Unit): Builder {
            mOnViewListener = listener
            return this
        }

        fun build() {
            if (mFloatWindowMap == null) {
                mFloatWindowMap = HashMap()
            }
            mFloatWindowMap?.let {
                if (it.containsKey(mTag) && FloatPermissionUtil.hasPermission(mContext)) {
                    throw IllegalArgumentException("FloatWindow of this tag has been added, Please set a new tag for the new FloatWindow")
                }
                if (mView == null && mLayoutId == 0) {
                    throw IllegalArgumentException("View has not been initialize!")
                }
                if (mView == null) {
                    mView = LayoutInflater.from(mContext).inflate(mLayoutId, null)
                }
                //设置contentView上控件的事件监听
                mView?.let { mOnViewListener?.invoke(ViewHolder(it), it) }
                val floatWindow = IFloatWindowImpl(this)
                it.put(mTag, floatWindow)
            }
        }

    }

}