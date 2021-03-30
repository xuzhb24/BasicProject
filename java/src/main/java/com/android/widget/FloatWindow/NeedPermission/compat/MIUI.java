package com.android.widget.FloatWindow.NeedPermission.compat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import com.android.util.LogUtil;
import com.android.widget.FloatWindow.NeedPermission.FloatPermissionUtil;
import com.android.widget.FloatWindow.NeedPermission.OnPermissionListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuzhb on 2021/3/8
 * Desc:兼容小米版本的权限申请
 */
public class MIUI {

    private static final String TAG = "MIUI";
    private static final String MIUI = "ro.miui.ui.version.name";
    private static final String MIUI5 = "V5";
    private static final String MIUI6 = "V6";
    private static final String MIUI7 = "V7";
    private static final String MIUI8 = "V8";
    private static final String MIUI9 = "V9";
    private static List<OnPermissionListener> mOnPermissionListenerList;
    private static OnPermissionListener mOnPermissionListener;

    public static boolean isMIUI() {
        LogUtil.i(TAG, "MIUI：" + getProp());
        return Build.MANUFACTURER.equals("Xiaomi");
    }

    public static String getProp() {
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + MIUI);
            input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
            String line = input.readLine();
            input.close();
            return line;
        } catch (IOException ex) {
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //Android6.0以下申请权限
    public static void request(Context context, OnPermissionListener listener) {
        if (FloatPermissionUtil.hasPermission(context)) {
            listener.onSuccess();
            return;
        }
        if (mOnPermissionListenerList == null || mOnPermissionListenerList.isEmpty()) {
            mOnPermissionListenerList = new ArrayList<>();
            mOnPermissionListener = new OnPermissionListener() {
                @Override
                public void onSuccess() {
                    for (OnPermissionListener listener : mOnPermissionListenerList) {
                        listener.onSuccess();
                    }
                    mOnPermissionListenerList.clear();
                }

                @Override
                public void onFailure() {
                    for (OnPermissionListener listener : mOnPermissionListenerList) {
                        listener.onFailure();
                    }
                    mOnPermissionListenerList.clear();
                }
            };
            request(context);
        }
        mOnPermissionListenerList.add(listener);
    }

    private static void request(final Context context) {
        switch (getProp()) {
            case MIUI5:
                requestForMIUI5(context);
                break;
            case MIUI6:
            case MIUI7:
                requestForMIUI67(context);
                break;
            case MIUI8:
            case MIUI9:
                requestForMIUI89(context);
                break;
        }
    }

    private static void requestForMIUI5(Context context) {
        String packageName = context.getPackageName();
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", packageName, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (isIntentAvailable(intent, context)) {
            context.startActivity(intent);
        } else {
            LogUtil.e(TAG, "intent is not available!");
        }
    }

    private static void requestForMIUI67(Context context) {
        Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
        intent.setClassName("com.miui.securitycenter",
                "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
        intent.putExtra("extra_pkgname", context.getPackageName());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (isIntentAvailable(intent, context)) {
            context.startActivity(intent);
        } else {
            LogUtil.e(TAG, "intent is not available!");
        }
    }

    private static void requestForMIUI89(Context context) {
        Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
        intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity");
        intent.putExtra("extra_pkgname", context.getPackageName());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (isIntentAvailable(intent, context)) {
            context.startActivity(intent);
        } else {
            intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
            intent.setPackage("com.miui.securitycenter");
            intent.putExtra("extra_pkgname", context.getPackageName());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (isIntentAvailable(intent, context)) {
                context.startActivity(intent);
            } else {
                LogUtil.e(TAG, "intent is not available!");
            }
        }
    }

    private static boolean isIntentAvailable(Intent intent, Context context) {
        return intent != null && context.getPackageManager().queryIntentActivities(
                intent, PackageManager.MATCH_DEFAULT_ONLY).size() > 0;
    }

}
