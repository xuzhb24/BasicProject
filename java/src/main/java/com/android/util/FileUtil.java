package com.android.util;

import java.io.*;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuzhb on 2020/2/6
 * Desc:文件管理类
 */
public class FileUtil {

    //获取操作系统对应的换行符，如java中的\r\n，windows中的\r\n，linux/unix中的\r，mac中的\n
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    //根据文件路径获取文件
    public static File getFileByPath(String filePath) {
        return StringUtil.isEmpty(filePath) ? null : new File(filePath);
    }

    //判断文件是否存在，不存在则判断是否创建成功
    public static boolean createOrExistsFile(String filePath) {
        return createOrExistsFile(getFileByPath(filePath));
    }

    //判断文件是否存在，不存在则判断是否创建成功
    public static boolean createOrExistsFile(File file) {
        if (file == null) {
            return false;
        }
        if (file.exists()) {  //如果存在，判断是否是文件
            return file.isFile();
        }
        if (!createOrExistsDirectory(file.getParentFile())) {
            return false;
        }
        try {
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    //判断目录是否存在，不存在则判断是否创建成功
    public static boolean createOrExistsDirectory(String dirPath) {
        return createOrExistsDirectory(getFileByPath(dirPath));
    }

    //判断目录是否存在，不存在则判断是否创建成功
    public static boolean createOrExistsDirectory(File file) {
        // 如果存在，是目录则返回true，是文件则返回false，不存在则返回是否创建成功
        return file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
    }

    //创建文件，如果文件已存在则删除重新创建
    public static boolean createFileAfterDeleteOldFile(String filePath) {
        return createFileAfterDeleteOldFile(getFileByPath(filePath));
    }

    //创建文件，如果文件已存在则删除重新创建，返回true：创建成功，false：创建失败
    public static boolean createFileAfterDeleteOldFile(File file) {
        if (file == null) {
            return false;
        }
        //如果文件已存在，则先删除，若删除失败返回false
        if (file.exists() && file.isFile() && !file.delete()) {
            return false;
        }
        //如果目录创建失败，返回false
        if (!createOrExistsDirectory(file.getParentFile())) {
            return false;
        }
        try {
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    //判断文件是否存在
    public static boolean isFileExists(String filePath) {
        return isFileExists(getFileByPath(filePath));
    }

    //判断文件是否存在
    public static boolean isFileExists(File file) {
        return file != null && file.exists();
    }

    //判断是否是文件
    public static boolean isFile(String filePath) {
        return isFile(getFileByPath(filePath));
    }

    //判断是否是文件
    public static boolean isFile(File file) {
        return isFileExists(file) && file.isFile();
    }

    //判断是否是目录
    public static boolean isDirectory(String filePath) {
        return isDirectory(getFileByPath(filePath));
    }

    //判断是否是目录
    public static boolean isDirectory(File file) {
        return isFileExists(file) && file.isDirectory();
    }

    //重命名文件
    public static boolean renameFile(String filePath, String newName) {
        return renameFile(getFileByPath(filePath), newName);
    }

    //重命名文件，返回true：重命名成功，false：重命名失败
    public static boolean renameFile(File file, String newName) {
        if (file == null || !file.exists() || StringUtil.isEmpty(newName)) {
            return false;
        }
        if (file.getName().equals(newName)) {
            return true;
        }
        File newFile = new File(file.getParent() + File.separator + newName);
        //如果重命名文件已存在则返回false
        return !newFile.exists() && file.renameTo(newFile);
    }

    //删除文件
    public static boolean deleteFile(String filePath) {
        return deleteFile(getFileByPath(filePath));
    }

    //删除文件，返回true：删除成功，false：删除失败
    public static boolean deleteFile(File file) {
        return file != null && (!file.exists() || file.isFile() && file.delete());
    }

    //删除目录
    public static boolean deleteDirectory(String dirPath) {
        return deleteDirectory(getFileByPath(dirPath));
    }

    //删除目录，返回true：删除成功，false：删除失败
    public static boolean deleteDirectory(File dir) {
        if (dir == null) {
            return false;
        }
        if (!dir.exists()) {
            return true;
        }
        if (!dir.isDirectory()) {
            return false;
        }
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (file.isFile()) {
                    if (!deleteFile(file)) {
                        return false;
                    }
                } else if (file.isDirectory()) {
                    if (!deleteDirectory(file)) {
                        return false;
                    }
                }
            }
        }
        return dir.delete();
    }

    //复制文件
    public static boolean copyFile(String srcFilePath, String destFilePath) {
        return copyFile(getFileByPath(srcFilePath), getFileByPath(destFilePath));
    }

    //复制文件
    public static boolean copyFile(File srcFile, File destFile) {
        return copyOrMoveFile(srcFile, destFile, false);
    }

    //移动文件
    public static boolean moveFile(String srcFilePath, String destFilePath) {
        return moveFile(getFileByPath(srcFilePath), getFileByPath(destFilePath));
    }

    //移动文件
    public static boolean moveFile(File srcFile, File destFile) {
        return copyOrMoveFile(srcFile, destFile, true);
    }

    //复制目录
    public static boolean copyDirectory(String srcDirPath, String destDirPath) {
        return copyDirectory(getFileByPath(srcDirPath), getFileByPath(destDirPath));
    }

    //复制目录
    public static boolean copyDirectory(File srcDir, File destDir) {
        return copyOrMoveDirectory(srcDir, destDir, false);
    }

    //移动目录
    public static boolean moveDirectory(String srcDirPath, String destDirPath) {
        return moveDirectory(getFileByPath(srcDirPath), getFileByPath(destDirPath));
    }

    //移动目录
    public static boolean moveDirectory(File srcDir, File destDir) {
        return copyOrMoveDirectory(srcDir, destDir, true);
    }

    /**
     * 复制或移动文件
     *
     * @param srcFile  源文件
     * @param destFile 目标文件
     * @param isMove   是否移动
     * @return true：复制或移动成功，false：复制或移动失败
     */
    private static boolean copyOrMoveFile(File srcFile, File destFile, boolean isMove) {
        if (srcFile == null || destFile == null) {
            return false;
        }
        //源文件不存在或不是文件则返回false
        if (!srcFile.exists() || !srcFile.isFile()) {
            return false;
        }
        //目标文件存在且是文件则返回false
        if (destFile.exists() && destFile.isFile()) {
            return false;
        }
        //目标目录不存在返回false
        if (!createOrExistsDirectory(destFile.getParentFile())) {
            return false;
        }
        try {
            return IOUtil.writeFileFromInputStream(destFile, new FileInputStream(srcFile), false)
                    && !(isMove && !deleteFile(srcFile));  //若要移动，则在复制后删除源文件
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 复制或移动目录
     *
     * @param srcDir  源文件
     * @param destDir 目标文件
     * @param isMove  是否移动
     * @return true：复制或移动成功，false：复制或移动失败
     */
    private static boolean copyOrMoveDirectory(File srcDir, File destDir, boolean isMove) {
        if (srcDir == null || destDir == null) {
            return false;
        }
        //为什么在后面加File.separator，是为了防止类似"test\dir1"和"test\dir"的误判
        String srcPath = srcDir.getPath() + File.separator;
        String destPath = destDir.getPath() + File.separator;
        if (destPath.contains(srcPath)) {
            return false;
        }
        // 源文件不存在或者不是目录则返回false
        if (!srcDir.exists() || !srcDir.isDirectory()) {
            return false;
        }
        // 目标目录不存在返回false
        if (!createOrExistsDirectory(destDir)) {
            return false;
        }
        File[] files = srcDir.listFiles();
        for (File file : files) {
            File tempDestFile = new File(destPath + file.getName());
            if (file.isFile()) {
                // 如果操作失败返回false
                if (!copyOrMoveFile(file, tempDestFile, isMove)) {
                    return false;
                }
            } else if (file.isDirectory()) {
                // 如果操作失败返回false
                if (!copyOrMoveDirectory(file, tempDestFile, isMove)) {
                    return false;
                }
            }
        }
        return !isMove || deleteDirectory(srcDir);
    }

    //删除目录下的所有文件，不包括根目录
    public static boolean deleteFilesInDirectory(String dirPath) {
        return deleteFilesInDirectory(getFileByPath(dirPath));
    }

    //删除目录下的所有文件，不包括根目录
    public static boolean deleteFilesInDirectory(File dir) {
        if (dir == null) {
            return false;
        }
        if (!dir.exists()) {
            return true;
        }
        if (!dir.isDirectory()) {
            return false;
        }
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (file.isFile()) {
                    if (!deleteFile(file)) {
                        return false;
                    }
                } else if (file.isDirectory()) {
                    if (!deleteDirectory(file)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    //获取目录下所有文件，包括目录和文件
    public static List<File> listFilesInDirectory(String dirPath, boolean isIncludeSubDir) {
        return listFilesInDirectory(getFileByPath(dirPath), isIncludeSubDir);
    }

    /**
     * 获取目录下所有文件，包括目录和文件
     *
     * @param dir             目录
     * @param isIncludeSubDir 是否包含子目录
     * @return 文件链表
     */
    public static List<File> listFilesInDirectory(File dir, boolean isIncludeSubDir) {
        if (dir == null || !isDirectory(dir)) {
            return null;
        }
        List<File> list = new ArrayList<>();
        File[] files = dir.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                list.add(file);
                if (isIncludeSubDir && file.isDirectory()) {
                    List<File> fileList = listFilesInDirectory(file, isIncludeSubDir);
                    if (fileList != null) {
                        list.addAll(fileList);
                    }
                }
            }
        }
        return list;
    }

    //获取目录下所有后缀名为suffix的文件，包括目录和文件
    public static List<File> listFilesInDirectoryEndWithSuffix(String dirPath, String suffix, boolean isIncludeSubDir, boolean isIgnoreCase) {
        return listFilesInDirectoryEndWithSuffix(getFileByPath(dirPath), suffix, isIncludeSubDir, isIgnoreCase);
    }

    /**
     * 获取目录下所有后缀名为suffix的文件，包括目录和文件
     *
     * @param dir             目录
     * @param suffix          后缀名
     * @param isIncludeSubDir 是否包含子目录
     * @param isIgnoreCase    是否忽略大小写
     * @return 文件链表
     */
    public static List<File> listFilesInDirectoryEndWithSuffix(File dir, String suffix, boolean isIncludeSubDir, boolean isIgnoreCase) {
        return listFilesInDirectoryWithFilter(dir, isIncludeSubDir, ((dirPath, name) -> {  //dirPath：文件路径，name：文件名称
            if (isIgnoreCase) {  //忽略大小写
                return name.toUpperCase().endsWith(suffix.toUpperCase());
            } else {  //区分大小写
                return name.endsWith(suffix);
            }
        }));
    }

    //获取目录下指定文件名的文件，包括目录和文件
    public static List<File> searchFileInDirectory(String dirPath, String fileName, boolean isIncludeSubDir, boolean isIgnoreCase) {
        return searchFileInDirectory(getFileByPath(dirPath), fileName, isIncludeSubDir, isIgnoreCase);
    }

    /**
     * 获取目录下指定文件名的文件，包括目录和文件
     *
     * @param dir             目录
     * @param fileName        文件名
     * @param isIncludeSubDir 是否包含子目录
     * @param isIgnoreCase    是否忽略大小写
     * @return 文件链表
     */
    public static List<File> searchFileInDirectory(File dir, String fileName, boolean isIncludeSubDir, boolean isIgnoreCase) {
        return listFilesInDirectoryWithFilter(dir, isIncludeSubDir, ((dirPath, name) -> {  //dirPath：文件路径，name：文件名称
            if (isIgnoreCase) {  //忽略大小写
                return name.toUpperCase().equals(fileName.toUpperCase());
            } else {  //区分大小写
                return name.equals(fileName);
            }
        }));
    }

    //获取目录下所有符合filter的文件，包括目录和文件
    public static List<File> listFilesInDirectoryWithFilter(String dirPath, boolean isIncludeSubDir, FilenameFilter filter) {
        return listFilesInDirectoryWithFilter(getFileByPath(dirPath), isIncludeSubDir, filter);
    }

    /**
     * 获取目录下所有符合filter的文件，包括目录和文件
     *
     * @param dir             目录
     * @param filter          过滤器
     * @param isIncludeSubDir 是否包含子目录
     * @return 文件链表
     */
    public static List<File> listFilesInDirectoryWithFilter(File dir, boolean isIncludeSubDir, FilenameFilter filter) {
        if (dir == null || !isDirectory(dir)) {
            return null;
        }
        List<File> list = new ArrayList<>();
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (filter.accept(file.getParentFile(), file.getName())) {
                    list.add(file);
                }
                if (isIncludeSubDir && file.isDirectory()) {
                    List<File> fileList = listFilesInDirectoryWithFilter(file, isIncludeSubDir, filter);
                    if (fileList != null) {
                        list.addAll(fileList);
                    }
                }
            }
        }
        return list;
    }

    //获取文件最后修改的毫秒时间戳
    public static long getFileLastModified(String filePath) {
        return getFileLastModified(getFileByPath(filePath));
    }

    //获取文件最后修改的毫秒时间戳
    public static long getFileLastModified(File file) {
        return file == null ? -1 : file.lastModified();
    }

    //获取文件编码格式
    public static String getFileCharset(String filePath) {
        return getFileCharset(getFileByPath(filePath));
    }

    //获取文件编码格式
    public static String getFileCharset(File file) {
        int p = 0;
        InputStream is = null;
        try {
            is = new BufferedInputStream(new FileInputStream(file));
            p = (is.read() << 8) + is.read();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtil.closeIO(is);
        }
        switch (p) {
            case 0xefbb:
                return "UTF-8";
            case 0xfffe:
                return "Unicode";
            case 0xfeff:
                return "UTF-16BE";
            default:
                return "GBK";
        }
    }

    //获取文件行数，返回-1表示读取失败，比readLine快
    public static int getFileLines(String filePath) {
        return getFileLines(getFileByPath(filePath));
    }

    //获取文件行数，返回-1表示读取失败，比readLine快
    public static int getFileLines(File file) {
        FileReader in = null;
        LineNumberReader reader = null;
        try {
            in = new FileReader(file);
            reader = new LineNumberReader(in);
            reader.skip(Long.MAX_VALUE);
            return reader.getLineNumber() + 1;  //实际上是读取换行符数量，而最后一行文本没有换行符，所以需要加1
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtil.closeIO(in, reader);
        }
        return -1;
    }

    //获取文件名
    public static String getFileName(String filePath) {
        if (StringUtil.isEmpty(filePath)) {
            return filePath;
        }
        int lastSep = filePath.lastIndexOf(File.separator);
        return lastSep == -1 ? filePath : filePath.substring(lastSep + 1);
    }

    //获取文件大小，以byte为单位
    public static long getFileSize(String filePath) {
        return getFileSize(getFileByPath(filePath));
    }

    //获取文件大小，以byte为单位
    public static long getFileSize(File file) {
        return isFile(file) ? file.length() : -1;
    }

    //获取文件大小，unit：字节单位，如b、kb、mb、gb等
    public static double getFileSize(String filePath, @ByteUnit.ByteUnitDef String unit) {
        return getFileSize(getFileByPath(filePath), unit);
    }

    //获取文件大小，unit：字节单位，如b、kb、mb、gb等
    public static double getFileSize(File file, @ByteUnit.ByteUnitDef String unit) {
        return isFile(file) ? ByteUnit.convertByteUnit(file.length(), unit) : -1;
    }

    //获取目录大小，以byte为单位
    public static long getDirectorySize(String dirPath) {
        return getDirectorySize(getFileByPath(dirPath));
    }

    //获取目录大小，以byte为单位
    public static long getDirectorySize(File dir) {
        if (!isDirectory(dir)) {
            return -1;
        }
        long size = 0;
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (file.isDirectory()) {
                    size += getDirectorySize(file);
                } else {
                    size += file.length();
                }
            }
        }
        return size;
    }

    //获取目录大小，unit：字节单位，如b、kb、mb、gb等
    public static double getDirectorySize(String filePath, @ByteUnit.ByteUnitDef String unit) {
        return getDirectorySize(getFileByPath(filePath), unit);
    }

    //获取目录大小，unit：字节单位，如b、kb、mb、gb等
    public static double getDirectorySize(File file, @ByteUnit.ByteUnitDef String unit) {
        return ByteUnit.convertByteUnit(getDirectorySize(file), unit);
    }

    //获取文件的MD5
    public static String getFileMD5String(String filePath) {
        return getFileMD5String(getFileByPath(filePath));
    }

    //获取文件的MD5
    public static String getFileMD5String(File file) {
        byte[] bytes = getFileMD5(file);
        if (bytes == null) {
            return null;
        }
        return TransformUtil.bytes2Hex(bytes);
    }

    //获取文件的MD5
    public static byte[] getFileMD5(String filePath) {
        return getFileMD5(getFileByPath(filePath));
    }

    //获取文件的MD5
    public static byte[] getFileMD5(File file) {
        if (file == null) {
            return null;
        }
        DigestInputStream dis = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            MessageDigest md = MessageDigest.getInstance("MD5");
            dis = new DigestInputStream(fis, md);
            byte[] buffer = new byte[1024 * 256];
            while (true) {
                if (!(dis.read(buffer) > 0)) break;
            }
            md = dis.getMessageDigest();
            return md.digest();
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        } finally {
            IOUtil.closeIO(dis);
        }
        return null;
    }

    public static void main(String[] args) {

        String path = "C:\\AATest\\Test\\test.txt";

        //删除后重新创建，测试createFileAfterDeleteOldFile，测试如下
        //1、先手动创建文件，再调用方法
        //2、如果文件已存在先手动删除文件，再调用方法
        //3、往文件写点什么，再调用方法，看调用前后是否变化
//        System.out.println("删除文件(" + path + ")后重建：" + createFileAfterDeleteOldFile(path));

        //文件是否存在，测试isFileExists
//        path = "C:\\AATest\\Test\\test.txt";
//        if (!isFileExists(path)) {
//            if (createOrExistsFile(path)) {
//                System.out.println("创建文件(" + path + ")成功");
//                System.out.println("文件(" + path + ")是否存在：" + isFileExists(path));
//            } else {
//                System.out.println("创建文件(" + path + ")失败");
//            }
//        } else {
//            System.out.println("文件(" + path + ")已存在");
//        }

        //是文件还是目录，测试isFile和isDirectory
//        System.out.println(isFile("C:\\AATest"));
//        System.out.println(isFile("C:\\AATest\\test1.txt"));
//        System.out.println(isDirectory("C:\\AATest"));
//        System.out.println(isDirectory("C:\\AATest\\test1.txt"));

        //重命名文件，测试renameFile
//        System.out.println("文件(" + path + ")重命名为testtest.txt：" + renameFile(path, "testtest.txt"));

        //删除文件或目录，测试deleteFile、deleteDirectory
//        path = "C:\\AATest\\Test\\test.txt";
//        System.out.println("删除文件(" + path + ")：" + deleteFile(path));
//        path = "C:\\AATest\\Test";
//        System.out.println("删除目录(" + path + ")：" + deleteDirectory(path));

        //复制文件，测试copyFile
        path = "C:\\AATest\\Test\\test.txt";
        String copyOrMovePath = "C:\\AATest\\TestTest\\t.txt";
//        if (createOrExistsFile(path) && deleteFile(copyOrMovePath)) {
//            sleep(5);
//            System.out.println("复制文件(" + path + ")到" + copyOrMovePath + "：" + copyFile(path, copyOrMovePath));
//        }

        //移动文件，测试moveFile
//        if (createOrExistsFile(path) && deleteFile(copyOrMovePath)) {
//            sleep(5);
//            System.out.println("移动文件(" + path + ")到" + copyOrMovePath + "：" + moveFile(path, copyOrMovePath));
//        }

        //复制目录，测试copyDirectory
        path = "C:\\AATest\\Test2";
        copyOrMovePath = "C:\\AATest\\Test3";
//        createTestFile();
//        if (createOrExistsDirectory(path) && deleteDirectory(copyOrMovePath)) {
//            sleep(5);
//            System.out.println("复制目录(" + path + ")到" + copyOrMovePath + "：" + copyDirectory(path, copyOrMovePath));
//        }

        //移动目录，测试moveDirectory
//        createTestFile();
//        if (createOrExistsDirectory(path) && deleteDirectory(copyOrMovePath)) {
//            sleep(5);
//            System.out.println("移动目录(" + path + ")到" + copyOrMovePath + "：" + moveDirectory(path, copyOrMovePath));
//        }

        //删除目录下的文件，不包括目录，测试deleteFilesInDirectory
//        createTestFile();
//        sleep(5);
//        System.out.println(deleteFilesInDirectory("C:\\AATest"));

        //列举目录下文件，测试listFilesInDirectory
        createTestFile();
        path = "C:\\AATest";
//        printFileList(listFilesInDirectory(path, false));
//        printFileList(listFilesInDirectory(path, true));
//        printFileList(listFilesInDirectory(path + "\\test", true));

        //列举目录下带有后缀的文件，测试listFilesInDirectoryEndWithSuffix
//        String suffix = "test1.txt";
//        System.out.println("\n不包含子目录，区分大小写(" + path + "，" + suffix + ")");
//        printFileList(listFilesInDirectoryEndWithSuffix(path, suffix, false, false));
//        System.out.println("\n不包含子目录，不区分大小写(" + path + "，" + suffix + ")");
//        printFileList(listFilesInDirectoryEndWithSuffix(path, suffix, false, true));
//        System.out.println("\n包含子目录，区分大小写(" + path + "，" + suffix + ")");
//        printFileList(listFilesInDirectoryEndWithSuffix(path, suffix, true, false));
//        System.out.println("\n包含子目录，不区分大小写(" + path + "，" + suffix + ")");
//        printFileList(listFilesInDirectoryEndWithSuffix(path, suffix, true, true));

        //查找文件
//        String fileName = "test1.txt";
//        System.out.println("\n不包含子目录，区分大小写(" + path + "，" + fileName + ")");
//        printFileList(searchFileInDirectory(path, fileName, false, false));
//        System.out.println("\n不包含子目录，不区分大小写(" + path + "，" + fileName + ")");
//        printFileList(searchFileInDirectory(path, fileName, false, true));
//        System.out.println("\n包含子目录，区分大小写(" + path + "，" + fileName + ")");
//        printFileList(searchFileInDirectory(path, fileName, true, false));
//        System.out.println("\n包含子目录，不区分大小写(" + path + "，" + fileName + ")");
//        printFileList(searchFileInDirectory(path, fileName, true, true));

        //获取文件最后修改时间，文件编码格式，文件行数，文件大小，MD5值
        path = "C:\\AATest\\Test\\test.txt";
        String content = "1\n2\n3\n4\n5\n6\n7\n8\n9\n10";
        IOUtil.writeFileFromString(path, content, false);
        sleep(10);
        System.out.println("文件(" + path + ")最后修改时间：" + DateUtil.long2String(getFileLastModified(path), DateUtil.Y_M_D_H_M_S_S));
        System.out.println("文件(" + path + ")编码格式：" + getFileCharset(path));
        System.out.println("文件(" + path + ")行数：" + getFileLines(path));
        System.out.println("文件(" + path + ")大小：" + getFileSize(path));
        //cmd命令查看MD5：certutil -hashfile 要查看文件路径 MD5
        System.out.println("文件(" + path + ")MD5：" + getFileMD5String(path));
        //获取目录大小
        path = "C:\\AAAAA";
        System.out.println("目录(" + path + ")大小：" + getDirectorySize(path) + "字节，" + getDirectorySize(path, ByteUnit.MB) + "mb");

    }

    /*
     * 测试目录
     * C\AATest
     * C\AATest\test1.txt
     * C\AATest\Test1.txt
     * C\AATest\TEST1.txt
     * C\AATest\test2.txt
     * C\AATest\test3.txt
     *
     * C:\AATest\Test1
     * C:\AATest\Test1\test1.txt
     * C:\AATest\Test1\test2.txt
     *
     * C:\AATest\Test2
     * C:\AATest\Test2\aaa.rar
     * C:\AATest\Test2\bbb.zip
     * C:\AATest\Test2\ccc.xml
     */
    private static void createTestFile() {
        String[] filePaths = new String[]{
                //AATest目录
                "C:\\AATest\\test1.txt", "C:\\AATest\\aTest1.txt", "C:\\AATest\\bTEST1.txt",
                "C:\\AATest\\test2.txt", "C:\\AATest\\test3.txt",
                //AATest\Test1目录
                "C:\\AATest\\Test1\\test1.txt", "C:\\AATest\\Test1\\test2.txt",
                //AATest\Test2目录
                "C:\\AATest\\Test2\\Test1.txt", "C:\\AATest\\Test2\\bbb.zip", "C:\\AATest\\Test2\\ccc.xml"
        };

        //创建文件、测试createFile、createOrExistsFile、createOrExistsDirectory
        for (String path : filePaths) {
            createOrExistsFile(path);
        }
    }

    private static void sleep(long seccond) {
        try {
            Thread.sleep(seccond * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void printFileList(List<File> list) {
        System.out.println("==============================================");
        if (list != null && list.size() > 0) {
            for (File file : list) {
                System.out.println(file.getAbsolutePath());
            }
        } else {
            System.out.println("未查询到数据");
        }
    }

}
