package com.android.util

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.IBinder
import android.text.InputType
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ImageSpan
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.android.basicproject.R
import com.android.basicproject.databinding.ActivityCommonLayoutBinding
import com.android.frame.http.AATest.ApiService
import com.android.frame.http.AATest.UrlConstant
import com.android.frame.http.AATest.WangYiNewsWebviewActivity
import com.android.frame.http.AATest.bean.NewsListBean
import com.android.frame.http.ExceptionUtil
import com.android.frame.http.RetrofitFactory
import com.android.frame.http.SchedulerUtil
import com.android.frame.mvc.BaseActivity
import com.android.util.StatusBar.TestStatusBarUtilActivity
import com.android.util.activity.ActivityUtil
import com.android.util.activity.TestJumpActivity
import com.android.util.app.AATest.TestAppListActivity
import com.android.util.app.AppUtil
import com.android.util.bitmap.BitmapUtil
import com.android.util.location.LocationService
import com.android.util.location.LocationUtil
import com.android.util.permission.PermissionUtil
import com.android.util.regex.RegexUtil
import com.android.util.service.ServiceUtil
import com.android.util.service.TestService
import com.android.util.threadPool.ThreadPoolManager
import com.android.widget.InputLayout
import com.android.widget.RecyclerView.AATest.entity.MonthBean
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.bumptech.glide.Glide
import com.google.gson.Gson
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_common_layout.*
import java.util.*
import kotlin.concurrent.thread
import kotlin.random.Random

/**
 * Created by xuzhb on 2019/9/22
 * Desc:测试工具类方法
 */
class TestUtilActivity : BaseActivity<ActivityCommonLayoutBinding>() {

    companion object {
        private const val TAG = "TestUtilActivity"

        const val TEST_STATUS_BAR = "TEST_STATUS_BAR"
        const val TEST_DATE = "TEST_DATE"
        const val TEST_KEYBOARD = "TEST_KEYBOARD"
        const val TEST_DRAWABLE = "TEST_DRAWABLE"
        const val TEST_SPUTIL = "TEST_SPUTIL"
        const val TEST_STRING = "TEST_STRING"
        const val TEST_NOTIFICATION = "TEST_NOTIFICATION"
        const val TEST_CONTINUOUS_CLICK = "TEST_CONTINUOUS_CLICK"
        const val TEST_PINYIN = "TEST_PINYIN"
        const val TEST_LAYOUT_PARAMS = "TEST_LAYOUT_PARAMS"
        const val TEST_REGEX = "TEST_REGEX"
        const val TEST_CACHE = "TEST_CACHE"
        const val TEST_ACTIVITY = "TEST_ACTIVITY"
        const val TEST_SHELL = "TEST_SHELL"
        const val TEST_DEVICE = "TEST_DEVICE"
        const val TEST_APP = "TEST_APP"
        const val TEST_CRASH = "TEST_CRASH"
        const val TEST_PICKER_VIEW = "TEST_PICKER_VIEW"
        const val TEST_CLEAN = "TEST_CLEAN"
        const val TEST_SDCARD = "TEST_SDCARD"
        const val TEST_SCREEN = "TEST_SCREEN"
        const val TEST_PHONE = "TEST_PHONE"
        const val TEST_ENCODE = "TEST_ENCODE"
        const val TEST_SERVICE = "TEST_SERVICE"
        const val TEST_LOCATION = "TEST_LOCATION"
        const val TEST_NETWORK = "TEST_NETWORK"
        const val TEST_APK_DOWNLOAD = "TEST_APK_DOWNLOAD"
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
            TEST_CONTINUOUS_CLICK -> testContinuousClick()
            TEST_PINYIN -> testPinyin()
            TEST_LAYOUT_PARAMS -> testLayoutParams()
            TEST_REGEX -> testRegex()
            TEST_CACHE -> testCache()
            TEST_ACTIVITY -> testActivity()
            TEST_SHELL -> testShell()
            TEST_DEVICE -> testDevice()
            TEST_APP -> testApp()
            TEST_CRASH -> testCrash()
            TEST_PICKER_VIEW -> testPickerView()
            TEST_CLEAN -> testClean()
            TEST_SDCARD -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    testSdcard()
                }
            }
            TEST_SCREEN -> testScreen()
            TEST_PHONE -> testPhone()
            TEST_ENCODE -> testEncode()
            TEST_SERVICE -> testService()
            TEST_LOCATION -> testLocation()
            TEST_NETWORK -> testNetwork()
            TEST_APK_DOWNLOAD -> testApkDownload()
        }
    }

    override fun initListener() {}

    override fun getViewBinding() = ActivityCommonLayoutBinding.inflate(layoutInflater)

    private fun testStatusBarUtil() {
        val text1 = "沉浸背景图片"
        val text2 = "状态栏白色，字体和图片黑色"
        val text3 = "状态栏黑色，字体和图片白色"
        val text4 = "状态栏黑色半透明，字体和图片白色"
        val text5 = "隐藏导航栏"
        val text6 = "导航栏和状态栏透明"
        val text7 = "透明度和十六进制对应表"
        initCommonLayout(this, "实现沉浸式状态栏", text1, text2, text3, text4, text5, text6, text7)
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
        btn5.setOnClickListener {
            jumpToTestStatusBarActivity(5, text5)
        }
        btn6.setOnClickListener {
            jumpToTestStatusBarActivity(6, text6)
        }
        btn7.setOnClickListener {
            val content =
                IOUtil.readInputStreameToString(resources.openRawResource(R.raw.hex_alpha_table))
            alert(this, content ?: "")
        }
    }

    private fun jumpToTestStatusBarActivity(type: Int, text: String) {
        val intent = Intent(this, TestStatusBarUtilActivity::class.java)
        intent.putExtra(TestStatusBarUtilActivity.EXTRA_TYPE, type)
        intent.putExtra(TestStatusBarUtilActivity.EXTRA_TEXT, text)
        startActivity(intent)
    }

    private fun testDateUtil() {
        initCommonLayout(this, "测试时间工具", false, true)
        val sb = StringBuilder()
        //测试getCurrentDateTime
        sb.append("当前时间\n").append(DateUtil.getCurrentDateTime())
            .append("\n").append(DateUtil.getCurrentDateTime(DateUtil.Y_M_D_H_M_S_S))
        //测试date2String和string2Date
        sb.append("\n\nDate <--> String\n")
            .append(DateUtil.date2String(Date(), DateUtil.Y_M_D_H_M_S))
            .append("\n")
            .append(
                DateUtil.date2String(
                    DateUtil.string2Date("20200202", DateUtil.YMD)!!,
                    DateUtil.Y_M_D_H_M_S_S
                )
            )
        //测试long2String和string2Long
        sb.append("\n\nLong <--> String\n")
            .append(DateUtil.long2String(System.currentTimeMillis(), DateUtil.Y_M_D_H_M_S_S))
            .append("\n")
            .append(
                DateUtil.long2String(
                    DateUtil.string2Long("20200202", DateUtil.YMD)!!,
                    DateUtil.Y_M_D_H_M_S_S
                )
            )
        //测试date2Long和long2Date
        val dateLong = Date()
        sb.append("\n\nDate <--> Long\n").append(DateUtil.date2Long(dateLong))
            .append("\n").append(DateUtil.date2Long(DateUtil.long2Date(dateLong.time)!!))
        //测试isLeapYear
        sb.append("\n\n是否是闰年：\n").append("2000：").append(DateUtil.isLeapYear(2000))
            .append("\n").append("2012：").append(DateUtil.isLeapYear(2012))
            .append("\n").append("2019：").append(DateUtil.isLeapYear(2019))
        //测试convertOtherFormat
        val convertTime = DateUtil.getCurrentDateTime()
        sb.append("\n\n转换时间格式\n").append(convertTime).append(" -> ")
            .append(
                DateUtil.convertOtherFormat(
                    convertTime,
                    DateUtil.Y_M_D_H_M_S,
                    "yyyyMMddHHmmssSSS"
                )
            )
        //测试compareDate
        val compareTime1 = "2019-02-28"
        val compareTime2 = "2019-02-28"
        val compareTime3 = "2019-02-27"
        val compareTime4 = "2019-03-01"
        sb.append("\n\n比较日期大小\n")
            .append(compareTime1).append(" ").append(compareTime2).append("：")
            .append(DateUtil.compareDate(compareTime1, compareTime2, DateUtil.Y_M_D)).append("\n")
            .append(compareTime1).append(" ").append(compareTime3).append("：")
            .append(DateUtil.compareDate(compareTime1, compareTime3, DateUtil.Y_M_D)).append("\n")
            .append(compareTime1).append(" ").append(compareTime4).append("：")
            .append(DateUtil.compareDate(compareTime1, compareTime4, DateUtil.Y_M_D))
        //测试getDistanceDateByYear
        val distanceDate = "2020-02-02 12:11:03.123"
        sb.append("\n\nN年前/后的日期\n").append(DateUtil.getCurrentDateTime(DateUtil.Y_M_D))
            .append("\n1年前：").append(DateUtil.getDistanceDateByYear(-1, DateUtil.Y_M_D))
            .append("\n2年后：").append(DateUtil.getDistanceDateByYear(2, DateUtil.Y_M_D))
            .append("\n").append(distanceDate)
            .append("\n3年前：")
            .append(DateUtil.getDistanceDateByYear(-3, DateUtil.Y_M_D_H_M_S_S, distanceDate))
            .append("\n4年后：")
            .append(DateUtil.getDistanceDateByYear(4, DateUtil.Y_M_D_H_M_S_S, distanceDate))
        //测试getDistanceDateByMonth
        sb.append("\n\nN月前/后的日期\n").append(DateUtil.getCurrentDateTime(DateUtil.Y_M_D))
            .append("\n1月前：").append(DateUtil.getDistanceDateByMonth(-1, DateUtil.Y_M_D))
            .append("\n2月后：").append(DateUtil.getDistanceDateByMonth(2, DateUtil.Y_M_D))
            .append("\n").append(distanceDate)
            .append("\n3月前：")
            .append(DateUtil.getDistanceDateByMonth(-3, DateUtil.Y_M_D_H_M_S_S, distanceDate))
            .append("\n4月后：")
            .append(DateUtil.getDistanceDateByMonth(4, DateUtil.Y_M_D_H_M_S_S, distanceDate))
        //测试getDistanceDateByWeek
        sb.append("\n\nN周前/后的日期\n").append(DateUtil.getCurrentDateTime(DateUtil.Y_M_D))
            .append("\n1周前：").append(DateUtil.getDistanceDateByWeek(-1, DateUtil.Y_M_D))
            .append("\n2周后：").append(DateUtil.getDistanceDateByWeek(2, DateUtil.Y_M_D))
            .append("\n").append(distanceDate)
            .append("\n3周前：")
            .append(DateUtil.getDistanceDateByWeek(-3, DateUtil.Y_M_D_H_M_S_S, distanceDate))
            .append("\n4周后：")
            .append(DateUtil.getDistanceDateByWeek(4, DateUtil.Y_M_D_H_M_S_S, distanceDate))
        //测试getDistanceDateByDay
        sb.append("\n\nN天前/后的日期\n").append(DateUtil.getCurrentDateTime(DateUtil.Y_M_D))
            .append("\n1天前：").append(DateUtil.getDistanceDateByDay(-1, DateUtil.Y_M_D))
            .append("\n2天后：").append(DateUtil.getDistanceDateByDay(2, DateUtil.Y_M_D))
            .append("\n").append(distanceDate)
            .append("\n3天前：")
            .append(DateUtil.getDistanceDateByDay(-3, DateUtil.Y_M_D_H_M_S_S, distanceDate))
            .append("\n4天后：")
            .append(DateUtil.getDistanceDateByDay(4, DateUtil.Y_M_D_H_M_S_S, distanceDate))
        //测试getDistanceDay
        val distanceDay1 = "2020-02-02"
        val distanceDay2 = "2020-01-31"
        sb.append("\n\n间隔天数\n").append(distanceDay1).append(" ").append(distanceDay2).append("：")
            .append(DateUtil.getDistanceDay(distanceDay1, distanceDay2, DateUtil.Y_M_D))
        //测试getDistanceSecond
        val distanceSecond1 = "12:11:56.786"
        val distanceSecond2 = "12:12:03.123"
        sb.append("\n\n间隔秒数\n").append(distanceSecond1).append(" ").append(distanceSecond2)
            .append("：")
            .append(DateUtil.getDistanceSecond(distanceSecond1, distanceSecond2, DateUtil.H_M_S_S))
        //测试isInThePeriod
        val startDate = "2020-02-02 12:23:45.004"
        val endDate = "2020-12-02 12:23:45.004"
        val dateTimeInThePeriod1 = "2020-02-02 12:23:45.002"
        val dateTimeInThePeriod2 = "2020-12-02 12:23:46.002"
        sb.append("\n\n是否在某个时间段\n").append("[").append(startDate).append(" - ").append(endDate)
            .append("]\n")
            .append(DateUtil.getCurrentDateTime()).append("：")
            .append(DateUtil.isInThePeriod(startDate, endDate, DateUtil.Y_M_D_H_M_S))
            .append("\n").append(dateTimeInThePeriod1).append("：")
            .append(
                DateUtil.isInThePeriod(
                    startDate,
                    endDate,
                    DateUtil.Y_M_D_H_M_S,
                    dateTimeInThePeriod1
                )
            )
            .append("\n").append(dateTimeInThePeriod2).append("：")
            .append(
                DateUtil.isInThePeriod(
                    startDate,
                    endDate,
                    DateUtil.Y_M_D_H_M_S,
                    dateTimeInThePeriod2
                )
            )
        //测试getStartTimeOfToday和getStartTimeOfDay
        val startTime = "2020-02-02"
        sb.append("\n\n某天零点时间\n").append(DateUtil.getCurrentDateTime(DateUtil.Y_M_D)).append("：")
            .append(DateUtil.long2String(DateUtil.getStartTimeOfToday(), DateUtil.Y_M_D_H_M_S_S))
            .append("\n").append(startTime).append("：")
            .append(
                DateUtil.long2String(
                    DateUtil.getStartTimeOfDay(startTime, DateUtil.Y_M_D),
                    DateUtil.Y_M_D_H_M_S_S
                )
            )
        //测试getStartTimeOfCurrentMonth和getStartTimeOfMonth
        sb.append("\n\n某月零点时间\n").append(DateUtil.getCurrentDateTime(DateUtil.Y_M)).append("：")
            .append(
                DateUtil.long2String(
                    DateUtil.getStartTimeOfCurrentMonth(),
                    DateUtil.Y_M_D_H_M_S_S
                )
            )
            .append("\n")
            .append(DateUtil.convertOtherFormat(startTime, DateUtil.Y_M_D, DateUtil.Y_M))
            .append("：")
            .append(
                DateUtil.long2String(
                    DateUtil.getStartTimeOfMonth(startTime, DateUtil.Y_M_D),
                    DateUtil.Y_M_D_H_M_S_S
                )
            )
        //测试getEndTimeOfToday和getEndTimeOfDay
        val endTime = "2020-02-02"
        sb.append("\n\n某天最后时间\n").append(DateUtil.getCurrentDateTime(DateUtil.Y_M_D)).append("：")
            .append(DateUtil.long2String(DateUtil.getEndTimeOfToday(), DateUtil.Y_M_D_H_M_S_S))
            .append("\n").append(endTime).append("：")
            .append(
                DateUtil.long2String(
                    DateUtil.getEndTimeOfDay(startTime, DateUtil.Y_M_D),
                    DateUtil.Y_M_D_H_M_S_S
                )
            )
        //测试getEndTimeOfCurrentMonth和getEndTimeOfMonth
        sb.append("\n\n某月最后时间\n").append(DateUtil.getCurrentDateTime(DateUtil.Y_M)).append("：")
            .append(
                DateUtil.long2String(
                    DateUtil.getEndTimeOfCurrentMonth(),
                    DateUtil.Y_M_D_H_M_S_S
                )
            )
            .append("\n")
            .append(DateUtil.convertOtherFormat(startTime, DateUtil.Y_M_D, DateUtil.Y_M))
            .append("：")
            .append(
                DateUtil.long2String(
                    DateUtil.getEndTimeOfMonth(startTime, DateUtil.Y_M_D),
                    DateUtil.Y_M_D_H_M_S_S
                )
            )
        //测试getCurrentDayOfWeekCH和getDayOfWeekCH
        val weekTime = "2020-02-02"
        sb.append("\n\n判断周几\n").append(DateUtil.getCurrentDateTime()).append("：")
            .append(DateUtil.getCurrentDayOfWeekCH()).append("\n").append(weekTime).append("：")
            .append(DateUtil.getDayOfWeekCH(weekTime, DateUtil.Y_M_D))
        tv.text = sb.toString()
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
        initCommonLayout(
            this,
            "SharePreferences工具类",
            "保存",
            "读取",
            showInputLayout = true,
            showTextView = true
        )
        il.inputTextHint = "请输入名字"
        var name by SPUtil(applicationContext, "default", "name", "")
        btn1.setOnClickListener {
            name = il.inputText.trim()
            il.inputText = ""
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
        val titleIl = createInputLayout(this, "请输入标题")
        ll.addView(titleIl, 0)
        il.inputTextHint = "请输入内容"
        initCommonLayout(
            this, "通知管理", "自定义通知", "自定义通知(带跳转)", "自定义通知(最全使用示例)",
            "新闻通知", "通知是否打开", "跳转通知设置界面", showInputLayout = true, showTextView = true
        )
        btn1.setOnClickListener {
            var title = titleIl.inputText.trim()
            if (TextUtils.isEmpty(title)) {
                title = "这是标题"
            }
            var content = il.inputText.trim()
            if (TextUtils.isEmpty(content)) {
                content = "这是内容"
            }
            NotificationUtil.showNotification(applicationContext, title, content)
        }
        btn2.setOnClickListener {
            var title = titleIl.inputText.trim()
            if (TextUtils.isEmpty(title)) {
                title = "这是标题"
            }
            var content = il.inputText.trim()
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
            var title = titleIl.inputText.trim()
            if (TextUtils.isEmpty(title)) {
                title = "这是标题"
            }
            var content = il.inputText.trim()
            if (TextUtils.isEmpty(content)) {
                content = "这是内容"
            }
            NotificationUtil.showNotificationFullUse(this, title, content)
        }
        btn4.setOnClickListener {
            requestNews()
        }
        btn5.setOnClickListener {
            tv.text =
                if (NotificationUtil.isNotificationEnabled(applicationContext)) "通知权限已打开" else "通知权限被关闭"
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
                            val target =
                                Glide.with(this@TestUtilActivity).asBitmap().load(result.image)
                                    .submit()
                            try {
                                thread(start = true) {
                                    val bitmap = target.get()
                                    val intent = Intent(
                                        this@TestUtilActivity,
                                        WangYiNewsWebviewActivity::class.java
                                    )
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

    private var mLastTime = 0L

    //连续点击事件监听
    private fun testContinuousClick() {
        initCommonLayout(this, "连续点击", "连续点击", "连续点击(点击最大时间间隔2秒)", showTextView = true)
        tv.gravity = Gravity.CENTER
        btn1.setOnClickListener(object : OnMultiClickListener() {
            override fun onMultiClick(v: View?, clickCount: Int) {
                val currentTime = System.currentTimeMillis()
                val sb = StringBuilder()
                sb.append("点击次数:").append(clickCount).append("\n距离上次点击的时间间隔:")
                    .append(currentTime - mLastTime).append("ms\n")
                    .append("当前默认最大时间间隔:").append(getClickInterval()).append("ms")
                tv.text = sb.toString()
                mLastTime = currentTime
            }
        })
        btn2.setOnClickListener(object : OnMultiClickListener() {
            override fun onMultiClick(v: View?, clickCount: Int) {
                val currentTime = System.currentTimeMillis()
                val sb = StringBuilder()
                sb.append("点击次数:").append(clickCount).append("\n距离上次点击的时间间隔:")
                    .append(currentTime - mLastTime).append("ms\n")
                    .append("当前默认最大时间间隔:").append(getClickInterval()).append("ms")
                tv.text = sb.toString()
                mLastTime = currentTime
            }

            override fun getClickInterval(): Int = 2000
        })
    }

    //拼音工具
    private fun testPinyin() {
        initCommonLayout(
            this, "拼音工具", "获取汉字拼音", "获取姓氏拼音",
            showInputLayout = true, showTextView = true
        )
        il.inputText = "测试拼音工具"
        val s = il.inputText.trim()
        val text = "汉字拼音：${PinyinUtil.hanzi2Pinyin(s)}" +
                "\n首字母拼音：${PinyinUtil.getFirstLetter(s)}"
        il.getEditText().setSelection(s.length)
        tv.text = text
        btn1.setOnClickListener {
            val content = il.inputText.trim()
            val split = " "
            val result = "汉字拼音：${PinyinUtil.hanzi2Pinyin(content, split)}" +
                    "\n首字母拼音：${PinyinUtil.getFirstLetter(content, split)}"
            tv.text = result
        }
        btn2.setOnClickListener {
            val name = il.inputText.trim()
            val result = "姓氏的拼音：${PinyinUtil.getSurnamePinyin(name)}" +
                    "\n首字母拼音：${PinyinUtil.getSurnameFirstLetter(name)}"
            tv.text = result
        }
        il.setOnTextClearListener {
            tv.text = ""
        }
    }

    //布局参数工具
    private fun testLayoutParams() {
        initCommonLayout(
            this, "布局参数工具", "设置上下左右Margin为10dp", "设置上下左右Padding为10dp",
            "增加上下左右Margin为5dp", "减少上下左右Margin为5dp",
            "增加上下左右Padding为5dp", "减少上下左右Padding为5dp", "还原"
        )
        LayoutParamsUtil.setMarginTop(btn1, SizeUtil.dp2px(10f).toInt())
        val rootLl = LinearLayout(this)
        val param1 =
            LinearLayout.LayoutParams(SizeUtil.dp2px(300f).toInt(), SizeUtil.dp2px(200f).toInt())
        param1.gravity = Gravity.CENTER_HORIZONTAL
        rootLl.layoutParams = param1
        rootLl.setBackgroundColor(Color.BLACK)
        val targetLl = LinearLayout(this)
        val params2 = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        targetLl.layoutParams = params2
        targetLl.setBackgroundColor(resources.getColor(R.color.colorPrimary))
        val dp20 = SizeUtil.dp2px(20f).toInt()
        val dp40 = SizeUtil.dp2px(40f).toInt()
        LayoutParamsUtil.setMargin(targetLl, dp20, dp20, dp20, dp20)
        LayoutParamsUtil.setPadding(targetLl, dp40, dp40, dp40, dp40)
        val view = View(this)
        val param3 =
            LinearLayout.LayoutParams(SizeUtil.dp2px(300f).toInt(), SizeUtil.dp2px(200f).toInt())
        view.layoutParams = param3
        view.setBackgroundColor(Color.WHITE)
        targetLl.addView(view)
        rootLl.addView(targetLl)
        ll.addView(rootLl, 0)
        btn1.setOnClickListener {
            val margin = SizeUtil.dp2px(10f).toInt()
            LayoutParamsUtil.setMarginLeft(targetLl, margin)
            LayoutParamsUtil.setMarginRight(targetLl, margin)
            LayoutParamsUtil.setMarginTop(targetLl, margin)
            LayoutParamsUtil.setMarginBottom(targetLl, margin)
        }
        btn2.setOnClickListener {
            val padding = SizeUtil.dp2px(10f).toInt()
            LayoutParamsUtil.setPaddingLeft(targetLl, padding)
            LayoutParamsUtil.setPaddingRight(targetLl, padding)
            LayoutParamsUtil.setPaddingTop(targetLl, padding)
            LayoutParamsUtil.setPaddingBottom(targetLl, padding)
        }
        btn3.setOnClickListener {
            val margin = SizeUtil.dp2px(5f).toInt()
            LayoutParamsUtil.addMarginLeft(targetLl, margin)
            LayoutParamsUtil.addMarginRight(targetLl, margin)
            LayoutParamsUtil.addMarginTop(targetLl, margin)
            LayoutParamsUtil.addMarginBottom(targetLl, margin)
        }
        btn4.setOnClickListener {
            val margin = -SizeUtil.dp2px(5f).toInt()
            LayoutParamsUtil.addMarginLeft(targetLl, margin)
            LayoutParamsUtil.addMarginRight(targetLl, margin)
            LayoutParamsUtil.addMarginTop(targetLl, margin)
            LayoutParamsUtil.addMarginBottom(targetLl, margin)
        }
        btn5.setOnClickListener {
            val padding = SizeUtil.dp2px(5f).toInt()
            LayoutParamsUtil.addPaddingLeft(targetLl, padding)
            LayoutParamsUtil.addPaddingRight(targetLl, padding)
            LayoutParamsUtil.addPaddingTop(targetLl, padding)
            LayoutParamsUtil.addPaddingBottom(targetLl, padding)
        }
        btn6.setOnClickListener {
            val padding = -SizeUtil.dp2px(5f).toInt()
            LayoutParamsUtil.addPaddingLeft(targetLl, padding)
            LayoutParamsUtil.addPaddingRight(targetLl, padding)
            LayoutParamsUtil.addPaddingTop(targetLl, padding)
            LayoutParamsUtil.addPaddingBottom(targetLl, padding)
        }
        btn7.setOnClickListener {
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            targetLl.layoutParams = params
            LayoutParamsUtil.setMargin(targetLl, dp20, dp20, dp20, dp20)
            LayoutParamsUtil.setPadding(targetLl, dp40, dp40, dp40, dp40)
        }
    }

    //正则表达式工具
    private fun testRegex() {
        initCommonLayout(
            this, "正则表达式工具", true, true,
            "匹配数字", "匹配整数", "匹配浮点数", "匹配汉字", "匹配英文字母或数字", "匹配英文字母和数字",
            "匹配手机号", "匹配身份证号", "匹配\"yyyy-MM-dd\"的日期格式", "匹配输入的正则表达式",
            "提取文本中数字", "根据给定的正则表达式提取内容"
        )
        val regexIl = createInputLayout(this, "请输入正则表达式")
        ll.addView(regexIl, 1)
        il.inputTextHint = "请输入要匹配的内容"
        btn1.setOnClickListener {
            setRegexResult(RegexUtil.isDigit(il.inputText))
        }
        btn2.setOnClickListener {
            setRegexResult(RegexUtil.isInteger(il.inputText))
        }
        btn3.setOnClickListener {
            setRegexResult(RegexUtil.isFloat(il.inputText))
        }
        btn4.setOnClickListener {
            setRegexResult(RegexUtil.isChinese(il.inputText))
        }
        btn5.setOnClickListener {
            setRegexResult(RegexUtil.isLetterOrDigit(il.inputText))
        }
        btn6.setOnClickListener {
            setRegexResult(RegexUtil.isLetterAndDigit(il.inputText))
        }
        btn7.setOnClickListener {
            setRegexResult(RegexUtil.isMobile(il.inputText))
        }
        btn8.setOnClickListener {
            setRegexResult(RegexUtil.isIdCard(il.inputText))
        }
        btn9.setOnClickListener {
            setRegexResult(RegexUtil.isyyyyMMdd(il.inputText))
        }
        btn10.setOnClickListener {
            val regex = regexIl.inputText.trim()
            if (TextUtils.isEmpty(regex)) {
                showToast("请先输入正则表达式")
                return@setOnClickListener
            }
            setRegexResult(RegexUtil.isMatch(regex, il.inputText))
        }
        btn11.setOnClickListener {
            val content = il.inputText.trim()
            if (TextUtils.isEmpty(content)) {
                showToast("请先输入要匹配的内容")
                return@setOnClickListener
            }
            tv.text = RegexUtil.extractDigit(content, "|")
        }
        btn12.setOnClickListener {
            val content = il.inputText.trim()
            if (TextUtils.isEmpty(content)) {
                showToast("请先输入要匹配的内容")
                return@setOnClickListener
            }
            val regex = regexIl.inputText.trim()
            if (TextUtils.isEmpty(regex)) {
                showToast("请先输入正则表达式")
                return@setOnClickListener
            }
            tv.text = RegexUtil.extract(regex, content, "|")
        }
    }

    private fun setRegexResult(isCorrect: Boolean) {
        val result = "\"${il.inputText}\"：$isCorrect"
        tv.text = result
    }

    //磁盘缓存工具
    private fun testCache() {
        initCommonLayout(
            this, "磁盘缓存工具", true, true,
            "读取", "保存字符串", "保存Serializable数据", "保存Bitmap", "保存Drawable",
            "移除指定key", "清除所有缓存内容"
        )
        il.inputTextHint = "请输入Key名"
        val valueIl = createInputLayout(this, "请输入要保存的字符串")
        val durationIl = createInputLayout(this, "请输入保存的时长")
        durationIl.inputTextType = InputType.TYPE_CLASS_NUMBER
        ll.addView(valueIl, 1)
        ll.addView(durationIl, 2)
        val tip = "默认Key名：\n字符串：String\t\t\t\t\tSerializable：Serializable\nBitmap：Bitmap\t\t\tDrawable：Drawable\n"
        tv.text = tip
        val cacheUtil = CacheUtil(this)
//        val cacheUtil = CacheUtil(this,"cacheUtils2")
        btn1.setOnClickListener {
            tv.text = tip
            switchCacheState(!isSaveCache, valueIl, durationIl)
            isSaveCache = !isSaveCache
        }
        btn2.setOnClickListener {
            tv.text = tip
            val key = getKey(il, "String")
            if (isSaveCache) {
                val value = valueIl.inputText.trim()
                if (TextUtils.isEmpty(value)) {
                    showToast("请输入保存的字符串")
                    return@setOnClickListener
                }
                val saveTime = getSaveTime(durationIl)
                when (saveTime) {
                    0 -> showToast("请输入大于0秒的时长")
                    -1 -> {
                        cacheUtil.putString(key, value)
                        showToast("保存成功")
                    }
                    else -> {
                        cacheUtil.putString(key, value, saveTime)
                        showToast("保存成功，数据将缓存${saveTime}秒")
                    }
                }
            } else {
                val result = cacheUtil.getString(key)
                tv.text = if (TextUtils.isEmpty(result)) "数据为空" else result
            }
        }
        btn3.setOnClickListener {
            tv.text = tip
            val key = getKey(il, "Serializable")
            val bean = MonthBean("2020-02", key)
            if (isSaveCache) {
                val saveTime = getSaveTime(durationIl)
                when (saveTime) {
                    0 -> showToast("请输入大于0秒的时长")
                    -1 -> {
                        cacheUtil.putObject(key, bean)
                        showToast("保存成功")
                    }
                    else -> {
                        cacheUtil.putObject(key, bean, saveTime)
                        showToast("保存成功，数据将缓存${saveTime}秒")
                    }
                }
            } else {
                val result = cacheUtil.getObject(key)
                tv.text = if (result == null) "数据为空" else Gson().toJson(result)
            }
        }
        btn4.setOnClickListener {
            tv.text = tip
            val key = getKey(il, "Bitmap")
            val bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_coupon_header)
            if (isSaveCache) {
                val saveTime = getSaveTime(durationIl)
                when (saveTime) {
                    0 -> showToast("请输入大于0秒的时长")
                    -1 -> {
                        cacheUtil.putBitmap(key, bitmap)
                        showToast("保存成功")
                    }
                    else -> {
                        cacheUtil.putBitmap(key, bitmap, saveTime)
                        showToast("保存成功，数据将缓存${saveTime}秒")
                    }
                }
            } else {
                val result = cacheUtil.getBitmap(key)
                if (result != null) {
                    showImage(this, result)
                } else {
                    showToast("数据为空")
                }
            }
        }
        btn5.setOnClickListener {
            tv.text = tip
            val key = getKey(il, "Drawable")
            val drawable = resources.getDrawable(R.drawable.shape_oval_size_34_solid_db4b3c)
            if (isSaveCache) {
                val saveTime = getSaveTime(durationIl)
                when (saveTime) {
                    0 -> showToast("请输入大于0秒的时长")
                    -1 -> {
                        cacheUtil.putDrawable(key, drawable)
                        showToast("保存成功")
                    }
                    else -> {
                        cacheUtil.putDrawable(key, drawable, saveTime)
                        showToast("保存成功，数据将缓存${saveTime}秒")
                    }
                }
            } else {
                val result = cacheUtil.getDrawable(key)
                if (result != null) {
                    showImage(this, result)
                } else {
                    showToast("数据为空")
                }
            }
        }
        btn6.setOnClickListener {
            val key = il.inputText.trim()
            if (TextUtils.isEmpty(key)) {
                showToast("请先输入要删除的Key名")
                return@setOnClickListener
            }
            cacheUtil.remove(key)
            showToast("已删除")
        }
        btn7.setOnClickListener {
            cacheUtil.delete()
            showToast("已清除")
        }
    }

    private var isSaveCache = true

    private fun switchCacheState(save: Boolean, vararg inputLayouts: InputLayout) {
        for (il in inputLayouts) {
            il.visibility = if (save) View.VISIBLE else View.GONE
            il.inputText = ""
        }
        il.inputText = ""
        btn1.text = if (save) "读取" else "保存"
        btn2.text = if (save) "保存字符串" else "读取字符串"
        btn3.text = if (save) "保存Serializable数据" else "读取Serializable数据"
        btn4.text = if (save) "保存Bitmap" else "读取Bitmap"
        btn5.text = if (save) "保存Drawable" else "读取Drawable"
    }

    private fun getKey(il: InputLayout, defaultKey: String): String {
        val key = il.inputText.trim()
        return if (TextUtils.isEmpty(key)) defaultKey else key
    }

    private fun getSaveTime(il: InputLayout): Int {
        val saveTime = il.inputText.trim()
        return if (TextUtils.isEmpty(saveTime)) -1 else saveTime.toInt()
    }

    private fun testActivity() {
        initCommonLayout(
            this, "Activity工具", false, true,
            "跳转到东方财富", "跳转Activity(带动画)", "跳转Activity(携带参数)"
        )
        val sb = StringBuilder()
        sb.append("MainActivity是否存在：")
            .append(ActivityUtil.isActivityExists(this, "com.android.kotlin", "com.android.base.MainActivity"))
            .append("\n启动Activity名：")
            .append(ActivityUtil.getLauncherActivityName(this, packageName))
        val topActivity =
            ActivityUtil.getTopActivityName(this).split(" ".toRegex()).toTypedArray()
        sb.append("\n\n栈顶Activity信息：\n")
            .append(topActivity[0]).append("(包名)\n")
            .append(topActivity[1]).append("(类名)")
        tv.text = sb.toString()
        btn1.setOnClickListener {
            /*
            * 哔哩哔哩：tv.danmaku.bili，tv.danmaku.bili.ui.splash.SplashActivity
            * 微博：com.sina.weibo，com.sina.weibo.SplashActivity
            * 东方财富：com.eastmoney.android.berlin，com.eastmoney.android.berlin.activity.MainActivity
            * 铁路12306：com.MobileTicket，com.alipay.mobile.quinox.LauncherActivity
            * 微信：com.tencent.mm，com.tencent.mm.ui.LauncherUI
            * 支付宝：com.eg.android.AlipayGphone，com.eg.android.AlipayGphone.AlipayLogin
            * UC浏览器：com.UCMobile，com.UCMobile.main.UCMobile
            * 淘宝：com.taobao.taobao，com.taobao.tao.welcome.Welcome
            * 京东：com.jingdong.app.mall，com.jingdong.app.mall.main.MainActivity
            * 爱奇艺：com.qiyi.video，com.qiyi.video.WelcomeActivity
            * 今日头条：com.ss.android.article.news，com.ss.android.article.news.activity.MainActivity
            */

            /*
             * 哔哩哔哩：tv.danmaku.bili，tv.danmaku.bili.ui.splash.SplashActivity
             * 微博：com.sina.weibo，com.sina.weibo.SplashActivity
             * 东方财富：com.eastmoney.android.berlin，com.eastmoney.android.berlin.activity.MainActivity
             * 铁路12306：com.MobileTicket，com.alipay.mobile.quinox.LauncherActivity
             * 微信：com.tencent.mm，com.tencent.mm.ui.LauncherUI
             * 支付宝：com.eg.android.AlipayGphone，com.eg.android.AlipayGphone.AlipayLogin
             * UC浏览器：com.UCMobile，com.UCMobile.main.UCMobile
             * 淘宝：com.taobao.taobao，com.taobao.tao.welcome.Welcome
             * 京东：com.jingdong.app.mall，com.jingdong.app.mall.main.MainActivity
             * 爱奇艺：com.qiyi.video，com.qiyi.video.WelcomeActivity
             * 今日头条：com.ss.android.article.news，com.ss.android.article.news.activity.MainActivity
             */
            val packageName = "com.eastmoney.android.berlin"
            val className = "com.eastmoney.android.berlin.activity.MainActivity"
            if (ActivityUtil.isActivityExists(this, packageName, className)) {
                ActivityUtil.startActivity(this, packageName, className)
            } else {
                showToast("不存在该应用")
            }
        }
        btn2.setOnClickListener {
            ActivityUtil.startActivity(
                this, TestJumpActivity::class.java, null,
                R.anim.slide_right_in, R.anim.slide_left_out
            )
        }
        btn3.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(TestJumpActivity.EXTRA_DATA, btn3.text.toString())
            ActivityUtil.startActivity(this, TestJumpActivity::class.java, bundle)
        }
    }

    //Shell工具
    private fun testShell() {
        val command1 = "getprop ro.product.model"
        val command2 = "cd sdcard/AATest\ncat test.txt"
        initCommonLayout(
            this, "Shell工具", true, true,
            "运行命令", command1, command2
        )
        il.inputTextHint = "请输入要执行的Shell命令，多条的话换行输入"
        btn1.setOnClickListener {
            val content = il.inputText.trim()
            if (TextUtils.isEmpty(content)) {
                showToast("请先输入命令")
            } else {
                val commands = content.split("\n")
                val result = ShellUtil.execCmd(commands, false).toString()
                LogUtil.w("Shell", result)
                tv.text = result
            }
        }
        btn2.setOnClickListener {
            tv.text = ShellUtil.execShellCmd(command1).toString()
        }
        btn3.setOnClickListener {
            val testFilePath = Environment.getExternalStorageDirectory().toString() + "/AATest/test.txt"
            if (!PermissionUtil.requestPermissions(
                    this, 1,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                showToast("请先允许权限")
                return@setOnClickListener
            }
            val result = ShellUtil.execCmd(command2.split("\n".toRegex()), false).toString()
            LogUtil.w("Shell", result)
            tv.text = result
        }
    }

    //设备工具
    fun testDevice() {
        initCommonLayout(this, "设备工具", false, true);
        val sb = StringBuilder()
        sb.append("设备是否root：").append(DeviceUtil.isDeviceRooted())
            .append("\n设备MAC地址：").append(DeviceUtil.getMacAddress(this))
            .append("\n设备厂商：").append(DeviceUtil.getManufacturer())
            .append("\n设备型号：").append(DeviceUtil.getModel())
            .append("\n设备品牌：").append(DeviceUtil.getBrand())
            .append("\nSDK版本号：").append(DeviceUtil.getSDKVersion())
            .append("\n系统版本号：").append(DeviceUtil.getOSVersion())
            .append("\n设备AndroidID：").append(DeviceUtil.getAndroidID(this))
            .append("\n分辨率：").append(DeviceUtil.getScreenResolution(this))
            .append("\n运营商名称：").append(DeviceUtil.getSimOperatorName(this))
            .append("\n系统语言：").append(DeviceUtil.getSystemLanguage())
            .append("\nIMEI：").append(DeviceUtil.getIMEI(this))
        tv.text = sb.toString()
    }

    //App工具
    private fun testApp() {
        initCommonLayout(
            this, "App工具",
            "本应用相关信息", "已安装应用列表", "打开应用设置页面",
            "安装测试应用", "卸载测试应用", "打开测试应用", "跳转至拨号界面",
            "拨打电话", "发送短信"
        )
        btn1.setOnClickListener {
            alert(this, getAppInfo())
        }
        btn2.setOnClickListener {
            startActivity(TestAppListActivity::class.java)
        }
        btn3.setOnClickListener {
            AppUtil.openLocalAppSettings(this)
        }
        val testApkPackageName = "com.android.testinstall"
        val testApkPath = Environment.getExternalStorageDirectory().toString() + "/AATest/test_install.apk"
        btn4.setOnClickListener {
            if (!PermissionUtil.requestPermissions(
                    this, 1,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                showToast("请先允许权限")
                return@setOnClickListener
            }
            if (AppUtil.isAppInstall(this, testApkPackageName)) {
                showToast("应用已安装")
            } else {
                if (FileUtil.isFileExists(testApkPath)) {
                    AppUtil.installApp(this, testApkPath, applicationInfo.packageName + ".provider")
                } else {
                    kotlin.runCatching {
                        if (IOUtil.writeFileFromInputStream(testApkPath, assets.open("test_install.apk"), false)) {  //写入成功
                            AppUtil.installApp(this, testApkPath, applicationInfo.packageName + ".provider")
                        } else {
                            showToast("文件写入失败")
                        }
                    }.onFailure {
                        it.printStackTrace()
                        showToast("文件写入异常，" + it.message)
                    }
                }
            }
        }
        btn5.setOnClickListener {
            if (AppUtil.isAppInstall(this, testApkPackageName)) {
                AppUtil.uninstallApp(this, testApkPackageName)
            } else {
                showToast("应用未安装")
            }
        }
        btn6.setOnClickListener {
            if (AppUtil.isAppInstall(this, testApkPackageName)) {
                AppUtil.openApp(this, testApkPackageName)
            } else {
                showToast("应用未安装")
            }
        }
        btn7.setOnClickListener {
            startActivity(IntentUtil.getDialIntent("13302480305"))
        }
        btn8.setOnClickListener {
            if (!PermissionUtil.requestPermissions(this, 1, Manifest.permission.CALL_PHONE)) {
                showToast("请先允许权限")
                return@setOnClickListener
            }
            startActivity(IntentUtil.getCallIntent("13302480305"))
        }
        btn9.setOnClickListener {
            startActivity(IntentUtil.getSendSmsIntent("13302480305", "短信内容"))
        }
    }

    private fun getAppInfo(): SpannableStringBuilder {
        val builder = SpannableStringBuilder()
        builder.append("应用图标： ")
        val icon = AppUtil.getLocalAppIcon(this)
        if (icon != null) {
            icon.setBounds(0, 0, icon.intrinsicWidth, icon.intrinsicHeight)
            val span = ImageSpan(icon)
            builder.setSpan(span, 5, 6, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        }
        builder.append("\n应用名称：")
            .append(AppUtil.getLocalAppLabel(this))
            .append("\n安装路径：\n")
            .append(AppUtil.getLocalAppPath(this))
            .append("\nversionName：")
            .append(AppUtil.getLocalAppVersionName(this))
            .append("\nversionCode：")
            .append(AppUtil.getLocalAppVersionCode(this).toString())
            .append("\n是否系统应用：")
            .append(AppUtil.isLocalSystemApp(this).toString())
            .append("\n是否是Debug版本：")
            .append(AppUtil.isLocalAppDebug(this).toString())
            .append("\n是否有root权限：")
            .append(AppUtil.isLocalAppRoot().toString())
            .append("\nMD5值：\n")
            .append(AppUtil.getLocalAppSignatureMD5(this))
            .append("\nSHA1值：\n")
            .append(AppUtil.getLocalAppSignatureSHA1(this))
            .append("\n是否处于前台：")
            .append(AppUtil.isLocalAppForeground(this).toString())
        LogUtil.e("AppInfo", " \n${AppUtil.getLocalAppInfo(this)}")
        return builder
    }

    //崩溃异常监听
    private fun testCrash() {
        initCommonLayout(
            this, "崩溃异常监听",
            "抛异常", "读取crash文件", "删除crash文件", "移动文件到sd卡"
        )
        val dirName = "$cacheDir/log"
        val fileName = "$dirName/crash.trace"
        btn1.setOnClickListener {
            findViewById<TextView>(R.id.toast_tv).text = ""  //空指针异常
//            throw RuntimeException("自定义异常")
        }
        btn2.setOnClickListener {
            if (FileUtil.isFileExists(fileName)) {
                val content = IOUtil.readFileToString(fileName)
                alert(this, content ?: "")
            } else {
                showToast("crash文件不存在")
            }
        }
        btn3.setOnClickListener {
            if (FileUtil.isFileExists(fileName)) {
                showToast(if (FileUtil.deleteDirectory(dirName)) "删除成功" else "删除失败")
            } else {
                showToast("crash文件不存在")
            }
        }
        btn4.setOnClickListener {
            if (!PermissionUtil.requestPermissions(
                    this, 1,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                showToast("请先允许权限")
                return@setOnClickListener
            }
            val moveFileName = "sdcard/AATest/crash.trace"
            if (FileUtil.deleteFile(moveFileName) && FileUtil.isFileExists(fileName)) {
                showToast(if (FileUtil.moveFile(fileName, moveFileName)) "文件已移动到$moveFileName" else "移动失败")
            } else {
                showToast("crash文件不存在")
            }
        }
    }

    //选择器工具类
    private fun testPickerView() {
        initCommonLayout(
            this, "底部选择器工具", false, true,
            "选择年月日", "选择年月日时分秒"
        )
        btn1.setOnClickListener {
            val curentTime = DateUtil.convertOtherFormat(tv.text.toString(), DateUtil.Y_M_D_H_M_S, DateUtil.Y_M_D)
            PickerViewUtil.selectDate(this, OnTimeSelectListener { date, v ->
                tv.text = DateUtil.date2String(date, DateUtil.Y_M_D)
            }, curentTime, formatStr = DateUtil.Y_M_D)
        }
        btn2.setOnClickListener {
            val startTime = "1990-01-01 00:00:00"
            val endTime = "2100-01-01 12:00:00"
            val formatStr = DateUtil.Y_M_D_H_M_S
            val type = booleanArrayOf(true, true, true, true, true, true)
            val curentTime = DateUtil.convertOtherFormat(tv.text.toString(), DateUtil.Y_M_D, DateUtil.Y_M_D_H_M_S)
            PickerViewUtil.selectDateTime(
                this, OnTimeSelectListener { date: Date?, v1: View? ->
                    tv.text = DateUtil.date2String(date!!, formatStr)
                }, curentTime, startTime, endTime, formatStr,
                "选择日期时间", "取消", "确定", type
            )
        }
    }

    //应用文件清除工具
    private fun testClean() {
        initCommonLayout(
            this, "应用文件清除工具", true, true,
            "创建测试数据", "清除内部缓存(cache)", "清除内部数据库(databases)",
            "清除内部文件(files)", "清除内部SharePreference", "清除外部缓存(cache)"
        )
        il.inputTextHint = "请输入要删除文件，如a.txt，不输入默认全部删除"
        btn1.setOnClickListener {
            showToast("正在创建")
            thread(start = true) {
                FileUtil.createOrExistsFile("$cacheDir/cache1.txt")
                FileUtil.createOrExistsFile("$cacheDir/cache2.txt")
                FileUtil.createOrExistsFile("$cacheDir/cache3.txt")
                FileUtil.createOrExistsFile("$cacheDir/cache4.txt")
                DBHelper(this, "test1.db").readableDatabase
                DBHelper(this, "test2.db").readableDatabase
                DBHelper(this, "test3.db").readableDatabase
                DBHelper(this, "test4.db").readableDatabase
                FileUtil.createOrExistsFile("$filesDir/files1.trace")
                FileUtil.createOrExistsFile("$filesDir/files2.trace")
                FileUtil.createOrExistsFile("$filesDir/files3.trace")
                FileUtil.createOrExistsFile("$filesDir/files4.trace")
                getSharedPreferences("share1", Context.MODE_PRIVATE).edit().putString("key", "value").apply()
                getSharedPreferences("share2", Context.MODE_PRIVATE).edit().putString("key", "value").apply()
                getSharedPreferences("share3", Context.MODE_PRIVATE).edit().putString("key", "value").apply()
                getSharedPreferences("share4", Context.MODE_PRIVATE).edit().putString("key", "value").apply()
                FileUtil.createOrExistsFile("$externalCacheDir/ecache1.txt")
                FileUtil.createOrExistsFile("$externalCacheDir/ecache2.txt")
                FileUtil.createOrExistsFile("$externalCacheDir/ecache3.txt")
                FileUtil.createOrExistsFile("$externalCacheDir/ecache4.txt")
                listFilesInDir()
            }
        }
        btn2.setOnClickListener {
            val name = il.inputText.trim()
            if (TextUtils.isEmpty(name)) { //删除全部文件
                if (CleanUtil.cleanInternalCache(this)) {
                    showToast("删除成功")
                } else {
                    showToast("删除失败")
                }
            } else {  //删除指定文件
                if (CleanUtil.cleanInternalCacheByName(this, name)) {
                    showToast("删除成功")
                } else {
                    showToast("删除失败")
                }
            }
            listFilesInDir()
        }
        btn3.setOnClickListener {
            val name = il.inputText.trim()
            if (TextUtils.isEmpty(name)) { //删除全部文件
                if (CleanUtil.cleanInternalDatabase(this)) {
                    showToast("删除成功")
                } else {
                    showToast("删除失败")
                }
            } else {  //删除指定文件
                if (CleanUtil.cleanInternalDatabaseByName(this, name)) {
                    showToast("删除成功")
                } else {
                    showToast("删除失败")
                }
            }
            listFilesInDir()
        }
        btn4.setOnClickListener {
            val name = il.inputText.trim()
            if (TextUtils.isEmpty(name)) { //删除全部文件
                if (CleanUtil.cleanInternalFiles(this)) {
                    showToast("删除成功")
                } else {
                    showToast("删除失败")
                }
            } else {  //删除指定文件
                if (CleanUtil.cleanInternalFilesByName(this, name)) {
                    showToast("删除成功")
                } else {
                    showToast("删除失败")
                }
            }
            listFilesInDir()
        }
        btn5.setOnClickListener {
            val name = il.inputText.trim()
            if (TextUtils.isEmpty(name)) { //删除全部文件
                if (CleanUtil.cleanInternalSharePreference(this)) {
                    showToast("删除成功")
                } else {
                    showToast("删除失败")
                }
            } else {  //删除指定文件
                if (CleanUtil.cleanInternalSharePreferenceByName(this, name)) {
                    showToast("删除成功")
                } else {
                    showToast("删除失败")
                }
            }
            listFilesInDir()
        }
        btn6.setOnClickListener {
            val name = il.inputText.trim()
            if (TextUtils.isEmpty(name)) { //删除全部文件
                if (CleanUtil.cleanExternalCache(this)) {
                    showToast("删除成功")
                } else {
                    showToast("删除失败")
                }
            } else {  //删除指定文件
                if (CleanUtil.cleanExternalCacheByName(this, name)) {
                    showToast("删除成功")
                } else {
                    showToast("删除失败")
                }
            }
            listFilesInDir()
        }
    }

    //创建数据库
    private class DBHelper(context: Context, datebaseName: String) : SQLiteOpenHelper(context, datebaseName, null, 1) {

        /**
         * 创建数据库表：person
         * _id为主键，自增
         */
        override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
            Log.i(TAG, "创建test数据库表！")
            sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS test(_id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR,info TEXT)")
        }

        override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

        override fun onOpen(sqLiteDatabase: SQLiteDatabase) {
            super.onOpen(sqLiteDatabase)
        }
    }

    private fun listFilesInDir() {
        runOnUiThread {
            val sb = StringBuilder()
            FileUtil.listFilesInDirectory(cacheDir.parent, true)?.let {
                for (file in it) {
                    sb.append(file.absolutePath).append("\n").append("------------------------------------").append("\n")
                }
            }
            FileUtil.listFilesInDirectory(externalCacheDir.absolutePath, true)?.let {
                for (file in it) {
                    sb.append(file.absolutePath).append("\n").append("------------------------------------").append("\n")
                }
            }
            alert(this, sb.toString())
        }
    }

    //SD卡工具
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private fun testSdcard() {
        initCommonLayout(this, "SD卡工具", false, true)
        val sb = StringBuilder()
        sb.append("SD卡是否可用：").append(SDCardUtil.isSDCardEnable())
            .append("\nSD卡路径：").append(SDCardUtil.getSDCardPath())
            .append("\n文件路径：").append(SDCardUtil.getDocumentPath())
            .append("\n下载路径：").append(SDCardUtil.getDownloadPath())
            .append("\n影视路径：").append(SDCardUtil.getMoviePath())
            .append("\n音乐路径：").append(SDCardUtil.getMusicPath())
            .append("\n图片路径：").append(SDCardUtil.getPicturePath())
            .append("\n\n总存储空间：").append(ByteUnit.convertByteUnit(SDCardUtil.getTotalSize().toDouble(), ByteUnit.GB))
            .append(" GB")
            .append("\n剩余空间：").append(ByteUnit.convertByteUnit(SDCardUtil.getAvailableSize().toDouble(), ByteUnit.GB))
            .append(" GB")
            .append("\n已用空间：").append(ByteUnit.convertByteUnit(SDCardUtil.getUsedSize().toDouble(), ByteUnit.GB)).append(" GB")
            .append("\n\nSD卡信息：\n").append(JsonUtil.formatJson(Gson().toJson(SDCardUtil.getSDCardInfo())))
        tv.text = sb.toString()
    }

    //屏幕工具
    private fun testScreen() {
        initCommonLayout(
            this, "屏幕工具", false, true,
            "截屏(包含状态栏)", "截屏(不包含状态栏)", "设置屏幕横屏", "设置屏幕竖屏",
            "设置屏幕跟随系统状态", "获取屏幕信息"
        )
        btn1.setOnClickListener {
            val bitmap = ScreenUtil.captureWithStatusBar(this)
            if (BitmapUtil.saveBitmapToGallery(this, bitmap, "截屏带状态栏")) {
                showToast("保存成功！")
            } else {
                showToast("保存失败！")
            }
        }
        btn2.setOnClickListener {
            val bitmap = ScreenUtil.captureWithoutStatusBar(this)
            if (BitmapUtil.saveBitmapToGallery(this, bitmap, "截屏不带状态栏")) {
                showToast("保存成功！")
            } else {
                showToast("保存失败！")
            }
        }
        btn3.setOnClickListener {
            ScreenUtil.setLandscape(this)
        }
        btn4.setOnClickListener {
            ScreenUtil.setPortrait(this)
        }
        btn5.setOnClickListener {
            ScreenUtil.setSensor(this)
        }
        btn6.setOnClickListener {
            val sb = StringBuilder()
            val dm = ScreenUtil.getDisplayMetrics(this)
            sb.append("屏幕宽度：").append(ScreenUtil.getScreenWidth(this))
                .append("\n屏幕高度：").append(ScreenUtil.getScreenHeight(this))
                .append("\ndensity：").append(dm.density)
                .append("\nscaledDensity：").append(dm.scaledDensity)
                .append("\ndensityDpi：").append(dm.densityDpi)
                .append("\n是否是横屏：").append(ScreenUtil.isLandscape(this))
                .append("\n是否是竖屏：").append(ScreenUtil.isPortrait(this))
                .append("\n是否锁屏：").append(ScreenUtil.isScreenLock(this))
                .append("\n旋转角度：").append(ScreenUtil.getScreenRotation(this))
            alert(this, sb.toString())
        }
    }

    //手机工具类
    private fun testPhone() {
        initCommonLayout(
            this, "手机工具", true, false,
            "获取手机信息", "判断手机号是否合法", "跳转至拨号界面", "拨打电话", "跳转至发送短信界面",
            "发送短信", "获取手机联系人信息", "获取手机短信内容"
        )
        val phoneNumber = "13302480305"
        btn1.setOnClickListener {
            if (!PermissionUtil.requestPermissions(this, 1, Manifest.permission.READ_PHONE_STATE)) {
                showToast("请先允许权限")
                return@setOnClickListener
            }
            val sb = StringBuilder()
            sb.append("当前设备是否是手机：").append(PhoneUtil.isPhone(this))
                .append("\nIMEI：").append(PhoneUtil.getIMEI(this))
                .append("\nIMSI：").append(PhoneUtil.getIMSI(this))
                .append("\n终端类型PhoneType：").append(PhoneUtil.getPhoneType(this))
                .append("\nsim卡是否准备好：").append(PhoneUtil.isSimReady(this))
                .append("\nSim卡运营商名称：").append(PhoneUtil.getSimOperatorName(this))
                .append("\t\t").append(PhoneUtil.getSimOperatorCH(this))
                .append("\n\n手机信息：\n").append(JsonUtil.formatJson(Gson().toJson(PhoneUtil.getPhoneInfo(this))))
            alert(this, sb.toString())
        }
        btn2.setOnClickListener {
            val phone = il.inputText.trim()
            if (TextUtils.isEmpty(phone)) {
                showToast("请先输入手机号")
                return@setOnClickListener
            }
            showToast(if (PhoneUtil.isPhoneNumberValid(phone)) "手机号合法" else "非手机号")
        }
        btn3.setOnClickListener {
            PhoneUtil.dial(this, phoneNumber)
        }
        btn4.setOnClickListener {
            if (!PermissionUtil.requestPermissions(this, 1, Manifest.permission.CALL_PHONE)) {
                showToast("请先允许权限")
                return@setOnClickListener
            }
            val phone = il.inputText.trim()
            if (TextUtils.isEmpty(phone)) {
                showToast("请先输入手机号")
                return@setOnClickListener
            }
            if (PhoneUtil.isPhoneNumberValid(phone)) {
                PhoneUtil.call(this, phone)
            } else {
                showToast("非手机号")
            }
        }
        btn5.setOnClickListener {
            val content = il.inputText.trim()
            if (TextUtils.isEmpty(content)) {
                showToast("请先输入要发送的短信内容")
                return@setOnClickListener
            }
            PhoneUtil.sendSms(this, phoneNumber, content)
        }
        btn6.setOnClickListener {
            if (!PermissionUtil.requestPermissions(this, 1, Manifest.permission.SEND_SMS)) {
                showToast("请先允许权限")
                return@setOnClickListener
            }
            val content = il.inputText.trim()
            if (TextUtils.isEmpty(content)) {
                showToast("请先输入要发送的短信内容")
                return@setOnClickListener
            }
            PhoneUtil.sendSmsSilent(this, phoneNumber, content)
        }
        btn7.setOnClickListener {
            if (!PermissionUtil.requestPermissions(
                    this, 1,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.READ_CONTACTS
                )
            ) {
                showToast("请先允许权限")
                return@setOnClickListener
            }
            ThreadPoolManager.instance.getFixedThreadPool().submit(Runnable {
                val info = Gson().toJson(PhoneUtil.getContactInfo(this, 10))
                runOnUiThread {
                    alert(this, JsonUtil.formatJson(info))
                }
                LogUtil.logLongTag(
                    "获取所有手机号码",
                    "==============================================================\n" +
                            JsonUtil.formatJson(Gson().toJson(PhoneUtil.getAllContactInfo(this)))
                )
            })
        }
        btn8.setOnClickListener {
            if (!PermissionUtil.requestPermissions(
                    this, 1,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.READ_SMS
                )
            ) {
                showToast("请先允许权限")
                return@setOnClickListener
            }
            ThreadPoolManager.instance.getFixedThreadPool().submit(Runnable {
                val info = Gson().toJson(PhoneUtil.getSMSInfo(this, 10))
                runOnUiThread {
                    alert(this, JsonUtil.formatJson(info))
                }
            })
        }
    }

    //编码解码工具
    private fun testEncode() {
        initCommonLayout(this, "编码解码工具", false, true)
        val sb = StringBuilder()
        val url = "https://www.google.com/imghp?hl=zh-CN&tab=wi&ogbl"
        val urlEncode = EncodeUtil.urlEncode(url)
        val urlDecode = EncodeUtil.urlDecode(urlEncode)
        LogUtil.w(TAG, "URL编码：$urlEncode")
        LogUtil.w(TAG, "URL解码：$urlDecode")
        sb.append(url).append("\nURL编码：\n").append(urlEncode)
            .append("\nURL解码：\n").append(urlDecode)
        val content = "https://www.google.com/imghp?hl=zh-CN&tab=wi&content=测试Base64编码和解码"
        val base64EncodeBytes = EncodeUtil.base64Encode(content)
        val base64DecodeBytes = EncodeUtil.base64Decode(base64EncodeBytes)
        val base64EncodeStr = String(base64EncodeBytes!!)
        val base64DecodeStr = String(base64DecodeBytes!!)
        LogUtil.w(TAG, "Base64编码：$base64EncodeStr")
        LogUtil.w(TAG, "Base64解码：$base64DecodeStr")
        LogUtil.w(TAG, "Base64编码成字符串：" + EncodeUtil.base64EncodeToString(content.toByteArray()))
        LogUtil.w(TAG, "Base64 URL安全编码：" + String(EncodeUtil.base64UrlSafeEncode(content)!!))
        sb.append("\n\n").append(content).append("\nBase64编码：\n").append(base64EncodeStr)
            .append("\nBase64解码：\n").append(base64DecodeStr)
        val htmlContent = "<!Doctype html>\n" +
                "<html lang=\"zh_cn\">\n" +
                "<head>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n" +
                "<title>测试Html编码和解码</title>\n" +
                "</head>"
        val htmlEncode = EncodeUtil.htmlEncode(htmlContent)
        val htmlDecode = EncodeUtil.htmlDecode(htmlEncode)
        LogUtil.w(TAG, "Html编码：\n$htmlEncode")
        LogUtil.w(TAG, "Html解码：\n$htmlDecode")
        sb.append("\n\n").append(htmlContent).append("\nHtml编码：\n").append(htmlEncode)
            .append("\nHtml解码：\n").append(htmlDecode)
        tv.text = sb.toString()
    }

    //Service工具
    private fun testService() {
        initCommonLayout(
            this, "Service工具", false, true,
            "获取所有运行的服务", "服务是否在运行", "开启服务", "停止服务", "绑定服务", "解绑服务"
        )
        btn1.setOnClickListener {
            val list = ServiceUtil.getAllRunningServiceInfo(this)
            val sb = StringBuilder()
            if (!list.isNullOrEmpty()) {
                for (info in list) {
                    sb.append(info.service.className).append("\n")
                }
            }
            if (TextUtils.isEmpty(sb.toString())) {
                tv.text = "目前没有正在运行的服务"
            } else {
                tv.text = sb.toString()
            }
        }
        btn2.setOnClickListener {
            tv.text = if (ServiceUtil.isServiceRunning(this, TestService::class.java)) "TestService正在运行" else "TestService未运行"
        }
        btn3.setOnClickListener {
            ServiceUtil.startService(this, TestService::class.java)
        }
        btn4.setOnClickListener {
            ServiceUtil.stopService(this, TestService::class.java)
        }
        btn5.setOnClickListener {
            ServiceUtil.bindService(this, TestService::class.java, mConn)
        }
        btn6.setOnClickListener {
            if (ServiceUtil.isServiceRunning(this, TestService::class.java)) {
                ServiceUtil.unbindService(this, mConn)
            } else {
                showToast("服务尚未绑定")
            }
        }
    }

    private val mConn: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as TestService.LocalBinder
            showToast(binder.getService().getBindState())
        }

        override fun onServiceDisconnected(name: ComponentName) {
            //onServiceDisconnected在连接正常关闭的情况下是不会被调用的
            //该方法只在Service被破坏了或者被杀死的时候调用，例如系统资源不足时
        }
    }

    //位置工具
    private fun testLocation() {
        initCommonLayout(
            this, "位置工具", false, true,
            "开启位置监听服务", "打开设置界面", "申请位置权限"
        )
        bindService(Intent(this, LocationService::class.java), mLocationConn, Context.BIND_AUTO_CREATE)
        btn1.setOnClickListener {
            if (ServiceUtil.isServiceRunning(this, LocationService::class.java)) {
                showToast("服务正在运行")
            } else {
                bindService(Intent(this, LocationService::class.java), mLocationConn, Context.BIND_AUTO_CREATE)
            }
        }
        btn2.setOnClickListener {
            LocationUtil.openGPSSettings(this)
        }
        btn3.setOnClickListener {
            if (PermissionUtil.requestPermissions(
                    this, 1,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                showToast("已申请权限！")
            }
        }
    }

    private var mLocationConn: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val locationService = (service as LocationService.LocationBinder).getService()
            locationService.setOnLocationListener(object : LocationService.OnLocationListener {
                override fun initState(isSuccess: Boolean) {
                    if (isSuccess) {
                        showToast("位置监听初始化成功")
                    } else {
                        if (PermissionUtil.isPermissionGranted(
                                applicationContext,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        ) {
                            if (!LocationUtil.isGPSEnable(applicationContext)) {
                                showToast("请打开GPS")
                            } else if (!LocationUtil.isLocationEnable(applicationContext)) {
                                showToast("定位不可用")
                            }
                        } else {
                            showToast("请先允许权限")
                        }
                        unbindService()
                    }
                }

                override fun getLocation(
                    lastLatitude: String, lastLongitude: String,
                    latitude: String, longitude: String,
                    country: String, locality: String, street: String
                ) {
                    runOnUiThread {
                        val sb = StringBuilder()
                        sb.append("lastLatitude：").append(lastLatitude).append("\nlastLongitude：").append(lastLongitude)
                            .append("\nlatitude：").append(latitude).append("\nlongitude：").append(longitude)
                            .append("\ncountryName：").append(country).append("\nlocality：").append(locality)
                            .append("\nstreet：").append(street)
                        tv.text = sb.toString()
                    }
                }
            })
        }

        override fun onServiceDisconnected(name: ComponentName) {}
    }

    private fun unbindService() {
        unbindService(mLocationConn)
    }

    //网络工具
    private fun testNetwork() {
        initCommonLayout(
            this, "网络工具", false, false,
            "获取网络信息", "打开网络设置页面", "打开WiFi", "关闭WiFi"
        )
        btn1.setOnClickListener {
            val sb = StringBuilder()
            sb.append("网络是否连接：").append(NetworkUtil.isConnected(this))
                .append("\n网络是否可用：").append(NetworkUtil.isAvailable())
                .append("\n移动数据是否打开：").append(NetworkUtil.getDataEnabled(this))
                .append("\n网络是否是4G：").append(NetworkUtil.is4G(this))
                .append("\nwifi是否打开：").append(NetworkUtil.isWifiEnabled(this))
                .append("\nwifi是否连接：").append(NetworkUtil.isWifiConnected(this))
                .append("\nwifi是否可用：").append(NetworkUtil.isWifiAvailable(this))
                .append("\n当前网络类型：").append(NetworkUtil.getNetworkType(this))
                .append("\nIP地址：").append(NetworkUtil.getIPAddress(true))
                .append("\nbaidu.com对应的IP地址：").append(NetworkUtil.getDomainAddress("baidu.com"))
            alert(this, sb.toString())
        }
        btn2.setOnClickListener {
            NetworkUtil.openWirelessSettings(this)
        }
        btn3.setOnClickListener {
            NetworkUtil.setWifiEnabled(this, true)
        }
        btn4.setOnClickListener {
            NetworkUtil.setWifiEnabled(this, false)
        }
    }

    //版本升级
    private fun testApkDownload() {
        initCommonLayout(this, "应用下载", "下载QQ邮箱", "版本更新")
        val apkDownloadUtil = ApkDownloadUtil(this)
        btn1.setOnClickListener {
            apkDownloadUtil.downLoadApk("http://app.mail.qq.com/cgi-bin/mailapp?latest=y&from=2")
        }
        btn2.setOnClickListener {
            apkDownloadUtil.downLoadApk("https://ugc-download-2.imfir.cn/92d9763a68537b42d156507779b3a63635cf3909.apk?auth_key=1594538420-0-0-abce665cfe38f8e444a000dd9411587c")
        }
    }

    override fun onDestroy() {
        if (ServiceUtil.isServiceRunning(this, LocationService::class.java)) {
            unbindService(mLocationConn)
        }
        super.onDestroy()
    }

}