package com.android.util

import android.os.Bundle
import com.android.basicproject.R
import com.android.frame.mvc.BaseActivity
import kotlinx.android.synthetic.main.activity_common_layout.*
import java.util.*

/**
 * Created by xuzhb on 2019/9/22
 * Desc:测试工具类方法
 */
class TestUtilActivity : BaseActivity() {

    companion object {
        const val TEST_DATE = "TEST_DATE"
        const val TEST_KEYBOARD = "TEST_KEYBOARD"
    }

    override fun handleView(savedInstanceState: Bundle?) {
        when (intent.getStringExtra(MODULE_NAME)) {
            TEST_DATE -> testDateUtil()
            TEST_KEYBOARD -> testKeyBoardUtil()
        }
    }

    private fun testDateUtil() {
        initCommonLayout(this, "测试时间工具", "按钮")
        btn1.setOnClickListener {
            println(DateUtil.isLeapYear(2016))
            println(DateUtil.isLeapYear(2100))
            println(DateUtil.isLeapYear(2013))
        }
    }

    private fun testKeyBoardUtil() {
        initCommonLayout(this, "测试键盘工具", "弹出软键盘", "收起软键盘", "复制到剪切板")
        btn1.setOnClickListener {
            KeyboardUtil.showSoftInput(this, it)
        }
        btn2.setOnClickListener {
            KeyboardUtil.hideSoftInput(this, it)
        }
        btn3.setOnClickListener {
            KeyboardUtil.copyToClipboard(this, "https://www.baidu.com")
        }
    }

    override fun initListener() {
    }

    override fun getLayoutId(): Int = R.layout.activity_common_layout
}