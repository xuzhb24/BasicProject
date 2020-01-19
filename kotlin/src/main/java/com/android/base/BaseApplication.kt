package com.android.base

import android.app.Activity
import android.app.Application
import com.squareup.leakcanary.BuildConfig
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher
import com.tencent.bugly.crashreport.CrashReport
import java.util.*

/**
 * Created by xuzhb on 2019/8/1
 * Desc:Application基类
 */
class BaseApplication : Application() {

    private lateinit var refWatcher: RefWatcher  //lateinit：声明一个不需要初始化的非空类型的属性

    private lateinit var mActivityStack: LinkedList<Activity>

    //获取Application单例
    companion object {
        lateinit var instance: BaseApplication

        //监控内存泄漏
        fun getRefWatcher(): RefWatcher {
            return instance.refWatcher
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        refWatcher = initRefWatcher()
        mActivityStack = LinkedList()

        /* Bugly start*/
        CrashReport.initCrashReport(applicationContext, "4728a77b29", BuildConfig.DEBUG)
        /* Bugly end*/
    }

    private fun initRefWatcher(): RefWatcher =
        if (LeakCanary.isInAnalyzerProcess(this)) RefWatcher.DISABLED
        else LeakCanary.install(this)

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