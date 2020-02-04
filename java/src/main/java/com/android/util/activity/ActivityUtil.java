package com.android.util.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;

import java.util.List;

/**
 * Created by xuzhb on 2020/2/3
 * Desc:Activity相关工具类
 */
public class ActivityUtil {

    //判断是否存在Activity
    public static boolean isActivityExists(Context context, String packageName, String className) {
        Intent intent = new Intent();
        intent.setClassName(packageName, className);
        return !(context.getApplicationContext().getPackageManager().resolveActivity(intent, 0) == null ||
                intent.resolveActivity(context.getApplicationContext().getPackageManager()) == null ||
                context.getApplicationContext().getPackageManager().queryIntentActivities(intent, 0).size() == 0);
    }

    public static void startActivity(Context context, Class<?> clazz) {
        startActivity(context, context.getPackageName(), clazz.getName(), null);
    }

    public static void startActivity(Context context, Class<?> clazz, Bundle extras) {
        startActivity(context, context.getPackageName(), clazz.getName(), extras);
    }

    public static void startActivity(Activity activity, Class<?> clazz, int enterAnim, int exitAnim) {
        startActivity(activity, activity.getPackageName(), clazz.getName(), null);
        activity.overridePendingTransition(enterAnim, exitAnim);
    }

    /**
     * 启动Activity
     *
     * @param activity  activity
     * @param clazz     activity类
     * @param extras    extras
     * @param enterAnim 入场动画
     * @param exitAnim  出场动画
     */
    public static void startActivity(Activity activity, Class<?> clazz, int enterAnim, int exitAnim, Bundle extras) {
        startActivity(activity, activity.getPackageName(), clazz.getName(), extras);
        activity.overridePendingTransition(enterAnim, exitAnim);
    }

    public static void startActivity(Context context, String packageName, String className) {
        startActivity(context, packageName, className, null);
    }

    /**
     * 启动Activity
     *
     * @param packageName 包名
     * @param className   类名
     * @param extras      extras
     */
    public static void startActivity(Context context, String packageName, String className, Bundle extras) {
        Intent intent = new Intent();
        if (extras != null) {
            intent.putExtras(extras);
        }
        intent.setComponent(new ComponentName(packageName, className));
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    //获取指定应用启动Activity的类名，packageName：指定应用的包名
    public static String getLauncherActivityName(Context context, String packageName) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> infoList = context.getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo info : infoList) {
            if (info.activityInfo.packageName.equals(packageName)) {
                return info.activityInfo.name;
            }
        }
        return null;
    }

    //获取栈顶Activity的包名和类名，以空格分隔
    public static String getTopActivityName(Context context) {
        ActivityManager manager = (ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.RunningTaskInfo info = manager.getRunningTasks(1).get(0);
        String packageName = info.topActivity.getPackageName();
        String className = info.topActivity.getClassName();
        return packageName + " " + className;
    }

}
