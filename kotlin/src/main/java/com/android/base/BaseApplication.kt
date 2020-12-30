package com.android.base

import android.app.Activity
import android.app.Application
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.multidex.MultiDex
import com.android.util.CrashHandler
import com.android.util.SizeUtil
import com.billy.android.swipe.SmartSwipeBack
import com.billy.android.swipe.SwipeConsumer
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
        initSwipeBack()
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    //设置侧滑框架
    private fun initSwipeBack() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            SmartSwipeBack.activityBezierBack(this) { activity -> activity !is DontSwipeBack }
        } else {
            SmartSwipeBack.activitySlidingBack(
                this, { activity -> activity !is DontSwipeBack },
                SizeUtil.dp2pxInt(20f), 0, Color.parseColor("#88A3A3A3"),
                SizeUtil.dp2pxInt(15f), 0.5f, SwipeConsumer.DIRECTION_LEFT
            )
        }
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