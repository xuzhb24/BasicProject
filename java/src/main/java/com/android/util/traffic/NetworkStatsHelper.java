package com.android.util.traffic;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AppOpsManager;
import android.app.usage.NetworkStats;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.RemoteException;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.util.ByteUnit;
import com.android.util.LogUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * Created by xuzhb on 2019/12/8
 * Desc:
 * 参考https://www.jianshu.com/p/52192441089b、https://blog.csdn.net/w7849516230/article/details/71705835
 * Android 6.0（API23）引入
 * 查询指定网络类型在某时间间隔内的总的流量统计信息:
 * NetworkStats.Bucket querySummaryForDevice(int networkType, String subscriberId, long startTime, long endTime)
 * 查询某uid在指定网络类型和时间间隔内的流量统计信息:
 * NetworkStats queryDetailsForUid(int networkType, String subscriberId, long startTime, long endTime, int uid)
 * 查询指定网络类型在某时间间隔内的详细的流量统计信息（包括每个uid）:
 * NetworkStats queryDetails(int networkType, String subscriberId, long startTime, long endTime)
 */
@TargetApi(Build.VERSION_CODES.M)
public class NetworkStatsHelper {

    private static final String TAG = "NetworkStatsHelper";
    private static final double SPACING = 1024;
    private static Context mContext;
    private NetworkStatsManager mManager;

    private NetworkStatsHelper() {
        mManager = (NetworkStatsManager) mContext.getSystemService(Context.NETWORK_STATS_SERVICE);
    }

    private static class SingleTonHolder {
        private static final NetworkStatsHelper holder = new NetworkStatsHelper();
    }

    public static NetworkStatsHelper getInstance() {
        return SingleTonHolder.holder;
    }

    //初始化Context
    public static void init(Context context) {
        mContext = context;
    }

    /**
     * 获取某个时间段内使用的总流量，包括上传和下载，返回-1表示取值发生异常
     *
     * @param networkType 网络连接方式，取值主要有ConnectivityManager.TYPE_WIFI(1)和ConnectivityManager.TYPE_MOBILE(0)
     * @param startTime   开始时间
     * @param endTime     结束时间
     * @param unit        返回值单位
     * @return 上传和下载的流量总和
     */
    public double getTotalBytes(int networkType, long startTime, long endTime, @ByteUnit.ByteUnitDef String unit) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        LogUtil.i(TAG, "networkType:" + networkType + " startTime:" + df.format(startTime) + " endTime:" + df.format(endTime));
        NetworkStats.Bucket bucket;
        try {
            String subscriberId = "";
            if (networkType == ConnectivityManager.TYPE_MOBILE) {  //Mobile
                subscriberId = getMobileSubscriberId();
            }
            bucket = mManager.querySummaryForDevice(networkType, subscriberId, startTime, endTime);
        } catch (RemoteException e) {
            e.printStackTrace();
            return -1;
        }
        long rxBytes = bucket.getRxBytes();
        long txBytes = bucket.getTxBytes();
        double totalBytes = rxBytes + txBytes;
        if (totalBytes > 0) {
            if (ByteUnit.KB.equals(unit)) {
                totalBytes /= Math.pow(SPACING, 1);
            } else if (ByteUnit.MB.equals(unit)) {
                totalBytes /= Math.pow(SPACING, 2);
            } else if (ByteUnit.GB.equals(unit)) {
                totalBytes /= Math.pow(SPACING, 3);
            }
        }
        LogUtil.i(TAG, "rxBytes:" + rxBytes + unit + " txBytes:" + txBytes + unit + " total:" + totalBytes);
        return totalBytes;
    }

    //获取到目前为止使用的Wifi总流量，包括上传和下载，返回-1表示取值发生异常
    public double getWifiTotalBytes(@ByteUnit.ByteUnitDef String unit) {
        return getTotalBytes(ConnectivityManager.TYPE_WIFI, 0, System.currentTimeMillis(), unit);
    }

    //获取到目前为止使用的Mobile总流量，包括上传和下载，返回-1表示取值发生异常
    public double getMobileTotalBytes(@ByteUnit.ByteUnitDef String unit) {
        return getTotalBytes(ConnectivityManager.TYPE_MOBILE, 0, System.currentTimeMillis(), unit);
    }

    /**
     * 获取指定应用某个时间段内使用的总流量，包括上传和下载
     *
     * @param networkType 网络连接方式，取值主要有ConnectivityManager.TYPE_WIFI(1)和ConnectivityManager.TYPE_MOBILE(0)
     * @param startTime   开始时间
     * @param endTime     结束时间
     * @param uid         应用的uid
     * @param unit        返回值单位
     * @return 上传和下载的流量总和，返回-1表示取值异常
     */
    public double getAppTotalBytes(int networkType, long startTime, long endTime, int uid, @ByteUnit.ByteUnitDef String unit) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        LogUtil.i(TAG, "networkType:" + networkType + " startTime:" + df.format(startTime) + " endTime:" + df.format(endTime) + " uid:" + uid);
        String subscriberId = "";
        if (networkType == ConnectivityManager.TYPE_MOBILE) {  //Mobile
            subscriberId = getMobileSubscriberId();
        }
        NetworkStats networkStats = mManager.queryDetailsForUid(networkType, subscriberId, startTime, endTime, uid);
        NetworkStats.Bucket bucket = new NetworkStats.Bucket();
        networkStats.getNextBucket(bucket);
        long rxBytes = bucket.getRxBytes();
        long txBytes = bucket.getTxBytes();
        double totalBytes = rxBytes + txBytes;
        if (totalBytes > 0) {
            if (ByteUnit.KB.equals(unit)) {
                totalBytes /= Math.pow(SPACING, 1);
            } else if (ByteUnit.MB.equals(unit)) {
                totalBytes /= Math.pow(SPACING, 2);
            } else if (ByteUnit.GB.equals(unit)) {
                totalBytes /= Math.pow(SPACING, 3);
            }
        }
        LogUtil.i(TAG, "rxBytes:" + rxBytes + unit + " txBytes:" + txBytes + unit + " total:" + totalBytes);
        return totalBytes;
    }

    //获取指定应用某个时间开始至今使用的Wifi总流量，包括上传和下载
    public double getAppWifiTotalBytes(long startTime, int uid, @ByteUnit.ByteUnitDef String unit) {
        return getAppTotalBytes(ConnectivityManager.TYPE_WIFI, startTime, System.currentTimeMillis(), uid, unit);
    }

    //获取指定应用某个时间开始至今使用的Mobile总流量，包括上传和下载
    public double getAppMobileTotalBytes(long startTime, int uid, @ByteUnit.ByteUnitDef String unit) {
        return getAppTotalBytes(ConnectivityManager.TYPE_MOBILE, startTime, System.currentTimeMillis(), uid, unit);
    }

    //获取已安装应用某个时间段内消耗的流量总和
    public List<TrafficInfo> getAppTotalBytesList(int networkType, long startTime, long endTime, @ByteUnit.ByteUnitDef String unit) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        LogUtil.i(TAG, "networkType:" + networkType + " startTime:" + df.format(startTime) + " endTime:" + df.format(endTime));
        String subscriberId = "";
        if (networkType == ConnectivityManager.TYPE_MOBILE) {  //Mobile
            subscriberId = getMobileSubscriberId();
        }
        List<TrafficInfo> list = new ArrayList<>();
        PackageManager pm = mContext.getPackageManager();
        //获取所有已安装应用
        List<PackageInfo> packageInfos = pm.getInstalledPackages(0);
        for (PackageInfo info : packageInfos) {
            TrafficInfo bean = new TrafficInfo();
            int uid = info.applicationInfo.uid;
            bean.setUid(uid);
            bean.setLabel(info.applicationInfo.loadLabel(pm).toString());
            bean.setPackageName(info.packageName);
            NetworkStats networkStats = mManager.queryDetailsForUid(networkType, subscriberId, startTime, endTime, uid);
            NetworkStats.Bucket bucket = new NetworkStats.Bucket();
            networkStats.getNextBucket(bucket);
            double rxBytes = bucket.getRxBytes();
            double txBytes = bucket.getTxBytes();
            if (ByteUnit.KB.equals(unit)) {
                rxBytes /= Math.pow(SPACING, 1);
                txBytes /= Math.pow(SPACING, 1);
            } else if (ByteUnit.MB.equals(unit)) {
                rxBytes /= Math.pow(SPACING, 2);
                txBytes /= Math.pow(SPACING, 2);
            } else if (ByteUnit.GB.equals(unit)) {
                rxBytes /= Math.pow(SPACING, 3);
                txBytes /= Math.pow(SPACING, 3);
            }
            bean.setRxBytes(rxBytes);
            bean.setTxBytes(txBytes);
            list.add(bean);
        }
        return list;
    }

    //获取已安装应用某个时间段内消耗的流量排行，根据总流量，从大到小排序，size：截取的记录个数
    public List<TrafficInfo> getAppTotalBytesSortList(int networkType, long startTime, long endTime, @ByteUnit.ByteUnitDef String unit, int size) {
        List<TrafficInfo> list = getAppTotalBytesList(networkType, startTime, endTime, unit);
        if (list.size() > 1) {
            Collections.sort(list, new Comparator<TrafficInfo>() {
                @Override
                public int compare(TrafficInfo o1, TrafficInfo o2) {
                    if (o1.getTotalBytes() < o2.getTotalBytes()) {  //根据总流量排序
                        return 1;
                    } else if (o1.getTotalBytes() == o2.getTotalBytes()) {
                        return 0;
                    } else {
                        return -1;
                    }
                }
            });
        }
        if (list.size() >= size) {
            return list.subList(0, size);
        }
        return list;
    }

    //获取已安装应用某个时间开始至今消耗的Wifi流量排行
    public List<TrafficInfo> getAppWifiTotalBytesSortList(long startTime, @ByteUnit.ByteUnitDef String unit, int size) {
        return getAppTotalBytesSortList(ConnectivityManager.TYPE_WIFI, startTime, System.currentTimeMillis(), unit, size);
    }

    //获取已安装应用某个时间开始至今消耗的Mobile流量排行
    public List<TrafficInfo> getAppMobileTotalBytesSortList(long startTime, @ByteUnit.ByteUnitDef String unit, int size) {
        return getAppTotalBytesSortList(ConnectivityManager.TYPE_MOBILE, startTime, System.currentTimeMillis(), unit, size);
    }

    //获取sim卡的IMSI码，即sim卡的唯一标识
    private String getMobileSubscriberId() {
        TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_GRANTED) {
            String subscriberId = tm.getSubscriberId();
            LogUtil.i(TAG, "IMSI:" + subscriberId);
            return tm.getSubscriberId();
        } else {
            LogUtil.e(TAG, "无权限获取IMSI");
        }
        return null;
    }

    //集成动态权限申请
    public static boolean hasPermissionToReadNetworkStats(Activity activity) {
        int permissionCheck = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.READ_PHONE_STATE}, 8);
        } else {
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        final AppOpsManager appOps = (AppOpsManager) activity.getApplicationContext().getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), activity.getApplicationContext().getPackageName());
        if (mode == AppOpsManager.MODE_ALLOWED) {
            return true;
        }
        requestReadNetworkStats();
        return false;
    }

    // 打开“有权查看使用情况的应用”页面
    public static void requestReadNetworkStats() {
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        mContext.startActivity(intent);
    }
}
