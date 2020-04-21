package com.android.util.service;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.text.TextUtils;

import java.util.List;

/**
 * Created by xuzhb on 2020/4/19
 * Desc:服务相关工具类
 */
public class ServiceUtil {

    //获取所有运行的服务
    public static List<ActivityManager.RunningServiceInfo> getAllRunningServiceInfo(Context context) {
        ActivityManager manager = (ActivityManager) context.getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);
        if (manager != null) {
            return manager.getRunningServices(Integer.MAX_VALUE);
        }
        return null;
    }

    //判断服务是否运行
    public static boolean isServiceRunning(Context context, Class<?> clazz) {
        return isServiceRunning(context, clazz.getName());
    }

    //判断服务是否运行
    public static boolean isServiceRunning(Context context, String className) {
        List<ActivityManager.RunningServiceInfo> infos = getAllRunningServiceInfo(context);
        if (infos != null) {
            for (ActivityManager.RunningServiceInfo info : infos) {
                if (TextUtils.equals(info.service.getClassName(), className)) {
                    return true;
                }
            }
        }
        return false;
    }

    //启动服务
    public static void startService(Context context, String className) {
        try {
            startService(context, Class.forName(className));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //启动服务
    public static void startService(Context context, Class<?> clazz) {
        Intent intent = new Intent(context, clazz);
        context.startService(intent);
    }

    //停止服务
    public static boolean stopService(Context context, String className) {
        try {
            return stopService(context, Class.forName(className));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //停止服务
    public static boolean stopService(Context context, Class<?> clazz) {
        Intent intent = new Intent(context, clazz);
        return context.stopService(intent);
    }

    //绑定服务
    public static void bindService(Context context, Class<?> clazz, ServiceConnection conn) {
        bindService(context, clazz, conn, Context.BIND_AUTO_CREATE);
    }

    //绑定服务
    public static void bindService(Context context, String className, ServiceConnection conn, int flags) {
        try {
            bindService(context, Class.forName(className), conn, flags);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //绑定服务
    public static void bindService(Context context, Class<?> clazz, ServiceConnection conn, int flags) {
        Intent intent = new Intent(context, clazz);
        context.bindService(intent, conn, flags);
    }

    //解绑服务
    public static void unbindService(Context context, ServiceConnection conn) {
        context.unbindService(conn);
    }

}
