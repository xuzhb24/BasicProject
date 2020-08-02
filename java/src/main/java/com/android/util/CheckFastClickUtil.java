package com.android.util;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by xuzhb on 2020/7/14
 * Desc:快速点击检测工具
 */
public class CheckFastClickUtil {

    private static long mLastClickTime = 0;

    public static boolean isFastClick() {
        return isFastClick(400);
    }

    public static boolean isFastClick(long interval) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - mLastClickTime > interval) {
            mLastClickTime = currentTime;
            return false;
        } else {
            return true;
        }
    }

    //已经连续点击的次数，原子操作，是线程安全的
    private static AtomicInteger mClickCount = new AtomicInteger(0);

    //连续点击事件监听，clickCount：连续点击的次数
    public static void setOnMultiClickListener(long interval, OnMultiClickListener listener) {
        long currentClickTime = System.currentTimeMillis();
        long delay = currentClickTime - mLastClickTime;
        if (delay <= interval) {
            mClickCount.incrementAndGet();
            listener.onMultiClick(mClickCount.get());
        } else {
            mClickCount.set(1);
            listener.onMultiClick(mClickCount.get());
        }
        mLastClickTime = currentClickTime;
    }

    public interface OnMultiClickListener {
        void onMultiClick(int clickCount);
    }

}
