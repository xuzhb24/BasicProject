package com.android.util.service

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection

/**
 * Created by xuzhb on 2020/12/11
 * Desc:服务相关工具类
 */
object ServiceUtil {

    //获取所有运行的服务
    fun getAllRunningServiceInfo(context: Context): MutableList<ActivityManager.RunningServiceInfo>? {
        val manager = context.applicationContext.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
        return manager?.getRunningServices(Int.MAX_VALUE)
    }

    //判断服务是否运行
    fun isServiceRunning(context: Context, clazz: Class<*>) =
        isServiceRunning(context, clazz.name)

    //判断服务是否运行
    fun isServiceRunning(context: Context, className: String): Boolean {
        getAllRunningServiceInfo(context)?.forEach {
            if (it.service.className == className) {
                return true
            }
        }
        return false
    }

    //启动服务
    fun startService(context: Context, className: String) {
        try {
            startService(context, Class.forName(className))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //启动服务
    fun startService(context: Context, clazz: Class<*>) {
        val intent = Intent(context, clazz)
        context.startService(intent)
    }

    //停止服务
    fun stopService(context: Context, className: String): Boolean {
        try {
            return stopService(context, Class.forName(className))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    //停止服务
    fun stopService(context: Context, clazz: Class<*>): Boolean {
        val intent = Intent(context, clazz)
        return context.stopService(intent)
    }

    //绑定服务
    fun bindService(context: Context, className: String, conn: ServiceConnection, flags: Int = Context.BIND_AUTO_CREATE) {
        try {
            bindService(context, Class.forName(className), conn, flags)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //绑定服务
    fun bindService(context: Context, clazz: Class<*>, conn: ServiceConnection, flags: Int = Context.BIND_AUTO_CREATE) {
        val intent = Intent(context, clazz)
        context.bindService(intent, conn, flags)
    }

    //解绑服务
    fun unbindService(context: Context, conn: ServiceConnection) {
        context.unbindService(conn)
    }

}