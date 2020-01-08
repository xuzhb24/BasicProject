package com.android.util

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.widget.EditText
import android.widget.LinearLayout
import com.android.basicproject.R
import com.android.frame.http.AATest.ApiService
import com.android.frame.http.AATest.UrlConstant
import com.android.frame.http.AATest.WangYiNewsWebviewActivity
import com.android.frame.http.AATest.bean.NewsListBean
import com.android.frame.http.ExceptionUtil
import com.android.frame.http.RetrofitFactory
import com.android.frame.http.SchedulerUtil
import com.android.frame.mvc.BaseActivity
import com.android.util.StatusBar.TestStatusBarUtilActivity
import com.bumptech.glide.Glide
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_common_layout.*
import kotlin.concurrent.thread
import kotlin.random.Random

/**
 * Created by xuzhb on 2019/9/22
 * Desc:测试工具类方法
 */
class TestUtilActivity : BaseActivity() {

    companion object {
        const val TEST_STATUS_BAR = "TEST_STATUS_BAR"
        const val TEST_DATE = "TEST_DATE"
        const val TEST_KEYBOARD = "TEST_KEYBOARD"
        const val TEST_DRAWABLE = "TEST_DRAWABLE"
        const val TEST_SPUTIL = "TEST_SPUTIL"
        const val TEST_STRING = "TEST_STRING"
        const val TEST_NOTIFICATION = "TEST_NOTIFICATION"
    }

    override fun handleView(savedInstanceState: Bundle?) {
        when (intent.getStringExtra(MODULE_NAME)) {
            TEST_STATUS_BAR -> testStatusBarUtil();
            TEST_DATE -> testDateUtil()
            TEST_KEYBOARD -> testKeyBoardUtil()
            TEST_DRAWABLE -> testDrawableUtil()
            TEST_SPUTIL -> testSPUtil()
            TEST_STRING -> testStringUtil()
            TEST_NOTIFICATION -> testNotification()
        }
    }

    override fun initListener() {}

    override fun getLayoutId(): Int = R.layout.activity_common_layout

    private fun testStatusBarUtil() {
        val text1 = "沉浸背景图片"
        val text2 = "状态栏白色，字体和图片黑色"
        val text3 = "状态栏黑色，字体和图片白色"
        val text4 = "状态栏黑色半透明，字体和图片白色"
        initCommonLayout(this, "实现沉浸式状态栏", text1, text2, text3, text4)
        btn1.setOnClickListener {
            jumpToTestStatusBarActivity(1, text1)
        }
        btn2.setOnClickListener {
            jumpToTestStatusBarActivity(2, text2)
        }
        btn3.setOnClickListener {
            jumpToTestStatusBarActivity(3, text3)
        }
        btn4.setOnClickListener {
            jumpToTestStatusBarActivity(4, text4)
        }
    }

    private fun jumpToTestStatusBarActivity(type: Int, text: String) {
        val intent = Intent(this, TestStatusBarUtilActivity::class.java)
        intent.putExtra(TestStatusBarUtilActivity.EXTRA_TYPE, type)
        intent.putExtra(TestStatusBarUtilActivity.EXTRA_TEXT, text)
        startActivity(intent)
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

    private fun testNotification() {
        tv.text = intent.getStringExtra("content")
        val titleEt = EditText(this)
        titleEt.layoutParams =
            LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        titleEt.hint = "请输入标题"
        ll.addView(titleEt, 0)
        et.hint = "请输入内容"
        initCommonLayout(
            this, "通知管理", "自定义通知", "自定义通知(带跳转)", "自定义通知(最全使用示例)",
            "新闻通知", "通知是否打开", "跳转通知设置界面", showEditText = true, showTextView = true
        )
        btn1.setOnClickListener {
            var title = titleEt.text.toString().trim()
            if (TextUtils.isEmpty(title)) {
                title = "这是标题"
            }
            var content = et.text.toString().trim()
            if (TextUtils.isEmpty(content)) {
                content = "这是内容"
            }
            NotificationUtil.showNotification(applicationContext, title, content)
        }
        btn2.setOnClickListener {
            var title = titleEt.text.toString().trim()
            if (TextUtils.isEmpty(title)) {
                title = "这是标题"
            }
            var content = et.text.toString().trim()
            if (TextUtils.isEmpty(content)) {
                content = "跳转到通知管理页面"
            }
            val intent = Intent()
            intent.setClass(this, TestUtilActivity::class.java)
            intent.putExtra(MODULE_NAME, TEST_NOTIFICATION)
            intent.putExtra("content", content)
            NotificationUtil.showNotification(
                applicationContext,
                title,
                content,
                intent = intent
            )
            finish()
        }
        btn3.setOnClickListener {
            var title = titleEt.text.toString().trim()
            if (TextUtils.isEmpty(title)) {
                title = "这是标题"
            }
            var content = et.text.toString().trim()
            if (TextUtils.isEmpty(content)) {
                content = "这是内容"
            }
            NotificationUtil.showNotificationFullUse(this, title, content)
        }
        btn4.setOnClickListener {
            requestNews()
        }
        btn5.setOnClickListener {
            tv.text = if (NotificationUtil.isNotificationEnabled(applicationContext)) "通知权限已打开" else "通知权限被关闭"
        }
        btn6.setOnClickListener {
            NotificationUtil.gotoNotificationSetting(this)
        }
    }

    private fun requestNews() {
        val count = 100
        RetrofitFactory.instance.createService(ApiService::class.java, UrlConstant.NEWS_URL)
            .getWangYiNewsByBody(1, count)
            .compose(SchedulerUtil.ioToMain())
            .subscribe(object : Observer<NewsListBean> {

                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(bean: NewsListBean) {
                    if (bean.isSuccess()) {
                        if (bean.result != null && bean.result.size > 0) {
                            val result = bean.result.get(Random.nextInt(count))
                            val title = "网易新闻"
                            val content = result.title
                            val target = Glide.with(this@TestUtilActivity).asBitmap().load(result.image).submit()
                            try {
                                thread(start = true) {
                                    val bitmap = target.get()
                                    val intent = Intent(this@TestUtilActivity, WangYiNewsWebviewActivity::class.java)
                                    with(intent) {
                                        putExtra("EXTRA_TITLE", title)
                                        putExtra("EXTRA_URL", result.path)
                                    }
                                    NotificationUtil.showNotification(
                                        this@TestUtilActivity,
                                        title,
                                        content,
                                        largeIconBitmap = bitmap,
                                        showBigText = true,
                                        intent = intent
                                    )
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                                showToast("获取图片失败，${e.message}")
                            }
                        } else {
                            showToast("获取新闻失败！")
                        }
                    } else {
                        showToast(bean.message)
                    }
                }

                override fun onError(e: Throwable) {
                    showToast(ExceptionUtil.convertExceptopn(e))
                    e.printStackTrace()
                }

            })
    }

}