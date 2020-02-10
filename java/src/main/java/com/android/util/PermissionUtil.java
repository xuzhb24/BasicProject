package com.android.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;

/**
 * Created by xuzhb on 2020/2/6
 * Desc:权限管理工具
 */
public class PermissionUtil {

    //权限申请，返回true：申请成功，返回false：申请失败
    public static boolean requestPermissions(Activity activity, int requestCode, String... permissions) {
        if (!isAllPermissionGranted(activity, permissions)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {  //Android 6.0以后权限动态申请
                activity.requestPermissions(permissions, requestCode);
            }
            return false;
        } else {
            return true;
        }
    }

    //是否有指定的权限
    public static boolean isPermissionGranted(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    //是否有指定的所有权限
    public static boolean isAllPermissionGranted(Context context, String... permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

}
