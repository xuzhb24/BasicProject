package com.android.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.TrafficStats;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.frame.mvc.BaseActivity;
import com.android.java.R;
import com.android.util.StatusBar.TestStatusBarUtilActivity;
import com.android.util.activity.ActivityUtil;
import com.android.util.activity.TestJumpActivity;
import com.android.util.app.AATest.TestAppListActivity;
import com.android.util.app.AppUtil;
import com.android.util.sharedPreferences.withoutContext.SPUtil;
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

import butterknife.BindView;

/**
 * Created by xuzhb on 2019/10/20
 * Desc:测试工具类方法
 */
public class TestUtilActivity extends BaseActivity {

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

    @BindView(R.id.ll)
    LinearLayout ll;
    @BindView(R.id.il)
    InputLayout il;
    @BindView(R.id.tv)
    TextView tv;
    @BindView(R.id.btn1)
    Button btn1;
    @BindView(R.id.btn2)
    Button btn2;
    @BindView(R.id.btn3)
    Button btn3;
    @BindView(R.id.btn4)
    Button btn4;
    @BindView(R.id.btn5)
    Button btn5;
    @BindView(R.id.btn6)
    Button btn6;
    @BindView(R.id.btn7)
    Button btn7;
    @BindView(R.id.btn8)
    Button btn8;
    @BindView(R.id.btn9)
    Button btn9;

    @Override
    public void handleView(Bundle savedInstanceState) {
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
        }
    }

    @Override
    public void initListener() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_common_layout;
    }

    private void testStatusBarUtil() {
        String text1 = "沉浸背景图片";
        String text2 = "状态栏白色，字体和图片黑色";
        String text3 = "状态栏黑色，字体和图片白色";
        String text4 = "状态栏黑色半透明，字体和图片白色";
        String text5 = "透明度和十六进制对应表";
        CommonLayoutUtil.initCommonLayout(this, "实现沉浸式状态栏", text1, text2, text3, text4, text5);
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
                "显示不同颜色", "带下划线", "带点击事件");
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
            if (BitmapUtil.saveImageToGallery(this, bitmap, "截屏带状态栏")) {
                showToast("保存成功！");
            } else {
                showToast("保存失败");
            }
        });
        btn2.setOnClickListener(v -> {
            Bitmap bitmap = ScreenUtil.captureWithoutStatusBar(this);
            if (BitmapUtil.saveImageToGallery(this, bitmap, "截屏不带状态栏")) {
                showToast("保存成功！");
            } else {
                showToast("保存失败");
            }
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
        rootLL.setBackgroundColor(getResources().getColor(R.color.black));
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
        view.setBackgroundColor(getResources().getColor(R.color.lightBlue));
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

}
