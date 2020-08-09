package com.android.util

import java.io.File

/**
 * Created by xuzhb on 2020/8/9
 * Desc:文件管理类
 */
object FileUtil {

    //根据文件路径获取文件
    fun getFileByPath(filePath: String?): File? {
        return if (StringUtil.isEmpty(filePath)) null else File(filePath)
    }

    //删除文件，返回true：删除成功，false：删除失败
    fun deleteFile(file: File?): Boolean {
        return file != null && (!file.exists() || file.isFile && file.delete())
    }

    //删除目录
    fun deleteDirectory(dirPath: String): Boolean {
        return deleteDirectory(getFileByPath(dirPath))
    }

    //删除目录，返回true：删除成功，false：删除失败
    fun deleteDirectory(dir: File?): Boolean {
        if (dir == null) {
            return false
        }
        if (!dir.exists()) {
            return true
        }
        if (!dir.isDirectory) {
            return false
        }
        val files = dir.listFiles()
        if (files != null && files.size != 0) {
            for (file in files) {
                if (file.isFile) {
                    if (!deleteFile(file)) {
                        return false
                    }
                } else if (file.isDirectory) {
                    if (!deleteDirectory(file)) {
                        return false
                    }
                }
            }
        }
        return dir.delete()
    }

}