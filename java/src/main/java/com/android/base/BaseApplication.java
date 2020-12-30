package com.android.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;

import androidx.multidex.MultiDex;

import com.android.util.CrashHandler;
import com.android.util.SizeUtil;
import com.android.util.traffic.NetworkStatsHelper;
import com.billy.android.swipe.SmartSwipeBack;
import com.billy.android.swipe.SwipeConsumer;

import java.util.LinkedList;

/**
 * Created by xuzhb on 2019/11/2
 * Desc:Application基类
 */
public class BaseApplication extends Application {

    private LinkedList<Activity> mActivityStack;

    private static BaseApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        CrashHandler.getInstance().init(this);
        mActivityStack = new LinkedList<>();
        initSwipeBack();
        NetworkStatsHelper.init(this);  //流量统计
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    //设置侧滑框架
    private void initSwipeBack() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            SmartSwipeBack.activityBezierBack(this, activity -> !(activity instanceof DontSwipeBack));
        } else {
            SmartSwipeBack.activitySlidingBack(
                    this, activity -> !(activity instanceof DontSwipeBack),
                    SizeUtil.dp2pxInt(20f), 0, Color.parseColor("#88A3A3A3"),
                    SizeUtil.dp2pxInt(15f), 0.5f, SwipeConsumer.DIRECTION_LEFT
            );
        }
    }

    //获取Application单例
    public static BaseApplication getInstance() {
        return mInstance;
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

