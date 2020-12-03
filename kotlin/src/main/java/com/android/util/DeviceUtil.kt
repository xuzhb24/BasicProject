package com.android.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.provider.Settings
import android.telephony.TelephonyManager
import android.text.TextUtils
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.net.NetworkInterface
import java.util.*

/**
 * Created by xuzhb on 2020/12/2
 * Desc:设备相关工具类
 */
object DeviceUtil {

    //设备是否Root
    fun isDeviceRooted() = checkRootMethod1() || checkRootMethod2() || checkRootMethod3()

    private fun checkRootMethod1(): Boolean {
        val buildTags = Build.TAGS
        return buildTags != null && buildTags.contains("test-keys")
    }

    private fun checkRootMethod2(): Boolean {
        val paths = arrayOf(
            "/system/app/Superuser.apk", "/sbin/su", "/system/bin/su", "/system/xbin/su", "/data/local/xbin/su",
            "/data/local/bin/su", "/system/sd/xbin/su", "/system/bin/failsafe/su", "/data/local/su", "/su/bin/su"
        )
        for (path in paths) {
            if (File(path).exists()) {
                return true
            }
        }
        return false
    }

    private fun checkRootMethod3(): Boolean {
        var process: Process? = null
        try {
            process = Runtime.getRuntime().exec(arrayOf("/system/xbin/which", "su"))
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            if (reader.readLine() != null) {
                return true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            process?.destroy()
        }
        return false
    }

    //获取设备MAC地址
    fun getMacAddress(context: Context): String? {
        var macAddress = getMacAddressByWifiInfo(context)
        if ("02:00:00:00:00:00" != macAddress) {
            return macAddress
        }
        macAddress = getMacAddressByNetworkInterface()
        if ("02:00:00:00:00:00" != macAddress) {
            return macAddress
        }
        macAddress = getMacAddressByReadFile()
        if ("02:00:00:00:00:00" != macAddress) {
            return macAddress
        }
        return null
    }

    //获取设备MAC地址，需要权限<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    @SuppressLint("HardwareIds")
    private fun getMacAddressByWifiInfo(context: Context): String {
        val manager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as? WifiManager
        return manager?.connectionInfo?.macAddress ?: "02:00:00:00:00:00"
    }

    //获取设备MAC地址
    fun getMacAddressByNetworkInterface(): String {
        try {
            val list: List<NetworkInterface> = Collections.list(NetworkInterface.getNetworkInterfaces())
            for (ni in list) {
                if (!ni.name.equals("wlan0", ignoreCase = true)) {
                    continue
                }
                val bytes = ni.hardwareAddress
                if (bytes != null && bytes.isNotEmpty()) {
                    val sb = StringBuilder()
                    for (b in bytes) {
                        sb.append(String.format("%02x:", b))
                    }
                    return sb.deleteCharAt(sb.length - 1).toString()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return "02:00:00:00:00:00"
    }

    //获取设备MAC地址
    fun getMacAddressByReadFile(): String {
        var result = ShellUtil.execCmd("getprop wifi.interface", false)
        if (result.code == 0 && !TextUtils.isEmpty(result.successMsg)) {
            result = ShellUtil.execCmd("cat /sys/class/net/" + result.successMsg + "/address", false)
            if (result.code == 0 && !TextUtils.isEmpty(result.successMsg)) {
                return result.successMsg!!
            }
        }
        return "02:00:00:00:00:00"
    }

    //获取设备厂商
    fun getManufacturer() = Build.MANUFACTURER

    //获取设备型号
    fun getModel() = Build.MODEL

    //获取设备品牌
    fun getBrand() = Build.BRAND

    //获取SDK版本号，返回值示例：28
    fun getSDKVersion() = Build.VERSION.SDK_INT

    //获取系统版本号，返回值示例：9（Android 9.0）
    fun getOSVersion() = Build.VERSION.RELEASE

    //获取设备AndroidID
    @SuppressLint("HardwareIds")
    fun getAndroidID(context: Context) =
        Settings.Secure.getString(context.applicationContext.contentResolver, Settings.Secure.ANDROID_ID)

    //获取设备分辨率
    fun getScreenResolution(context: Context): String {
        val dm = context.applicationContext.resources.displayMetrics
        val width = dm.widthPixels
        val height = dm.heightPixels
        return width.toString() + "x" + height
    }

    //获取运营商名称，需要权限<uses-permission android:name="android.permission.READ_PHONE_STATE" />
    @SuppressLint("MissingPermission")
    fun getSimOperatorName(context: Context): String {
        val tm = context.applicationContext.getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager
        var providerName = ""
        try {
            val imsi = tm?.subscriberId
            if (imsi != null) {
                providerName = if (imsi.startsWith("46000") || imsi.startsWith("46002") || imsi.startsWith("46007")) {
                    "中国移动"
                } else if (imsi.startsWith("46001") || imsi.startsWith("46006")) {
                    "中国联通"
                } else if (imsi.startsWith("46003")) {
                    "中国电信"
                } else {
                    "未知"
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            providerName = "获取失败，" + e.message
        }
        return providerName
    }

    //获取设备当前系统语言
    fun getSystemLanguage() = Locale.getDefault().language

    //获取设备支持的系统语言列表
    fun getSystemLanguages(): Array<Locale> {
        return Locale.getAvailableLocales()
    }

    //获取设备IMEI，需要权限<uses-permission android:name="android.permission.READ_PHONE_STATE" />
    @SuppressLint("MissingPermission")
    fun getIMEI(context: Context): String? {
        val tm = context.applicationContext.getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager
        try {
            if (tm != null) {
                return tm.deviceId
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    //关机，通过发送关机广播，
    //需要系统权限<android:sharedUserId="android.uid.system" />和关机权限<uses-permission android:name="android.permission.SHUTDOWN" />
    fun shutdownByBroadcast(context: Context) {
        val intent = Intent("android.intent.action.ACTION_REQUEST_SHUTDOWN")
        intent.putExtra("android.intent.extra.KEY_CONFIRM", false)  //true会弹出是否关机的确认窗口
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.sendBroadcast(intent)
    }

    //关机，通过反射调用IPowerManager的shutdown方法，
    //需要系统权限<android:sharedUserId="android.uid.system" />和关机权限<uses-permission android:name="android.permission.SHUTDOWN" />
    @SuppressLint("PrivateApi")
    fun shutdownByPowerManager() {
        try {
            //获取ServiceManager类
            val serviceManager = Class.forName("android.os.ServiceManager")
            //获取ServiceManager的getService方法
            val getService = serviceManager.getMethod("getService", String::class.java)
            //调用getService获取PowerManager
            val powerManager = getService.invoke(null, Context.POWER_SERVICE)
            //获得IPowerManager.Stub类
            val stub = Class.forName("android.os.IPowerManager\$Stub")
            //获取asInterface方法
            val asInterface = stub.getMethod("asInterface", IBinder::class.java)
            //调用asInterface方法获取IPowerManager对象
            val ipowerManager = asInterface.invoke(null, powerManager)
            //获得shutdown()方法
            val shutdown = ipowerManager.javaClass.getMethod("shutdown", Boolean::class.javaPrimitiveType, Boolean::class.javaPrimitiveType)
            //调用shutdown()方法
            shutdown.invoke(ipowerManager, false, true)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //关机，通过root命令，需要root权限
    fun shutdownByRoot() {
        ShellUtil.execCmd("reboot -p", true)
    }

    //重启，通过发送重启广播，需要系统权限<android:sharedUserId="android.uid.system"/>
    fun rebootByBroadcast(context: Context) {
        val intent = Intent(Intent.ACTION_REBOOT)
        intent.putExtra("nowait", 1)
        intent.putExtra("interval", 1)
        intent.putExtra("window", 0)
        context.sendBroadcast(intent)
    }

    //重启，通过PowerManager，需要系统权限<android:sharedUserId="android.uid.system"/>
    fun rebootByPowerManager(context: Context) {
        val manager = context.applicationContext.getSystemService(Context.POWER_SERVICE) as PowerManager
        try {
            manager.reboot(null)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //重启，通过root命令
    fun rebootByRoot() {
        ShellUtil.execCmd("reboot", true)
    }

    //重启到recovery，需要root权限
    fun rebootToRecovery() {
        ShellUtil.execCmd("reboot recovery", true)
    }

    //重启到bootloader，需要root权限
    fun rebootToBootloader() {
        ShellUtil.execCmd("reboot bootloader", true)
    }

}