package com.android.util.permission;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuzhb on 2020/2/6
 * Desc:权限管理工具
 */
public class PermissionUtil {

    private static int mRequestCode = -1;

    //权限申请，返回true：申请成功，返回false：申请失败
    public static boolean requestPermissions(Activity activity, int requestCode, String... permissions) {
        mRequestCode = requestCode;
        if (!isPermissionGranted(activity, permissions)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {  //Android 6.0以后权限动态申请
                activity.requestPermissions(permissions, requestCode);
            }
            return false;
        } else {
            return true;
        }
    }

    //申请读写权限，返回true：申请成功，返回false：申请失败
    public static boolean requestReadWritePermissions(Activity activity, int requestCode, int settingCode) {
        String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (!isPermissionGranted(activity, permissions)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {  //Android 6.0以后权限动态申请
                if (isPermissionDeniedForever(activity, permissions)) {
                    activity.runOnUiThread(() -> ToastUtil.showToast("请先允许存储权限"));
                    //打开应用设置页面
                    openSettings(activity, activity.getPackageName(), settingCode);
                } else {
                    activity.requestPermissions(permissions, requestCode);
                }
            }
            return false;
        }
        return true;
    }

    //处理权限申请结果，对应Activity中onRequestPermissionsResult()方法
    public static void onRequestPermissionsResult(Activity activity, int requestCode, String[] permissions,
                                                  int[] grantResults, @NonNull OnPermissionListener listener) {
        if (mRequestCode != -1 && requestCode == mRequestCode) {
            String[] deniedForeverPermissions = getDeniedForeverPermissions(activity, permissions);
            String[] deniedPermissions = getDeniedPermissions(activity, permissions);
            if (deniedForeverPermissions.length > 0) {
                listener.onPermissionDeniedForever(deniedForeverPermissions);
            } else if (deniedPermissions.length > 0) {
                listener.onPermissionDenied(deniedPermissions);
            } else {
                listener.onPermissionGranted();
            }
        }
    }

    //是否有指定的权限
    public static boolean isPermissionGranted(Context context, String... permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    //是否拒绝了权限
    public static boolean isPermissionDenied(Activity activity, String... permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_DENIED) {
                return true;
            }
        }
        return false;
    }

    //是否拒绝了权限且选择了不再提示
    public static boolean isPermissionDeniedForever(Activity activity, String... permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_DENIED) {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                    return true;
                }
            }
        }
        return false;
    }

    //获取请求权限中需要授权的权限
    public static String[] getDeniedPermissions(Context context, String... permissions) {
        List<String> deniedPermissions = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED) {
                deniedPermissions.add(permission);
            }
        }
        String[] result = new String[deniedPermissions.size()];
        return deniedPermissions.toArray(result);
    }

    //获取请求权限中被拒绝且选择不再提示的权限
    public static String[] getDeniedForeverPermissions(Activity activity, String... permissions) {
        List<String> deniedPermissions = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_DENIED) {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                    deniedPermissions.add(permission);
                }
            }
        }
        String[] result = new String[deniedPermissions.size()];
        return deniedPermissions.toArray(result);
    }

    //打开应用设置页面
    public static void openSettings(Activity activity, String packageName, int requestCode) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", packageName, null));
        activity.startActivityForResult(intent, requestCode);
    }

    //权限申请监听
    public interface OnPermissionListener {

        //权限申请成功
        void onPermissionGranted();

        //权限申请被拒绝
        void onPermissionDenied(String[] deniedPermissions);

        //权限申请被拒绝，且选择了不再提示
        void onPermissionDeniedForever(String[] deniedForeverPermissions);

    }

}
