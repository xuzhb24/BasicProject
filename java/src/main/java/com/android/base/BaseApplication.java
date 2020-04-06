package com.android.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.android.util.CrashHandler;
import com.android.util.traffic.NetworkStatsHelper;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import java.util.LinkedList;

/**
 * Created by xuzhb on 2019/11/2
 * Desc:Application基类
 */
public class BaseApplication extends Application {

    private RefWatcher mRefWatcher;
    private LinkedList<Activity> mActivityStack;

    private static BaseApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        mRefWatcher = initRefWatcher();
        mActivityStack = new LinkedList<>();

        CrashHandler.getInstance().init(this);

        NetworkStatsHelper.init(this);  //流量统计
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    //获取Application单例
    public static BaseApplication getInstance() {
        return mInstance;
    }

    //监控内存泄漏
    public static RefWatcher getRefWatcher() {
        return mInstance.mRefWatcher;
    }

    private RefWatcher initRefWatcher() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return RefWatcher.DISABLED;
        }
        return LeakCanary.install(this);
    }

    public void addActivity(Activity activity) {
        mActivityStack.add(activity);
    }

    public void removeActivity(Activity activity) {
        mActivityStack.remove(activity);
    }

    //结束所有的Activity
    public void finishAllActivities() {
        for (Activity activity : mActivityStack) {
            activity.finish();
        }
        mActivityStack.clear();
    }

}

