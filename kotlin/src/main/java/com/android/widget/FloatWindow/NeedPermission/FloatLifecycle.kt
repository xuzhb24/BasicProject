package com.android.widget.FloatWindow.NeedPermission

import android.app.Activity
import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import com.android.util.LogUtil

/**
 * Created by xuzhb on 2021/3/29
 * Desc:控制悬浮窗显示周期
 * 使用了两种方法针对返回桌面时隐藏悬浮按钮
 * 1.应用退出
 * 2.监听home键
 */
class FloatLifecycle constructor(
    private val mContext: Context,
    private val isShow: Boolean,
    private val mActivities: Array<out Class<*>>?,
    private val mExitActivity: Class<*>?,
    private val mOnLifecycleListener: OnLifecycleListener
) : BroadcastReceiver(), Application.ActivityLifecycleCallbacks {

    companion object {
        private const val TAG = "FloatLifecycle"
        private const val SYSTEM_DIALOG_REASON = "reason"
        private const val SYSTEM_DIALOG_HOME_KEY = "homekey"
    }

    init {
        (mContext.applicationContext as Application).registerActivityLifecycleCallbacks(this)
        val intentFilter = IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
        mContext.applicationContext.registerReceiver(this, intentFilter)
    }

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
        LogUtil.i(TAG, "onActivityCreated  ${activity?.javaClass?.name}")
    }

    override fun onActivityStarted(activity: Activity?) {
        LogUtil.i(TAG, "onActivityStarted  ${activity?.javaClass?.name}")
    }

    override fun onActivityResumed(activity: Activity?) {
        LogUtil.i(TAG, "onActivityResumed  ${activity?.javaClass?.name}")
        if (needShow(activity)) {
            mOnLifecycleListener.onShow()
        } else {
            mOnLifecycleListener.onHide()
        }
    }

    private fun needShow(activity: Activity?): Boolean {
        if (mActivities.isNullOrEmpty()) {
            return true
        }
        mActivities.forEach {
            if (it.isInstance(activity)) {
                return isShow
            }
        }
        return !isShow
    }

    override fun onActivityPaused(activity: Activity?) {
        LogUtil.i(TAG, "onActivityPaused  ${activity?.javaClass?.name}")
    }

    override fun onActivityStopped(activity: Activity?) {
        LogUtil.i(TAG, "onActivityStopped  ${activity?.javaClass?.name}")
    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
        LogUtil.i(TAG, "onActivitySaveInstanceState  ${activity?.javaClass?.name}")
    }

    override fun onActivityDestroyed(activity: Activity?) {
        LogUtil.i(TAG, "onActivityDestroyed  ${activity?.javaClass?.name}")
        //应用退出
        activity?.let {
            if (it.javaClass.name == mExitActivity?.name) {
                mOnLifecycleListener.onBackToDesktop()
            }
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        LogUtil.i(TAG, "onReceive")
        val action = intent?.action
        //点击Home键
        if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS == action) {
            val reason = intent.getStringExtra(SYSTEM_DIALOG_REASON)
            if (SYSTEM_DIALOG_HOME_KEY == reason) {
                mOnLifecycleListener.onBackToDesktop()
            }
        }
    }

    interface OnLifecycleListener {

        //显示
        fun onShow()

        //隐藏
        fun onHide()

        //回到桌面
        fun onBackToDesktop()

    }

}