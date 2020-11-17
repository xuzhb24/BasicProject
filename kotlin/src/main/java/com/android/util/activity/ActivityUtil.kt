package com.android.util.activity

import android.app.Activity
import android.app.ActivityManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle

/**
 * Created by xuzhb on 2020/11/17
 * Desc:Activity相关工具类
 */
object ActivityUtil {

    //判断是否存在Activity
    fun isActivityExists(context: Context, packageName: String, className: String): Boolean {
        val intent = Intent()
        intent.setClassName(packageName, className)
        return !(context.applicationContext.packageManager.resolveActivity(intent, 0) == null ||
                intent.resolveActivity(context.applicationContext.packageManager) == null ||
                context.applicationContext.packageManager.queryIntentActivities(intent, 0).size == 0)
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
    fun startActivity(activity: Activity, clazz: Class<*>, extras: Bundle? = null, enterAnim: Int = -1, exitAnim: Int = -1) {
        startActivity(activity, activity.packageName, clazz.name, extras)
        if (enterAnim != -1 && exitAnim != -1) {
            activity.overridePendingTransition(enterAnim, exitAnim)
        }
    }

    fun startActivity(context: Context, clazz: Class<*>, extras: Bundle? = null) {
        startActivity(context, context.packageName, clazz.name, extras)
    }

    /**
     * 启动Activity
     *
     * @param packageName 包名
     * @param className   类名
     * @param extras      extras
     */
    fun startActivity(context: Context, packageName: String, className: String, extras: Bundle? = null) {
        val intent = Intent()
        if (extras != null) {
            intent.putExtras(extras)
        }
        intent.component = ComponentName(packageName, className)
        if (context !is Activity) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    //获取指定应用启动Activity的类名，packageName：指定应用的包名
    fun getLauncherActivityName(context: Context, packageName: String): String? {
        val intent = Intent()
        intent.action = Intent.ACTION_MAIN
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        val infoList = context.packageManager.queryIntentActivities(intent, 0)
        for (info in infoList) {
            if (info.activityInfo.packageName == packageName) {
                return info.activityInfo.name
            }
        }
        return null
    }

    //获取栈顶Activity的包名和类名，以空格分隔
    fun getTopActivityName(context: Context): String {
        val manager = context.applicationContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val info = manager.getRunningTasks(1)[0]
        val packageName = info.topActivity.packageName
        val className = info.topActivity.className
        return "$packageName $className"
    }

}