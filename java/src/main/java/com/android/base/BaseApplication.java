package com.android.base;

import android.app.Application;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by xuzhb on 2019/11/2
 * Desc:Application基类
 */
public class BaseApplication extends Application {

    private RefWatcher mRefWatcher;
    private static BaseApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        mRefWatcher = initRefWatcher();
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

}

