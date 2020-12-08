package com.android.util

import android.content.Context
import java.io.File

/**
 * Created by xuzhb on 2020/12/8
 * Desc:应用文件清除工具类
 */
object CleanUtil {

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
    fun cleanInternalCache(context: Context) = FileUtil.deleteFilesInDirectory(context.cacheDir)

    //根据名称清除内部缓存，即/data/data/com.xxx.xxx/cache目录下指定文件
    fun cleanInternalCacheByName(context: Context, name: String) = FileUtil.deleteFile(context.cacheDir.toString() + File.separator + name)

    //清除内部数据库，即/data/data/com.xxx.xxx/databases目录下所有文件
    fun cleanInternalDatabase(context: Context) = FileUtil.deleteFilesInDirectory(context.filesDir.parent + File.separator + "databases")

    //根据名称清除数据库，如/data/data/com.xxx.xxx/databases目录下指定数据库
    fun cleanInternalDatabaseByName(context: Context, name: String) = context.deleteDatabase(name)

    //清除内部文件，即/data/data/com.xxx.xxx/files目录下所有文件
    fun cleanInternalFiles(context: Context) = FileUtil.deleteFilesInDirectory(context.filesDir)

    //根据名称清除内部文件，即/data/data/com.xxx.xxx/files目录下指定文件
    fun cleanInternalFilesByName(context: Context, name: String) = FileUtil.deleteFile(context.filesDir.toString() + File.separator + name)

    //清除内部SharePreference，即/data/data/com.xxx.xxx/shared_prefs目录下所有文件
    fun cleanInternalSharePreference(context: Context) = FileUtil.deleteFilesInDirectory(context.filesDir.parent + File.separator + "shared_prefs")

    //根据名称清除内部SharePreference，即/data/data/com.xxx.xxx/shared_prefs目录下指定文件
    fun cleanInternalSharePreferenceByName(context: Context, name: String) =
        FileUtil.deleteFile(context.filesDir.parent + File.separator + "shared_prefs" + File.separator + name)

    //清除外部缓存，即/storage/emulated/0/android/data/com.xxx.xxx/cache目录下所有文件
    fun cleanExternalCache(context: Context) = SDCardUtil.isSDCardEnable() && FileUtil.deleteFilesInDirectory(context.externalCacheDir)

    //根据名称清除外部缓存，即/storage/emulated/0/android/data/com.xxx.xxx/cache目录下指定文件
    fun cleanExternalCacheByName(context: Context, name: String) =
        SDCardUtil.isSDCardEnable() && FileUtil.deleteFile(context.externalCacheDir.toString() + File.separator + name)

}