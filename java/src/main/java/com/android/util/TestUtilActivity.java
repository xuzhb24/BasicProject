package com.android.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.TrafficStats;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.text.InputType;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.android.frame.mvc.BaseActivity;
import com.android.java.R;
import com.android.java.databinding.ActivityCommonLayoutBinding;
import com.android.util.CapturePicture.CapturePictureUtil;
import com.android.util.CapturePicture.TestCaptureGridViewActivity;
import com.android.util.CapturePicture.TestCaptureListViewActivity;
import com.android.util.CapturePicture.TestCaptureRecyclerViewActivity;
import com.android.util.CapturePicture.TestCaptureWebViewActivity;
import com.android.util.StatusBar.TestStatusBarUtilActivity;
import com.android.util.activity.ActivityUtil;
import com.android.util.activity.TestJumpActivity;
import com.android.util.app.AATest.TestAppListActivity;
import com.android.util.app.AppUtil;
import com.android.util.bitmap.BitmapUtil;
import com.android.util.location.LocationService;
import com.android.util.location.LocationUtil;
import com.android.util.permission.PermissionUtil;
import com.android.util.regex.RegexUtil;
import com.android.util.service.ServiceUtil;
import com.android.util.service.TestService;
import com.android.util.sharedPreferences.withoutContext.SPUtil;
import com.android.util.threadPool.ThreadPoolManager;
import com.android.util.traffic.NetworkStatsHelper;
import com.android.util.traffic.TrafficInfo;
import com.android.util.traffic.TrafficStatsUtil;
import com.android.widget.InputLayout;
import com.android.widget.RecyclerView.AATest.entity.MonthBean;
import com.google.gson.Gson;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by xuzhb on 2019/10/20
 * Desc:测试工具类方法
 */
public class TestUtilActivity extends BaseActivity<ActivityCommonLayoutBinding> {

    private static final String TAG = "TestUtilActivity";

    public static final String TEST_STATUS_BAR = "TEST_STATUS_BAR";
    public static final String TEST_DATE = "TEST_DATE";
    public static final String TEST_DRAWABLE = "TEST_DRAWABLE";
    public static final String TEST_STRING = "TEST_STRING";
    public static final String TEST_KEYBOARD = "TEST_KEYBOARD";
    public static final String TEST_NOTIFICATION = "TEST_NOTIFICATION";
    public static final String TEST_TRAFFICSTATS = "TEST_TRAFFICSTATS";
    public static final String TEST_NETWORK_STATS = "TEST_NETWORK_STATS";
    public static final String TEST_CONTINUOUS_CLICK = "TEST_CONTINUOUS_CLICK";
    public static final String TEST_PINYIN = "TEST_PINYIN";
    public static final String TEST_ACTIVITY = "TEST_ACTIVITY";
    public static final String TEST_APP = "TEST_APP";
    public static final String TEST_DEVICE = "TEST_DEVICE";
    public static final String TEST_SHELL = "TEST_SHELL";
    public static final String TEST_PICKER_VIEW = "TEST_PICKER_VIEW";
    public static final String TEST_CRASH = "TEST_CRASH";
    public static final String TEST_CLEAN = "TEST_CLEAN";
    public static final String TEST_SDCARD = "TEST_SDCARD";
    public static final String TEST_SCREEN = "TEST_SCREEN";
    public static final String TEST_CACHE = "TEST_CACHE";
    public static final String TEST_SP = "TEST_SP";
    public static final String TEST_LAYOUT_PARAMS = "TEST_LAYOUT_PARAMS";
    public static final String TEST_PHONE = "TEST_PHONE";
    public static final String TEST_REGEX = "TEST_REGEX";
    public static final String TEST_ENCODE = "TEST_ENCODE";
    public static final String TEST_SERVICE = "TEST_SERVICE";
    public static final String TEST_LOCATION = "TEST_LOCATION";
    public static final String TEST_NETWORK = "TEST_NETWORK";
    public static final String TEST_APK_DOWNLOAD = "TEST_APK_DOWNLOAD";
    public static final String TEST_SPANNABLE_STRING = "TEST_SPANNABLE_STRING";
    public static final String TEST_CPU = "TEST_CPU";
    public static final String TEST_VIBRATION = "TEST_VIBRATION";
    public static final String TEST_AUDIO = "TEST_AUDIO";
    public static final String TEST_BRIGHTNESS = "TEST_BRIGHTNESS";
    public static final String TEST_CAPTURE = "TEST_CAPTURE";

    private LinearLayout ll;
    private InputLayout il;
    private TextView tv;
    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    private Button btn5;
    private Button btn6;
    private Button btn7;
    private Button btn8;
    private Button btn9;
    private Button btn10;
    private Button btn11;
    private Button btn12;

    @Override
    public void handleView(Bundle savedInstanceState) {
        ll = binding.ll;
        il = binding.il;
        tv = binding.tv;
        btn1 = binding.btn1;
        btn2 = binding.btn2;
        btn3 = binding.btn3;
        btn4 = binding.btn4;
        btn5 = binding.btn5;
        btn6 = binding.btn6;
        btn7 = binding.btn7;
        btn8 = binding.btn8;
        btn9 = binding.btn9;
        btn10 = binding.btn10;
        btn11 = binding.btn11;
        btn12 = binding.btn12;
        switch (getIntent().getStringExtra(CommonLayoutUtil.MODULE_NAME)) {
            case TEST_STATUS_BAR:
                testStatusBarUtil();
                break;
            case TEST_DATE:
                testDateUtil();
                break;
            case TEST_KEYBOARD:
                testKeyBoardUtil();
                break;
            case TEST_DRAWABLE:
                testDrawableUtil();
                break;
            case TEST_STRING:
                testStringUtil();
                break;
            case TEST_NOTIFICATION:
                testNotification();
                break;
            case TEST_TRAFFICSTATS:
                testTrafficStats();
                break;
            case TEST_NETWORK_STATS:
                testNetworkStats();
                break;
            case TEST_CONTINUOUS_CLICK:
                testContinuousClick();
                break;
            case TEST_PINYIN:
                testPinyin();
                break;
            case TEST_ACTIVITY:
                testActivity();
                break;
            case TEST_APP:
                testApp();
                break;
            case TEST_DEVICE:
                testDevice();
                break;
            case TEST_SHELL:
                testShell();
                break;
            case TEST_PICKER_VIEW:
                testPickerView();
                break;
            case TEST_CRASH:
                testCrash();
                break;
            case TEST_CLEAN:
                testClean();
                break;
            case TEST_SDCARD:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    testSdcard();
                }
                break;
            case TEST_SCREEN:
                testScreen();
                break;
            case TEST_CACHE:
                testCache();
                break;
            case TEST_SP:
                testSP();
                break;
            case TEST_LAYOUT_PARAMS:
                testLayoutParams();
                break;
            case TEST_PHONE:
                testPhone();
                break;
            case TEST_REGEX:
                testRegex();
                break;
            case TEST_ENCODE:
                testEncode();
                break;
            case TEST_SERVICE:
                testService();
                break;
            case TEST_LOCATION:
                testLocation();
                break;
            case TEST_NETWORK:
                testNetwork();
                break;
            case TEST_APK_DOWNLOAD:
                testApkDownload();
                break;
            case TEST_SPANNABLE_STRING:
                testSpannableString();
                break;
            case TEST_CPU:
                testCPU();
                break;
            case TEST_VIBRATION:
                testVibration();
                break;
            case TEST_AUDIO:
                testAudio();
                break;
            case TEST_BRIGHTNESS:
                testBrightness();
                break;
            case TEST_CAPTURE:
                testCapture();
                break;
        }
    }

    @Override
    public void initListener() {

    }

    private void testStatusBarUtil() {
        String text1 = "沉浸背景图片";
        String text2 = "状态栏白色，字体和图片黑色";
        String text3 = "状态栏黑色，字体和图片白色";
        String text4 = "状态栏黑色半透明，字体和图片白色";
        String text5 = "隐藏导航栏";
        String text6 = "导航栏和状态栏透明";
        String text7 = "透明度和十六进制对应表";
        CommonLayoutUtil.initCommonLayout(this, "实现沉浸式状态栏", text1, text2, text3, text4, text5, text6, text7);
        btn1.setOnClickListener(v -> {
            jumpToTestStatusBarActivity(1, text1);
        });
        btn2.setOnClickListener(v -> {
            jumpToTestStatusBarActivity(2, text2);
        });
        btn3.setOnClickListener(v -> {
            jumpToTestStatusBarActivity(3, text3);
        });
        btn4.setOnClickListener(v -> {
            jumpToTestStatusBarActivity(4, text4);
        });
        btn5.setOnClickListener(v -> {
            jumpToTestStatusBarActivity(5, text5);
        });
        btn6.setOnClickListener(v -> {
            jumpToTestStatusBarActivity(6, text6);
        });
        btn7.setOnClickListener(v -> {
            String content = IOUtil.readInputStreameToString(getResources().openRawResource(R.raw.hex_alpha_table));
            ExtraUtil.alert(this, content);
        });
    }

    private void jumpToTestStatusBarActivity(int type, String text) {
        Intent intent = new Intent(this, TestStatusBarUtilActivity.class);
        intent.putExtra(TestStatusBarUtilActivity.EXTRA_TYPE, type);
        intent.putExtra(TestStatusBarUtilActivity.EXTRA_TEXT, text);
        startActivity(intent);
    }

    private void testDateUtil() {
        CommonLayoutUtil.initCommonLayout(this, "测试时间工具", false, true);
        StringBuilder sb = new StringBuilder();
        //测试getCurrentDateTime
        sb.append("当前时间\n").append(DateUtil.getCurrentDateTime())
                .append("\n").append(DateUtil.getCurrentDateTime(DateUtil.Y_M_D_H_M_S_S));
        //测试date2String和string2Date
        sb.append("\n\nDate <--> String\n").append(DateUtil.date2String(new Date(), DateUtil.Y_M_D_H_M_S))
                .append("\n").append(DateUtil.date2String(DateUtil.string2Date("20200202", DateUtil.YMD), DateUtil.Y_M_D_H_M_S_S));
        //测试long2String和string2Long
        sb.append("\n\nLong <--> String\n").append(DateUtil.long2String(System.currentTimeMillis(), DateUtil.Y_M_D_H_M_S_S))
                .append("\n").append(DateUtil.long2String(DateUtil.string2Long("20200202", DateUtil.YMD), DateUtil.Y_M_D_H_M_S_S));
        //测试date2Long和long2Date
        Date dateLong = new Date();
        sb.append("\n\nDate <--> Long\n").append(DateUtil.date2Long(dateLong))
                .append("\n").append(DateUtil.date2Long(DateUtil.long2Date(dateLong.getTime())));
        //测试isLeapYear
        sb.append("\n\n是否是闰年：\n").append("2000：").append(DateUtil.isLeapYear(2000))
                .append("\n").append("2012：").append(DateUtil.isLeapYear(2012))
                .append("\n").append("2019：").append(DateUtil.isLeapYear(2019));
        //测试convertOtherFormat
        String convertTime = DateUtil.getCurrentDateTime();
        sb.append("\n\n转换时间格式\n").append(convertTime).append(" -> ")
                .append(DateUtil.convertOtherFormat(convertTime, DateUtil.Y_M_D_H_M_S, "yyyyMMddHHmmssSSS"));
        //测试compareDate
        String compareTime1 = "2019-02-28";
        String compareTime2 = "2019-02-28";
        String compareTime3 = "2019-02-27";
        String compareTime4 = "2019-03-01";
        sb.append("\n\n比较日期大小\n")
                .append(compareTime1).append(" ").append(compareTime2).append("：")
                .append(DateUtil.compareDate(compareTime1, compareTime2, DateUtil.Y_M_D)).append("\n")
                .append(compareTime1).append(" ").append(compareTime3).append("：")
                .append(DateUtil.compareDate(compareTime1, compareTime3, DateUtil.Y_M_D)).append("\n")
                .append(compareTime1).append(" ").append(compareTime4).append("：")
                .append(DateUtil.compareDate(compareTime1, compareTime4, DateUtil.Y_M_D));
        //测试getDistanceDateByYear
        String distanceDate = "2020-02-02 12:11:03.123";
        sb.append("\n\nN年前/后的日期\n").append(DateUtil.getCurrentDateTime(DateUtil.Y_M_D))
                .append("\n1年前：").append(DateUtil.getDistanceDateByYear(-1, DateUtil.Y_M_D))
                .append("\n2年后：").append(DateUtil.getDistanceDateByYear(2, DateUtil.Y_M_D))
                .append("\n").append(distanceDate)
                .append("\n3年前：").append(DateUtil.getDistanceDateByYear(-3, DateUtil.Y_M_D_H_M_S_S, distanceDate))
                .append("\n4年后：").append(DateUtil.getDistanceDateByYear(4, DateUtil.Y_M_D_H_M_S_S, distanceDate));
        //测试getDistanceDateByMonth
        sb.append("\n\nN月前/后的日期\n").append(DateUtil.getCurrentDateTime(DateUtil.Y_M_D))
                .append("\n1月前：").append(DateUtil.getDistanceDateByMonth(-1, DateUtil.Y_M_D))
                .append("\n2月后：").append(DateUtil.getDistanceDateByMonth(2, DateUtil.Y_M_D))
                .append("\n").append(distanceDate)
                .append("\n3月前：").append(DateUtil.getDistanceDateByMonth(-3, DateUtil.Y_M_D_H_M_S_S, distanceDate))
                .append("\n4月后：").append(DateUtil.getDistanceDateByMonth(4, DateUtil.Y_M_D_H_M_S_S, distanceDate));
        //测试getDistanceDateByWeek
        sb.append("\n\nN周前/后的日期\n").append(DateUtil.getCurrentDateTime(DateUtil.Y_M_D))
                .append("\n1周前：").append(DateUtil.getDistanceDateByWeek(-1, DateUtil.Y_M_D))
                .append("\n2周后：").append(DateUtil.getDistanceDateByWeek(2, DateUtil.Y_M_D))
                .append("\n").append(distanceDate)
                .append("\n3周前：").append(DateUtil.getDistanceDateByWeek(-3, DateUtil.Y_M_D_H_M_S_S, distanceDate))
                .append("\n4周后：").append(DateUtil.getDistanceDateByWeek(4, DateUtil.Y_M_D_H_M_S_S, distanceDate));
        //测试getDistanceDateByDay
        sb.append("\n\nN天前/后的日期\n").append(DateUtil.getCurrentDateTime(DateUtil.Y_M_D))
                .append("\n1天前：").append(DateUtil.getDistanceDateByDay(-1, DateUtil.Y_M_D))
                .append("\n2天后：").append(DateUtil.getDistanceDateByDay(2, DateUtil.Y_M_D))
                .append("\n").append(distanceDate)
                .append("\n3天前：").append(DateUtil.getDistanceDateByDay(-3, DateUtil.Y_M_D_H_M_S_S, distanceDate))
                .append("\n4天后：").append(DateUtil.getDistanceDateByDay(4, DateUtil.Y_M_D_H_M_S_S, distanceDate));
        //测试getDistanceDay
        String distanceDay1 = "2020-02-02";
        String distanceDay2 = "2020-01-31";
        sb.append("\n\n间隔天数\n").append(distanceDay1).append(" ").append(distanceDay2).append("：")
                .append(DateUtil.getDistanceDay(distanceDay1, distanceDay2, DateUtil.Y_M_D));
        //测试getDistanceSecond
        String distanceSecond1 = "12:11:56.786";
        String distanceSecond2 = "12:12:03.123";
        sb.append("\n\n间隔秒数\n").append(distanceSecond1).append(" ").append(distanceSecond2).append("：")
                .append(DateUtil.getDistanceSecond(distanceSecond1, distanceSecond2, DateUtil.H_M_S_S));
        //测试isInThePeriod
        String startDate = "2020-02-02 12:23:45.004";
        String endDate = "2020-12-02 12:23:45.004";
        String dateTimeInThePeriod1 = "2020-02-02 12:23:45.002";
        String dateTimeInThePeriod2 = "2020-12-02 12:23:46.002";
        sb.append("\n\n是否在某个时间段\n").append("[").append(startDate).append(" - ").append(endDate).append("]\n")
                .append(DateUtil.getCurrentDateTime()).append("：")
                .append(DateUtil.isInThePeriod(startDate, endDate, DateUtil.Y_M_D_H_M_S))
                .append("\n").append(dateTimeInThePeriod1).append("：")
                .append(DateUtil.isInThePeriod(startDate, endDate, DateUtil.Y_M_D_H_M_S, dateTimeInThePeriod1))
                .append("\n").append(dateTimeInThePeriod2).append("：")
                .append(DateUtil.isInThePeriod(startDate, endDate, DateUtil.Y_M_D_H_M_S, dateTimeInThePeriod2));
        //测试getStartTimeOfToday和getStartTimeOfDay
        String startTime = "2020-02-02";
        sb.append("\n\n某天零点时间\n").append(DateUtil.getCurrentDateTime(DateUtil.Y_M_D)).append("：")
                .append(DateUtil.long2String(DateUtil.getStartTimeOfToday(), DateUtil.Y_M_D_H_M_S_S))
                .append("\n").append(startTime).append("：")
                .append(DateUtil.long2String(DateUtil.getStartTimeOfDay(startTime, DateUtil.Y_M_D), DateUtil.Y_M_D_H_M_S_S));
        //测试getStartTimeOfCurrentMonth和getStartTimeOfMonth
        sb.append("\n\n某月零点时间\n").append(DateUtil.getCurrentDateTime(DateUtil.Y_M)).append("：")
                .append(DateUtil.long2String(DateUtil.getStartTimeOfCurrentMonth(), DateUtil.Y_M_D_H_M_S_S))
                .append("\n").append(DateUtil.convertOtherFormat(startTime, DateUtil.Y_M_D, DateUtil.Y_M)).append("：")
                .append(DateUtil.long2String(DateUtil.getStartTimeOfMonth(startTime, DateUtil.Y_M_D), DateUtil.Y_M_D_H_M_S_S));
        //测试getEndTimeOfToday和getEndTimeOfDay
        String endTime = "2020-02-02";
        sb.append("\n\n某天最后时间\n").append(DateUtil.getCurrentDateTime(DateUtil.Y_M_D)).append("：")
                .append(DateUtil.long2String(DateUtil.getEndTimeOfToday(), DateUtil.Y_M_D_H_M_S_S))
                .append("\n").append(endTime).append("：")
                .append(DateUtil.long2String(DateUtil.getEndTimeOfDay(startTime, DateUtil.Y_M_D), DateUtil.Y_M_D_H_M_S_S));
        //测试getEndTimeOfCurrentMonth和getEndTimeOfMonth
        sb.append("\n\n某月最后时间\n").append(DateUtil.getCurrentDateTime(DateUtil.Y_M)).append("：")
                .append(DateUtil.long2String(DateUtil.getEndTimeOfCurrentMonth(), DateUtil.Y_M_D_H_M_S_S))
                .append("\n").append(DateUtil.convertOtherFormat(startTime, DateUtil.Y_M_D, DateUtil.Y_M)).append("：")
                .append(DateUtil.long2String(DateUtil.getEndTimeOfMonth(startTime, DateUtil.Y_M_D), DateUtil.Y_M_D_H_M_S_S));
        //测试getCurrentDayOfWeekCH和getDayOfWeekCH
        String weekTime = "2020-02-02";
        sb.append("\n\n判断周几\n").append(DateUtil.getCurrentDateTime()).append("：")
                .append(DateUtil.getCurrentDayOfWeekCH()).append("\n").append(weekTime).append("：")
                .append(DateUtil.getDayOfWeekCH(weekTime, DateUtil.Y_M_D));
        tv.setText(sb.toString());
    }

    private void testKeyBoardUtil() {
        CommonLayoutUtil.initCommonLayout(this, "测试键盘工具", true, true,
                "弹出软键盘", "收起软键盘", "复制到剪切板", "获取剪切板的内容");
        il.setInputTextHint("请输入要复制到剪切板的内容");
        btn1.setOnClickListener(v -> {
            KeyboardUtil.showSoftInput(getApplicationContext(), v);
        });
        btn2.setOnClickListener(v -> {
            KeyboardUtil.hideSoftInput(getApplicationContext(), v);
        });
        btn3.setOnClickListener(v -> {
            if (TextUtils.isEmpty(il.getInputText().trim())) {
                showToast("请先输入内容");
                return;
            }
            KeyboardUtil.copyToClipboard(getApplicationContext(), il.getInputText());
        });
        btn4.setOnClickListener(v -> {
            tv.setText(KeyboardUtil.getClipboardText(this));
        });
    }

    private void testDrawableUtil() {
        CommonLayoutUtil.initCommonLayout(this, "代码创建Drawable", "按钮", "solid", "stroke", "虚线stroke", "solid和stroke");
        btn2.setOnClickListener(v -> {
            GradientDrawable drawable = DrawableUtil.createSolidShape(
                    SizeUtil.dp2px(10f),
                    getResources().getColor(R.color.orange)
            );
            btn1.setBackground(drawable);
            btn1.setTextColor(getResources().getColor(R.color.white));
        });
        btn3.setOnClickListener(v -> {
            GradientDrawable drawable = DrawableUtil.createStrokeShape(
                    SizeUtil.dp2px(15),
                    (int) SizeUtil.dp2px(1),
                    getResources().getColor(R.color.orange)
            );
            btn1.setBackground(drawable);
            btn1.setTextColor(getResources().getColor(R.color.orange));
        });
        btn4.setOnClickListener(v -> {
            GradientDrawable drawable = DrawableUtil.createStrokeShape(
                    SizeUtil.dp2px(15),
                    (int) SizeUtil.dp2px(1),
                    getResources().getColor(R.color.orange),
                    SizeUtil.dp2px(2f),
                    SizeUtil.dp2px(2f)
            );
            btn1.setBackground(drawable);
            btn1.setTextColor(getResources().getColor(R.color.orange));
        });
        btn5.setOnClickListener(v -> {
            GradientDrawable drawable = DrawableUtil.createSolidStrokeShape(
                    SizeUtil.dp2px(25),
                    getResources().getColor(R.color.white),
                    (int) SizeUtil.dp2px(2),
                    getResources().getColor(R.color.orange)
            );
            btn1.setBackground(drawable);
            btn1.setTextColor(getResources().getColor(R.color.orange));
        });
    }

    private void testStringUtil() {
        CommonLayoutUtil.initCommonLayout(this, "字符串工具类", false, true,
                "显示不同颜色", "带下划线", "带点击事件", "反转字符串", "转化为半角字符", "转化为全角字符");
        String content = "欢迎拨打热线电话";
        tv.setTextColor(Color.BLACK);
        tv.setTextSize(15);
        btn1.setOnClickListener(v -> {
            tv.setText(StringUtil.createTextSpan(
                    content, 4, 8,
                    getResources().getColor(R.color.orange),
                    (int) SizeUtil.sp2px(18),
                    Typeface.ITALIC, false, null
            ));
        });
        btn2.setOnClickListener(v -> {
            tv.setText(StringUtil.createTextSpan(
                    content, 4, 8,
                    getResources().getColor(R.color.orange),
                    (int) SizeUtil.sp2px(18),
                    Typeface.NORMAL, true, null
            ));
        });
        btn3.setOnClickListener(v -> {
            tv.setMovementMethod(LinkMovementMethod.getInstance());  //必须设置这个点击事件才能生效
            tv.setText(StringUtil.createTextSpan(
                    content, 4, 8,
                    getResources().getColor(R.color.orange),
                    (int) SizeUtil.sp2px(18),
                    Typeface.BOLD_ITALIC, true,
                    view -> {
                        showToast("热线电话：10086");
                    }
            ));
        });
        btn4.setOnClickListener(v -> {
            String result = content + "\n" + StringUtil.reverse(content);
            tv.setText(result);
        });
        btn5.setOnClickListener(v -> {
            String s = "123ABCabc";
            String result = s + "\n" + StringUtil.toDBC(s);
            tv.setText(result);
        });
        btn6.setOnClickListener(v -> {
            String s = "123ABCabc";
            String result = s + "\n" + StringUtil.toSBC(s);
            tv.setText(result);
        });
    }

    //通知管理
    private void testNotification() {
        String extraCpntent = getIntent().getStringExtra("content");
        tv.setText(extraCpntent);  //传递过来的参数
        InputLayout titleIl = CommonLayoutUtil.createInputLayout(this, "请输入标题");
        ll.addView(titleIl, 0);
        il.setInputTextHint("请输入内容");
        CommonLayoutUtil.initCommonLayout(this, "通知管理", true, true,
                "自定义通知", "自定义通知（带跳转）", "通知是否打开", "跳转通知设置界面");
        btn1.setOnClickListener(v -> {
            String title = titleIl.getInputText().trim();
            if (TextUtils.isEmpty(title)) {
                title = "这是标题";
            }
            String content = il.getInputText().trim();
            if (TextUtils.isEmpty(content)) {
                content = "这是内容";
            }
            NotificationUtil.showNotification(getApplicationContext(), title, content);
        });
        btn2.setOnClickListener(v -> {
            String title = titleIl.getInputText().trim();
            if (TextUtils.isEmpty(title)) {
                title = "这是标题";
            }
            String content = il.getInputText().trim();
            if (TextUtils.isEmpty(content)) {
                content = "跳转到通知管理页面";
            }
            Intent intent = new Intent(TestUtilActivity.this, TestUtilActivity.class);
            intent.putExtra(CommonLayoutUtil.MODULE_NAME, TEST_NOTIFICATION);
            intent.putExtra("content", content);
            NotificationUtil.showNotificationWithIntent(TestUtilActivity.this, intent, title, content);
            finish();
        });
        btn3.setOnClickListener(v -> {
            boolean isOpen = NotificationUtil.isNotificationEnabled(getApplicationContext());
            tv.setText(isOpen ? "通知权限已打开" : "通知权限被关闭");
        });
        btn4.setOnClickListener(v -> {
            NotificationUtil.gotoNotificationSetting(getApplicationContext());
        });
    }

    //流量工具
    private void testTrafficStats() {
        CommonLayoutUtil.initCommonLayout(this, "TrafficStats", false, true, "流量统计", "已安装应用流量统计", "已安装应用流量排行");
        String spacing = "=================================================================================\n";
        btn1.setOnClickListener(v -> {
            StringBuilder sb = new StringBuilder();
            sb.append("开机以来(B)\t\t上传\t\t下载\t\t总和\n");
            sb.append("总流量\t\t" + format(TrafficStats.getTotalRxBytes()) + "\t\t" + format(TrafficStats.getTotalTxBytes()) +
                    "\t\t" + format(TrafficStats.getTotalRxBytes() + TrafficStats.getTotalTxBytes()) + "\n");
            sb.append("总数据包\t\t" + TrafficStats.getTotalRxPackets() + "\t\t" + TrafficStats.getTotalTxPackets() +
                    "\t\t" + (TrafficStats.getTotalRxPackets() + TrafficStats.getTotalTxPackets()) + "\n");
            sb.append("Mobile流量\t\t" + format(TrafficStats.getMobileRxBytes()) + "\t\t" + format(TrafficStats.getMobileTxBytes()) +
                    "\t\t" + format(TrafficStats.getMobileRxBytes() + TrafficStats.getMobileTxBytes()) + "\n");
            sb.append("Mobile数据包\t\t" + TrafficStats.getMobileRxPackets() + "\t\t" + TrafficStats.getMobileTxPackets() +
                    "\t\t" + (TrafficStats.getMobileRxPackets() + TrafficStats.getMobileTxPackets()) + "\n");
            sb.append("本应用流量\t\t" + TrafficStatsUtil.getLocalAppTotalBytes(this, ByteUnit.B) + "\t" +
                    TrafficStatsUtil.getLocalAppTotalBytes(this, ByteUnit.KB) + "\t" +
                    SystemUtil.getUid(this, getPackageName()) + "\n");
            LogUtil.i("流量统计", " \n" + spacing + sb + spacing);
        });
        btn2.setOnClickListener(v -> {
            new Thread(() -> {
                List<TrafficInfo> list = TrafficStatsUtil.getAppTotalBytesList(this, ByteUnit.MB);
                StringBuilder sb = new StringBuilder();
                for (TrafficInfo info : list) {
                    sb.append(info + "\n");
                }
                LogUtil.logLongTag("流量列表(MB)", " \n" + spacing + sb + spacing);
            }).start();
        });
        btn3.setOnClickListener(v -> {
            new Thread(() -> {
                List<TrafficInfo> list = TrafficStatsUtil.getAppTotalBytesSortList(this, ByteUnit.MB, 5);
                StringBuilder sb = new StringBuilder();
                for (TrafficInfo info : list) {
                    sb.append(info + "\n");
                }
                LogUtil.logLongTag("流量排行(MB)", " \n" + spacing + sb + spacing);
            }).start();
        });
    }

    @SuppressLint("DefaultLocale")
    private String format(long value) {
        return String.format("%.2f", (value / 1024f / 1024f));
    }

    //流量统计
    private void testNetworkStats() {
        CommonLayoutUtil.initCommonLayout(this, "NetworkStatsManager", false, true,
                "流量统计", "已安装应用流量统计", "已安装应用流量排行", "是否有权限", "跳转权限申请页面");
        String spacing = "=================================================================================\n";
        btn1.setOnClickListener(v -> {
            StringBuilder sb = new StringBuilder();
            sb.append("Wifi流量\t" + NetworkStatsHelper.getInstance().getWifiTotalBytes(ByteUnit.MB) + "\n");
            sb.append("Mobile流量\t" + NetworkStatsHelper.getInstance().getMobileTotalBytes(ByteUnit.MB) + "\n");
            int uid = SystemUtil.getUid(this, getPackageName());
            double wifiBytes = NetworkStatsHelper.getInstance().getAppWifiTotalBytes(0, uid, ByteUnit.B);
            double mobileBytes = NetworkStatsHelper.getInstance().getAppMobileTotalBytes(0, uid, ByteUnit.B);
            sb.append("本应用Wifi流量\t" + wifiBytes + "\n");
            sb.append("本应用Moile流量\t" + mobileBytes + "\n");
            LogUtil.i("流量统计", "  \n" + spacing + sb + spacing);
        });
        btn2.setOnClickListener(v -> {
            new Thread(() -> {
                showToast("正在获取...");
                List<TrafficInfo> list = NetworkStatsHelper.getInstance().getAppTotalBytesList(ConnectivityManager.TYPE_MOBILE
                        , 0, System.currentTimeMillis(), ByteUnit.MB);
                StringBuilder sb = new StringBuilder();
                for (TrafficInfo info : list) {
                    sb.append(info + "\n");
                }
                LogUtil.logLongTag("流量列表(MB)", " \n" + spacing + sb + spacing);
                showToast("获取成功，请查看日志");
            }).start();
        });
        btn3.setOnClickListener(v -> {
            new Thread(() -> {
                showToast("正在获取...");
                StringBuilder sb = new StringBuilder();

                sb.append("总流量排行前10(MB)\n");
                List<TrafficInfo> totalList = NetworkStatsHelper.getInstance().getAppMobileTotalBytesSortList(0, ByteUnit.MB, 10);
                for (TrafficInfo info : totalList) {
                    sb.append(info + "\n");
                }

                sb.append("\n\n本月流量排行前10(MB)\n");
                List<TrafficInfo> monthList = NetworkStatsHelper.getInstance().getAppMobileTotalBytesSortList(DateUtil.getStartTimeOfCurrentMonth(), ByteUnit.MB, 10);
                for (TrafficInfo info : monthList) {
                    sb.append(info + "\n");
                }

                sb.append("\n\n今日流量排行前10(MB)\n");
                List<TrafficInfo> dayList = NetworkStatsHelper.getInstance().getAppMobileTotalBytesSortList(DateUtil.getStartTimeOfToday(), ByteUnit.MB, 10);
                for (TrafficInfo info : dayList) {
                    sb.append(info + "\n");
                }

                sb.append("\n\n昨日流量排行前10(MB)\n");
                String formatStr = DateUtil.Y_M_D_H_M_S;
                String yesterdayStr = DateUtil.getDistanceDateByDay(-1, formatStr);
                List<TrafficInfo> yesterdayList = NetworkStatsHelper.getInstance().getAppTotalBytesSortList(ConnectivityManager.TYPE_MOBILE,
                        DateUtil.getStartTimeOfDay(yesterdayStr, formatStr), DateUtil.getEndTimeOfDay(yesterdayStr, formatStr), ByteUnit.MB, 10);
                for (TrafficInfo info : yesterdayList) {
                    sb.append(info + "\n");
                }

                LogUtil.logLongTag("流量列表(MB)", " \n" + spacing + sb + spacing);
                showToast("获取成功，请查看日志");
            }).start();
        });
        btn4.setOnClickListener(v -> {
            if (NetworkStatsHelper.hasPermissionToReadNetworkStats(this)) {
                showToast("已申请权限");
            } else {
                showToast("无权限");
            }
        });
        btn5.setOnClickListener(v -> {
            NetworkStatsHelper.requestReadNetworkStats();
        });
    }


    private long mLastClickTime = 0;

    //连续点击事件监听
    private void testContinuousClick() {
        CommonLayoutUtil.initCommonLayout(this, "连续点击", false, true,
                "连续点击", "连续点击(点击最大时间间隔2秒)");
        tv.setGravity(Gravity.CENTER);
        btn1.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v, int clickCount) {
                long currentClickTime = System.currentTimeMillis();
                StringBuilder sb = new StringBuilder();
                sb.append("点击次数:").append(clickCount).append("\n距离上次点击的时间间隔:")
                        .append(currentClickTime - mLastClickTime).append("ms\n")
                        .append("当前默认最大时间间隔:").append(getClickInterval()).append("ms");
                tv.setText(sb.toString());
                mLastClickTime = currentClickTime;
            }
        });
        btn2.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v, int clickCount) {
                long currentClickTime = System.currentTimeMillis();
                StringBuilder sb = new StringBuilder();
                sb.append("点击次数:").append(clickCount).append("\n距离上次点击的时间间隔:")
                        .append(currentClickTime - mLastClickTime).append("ms\n")
                        .append("当前默认最大时间间隔:").append(getClickInterval()).append("ms");
                tv.setText(sb.toString());
                mLastClickTime = currentClickTime;
            }

            @Override
            protected int getClickInterval() {
                return 2000;
            }
        });
    }

    //拼音工具
    private void testPinyin() {
        CommonLayoutUtil.initCommonLayout(this, "拼音工具", true, true,
                "获取汉字拼音", "获取姓氏拼音");
        il.setInputText("测试拼音工具");
        String s = il.getInputText().trim();
        String text = "汉字拼音：" + PinyinUtil.hanzi2Pinyin(s) +
                "\n首字母拼音：" + PinyinUtil.getFirstLetter(s);
        il.getEditText().setSelection(s.length());
        tv.setText(text);
        btn1.setOnClickListener(v -> {
            String content = il.getInputText().trim();
            String split = " ";
            String result = "汉字拼音：" + PinyinUtil.hanzi2Pinyin(content, split) +
                    "\n首字母拼音：" + PinyinUtil.getFirstLetter(content, split);
            tv.setText(result);
        });
        btn2.setOnClickListener(v -> {
            String name = il.getInputText().trim();
            String result = "姓氏的拼音：" + PinyinUtil.getSurnamePinyin(name) +
                    "\n首字母拼音：" + PinyinUtil.getSurnameFirstLetter(name);
            tv.setText(result);
        });
        il.setOnTextClearListener(() -> {
            tv.setText("");
        });
    }

    //Activity工具
    private void testActivity() {
        CommonLayoutUtil.initCommonLayout(this, "Activity工具", false, true,
                "跳转到东方财富", "跳转Activity(带动画)", "跳转Activity(携带参数)");
        StringBuilder sb = new StringBuilder();
        sb.append("MainActivity是否存在：")
                .append(ActivityUtil.isActivityExists(this, "com.android.java",
                        "com.android.base.MainActivity"))
                .append("\n启动Activity名：")
                .append(ActivityUtil.getLauncherActivityName(this, getPackageName()));
        String[] topActivity = ActivityUtil.getTopActivityName(this).split(" ");
        sb.append("\n\n栈顶Activity信息：\n")
                .append(topActivity[0]).append("(包名)\n")
                .append(topActivity[1]).append("(类名)");
        tv.setText(sb.toString());
        btn1.setOnClickListener(v -> {
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
            String packageName = "com.eastmoney.android.berlin";
            String className = "com.eastmoney.android.berlin.activity.MainActivity";
            if (ActivityUtil.isActivityExists(this, packageName, className)) {
                ActivityUtil.startActivity(this, packageName, className);
            } else {
                showToast("不存在该应用");
            }
        });
        btn2.setOnClickListener(v -> {
            ActivityUtil.startActivity(this, TestJumpActivity.class,
                    R.anim.slide_right_in, R.anim.slide_left_out);
        });
        btn3.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString(TestJumpActivity.EXTRA_DATA, btn3.getText().toString());
            ActivityUtil.startActivity(this, TestJumpActivity.class, bundle);
        });
    }

    //App工具
    private void testApp() {
        CommonLayoutUtil.initCommonLayout(this, "App工具",
                "本应用相关信息", "已安装应用列表", "打开应用设置页面",
                "安装测试应用", "卸载测试应用", "打开测试应用", "跳转至拨号界面",
                "拨打电话", "发送短信");
        btn1.setOnClickListener(v -> {
            ExtraUtil.alert(this, getAppInfo());
        });
        btn2.setOnClickListener(v -> {
            startActivity(TestAppListActivity.class);
        });
        btn3.setOnClickListener(v -> {
            AppUtil.openLocalAppSettings(this);
        });
        String testApkPackageName = "com.android.testinstall";
        String testApkPath = Environment.getExternalStorageDirectory() + "/AATest/test_install.apk";
        btn4.setOnClickListener(v -> {
            if (!PermissionUtil.requestPermissions(this, 1,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                showToast("请先允许权限");
                return;
            }
            if (AppUtil.isAppInstall(this, testApkPackageName)) {
                showToast("应用已安装");
            } else {
                if (FileUtil.isFileExists(testApkPath)) {
                    AppUtil.installApp(this, testApkPath, getApplicationInfo().packageName + ".provider");
                } else {
                    try {
                        if (IOUtil.writeFileFromInputStream(testApkPath, getAssets().open("test_install.apk"), false)) { //写入成功
                            AppUtil.installApp(this, testApkPath, getApplicationInfo().packageName + ".provider");
                        } else {
                            showToast("文件写入失败");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        showToast("文件写入异常，" + e.getMessage());
                    }
                }
            }
        });
        btn5.setOnClickListener(v -> {
            if (AppUtil.isAppInstall(this, testApkPackageName)) {
                AppUtil.uninstallApp(this, testApkPackageName);
            } else {
                showToast("应用未安装");
            }
        });
        btn6.setOnClickListener(v -> {
            if (AppUtil.isAppInstall(this, testApkPackageName)) {
                AppUtil.openApp(this, testApkPackageName);
            } else {
                showToast("应用未安装");
            }
        });
        btn7.setOnClickListener(v -> {
            startActivity(IntentUtil.getDialIntent("13302480305"));
        });
        btn8.setOnClickListener(v -> {
            if (!PermissionUtil.requestPermissions(this, 1,
                    Manifest.permission.CALL_PHONE)) {
                showToast("请先允许权限");
                return;
            }
            startActivity(IntentUtil.getCallIntent("13302480305"));
        });
        btn9.setOnClickListener(v -> {
            startActivity(IntentUtil.getSendSmsIntent("13302480305", "短信内容"));
        });
    }

    private SpannableStringBuilder getAppInfo() {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append("应用图标： ");
        Drawable icon = AppUtil.getLocalAppIcon(this);
        if (icon != null) {
            icon.setBounds(0, 0, icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
            ImageSpan span = new ImageSpan(icon);
            builder.setSpan(span, 5, 6, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        builder.append("\n应用名称：")
                .append(AppUtil.getLocalAppLabel(this))
                .append("\n安装路径：\n")
                .append(AppUtil.getLocalAppPath(this))
                .append("\nversionName：")
                .append(AppUtil.getLocalAppVersionName(this))
                .append("\nversionCode：")
                .append(String.valueOf(AppUtil.getLocalAppVersionCode(this)))
                .append("\n是否系统应用：")
                .append(String.valueOf(AppUtil.isLocalSystemApp(this)))
                .append("\n是否是Debug版本：")
                .append(String.valueOf(AppUtil.isLocalAppDebug(this)))
                .append("\n是否有root权限：")
                .append(String.valueOf(AppUtil.isLocalAppRoot()))
                .append("\nMD5值：\n")
                .append(AppUtil.getLocalAppSignatureMD5(this))
                .append("\nSHA1值：\n")
                .append(AppUtil.getLocalAppSignatureSHA1(this))
                .append("\n是否处于前台：")
                .append(String.valueOf(AppUtil.isLocalAppForeground(this)));
        LogUtil.e("AppInfo", " \n" + AppUtil.getLocalAppInfo(this));
        return builder;
    }

    //设备工具
    private void testDevice() {
        CommonLayoutUtil.initCommonLayout(this, "设备工具", false, true);
        StringBuilder sb = new StringBuilder();
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
                .append("\nIMEI：").append(DeviceUtil.getIMEI(this));
        tv.setText(sb.toString());
    }

    //Shell工具
    private void testShell() {
        String command1 = "getprop ro.product.model";
        String command2 = "cd sdcard/AATest\ncat test.txt";
        CommonLayoutUtil.initCommonLayout(this, "Shell工具", true, true,
                "运行命令", command1, command2);
        il.setInputTextHint("请输入要执行的Shell命令，多条的话换行输入");
        btn1.setOnClickListener(v -> {
            String content = il.getInputText().trim();
            if (TextUtils.isEmpty(content)) {
                showToast("请先输入命令");
            } else {
                String[] commands = content.split("\n");
                String result = ShellUtil.execCmd(commands, false).toString();
                LogUtil.w("Shell", result);
                tv.setText(result);
            }
        });
        btn2.setOnClickListener(v -> {
            tv.setText(ShellUtil.execShellCmd(command1).toString());
        });
        btn3.setOnClickListener(v -> {
            String testFilePath = Environment.getExternalStorageDirectory() + "/AATest/test.txt";
            if (!PermissionUtil.requestPermissions(this, 1,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                showToast("请先允许权限");
                return;
            }
            FileUtil.createOrExistsFile(testFilePath);
            IOUtil.writeFileFromString(testFilePath, "test shell " + DateUtil.getCurrentDateTime(), false);
            String result = ShellUtil.execCmd(command2.split("\n"), false).toString();
            LogUtil.w("Shell", result);
            tv.setText(result);
        });
    }

    //选择器工具类
    private void testPickerView() {
        CommonLayoutUtil.initCommonLayout(this, "底部选择器工具", false, true,
                "选择年月日", "选择年月日时分秒");
        btn1.setOnClickListener(v -> {
            String curentTime = DateUtil.convertOtherFormat(tv.getText().toString(), DateUtil.Y_M_D_H_M_S, DateUtil.Y_M_D);
            PickerViewUtil.selectDate(this, (date, v1) -> {
                tv.setText(DateUtil.date2String(date, DateUtil.Y_M_D));
            }, curentTime);
        });
        btn2.setOnClickListener(v -> {
            String startTime = "1990-01-01 00:00:00";
            String endTime = "2100-01-01 12:00:00";
            String formatStr = DateUtil.Y_M_D_H_M_S;
            boolean[] type = new boolean[]{true, true, true, true, true, true};
            String curentTime = DateUtil.convertOtherFormat(tv.getText().toString(), DateUtil.Y_M_D, DateUtil.Y_M_D_H_M_S);
            PickerViewUtil.selectDateTime(
                    this, (date, v1) -> {
                        tv.setText(DateUtil.date2String(date, formatStr));
                    }, curentTime, startTime, endTime, formatStr,
                    "选择日期时间", "取消", "确定", type
            );
        });
    }

    //崩溃异常监听
    private void testCrash() {
        CommonLayoutUtil.initCommonLayout(this, "崩溃异常监听",
                "抛异常", "读取crash文件", "删除crash文件", "移动文件到sd卡");
        String dirName = getCacheDir() + "/log";
        String fileName = dirName + "/crash.trace";
        btn1.setOnClickListener(v -> {
            ((TextView) findViewById(R.id.toast_tv)).setText("");  //空指针异常
//            throw new RuntimeException("自定义异常");
        });
        btn2.setOnClickListener(v -> {
            if (FileUtil.isFileExists(fileName)) {
                String content = IOUtil.readFileToString(fileName);
                ExtraUtil.alert(this, content);
            } else {
                showToast("crash文件不存在");
            }
        });
        btn3.setOnClickListener(v -> {
            if (FileUtil.isFileExists(fileName)) {
                showToast(FileUtil.deleteDirectory(dirName) ? "删除成功" : "删除失败");
            } else {
                showToast("crash文件不存在");
            }
        });
        btn4.setOnClickListener(v -> {
            if (!PermissionUtil.requestPermissions(this, 1,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                showToast("请先允许权限");
                return;
            }
            String moveFileName = "sdcard/AATest/crash.trace";
            if (FileUtil.deleteFile(moveFileName) && FileUtil.isFileExists(fileName)) {
                showToast(FileUtil.moveFile(fileName, moveFileName) ? "文件已移动到" + moveFileName : "移动失败");
            } else {
                showToast("crash文件不存在");
            }
        });
    }

    //应用文件清除工具
    private void testClean() {
        CommonLayoutUtil.initCommonLayout(this, "应用文件清除工具",
                true, true,
                "创建测试数据", "清除内部缓存(cache)", "清除内部数据库(databases)",
                "清除内部文件(files)", "清除内部SharePreference", "清除外部缓存(cache)");
        il.setInputTextHint("请输入要删除文件，如a.txt，不输入默认全部删除");
        btn1.setOnClickListener(v -> {
            showToast("正在创建");
            new Thread(() -> {
                FileUtil.createOrExistsFile(getCacheDir() + "/cache1.txt");
                FileUtil.createOrExistsFile(getCacheDir() + "/cache2.txt");
                FileUtil.createOrExistsFile(getCacheDir() + "/cache3.txt");
                FileUtil.createOrExistsFile(getCacheDir() + "/cache4.txt");
                new DBHelper(this, "test1.db").getReadableDatabase();
                new DBHelper(this, "test2.db").getReadableDatabase();
                new DBHelper(this, "test3.db").getReadableDatabase();
                new DBHelper(this, "test4.db").getReadableDatabase();
                FileUtil.createOrExistsFile(getFilesDir() + "/files1.trace");
                FileUtil.createOrExistsFile(getFilesDir() + "/files2.trace");
                FileUtil.createOrExistsFile(getFilesDir() + "/files3.trace");
                FileUtil.createOrExistsFile(getFilesDir() + "/files4.trace");
                getSharedPreferences("share1", Context.MODE_PRIVATE).edit().putString("key", "value").apply();
                getSharedPreferences("share2", Context.MODE_PRIVATE).edit().putString("key", "value").apply();
                getSharedPreferences("share3", Context.MODE_PRIVATE).edit().putString("key", "value").apply();
                getSharedPreferences("share4", Context.MODE_PRIVATE).edit().putString("key", "value").apply();
                FileUtil.createOrExistsFile(getExternalCacheDir() + "/ecache1.txt");
                FileUtil.createOrExistsFile(getExternalCacheDir() + "/ecache2.txt");
                FileUtil.createOrExistsFile(getExternalCacheDir() + "/ecache3.txt");
                FileUtil.createOrExistsFile(getExternalCacheDir() + "/ecache4.txt");
                listFilesInDir();
            }).start();
        });
        btn2.setOnClickListener(v -> {
            String name = il.getInputText().trim();
            if (TextUtils.isEmpty(name)) {  //删除全部文件
                showToast(CleanUtil.cleanInternalCache(this) ? "删除成功" : "删除失败");
            } else {  //删除指定文件
                showToast(CleanUtil.cleanInternalCacheByName(this, name) ? "删除成功" : "删除失败");
            }
            listFilesInDir();
        });
        btn3.setOnClickListener(v -> {
            String name = il.getInputText().trim();
            if (TextUtils.isEmpty(name)) {  //删除全部文件
                showToast(CleanUtil.cleanInternalDatabase(this) ? "删除成功" : "删除失败");
            } else {  //删除指定文件
                showToast(CleanUtil.cleanInternalDatabaseByName(this, name) ? "删除成功" : "删除失败");
            }
            listFilesInDir();
        });
        btn4.setOnClickListener(v -> {
            String name = il.getInputText().trim();
            if (TextUtils.isEmpty(name)) {  //删除全部文件
                showToast(CleanUtil.cleanInternalFiles(this) ? "删除成功" : "删除失败");
            } else {  //删除指定文件
                showToast(CleanUtil.cleanInternalFilesByName(this, name) ? "删除成功" : "删除失败");
            }
            listFilesInDir();
        });
        btn5.setOnClickListener(v -> {
            String name = il.getInputText().trim();
            if (TextUtils.isEmpty(name)) {  //删除全部文件
                showToast(CleanUtil.cleanInternalSharePreference(this) ? "删除成功" : "删除失败");
            } else {  //删除指定文件
                showToast(CleanUtil.cleanInternalSharePreferenceByName(this, name) ? "删除成功" : "删除失败");
            }
            listFilesInDir();
        });
        btn6.setOnClickListener(v -> {
            String name = il.getInputText().trim();
            if (TextUtils.isEmpty(name)) {  //删除全部文件
                showToast(CleanUtil.cleanExternalCache(this) ? "删除成功" : "删除失败");
            } else {  //删除指定文件
                showToast(CleanUtil.cleanExternalCacheByName(this, name) ? "删除成功" : "删除失败");
            }
            listFilesInDir();
        });
    }

    //创建数据库
    public class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String datebaseName) {
            super(context, datebaseName, null, 1);
        }


        /**
         * 创建数据库表：person
         * _id为主键，自增
         **/
        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            Log.i(TAG, "创建test数据库表！");
            sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS test(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " name VARCHAR,info TEXT)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        }

        @Override
        public void onOpen(SQLiteDatabase sqLiteDatabase) {
            super.onOpen(sqLiteDatabase);
        }
    }

    private void listFilesInDir() {
        runOnUiThread(() -> {
            StringBuilder sb = new StringBuilder();
            List<File> internalList = FileUtil.listFilesInDirectory(getCacheDir().getParent(), true);
            for (File file : internalList) {
                sb.append(file.getAbsolutePath()).append("\n").append("------------------------------------").append("\n");
            }
            List<File> externalList = FileUtil.listFilesInDirectory(getExternalCacheDir().getAbsolutePath(), true);
            for (File file : externalList) {
                sb.append(file.getAbsolutePath()).append("\n").append("------------------------------------").append("\n");
            }
            ExtraUtil.alert(this, sb.toString());
        });
    }

    //SD卡工具
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void testSdcard() {
        CommonLayoutUtil.initCommonLayout(this, "SD卡工具", false, true);
        StringBuilder sb = new StringBuilder();
        sb.append("SD卡是否可用：").append(SDCardUtil.isSDCardEnable())
                .append("\nSD卡路径：").append(SDCardUtil.getSDCardPath())
                .append("\n文件路径：").append(SDCardUtil.getDocumentPath())
                .append("\n下载路径：").append(SDCardUtil.getDownloadPath())
                .append("\n影视路径：").append(SDCardUtil.getMoviePath())
                .append("\n音乐路径：").append(SDCardUtil.getMusicPath())
                .append("\n图片路径：").append(SDCardUtil.getPicturePath())
                .append("\n\n总存储空间：").append(ByteUnit.convertByteUnit(SDCardUtil.getTotalSize(), ByteUnit.GB)).append(" GB")
                .append("\n剩余空间：").append(ByteUnit.convertByteUnit(SDCardUtil.getAvailableSize(), ByteUnit.GB)).append(" GB")
                .append("\n已用空间：").append(ByteUnit.convertByteUnit(SDCardUtil.getUsedSize(), ByteUnit.GB)).append(" GB")
                .append("\n\nSD卡信息：\n").append(JsonUtil.formatJson(new Gson().toJson(SDCardUtil.getSDCardInfo())));
        tv.setText(sb.toString());
    }

    //屏幕工具
    private void testScreen() {
        CommonLayoutUtil.initCommonLayout(this, "屏幕工具", false, true,
                "截屏(包含状态栏)", "截屏(不包含状态栏)", "设置屏幕横屏", "设置屏幕竖屏",
                "设置屏幕跟随系统状态", "获取屏幕信息");
        btn1.setOnClickListener(v -> {
            Bitmap bitmap = ScreenUtil.captureWithStatusBar(this);
            saveBitmapToGallery(bitmap, "截屏带状态栏");
        });
        btn2.setOnClickListener(v -> {
            Bitmap bitmap = ScreenUtil.captureWithoutStatusBar(this);
            saveBitmapToGallery(bitmap, "截屏不带状态栏");
        });
        btn3.setOnClickListener(v -> {
            ScreenUtil.setLandscape(this);
        });
        btn4.setOnClickListener(v -> {
            ScreenUtil.setPortrait(this);
        });
        btn5.setOnClickListener(v -> {
            ScreenUtil.setSensor(this);
        });
        btn6.setOnClickListener(v -> {
            StringBuilder sb = new StringBuilder();
            DisplayMetrics dm = ScreenUtil.getDisplayMetrics(this);
            sb.append("屏幕宽度：").append(ScreenUtil.getScreenWidth(this))
                    .append("\n屏幕高度：").append(ScreenUtil.getScreenHeight(this))
                    .append("\ndensity：").append(dm.density)
                    .append("\nscaledDensity：").append(dm.scaledDensity)
                    .append("\ndensityDpi：").append(dm.densityDpi)
                    .append("\n是否是横屏：").append(ScreenUtil.isLandscape(this))
                    .append("\n是否是竖屏：").append(ScreenUtil.isPortrait(this))
                    .append("\n是否锁屏：").append(ScreenUtil.isScreenLock(this))
                    .append("\n旋转角度：").append(ScreenUtil.getScreenRotation(this));
            ExtraUtil.alert(this, sb.toString());
        });
    }

    //磁盘缓存工具
    private void testCache() {
        CommonLayoutUtil.initCommonLayout(this, "磁盘缓存工具", true, true,
                "读取", "保存字符串", "保存Serializable数据", "保存Bitmap", "保存Drawable",
                "移除指定key", "清除所有缓存内容");
        il.setInputTextHint("请输入Key名");
        InputLayout valueIl = CommonLayoutUtil.createInputLayout(this, "请输入要保存的字符串");
        InputLayout durationIl = CommonLayoutUtil.createInputLayout(this, "请输入保存的时长");
        durationIl.setInputTextType(InputType.TYPE_CLASS_NUMBER);
        ll.addView(valueIl, 1);
        ll.addView(durationIl, 2);
        String tip = "默认Key名：\n字符串：String\t\t\t\t\tSerializable：Serializable\nBitmap：Bitmap\t\t\tDrawable：Drawable\n";
        tv.setText(tip);
        CacheUtil cacheUtil = CacheUtil.get(this);
        btn1.setOnClickListener(v -> {
            tv.setText(tip);
            switchCacheState(!isSaveCache, valueIl, durationIl);
            isSaveCache = !isSaveCache;
        });
        btn2.setOnClickListener(v -> {
            tv.setText(tip);
            String key = getKey(il, "String");
            if (isSaveCache) {
                String value = valueIl.getInputText().trim();
                if (TextUtils.isEmpty(value)) {
                    showToast("请输入保存的字符串");
                    return;
                }
                int saveTime = getSaveTime(durationIl);
                if (saveTime == 0) {
                    showToast("请输入大于0秒的时长");
                } else if (saveTime == -1) {
                    cacheUtil.putString(key, value);
                    showToast("保存成功");
                } else {
                    cacheUtil.putString(key, value, saveTime);
                    showToast("保存成功，数据将缓存" + saveTime + "秒");
                }
            } else {
                String result = cacheUtil.getString(key);
                tv.setText(TextUtils.isEmpty(result) ? "数据为空" : result);
            }
        });
        btn3.setOnClickListener(v -> {
            tv.setText(tip);
            String key = getKey(il, "Serializable");
            MonthBean bean = new MonthBean("2020-02", key);
            if (isSaveCache) {
                int saveTime = getSaveTime(durationIl);
                if (saveTime == 0) {
                    showToast("请输入大于0秒的时长");
                } else if (saveTime == -1) {
                    cacheUtil.putObject(key, bean);
                    showToast("保存成功");
                } else {
                    cacheUtil.putObject(key, bean, saveTime);
                    showToast("保存成功，数据将缓存" + saveTime + "秒");
                }
            } else {
                MonthBean result = (MonthBean) cacheUtil.getObject(key);
                tv.setText(result == null ? "数据为空" : new Gson().toJson(result));
            }
        });
        btn4.setOnClickListener(v -> {
            tv.setText(tip);
            String key = getKey(il, "Bitmap");
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_coupon_header);
            if (isSaveCache) {
                int saveTime = getSaveTime(durationIl);
                if (saveTime == 0) {
                    showToast("请输入大于0秒的时长");
                } else if (saveTime == -1) {
                    cacheUtil.putBitmap(key, bitmap);
                    showToast("保存成功");
                } else {
                    cacheUtil.putBitmap(key, bitmap, saveTime);
                    showToast("保存成功，数据将缓存" + saveTime + "秒");
                }
            } else {
                Bitmap result = cacheUtil.getBitmap(key);
                if (result != null) {
                    ExtraUtil.showImage(this, result);
                } else {
                    showToast("数据为空");
                }
            }
        });
        btn5.setOnClickListener(v -> {
            tv.setText(tip);
            String key = getKey(il, "Drawable");
            Drawable drawable = getResources().getDrawable(R.drawable.shape_oval_size_34_solid_db4b3c);
            if (isSaveCache) {
                int saveTime = getSaveTime(durationIl);
                if (saveTime == 0) {
                    showToast("请输入大于0秒的时长");
                } else if (saveTime == -1) {
                    cacheUtil.putDrawable(key, drawable);
                    showToast("保存成功");
                } else {
                    cacheUtil.putDrawable(key, drawable, saveTime);
                    showToast("保存成功，数据将缓存" + saveTime + "秒");
                }
            } else {
                Drawable result = cacheUtil.getDrawable(key);
                if (result != null) {
                    ExtraUtil.showImage(this, result);
                } else {
                    showToast("数据为空");
                }
            }
        });
        btn6.setOnClickListener(v -> {
            String key = il.getInputText().trim();
            if (TextUtils.isEmpty(key)) {
                showToast("请先输入要删除的Key名");
                return;
            }
            cacheUtil.remove(key);
            showToast("已删除");
        });
        btn7.setOnClickListener(v -> {
            cacheUtil.delete();
            showToast("已清除");
        });
    }

    private boolean isSaveCache = true;

    private void switchCacheState(boolean save, InputLayout... inputLayouts) {
        for (InputLayout il : inputLayouts) {
            il.setVisibility(save ? View.VISIBLE : View.GONE);
            il.setInputText("");
        }
        il.setInputText("");
        btn1.setText(save ? "读取" : "保存");
        btn2.setText(save ? "保存字符串" : "读取字符串");
        btn3.setText(save ? "保存Serializable数据" : "读取Serializable数据");
        btn4.setText(save ? "保存Bitmap" : "读取Bitmap");
        btn5.setText(save ? "保存Drawable" : "读取Drawable");
    }

    //SharedPreferences工具
    private void testSP() {
        CommonLayoutUtil.initCommonLayout(this, "SharedPreferences工具", true, true,
                "保存字符串", "读取字符串", "保存Int、Long、Boolean、Float类型数据",
                "读取Int、Long、Boolean、Float类型数据", "遍历所有的Key", "移除指定key", "清除所有数据");
        il.setInputTextHint("请输入Key名");
        InputLayout valueIl = CommonLayoutUtil.createInputLayout(this, "请输入要保存的字符串");
        InputLayout durationIl = CommonLayoutUtil.createInputLayout(this, "请输入保存的时长");
        durationIl.setInputTextType(InputType.TYPE_CLASS_NUMBER);
        ll.addView(valueIl, 1);
        ll.addView(durationIl, 2);
        String tip = "默认Key名：String\n";
        tv.setText(tip);
        btn1.setOnClickListener(v -> {
            tv.setText(tip);
            String key = getKey(il, "String");
            String value = valueIl.getInputText().trim();
            if (TextUtils.isEmpty(value)) {
                showToast("请输入保存的字符串");
                return;
            }
            int saveTime = getSaveTime(durationIl);
            if (saveTime == 0) {
                showToast("请输入大于0秒的时长");
            } else if (saveTime == -1) {
                SPUtil.putString(key, value);
                showToast("保存成功");
            } else {
                SPUtil.putString(key, value, saveTime);
                showToast("保存成功，数据将缓存" + saveTime + "秒");
            }
        });
        btn2.setOnClickListener(v -> {
            String key = getKey(il, "String");
            String result = SPUtil.getString(key, "");
            tv.setText(TextUtils.isEmpty(result) ? "数据为空" : result);
        });
        btn3.setOnClickListener(v -> {
            SPUtil.putInt("Int", new Random().nextInt());
            SPUtil.putLong("Long", new Random().nextLong());
            SPUtil.putBoolean("Boolean", new Random().nextBoolean());
            SPUtil.putFloat("Float", new Random().nextFloat());
            showToast("保存成功");
        });
        btn4.setOnClickListener(v -> {
            StringBuilder sb = new StringBuilder();
            sb.append("Key：值(缺省值)\n")
                    .append("Int：").append(SPUtil.getInt("Int", -1)).append("(-1)\n")
                    .append("Long：").append(SPUtil.getLong("Long", -1)).append("(-1)\n")
                    .append("Boolean：").append(SPUtil.getBoolean("Boolean", false)).append("(false)\n")
                    .append("Float：").append(SPUtil.getFloat("Float", -1.1f)).append("(-1.1)");
            tv.setText(sb.toString());
        });
        btn5.setOnClickListener(v -> {
            Map<String, ?> map = SPUtil.getAll();
            StringBuilder sb = new StringBuilder();
            for (String key : map.keySet()) {
                sb.append(key).append("\t\t");
            }
            if (TextUtils.isEmpty(sb.toString())) {
                tv.setText("当前没有包含任何数据");
            } else {
                tv.setText("所有Key名如下：\n");
                tv.append(sb.toString());
            }
        });
        btn6.setOnClickListener(v -> {
            String key = il.getInputText().trim();
            if (TextUtils.isEmpty(key)) {
                showToast("请先输入要删除的Key名");
                return;
            }
            if (SPUtil.contains(key)) {
                SPUtil.remove(key);
                showToast("已删除");
            } else {
                showToast("未找到该Key对应的数据");
            }
        });
        btn7.setOnClickListener(v -> {
            SPUtil.clear();
            showToast("已清除");
        });
    }

    private String getKey(InputLayout il, String defaultKey) {
        String key = il.getInputText().trim();
        return TextUtils.isEmpty(key) ? defaultKey : key;
    }

    private int getSaveTime(InputLayout il) {
        String saveTime = il.getInputText().trim();
        return TextUtils.isEmpty(saveTime) ? -1 : Integer.parseInt(saveTime);
    }

    //布局参数工具
    private void testLayoutParams() {
        CommonLayoutUtil.initCommonLayout(this, "布局参数工具", false, false,
                "设置上下左右Margin为10dp", "设置上下左右Padding为10dp",
                "增加上下左右Margin为5dp", "减少上下左右Margin为5dp",
                "增加上下左右Padding为5dp", "减少上下左右Padding为5dp", "还原");
        LayoutParamsUtil.setMarginTop(btn1, (int) SizeUtil.dp2px(10));
        LinearLayout rootLL = new LinearLayout(this);
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
                (int) SizeUtil.dp2px(300), (int) SizeUtil.dp2px(200));
        params1.gravity = Gravity.CENTER_HORIZONTAL;
        rootLL.setLayoutParams(params1);
        rootLL.setBackgroundColor(Color.BLACK);
        LinearLayout targetLl = new LinearLayout(this);
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        targetLl.setLayoutParams(params2);
        targetLl.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        int dp20 = (int) SizeUtil.dp2px(20);
        int dp40 = (int) SizeUtil.dp2px(40);
        LayoutParamsUtil.setMargin(targetLl, dp20, dp20, dp20, dp20);
        LayoutParamsUtil.setPadding(targetLl, dp40, dp40, dp40, dp40);
        View view = new View(this);
        LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(params3);
        view.setBackgroundColor(Color.WHITE);
        targetLl.addView(view);
        rootLL.addView(targetLl);
        ll.addView(rootLL, 0);
        btn1.setOnClickListener(v -> {
            int margin = (int) SizeUtil.dp2px(10);
            LayoutParamsUtil.setMarginLeft(targetLl, margin);
            LayoutParamsUtil.setMarginRight(targetLl, margin);
            LayoutParamsUtil.setMarginTop(targetLl, margin);
            LayoutParamsUtil.setMarginBottom(targetLl, margin);
        });
        btn2.setOnClickListener(v -> {
            int padding = (int) SizeUtil.dp2px(10);
            LayoutParamsUtil.setPaddingLeft(targetLl, padding);
            LayoutParamsUtil.setPaddingRight(targetLl, padding);
            LayoutParamsUtil.setPaddingTop(targetLl, padding);
            LayoutParamsUtil.setPaddingBottom(targetLl, padding);
        });
        btn3.setOnClickListener(v -> {
            int margin = (int) SizeUtil.dp2px(5);
            LayoutParamsUtil.addMarginLeft(targetLl, margin);
            LayoutParamsUtil.addMarginRight(targetLl, margin);
            LayoutParamsUtil.addMarginTop(targetLl, margin);
            LayoutParamsUtil.addMarginBottom(targetLl, margin);
        });
        btn4.setOnClickListener(v -> {
            int margin = -(int) SizeUtil.dp2px(5);
            LayoutParamsUtil.addMarginLeft(targetLl, margin);
            LayoutParamsUtil.addMarginRight(targetLl, margin);
            LayoutParamsUtil.addMarginTop(targetLl, margin);
            LayoutParamsUtil.addMarginBottom(targetLl, margin);
        });
        btn5.setOnClickListener(v -> {
            int padding = (int) SizeUtil.dp2px(5);
            LayoutParamsUtil.addPaddingLeft(targetLl, padding);
            LayoutParamsUtil.addPaddingRight(targetLl, padding);
            LayoutParamsUtil.addPaddingTop(targetLl, padding);
            LayoutParamsUtil.addPaddingBottom(targetLl, padding);
        });
        btn6.setOnClickListener(v -> {
            int padding = -(int) SizeUtil.dp2px(5);
            LayoutParamsUtil.addPaddingLeft(targetLl, padding);
            LayoutParamsUtil.addPaddingRight(targetLl, padding);
            LayoutParamsUtil.addPaddingTop(targetLl, padding);
            LayoutParamsUtil.addPaddingBottom(targetLl, padding);
        });
        btn7.setOnClickListener(v -> {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            targetLl.setLayoutParams(params);
            LayoutParamsUtil.setMargin(targetLl, dp20, dp20, dp20, dp20);
            LayoutParamsUtil.setPadding(targetLl, dp40, dp40, dp40, dp40);
        });
    }

    //手机工具类
    private void testPhone() {
        CommonLayoutUtil.initCommonLayout(this, "手机工具", true, false,
                "获取手机信息", "判断手机号是否合法", "跳转至拨号界面", "拨打电话", "跳转至发送短信界面",
                "发送短信", "获取手机联系人信息", "获取手机短信内容");
        String phoneNumber = "13302480305";
        btn1.setOnClickListener(v -> {
            if (!PermissionUtil.requestPermissions(this, 1, Manifest.permission.READ_PHONE_STATE)) {
                showToast("请先允许权限");
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("当前设备是否是手机：").append(PhoneUtil.isPhone(this))
                    .append("\nIMEI：").append(PhoneUtil.getIMEI(this))
                    .append("\nIMSI：").append(PhoneUtil.getIMSI(this))
                    .append("\n终端类型PhoneType：").append(PhoneUtil.getPhoneType(this))
                    .append("\nsim卡是否准备好：").append(PhoneUtil.isSimReady(this))
                    .append("\nSim卡运营商名称：").append(PhoneUtil.getSimOperatorName(this))
                    .append("\t\t").append(PhoneUtil.getSimOperatorCH(this))
                    .append("\n\n手机信息：\n").append(JsonUtil.formatJson(new Gson().toJson(PhoneUtil.getPhoneInfo(this))));
            ExtraUtil.alert(this, sb.toString());
        });
        btn2.setOnClickListener(v -> {
            String phone = il.getInputText().trim();
            if (TextUtils.isEmpty(phone)) {
                showToast("请先输入手机号");
                return;
            }
            showToast(PhoneUtil.isPhoneNumberValid(phone) ? "手机号合法" : "非手机号");
        });
        btn3.setOnClickListener(v -> {
            PhoneUtil.dial(this, phoneNumber);
        });
        btn4.setOnClickListener(v -> {
            if (!PermissionUtil.requestPermissions(this, 1, Manifest.permission.CALL_PHONE)) {
                showToast("请先允许权限");
                return;
            }
            String phone = il.getInputText().trim();
            if (TextUtils.isEmpty(phone)) {
                showToast("请先输入手机号");
                return;
            }
            if (PhoneUtil.isPhoneNumberValid(phone)) {
                PhoneUtil.call(this, phone);
            } else {
                showToast("非手机号");
            }
        });
        btn5.setOnClickListener(v -> {
            String content = il.getInputText().trim();
            if (TextUtils.isEmpty(content)) {
                showToast("请先输入要发送的短信内容");
                return;
            }
            PhoneUtil.sendSms(this, phoneNumber, content);
        });
        btn6.setOnClickListener(v -> {
            if (!PermissionUtil.requestPermissions(this, 1, Manifest.permission.SEND_SMS)) {
                showToast("请先允许权限");
                return;
            }
            String content = il.getInputText().trim();
            if (TextUtils.isEmpty(content)) {
                showToast("请先输入要发送的短信内容");
                return;
            }
            PhoneUtil.sendSmsSilent(this, phoneNumber, content);
        });
        btn7.setOnClickListener(v -> {
            if (!PermissionUtil.requestPermissions(this, 1,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.READ_CONTACTS)) {
                showToast("请先允许权限");
                return;
            }
            ThreadPoolManager.getInstance().getFixedThreadPool().submit(() -> {
                String info = new Gson().toJson(PhoneUtil.getContactInfo(this, 10));
                runOnUiThread(() -> ExtraUtil.alert(this, JsonUtil.formatJson(info)));
                LogUtil.logLongTag("获取所有手机号码", "==============================================================\n" +
                        JsonUtil.formatJson(new Gson().toJson(PhoneUtil.getAllContactInfo(this))));
            });
        });
        btn8.setOnClickListener(v -> {
            if (!PermissionUtil.requestPermissions(this, 1,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.READ_SMS)) {
                showToast("请先允许权限");
                return;
            }
            ThreadPoolManager.getInstance().getFixedThreadPool().submit(() -> {
                String info = new Gson().toJson(PhoneUtil.getSMSInfo(this, 10));
                runOnUiThread(() -> ExtraUtil.alert(this, JsonUtil.formatJson(info)));
            });
        });
    }

    //正则表达式工具
    private void testRegex() {
        CommonLayoutUtil.initCommonLayout(this, "正则表达式工具", true, true,
                "匹配数字", "匹配整数", "匹配浮点数", "匹配汉字", "匹配英文字母或数字", "匹配英文字母和数字",
                "匹配手机号", "匹配身份证号", "匹配\"yyyy-MM-dd\"的日期格式", "匹配输入的正则表达式",
                "提取文本中数字", "根据给定的正则表达式提取内容");
        InputLayout regexIl = CommonLayoutUtil.createInputLayout(this, "请输入正则表达式");
        ll.addView(regexIl, 1);
        il.setInputTextHint("请输入要匹配的内容");
        btn1.setOnClickListener(v -> {
            setRegexResult(RegexUtil.isDigit(il.getInputText()));
        });
        btn2.setOnClickListener(v -> {
            setRegexResult(RegexUtil.isInteger(il.getInputText()));
        });
        btn3.setOnClickListener(v -> {
            setRegexResult(RegexUtil.isFloat(il.getInputText()));
        });
        btn4.setOnClickListener(v -> {
            setRegexResult(RegexUtil.isChinese(il.getInputText()));
        });
        btn5.setOnClickListener(v -> {
            setRegexResult(RegexUtil.isLetterOrDigit(il.getInputText()));
        });
        btn6.setOnClickListener(v -> {
            setRegexResult(RegexUtil.isLetterAndDigit(il.getInputText()));
        });
        btn7.setOnClickListener(v -> {
            setRegexResult(RegexUtil.isMobile(il.getInputText()));
        });
        btn8.setOnClickListener(v -> {
            setRegexResult(RegexUtil.isIdCard(il.getInputText()));
        });
        btn9.setOnClickListener(v -> {
            setRegexResult(RegexUtil.isyyyyMMdd(il.getInputText()));
        });
        btn10.setOnClickListener(v -> {
            String regex = regexIl.getInputText().trim();
            if (TextUtils.isEmpty(regex)) {
                showToast("请先输入正则表达式");
                return;
            }
            setRegexResult(RegexUtil.isMatch(regex, il.getInputText()));
        });
        btn11.setOnClickListener(v -> {
            String content = il.getInputText().trim();
            if (TextUtils.isEmpty(content)) {
                showToast("请先输入要匹配的内容");
                return;
            }
            String result = RegexUtil.extractDigit(content, "|");
            tv.setText(result);
        });
        btn12.setOnClickListener(v -> {
            String content = il.getInputText().trim();
            if (TextUtils.isEmpty(content)) {
                showToast("请先输入要匹配的内容");
                return;
            }
            String regex = regexIl.getInputText().trim();
            if (TextUtils.isEmpty(regex)) {
                showToast("请先输入正则表达式");
                return;
            }
            String result = RegexUtil.extract(regex, content, "|");
            tv.setText(result);
        });
    }

    private void setRegexResult(boolean isCorrect) {
        String result = "\"" + il.getInputText() + "\"：" + isCorrect;
        tv.setText(result);
    }

    //编码解码工具
    private void testEncode() {
        CommonLayoutUtil.initCommonLayout(this, "编码解码工具", false, true);
        StringBuilder sb = new StringBuilder();
        String url = "https://www.google.com/imghp?hl=zh-CN&tab=wi&ogbl";
        String urlEncode = EncodeUtil.urlEncode(url);
        String urlDecode = EncodeUtil.urlDecode(urlEncode);
        LogUtil.w(TAG, "URL编码：" + urlEncode);
        LogUtil.w(TAG, "URL解码：" + urlDecode);
        sb.append(url).append("\nURL编码：\n").append(urlEncode)
                .append("\nURL解码：\n").append(urlDecode);
        String content = "https://www.google.com/imghp?hl=zh-CN&tab=wi&content=测试Base64编码和解码";
        byte[] base64EncodeBytes = EncodeUtil.base64Encode(content);
        byte[] base64DecodeBytes = EncodeUtil.base64Decode(base64EncodeBytes);
        String base64EncodeStr = new String(base64EncodeBytes);
        String base64DecodeStr = new String(base64DecodeBytes);
        LogUtil.w(TAG, "Base64编码：" + base64EncodeStr);
        LogUtil.w(TAG, "Base64解码：" + base64DecodeStr);
        LogUtil.w(TAG, "Base64编码成字符串：" + EncodeUtil.base64EncodeToString(content.getBytes()));
        LogUtil.w(TAG, "Base64 URL安全编码：" + new String(EncodeUtil.base64UrlSafeEncode(content)));
        sb.append("\n\n").append(content).append("\nBase64编码：\n").append(base64EncodeStr)
                .append("\nBase64解码：\n").append(base64DecodeStr);
        String htmlContent = "<!Doctype html>\n" +
                "<html lang=\"zh_cn\">\n" +
                "<head>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n" +
                "<title>测试Html编码和解码</title>\n" +
                "</head>";
        String htmlEncode = EncodeUtil.htmlEncode(htmlContent);
        CharSequence htmlDecode = EncodeUtil.htmlDecode(htmlEncode);
        LogUtil.w(TAG, "Html编码：\n" + htmlEncode);
        LogUtil.w(TAG, "Html解码：\n" + htmlDecode);
        sb.append("\n\n").append(htmlContent).append("\nHtml编码：\n").append(htmlEncode)
                .append("\nHtml解码：\n").append(htmlDecode);
        tv.setText(sb.toString());
    }

    //Service工具
    private void testService() {
        CommonLayoutUtil.initCommonLayout(this, "Service工具", false, true,
                "获取所有运行的服务", "服务是否在运行", "开启服务", "停止服务", "绑定服务", "解绑服务");
        btn1.setOnClickListener(v -> {
            List<ActivityManager.RunningServiceInfo> list = ServiceUtil.getAllRunningServiceInfo(this);
            StringBuilder sb = new StringBuilder();
            for (ActivityManager.RunningServiceInfo info : list) {
                sb.append(info.service.getClassName()).append("\n");
            }
            if (TextUtils.isEmpty(sb.toString())) {
                tv.setText("目前没有正在运行的服务");
            } else {
                tv.setText(sb.toString());
            }
        });
        btn2.setOnClickListener(v -> {
            tv.setText(ServiceUtil.isServiceRunning(this, TestService.class)
                    ? "TestService正在运行" : "TestService未运行");
        });
        btn3.setOnClickListener(v -> {
            ServiceUtil.startService(this, TestService.class);
        });
        btn4.setOnClickListener(v -> {
            ServiceUtil.stopService(this, TestService.class);
        });
        btn5.setOnClickListener(v -> {
            ServiceUtil.bindService(this, TestService.class, mConn);
        });
        btn6.setOnClickListener(v -> {
            if (ServiceUtil.isServiceRunning(this, TestService.class)) {
                ServiceUtil.unbindService(this, mConn);
            } else {
                showToast("服务尚未绑定");
            }
        });
    }

    private ServiceConnection mConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            TestService.LocalBinder binder = (TestService.LocalBinder) service;
            showToast(binder.getService().getBindState());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //onServiceDisconnected在连接正常关闭的情况下是不会被调用的
            //该方法只在Service被破坏了或者被杀死的时候调用，例如系统资源不足时
        }
    };

    //位置工具
    private void testLocation() {
        CommonLayoutUtil.initCommonLayout(this, "位置工具", false, true,
                "开启位置监听服务", "打开设置界面", "申请位置权限");
        bindService(new Intent(this, LocationService.class), mLocationConn, Context.BIND_AUTO_CREATE);
        btn1.setOnClickListener(v -> {
            if (ServiceUtil.isServiceRunning(this, LocationService.class)) {
                showToast("服务正在运行");
            } else {
                bindService(new Intent(this, LocationService.class), mLocationConn, Context.BIND_AUTO_CREATE);
            }
        });
        btn2.setOnClickListener(v -> {
            LocationUtil.openGPSSettings(this);
        });
        btn3.setOnClickListener(v -> {
            if (PermissionUtil.requestPermissions(this, 1,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                showToast("已申请权限！");
            }
        });
    }

    ServiceConnection mLocationConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LocationService locationService = ((LocationService.LocationBinder) service).getService();
            locationService.setOnLocationListener(new LocationService.OnLocationListener() {
                @Override
                public void initState(boolean isSuccess) {
                    if (isSuccess) {
                        showToast("位置监听初始化成功");
                    } else {
                        if (PermissionUtil.isPermissionGranted(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                            if (!LocationUtil.isGPSEnable(getApplicationContext())) {
                                showToast("请打开GPS");
                            } else if (!LocationUtil.isLocationEnable(getApplicationContext())) {
                                showToast("定位不可用");
                            }
                        } else {
                            showToast("请先允许权限");
                        }
                        unbindService(mLocationConn);
                    }
                }

                @Override
                public void getLocation(String lastLatitude, String lastLongitude, String latitude, String longitude, String country, String locality, String street) {
                    runOnUiThread(() -> {
                        StringBuilder sb = new StringBuilder();
                        sb.append("lastLatitude：").append(lastLatitude).append("\nlastLongitude：").append(lastLongitude)
                                .append("\nlatitude：").append(latitude).append("\nlongitude：").append(longitude)
                                .append("\ncountryName：").append(country).append("\nlocality：").append(locality)
                                .append("\nstreet：").append(street);
                        tv.setText(sb.toString());
                    });
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    //网络工具
    private void testNetwork() {
        CommonLayoutUtil.initCommonLayout(this, "网络工具", false, false,
                "获取网络信息", "打开网络设置页面", "打开WiFi", "关闭WiFi");
        btn1.setOnClickListener(v -> {
            StringBuilder sb = new StringBuilder();
            sb.append("网络是否连接：").append(NetworkUtil.isConnected(this))
                    .append("\n网络是否可用：").append(NetworkUtil.isAvailable())
                    .append("\n移动数据是否打开：").append(NetworkUtil.getDataEnabled(this))
                    .append("\n网络是否是4G：").append(NetworkUtil.is4G(this))
                    .append("\nwifi是否打开：").append(NetworkUtil.isWifiEnabled(this))
                    .append("\nwifi是否连接：").append(NetworkUtil.isWifiConnected(this))
                    .append("\nwifi是否可用：").append(NetworkUtil.isWifiAvailable(this))
                    .append("\n当前网络类型：").append(NetworkUtil.getNetworkType(this))
                    .append("\nIP地址：").append(NetworkUtil.getIPAddress(true))
                    .append("\nbaidu.com对应的IP地址：").append(NetworkUtil.getDomainAddress("baidu.com"));
            ExtraUtil.alert(this, sb.toString());
        });
        btn2.setOnClickListener(v -> {
            NetworkUtil.openWirelessSettings(this);
        });
        btn3.setOnClickListener(v -> {
            NetworkUtil.setWifiEnabled(this, true);
        });
        btn4.setOnClickListener(v -> {
            NetworkUtil.setWifiEnabled(this, false);
        });
    }

    //版本升级
    private void testApkDownload() {
        CommonLayoutUtil.initCommonLayout(this, "应用下载",
                "下载QQ邮箱", "版本更新");
        ApkDownloadUtil apkDownloadUtil = new ApkDownloadUtil(this);
        btn1.setOnClickListener(v -> {
            apkDownloadUtil.downLoadApk("http://app.mail.qq.com/cgi-bin/mailapp?latest=y&from=2");
        });
        btn2.setOnClickListener(v -> {
            apkDownloadUtil.downLoadApk("https://ugc-download-2.imfir.cn/92d9763a68537b42d156507779b3a63635cf3909.apk?auth_key=1594538420-0-0-abce665cfe38f8e444a000dd9411587c");
        });
    }

    //SpannableString
    private void testSpannableString() {
        CommonLayoutUtil.initCommonLayout(this, "测试SpannableString", false, true);
        SpannableStringBuilder builder = new SpannableStringUtil.Builder()
                .appendLine("测试SpannableStringUtils").setBackgroundColor(Color.LTGRAY).setBold().setForegroundColor(Color.YELLOW).setAlignment(Layout.Alignment.ALIGN_CENTER)
                .append("测试")
                .append("前景色").setForegroundColor(Color.GREEN)
                .appendLine("背景色").setBackgroundColor(Color.LTGRAY)
                .appendLine("测试首行缩进").setLeadingMargin(30, 50)
                .appendLine("测试引用").setQuoteColor(Color.BLUE, 10, 10)
                .appendLine("测试列表项").setBullet(Color.GREEN, 30, 10)
                .appendLine("测试32dp字体").setFontSize(36, true)
                .append("测试")
                .appendLine("2倍字体").setFontProportion(2)
                .append("测试")
                .appendLine("横向2倍字体").setFontXProportion(2)
                .append("测试")
                .append("删除线").setStrikethrough()
                .appendLine("下划线").setUnderline()
                .append("测试")
                .append("上标").setSuperscript()
                .appendLine("下标").setSubscript()
                .append("测试")
                .append("粗体").setBold()
                .append("斜体").setItalic()
                .appendLine("粗斜体").setBoldItalic()
                .appendLine("monospace font").setFontFamily("monospace")
                .appendLine("测试自定义字体").setTypeface(Typeface.createFromAsset(getAssets(), "fonts/fonts.ttf"))
                .appendLine("测试相反对齐").setAlignment(Layout.Alignment.ALIGN_OPPOSITE)
                .appendLine("测试居中对齐").setAlignment(Layout.Alignment.ALIGN_CENTER)
                .append("测试小图对齐").setBackgroundColor(Color.LTGRAY)
                .append("").setResourceId(R.drawable.ic_check, SpannableStringUtil.ALIGN_TOP)
                .append("").setResourceId(R.drawable.ic_check, SpannableStringUtil.ALIGN_CENTER)
                .append("").setResourceId(R.drawable.ic_check, SpannableStringUtil.ALIGN_BASELINE)
                .append("").setResourceId(R.drawable.ic_check, SpannableStringUtil.ALIGN_BOTTOM)
                .appendLine("end").setBackgroundColor(Color.LTGRAY)
                .appendLine("测试正常对齐").setAlignment(Layout.Alignment.ALIGN_NORMAL)
                .append("测试顶部对齐").setBackgroundColor(Color.GREEN)
                .append("image").setResourceId(R.drawable.shape_spannable_block, SpannableStringUtil.ALIGN_TOP)
                .appendLine("end").setBackgroundColor(Color.GREEN)
                .append("居中对齐").setBackgroundColor(Color.LTGRAY)
                .append("").setResourceId(R.drawable.shape_spannable_block, SpannableStringUtil.ALIGN_CENTER)
                .appendLine("end").setBackgroundColor(Color.LTGRAY)
                .append("Baseline对齐").setBackgroundColor(Color.GREEN)
                .append("").setResourceId(R.drawable.shape_spannable_block, SpannableStringUtil.ALIGN_BASELINE)
                .appendLine("end").setBackgroundColor(Color.GREEN)
                .append("底部对齐").setBackgroundColor(Color.LTGRAY)
                .append("").setResourceId(R.drawable.shape_spannable_block, SpannableStringUtil.ALIGN_BOTTOM)
                .appendLine("end").setBackgroundColor(Color.LTGRAY)
                .append("测试")
                .appendLine("点击事件").setClickSpan(new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View widget) {
                        showToast("点击事件");
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        ds.setColor(Color.BLUE);
                        ds.setUnderlineText(false);
                    }
                })
                .append("测试")
                .appendLine("Url").setUrl("https://www.baidu.com/")
                .append("测试")
                .append("模糊").setBlur(3, BlurMaskFilter.Blur.NORMAL)
                .create();
        //应点击事件的话必须设置以下属性
        tv.setMovementMethod(LinkMovementMethod.getInstance());
        tv.setTextSize(25);
        tv.setTextColor(Color.BLACK);
        tv.setLineSpacing(0, 1);  //设置lineSpacingExtra后有可能会影响图文垂直对齐
        System.out.println("");
        tv.setText(builder);
    }

    //CPU工具
    private void testCPU() {
        CommonLayoutUtil.initCommonLayout(this, "CPU工具", false, true);
        StringBuilder sb = new StringBuilder();
        sb.append("虚拟机数量：").append(CPUUtil.getProcessorsCount())
                .append("\nCPU序列号：").append(CPUUtil.getCPUSerial())
                .append("\nCPU信息：").append(CPUUtil.getCpuInfo())
                .append("\nCPU型号：").append(CPUUtil.getCpuModel())
                .append("\nCPU最大频率：").append(CPUUtil.getMaxCpuFrequency(this))
                .append("\nCPU最小频率：").append(CPUUtil.getMinCpuFrequency(this))
                .append("\nCPU当前频率：").append(CPUUtil.getCurCpuFrequency(this))
                .append("\nCPU核心数：").append(CPUUtil.getCoreNumbers());
        tv.setText(sb.toString());
    }

    //震动工具
    private void testVibration() {
        CommonLayoutUtil.initCommonLayout(this, "震动工具", "震动5秒", "pattern模式震动", "取消震动");
        btn1.setOnClickListener(v -> {
            VibrationUtil.vibrate(this, 5000);
        });
        btn2.setOnClickListener(v -> {
            long[] pattern = new long[]{100, 500, 1000, 2000, 1000, 5000};
            VibrationUtil.vibrate(this, pattern, -1);
        });
        btn3.setOnClickListener(v -> {
            VibrationUtil.cancel(this);
        });
    }

    //音频管理工具
    private void testAudio() {
        CommonLayoutUtil.initCommonLayout(this, "音频管理工具", false, true,
                "设置媒体音量静音", "调低媒体音量", "调高媒体音量", "设置闹钟音量为5", "调低闹钟音量", "调高闹钟音量",
                "设置铃声静音模式", "设置铃声正常模式");
        showAudioInfo();
        btn1.setOnClickListener(v -> {
            AudioUtil.setStreamMuteByMusic(true);
            showAudioInfo();
        });
        btn2.setOnClickListener(v -> {
            AudioUtil.adjustVolumeLower();
            showAudioInfo();
        });
        btn3.setOnClickListener(v -> {
            AudioUtil.adjustVolumeRaise();
            showAudioInfo();
        });
        btn4.setOnClickListener(v -> {
            AudioUtil.setStreamVolume(AudioManager.STREAM_ALARM, 5);
            showAudioInfo();
        });
        btn5.setOnClickListener(v -> {
            AudioUtil.adjustStreamVolumeLower(AudioManager.STREAM_ALARM);
            showAudioInfo();
        });
        btn6.setOnClickListener(v -> {
            AudioUtil.adjustStreamVolumeRaise(AudioManager.STREAM_ALARM);
            showAudioInfo();
        });
        btn7.setOnClickListener(v -> {
            AudioUtil.ringerSilent();
            showAudioInfo();
        });
        btn8.setOnClickListener(v -> {
            AudioUtil.ringerNormal();
            showAudioInfo();
        });
    }

    private void showAudioInfo() {
        String sb = getStreamVolume(AudioManager.STREAM_RING) + getStreamVolume(AudioManager.STREAM_ALARM) +
                getStreamVolume(AudioManager.STREAM_MUSIC) + getStreamVolume(AudioManager.STREAM_VOICE_CALL) +
                getStreamVolume(AudioManager.STREAM_SYSTEM) + getStreamVolume(AudioManager.STREAM_NOTIFICATION) +
                "当前音频模式：" + AudioUtil.getMode() +
                "\t\t当前铃声模式：" + AudioUtil.getRingerMode() +
                "\n是否打开扬声器：" + AudioUtil.isSpeakerphoneOn() +
                "\t\t麦克风是否静音：" + AudioUtil.isMicrophoneMute() +
                "\n是否有音乐活跃：" + AudioUtil.isMusicActive() +
                "\t\t是否插入了耳机：" + AudioUtil.isWiredHeadsetOn();
        tv.setText(sb);
    }

    private String getStreamVolume(int streamType) {
        String type = "";
        switch (streamType) {
            case AudioManager.STREAM_VOICE_CALL:
                type = "通话音量";
                break;
            case AudioManager.STREAM_SYSTEM:
                type = "系统音量";
                break;
            case AudioManager.STREAM_RING:
                type = "来电音量";
                break;
            case AudioManager.STREAM_MUSIC:
                type = "媒体音量";
                break;
            case AudioManager.STREAM_ALARM:
                type = "闹钟音量";
                break;
            case AudioManager.STREAM_NOTIFICATION:
                type = "通知音量";
                break;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(type).append("：").append(AudioUtil.getStreamVolume(streamType)).append("\t\t音量范围：");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            sb.append(AudioUtil.getStreamMinVolume(streamType));
        } else {
            sb.append("0");
        }
        sb.append("-").append(AudioUtil.getStreamMaxVolume(streamType)).append("\n");
        return sb.toString();
    }

    //亮度工具
    private void testBrightness() {
        CommonLayoutUtil.initCommonLayout(this, "亮度工具", true, true,
                "开启自动调节亮度", "设置屏幕亮度", "设置窗口亮度");
        il.setInputTextHint("请输入设置的亮度值，如50（范围0-255）");
        il.setInputTextType(InputType.TYPE_CLASS_NUMBER);
        showBrightness();
        btn1.setOnClickListener(v -> {
            if (BrightnessUtil.setAutoBrightnessEnabled(this, true)) {
                showToast("已开启");
            }
        });
        btn2.setOnClickListener(v -> {
            if (TextUtils.isEmpty(il.getInputText())) {
                showToast("请先输入要设置的亮度值");
                return;
            }
            int value = Integer.parseInt(il.getInputText());
            if (value >= 0 && value <= 255) {
                BrightnessUtil.setBrightness(this, value);
                showBrightness();
            } else {
                showToast("输入有误！");
                il.setInputText("");
            }
        });
        btn3.setOnClickListener(v -> {
            if (TextUtils.isEmpty(il.getInputText())) {
                showToast("请先输入要设置的亮度值");
                return;
            }
            int value = Integer.parseInt(il.getInputText());
            if (value >= 0 && value <= 255) {
                BrightnessUtil.setWindowBrightness(getWindow(), value);
                showBrightness();
            } else {
                showToast("输入有误！");
                il.setInputText("");
            }
        });
    }

    private void showBrightness() {
        String sb = "是否开启自动调节亮度：" + BrightnessUtil.isAutoBrightnessEnabled() +
                "\n屏幕亮度：" + BrightnessUtil.getBrightness() +
                "\n窗口亮度：" + BrightnessUtil.getWindowBrightness(getWindow());
        tv.setText(sb);
    }

    //截图工具
    private void testCapture() {
        CommonLayoutUtil.initCommonLayout(this, "截图工具", "截屏（带状态栏）", "截屏（不带状态栏）",
                "WebView截图", "View截图", "ViewCache截图", "ScrollView截图", "ListView截图", "GridView截图", "RecyclerView截图");
        btn1.setOnClickListener(v -> {
            Bitmap bitmap = CapturePictureUtil.captureWithStatusBar(this);
            saveBitmapToGallery(bitmap, "截屏带状态栏");
        });
        btn2.setOnClickListener(v -> {
            Bitmap bitmap = CapturePictureUtil.captureWithoutStatusBar(this);
            saveBitmapToGallery(bitmap, "截屏不带状态栏");
        });
        btn3.setOnClickListener(v -> {
            startActivity(TestCaptureWebViewActivity.class);
        });
        btn4.setOnClickListener(v -> {
            Bitmap bitmap = CapturePictureUtil.captureByView(v);
            saveBitmapToGallery(bitmap, "View截图");
        });
        btn5.setOnClickListener(v -> {
            Bitmap bitmap = CapturePictureUtil.captureByViewCache(v);
            saveBitmapToGallery(bitmap, "ViewCache截图");
        });
        btn6.setOnClickListener(v -> {
            Bitmap bitmap = CapturePictureUtil.captureByScrollView(binding.sv);
            saveBitmapToGallery(bitmap, "ScrollView截图");
        });
        btn7.setOnClickListener(v -> {
            startActivity(TestCaptureListViewActivity.class);
        });
        btn8.setOnClickListener(v -> {
            startActivity(TestCaptureGridViewActivity.class);
        });
        btn9.setOnClickListener(v -> {
            startActivity(TestCaptureRecyclerViewActivity.class);
        });
    }

    //保存图片到相册
    private void saveBitmapToGallery(Bitmap bitmap, String bitmapName) {
        BitmapUtil.saveBitmapToGallery(this, bitmap, bitmapName);
    }

    @Override
    protected void onDestroy() {
        if (ServiceUtil.isServiceRunning(this, LocationService.class)) {
            unbindService(mLocationConn);
        }
        super.onDestroy();
    }
}
