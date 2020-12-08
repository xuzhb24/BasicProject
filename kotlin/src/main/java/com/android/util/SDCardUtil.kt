package com.android.util

import android.annotation.SuppressLint
import android.os.Environment
import android.os.StatFs

/**
 * Created by xuzhb on 2020/12/8
 * Desc:SD卡工具类
 */
@SuppressLint("NewApi")
object SDCardUtil {

    //判断SD卡是否可用
    fun isSDCardEnable() = Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()

    //获取SD卡路径，如/storage/emulated/0
    fun getSDCardPath() = Environment.getExternalStorageDirectory().path

    //获取SD卡下文件目录路径，如/storage/emulated/0/Documents
    fun getDocumentPath() = getDirectoryPathInSDCard(Environment.DIRECTORY_DOCUMENTS)

    //获取SD卡下下载目录路径，如/storage/emulated/0/Download
    fun getDownloadPath() = getDirectoryPathInSDCard(Environment.DIRECTORY_DOWNLOADS)

    //获取SD卡下影视目录路径，如/storage/emulated/0/Movies
    fun getMoviePath() = getDirectoryPathInSDCard(Environment.DIRECTORY_MOVIES)

    //获取SD卡下音乐目录路径，如/storage/emulated/0/Music
    fun getMusicPath() = getDirectoryPathInSDCard(Environment.DIRECTORY_MUSIC)

    //获取SD卡下图片目录路径，如/storage/emulated/0/Pictures
    fun getPicturePath() = getDirectoryPathInSDCard(Environment.DIRECTORY_PICTURES)

    //获取SD卡下指定目录的路径
    fun getDirectoryPathInSDCard(dirPath: String) = Environment.getExternalStoragePublicDirectory(dirPath).path

    //获取SD卡总存储空间，以byte为单位
    fun getTotalSize(): Long {
        if (!isSDCardEnable()) {
            return -1
        }
        val statFs = StatFs(getSDCardPath())
        return statFs.blockCountLong * statFs.blockSizeLong
    }

    //获取SD卡剩余空间，以byte为单位
    fun getAvailableSize(): Long {
        if (!isSDCardEnable()) {
            return -1
        }
        val statFs = StatFs(getSDCardPath())
        return statFs.availableBlocksLong * statFs.blockSizeLong
    }

    //获取SD卡已用空间，以byte为单位
    fun getUsedSize() = if (!isSDCardEnable()) -1 else getTotalSize() - getAvailableSize()

    /*
     * 获取SD卡信息
     * API小于18
     * getAvailableBlocks()：文件系统中可被应用程序使用的空闲存储区块的数量
     * getFreeBlocks()：文件系统中的空闲存储区块总数，包括保留的存储区块（不能被普通应用程序使用）
     * getBlockCount()：文件系统中的存储区块总数
     * getBlockSize()：文件系统中每个存储区块的字节数
     * API大于大于18
     * getAvailableBlocksLong()：文件系统中可被应用程序使用的空闲存储区块的数量
     * getAvailableBytes()：文件系统中可被应用程序使用的空闲字节数
     * getFreeBlocksLong()：文件系统中的空闲存储区块总数，包括保留的存储区块（不能被普通应用程序使用）
     * getFreeBytes()：文件系统中总的空闲字节数，包括保留的存储区块（不能被普通应用程序使用）
     * getBlockCountLong()：文件系统中的存储区块总数
     * getBlockSizeLong()：文件系统中每个存储区块的字节数
     * getTotalBytes()：文件系统支持的总的存储字节数
     */
    fun getSDCardInfo(): StatFs? {
        if (!isSDCardEnable()) {
            return null
        }
        return StatFs(getSDCardPath())
    }

}