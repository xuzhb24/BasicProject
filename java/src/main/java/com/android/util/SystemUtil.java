package com.android.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

/**
 * Created by xuzhb on 2019/12/7
 * Desc:系统工具
 */
public class SystemUtil {

    private static final String TAG = "SystemUtil";

    //获取应用UID
    public static int getUid(Context context, String packageName) {
        int uid = -1;
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo ai = pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
            uid = ai.uid;
            LogUtil.i(TAG, "label:" + ai.loadLabel(pm) + "\tuid:" + ai.uid);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return uid;
    }

}
