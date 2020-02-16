package com.android.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Created by xuzhb on 2020/2/4
 * Desc:设备相关工具类
 */
public class DeviceUtil {

    //设备是否Root
    public static boolean isDeviceRooted() {
        return checkRootMethod1() || checkRootMethod2() || checkRootMethod3();
    }

    private static boolean checkRootMethod1() {
        String buildTags = android.os.Build.TAGS;
        return buildTags != null && buildTags.contains("test-keys");
    }

    private static boolean checkRootMethod2() {
        String[] paths = {"/system/app/Superuser.apk", "/sbin/su", "/system/bin/su", "/system/xbin/su", "/data/local/xbin/su",
                "/data/local/bin/su", "/system/sd/xbin/su", "/system/bin/failsafe/su", "/data/local/su", "/su/bin/su"};
        for (String path : paths) {
            if (new File(path).exists()) {
                return true;
            }
        }
        return false;
    }

    private static boolean checkRootMethod3() {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(new String[]{"/system/xbin/which", "su"});
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            if (in.readLine() != null) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
        return false;
    }

    //获取设备MAC地址
    public static String getMacAddress(Context context) {
        String macAddress = getMacAddressByWifiInfo(context);
        if (!"02:00:00:00:00:00".equals(macAddress)) {
            return macAddress;
        }
        macAddress = getMacAddressByNetworkInterface();
        if (!"02:00:00:00:00:00".equals(macAddress)) {
            return macAddress;
        }
        macAddress = getMacAddressByReadFile();
        if (!"02:00:00:00:00:00".equals(macAddress)) {
            return macAddress;
        }
        return null;
    }

    //获取设备MAC地址，需要权限<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    @SuppressLint("HardwareIds")
    private static String getMacAddressByWifiInfo(Context context) {
        try {
            WifiManager manager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if (manager != null) {
                WifiInfo info = manager.getConnectionInfo();
                if (info != null) {
                    //Android 6.0开始WifiInfo.getMacAddress()和BluetoothAdapter.getAddress()方法都将返回02:00:00:00:00:00
                    return info.getMacAddress();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "02:00:00:00:00:00";
    }

    //获取设备MAC地址
    private static String getMacAddressByNetworkInterface() {
        try {
            List<NetworkInterface> list = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface ni : list) {
                if (!ni.getName().equalsIgnoreCase("wlan0")) {
                    continue;
                }
                byte[] bytes = ni.getHardwareAddress();
                if (bytes != null && bytes.length > 0) {
                    StringBuilder sb = new StringBuilder();
                    for (byte b : bytes) {
                        sb.append(String.format("%02x:", b));
                    }
                    return sb.deleteCharAt(sb.length() - 1).toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "02:00:00:00:00:00";
    }

    //获取设备MAC地址
    private static String getMacAddressByReadFile() {
        ShellUtil.CommandResult result = ShellUtil.execCmd("getprop wifi.interface", false);
        if (result.getCode() == 0 && !TextUtils.isEmpty(result.getSuccessMsg())) {
            result = ShellUtil.execCmd("cat /sys/class/net/" + result.getSuccessMsg() + "/address", false);
            if (result.getCode() == 0 && !TextUtils.isEmpty(result.getSuccessMsg())) {
                return result.getSuccessMsg();
            }
        }
        return "02:00:00:00:00:00";
    }

    //获取设备厂商
    public static String getManufacturer() {
        return Build.MANUFACTURER;
    }

    //获取设备型号
    public static String getModel() {
        return Build.MODEL;
    }

    //获取设备品牌
    public static String getBrand() {
        return Build.BRAND;
    }

    //获取SDK版本号，返回值示例：28
    public static int getSDKVersion() {
        return Build.VERSION.SDK_INT;
    }

    //获取系统版本号，返回值示例：9（Android 9.0）
    public static String getOSVersion() {
        return Build.VERSION.RELEASE;
    }

    //获取设备AndroidID
    @SuppressLint("HardwareIds")
    public static String getAndroidID(Context context) {
        return Settings.Secure.getString(context.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    //获取设备分辨率
    public static String getScreenResolution(Context context) {
        DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        return width + "x" + height;
    }

    //获取运营商名称，需要权限<uses-permission android:name="android.permission.READ_PHONE_STATE" />
    @SuppressLint("MissingPermission")
    public static String getSimOperatorName(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getApplicationContext().
                getSystemService(Context.TELEPHONY_SERVICE);
        String providerName = "";
        try {
            String imsi = tm.getSubscriberId();
            if (imsi != null) {
                if (imsi.startsWith("46000") || imsi.startsWith("46002") || imsi.startsWith("46007")) {
                    providerName = "中国移动";
                } else if (imsi.startsWith("46001") || imsi.startsWith("46006")) {
                    providerName = "中国联通";
                } else if (imsi.startsWith("46003")) {
                    providerName = "中国电信";
                } else {
                    providerName = "未知";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            providerName = "获取失败，" + e.getMessage();
        }
        return providerName;
    }

    //获取设备当前系统语言
    public static String getSystemLanguage() {
        return Locale.getDefault().getLanguage();
    }

    //获取设备支持的系统语言列表
    public static Locale[] getSystemLanguages() {
        return Locale.getAvailableLocales();
    }

    //获取设备IMEI，需要权限<uses-permission android:name="android.permission.READ_PHONE_STATE" />
    @SuppressLint("MissingPermission")
    public static String getIMEI(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getApplicationContext().
                getSystemService(Context.TELEPHONY_SERVICE);
        try {
            if (tm != null) {
                return tm.getDeviceId();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //关机，通过发送关机广播，
    //需要系统权限<android:sharedUserId="android.uid.system" />和关机权限<uses-permission android:name="android.permission.SHUTDOWN" />
    public static void shutdownByBroadcast(Context context) {
        Intent intent = new Intent("android.intent.action.ACTION_REQUEST_SHUTDOWN");
        intent.putExtra("android.intent.extra.KEY_CONFIRM", false);  //true会弹出是否关机的确认窗口
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.sendBroadcast(intent);
    }

    //关机，通过反射调用IPowerManager的shutdown方法，
    //需要系统权限<android:sharedUserId="android.uid.system" />和关机权限<uses-permission android:name="android.permission.SHUTDOWN" />
    @SuppressLint("PrivateApi")
    public static void shutdownByPowerManager() {
        try {
            //获取ServiceManager类
            Class<?> serviceManager = Class.forName("android.os.ServiceManager");
            //获取ServiceManager的getService方法
            Method getService = serviceManager.getMethod("getService", java.lang.String.class);
            //调用getService获取PowerManager
            Object powerManager = getService.invoke(null, Context.POWER_SERVICE);
            //获得IPowerManager.Stub类
            Class<?> stub = Class.forName("android.os.IPowerManager$Stub");
            //获取asInterface方法
            Method asInterface = stub.getMethod("asInterface", android.os.IBinder.class);
            //调用asInterface方法获取IPowerManager对象
            Object ipowerManager = asInterface.invoke(null, powerManager);
            //获得shutdown()方法
            Method shutdown = ipowerManager.getClass().getMethod("shutdown", boolean.class, boolean.class);
            //调用shutdown()方法
            shutdown.invoke(ipowerManager, false, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //关机，通过root命令，需要root权限
    public static void shutdownByRoot() {
        ShellUtil.execCmd("reboot -p", true);
    }

    //重启，通过发送重启广播，需要系统权限<android:sharedUserId="android.uid.system"/>
    public static void rebootByBroadcast(Context context) {
        Intent intent = new Intent(Intent.ACTION_REBOOT);
        intent.putExtra("nowait", 1);
        intent.putExtra("interval", 1);
        intent.putExtra("window", 0);
        context.sendBroadcast(intent);
    }

    //重启，通过PowerManager，需要系统权限<android:sharedUserId="android.uid.system"/>
    public static void rebootByPowerManager(Context context) {
        PowerManager manager = (PowerManager) context.getApplicationContext().getSystemService(Context.POWER_SERVICE);
        try {
            manager.reboot(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //重启，通过root命令
    public static void rebootByRoot() {
        ShellUtil.execCmd("reboot", true);
    }

    //重启到recovery，需要root权限
    public static void rebootToRecovery() {
        ShellUtil.execCmd("reboot recovery", true);
    }

    //重启到bootloader，需要root权限
    public static void rebootToBootloader() {
        ShellUtil.execCmd("reboot bootloader", true);
    }

}
