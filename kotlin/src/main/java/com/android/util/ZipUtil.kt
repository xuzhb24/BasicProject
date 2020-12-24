package com.android.util

import java.io.*
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream

/**
 * Created by xuzhb on 2020/12/23
 * Desc:压缩工具类
 */
object ZipUtil {

    private const val KB = 1024

    /**
     * 压缩文件
     *
     * @param srcFilePath  待压缩文件路径
     * @param destFilePath 压缩文件路径，如"sdcard/aaa/aa.zip"
     * @param comment      压缩文件的注释
     * @return true: 压缩成功  false: 压缩失败
     * @throws IOException IO错误时抛出
     */
    @Throws(IOException::class)
    fun zipFile(srcFilePath: String?, destFilePath: String?, comment: String? = null) =
        zipFile(FileUtil.getFileByPath(srcFilePath), FileUtil.getFileByPath(destFilePath), comment)

    /**
     * 压缩文件
     *
     * @param srcFile  待压缩文件
     * @param destFile 压缩文件
     * @param comment  压缩文件的注释
     * @return true: 压缩成功  false: 压缩失败
     * @throws IOException IO错误时抛出
     */
    @Throws(IOException::class)
    fun zipFile(srcFile: File?, destFile: File?, comment: String? = null): Boolean {
        if (srcFile == null || destFile == null) {
            return false
        }
        var zos: ZipOutputStream? = null
        return try {
            zos = ZipOutputStream(FileOutputStream(destFile))
            zipFile(srcFile, "", zos, comment)
        } finally {
            if (zos != null) {
                IOUtil.closeIO(zos)
            }
        }
    }

    /**
     * 压缩文件
     *
     * @param srcFile  待压缩文件
     * @param rootPath 相对于压缩文件的路径
     * @param zos      压缩文件输出流
     * @param comment  压缩文件的注释
     * @return true: 压缩成功  false: 压缩失败
     * @throws IOException IO错误时抛出
     */
    @Throws(IOException::class)
    private fun zipFile(srcFile: File?, rootPath: String?, zos: ZipOutputStream, comment: String?): Boolean {
        if (srcFile == null) {
            return false
        }
        val path = rootPath + (if (StringUtil.isEmpty(rootPath)) "" else File.separator) + srcFile.name
        if (srcFile.isDirectory) {
            val fileList = srcFile.listFiles()
            if (fileList == null || fileList.isEmpty()) {
                val entry = ZipEntry("$path/")
                if (!StringUtil.isEmpty(comment)) {
                    entry.comment = comment
                }
                zos.putNextEntry(entry)
                zos.closeEntry()
            } else {
                for (file in fileList) {
                    if (!zipFile(file, path, zos, comment)) {
                        return false
                    }
                }
            }
        } else {
            var inputStream: InputStream? = null
            try {
                inputStream = BufferedInputStream(FileInputStream(srcFile))
                val entry = ZipEntry(path)
                if (!StringUtil.isEmpty(comment)) {
                    entry.comment = comment
                }
                zos.putNextEntry(entry)
                val buffer = ByteArray(KB)
                var len: Int
                while (inputStream.read(buffer, 0, KB).also { len = it } != -1) {
                    zos.write(buffer, 0, len)
                }
                zos.closeEntry()
            } finally {
                IOUtil.closeIO(inputStream)
            }
        }
        return true
    }

    /**
     * 解压文件
     *
     * @param srcFilePath 待解压文件路径
     * @param destDirPath 目标目录路径
     * @return true: 解压成功  false: 解压失败
     * @throws IOException IO错误时抛出
     */
    @Throws(IOException::class)
    fun unzipFile(srcFilePath: String?, destDirPath: String?): Boolean =
        unzipFile(FileUtil.getFileByPath(srcFilePath), FileUtil.getFileByPath(destDirPath))

    /**
     * 解压文件
     *
     * @param srcFile 待解压文件
     * @param destDir 目标目录
     * @return true: 解压成功  false: 解压失败
     * @throws IOException IO错误时抛出
     */
    @Throws(IOException::class)
    fun unzipFile(srcFile: File?, destDir: File?): Boolean =
        unzipFileByKeyword(srcFile, destDir, null) != null

    /**
     * 解压带有关键字的文件
     *
     * @param srcFilePath 待解压文件路径
     * @param destDirPath 目标目录路径
     * @param keyword     关键字
     * @return 返回带有关键字的文件链表
     * @throws IOException IO错误时抛出
     */
    @Throws(IOException::class)
    fun unzipFileByKeyword(srcFilePath: String?, destFilePath: String?, keyword: String?): MutableList<File>? =
        unzipFileByKeyword(FileUtil.getFileByPath(srcFilePath), FileUtil.getFileByPath(destFilePath), keyword)

    /**
     * 解压带有关键字的文件
     *
     * @param srcFile 待解压文件
     * @param destDir 目标目录
     * @param keyword 关键字
     * @return 返回带有关键字的文件链表
     * @throws IOException IO错误时抛出
     */
    @Throws(IOException::class)
    fun unzipFileByKeyword(srcFile: File?, destDir: File?, keyword: String?): MutableList<File>? {
        if (srcFile == null || destDir == null) {
            return null
        }
        val files: MutableList<File> = mutableListOf()
        val zf = ZipFile(srcFile)
        val entries: Enumeration<*> = zf.entries()
        while (entries.hasMoreElements()) {
            val entry = entries.nextElement() as ZipEntry
            val entryName = entry.name
            if (StringUtil.isEmpty(keyword) || (FileUtil.getFileName(entryName) != null && FileUtil.getFileName(entryName)!!
                    .toLowerCase().contains(keyword!!.toLowerCase()))
            ) {
                val filePath = destDir.toString() + File.separator + entryName
                val file = File(filePath)
                files.add(file)
                if (entry.isDirectory) {
                    if (!FileUtil.createOrExistsDirectory(file)) {
                        return null
                    }
                } else {
                    if (!FileUtil.createOrExistsFile(file)) {
                        return null
                    }
                    var input: InputStream? = null
                    var output: OutputStream? = null
                    try {
                        input = BufferedInputStream(zf.getInputStream(entry))
                        output = BufferedOutputStream(FileOutputStream(file))
                        val buffer = ByteArray(KB)
                        var len: Int
                        while (input.read(buffer).also { len = it } != -1) {
                            output.write(buffer, 0, len)
                        }
                    } finally {
                        IOUtil.closeIO(input, output)
                    }
                }
            }
        }
        return files
    }
}