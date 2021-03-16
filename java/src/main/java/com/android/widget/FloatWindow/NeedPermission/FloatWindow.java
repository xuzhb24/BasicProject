package com.android.widget.FloatWindow.NeedPermission;

import android.animation.TimeInterpolator;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;

import com.android.util.ScreenUtil;
import com.android.widget.ViewHolder;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xuzhb on 2021/3/8
 * Desc:悬浮窗
 */
public class FloatWindow {

    private FloatWindow() {
    }

    private static final String mDefaultTag = "default_float_window_tag";
    private static Map<String, IFloatWindow> mFloatWindowMap;

    @MainThread
    public static Builder with(@NonNull Context context) {
        return new Builder(context);
    }

    public static IFloatWindow get() {
        return get(mDefaultTag);
    }

    public static IFloatWindow get(@NonNull String tag) {
        return mFloatWindowMap == null ? null : mFloatWindowMap.get(tag);
    }

    public static void destroy() {
        destroy(mDefaultTag);
    }

    public static void destroy(String tag) {
        if (mFloatWindowMap == null || !mFloatWindowMap.containsKey(tag)) {
            return;
        }
        mFloatWindowMap.get(tag).dismiss();
        mFloatWindowMap.remove(tag);
    }

    public static class Builder {
        Context mContext;
        View mView;
        @LayoutRes
        int mLayoutId;
        @IdRes
        int mContentViewId = -1;
        int mWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
        int mHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
        int mGravity = Gravity.TOP | Gravity.START;
        int mXOffset;
        int mYOffset;
        int mMoveType = MoveType.slide;
        int mSlideLeftMargin;
        int mSlideRightMargin;
        long mDuration = 300;
        boolean isShow = true;
        boolean isDesktopShow;
        String mTag = mDefaultTag;
        Class mExitActivity;   //应用退出回到桌面的最后一个Activity
        Class[] mActivities;
        TimeInterpolator mInterpolator;
        OnViewStateListener mOnViewStateListener;
        OnPermissionListener mOnPermissionListener;
        private OnViewListener mOnViewListener;

        private Builder() {
        }

        public Builder(Context context) {
            mContext = context.getApplicationContext();
        }

        public Builder setView(@NonNull View view) {
            mView = view;
            return this;
        }

        public Builder setView(@LayoutRes int layoutId) {
            mLayoutId = layoutId;
            return this;
        }

        //设置主要操作的控件Id，提供这个方法主要是为了避免整个布局最大的控件设置了点击事件后无法拖动的现象
        public Builder setContentViewId(@IdRes int contentViewId) {
            mContentViewId = contentViewId;
            return this;
        }

        public Builder setWidth(int width) {
            mWidth = width;
            return this;
        }

        public Builder setHeight(int height) {
            mHeight = height;
            return this;
        }

        public Builder setWidth(@ScreenType.screenType int screenType, float ratio) {
            mWidth = (int) ((screenType == ScreenType.width ? ScreenUtil.getScreenWidth(mContext) : ScreenUtil.getScreenHeight(mContext)) * ratio);
            return this;
        }

        public Builder setHeight(@ScreenType.screenType int screenType, float ratio) {
            mHeight = (int) ((screenType == ScreenType.width ? ScreenUtil.getScreenWidth(mContext) : ScreenUtil.getScreenHeight(mContext)) * ratio);
            return this;
        }

        public Builder setX(int x) {
            mXOffset = x;
            return this;
        }

        public Builder setY(int y) {
            mYOffset = y;
            return this;
        }

        public Builder setX(@ScreenType.screenType int screenType, float ratio) {
            mXOffset = (int) ((screenType == ScreenType.width ? ScreenUtil.getScreenWidth(mContext) : ScreenUtil.getScreenHeight(mContext)) * ratio);
            return this;
        }

        public Builder setY(@ScreenType.screenType int screenType, float ratio) {
            mYOffset = (int) ((screenType == ScreenType.width ? ScreenUtil.getScreenWidth(mContext) : ScreenUtil.getScreenHeight(mContext)) * ratio);
            return this;
        }

        /**
         * 设置Activity过滤器，用于指定在哪些界面显示悬浮窗，默认全部界面都显示
         *
         * @param show       是否显示
         * @param activities 进行过滤的Activity
         */
        public Builder setFilter(boolean show, @NonNull Class... activities) {
            isShow = show;
            mActivities = activities;
            return this;
        }

        /**
         * 设置带边距的贴边动画，只有moveType为MoveType.slide，设置边距才有意义
         *
         * @param moveType         移动方式
         * @param slideLeftMargin  贴边动画的左边距，默认为0
         * @param slideRightMargin 贴边动画的右边距，默认为0
         */
        public Builder setMoveType(@MoveType.moveType int moveType, int slideLeftMargin, int slideRightMargin) {
            mMoveType = moveType;
            mSlideLeftMargin = slideLeftMargin;
            mSlideRightMargin = slideRightMargin;
            return this;
        }

        public Builder setMoveStyle(long duration, @NonNull TimeInterpolator interpolator) {
            mDuration = duration;
            mInterpolator = interpolator;
            return this;
        }

        public Builder setTag(@NonNull String tag) {
            mTag = tag;
            return this;
        }

        public Builder setDesktopShow(boolean show, @NonNull Class exitActivity) {
            isDesktopShow = show;
            mExitActivity = exitActivity;
            return this;
        }

        public Builder setOnViewStateListener(OnViewStateListener listener) {
            mOnViewStateListener = listener;
            return this;
        }

        public Builder setOnPermissionListener(OnPermissionListener listener) {
            mOnPermissionListener = listener;
            return this;
        }

        public Builder setOnViewListener(OnViewListener listener) {
            mOnViewListener = listener;
            return this;
        }

        public void build() {
            if (mFloatWindowMap == null) {
                mFloatWindowMap = new HashMap<>();
            }
            if (mFloatWindowMap.containsKey(mTag) && FloatPermissionUtil.hasPermission(mContext)) {
                throw new IllegalArgumentException("FloatWindow of this tag has been added, Please set a new tag for the new FloatWindow");
            }
            if (mView == null && mLayoutId == 0) {
                throw new IllegalArgumentException("View has not been initialize!");
            }
            if (mView == null) {
                mView = LayoutInflater.from(mContext).inflate(mLayoutId, null);
            }
            //设置contentView上控件的事件监听
            if (mOnViewListener != null) {
                mOnViewListener.onView(new ViewHolder(mView), mView);
            }
            IFloatWindow floatWindow = new IFloatWindowImpl(this);
            mFloatWindowMap.put(mTag, floatWindow);
        }

    }

}
