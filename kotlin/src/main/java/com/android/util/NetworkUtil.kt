package com.android.util

import android.content.Context
import android.net.ConnectivityManager
import com.android.base.BaseApplication

/**
 * Created by xuzhb on 2019/12/29
 * Desc:网络工具
 */
object NetworkUtil {

    //是否连接网络
    fun isConnected(context: Context = BaseApplication.instance): Boolean {
        val cm = context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = cm.activeNetworkInfo ?: return false
        return info.isAvailable
    }

}