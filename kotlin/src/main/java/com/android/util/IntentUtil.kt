package com.android.util

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import androidx.core.content.FileProvider
import java.io.File

/**
 * Created by xuzhb on 2020/11/18
 * Desc:Intent相关工具类
 */
object IntentUtil {

    //获取安装App的意图
    fun getInstallAppIntent(context: Context, filePath: String, authority: String): Intent? {
        if (TextUtils.isEmpty(filePath)) {
            return null
        }
        return getInstallAppIntent(context, File(filePath), authority)
    }

    /**
     * 获取安装App的意图，Android 8.0后需要权限<uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />，否则无法安装成功
     *
     * @param file      文件
     * @param authority 7.0及以上安装需要传入AndroidManifest中<provider>标签的authorities属性
     */
    fun getInstallAppIntent(context: Context, file: File?, authority: String): Intent? {
        if (file == null) {
            return null
        }
        val intent = Intent(Intent.ACTION_VIEW)
        val type = "application/vnd.android.package-archive"
        val data = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            Uri.fromFile(file)
        } else {
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            FileProvider.getUriForFile(context.applicationContext, authority, file)
        }
        intent.setDataAndType(data, type)
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    //获取卸载App的意图
    fun getUninstallAppIntent(packageName: String): Intent {
        val intent = Intent(Intent.ACTION_DELETE)
        intent.data = Uri.parse("package:$packageName")
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    //获取打开App的意图
    fun getLaunchAppIntent(context: Context, packageName: String): Intent? {
        return context.applicationContext.packageManager.getLaunchIntentForPackage(packageName)
    }

    //获取APP设置的意图
    fun getAppSettingsIntent(packageName: String): Intent? {
        val intent = Intent("android.settings.APPLICATION_DETAILS_SETTINGS")
        intent.data = Uri.parse("package:$packageName")
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    //获取分享文本的意图
    fun getShareTextIntent(content: String): Intent? {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, content)
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    //获取分享图片的意图
    fun getShareImageIntent(context: Context, content: String, filePath: String, authority: String): Intent? {
        if (TextUtils.isEmpty(filePath)) {
            return null
        }
        return getShareImageIntent(context, content, File(filePath), authority)
    }

    /**
     * 获取分享图片的意图
     *
     * @param file      文件
     * @param authority 7.0及以上安装需要传入AndroidManifest中<provider>标签的authorities属性
     */
    fun getShareImageIntent(context: Context, content: String, file: File?, authority: String): Intent? {
        if (file == null || !file.exists()) {
            return null
        }
        val imgUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)  //Android 7.0后通过FileProvider共享文件，如系统照片
            FileProvider.getUriForFile(context.applicationContext, authority, file) else Uri.fromFile(file)
        return getShareImageIntent(content, imgUri)
    }

    /**
     * 获取分享图片的意图
     *
     * @param content 分享文本
     * @param imgUri  图片uri
     * @return intent
     */
    fun getShareImageIntent(content: String, imgUri: Uri): Intent? {
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_TEXT, content)
        intent.putExtra(Intent.EXTRA_STREAM, imgUri)
        intent.type = "image/*"
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    //获取打开其他应用组件的意图
    fun getComponentIntent(packageName: String, className: String): Intent? {
        return getComponentIntent(packageName, className, null)
    }

    /**
     * 获取打开其他应用组件的意图
     *
     * @param packageName 包名
     * @param className   类名
     * @param extras      extras
     */
    fun getComponentIntent(packageName: String, className: String, extras: Bundle?): Intent? {
        val intent = Intent(Intent.ACTION_VIEW)
        extras?.let {
            intent.putExtras(extras)
        }
        intent.component = ComponentName(packageName, className)
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    //获取关机的意图，需要权限<uses-permission android:name="android.permission.SHUTDOWN"/>
    fun getShutdownIntent(): Intent? {
        val intent = Intent(Intent.ACTION_SHUTDOWN)
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    //获取跳转至拨号界面意图
    fun getDialIntent(phoneNumber: String): Intent? {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    //获取拨打电话意图，需要权限<uses-permission android:name="android.permission.CALL_PHONE"/>
    fun getCallIntent(phoneNumber: String): Intent? {
        val intent = Intent("android.intent.action.CALL", Uri.parse("tel:$phoneNumber"))
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    //获取跳转至发送短信界面的意图
    fun getSendSmsIntent(phoneNumber: String, content: String): Intent? {
        val uri = Uri.parse("smsto:$phoneNumber")
        val intent = Intent(Intent.ACTION_SENDTO, uri)
        intent.putExtra("sms_body", content)
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    //获取拍照的意图
    fun getCameraIntent(context: Context, filePath: String, authority: String): Intent? {
        if (TextUtils.isEmpty(filePath)) {
            return null
        }
        return getCameraIntent(context, File(filePath), authority)
    }

    /**
     * 获取拍照的意图
     *
     * @param file      文件
     * @param authority 7.0及以上安装需要传入AndroidManifest中<provider>标签的authorities属性
     */
    fun getCameraIntent(context: Context, file: File?, authority: String): Intent? {
        if (file == null) {
            return null
        }
        val outputUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) //Android 7.0后通过FileProvider共享文件，如系统照片
            FileProvider.getUriForFile(context.applicationContext, authority, file) else Uri.fromFile(file)
        return getCameraIntent(outputUri)
    }

    //获取拍照的意图，outputUri：输出的uri
    fun getCameraIntent(outputUri: Uri): Intent? {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri)
        return intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK)
    }

}