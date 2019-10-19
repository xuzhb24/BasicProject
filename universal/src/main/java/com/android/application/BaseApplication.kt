package com.android.application

import android.app.Application
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher

/**
 * Created by xuzhb on 2019/8/1
 * Desc:Application基类
 */
class BaseApplication : Application() {

    private lateinit var refWatcher: RefWatcher //lateinit：声明一个不需要初始化的非空类型的属性

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
    }

    private fun initRefWatcher(): RefWatcher =
        if (LeakCanary.isInAnalyzerProcess(this)) RefWatcher.DISABLED
        else LeakCanary.install(this)

}