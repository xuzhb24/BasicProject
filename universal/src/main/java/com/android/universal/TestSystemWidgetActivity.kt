package com.android.universal

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import kotlinx.android.synthetic.main.layout_title_bar.*

/**
 * Created by xuzhb on 2020/2/16
 * Desc:系统自带控件的使用
 */
class TestSystemWidgetActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_system_widget)
        initTitleBar()
    }

    private fun initTitleBar() {
        title_fl.setBackgroundColor(resources.getColor(R.color.colorPrimary))
        val dp20 = (20 * resources.displayMetrics.density + 0.5f).toInt()
        title_fl.setPadding(dp20, 0, dp20, 0)
        title_tv.text = "系统控件"
        title_tv.setTextColor(Color.WHITE)
        right_tv.text = "说明"
        right_tv.setTextColor(Color.WHITE)
        left_fl.setOnClickListener {
            finish()
        }
        right_tv.setOnClickListener {
            AlertDialog.Builder(this)
                .setMessage("本页面主要记录一些系统自带控件的属性和使用方法")
                .show()
        }
    }

}