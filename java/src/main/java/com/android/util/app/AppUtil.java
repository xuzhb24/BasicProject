package com.android.util.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import com.android.util.DeviceUtil;
import com.android.util.IntentUtil;
import com.android.util.ShellUtil;
import com.android.util.TransformUtil;
import com.android.util.encrypt.MessageDigestUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuzhb on 2020/2/4
 * Desc:应用相关工具类
 */
public class AppUtil {

    //是否安装指定应用
    public static boolean isAppInstall(Context context, String packageName) {
        return !TextUtils.isEmpty(packageName) && IntentUtil.getLaunchAppIntent(context, packageName) != null;
    }

    //安装App，authority：7.0及以上安装需要传入AndroidManifest中<provider>标签的authorities属性
    public static void installApp(Context context, String apkPath, String authority) {
        if (TextUtils.isEmpty(apkPath)) {
            return;
        }
        installApp(context, new File(apkPath), authority);
    }

    //安装App，authority：7.0及以上安装需要传入AndroidManifest中<provider>标签的authorities属性
    public static void installApp(Context context, File file, String authority) {
        if (file == null || !file.exists()) {
            return;
        }
        context.startActivity(IntentUtil.getInstallAppIntent(context, file, authority));
    }

    //安装App，可以通过startActivityForResult回调结果
    public static void installAppForResult(Activity activity, String apkPath, String authority, int requestCode) {
        if (TextUtils.isEmpty(apkPath)) {
            return;
        }
        installAppForResult(activity, new File(apkPath), authority, requestCode);
    }

    //安装App，可以通过startActivityForResult回调结果
    public static void installAppForResult(Activity activity, File file, String authority, int requestCode) {
        if (file == null || !file.exists()) {
            return;
        }
        activity.startActivityForResult(IntentUtil.getInstallAppIntent(activity, file, authority), requestCode);
    }

    //卸载App
    public static void uninstallApp(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return;
        }
        context.startActivity(IntentUtil.getUninstallAppIntent(packageName));
    }

    //卸载App，可以通过startActivityForResult回调结果
    public static void uninstallAppForResult(Activity activity, String packageName, int requestCode) {
        if (TextUtils.isEmpty(packageName)) {
            return;
        }
        activity.startActivityForResult(IntentUtil.getUninstallAppIntent(packageName), requestCode);
    }

    /**
     * 静默安装App
     * 非root需添加系统权限<uses-permission android:name="android.permission.INSTALL_PACKAGES" />
     *
     * @param apkPath     apk文件路径
     * @param packageName 本应用的包名，而不是要安装的应用包名
     * @return true：安装成功，false：安装失败
     */
    public static boolean installAppSilent(String apkPath, String packageName) {
        if (TextUtils.isEmpty(apkPath)) {
            return false;
        }
        File file = new File(apkPath);
        if (!file.exists()) {
            return false;
        }
        String command = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N  //7.0及以上的系统需要添加包名，否则会执行失败
                ? "pm install -i " + packageName + " -r " + apkPath
                : "pm install -r " + apkPath;
        ShellUtil.CommandResult commandResult = ShellUtil.execCmd(command, DeviceUtil.isDeviceRooted());
        return !TextUtils.isEmpty(commandResult.getSuccessMsg()) && commandResult.getSuccessMsg().toLowerCase().contains("success");
    }

    /**
     * 静默卸载App
     * 非root需添加系统权限<uses-permission android:name="android.permission.DELETE_PACKAGES" />
     *
     * @param packageName 包名
     * @param isKeepData  是否保留数据
     * @return true：卸载成功，false：卸载失败
     */
    public static boolean uninstallAppSilent(String packageName, boolean isKeepData) {
        if (TextUtils.isEmpty(packageName)) {
            return false;
        }
        String command = "pm uninstall " + (isKeepData ? "-k " : "") + packageName;
        ShellUtil.CommandResult commandResult = ShellUtil.execCmd(command, DeviceUtil.isDeviceRooted());
        return !TextUtils.isEmpty(commandResult.getSuccessMsg()) && commandResult.getSuccessMsg().toLowerCase().contains("success");
    }

    //打开App
    public static void openApp(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return;
        }
        Intent intent = IntentUtil.getLaunchAppIntent(context, packageName);
        if (intent != null) {
            context.startActivity(intent);
        }
    }

    //打开App，可以通过startActivityForResult回调结果
    public static void openAppForResult(Activity activity, String packageName, int requestCode) {
        if (TextUtils.isEmpty(packageName)) {
            return;
        }
        Intent intent = IntentUtil.getLaunchAppIntent(activity, packageName);
        if (intent != null) {
            activity.startActivityForResult(intent, requestCode);
        }
    }

    //打开本App的设置页面
    public static void openLocalAppSettings(Context context) {
        openAppSettings(context, context.getPackageName());
    }

    //打开App设置页面
    public static void openAppSettings(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return;
        }
        context.startActivity(IntentUtil.getAppSettingsIntent(packageName));
    }

    //获取本App的名称
    public static String getLocalAppLabel(Context context) {
        return getAppLabel(context, context.getPackageName());
    }

    //获取App名称
    public static String getAppLabel(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return null;
        }
        try {
            PackageManager pm = context.getApplicationContext().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? null : pi.applicationInfo.loadLabel(pm).toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    //获取本App图标
    public static Drawable getLocalAppIcon(Context context) {
        return getAppIcon(context, context.getPackageName());
    }

    //获取App图标
    public static Drawable getAppIcon(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return null;
        }
        try {
            PackageManager pm = context.getApplicationContext().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? null : pi.applicationInfo.loadIcon(pm);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    //获取本App路径
    public static String getLocalAppPath(Context context) {
        return getAppPath(context, context.getPackageName());
    }

    //获取App路径
    public static String getAppPath(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return null;
        }
        try {
            PackageManager pm = context.getApplicationContext().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? null : pi.applicationInfo.sourceDir;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    //获取本App版本号
    public static String getLocalAppVersionName(Context context) {
        return getAppVersionName(context, context.getPackageName());
    }

    //获取App版本号
    public static String getAppVersionName(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return null;
        }
        try {
            PackageManager pm = context.getApplicationContext().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? null : pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    //获取本App版本码
    public static int getLocalAppVersionCode(Context context) {
        return getAppVersionCode(context, context.getPackageName());
    }

    //获取App版本码
    public static int getAppVersionCode(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return -1;
        }
        try {
            PackageManager pm = context.getApplicationContext().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? -1 : pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    //判断本应用是否是系统应用
    public static boolean isLocalSystemApp(Context context) {
        return isSystemApp(context, context.getPackageName());
    }

    //判断指定应用是否是系统应用
    public static boolean isSystemApp(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return false;
        }
        try {
            PackageManager pm = context.getApplicationContext().getPackageManager();
            ApplicationInfo ai = pm.getApplicationInfo(packageName, 0);
            return ai != null && (ai.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    //判断本App是否是Debug版本
    public static boolean isLocalAppDebug(Context context) {
        return isAppDebug(context, context.getPackageName());
    }

    //判断App是否是Debug版本
    public static boolean isAppDebug(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return false;
        }
        try {
            PackageManager pm = context.getApplicationContext().getPackageManager();
            ApplicationInfo ai = pm.getApplicationInfo(packageName, 0);
            return ai != null && (ai.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    //判断本App是否有root权限
    public static boolean isLocalAppRoot() {
        ShellUtil.CommandResult result = ShellUtil.execCmd("echo root", true);
        if (result.getCode() == 0) {
            return true;
        }
        if (!TextUtils.isEmpty(result.getFailureMsg())) {
            Log.i("isAppRoot", result.getFailureMsg());
        }
        return false;
    }

    //获取本App签名
    public static Signature[] getLocalAppSignature(Context context) {
        return getAppSignature(context, context.getPackageName());
    }

    //获取App签名
    public static Signature[] getAppSignature(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return null;
        }
        try {
            PackageManager pm = context.getApplicationContext().getPackageManager();
            @SuppressLint("PackageManagerGetSignatures")
            PackageInfo pi = pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            return pi == null ? null : pi.signatures;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    //获取本App的MD5值
    public static String getLocalAppSignatureMD5(Context context) {
        return getAppSignatureMD5(context, context.getPackageName());
    }

    //获取App的MD5值
    public static String getAppSignatureMD5(Context context, String packageName) {
        Signature[] signatures = getAppSignature(context, packageName);
        if (signatures == null) {
            return null;
        }
        return TransformUtil.bytes2Hex(MessageDigestUtil.md5(signatures[0].toByteArray()));
    }

    //获取本App签名的SHA1值
    public static String getLocalAppSignatureSHA1(Context context) {
        return getAppSignatureSHA1(context, context.getPackageName());
    }

    //获取App签名的SHA1值
    public static String getAppSignatureSHA1(Context context, String packageName) {
        Signature[] signatures = getAppSignature(context, packageName);
        if (signatures == null) {
            return null;
        }
        return TransformUtil.bytes2Hex(MessageDigestUtil.sha1(signatures[0].toByteArray())).
                toUpperCase().replaceAll("(?<=[0-9A-F]{2})[0-9A-F]{2}", ":$0");
    }

    //判断本App是否处于前台
    public static boolean isLocalAppForeground(Context context) {
        ActivityManager manager = (ActivityManager) context.getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> infos = manager.getRunningAppProcesses();
        if (infos == null || infos.size() == 0) {
            return false;
        }
        for (ActivityManager.RunningAppProcessInfo info : infos) {
            if (info.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return info.processName.equals(context.getApplicationContext().getPackageName());
            }
        }
        return false;
    }

    //判断App是否处于前台，如果不是本App，且SDK大于21时需添加权限<uses-permission android:name="android.permission.PACKAGE_USAGE_STATS"/>
//    public static boolean isAppForeground(Context context, String packageName) {
//
//    }

    //清除App所有数据
//    public static boolean cleanAppData(){
//
//    }

    //清除App所有数据
    public static boolean cleanAppData(String... dirPaths) {
        File[] dirs = new File[dirPaths.length];
        for (int i = 0; i < dirPaths.length; i++) {
            dirs[i] = new File(dirPaths[i]);
        }
        return cleanAppData(dirs);
    }

    /**
     * 清除App所有数据
     *
     * @param dirs 目录
     * @return true：成功，false：失败
     */
    public static boolean cleanAppData(File... dirs) {
        return false;
    }

    //获取本App信息
    public static AppInfo getLocalAppInfo(Context context) {
        return getAppInfo(context, context.getPackageName());
    }

    //获取App信息
    public static AppInfo getAppInfo(Context context, String packageName) {
        try {
            PackageManager pm = context.getApplicationContext().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return loadAppInfo(pm, pi);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    //获取所有已安装App信息
    public static List<AppInfo> getAppInfoList(Context context) {
        List<AppInfo> list = new ArrayList<>();
        PackageManager pm = context.getApplicationContext().getPackageManager();
        //获取所有已安装应用
        List<PackageInfo> packageInfos = pm.getInstalledPackages(0);
        for (PackageInfo pi : packageInfos) {
            AppInfo appInfo = loadAppInfo(pm, pi);
            if (appInfo == null) {
                continue;
            }
            list.add(appInfo);
        }
        return list;
    }

    private static AppInfo loadAppInfo(PackageManager pm, PackageInfo pi) {
        if (pm == null || pi == null) {
            return null;
        }
        AppInfo appInfo = new AppInfo();
        ApplicationInfo ai = pi.applicationInfo;
        appInfo.setUid(ai.uid);
        appInfo.setLabel(ai.loadLabel(pm).toString());
        appInfo.setIcon(ai.loadIcon(pm));
        appInfo.setPackageName(pi.packageName);
        appInfo.setApkPath(ai.sourceDir);
        appInfo.setVersionName(pi.versionName);
        appInfo.setVersionCode(pi.versionCode);
        appInfo.setSystemApp((ApplicationInfo.FLAG_SYSTEM & ai.flags) != 0);
        return appInfo;
    }

}
