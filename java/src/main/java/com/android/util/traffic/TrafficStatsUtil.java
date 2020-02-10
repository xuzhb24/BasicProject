package com.android.util.traffic;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import com.android.util.ByteUnit;
import com.android.util.SystemUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by xuzhb on 2019/12/7
 * Desc:流量工具
 * TrafficStats:2.2后提供
 * Rx:下载   Tx:上传
 * 返回值类型均为long型，如果返回等于-1代表UNSUPPORTED，当前设备不支持统计
 * TrafficStats.getTotalRxBytes()       //获取总的接受字节数，包含Mobile和WiFi等
 * TrafficStats.getTotalTxBytes()       //获取总的发送字节数，包含Mobile和WiFi等
 * TrafficStats.getTotalRxPackets()     //获取总的接受数据包数，包含Mobile和WiFi等
 * TrafficStats.getTotalTxPackets()     //获取发送的总数据包数，包含Mobile和WiFi等
 * TrafficStats.getMobileRxBytes()      //获取通过Mobile连接收到的字节总数，不包含WiFi
 * TrafficStats.getMobileTxBytes()      //获取Mobile发送的总字节数，不包含WiFi
 * TrafficStats.getMobileRxPackets()    //获取Mobile连接收到的数据包总数，不包含WiFi
 * TrafficStats.getMobileTxPackets()    //获取Mobile发送的总数据包数，不包含WiFi
 * TrafficStats.getUidRxBytes(int uid)  //获取指定UID对应的应用程序的接受字节数
 * TrafficStats.getUidTxBytes(int uid)  //获取指定UID对应的应用程序的发送字节数
 */
public class TrafficStatsUtil {

    private static final int SPACING = 1024;

    //获取指定应用到目前为止使用的总流量，包括上传和下载
    public static double getAppTotalBytes(int uid, @ByteUnit.ByteUnitDef String unit) {
        double rxBytes = TrafficStats.getUidRxBytes(uid);
        double txBytes = TrafficStats.getUidTxBytes(uid);
        if (rxBytes != -1 && txBytes != -1) {  //返回-1表示当前设备不支持统计
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
            return rxBytes + txBytes;
        }
        return -1;
    }

    //获取本应用到目前为止使用的总流量，包括上传和下载
    public static double getLocalAppTotalBytes(Context context, @ByteUnit.ByteUnitDef String unit) {
        int uid = SystemUtil.getUid(context, context.getPackageName());
        return getAppTotalBytes(uid, unit);
    }

    //获取已安装应用消耗的流量
    public static List<TrafficInfo> getAppTotalBytesList(Context context, @ByteUnit.ByteUnitDef String unit) {
        List<TrafficInfo> list = new ArrayList<>();
        PackageManager pm = context.getPackageManager();
        //获取所有已安装应用
        List<PackageInfo> packageInfos = pm.getInstalledPackages(0);
        for (PackageInfo info : packageInfos) {
            TrafficInfo bean = new TrafficInfo();
            int uid = info.applicationInfo.uid;
            bean.setUid(uid);
            bean.setLabel(info.applicationInfo.loadLabel(pm).toString());
            bean.setPackageName(info.packageName);
            double rxBytes = TrafficStats.getUidRxBytes(uid);
            double txBytes = TrafficStats.getUidTxBytes(uid);
            if (rxBytes != -1 && txBytes != -1) {
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
            }
            bean.setRxBytes(rxBytes);
            bean.setTxBytes(txBytes);
            list.add(bean);
        }
        return list;
    }

    //获取已安装应用消耗的流量排行，根据总流量，从大到小排序，size：截取的记录个数
    public static List<TrafficInfo> getAppTotalBytesSortList(Context context, @ByteUnit.ByteUnitDef String unit, int size) {
        List<TrafficInfo> list = getAppTotalBytesList(context, unit);
        if (list.size() > 1) {
            Collections.sort(list, new Comparator<TrafficInfo>() {
                @Override
                public int compare(TrafficInfo o1, TrafficInfo o2) {
                    if (o1.getTotalBytes() < o2.getTotalBytes()) {
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

}
