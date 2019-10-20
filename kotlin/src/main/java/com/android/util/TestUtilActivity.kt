package com.android.util

import android.os.Bundle
import com.android.basicproject.R
import com.android.frame.mvc.BaseActivity
import kotlinx.android.synthetic.main.activity_common_layout.*

/**
 * Created by xuzhb on 2019/9/22
 * Desc:测试工具类方法
 */
class TestUtilActivity : BaseActivity() {

    companion object {
        const val TEST_DATE = "TEST_DATE"
        const val TEST_KEYBOARD = "TEST_KEYBOARD"
        const val TEST_DRAWABLE = "TEST_DRAWABLE"
    }

    override fun handleView(savedInstanceState: Bundle?) {
        when (intent.getStringExtra(MODULE_NAME)) {
            TEST_DATE -> testDateUtil()
            TEST_KEYBOARD -> testKeyBoardUtil()
            TEST_DRAWABLE -> testDrawableUtil()
        }
    }

    override fun initListener() {}

    override fun getLayoutId(): Int = R.layout.activity_common_layout

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

    private fun testDrawableUtil() {
        initCommonLayout(this, "代码创建Drawable", "按钮", "solid", "stroke", "虚线stroke", "solid和stroke")
        btn2.setOnClickListener {
            val drawable = DrawableUtil.createSolidShape(
                SizeUtil.dp2px(10f),
                resources.getColor(R.color.orange)
            )
            btn1.setBackground(drawable)
            btn1.setTextColor(resources.getColor(R.color.white))
        }
        btn3.setOnClickListener {
            val drawable = DrawableUtil.createStrokeShape(
                SizeUtil.dp2px(15f),
                SizeUtil.dp2px(1f).toInt(),
                resources.getColor(R.color.orange)
            )
            btn1.setBackground(drawable)
            btn1.setTextColor(resources.getColor(R.color.orange))
        }
        btn4.setOnClickListener {
            val drawable = DrawableUtil.createStrokeShape(
                SizeUtil.dp2px(15f),
                SizeUtil.dp2px(1f).toInt(),
                resources.getColor(R.color.orange),
                SizeUtil.dp2px(2f),
                SizeUtil.dp2px(2f)
            )
            btn1.setBackground(drawable)
            btn1.setTextColor(resources.getColor(R.color.orange))
        }
        btn5.setOnClickListener {
            val drawable = DrawableUtil.createSolidStrokeShape(
                SizeUtil.dp2px(25f),
                resources.getColor(R.color.white),
                SizeUtil.dp2px(5f).toInt(),
                resources.getColor(R.color.orange)
            )
            btn1.setBackground(drawable)
            btn1.setTextColor(resources.getColor(R.color.orange))
        }
    }

}