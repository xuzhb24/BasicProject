package com.android.util;

import android.content.Context;
import android.text.format.Formatter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.regex.Pattern;

/**
 * Created by xuzhb on 2021/5/6
 * Desc:CPU工具类
 */
public class CPUUtil {

    //获取处理器的Java虚拟机的数量
    public static int getProcessorsCount() {
        return Runtime.getRuntime().availableProcessors();
    }

    //获取手机CPU序列号(16位)
    public static String getCPUSerial() {
        String str, cpuSerialNum = "0000000000000000";
        try {
            //读取CPU信息
            Process pp = Runtime.getRuntime().exec("cat/proc/cpuinfo");
            InputStreamReader isr = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(isr);
            //查找CPU序列号
            for (int i = 1; i < 100; i++) {
                str = input.readLine();
                if (str != null) {
                    // 查找到序列号所在行
                    if (str.contains("Serial")) {
                        // 提取序列号
                        cpuSerialNum = str.substring(str.indexOf(':') + 1).trim();
                        break;
                    }
                } else {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cpuSerialNum;
    }

    //获取CPU信息
    public static String getCpuInfo() {
        try {
            FileReader fr = new FileReader("/proc/cpuinfo");
            BufferedReader br = new BufferedReader(fr);
            return br.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    //获取CPU型号
    public static String getCpuModel() {
        try {
            FileReader fr = new FileReader("/proc/cpuinfo");
            BufferedReader br = new BufferedReader(fr);
            String text = br.readLine();
            return text.split(":\\s+", 2)[1];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    //获取CPU最大频率(单位KHZ)
    public static String getMaxCpuFrequency(Context context) {
        ProcessBuilder cmd;
        InputStream is = null;
        try {
            StringBuilder builder = new StringBuilder();
            String[] args = {"/system/bin/cat", "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq"};
            cmd = new ProcessBuilder(args);
            Process process = cmd.start();
            is = process.getInputStream();
            byte[] re = new byte[24];
            while (is.read(re) != -1) {
                builder.append(new String(re));
            }
            return Formatter.formatFileSize(context.getApplicationContext(), Long.parseLong(builder.toString().trim()) * 1024) + " Hz";
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtil.closeIO(is);
        }
        return "unknown";
    }

    //获取CPU最小频率(单位KHZ)
    public static String getMinCpuFrequency(Context context) {
        ProcessBuilder cmd;
        InputStream is = null;
        try {
            StringBuilder builder = new StringBuilder();
            String[] args = {"/system/bin/cat", "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq"};
            cmd = new ProcessBuilder(args);
            Process process = cmd.start();
            is = process.getInputStream();
            byte[] re = new byte[24];
            while (is.read(re) != -1) {
                builder.append(new String(re));
            }
            return Formatter.formatFileSize(context.getApplicationContext(), Long.parseLong(builder.toString().trim()) * 1024) + " Hz";
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtil.closeIO(is);
        }
        return "unknown";
    }

    //获取CPU当前频率(单位KHZ)
    public static String getCurCpuFrequency(Context context) {
        try {
            FileReader fr = new FileReader("/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq");
            BufferedReader br = new BufferedReader(fr);
            String text = br.readLine();
            return Formatter.formatFileSize(context.getApplicationContext(), Long.parseLong(text.trim()) * 1024) + " Hz";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "unknown";
    }

    //获取CPU名字
    public static String getCpuName() {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("/proc/cpuinfo"), 8192);
            String line = br.readLine();
            String[] array = line.split(":\\s+", 2);
            if (array.length > 1) {
                return array[1];
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtil.closeIO(br);
        }
        return null;
    }

    //获取CPU核心数
    public static int getCoreNumbers() {
        int numbers = 0;
        try {
            File dir = new File("/sys/devices/system/cpu/");
            File[] files = dir.listFiles(new CpuFilter());
            numbers = files.length;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (numbers < 1) {
            numbers = Runtime.getRuntime().availableProcessors();
        }
        if (numbers < 1) {
            numbers = 1;
        }
        return numbers;
    }

    private static class CpuFilter implements FileFilter {
        @Override
        public boolean accept(File pathname) {
            return Pattern.matches("cpu[0-9]+", pathname.getName());
        }
    }

}
