package com.android.util

import android.content.Context
import android.text.format.Formatter
import com.android.base.BaseApplication
import java.io.*
import java.util.regex.Pattern

/**
 * Created by xuzhb on 2021/5/6
 * Desc:CPU工具类
 */
object CPUUtil {

    //获取处理器的Java虚拟机的数量
    fun getProcessorsCount(): Int = Runtime.getRuntime().availableProcessors()

    //获取手机CPU序列号(16位)
    fun getCPUSerial(): String {
        var str: String = ""
        var cpuSerial = "0000000000000000"
        try {
            //读取CPU信息
            val pp = Runtime.getRuntime().exec("cat/proc/cpuinfo")
            val isr = InputStreamReader(pp.inputStream)
            val input = LineNumberReader(isr)
            //查找CPU序列号
            for (i in 1..99) {
                str = input.readLine()
                if (str != null) {
                    //查找到序列号所在行
                    if (str.contains("Serial")) {
                        //提取序列号
                        cpuSerial = str.substring(str.indexOf(':') + 1).trim()
                        break
                    }
                } else {
                    break
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return cpuSerial
    }

    //获取CPU信息
    fun getCpuInfo(): String {
        try {
            val fr = FileReader("/proc/cpuinfo")
            val br = BufferedReader(fr)
            return br.readLine()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    //获取CPU型号
    fun getCpuModel(): String {
        try {
            val fr = FileReader("/proc/cpuinfo")
            val br = BufferedReader(fr)
            val text = br.readLine()
            return text.split(":\\s+".toRegex(), 2).toTypedArray()[1]
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    //获取CPU最大频率(单位KHZ)
    fun getMaxCpuFrequency(context: Context = BaseApplication.instance): String {
        val cmd: ProcessBuilder
        var inputStream: InputStream? = null
        try {
            val builder = StringBuilder()
            val args = arrayOf("/system/bin/cat", "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq")
            cmd = ProcessBuilder(*args)
            val process = cmd.start()
            inputStream = process.inputStream
            val re = ByteArray(24)
            while (inputStream.read(re) != -1) {
                builder.append(String(re))
            }
            return Formatter.formatFileSize(context.applicationContext, builder.toString().trim().toLong() * 1024) + " Hz"
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            IOUtil.closeIO(inputStream)
        }
        return "unknown"
    }

    //获取CPU最小频率(单位KHZ)
    fun getMinCpuFrequency(context: Context = BaseApplication.instance): String {
        val cmd: ProcessBuilder
        var inputStream: InputStream? = null
        try {
            val builder = StringBuilder()
            val args = arrayOf("/system/bin/cat", "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq")
            cmd = ProcessBuilder(*args)
            val process = cmd.start()
            inputStream = process.inputStream
            val re = ByteArray(24)
            while (inputStream.read(re) != -1) {
                builder.append(String(re))
            }
            return Formatter.formatFileSize(context.applicationContext, builder.toString().trim().toLong() * 1024) + " Hz"
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            IOUtil.closeIO(inputStream)
        }
        return "unknown"
    }

    //获取CPU当前频率(单位KHZ)
    fun getCurCpuFrequency(context: Context = BaseApplication.instance): String {
        try {
            val fr = FileReader("/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq")
            val br = BufferedReader(fr)
            val text = br.readLine()
            return Formatter.formatFileSize(context.applicationContext, text.trim().toLong() * 1024) + " Hz"
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return "unknown"
    }

    //获取CPU核心数
    fun getCoreNumbers(): Int {
        var numbers = 0
        try {
            val dir = File("/sys/devices/system/cpu/")
            val files = dir.listFiles(CpuFilter())
            numbers = files.size
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (numbers < 1) {
            numbers = Runtime.getRuntime().availableProcessors()
        }
        if (numbers < 1) {
            numbers = 1
        }
        return numbers
    }

    private class CpuFilter : FileFilter {
        override fun accept(pathname: File): Boolean {
            return Pattern.matches("cpu[0-9]+", pathname.name)
        }
    }

}