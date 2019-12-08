package com.android.util.traffic;

import android.annotation.SuppressLint;

/**
 * Created by xuzhb on 2019/12/8
 * Desc:
 */
public class TrafficInfo {

    private int uid;  //应用UID
    private String label;  //应用标签
    private String packageName;  //应用包名
    private double rxBytes;  //下载的流量，以byte为单位
    private double txBytes;  //上传的流量

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public double getRxBytes() {
        return rxBytes;
    }

    public void setRxBytes(double rxBytes) {
        this.rxBytes = rxBytes;
    }

    public double getTxBytes() {
        return txBytes;
    }

    public void setTxBytes(double txBytes) {
        this.txBytes = txBytes;
    }

    public double getTotalBytes() {
        if (rxBytes == -1 && txBytes == -1) {
            return -1;
        }
        return rxBytes + txBytes;
    }

    @Override
    public String toString() {
        return "总流量:" + format(getTotalBytes()) + "\t\t下载:" + format(rxBytes) + "\t\t上传:" + format(txBytes) +
                "\t\t" + label + "\t\t" + uid + "\t\t" + packageName;
    }

    @SuppressLint("DefaultLocale")
    private String format(double value) {
        return String.format("%.2f", value);
    }

}