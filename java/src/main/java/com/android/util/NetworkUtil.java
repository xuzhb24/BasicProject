package com.android.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by xuzhb on 2020/1/5
 * Desc:网络工具
 */
public class NetworkUtil {

    //是否连接到网络
    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getApplicationContext().
                getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo info = cm.getActiveNetworkInfo();
            if (info != null) {
                return info.isAvailable();
            }
        }
        return false;
    }

}
