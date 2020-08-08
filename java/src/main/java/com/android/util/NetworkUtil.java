package com.android.util;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.android.base.BaseApplication;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by xuzhb on 2020/1/5
 * Desc:网络工具
 */
public class NetworkUtil {

    //打开网络设置界面
    public static void openWirelessSettings(Context context) {
        Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    //是否连接到网络
    public static boolean isConnected() {
        return isConnected(BaseApplication.getInstance());
    }

    //是否连接到网络
    public static boolean isConnected(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        return info != null && info.isConnected();
    }

    //获取NetworkInfo
    public static NetworkInfo getNetworkInfo(Context context) {
        return getConnectivityManager(context).getActiveNetworkInfo();
    }

    //判断网络是否可用
    public static boolean isAvailable() {
        ShellUtil.CommandResult result = ShellUtil.execCmd("ping -c 1 -w 1 223.5.5.5", false);
        LogUtil.e("isAvailable", result.toString());
        return result.getCode() == 0;
    }

    //判断移动数据是否打开
    public static boolean getDataEnabled(Context context) {
        try {
            TelephonyManager tm = getTelephonyManager(context);
            return (boolean) tm.getClass().getDeclaredMethod("getDataEnabled").invoke(tm);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //打开或关闭移动数据，需要系统应用和权限<uses-permission android:name="android.permission.MODIFY_PHONE_STATE"/>
    public static void setDataEnabled(Context context, boolean enabled) {
        try {
            TelephonyManager tm = getTelephonyManager(context);
            tm.getClass().getDeclaredMethod("setDataEnabled", boolean.class).invoke(tm, enabled);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //判断网络是否是4G
    public static boolean is4G(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        return info != null && info.isAvailable() && info.getSubtype() == TelephonyManager.NETWORK_TYPE_LTE;
    }

    //判断wifi是否打开
    public static boolean isWifiEnabled(Context context) {
        WifiManager wifiManager = getWifiManager(context);
        return wifiManager.isWifiEnabled();
    }

    //打开或关闭wifi
    public static void setWifiEnabled(Context context, boolean enabled) {
        WifiManager wifiManager = getWifiManager(context);
        if (enabled) {  //打开
            if (!wifiManager.isWifiEnabled()) {
                wifiManager.setWifiEnabled(true);
            }
        } else {  //关闭
            if (wifiManager.isWifiEnabled()) {
                wifiManager.setWifiEnabled(false);
            }
        }
    }

    //判断wifi是否连接
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager cm = getConnectivityManager(context);
        return cm != null && cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;
    }

    //判断wifi是否可用
    public static boolean isWifiAvailable(Context context) {
        return isWifiEnabled(context) && isAvailable();
    }

    //获取ConnectivityManager
    public static ConnectivityManager getConnectivityManager(Context context) {
        return (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    //获取TelephonyManager
    public static TelephonyManager getTelephonyManager(Context context) {
        return (TelephonyManager) context.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
    }

    //获取WifiManager
    public static WifiManager getWifiManager(Context context) {
        return (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }

    private static final int NETWORK_TYPE_GSM = 16;
    private static final int NETWORK_TYPE_TD_SCDMA = 17;
    private static final int NETWORK_TYPE_IWLAN = 18;

    public enum NetworkType {
        NETWORK_WIFI,
        NETWORK_4G,
        NETWORK_3G,
        NETWORK_2G,
        NETWORK_UNKNOWN,
        NETWORK_NO
    }

    //获取当前网络类型
    public static NetworkType getNetworkType(Context context) {
        NetworkType netType = NetworkType.NETWORK_NO;
        NetworkInfo info = getNetworkInfo(context);
        if (info != null && info.isAvailable()) {
            if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                netType = NetworkType.NETWORK_WIFI;
            } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                switch (info.getSubtype()) {
                    case NETWORK_TYPE_GSM:
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN:
                        netType = NetworkType.NETWORK_2G;
                        break;
                    case NETWORK_TYPE_TD_SCDMA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    case TelephonyManager.NETWORK_TYPE_EHRPD:
                    case TelephonyManager.NETWORK_TYPE_HSPAP:
                        netType = NetworkType.NETWORK_3G;
                        break;
                    case NETWORK_TYPE_IWLAN:
                    case TelephonyManager.NETWORK_TYPE_LTE:
                        netType = NetworkType.NETWORK_4G;
                        break;
                    default:
                        String subtypeName = info.getSubtypeName();
                        if (subtypeName.equalsIgnoreCase("TD-SCDMA")
                                || subtypeName.equalsIgnoreCase("WCDMA")
                                || subtypeName.equalsIgnoreCase("CDMA2000")) {
                            netType = NetworkType.NETWORK_3G;
                        } else {
                            netType = NetworkType.NETWORK_UNKNOWN;
                        }
                        break;
                }
            } else {
                netType = NetworkType.NETWORK_UNKNOWN;
            }
        }
        return netType;
    }

    //获取IP地址
    public static String getIPAddress(boolean useIPv4) {
        try {
            for (Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces(); nis.hasMoreElements(); ) {
                NetworkInterface ni = nis.nextElement();
                // 防止小米手机返回10.0.2.15
                if (!ni.isUp()) continue;
                for (Enumeration<InetAddress> addresses = ni.getInetAddresses(); addresses.hasMoreElements(); ) {
                    InetAddress inetAddress = addresses.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String hostAddress = inetAddress.getHostAddress();
                        boolean isIPv4 = hostAddress.indexOf(':') < 0;
                        if (useIPv4) {
                            if (isIPv4) return hostAddress;
                        } else {
                            if (!isIPv4) {
                                int index = hostAddress.indexOf('%');
                                return index < 0 ? hostAddress.toUpperCase() : hostAddress.substring(0, index).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

    //解析域名对应的IP地址
    public static String getDomainAddress(String domain) {
        try {
            ExecutorService exec = Executors.newCachedThreadPool();
            Future<String> fs = exec.submit(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    InetAddress inetAddress;
                    try {
                        inetAddress = InetAddress.getByName(domain);
                        return inetAddress.getHostAddress();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            });
            return fs.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

}
