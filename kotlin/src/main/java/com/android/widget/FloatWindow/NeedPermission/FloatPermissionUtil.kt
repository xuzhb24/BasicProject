package com.android.widget.FloatWindow.NeedPermission

import android.app.AppOpsManager
import android.content.Context
import android.graphics.PixelFormat
import android.os.Binder
import android.os.Build
import android.provider.Settings
import android.view.View
import android.view.WindowManager
import androidx.annotation.RequiresApi

/**
 * Created by xuzhb on 2021/3/29
 * Desc:悬浮窗权限
 */
object FloatPermissionUtil {

    /**
     * 判断是否有悬浮窗权限
     */
    fun hasPermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Settings.canDrawOverlays(context)
        } else {
            hasPermissionBelowMarshmallow(context)
        }
    }

    /**
     * 仅用在onActivityResult中判断是否有悬浮窗权限
     */
    fun hasPermissionOnActivityResult(context: Context): Boolean {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O) {
            return hasPermissionForO(context)
        }
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Settings.canDrawOverlays(context)
        } else {
            hasPermissionBelowMarshmallow(context)
        }
    }

    /**
     * 6.0以下判断是否有权限
     * 理论上6.0以上才需处理权限，但有的国内rom在6.0以下就添加了权限
     * 其实此方式也可以用于判断6.0以上版本，只不过有更简单的canDrawOverlays代替
     */
    private fun hasPermissionBelowMarshmallow(context: Context): Boolean {
        return try {
            val manager = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
            val dispatchMethod = AppOpsManager::class.java.getMethod(
                "checkOp", Int::class.javaPrimitiveType,
                Int::class.javaPrimitiveType, String::class.java
            )
            AppOpsManager.MODE_ALLOWED == dispatchMethod.invoke(
                manager, 24, Binder.getCallingUid(),
                context.applicationContext.packageName
            ) as Int
        } catch (e: Exception) {
            false
        }
    }

    /**
     * 用于判断8.0时是否有权限，仅用于OnActivityResult
     * 针对8.0官方bug:在用户授予权限后Settings.canDrawOverlays或checkOp方法判断仍然返回false
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun hasPermissionForO(context: Context): Boolean {
        try {
            val manager = context.getSystemService(Context.WINDOW_SERVICE) as? WindowManager ?: return false
            val viewToAdd = View(context)
            val params = WindowManager.LayoutParams(
                0, 0,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                else WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSPARENT
            )
            viewToAdd.layoutParams = params
            manager.addView(viewToAdd, params)
            manager.removeView(viewToAdd)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

}