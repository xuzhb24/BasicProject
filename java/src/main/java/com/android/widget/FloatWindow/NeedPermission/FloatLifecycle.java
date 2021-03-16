package com.android.widget.FloatWindow.NeedPermission;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.android.util.LogUtil;

/**
 * Created by xuzhb on 2021/3/8
 * Desc:控制悬浮窗显示周期
 * 使用了两种方法针对返回桌面时隐藏悬浮按钮
 * 1.应用退出
 * 2.监听home键
 */
public class FloatLifecycle extends BroadcastReceiver implements Application.ActivityLifecycleCallbacks {

    private static final String TAG = "FloatLifecycle";
    private static final String SYSTEM_DIALOG_REASON = "reason";
    private static final String SYSTEM_DIALOG_HOME_KEY = "homekey";
    private final boolean isShow;
    private final Class mExitActivity;
    private final Class[] mActivities;
    private final OnLifecycleListener mOnLifecycleListener;

    public FloatLifecycle(Context context, boolean show, Class[] activities, Class exitActivity, OnLifecycleListener onLifecycleListener) {
        this.isShow = show;
        this.mActivities = activities;
        this.mExitActivity = exitActivity;
        this.mOnLifecycleListener = onLifecycleListener;
        ((Application) context.getApplicationContext()).registerActivityLifecycleCallbacks(this);
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);  //监听Home键
        context.getApplicationContext().registerReceiver(this, intentFilter);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        LogUtil.i(TAG, "onActivityCreated  " + activity.getClass().getName());
    }

    @Override
    public void onActivityStarted(Activity activity) {
        LogUtil.i(TAG, "onActivityStarted  " + activity.getClass().getName());
    }

    @Override
    public void onActivityResumed(Activity activity) {
        LogUtil.i(TAG, "onActivityResumed  " + activity.getClass().getName());
        if (needShow(activity)) {
            mOnLifecycleListener.onShow();
        } else {
            mOnLifecycleListener.onHide();
        }
    }

    private boolean needShow(Activity activity) {
        if (mActivities == null) {
            return true;
        }
        for (Class clazz : mActivities) {
            if (clazz.isInstance(activity)) {
                return isShow;
            }
        }
        return !isShow;
    }

    @Override
    public void onActivityPaused(Activity activity) {
        LogUtil.i(TAG, "onActivityPaused  " + activity.getClass().getName());
    }

    @Override
    public void onActivityStopped(Activity activity) {
        LogUtil.i(TAG, "onActivityStopped  " + activity.getClass().getName());
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        LogUtil.i(TAG, "onActivitySaveInstanceState  " + activity.getClass().getName());
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        LogUtil.i(TAG, "onActivityDestroyed  " + activity.getClass().getName());
        //应用退出
        if (mExitActivity != null && activity.getClass().getName().equals(mExitActivity.getName())) {
            mOnLifecycleListener.onBackToDesktop();
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtil.i(TAG, "onReceive");
        String action = intent.getAction();
        //点击Home键
        if (action != null && action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
            String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON);
            if (SYSTEM_DIALOG_HOME_KEY.equals(reason)) {
                mOnLifecycleListener.onBackToDesktop();
            }
        }
    }

    public interface OnLifecycleListener {

        //显示
        void onShow();

        //隐藏
        void onHide();

        //回到桌面
        void onBackToDesktop();

    }

}
