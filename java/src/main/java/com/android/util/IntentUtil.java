package com.android.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;

import androidx.core.content.FileProvider;

import java.io.File;

/**
 * Created by xuzhb on 2020/2/4
 * Desc:Intent相关工具类
 */
public class IntentUtil {

    //获取安装App的意图
    public static Intent getInstallAppIntent(Context context, String filePath, String authority) {
        if (TextUtils.isEmpty(filePath)) {
            return null;
        }
        return getInstallAppIntent(context, new File(filePath), authority);
    }

    /**
     * 获取安装App的意图，Android 8.0后需要权限<uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />，否则无法安装成功
     *
     * @param file      文件
     * @param authority 7.0及以上安装需要传入AndroidManifest中<provider>标签的authorities属性
     */
    public static Intent getInstallAppIntent(Context context, File file, String authority) {
        if (file == null) {
            return null;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data;
        String type = "application/vnd.android.package-archive";
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            data = Uri.fromFile(file);
        } else {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            data = FileProvider.getUriForFile(context.getApplicationContext(), authority, file);
        }
        intent.setDataAndType(data, type);
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    //获取卸载App的意图
    public static Intent getUninstallAppIntent(String packageName) {
        Intent intent = new Intent(Intent.ACTION_DELETE);
        intent.setData(Uri.parse("package:" + packageName));
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    //获取打开App的意图
    public static Intent getLaunchAppIntent(Context context, String packageName) {
        return context.getApplicationContext().getPackageManager().getLaunchIntentForPackage(packageName);
    }

    //获取APP设置的意图
    public static Intent getAppSettingsIntent(String packageName) {
        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.parse("package:" + packageName));
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    //获取分享文本的意图
    public static Intent getShareTextIntent(String content) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, content);
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    //获取分享图片的意图
    public static Intent getShareImageIntent(Context context, String content, String filePath, String authority) {
        if (TextUtils.isEmpty(filePath)) {
            return null;
        }
        return getShareImageIntent(context, content, new File(filePath), authority);
    }

    /**
     * 获取分享图片的意图
     *
     * @param file      文件
     * @param authority 7.0及以上安装需要传入AndroidManifest中<provider>标签的authorities属性
     */
    public static Intent getShareImageIntent(Context context, String content, File file, String authority) {
        if (file == null || !file.exists()) {
            return null;
        }
        Uri imgUri = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N   //Android 7.0后通过FileProvider共享文件，如系统照片
                ? FileProvider.getUriForFile(context.getApplicationContext(), authority, file)
                : Uri.fromFile(file);
        return getShareImageIntent(content, imgUri);
    }

    /**
     * 获取分享图片的意图
     *
     * @param content 分享文本
     * @param imgUri  图片uri
     * @return intent
     */
    public static Intent getShareImageIntent(String content, Uri imgUri) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, content);
        intent.putExtra(Intent.EXTRA_STREAM, imgUri);
        intent.setType("image/*");
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    //获取打开其他应用组件的意图
    public static Intent getComponentIntent(String packageName, String className) {
        return getComponentIntent(packageName, className, null);
    }

    /**
     * 获取打开其他应用组件的意图
     *
     * @param packageName 包名
     * @param className   类名
     * @param extras      extras
     */
    public static Intent getComponentIntent(String packageName, String className, Bundle extras) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (extras != null) {
            intent.putExtras(extras);
        }
        intent.setComponent(new ComponentName(packageName, className));
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    //获取关机的意图，需要权限<uses-permission android:name="android.permission.SHUTDOWN"/>
    public static Intent getShutdownIntent() {
        Intent intent = new Intent(Intent.ACTION_SHUTDOWN);
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    //获取跳转至拨号界面意图
    public static Intent getDialIntent(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    //获取拨打电话意图，需要权限<uses-permission android:name="android.permission.CALL_PHONE"/>
    public static Intent getCallIntent(String phoneNumber) {
        Intent intent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + phoneNumber));
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    //获取跳转至发送短信界面的意图
    public static Intent getSendSmsIntent(String phoneNumber, String content) {
        Uri uri = Uri.parse("smsto:" + phoneNumber);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra("sms_body", content);
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    //获取拍照的意图
    public static Intent getCameraIntent(Context context, String filePath, String authority) {
        if (TextUtils.isEmpty(filePath)) {
            return null;
        }
        return getCameraIntent(context, new File(filePath), authority);
    }

    /**
     * 获取拍照的意图
     *
     * @param file      文件
     * @param authority 7.0及以上安装需要传入AndroidManifest中<provider>标签的authorities属性
     */
    public static Intent getCameraIntent(Context context, File file, String authority) {
        if (file == null) {
            return null;
        }
        Uri outputUri = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N   //Android 7.0后通过FileProvider共享文件，如系统照片
                ? FileProvider.getUriForFile(context.getApplicationContext(), authority, file)
                : Uri.fromFile(file);
        return getCameraIntent(outputUri);
    }

    //获取拍照的意图，outputUri：输出的uri
    public static Intent getCameraIntent(Uri outputUri) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        return intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_NEW_TASK);
    }

}
