package com.android.widget.FloatWindow.NeedPermission.compat

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import com.android.util.LogUtil
import com.android.widget.FloatWindow.NeedPermission.FloatPermissionUtil
import com.android.widget.FloatWindow.NeedPermission.OnPermissionListener
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

/**
 * Created by xuzhb on 2021/3/29
 * Desc:兼容小米版本的权限申请
 */
object MIUI {

    private const val TAG = "MIUI"
    private const val MIUI = "ro.miui.ui.version.name"
    private const val MIUI5 = "V5"
    private const val MIUI6 = "V6"
    private const val MIUI7 = "V7"
    private const val MIUI8 = "V8"
    private const val MIUI9 = "V9"

    private var mOnPermissionListenerList: MutableList<OnPermissionListener>? = null
    private var mOnPermissionListener: OnPermissionListener? = null

    fun isMIUI(): Boolean {
        LogUtil.i(TAG, "MIUI:${getProp()}")
        return Build.MANUFACTURER == "Xiaomi"
    }

    fun getProp(): String? {
        var input: BufferedReader? = null
        try {
            val p = Runtime.getRuntime().exec("getprop $MIUI")
            input = BufferedReader(InputStreamReader(p.inputStream), 1024)
            val line = input.readLine()
            input.close()
            return line
        } catch (e: IOException) {
            return null
        } finally {
            if (input != null) {
                try {
                    input.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    //Android6.0以下申请权限
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
            request(context)
        }
        mOnPermissionListenerList?.add(listener)
    }

    private fun request(context: Context) {
        when (getProp()) {
            MIUI5 -> requestForMIUI5(context)
            MIUI6, MIUI7 -> requestForMIUI67(context)
            MIUI8, MIUI9 -> requestForMIUI89(context)
        }
    }

    private fun requestForMIUI5(context: Context) {
        val packageName = context.packageName
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        if (isIntentAvailable(intent, context)) {
            context.startActivity(intent)
        } else {
            LogUtil.e(TAG, "intent is not available!")
        }
    }

    private fun requestForMIUI67(context: Context) {
        val intent = Intent("miui.intent.action.APP_PERM_EDITOR").apply {
            setClassName(
                "com.miui.securitycenter",
                "com.miui.permcenter.permissions.AppPermissionsEditorActivity"
            )
            putExtra("extra_pkgname", context.packageName)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        if (isIntentAvailable(intent, context)) {
            context.startActivity(intent)
        } else {
            LogUtil.e(TAG, "intent is not available!")
        }
    }

    private fun requestForMIUI89(context: Context) {
        var intent = Intent("miui.intent.action.APP_PERM_EDITOR").apply {
            setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity")
            putExtra("extra_pkgname", context.packageName)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        if (isIntentAvailable(intent, context)) {
            context.startActivity(intent)
        } else {
            intent = Intent("miui.intent.action.APP_PERM_EDITOR").apply {
                setPackage("com.miui.securitycenter")
                putExtra("extra_pkgname", context.packageName)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            if (isIntentAvailable(intent, context)) {
                context.startActivity(intent)
            } else {
                LogUtil.e(TAG, "intent is not available!")
            }
        }
    }

    private fun isIntentAvailable(intent: Intent?, context: Context): Boolean {
        return intent != null && context.packageManager.queryIntentActivities(
            intent, PackageManager.MATCH_DEFAULT_ONLY
        ).size > 0
    }

}