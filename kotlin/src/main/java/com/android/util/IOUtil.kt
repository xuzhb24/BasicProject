package com.android.util

import java.io.*

/**
 * Created by xuzhb on 2020/2/20
 * Desc:
 */
object IOUtil {

    //获取操作系统对应的换行符，如java中的\r\n，windows中的\r\n，linux/unix中的\r，mac中的\n
    private val LINE_SEPARATOR = System.getProperty("line.separator")

    //读取InputStream到字符串中
    fun readInputStreameToString(inputStream: InputStream) = readInputStreameToString(inputStream, "")

    /**
     * 读取InputStream到字符串中
     *
     * @param inputStream 输出流
     * @param charsetName 编码格式
     * @return 字符串
     */
    fun readInputStreameToString(inputStream: InputStream, charsetName: String): String? {
        var reader: BufferedReader? = null
        try {
            val sb = StringBuilder()
            if (StringUtil.isEmpty(charsetName)) {
                reader = BufferedReader(InputStreamReader(inputStream))
            } else {
                reader = BufferedReader(InputStreamReader(inputStream, charsetName))
            }
            do {
                val line = reader.readLine()
                if (line != null) {
                    sb.append(line).append(LINE_SEPARATOR)
                } else {
                    break
                }
            } while (true)
            //删除最后的换行符
            return sb.delete(sb.length - LINE_SEPARATOR.length, sb.length).toString()
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        } finally {
            closeIO(reader)
        }
    }

    //关闭IO流
    fun closeIO(vararg closeables: Closeable?) {
        for (closeable in closeables) {
            closeable?.let {
                try {
                    it.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

}