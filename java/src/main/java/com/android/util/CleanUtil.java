package com.android.util;

import android.content.Context;

import java.io.File;

/**
 * Created by xuzhb on 2020/3/3
 * Desc:应用文件清除工具类
 */
public class CleanUtil {

    /*
     * /data/data/com.xxx.xxx路径下各目录：
     * cache
     * code_cache
     * databases
     * files
     * lib
     * shared_prefs
     */

    //清除内部缓存，即/data/data/com.xxx.xxx/cache目录下所有文件
    public static boolean cleanInternalCache(Context context) {
        return FileUtil.deleteFilesInDirectory(context.getCacheDir());
    }

    //根据名称清除内部缓存，即/data/data/com.xxx.xxx/cache目录下指定文件
    public static boolean cleanInternalCacheByName(Context context, String name) {
        return FileUtil.deleteFile(context.getCacheDir() + File.separator + name);
    }

    //清除内部数据库，即/data/data/com.xxx.xxx/databases目录下所有文件
    public static boolean cleanInternalDatabase(Context context) {
        return FileUtil.deleteFilesInDirectory(context.getFilesDir().getParent() + File.separator + "databases");
    }

    //根据名称清除数据库，如/data/data/com.xxx.xxx/databases目录下指定数据库
    public static boolean cleanInternalDatabaseByName(Context context, String name) {
        return context.deleteDatabase(name);
    }

    //清除内部文件，即/data/data/com.xxx.xxx/files目录下所有文件
    public static boolean cleanInternalFiles(Context context) {
        return FileUtil.deleteFilesInDirectory(context.getFilesDir());
    }

    //根据名称清除内部文件，即/data/data/com.xxx.xxx/files目录下指定文件
    public static boolean cleanInternalFilesByName(Context context, String name) {
        return FileUtil.deleteFile(context.getFilesDir() + File.separator + name);
    }

    //清除内部SharePreference，即/data/data/com.xxx.xxx/shared_prefs目录下所有文件
    public static boolean cleanInternalSharePreference(Context context) {
        return FileUtil.deleteFilesInDirectory(context.getFilesDir().getParent() + File.separator + "shared_prefs");
    }

    //根据名称清除内部SharePreference，即/data/data/com.xxx.xxx/shared_prefs目录下指定文件
    public static boolean cleanInternalSharePreferenceByName(Context context, String name) {
        return FileUtil.deleteFile(context.getFilesDir().getParent() + File.separator + "shared_prefs" + File.separator + name);
    }

    //清除外部缓存，即/storage/emulated/0/android/data/com.xxx.xxx/cache目录下所有文件
    public static boolean cleanExternalCache(Context context) {
        return SDCardUtil.isSDCardEnable() && FileUtil.deleteFilesInDirectory(context.getExternalCacheDir());
    }

    //根据名称清除外部缓存，即/storage/emulated/0/android/data/com.xxx.xxx/cache目录下指定文件
    public static boolean cleanExternalCacheByName(Context context, String name) {
        return SDCardUtil.isSDCardEnable() && FileUtil.deleteFile(context.getExternalCacheDir() + File.separator + name);
    }

}
