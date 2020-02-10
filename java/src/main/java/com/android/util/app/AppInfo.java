package com.android.util.app;

import android.graphics.drawable.Drawable;

/**
 * Created by xuzhb on 2020/2/5
 * Desc:应用信息类
 */
public class AppInfo {

    private int uid;               //应用UID
    private String label;          //应用标签，即名称
    private Drawable icon;         //应用图片
    private String packageName;    //应用包名
    private String apkPath;        //应用路径
    private String versionName;    //应用版本号
    private int versionCode;       //应用版本码
    private boolean isSystemApp;  //是否是系统App

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

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getApkPath() {
        return apkPath;
    }

    public void setApkPath(String apkPath) {
        this.apkPath = apkPath;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public boolean isSystemApp() {
        return isSystemApp;
    }

    public void setSystemApp(boolean systemApp) {
        isSystemApp = systemApp;
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                "uid=" + uid +
                ", label='" + label + '\'' +
                ", icon=" + icon +
                ", packageName='" + packageName + '\'' +
                ", apkPath='" + apkPath + '\'' +
                ", versionName='" + versionName + '\'' +
                ", versionCode=" + versionCode +
                ", isSystemApp=" + isSystemApp +
                '}';
    }
}
