package com.android.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.Window
import androidx.annotation.IntRange
import com.android.base.BaseApplication

/**
 * Created by xuzhb on 2021/5/10
 * Desc:亮度相关工具类
 * 需要权限<uses-permission android:name="android.permission.WRITE_SETTINGS" />
 */
object BrightnessUtil {

    /**
     * 判断是否开启自动调节亮度
     */
    fun isAutoBrightnessEnabled(): Boolean {
        kotlin.runCatching {
            val mode = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE)
            return mode == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC
        }
        return false
    }

    /**
     * 设置是否开启自动调节亮度
     *
     * @param enabled true：打开，false：关闭
     */
    fun setAutoBrightnessEnabled(context: Context, enabled: Boolean): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.System.canWrite(context.applicationContext)) {
            kotlin.runCatching {
                val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
                intent.data = Uri.parse("package:${context.packageName}")
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }
            return false
        }
        kotlin.runCatching {
            return Settings.System.putInt(
                getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE,
                if (enabled) Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC else Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL
            )
        }
        return false
    }

    /**
     * 获取屏幕亮度 0-255
     */
    fun getBrightness(): Int {
        kotlin.runCatching {
            return Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS)
        }
        return 0
    }

    /**
     * 设置屏幕亮度
     *
     * @param brightness 亮度值
     */
    fun setBrightness(context: Context, @IntRange(from = 0, to = 255) brightness: Int): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.System.canWrite(context.applicationContext)) {
            kotlin.runCatching {
                val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
                intent.data = Uri.parse("package:${context.packageName}")
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }
            return false
        }
        kotlin.runCatching {
            val resolver = getContentResolver()
            val result = Settings.System.putInt(resolver, Settings.System.SCREEN_BRIGHTNESS, brightness)
            resolver.notifyChange(Settings.System.getUriFor("screen_brightness"), null)
            return result
        }
        return false
    }

    /**
     * 获取窗口亮度
     */
    fun getWindowBrightness(window: Window?): Int {
        if (window == null) {
            return -1
        }
        kotlin.runCatching {
            val brightness = window.attributes.screenBrightness
            if (brightness < 0) {
                return getBrightness()
            }
            return (brightness * 255).toInt()
        }
        return 0
    }

    /**
     * 设置窗口亮度
     *
     * @param brightness 亮度值
     */
    fun setWindowBrightness(window: Window?, @IntRange(from = 0, to = 255) brightness: Int): Boolean {
        if (window == null) {
            return false
        }
        kotlin.runCatching {
            val layoutParams = window.attributes
            layoutParams.screenBrightness = brightness / 255f
            window.attributes = layoutParams
            return true
        }
        return false
    }

    private fun getContentResolver() = BaseApplication.instance.contentResolver

}