package com.android.util;

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

}
