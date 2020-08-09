package com.android.util;

import android.util.Log;

import com.android.java.BuildConfig;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by xuzhb on 2019/10/20
 * Desc:日志工具
 */
public class LogUtil {

    private static final int Verbose = 1;
    private static final int Debug = 2;
    private static final int Info = 3;
    private static final int Warn = 4;
    private static final int Error = 5;

    //日志文件总开关
    private static Boolean mLogSwitch = BuildConfig.DEBUG;
    //输出日志级别，如Verbose代表输出Verbose及以上级别的日志信息，Warn代表输出Warn及以上级别的日志信息
    private static int mLogType = Debug;
    //日志写入文件开关
    private static Boolean mLogToFile = false;
    //日志文件存放路径
    private static String mLogPath = "/sdcard/log";
    //日志文件名称
    private static String mLogFileName = "Log.txt";

    private static ThreadLocal<SimpleDateFormat> threadLocal1 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };
    private static ThreadLocal<SimpleDateFormat> threadLocal2 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        }
    };

    public static void e(String tag, Exception e) {
        Writer writer = new StringWriter();
        e.printStackTrace(new PrintWriter(writer));
        log(tag, writer.toString(), Error);
    }

    public static void e(String tag, String text) {
        log(tag, text, Error);
    }

    public static void w(String tag, String text) {
        log(tag, text, Warn);
    }

    public static void i(String tag, String text) {
        log(tag, text, Info);
    }

    public static void d(String tag, String text) {
        log(tag, text, Debug);
    }

    public static void v(String tag, String text) {
        log(tag, text, Verbose);
    }

    //根据tag, msg和等级，输出日志
    private static void log(String tag, String msg, int level) {
        if (mLogSwitch) {
            if (level == Verbose && level >= mLogType) {
                Log.v(tag, msg);
            } else if (level == Debug && level >= mLogType) {
                Log.d(tag, msg);
            } else if (level == Info && level >= mLogType) {
                Log.i(tag, msg);
            } else if (level == Warn && level >= mLogType) {
                Log.w(tag, msg);
            } else if (level == Error && level >= mLogType) {
                Log.e(tag, msg);
            }
        }
        if (mLogToFile) { //写入文件
            writeLogToFile(level, tag, msg);
        }
    }

    //打开日志文件并写入日志
    private static void writeLogToFile(int level, String tag, String text) {
        String logType = "i";
        if (level == Verbose) {
            logType = "V";
        } else if (level == Debug) {
            logType = "D";
        } else if (level == Info) {
            logType = "I";
        } else if (level == Warn) {
            logType = "W";
        } else {
            logType = "E";
        }
        Date currDate = new Date();
        String logFileNamePrefix = threadLocal1.get().format(currDate);
        String logMessage = threadLocal2.get().format(currDate) + " " + logType + "/" + tag + ": " + text;
        File filePath = new File(mLogPath);
        if (!filePath.exists()) {
            filePath.mkdirs();
        }
        String fullLogName = logFileNamePrefix + mLogFileName;
        File logFileName = new File(filePath, fullLogName);
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new BufferedWriter(new FileWriter(logFileName, true)));
            writer.println(logMessage);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != writer) {
                writer.close();
            }
        }
    }

    //删除除当天外的所有日志文件
    public static void deleteFile() {  // 删除日志文件
        String currFileName = threadLocal1.get().format(new Date()) + mLogFileName;
        System.out.println(currFileName);
        File filePath = new File(mLogPath);
        if (!filePath.exists()) {
            return;
        }
        File[] fileLists = filePath.listFiles();
        for (File f : fileLists) {
            if (!f.getName().equals(currFileName)) {
                f.delete();
            }
        }
    }

    //分段打印超长日志
    public static void logLongTag(String tag, String msg) {
        int max_str_length = 2001 - tag.length();
        //大于4000时
        while (msg.length() > max_str_length) {
            Log.i(tag, msg.substring(0, max_str_length));
            msg = msg.substring(max_str_length);
        }
        //剩余部分
        Log.i(tag, msg);
    }

}
