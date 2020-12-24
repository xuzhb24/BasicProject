package com.android.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * Created by xuzhb on 2020/6/1
 * Desc:压缩工具类
 */
public class ZipUtil {

    private static final int KB = 1024;

    //压缩文件
    public static boolean zipFile(String srcFilePath, String destFilePath) throws IOException {
        return zipFile(FileUtil.getFileByPath(srcFilePath), FileUtil.getFileByPath(destFilePath), null);
    }

    /**
     * 压缩文件
     *
     * @param srcFilePath  待压缩文件路径
     * @param destFilePath 压缩文件路径，如"sdcard/aaa/aa.zip"
     * @param comment      压缩文件的注释
     * @return true: 压缩成功  false: 压缩失败
     * @throws IOException IO错误时抛出
     */
    public static boolean zipFile(String srcFilePath, String destFilePath, String comment) throws IOException {
        return zipFile(FileUtil.getFileByPath(srcFilePath), FileUtil.getFileByPath(destFilePath), comment);
    }

    //压缩文件
    public static boolean zipFile(File srcFile, File destFile) throws IOException {
        return zipFile(srcFile, destFile, null);
    }

    /**
     * 压缩文件
     *
     * @param srcFile  待压缩文件
     * @param destFile 压缩文件
     * @param comment  压缩文件的注释
     * @return true: 压缩成功  false: 压缩失败
     * @throws IOException IO错误时抛出
     */
    public static boolean zipFile(File srcFile, File destFile, String comment) throws IOException {
        if (srcFile == null || destFile == null) return false;
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(new FileOutputStream(destFile));
            return zipFile(srcFile, "", zos, comment);
        } finally {
            if (zos != null) {
                IOUtil.closeIO(zos);
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
    private static boolean zipFile(File srcFile, String rootPath, ZipOutputStream zos, String comment) throws IOException {
        if (srcFile == null) return false;
        rootPath = rootPath + (StringUtil.isEmpty(rootPath) ? "" : File.separator) + srcFile.getName();
        if (srcFile.isDirectory()) {
            File[] fileList = srcFile.listFiles();
            if (fileList == null || fileList.length <= 0) {
                ZipEntry entry = new ZipEntry(rootPath + '/');
                if (!StringUtil.isEmpty(comment)) entry.setComment(comment);
                zos.putNextEntry(entry);
                zos.closeEntry();
            } else {
                for (File file : fileList) {
                    if (!zipFile(file, rootPath, zos, comment)) return false;
                }
            }
        } else {
            InputStream is = null;
            try {
                is = new BufferedInputStream(new FileInputStream(srcFile));
                ZipEntry entry = new ZipEntry(rootPath);
                if (!StringUtil.isEmpty(comment)) entry.setComment(comment);
                zos.putNextEntry(entry);
                byte buffer[] = new byte[KB];
                int len;
                while ((len = is.read(buffer, 0, KB)) != -1) {
                    zos.write(buffer, 0, len);
                }
                zos.closeEntry();
            } finally {
                IOUtil.closeIO(is);
            }
        }
        return true;
    }

    /**
     * 解压文件
     *
     * @param srcFilePath 待解压文件路径
     * @param destDirPath 目标目录路径
     * @return true: 解压成功  false: 解压失败
     * @throws IOException IO错误时抛出
     */
    public static boolean unzipFile(String srcFilePath, String destDirPath) throws IOException {
        return unzipFile(FileUtil.getFileByPath(srcFilePath), FileUtil.getFileByPath(destDirPath));
    }

    /**
     * 解压文件
     *
     * @param srcFile 待解压文件
     * @param destDir 目标目录
     * @return true: 解压成功  false: 解压失败
     * @throws IOException IO错误时抛出
     */
    public static boolean unzipFile(File srcFile, File destDir) throws IOException {
        return unzipFileByKeyword(srcFile, destDir, null) != null;
    }

    /**
     * 解压带有关键字的文件
     *
     * @param srcFilePath 待解压文件路径
     * @param destDirPath 目标目录路径
     * @param keyword     关键字
     * @return 返回带有关键字的文件链表
     * @throws IOException IO错误时抛出
     */
    public static List<File> unzipFileByKeyword(String srcFilePath, String destDirPath, String keyword) throws IOException {
        return unzipFileByKeyword(FileUtil.getFileByPath(srcFilePath), FileUtil.getFileByPath(destDirPath), keyword);
    }

    /**
     * 解压带有关键字的文件
     *
     * @param srcFile 待解压文件
     * @param destDir 目标目录
     * @param keyword 关键字
     * @return 返回带有关键字的文件链表
     * @throws IOException IO错误时抛出
     */
    public static List<File> unzipFileByKeyword(File srcFile, File destDir, String keyword) throws IOException {
        if (srcFile == null || destDir == null) return null;
        List<File> files = new ArrayList<>();
        ZipFile zf = new ZipFile(srcFile);
        Enumeration<?> entries = zf.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = ((ZipEntry) entries.nextElement());
            String entryName = entry.getName();
            if (StringUtil.isEmpty(keyword) || FileUtil.getFileName(entryName).toLowerCase().contains(keyword.toLowerCase())) {
                String filePath = destDir + File.separator + entryName;
                File file = new File(filePath);
                files.add(file);
                if (entry.isDirectory()) {
                    if (!FileUtil.createOrExistsDirectory(file)) return null;
                } else {
                    if (!FileUtil.createOrExistsFile(file)) return null;
                    InputStream in = null;
                    OutputStream out = null;
                    try {
                        in = new BufferedInputStream(zf.getInputStream(entry));
                        out = new BufferedOutputStream(new FileOutputStream(file));
                        byte buffer[] = new byte[KB];
                        int len;
                        while ((len = in.read(buffer)) != -1) {
                            out.write(buffer, 0, len);
                        }
                    } finally {
                        IOUtil.closeIO(in, out);
                    }
                }
            }
        }
        return files;
    }

    public static void main(String[] args) throws IOException {
        createTestFile();
        FileUtil.createOrExistsDirectory("D:\\AAZip");
        if (zipFile("D:\\AATest", "D:\\AAZip\\AATest.zip", null)) {
            unzipFile("D:\\AAZip\\AATest.zip", "D:\\AAZip\\AA1");
            unzipFileByKeyword("D:\\AAZip\\AATest.zip", "D:\\AAZip\\AA2", "test2.txt");
        } else {
            System.out.println("文件压缩失败");
        }
    }

    private static void createTestFile() {
        String[] filePaths = new String[]{
                //AATest目录
                "D:\\AATest\\test1.txt", "D:\\AATest\\aTest1.txt", "D:\\AATest\\bTEST1.txt",
                "D:\\AATest\\test2.txt", "D:\\AATest\\test3.txt",
                //AATest\Test1目录
                "D:\\AATest\\Test1\\test1.txt", "D:\\AATest\\Test1\\test2.txt",
                //AATest\Test2目录
                "D:\\AATest\\Test2\\Test1.txt", "D:\\AATest\\Test2\\bbb.zip", "D:\\AATest\\Test2\\ccc.xml"
        };

        //创建文件、测试createFile、createOrExistsFile、createOrExistsDirectory
        for (String path : filePaths) {
            FileUtil.createOrExistsFile(path);
        }
    }

}
