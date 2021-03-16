package com.android.widget.FloatWindow.NeedPermission;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;

import com.android.util.LogUtil;
import com.android.util.ScreenUtil;
import com.android.widget.FloatWindow.NeedPermission.compat.MIUI;

/**
 * Created by xuzhb on 2021/3/8
 * Desc:
 */
public class IFloatWindowImpl implements IFloatWindow {

    private int mScaledTouchSlop;      //系统最小滑动距离
    private int mX, mY;                //当前的X、Y坐标
    private boolean isShow;            //是否显示
    private boolean isInit = false;    //是否已添加
    private boolean isRemove = false;  //是否被移除
    private FloatWindow.Builder mBuilder;
    private ValueAnimator mAnimator;
    private TimeInterpolator mDecelerateInterpolator;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayoutParams;

    private IFloatWindowImpl() {
    }

    public IFloatWindowImpl(FloatWindow.Builder builder) {
        mBuilder = builder;
        mScaledTouchSlop = ViewConfiguration.get(mBuilder.mContext).getScaledTouchSlop();
        initPermission();  //申请悬浮窗权限
        initTouchEvent();
        isInit = true;
        isShow = true;
        new FloatLifecycle(mBuilder.mContext, mBuilder.isShow, mBuilder.mActivities, mBuilder.mExitActivity, new FloatLifecycle.OnLifecycleListener() {
            @Override
            public void onShow() {
                show();
            }

            @Override
            public void onHide() {
                hide();
            }

            @Override
            public void onBackToDesktop() {
                if (!mBuilder.isDesktopShow) {
                    hide();
                }
                if (mBuilder.mOnViewStateListener != null) {
                    mBuilder.mOnViewStateListener.onBackToDesktop();
                }
            }
        });
    }

    public void initPermission() {
        mWindowManager = (WindowManager) mBuilder.mContext.getSystemService(Context.WINDOW_SERVICE);
        mLayoutParams = new WindowManager.LayoutParams();
        mLayoutParams.format = PixelFormat.RGBA_8888;
        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
        mLayoutParams.windowAnimations = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            request();
        } else if (MIUI.isMIUI()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                request();
            } else {
                requestForMIUI();
            }
        } else {
            try {
                mLayoutParams.type = WindowManager.LayoutParams.TYPE_TOAST;
                mWindowManager.addView(mBuilder.mView, mLayoutParams);
            } catch (Exception e) {
                mWindowManager.removeView(mBuilder.mView);
                LogUtil.e("FloatLayout", "TYPE_TOAST失败");
                request();
            }
        }
    }

    //申请权限
    private void request() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mLayoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            mLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        FloatActivity.request(mBuilder.mContext, new OnPermissionListener() {
            @Override
            public void onSuccess() {
                onSuccessCallBack();
                if (mBuilder.mOnPermissionListener != null) {
                    mBuilder.mOnPermissionListener.onSuccess();
                }
            }

            @Override
            public void onFailure() {
                if (mBuilder.mOnPermissionListener != null) {
                    mBuilder.mOnPermissionListener.onFailure();
                }
            }
        });
    }

    //申请权限(兼容小米)
    private void requestForMIUI() {
        mLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        MIUI.request(mBuilder.mContext, new OnPermissionListener() {
            @Override
            public void onSuccess() {
                onSuccessCallBack();
                if (mBuilder.mOnPermissionListener != null) {
                    mBuilder.mOnPermissionListener.onSuccess();
                }
            }

            @Override
            public void onFailure() {
                if (mBuilder.mOnPermissionListener != null) {
                    mBuilder.mOnPermissionListener.onFailure();
                }
            }
        });
    }

    private void onSuccessCallBack() {
        mLayoutParams.width = mBuilder.mWidth;
        mLayoutParams.height = mBuilder.mHeight;
        mLayoutParams.gravity = mBuilder.mGravity;
        mLayoutParams.x = mX = mBuilder.mXOffset;
        mLayoutParams.y = mY = mBuilder.mYOffset;
        mWindowManager.addView(mBuilder.mView, mLayoutParams);
    }

    private void initTouchEvent() {
        switch (mBuilder.mMoveType) {
            case MoveType.inactive:
                break;
            default:
                View layout = (getView() instanceof ViewGroup) ? getView().findViewById(mBuilder.mContentViewId) : getView();
                if (layout == null) {
                    layout = getView();
                }
                layout.setOnTouchListener(new View.OnTouchListener() {

                    float downX, downY, upX, upY, lastX, lastY, offsetX, offsetY;
                    int newX, newY;
                    boolean isClick = false;

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
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
                                updateXY(newX, newY);
                                if (mBuilder.mOnViewStateListener != null) {
                                    mBuilder.mOnViewStateListener.onPositionUpdate(newX, newY);
                                }
                                lastX = event.getRawX();
                                lastY = event.getRawY();
                                break;
                            case MotionEvent.ACTION_UP:
                                upX = event.getRawX();
                                upY = event.getRawY();
                                isClick = (Math.abs(upX - downX) > mScaledTouchSlop) || (Math.abs(upY - downY) > mScaledTouchSlop);
                                switch (mBuilder.mMoveType) {
                                    case MoveType.slide:
                                        int startX = getX();
                                        int endX = (startX * 2 + v.getWidth() > ScreenUtil.getScreenWidth(mBuilder.mContext)) ?
                                                ScreenUtil.getScreenWidth(mBuilder.mContext) - v.getWidth() - mBuilder.mSlideRightMargin :
                                                mBuilder.mSlideLeftMargin;
                                        mAnimator = ObjectAnimator.ofInt(startX, endX);
                                        mAnimator.addUpdateListener(animation -> {
                                            int x = (int) animation.getAnimatedValue();
                                            updateX(x);
                                            if (mBuilder.mOnViewStateListener != null) {
                                                mBuilder.mOnViewStateListener.onPositionUpdate(x, (int) upY);
                                            }
                                        });
                                        startAnimator();
                                        break;
                                    case MoveType.back:
                                        PropertyValuesHolder pvhX = PropertyValuesHolder.ofInt("x", getX(), mBuilder.mXOffset);
                                        PropertyValuesHolder pvhY = PropertyValuesHolder.ofInt("y", getY(), mBuilder.mYOffset);
                                        mAnimator = ObjectAnimator.ofPropertyValuesHolder(pvhX, pvhY);
                                        mAnimator.addUpdateListener(animation -> {
                                            int x = (int) animation.getAnimatedValue("x");
                                            int y = (int) animation.getAnimatedValue("y");
                                            updateXY(x, y);
                                            if (mBuilder.mOnViewStateListener != null) {
                                                mBuilder.mOnViewStateListener.onPositionUpdate(x, y);
                                            }
                                        });
                                        startAnimator();
                                        break;
                                }
                                break;
                        }
                        return isClick;
                    }
                });
        }
    }

    @Override
    public void show() {
        if (!isInit) {
            initPermission();
            isInit = true;
        } else {
            if (isShow) {
                return;
            }
            getView().setVisibility(View.VISIBLE);
        }
        isShow = true;
        if (mBuilder.mOnViewStateListener != null) {
            mBuilder.mOnViewStateListener.onShow();
        }
    }

    @Override
    public void hide() {
        if (!isInit || !isShow) {
            return;
        }
        getView().setVisibility(View.INVISIBLE);
        isShow = false;
        if (mBuilder.mOnViewStateListener != null) {
            mBuilder.mOnViewStateListener.onHide();
        }
    }

    @Override
    public boolean isShowing() {
        return isShow;
    }

    @Override
    public int getX() {
        return mX;
    }

    @Override
    public int getY() {
        return mY;
    }

    @Override
    public void updateX(int x) {
        if (isRemove) {
            return;
        }
        mLayoutParams.x = mBuilder.mXOffset = mX = x;
        mWindowManager.updateViewLayout(mBuilder.mView, mLayoutParams);
    }

    @Override
    public void updateX(int screenType, float ratio) {
        int x = (int) ((screenType == ScreenType.width ?
                ScreenUtil.getScreenWidth(mBuilder.mContext) :
                ScreenUtil.getScreenHeight(mBuilder.mContext)) * ratio);
        updateX(x);
    }

    @Override
    public void updateY(int y) {
        if (isRemove) {
            return;
        }
        mLayoutParams.y = mBuilder.mYOffset = mY = y;
        mWindowManager.updateViewLayout(mBuilder.mView, mLayoutParams);
    }

    @Override
    public void updateY(int screenType, float ratio) {
        int y = (int) ((screenType == ScreenType.width ?
                ScreenUtil.getScreenWidth(mBuilder.mContext) :
                ScreenUtil.getScreenHeight(mBuilder.mContext)) * ratio);
        updateY(y);
    }

    @Override
    public void updateXY(int x, int y) {
        if (isRemove) {
            return;
        }
        mLayoutParams.x = mBuilder.mXOffset = mX = x;
        mLayoutParams.y = mBuilder.mYOffset = mY = y;
        mWindowManager.updateViewLayout(mBuilder.mView, mLayoutParams);
    }

    @Override
    public View getView() {
        return mBuilder.mView;
    }

    @Override
    public void dismiss() {
        isShow = false;
        isRemove = true;
        mWindowManager.removeView(mBuilder.mView);
        if (mBuilder.mOnViewStateListener != null) {
            mBuilder.mOnViewStateListener.onDismiss();
        }
    }

    private void startAnimator() {
        if (mBuilder.mInterpolator == null) {
            if (mDecelerateInterpolator == null) {
                mDecelerateInterpolator = new DecelerateInterpolator();
            }
            mBuilder.mInterpolator = mDecelerateInterpolator;
        }
        mAnimator.setInterpolator(mBuilder.mInterpolator);
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mAnimator.removeAllUpdateListeners();
                mAnimator.removeAllListeners();
                mAnimator = null;
                if (mBuilder.mOnViewStateListener != null) {
                    mBuilder.mOnViewStateListener.onMoveAnimEnd();
                }
            }
        });
        mAnimator.setDuration(mBuilder.mDuration).start();
        if (mBuilder.mOnViewStateListener != null) {
            mBuilder.mOnViewStateListener.onMoveAnimStart();
        }
    }

    private void cancelAnimator() {
        if (mAnimator != null && mAnimator.isRunning()) {
            mAnimator.cancel();
        }
    }

}
