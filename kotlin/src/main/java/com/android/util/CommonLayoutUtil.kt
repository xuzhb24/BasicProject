package com.android.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.android.basicproject.R
import com.android.widget.InputLayout
import com.android.widget.TitleBar

/**
 * Created by xuzhb on 2019/9/22
 * Desc:
 */
fun initCommonLayout(
    activity: Activity,
    title: String,
    showInputLayout: Boolean = false,
    showTextView: Boolean = false
) {
    initCommonLayout(
        activity,
        title,
        "",
        showInputLayout = showInputLayout,
        showTextView = showTextView
    )
}

fun initCommonLayout(
    activity: Activity,
    title: String,
    vararg text: String,
    showInputLayout: Boolean = false,
    showTextView: Boolean = false
) {
    val titleBar: TitleBar = activity.findViewById(R.id.title_bar)
    val il: InputLayout = activity.findViewById(R.id.il)
    val tv: TextView = activity.findViewById(R.id.tv)
    val btn1: Button = activity.findViewById(R.id.btn1)
    val btn2: Button = activity.findViewById(R.id.btn2)
    val btn3: Button = activity.findViewById(R.id.btn3)
    val btn4: Button = activity.findViewById(R.id.btn4)
    val btn5: Button = activity.findViewById(R.id.btn5)
    val btn6: Button = activity.findViewById(R.id.btn6)
    val btn7: Button = activity.findViewById(R.id.btn7)
    val btn8: Button = activity.findViewById(R.id.btn8)
    val btn9: Button = activity.findViewById(R.id.btn9)
    with(titleBar) {
        titleText = title
        setOnLeftClickListener {
            activity.finish()
        }
    }
    il.visibility = View.GONE
    tv.visibility = View.GONE
    btn1.visibility = View.GONE
    btn2.visibility = View.GONE
    btn3.visibility = View.GONE
    btn4.visibility = View.GONE
    btn5.visibility = View.GONE
    btn6.visibility = View.GONE
    btn7.visibility = View.GONE
    btn8.visibility = View.GONE
    btn9.visibility = View.GONE
    if (showInputLayout) {
        il.visibility = View.VISIBLE
    }
    if (showTextView) {
        tv.visibility = View.VISIBLE
    } else {
        if (text.size > 0) {
            val topMargin = (ScreenUtil.getScreenHeight(activity) - titleBar.height
                    - text.size * SizeUtil.dp2px(70f) - SizeUtil.dp2px(60f)) / 2f
            LayoutParamsUtil.setMarginTop(btn1, if (topMargin > 0) topMargin.toInt() else SizeUtil.dp2px(10f).toInt())
        }
    }
    if (text.size >= 1 && !TextUtils.isEmpty(text[0])) {
        btn1.visibility = View.VISIBLE
        btn1.text = text[0]
    }
    if (text.size >= 2) {
        btn2.visibility = View.VISIBLE
        btn2.text = text[1]
    }
    if (text.size >= 3) {
        btn3.visibility = View.VISIBLE
        btn3.text = text[2]
    }
    if (text.size >= 4) {
        btn4.visibility = View.VISIBLE
        btn4.text = text[3]
    }
    if (text.size >= 5) {
        btn5.visibility = View.VISIBLE
        btn5.text = text[4]
    }
    if (text.size >= 6) {
        btn6.visibility = View.VISIBLE
        btn6.text = text[5]
    }
    if (text.size >= 7) {
        btn7.visibility = View.VISIBLE
        btn7.text = text[6]
    }
    if (text.size >= 8) {
        btn8.visibility = View.VISIBLE
        btn8.text = text[7]
    }
    if (text.size >= 9) {
        btn9.visibility = View.VISIBLE
        btn9.text = text[8]
    }
}

const val MODULE_NAME = "MODULE_NAME"
fun jumpToTestUtilActivity(context: Context, moduleName: String) {
    val intent = Intent()
    intent.setClass(context, TestUtilActivity::class.java)
    intent.putExtra(MODULE_NAME, moduleName)
    context.startActivity(intent)
}

fun createInputLayout(activity: Activity, hint: String): InputLayout {
    val il = InputLayout(activity)
    return il.apply {
        layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT)
        inputTextHint = hint
        dividerHeight = SizeUtil.dp2px(2f)
        dividerColor = activity.resources.getColor(R.color.colorPrimary)
        LayoutParamsUtil.setMargin(il, SizeUtil.dp2px(20f).toInt(), 0, SizeUtil.dp2px(20f).toInt(), SizeUtil.dp2px(8f).toInt())
    }
}