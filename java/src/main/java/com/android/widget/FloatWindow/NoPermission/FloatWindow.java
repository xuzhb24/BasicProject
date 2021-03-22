package com.android.widget.FloatWindow.NoPermission;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;

import com.android.util.LogUtil;
import com.android.util.ScreenUtil;
import com.android.util.SizeUtil;
import com.android.widget.ViewHolder;

/**
 * Created by xuzhb on 2021/3/16
 * Desc:悬浮窗
 */
public class FloatWindow implements IFloatWindow {

    private static final String TAG = "FloatWindow";

    private FloatWindow() {
    }

    //静态内部类单例模式
    private static class SingleTonHolder {
        private static final FloatWindow holder = new FloatWindow();
    }

    public static FloatWindow get() {
        return SingleTonHolder.holder;
    }

    private static final int INITIAL_VALUE = -9999;

    private int mScaledTouchSlop;      //系统最小滑动距离
    private int mX = INITIAL_VALUE;
    private int mY = INITIAL_VALUE;
    private int mOriginX, mOriginY;    //原始坐标
    private int mMoveType = MoveType.slide;
    private int mSlideLeftMargin;
    private int mSlideRightMargin;
    private Context mContext;
    private View mView;
    @LayoutRes
    private int mLayoutId;
    @IdRes
    int mContentViewId = -1;
    private ViewGroup.LayoutParams mLayoutParams;
    private ValueAnimator mAnimator;
    private TimeInterpolator mInterpolator;
    private long mDuration = 300;
    private OnViewListener mOnViewListener;

    @Override
    public FloatWindow setView(@NonNull View view) {
        mView = view;
        return this;
    }

    @Override
    public FloatWindow setView(int layoutId) {
        mLayoutId = layoutId;
        return this;
    }

    //设置主要操作的控件Id，提供这个方法主要是为了避免整个布局最大的控件设置了点击事件后无法拖动的现象
    public FloatWindow setContentViewId(@IdRes int contentViewId) {
        mContentViewId = contentViewId;
        return this;
    }

    @Override
    public FloatWindow setLayoutParams(ViewGroup.LayoutParams params) {
        mLayoutParams = params;
        return this;
    }

    @Override
    public FloatWindow setMoveType(int moveType, int slideLeftMargin, int slideRightMargin) {
        mMoveType = moveType;
        mSlideLeftMargin = slideLeftMargin;
        mSlideRightMargin = slideRightMargin;
        return this;
    }

    @Override
    public FloatWindow setMoveStyle(long duration, @NonNull TimeInterpolator interpolator) {
        mDuration = duration;
        mInterpolator = interpolator;
        return this;
    }

    @Override
    public FloatWindow setX(int x) {
        mX = x;
        if (mView != null) {
            mView.setX(x);
        }
        return this;
    }

    @Override
    public FloatWindow setX(int screenType, float ratio) {
        int x = (int) ((screenType == ScreenType.width ?
                ScreenUtil.getScreenWidth() :
                ScreenUtil.getScreenHeight()) * ratio);
        setX(x);
        return this;
    }

    @Override
    public FloatWindow setY(int y) {
        mY = y;
        if (mView != null) {
            mView.setY(y);
        }
        return this;
    }

    @Override
    public FloatWindow setY(int screenType, float ratio) {
        int y = (int) ((screenType == ScreenType.width ?
                ScreenUtil.getScreenWidth() :
                ScreenUtil.getScreenHeight()) * ratio);
        setY(y);
        return this;
    }

    @Override
    public FloatWindow setXY(int x, int y) {
        mX = x;
        mY = y;
        if (mView != null) {
            mView.setX(x);
            mView.setY(y);
        }
        return this;
    }

    public FloatWindow setOnViewListener(OnViewListener listener) {
        mOnViewListener = listener;
        return this;
    }

    @Override
    public void attach(Activity activity) {
        LogUtil.i(TAG, "attach activity:" + activity.getClass().getName());
        if (isAttachedToWindow()) {
            return;
        }
        FrameLayout container = getActivityContainer(activity);
        if (mView == null && mLayoutId == 0) {
            throw new IllegalArgumentException("View has not been initialize!");
        }
        if (mView == null) {
            mView = LayoutInflater.from(container.getContext().getApplicationContext()).inflate(mLayoutId, null);
        }
        mContext = activity.getApplicationContext();
        mScaledTouchSlop = ViewConfiguration.get(mContext).getScaledTouchSlop();
        if (mView.getParent() == container) {
            LogUtil.i(TAG, "View has setted");
            return;
        }
        if (mView.getParent() != null) {
            LogUtil.i(TAG, "remove view");
            ((ViewGroup) mView.getParent()).removeView(mView);
        }
        mLayoutParams = getDefaultParams();
        mView.setLayoutParams(mLayoutParams);
        if (mX != INITIAL_VALUE) {
            mView.setX(mX);
        } else {
            mX = (int) mView.getX();
        }
        if (mY != INITIAL_VALUE) {
            mView.setY(mY);
        } else {
            mY = (int) mView.getY();
        }
        mOriginX = (int) mView.getX();
        mOriginY = (int) mView.getY();
        LogUtil.i(TAG, "mX:" + mX + " mY:" + mY);
        if (mOnViewListener != null) {
            mOnViewListener.onView(new ViewHolder(mView), mView);
        }
        initTouchEvent();
        container.addView(mView);
    }

    private void initTouchEvent() {
        if (mView == null) {
            return;
        }
        switch (mMoveType) {
            case MoveType.inactive:
                break;
            default:
                View layout = (mView instanceof ViewGroup) ? mView.findViewById(mContentViewId) : mView;
                if (layout == null) {
                    layout = mView;
                }
                layout.setOnTouchListener(new View.OnTouchListener() {

                    float downX, downY, upX, upY, lastX, lastY, offsetX, offsetY;
                    int newX, newY;
                    boolean isClick = false;

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                LogUtil.i(TAG, "ACTION_DOWN,isClick:" + isClick);
                                downX = event.getRawX();
                                downY = event.getRawY();
                                lastX = event.getRawX();
                                lastY = event.getRawY();
                                cancelAnimator();
                                break;
                            case MotionEvent.ACTION_MOVE:
                                offsetX = event.getRawX() - lastX;
                                offsetY = event.getRawY() - lastY;
                                newX = (int) (getX() + offsetX);
                                newY = (int) (getY() + offsetY);
                                setXY(newX, newY);
                                lastX = event.getRawX();
                                lastY = event.getRawY();
                                LogUtil.i(TAG, "ACTION_MOVE,mX:" + mX + " mY:" + mY);
                                break;
                            case MotionEvent.ACTION_UP:
                                upX = event.getRawX();
                                upY = event.getRawY();
                                isClick = (Math.abs(upX - downX) > mScaledTouchSlop) || (Math.abs(upY - downY) > mScaledTouchSlop);
                                switch (mMoveType) {
                                    case MoveType.slide:
                                        int startX = (int) getX();
                                        int endX = (startX * 2 + v.getWidth() > ScreenUtil.getScreenWidth(mContext)) ?
                                                ScreenUtil.getScreenWidth(mContext) - v.getWidth() - mSlideRightMargin :
                                                mSlideLeftMargin;
                                        mAnimator = ObjectAnimator.ofInt(startX, endX);
                                        mAnimator.addUpdateListener(animation -> {
                                            int x = (int) animation.getAnimatedValue();
                                            setX(x);
                                        });
                                        startAnimator();
                                        break;
                                    case MoveType.back:
                                        PropertyValuesHolder pvhX = PropertyValuesHolder.ofInt("x", (int) getX(), mOriginX);
                                        PropertyValuesHolder pvhY = PropertyValuesHolder.ofInt("y", (int) getY(), mOriginY);
                                        mAnimator = ObjectAnimator.ofPropertyValuesHolder(pvhX, pvhY);
                                        mAnimator.addUpdateListener(animation -> {
                                            int x = (int) animation.getAnimatedValue("x");
                                            int y = (int) animation.getAnimatedValue("y");
                                            setXY(x, y);
                                        });
                                        startAnimator();
                                        break;
                                }
                                LogUtil.i(TAG, "ACTION_UP,isClick:" + isClick + ",mMoveType:" + mMoveType + ",mX:" + mX + " mY:" + mY);
                                break;
                        }
                        return isClick;
                    }
                });
        }
    }

    @Override
    public void detach(Activity activity) {
        LogUtil.i(TAG, "detach activity:" + activity.getClass().getName());
        FrameLayout container = getActivityContainer(activity);
        if (isAttachedToWindow()) {
            container.removeView(mView);
            mOnViewListener = null;
            mView = null;
        }
    }

    @Override
    public void show() {
        if (mView == null) {
            return;
        }
        mView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hide() {
        if (mView == null) {
            return;
        }
        mView.setVisibility(View.GONE);
    }

    @Override
    public float getX() {
        return mX;
    }

    @Override
    public float getY() {
        return mY;
    }

    @Override
    public boolean isShowing() {
        if (mView == null) {
            return false;
        }
        return mView.getVisibility() == View.VISIBLE;
    }

    @Override
    public View getView() {
        return mView;
    }

    private FrameLayout getActivityContainer(@NonNull Activity activity) {
        return activity.findViewById(android.R.id.content);
    }

    private FrameLayout.LayoutParams getDefaultParams() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
//        params.gravity = Gravity.END | Gravity.BOTTOM;
//        params.bottomMargin = SizeUtil.dp2pxInt(300);
        return params;
    }

    private boolean isAttachedToWindow() {
        if (mView != null && ViewCompat.isAttachedToWindow(mView)) {
            LogUtil.i(TAG, "View has attached to window");
            return true;
        }
        LogUtil.i(TAG, "View doesn't attached to window");
        return false;
    }

    private void startAnimator() {
        if (mInterpolator == null) {
            mInterpolator = new DecelerateInterpolator();
        }
        mAnimator.setInterpolator(mInterpolator);
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mAnimator.removeAllUpdateListeners();
                mAnimator.removeAllListeners();
                mAnimator = null;
            }
        });
        mAnimator.setDuration(mDuration).start();
    }

    private void cancelAnimator() {
        if (mAnimator != null && mAnimator.isRunning()) {
            mAnimator.cancel();
        }
    }

}
