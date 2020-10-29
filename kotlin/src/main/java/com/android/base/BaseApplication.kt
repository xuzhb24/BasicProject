package com.android.base

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.android.util.CrashHandler
import java.util.*

/**
 * Created by xuzhb on 2019/8/1
 * Desc:Application基类
 */
class BaseApplication : Application() {

    private lateinit var mActivityStack: LinkedList<Activity>

    //获取Application单例
    companion object {
        lateinit var instance: BaseApplication
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        CrashHandler.instance.init(this)
        mActivityStack = LinkedList()
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    fun addActivity(activity: Activity) {
        mActivityStack.add(activity)
    }

    fun removeActivity(activity: Activity) {
        if (mActivityStack.contains(activity)) {
            mActivityStack.remove(activity)
        }
    }

    //结束所有的Activity
    fun finishAllActivities() {
        for (i in mActivityStack.indices) {
            mActivityStack.get(i).finish()
        }
        mActivityStack.clear()
    }

}