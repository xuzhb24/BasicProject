package com.android.frame.bugly;

import android.content.Context;
import com.android.java.BuildConfig;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * Create by KD on 2020/1/19
 * 腾讯Bugly
 */
public class BuglyUtil {

    //初始化Bugly
    public static void init(Context context) {
        CrashReport.initCrashReport(context.getApplicationContext(), "4728a77b29", BuildConfig.DEBUG);
    }

}
