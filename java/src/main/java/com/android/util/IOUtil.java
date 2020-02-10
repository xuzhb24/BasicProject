package com.android.util;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by xuzhb on 2020/2/6
 * Desc:IO流相关工具类
 */
public class IOUtil {

    //获取操作系统对应的换行符，如java中的\r\n，windows中的\r\n，linux/unix中的\r，mac中的\n
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    //将输入流写入文件
    public static boolean writeFileFromInputStream(String filePath, InputStream is, boolean append) {
        return writeFileFromInputStream(FileUtil.getFileByPath(filePath), is, append);
    }

    /**
     * 将输入流写入文件
     *
     * @param file   文件
     * @param is     输入流
     * @param append 是否追加在文件末
     * @return true：写入成功，false：写入失败
     */
    public static boolean writeFileFromInputStream(File file, InputStream is, boolean append) {
        if (!FileUtil.createOrExistsFile(file) || is == null) {
            return false;
        }
        OutputStream os = null;
        try {
            os = new BufferedOutputStream(new FileOutputStream(file, append));
            byte data[] = new byte[1024];
            int len;
            while ((len = is.read(data, 0, 1024)) != -1) {
                os.write(data, 0, len);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeIO(is, os);
        }
    }

    //将字符串写入文件
    public static boolean writeFileFromString(String filePath, String content, boolean append) {
        return writeFileFromString(FileUtil.getFileByPath(filePath), content, append);
    }

    /**
     * 将字符串写入文件
     *
     * @param file    文件
     * @param content 字符串内容
     * @param append  是否追加在文件末
     * @return true：写入成功，false：写入失败
     */
    public static boolean writeFileFromString(File file, String content, boolean append) {
        if (!FileUtil.createOrExistsFile(file) || StringUtil.isEmpty(content)) {
            return false;
        }
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(file, append));
            bw.write(content);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeIO(bw);
        }
    }

    //读取文件到字符串中
    public static String readFileToString(String filePath) {
        return readFileToString(FileUtil.getFileByPath(filePath), null);
    }

    //读取文件到字符串中
    public static String readFileToString(String filePath, String charsetName) {
        return readFileToString(FileUtil.getFileByPath(filePath), charsetName);
    }

    //读取文件到字符串中
    public static String readFileToString(File file) {
        return readFileToString(file, null);
    }

    /**
     * 读取文件到字符串中
     *
     * @param file        文件
     * @param charsetName 编码格式
     * @return 字符串
     */
    public static String readFileToString(File file, String charsetName) {
        if (!FileUtil.isFileExists(file)) {
            return null;
        }
        BufferedReader reader = null;
        try {
            StringBuilder sb = new StringBuilder();
            if (StringUtil.isEmpty(charsetName)) {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            } else {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), charsetName));
            }
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append(LINE_SEPARATOR);
            }
            //删除最后的换行符
            return sb.delete(sb.length() - LINE_SEPARATOR.length(), sb.length()).toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            closeIO(reader);
        }
    }

    //将字节数组写入文件
    public static boolean writeFileFromBytes(String filePath, byte[] bytes, boolean append) {
        return writeFileFromBytes(FileUtil.getFileByPath(filePath), bytes, append);
    }

    /**
     * 将字节数组写入文件
     *
     * @param file   文件
     * @param bytes  字节数组
     * @param append 是否追加在文件末
     * @return true：写入成功，false：写入失败
     */
    public static boolean writeFileFromBytes(File file, byte[] bytes, boolean append) {
        if (!FileUtil.createOrExistsFile(file) || bytes == null) {
            return false;
        }
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(file, append));
            bos.write(bytes);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeIO(bos);
        }
    }

    //读取文件到字节数组中
    public static byte[] readFileToBytes(String filePath) {
        return readFileToBytes(FileUtil.getFileByPath(filePath));
    }

    //读取文件到字节数组中
    public static byte[] readFileToBytes(File file) {
        if (!FileUtil.isFileExists(file)) {
            return null;
        }
        FileInputStream fis = null;
        ByteArrayOutputStream os = null;
        try {
            fis = new FileInputStream(file);
            os = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int len;
            while ((len = fis.read(b, 0, 1024)) != -1) {
                os.write(b, 0, len);
            }
            return os.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            closeIO(fis, os);
        }
    }

    //将字节数组写入文件，使用FileChannel
    public static boolean writeFileFromBytesByFileChannel(String filePath, final byte[] bytes, boolean isForce) {
        return writeFileFromBytesByFileChannel(FileUtil.getFileByPath(filePath), bytes, isForce);
    }

    /**
     * 将字节数组写入文件，使用FileChannel
     *
     * @param file    文件
     * @param bytes   字节数组
     * @param isForce 是否立即写入磁盘
     * @return true：写入成功，false：写入失败
     */
    public static boolean writeFileFromBytesByFileChannel(File file, final byte[] bytes, boolean isForce) {
        if (!FileUtil.createFileAfterDeleteOldFile(file) || bytes == null) {  //写入文件前先删除文件，如果存在的话
            return false;
        }
        FileChannel fc = null;
        try {
            fc = new RandomAccessFile(file, "rw").getChannel();
            fc.position(fc.size());
            fc.write(ByteBuffer.wrap(bytes));
            if (isForce) {
                fc.force(true);   //立即写入磁盘
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeIO(fc);
        }
    }

    //读取文件到字节数组中，使用FileChannel
    public static byte[] readFileToBytesByFileChannel(String filePath) {
        return readFileToBytesByFileChannel(FileUtil.getFileByPath(filePath));
    }

    //读取文件到字节数组中，使用FileChannel
    public static byte[] readFileToBytesByFileChannel(File file) {
        if (!FileUtil.isFileExists(file)) {
            return null;
        }
        FileChannel fc = null;
        try {
            fc = new RandomAccessFile(file, "r").getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate((int) fc.size());
            while (true) {
                if (!((fc.read(byteBuffer)) > 0)) break;
            }
            return byteBuffer.array();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            closeIO(fc);
        }
    }

    //将字节数组写入文件，使用MappedByteBuffer
    public static boolean writeFileFromBytesByMapp(String filePath, final byte[] bytes, boolean isForce) {
        return writeFileFromBytesByMapp(FileUtil.getFileByPath(filePath), bytes, isForce);
    }

    /**
     * 将字节数组写入文件，使用MappedByteBuffer
     *
     * @param file    文件
     * @param bytes   字节数组
     * @param isForce 是否立即写入磁盘
     * @return true：写入成功，false：写入失败
     */
    public static boolean writeFileFromBytesByMapp(File file, final byte[] bytes, boolean isForce) {
        if (!FileUtil.createFileAfterDeleteOldFile(file) || bytes == null) {  //写入文件前先删除文件，如果存在的话
            return false;
        }
        FileChannel fc = null;
        try {
            fc = new RandomAccessFile(file, "rw").getChannel();
            MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_WRITE, fc.size(), bytes.length);
            mbb.put(bytes);
            if (isForce) {
                mbb.force();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeIO(fc);
        }
    }

    //读取文件到字节数组中，使用MappedByteBuffer
    public static byte[] readFileToBytesByMapp(String filePath) {
        return readFileToBytesByMapp(FileUtil.getFileByPath(filePath));
    }

    //读取文件到字节数组中，使用MappedByteBuffer
    public static byte[] readFileToBytesByMapp(File file) {
        if (!FileUtil.isFileExists(file)) {
            return null;
        }
        FileChannel fc = null;
        try {
            fc = new RandomAccessFile(file, "r").getChannel();
            int size = (int) fc.size();
            MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_ONLY, 0, size).load();
            byte[] result = new byte[size];
            mbb.get(result, 0, size);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            closeIO(fc);
        }
    }

    //关闭IO流
    public static void closeIO(Closeable... closeables) {
        if (closeables != null) {
            for (Closeable closeable : closeables) {
                if (closeable != null) {
                    try {
                        closeable.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
