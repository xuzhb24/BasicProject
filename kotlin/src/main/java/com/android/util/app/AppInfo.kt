package com.android.util.app

import android.graphics.drawable.Drawable

/**
 * Created by xuzhb on 2020/11/17
 * Desc:应用信息类
 */
data class AppInfo(
    val uid: Int = 0,                 //应用UID
    val label: String? = null,        //应用标签，即名称
    val icon: Drawable? = null,       //应用图片
    val packageName: String? = null,  //应用包名
    val apkPath: String? = null,      //应用路径
    val versionName: String? = null,  //应用版本号
    val versionCode: Int = 0,         //应用版本码
    val isSystemApp: Boolean = false  //是否是系统App
)