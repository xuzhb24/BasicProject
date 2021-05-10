package com.android.util;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.IntRange;

import com.android.base.BaseApplication;

/**
 * Created by xuzhb on 2021/5/10
 * Desc:亮度相关工具类
 * 需要权限<uses-permission android:name="android.permission.WRITE_SETTINGS" />
 */
public class BrightnessUtil {

    /**
     * 判断是否开启自动调节亮度
     */
    public static boolean isAutoBrightnessEnabled() {
        try {
            int mode = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE);
            return mode == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 设置是否开启自动调节亮度
     *
     * @param enabled true：打开，false：关闭
     */
    public static boolean setAutoBrightnessEnabled(Context context, boolean enabled) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.System.canWrite(context.getApplicationContext())) {
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + context.getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
        try {
            return Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE,
                    enabled ? Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC : Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取屏幕亮度 0-255
     */
    public static int getBrightness() {
        try {
            return Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 设置屏幕亮度
     *
     * @param brightness 亮度值
     */
    public static boolean setBrightness(Context context, @IntRange(from = 0, to = 255) int brightness) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.System.canWrite(context.getApplicationContext())) {
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + context.getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
        try {
            ContentResolver resolver = getContentResolver();
            boolean result = Settings.System.putInt(resolver, Settings.System.SCREEN_BRIGHTNESS, brightness);
            resolver.notifyChange(Settings.System.getUriFor("screen_brightness"), null);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取窗口亮度
     */
    public static int getWindowBrightness(Window window) {
        if (window == null) {
            return -1;
        }
        try {
            float brightness = window.getAttributes().screenBrightness;
            if (brightness < 0) {
                return getBrightness();
            }
            return (int) (brightness * 255);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 设置窗口亮度
     *
     * @param brightness 亮度值
     */
    public static boolean setWindowBrightness(Window window, @IntRange(from = 0, to = 255) int brightness) {
        if (window == null) {
            return false;
        }
        try {
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.screenBrightness = brightness / 255f;
            window.setAttributes(layoutParams);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static ContentResolver getContentResolver() {
        return BaseApplication.getInstance().getContentResolver();
    }

}
