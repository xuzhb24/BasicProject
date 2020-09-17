package com.android.util

import android.app.Activity
import android.app.ActivityManager
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.Settings
import android.text.TextUtils
import android.view.Gravity
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.android.basicproject.BuildConfig
import com.android.basicproject.R
import com.android.widget.PopupWindow.CommonPopupWindow
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

//屏幕顶部中间区域双击获取当前Activity类名，只在debug环境下有效
fun getTopActivityName(activity: AppCompatActivity, ev: MotionEvent?) {
    ev?.let {
        val screenWidth = ScreenUtil.getScreenWidth(activity)
        val width = SizeUtil.dp2px(80f)
        val height = SizeUtil.dp2px(80f)
        val left = (screenWidth - width) / 2f
        val right = left + width
        if (BuildConfig.DEBUG && it.action == MotionEvent.ACTION_DOWN &&
            it.rawY < height && it.rawX > left && it.rawX < right
        ) {
            CheckFastClickUtil.setOnMultiClickListener(600) {
                if (it == 2) {
                    val manager = activity.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
                    val info = manager.getRunningTasks(1).get(0)
                    val sb = StringBuilder()
                    sb.append("当前应用包名：\n").append(info.topActivity.packageName)
                        .append("\n当前Activity类名：\n").append(info.topActivity.className)
                    var visibleFragmentName = ""
                    val fragments = activity.supportFragmentManager.fragments
                    if (!fragments.isNullOrEmpty()) {
                        for (i in fragments.indices) {
                            if (i == 0) {
                                sb.append("\n\n当前Fragment列表：\n")
                            }
                            sb.append(fragments[i].javaClass.name).append("\n")
                            if (fragments[i].userVisibleHint) {
                                visibleFragmentName = fragments[i].javaClass.name
                            }
                        }
                        if (!TextUtils.isEmpty(visibleFragmentName)) {
                            sb.append("当前Fragment类名：\n").append(visibleFragmentName)
                        }
                    }
                    alert(activity, sb.toString())
                }
            }
        }
    }
}

fun showImage(activity: Activity, bitmap: Bitmap) {
    CommonPopupWindow.Builder(activity)
        .setContentView(R.layout.layout_image)
        .setViewParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        .setBackGroundAlpha(0.5f)
        .setOnViewListener { holder, popupWindow ->
            holder.getView<ImageView>(R.id.iv)?.setImageBitmap(bitmap)
        }
        .build()
        .showAtLocation(activity.findViewById(android.R.id.content), Gravity.CENTER, 0, 0)
}


fun showImage(activity: Activity, drawable: Drawable) {
    CommonPopupWindow.Builder(activity)
        .setContentView(R.layout.layout_image)
        .setViewParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        .setBackGroundAlpha(0.5f)
        .setOnViewListener { holder, popupWindow ->
            holder.getView<ImageView>(R.id.iv)?.setImageDrawable(drawable)
        }
        .build()
        .showAtLocation(activity.findViewById(android.R.id.content), Gravity.CENTER, 0, 0)
}