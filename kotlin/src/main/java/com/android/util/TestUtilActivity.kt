package com.android.util

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.method.LinkMovementMethod
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
        const val TEST_SPUTIL = "TEST_SPUTIL"
        const val TEST_STRING = "TEST_STRING"
    }

    override fun handleView(savedInstanceState: Bundle?) {
        when (intent.getStringExtra(MODULE_NAME)) {
            TEST_DATE -> testDateUtil()
            TEST_KEYBOARD -> testKeyBoardUtil()
            TEST_DRAWABLE -> testDrawableUtil()
            TEST_SPUTIL -> testSPUtil()
            TEST_STRING -> testStringUtil()
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

    private fun testSPUtil() {
        initCommonLayout(this, "SharePreferences工具类", "保存", "读取", showEditText = true, showTextView = true)
        et.hint = "请输入名字"
        var name by SPUtil(applicationContext, "default", "name", "")
        btn1.setOnClickListener {
            name = et.text.toString().trim()
            et.setText("")
            KeyboardUtil.hideSoftInput(this, it)
        }
        btn2.setOnClickListener {
            tv.text = name
        }
    }

    private fun testStringUtil() {
        initCommonLayout(this, "字符串工具类", "显示不同颜色", "带下划线", "带点击事件", showTextView = true)
        val content = "欢迎拨打热线电话"
        tv.setTextColor(Color.BLACK)
        tv.setTextSize(15f)
        btn1.setOnClickListener {
            tv.text = StringUtil.createTextSpan(
                content, 4, 8,
                resources.getColor(R.color.orange),
                SizeUtil.sp2px(18f).toInt(),
                Typeface.ITALIC
            )
        }
        btn2.setOnClickListener {
            tv.text = StringUtil.createTextSpan(
                content, 4, 8,
                resources.getColor(R.color.orange),
                SizeUtil.sp2px(18f).toInt(),
                Typeface.NORMAL,
                true
            )
        }
        btn3.setOnClickListener {
            tv.movementMethod = LinkMovementMethod.getInstance()  //必须设置这个点击事件才能生效
            tv.text = StringUtil.createTextSpan(
                content, 4, 8,
                resources.getColor(R.color.orange),
                SizeUtil.sp2px(18f).toInt(),
                Typeface.BOLD_ITALIC,
                true
            ) { showToast("热线电话：10086") }
        }
    }

}