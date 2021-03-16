package com.android.widget.FloatWindow.NeedPermission;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;

import com.android.util.LogUtil;
import com.android.widget.FloatWindow.NeedPermission.compat.MIUI;

/**
 * Created by xuzhb on 2021/3/9
 * Desc:悬浮窗控件
 */
public class FloatLayout {

    private int mX, mY;
    private boolean isRemove = false;
    private View mView;
    private final FloatWindow.Builder mBuilder;
    private final WindowManager mWindowManager;
    private final WindowManager.LayoutParams mLayoutParams;
    private final OnPermissionListener mPermissionListener;

    public FloatLayout(FloatWindow.Builder builder) {
        mBuilder = builder;
        mPermissionListener = mBuilder.mOnPermissionListener;
        mWindowManager = (WindowManager) mBuilder.mContext.getSystemService(Context.WINDOW_SERVICE);
        mLayoutParams = new WindowManager.LayoutParams();
        mLayoutParams.format = PixelFormat.RGBA_8888;
        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
        mLayoutParams.windowAnimations = 0;
    }

    public void init() {
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
                mWindowManager.addView(mView, mLayoutParams);
            } catch (Exception e) {
                mWindowManager.removeView(mView);
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
                if (mPermissionListener != null) {
                    mPermissionListener.onSuccess();
                }
            }

            @Override
            public void onFailure() {
                if (mPermissionListener != null) {
                    mPermissionListener.onFailure();
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
                if (mPermissionListener != null) {
                    mPermissionListener.onSuccess();
                }
            }

            @Override
            public void onFailure() {
                if (mPermissionListener != null) {
                    mPermissionListener.onFailure();
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
        mView = mBuilder.mView;
        mWindowManager.addView(mView, mLayoutParams);
    }

    public void dismiss() {
        isRemove = true;
        mWindowManager.removeView(mView);
    }

    public void updateXY(int x, int y) {
        if (isRemove) {
            return;
        }
        mLayoutParams.x = mX = x;
        mLayoutParams.y = mY = y;
        mWindowManager.updateViewLayout(mView, mLayoutParams);
    }

    public void updateX(int x) {
        if (isRemove) {
            return;
        }
        mLayoutParams.x = mX = x;
        mWindowManager.updateViewLayout(mView, mLayoutParams);
    }

    public void updateY(int y) {
        if (isRemove) {
            return;
        }
        mLayoutParams.y = mY = y;
        mWindowManager.updateViewLayout(mView, mLayoutParams);
    }

    public int getX() {
        return mX;
    }

    public int getY() {
        return mY;
    }

}
