package com.android.util

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.android.application.BaseApplication
import com.android.basicproject.R

/**
 * Created by xuzhb on 2019/9/26
 * Desc:自定义Toast
 */
object ToastUtil {

    fun showToast(
        text: CharSequence,
        context: Context = BaseApplication.instance,
        longToast: Boolean = false,
        isCenter: Boolean = false
    ) {
        val duration = if (longToast) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
        val toast = Toast.makeText(context.applicationContext, text, duration)
        val layout = LayoutInflater.from(context.applicationContext).inflate(R.layout.layout_toast, null)
        val toastTv: TextView = layout.findViewById(R.id.toast_tv)
        toastTv.setText(text)
        val toastShape = GradientDrawable()
        toastShape.setColor(Color.parseColor("#CC1F2735"))
        toastShape.cornerRadius = SizeUtil.dp2px(3f)
        layout.background = toastShape
        if (isCenter) {
            toast.setGravity(Gravity.CENTER, 0, 0)
        }
        toast.view = layout
        toast.show()
    }

    fun toast(text: CharSequence, context: Context = BaseApplication.instance) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    fun longToast(text: CharSequence, context: Context = BaseApplication.instance) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
    }

}