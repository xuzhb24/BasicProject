package com.android.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;

import com.android.base.BaseApplication;
import com.android.util.StatusBar.StatusBarUtil;

/**
 * Created by xuzhb on 2020/3/17
 * Desc:屏幕相关工具类
 */
public class ScreenUtil {

    //获取屏幕宽度，单位像素
    public static int getScreenWidth() {
        return getScreenWidth(BaseApplication.getInstance());
    }

    //获取屏幕宽度，单位像素
    public static int getScreenWidth(Context context) {
        return getDisplayMetrics(context).widthPixels;
    }

    //获取屏幕高度，单位像素
    public static int getScreenHeight() {
        return getScreenHeight(BaseApplication.getInstance());
    }

    //获取屏幕高度，单位像素
    public static int getScreenHeight(Context context) {
        return getDisplayMetrics(context).heightPixels;
    }

    //获取屏幕参数
    public static DisplayMetrics getDisplayMetrics(Context context) {
        WindowManager windowManager = (WindowManager) context.getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        return dm;
    }

    //判断是否是横屏
    public static boolean isLandscape(Context context) {
        return context.getApplicationContext().getResources().getConfiguration()
                .orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    //判断是否是竖屏
    public static boolean isPortrait(Context context) {
        return context.getApplicationContext().getResources().getConfiguration()
                .orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    //在代码中设置屏幕为横屏
    @SuppressLint("SourceLockedOrientationActivity")
    public static void setLandscape(Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    //在代码中设置屏幕为竖屏
    @SuppressLint("SourceLockedOrientationActivity")
    public static void setPortrait(Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    //设置屏幕跟随系统sensor的状态
    public static void setSensor(Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    }

    //判断是否锁屏
    public static boolean isScreenLock(Context context) {
        KeyguardManager km = (KeyguardManager) context.getApplicationContext()
                .getSystemService(Context.KEYGUARD_SERVICE);
        return km.inKeyguardRestrictedInputMode();
    }

    //获取屏幕截图，包含状态栏
    public static Bitmap captureWithStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        Bitmap result = Bitmap.createBitmap(bitmap, 0, 0, dm.widthPixels, dm.heightPixels);
        view.destroyDrawingCache();
        return result;
    }

    //获取屏幕截图，不包含状态栏
    public static Bitmap captureWithoutStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        int statusBarHeight = StatusBarUtil.getStatusBarHeight(activity);
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        Bitmap result = Bitmap.createBitmap(bitmap, 0, statusBarHeight, dm.widthPixels, dm.heightPixels - statusBarHeight);
        view.destroyDrawingCache();
        return result;
    }

    //通过shell命令获取屏幕截图，需要系统权限或root权限
    public static boolean captureByCommand(String fileName) {
        String command = "screencap -p " + fileName;
        ShellUtil.CommandResult result = ShellUtil.execCmd(command, true);
        return result.isSuccess();
    }

    //获取屏幕旋转角度
    public static int getScreenRotation(Activity activity) {
        switch (activity.getWindowManager().getDefaultDisplay().getRotation()) {
            case Surface.ROTATION_270:
                return 270;
            case Surface.ROTATION_180:
                return 180;
            case Surface.ROTATION_90:
                return 90;
            case Surface.ROTATION_0:
            default:
                return 0;
        }
    }
}
