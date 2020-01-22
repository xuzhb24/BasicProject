package com.android.util

import android.app.Activity
import android.app.ActivityManager
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.View
import com.android.basicproject.BuildConfig
import com.android.basicproject.R
import com.android.widget.TitleBar
import com.google.gson.Gson
import java.util.*

/**
 * Created by xuzhb on 2019/9/22
 * Desc:工具类扩展，存放单个方法
 */

fun main() {
    println("随机5位字符串：${createRandomKey(5)}")
}

//跳转到系统设置界面
fun jumpToAppSetting(activity: Activity) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    intent.setData(Uri.fromParts("package", activity.packageName, null))
    activity.startActivity(intent)
}

//生成指定长度由大小写字母、数字组成的随机字符串
fun createRandomKey(length: Int): String {
    val sb = StringBuilder()
    val random = Random()
    for (i in 0 until length) {
        var s: Char = 0.toChar()
        val j = random.nextInt(3) % 4
        when (j) {
            0 -> s = (random.nextInt(57) % (57 - 48 + 1) + 48).toChar()  //随机生成数字
            1 -> s = (random.nextInt(90) % (90 - 65 + 1) + 65).toChar()  //随机生成大写字母
            2 -> s = (random.nextInt(122) % (122 - 97 + 1) + 97).toChar()  //随机生成小写字母
        }
        sb.append(s)
    }
    return sb.toString()
}

fun alert(context: Context, msg: String) {
    AlertDialog.Builder(context).setMessage(msg).show()
}

fun isEntity(text: String, obj: Any): Boolean {
    try {
        Gson().fromJson(text, obj::class.java)
        return true
    } catch (e: Exception) {
        return false
    }
}

//双击标题栏获取当前页面的Activity名称，只是调试用
fun getTopActivityName(activity: Activity) {
    if (BuildConfig.DEBUG) {
        val titleBar: TitleBar? = activity.findViewById(R.id.title_bar)
        titleBar?.setOnClickListener(object : OnMultiClickListener() {
            override fun onMultiClick(v: View?, clickCount: Int) {
                if (clickCount == 2) {
                    val manager = activity.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
                    val info = manager.getRunningTasks(1).get(0)
                    alert(activity, "${info.topActivity.packageName}(包名)\n${info.topActivity.className}(类名)")
                }
            }
        })
    }
}