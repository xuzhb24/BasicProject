package com.android.util.app

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.TextUtils
import android.util.Log
import com.android.util.DeviceUtil
import com.android.util.IntentUtil
import com.android.util.ShellUtil
import com.android.util.TransformUtil
import com.android.util.encrypt.MessageDigestUtil
import java.io.File

/**
 * Created by xuzhb on 2020/11/18
 * Desc:应用相关工具类
 */
object AppUtil {

    //是否安装指定应用
    fun isAppInstall(context: Context, packageName: String) =
        !TextUtils.isEmpty(packageName) && IntentUtil.getLaunchAppIntent(context, packageName) != null

    //安装App，authority：7.0及以上安装需要传入AndroidManifest中<provider>标签的authorities属性
    fun installApp(context: Context, apkPath: String, authority: String) {
        if (TextUtils.isEmpty(apkPath)) {
            return
        }
        installApp(context, File(apkPath), authority)
    }

    //安装App，authority：7.0及以上安装需要传入AndroidManifest中<provider>标签的authorities属性
    fun installApp(context: Context, file: File?, authority: String) {
        if (file == null || !file.exists()) {
            return
        }
        context.startActivity(IntentUtil.getInstallAppIntent(context, file, authority))
    }

    //安装App，可以通过startActivityForResult回调结果
    fun installAppForResult(activity: Activity, apkPath: String, authority: String, requestCode: Int) {
        if (TextUtils.isEmpty(apkPath)) {
            return
        }
        installAppForResult(activity, File(apkPath), authority, requestCode)
    }

    //安装App，可以通过startActivityForResult回调结果
    fun installAppForResult(activity: Activity, file: File?, authority: String, requestCode: Int) {
        if (file == null || !file.exists()) {
            return
        }
        activity.startActivityForResult(IntentUtil.getInstallAppIntent(activity, file, authority), requestCode)
    }

    //卸载App
    fun uninstallApp(context: Context, packageName: String) {
        if (TextUtils.isEmpty(packageName)) {
            return
        }
        context.startActivity(IntentUtil.getUninstallAppIntent(packageName))
    }

    //卸载App，可以通过startActivityForResult回调结果
    fun uninstallAppForResult(activity: Activity, packageName: String, requestCode: Int) {
        if (TextUtils.isEmpty(packageName)) {
            return
        }
        activity.startActivityForResult(IntentUtil.getUninstallAppIntent(packageName), requestCode)
    }

    /**
     * 静默安装App
     * 非root需添加系统权限<uses-permission android:name="android.permission.INSTALL_PACKAGES" />
     *
     * @param apkPath     apk文件路径
     * @param packageName 本应用的包名，而不是要安装的应用包名
     * @return true：安装成功，false：安装失败
     */
    fun installAppSilent(apkPath: String, packageName: String): Boolean {
        if (TextUtils.isEmpty(apkPath)) {
            return false
        }
        val file = File(apkPath)
        if (!file.exists()) {
            return false
        }
        val command = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)//7.0及以上的系统需要添加包名，否则会执行失败
            "pm install -i $packageName -r $apkPath" else "pm install -r $apkPath"
        val commandResult = ShellUtil.execCmd(command, DeviceUtil.isDeviceRooted())
        return !TextUtils.isEmpty(commandResult.successMsg) && commandResult.successMsg!!.toLowerCase().contains("success")
    }

    //打开App
    fun openApp(context: Context, packageName: String?) {
        if (packageName.isNullOrBlank()) {
            return
        }
        IntentUtil.getLaunchAppIntent(context, packageName)?.let {
            context.startActivity(it)
        }
    }

    //打开App，可以通过startActivityForResult回调结果
    fun openAppForResult(activity: Activity, packageName: String?, requestCode: Int) {
        if (packageName.isNullOrBlank()) {
            return
        }
        IntentUtil.getLaunchAppIntent(activity, packageName)?.let {
            activity.startActivityForResult(it, requestCode)
        }
    }

    //打开本App的设置页面
    fun openLocalAppSettings(context: Context) = openAppSettings(context, context.packageName)

    //打开App设置页面
    fun openAppSettings(context: Context, packageName: String?) {
        if (packageName.isNullOrBlank()) {
            return
        }
        context.startActivity(IntentUtil.getAppSettingsIntent(packageName))
    }

    //获取本App的名称
    fun getLocalAppLabel(context: Context) = getAppLabel(context, context.packageName)

    //获取App名称
    fun getAppLabel(context: Context, packageName: String?): String? {
        if (packageName.isNullOrBlank()) {
            return null
        }
        try {
            val pm = context.applicationContext.packageManager
            val pi = pm.getPackageInfo(packageName, 0)
            return pi?.applicationInfo?.loadLabel(pm)?.toString()
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return null
    }

    //获取本App图标
    fun getLocalAppIcon(context: Context) = getAppIcon(context, context.packageName)

    //获取App图标
    fun getAppIcon(context: Context, packageName: String?): Drawable? {
        if (packageName.isNullOrBlank()) {
            return null
        }
        try {
            val pm = context.applicationContext.packageManager
            val pi = pm.getPackageInfo(packageName, 0)
            return pi?.applicationInfo?.loadIcon(pm)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return null
    }

    //获取本App路径
    fun getLocalAppPath(context: Context) = getAppPath(context, context.packageName)

    //获取App路径
    fun getAppPath(context: Context, packageName: String?): String? {
        if (packageName.isNullOrBlank()) {
            return null
        }
        try {
            val pm = context.applicationContext.packageManager
            val pi = pm.getPackageInfo(packageName, 0)
            return pi?.applicationInfo?.sourceDir
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return null
    }

    //获取本App版本号
    fun getLocalAppVersionName(context: Context) = getAppVersionName(context, context.packageName)

    //获取App版本号
    fun getAppVersionName(context: Context, packageName: String?): String? {
        if (packageName.isNullOrBlank()) {
            return null
        }
        try {
            val pm = context.applicationContext.packageManager
            val pi = pm.getPackageInfo(packageName, 0)
            return pi?.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return null
    }

    //获取本App版本码
    fun getLocalAppVersionCode(context: Context) = getAppVersionCode(context, context.packageName)

    //获取App版本码
    fun getAppVersionCode(context: Context, packageName: String?): Int {
        if (packageName.isNullOrBlank()) {
            return -1
        }
        try {
            val pm = context.applicationContext.packageManager
            val pi = pm.getPackageInfo(packageName, 0)
            return pi?.versionCode ?: -1
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return -1
    }

    //判断本应用是否是系统应用
    fun isLocalSystemApp(context: Context) = isSystemApp(context, context.packageName)

    //判断指定应用是否是系统应用
    fun isSystemApp(context: Context, packageName: String?): Boolean {
        if (packageName.isNullOrBlank()) {
            return false
        }
        try {
            val pm = context.applicationContext.packageManager
            val ai = pm.getApplicationInfo(packageName, 0)
            return ai != null && ai.flags and ApplicationInfo.FLAG_SYSTEM != 0
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return false
    }

    //判断本App是否是Debug版本
    fun isLocalAppDebug(context: Context) = isAppDebug(context, context.packageName)

    //判断App是否是Debug版本
    fun isAppDebug(context: Context, packageName: String?): Boolean {
        if (packageName.isNullOrBlank()) {
            return false
        }
        try {
            val pm = context.applicationContext.packageManager
            val ai = pm.getApplicationInfo(packageName, 0)
            return ai != null && ai.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return false
    }

    //判断本App是否有root权限
    fun isLocalAppRoot(): Boolean {
        val result = ShellUtil.execCmd("echo root", true)
        if (result.code == 0) {
            return true
        }
        if (!TextUtils.isEmpty(result.failureMsg)) {
            Log.i("isAppRoot", result.failureMsg)
        }
        return false
    }

    //获取本App签名
    fun getLocalAppSignature(context: Context) = getAppSignature(context, context.packageName)

    //获取App签名
    fun getAppSignature(context: Context, packageName: String?): Array<Signature>? {
        if (packageName.isNullOrBlank()) {
            return null
        }
        try {
            val pm = context.applicationContext.packageManager

            @SuppressLint("PackageManagerGetSignatures")
            val pi = pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            return pi?.signatures
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return null
    }

    //获取本App的MD5值
    fun getLocalAppSignatureMD5(context: Context) = getAppSignatureMD5(context, context.packageName)

    //获取App的MD5值
    fun getAppSignatureMD5(context: Context, packageName: String?): String? {
        val signatures = getAppSignature(context, packageName) ?: return null
        MessageDigestUtil.md5(signatures[0].toByteArray())?.let {
            return TransformUtil.bytes2Hex(it)
        }
        return null
    }

    //获取本App签名的SHA1值
    fun getLocalAppSignatureSHA1(context: Context) = getAppSignatureSHA1(context, context.packageName)

    //获取App签名的SHA1值
    fun getAppSignatureSHA1(context: Context, packageName: String): String? {
        val signatures = getAppSignature(context, packageName) ?: return null
        return TransformUtil.bytes2Hex(MessageDigestUtil.sha1(signatures[0].toByteArray())!!)
            .toUpperCase().replace("(?<=[0-9A-F]{2})[0-9A-F]{2}".toRegex(), ":$0")
    }

    //判断本App是否处于前台
    fun isLocalAppForeground(context: Context): Boolean {
        val manager = context.applicationContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val infos = manager.runningAppProcesses
        if (infos.isNullOrEmpty()) {
            return false
        }
        for (info in infos) {
            if (info.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return info.processName == context.applicationContext.packageName
            }
        }
        return false
    }

    //获取本App信息
    fun getLocalAppInfo(context: Context) = getAppInfo(context, context.packageName)

    //获取App信息
    fun getAppInfo(context: Context, packageName: String): AppInfo? {
        try {
            val pm = context.applicationContext.packageManager
            val pi = pm.getPackageInfo(packageName, 0)
            return loadAppInfo(pm, pi)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    //获取所有已安装App信息
    fun getAppInfoList(context: Context): MutableList<AppInfo> {
        val list: MutableList<AppInfo> = mutableListOf()
        val pm = context.applicationContext.packageManager
        //获取所有已安装应用
        val packageInfos = pm.getInstalledPackages(0)
        for (pi in packageInfos) {
            loadAppInfo(pm, pi)?.let {
                list.add(it)
            }
        }
        return list
    }

    private fun loadAppInfo(pm: PackageManager?, pi: PackageInfo?): AppInfo? {
        if (pm == null || pi == null) {
            return null
        }
        val ai = pi.applicationInfo
        return AppInfo(
            ai.uid,
            ai.loadLabel(pm).toString(),
            ai.loadIcon(pm),
            pi.packageName,
            ai.sourceDir,
            pi.versionName,
            pi.versionCode,
            ApplicationInfo.FLAG_SYSTEM and ai.flags != 0
        )
    }

}