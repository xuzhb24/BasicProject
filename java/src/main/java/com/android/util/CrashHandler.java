package com.android.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Process;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by xuzhb on 2020/2/23
 * Desc:崩溃异常监听
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static final String TAG = "CrashHandler";

    private Context mContext;
    private Thread.UncaughtExceptionHandler mDefaultCrashHandler;

    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        return SingleTonHolder.holder;
    }

    private static class SingleTonHolder {
        private static final CrashHandler holder = new CrashHandler();
    }

    public void init(Context context) {
        mContext = context.getApplicationContext();
        mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);  //设置为线程默认的异常处理器
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        try {
            saveExceptionToCache(e);
        } catch (Exception ex) {
            Log.e(TAG, "dump exception fail!");
            ex.printStackTrace();
        }
        e.printStackTrace();
        if (mDefaultCrashHandler != null) {
            mDefaultCrashHandler.uncaughtException(t, e);
        } else {
            Process.killProcess(Process.myPid());
        }
    }

    private void saveExceptionToCache(Throwable e) throws Exception {
        String fileName = mContext.getCacheDir() + "/log/crash.trace";
        File file = new File(fileName);
        File dir = file.getParentFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));  //追加在文件末尾
        pw.println("=======================================================================");
        savePhoneInfo(pw);
        pw.println();
        pw.println(time);
        e.printStackTrace(pw);
        pw.println();
        pw.println();
        pw.close();
    }

    private void savePhoneInfo(PrintWriter pw) throws PackageManager.NameNotFoundException {
        PackageManager pm = mContext.getApplicationContext().getPackageManager();
        PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
        pw.print("应用版本：");
        pw.print(pi.versionName);
        pw.print("_");
        pw.println(pi.versionCode);
        pw.print("系统版本：");
        pw.print(Build.VERSION.RELEASE);
        pw.print("_");
        pw.println(Build.VERSION.SDK_INT);
        pw.print("手机制造商：");
        pw.println(Build.MANUFACTURER);
        pw.print("手机型号：");
        pw.println(Build.MODEL);
        pw.print("CPU型号：");
        pw.println(Build.CPU_ABI);
    }

}
