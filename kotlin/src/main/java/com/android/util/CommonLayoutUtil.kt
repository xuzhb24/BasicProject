package com.android.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.android.basicproject.R
import com.android.widget.TitleBar

/**
 * Created by xuzhb on 2019/9/22
 * Desc:
 */
fun initCommonLayout(
    activity: Activity,
    title: String,
    vararg text: String,
    showEditText: Boolean = false,
    showTextView: Boolean = false
) {
    val titleBar: TitleBar = activity.findViewById(R.id.title_bar)
    val et: EditText = activity.findViewById(R.id.et)
    val tv: TextView = activity.findViewById(R.id.tv)
    val btn1: Button = activity.findViewById(R.id.btn1)
    val btn2: Button = activity.findViewById(R.id.btn2)
    val btn3: Button = activity.findViewById(R.id.btn3)
    val btn4: Button = activity.findViewById(R.id.btn4)
    val btn5: Button = activity.findViewById(R.id.btn5)
    val btn6: Button = activity.findViewById(R.id.btn6)
    with(titleBar) {
        titleText = title
        setOnLeftClickListener {
            activity.finish()
        }
    }
    et.visibility = View.GONE
    tv.visibility = View.GONE
    btn1.visibility = View.GONE
    btn2.visibility = View.GONE
    btn3.visibility = View.GONE
    btn4.visibility = View.GONE
    btn5.visibility = View.GONE
    btn6.visibility = View.GONE
    if (showEditText) {
        et.visibility = View.VISIBLE
    }
    if (showTextView) {
        tv.visibility = View.VISIBLE
    }
    if (text.size >= 1) {
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
}

const val MODULE_NAME = "MODULE_NAME"
fun jumpToTestUtilActivity(context: Context, moduleName: String) {
    val intent = Intent()
    intent.setClass(context, TestUtilActivity::class.java)
    intent.putExtra(MODULE_NAME, moduleName)
    context.startActivity(intent)
}