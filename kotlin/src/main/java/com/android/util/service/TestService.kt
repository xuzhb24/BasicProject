package com.android.util.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.android.util.LogUtil
import com.android.util.ToastUtil

/**
 * Created by xuzhb on 2020/12/14
 * Desc:
 */
class TestService : Service() {

    companion object {
        private const val TAG = "TestService"
    }

    override fun onCreate() {
        super.onCreate()
        LogUtil.w(TAG, "onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        LogUtil.w(TAG, "onStartCommand")
        ToastUtil.showToast("TestService已开启")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        LogUtil.w(TAG, "onDestroy")
        ToastUtil.showToast("TestService已销毁")
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = mBinder

    private val mBinder: IBinder = LocalBinder()

    inner class LocalBinder : Binder() {
        fun getService() = this@TestService
    }

    fun getBindState() = "TestService已绑定"

}