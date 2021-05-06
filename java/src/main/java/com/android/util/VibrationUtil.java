package com.android.util;

import android.content.Context;
import android.os.Vibrator;

/**
 * Created by xuzhb on 2021/5/6
 * Desc:震动相关工具类
 * 需要权限，<uses-permission android:name="android.permission.VIBRATE" />
 */
public class VibrationUtil {

    /**
     * 震动
     *
     * @param milliseconds 震动时长 ( 毫秒 )
     */
    public static boolean vibrate(Context context, long milliseconds) {
        try {
            Vibrator vibrator = getVibratorService(context);
            vibrator.vibrate(milliseconds);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * pattern 模式震动
     *
     * @param pattern new long[]{400, 800, 1200, 1600}, 等待400ms，震动800ms，等待1200ms，震动1600ms
     * @param repeat  指定 pattern 数组的索引, 指定 pattern 数组中从 repeat 索引开始的震动进行循环,
     *                -1 表示不重复, 非 -1 表示从 pattern 数组指定下标开始重复震动
     */
    public static boolean vibrate(Context context, long[] pattern, int repeat) {
        if (pattern == null || pattern.length == 0) {
            return false;
        }
        try {
            Vibrator vibrator = getVibratorService(context);
            vibrator.vibrate(pattern, repeat);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 取消震动
     */
    public static boolean cancel(Context context) {
        try {
            getVibratorService(context).cancel();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static Vibrator getVibratorService(Context context) {
        return (Vibrator) context.getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
    }

}
