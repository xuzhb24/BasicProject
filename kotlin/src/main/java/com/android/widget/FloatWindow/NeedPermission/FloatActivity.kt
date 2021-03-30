package com.android.widget.FloatWindow.NeedPermission

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.annotation.RequiresApi

/**
 * Created by xuzhb on 2021/3/29
 * Desc:申请悬浮窗权限
 */
class FloatActivity : Activity() {

    companion object {
        private const val REQUEST_CODE = 756232212
        private var mOnPermissionListenerList: MutableList<OnPermissionListener>? = null
        private var mOnPermissionListener: OnPermissionListener? = null

        @Synchronized
        fun request(context: Context, listener: OnPermissionListener) {
            if (FloatPermissionUtil.hasPermission(context)) {
                listener.onSuccess()
                return
            }
            if (mOnPermissionListenerList.isNullOrEmpty()) {
                mOnPermissionListenerList = mutableListOf()
                mOnPermissionListener = object : OnPermissionListener {
                    override fun onSuccess() {
                        mOnPermissionListenerList?.let {
                            it.forEach {
                                it.onSuccess()
                            }
                            it.clear()
                        }
                    }

                    override fun onFailure() {
                        mOnPermissionListenerList?.let {
                            it.forEach {
                                it.onFailure()
                            }
                            it.clear()
                        }
                    }
                }
                val intent = Intent(context, FloatActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
            }
            mOnPermissionListenerList?.add(listener)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestAlertWindowPermission()
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun requestAlertWindowPermission() {
        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
        intent.data = Uri.parse("package:${packageName}")
        startActivityForResult(intent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            if (FloatPermissionUtil.hasPermissionOnActivityResult(this)) {
                mOnPermissionListener?.onSuccess()
            } else {
                mOnPermissionListener?.onFailure()
            }
        }
        finish()
    }

}