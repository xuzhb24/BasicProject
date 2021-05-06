package com.android.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.android.frame.mvc.BaseFragment;
import com.android.java.databinding.FragmentUtilBinding;
import com.android.util.CommonLayoutUtil;
import com.android.util.TestUtilActivity;
import com.android.util.bitmap.TestBitmapActivity;
import com.android.util.code.TestCodeUtilActivity;
import com.android.util.glide.TestGlideActivity;
import com.android.util.permission.TestPermissionActivity;
import com.android.util.threadPool.AATest.TestThreadPoolUtilActivity;

/**
 * Created by xuzhb on 2019/10/19
 * Desc:工具篇
 */
public class UtilFragment extends BaseFragment<FragmentUtilBinding> {

    public static UtilFragment newInstance() {
        return new UtilFragment();
    }

    @Override
    public void handleView(Bundle savedInstanceState) {

    }

    @Override
    public void initListener() {
        //实现沉浸式状态栏
        binding.statusbarTv.setOnClickListener(v -> {
            CommonLayoutUtil.jumpToTestUtilActivity(mActivity, TestUtilActivity.TEST_STATUS_BAR);
        });
        //时间工具
        binding.timeTv.setOnClickListener(v -> {
            CommonLayoutUtil.jumpToTestUtilActivity(mActivity, TestUtilActivity.TEST_DATE);
        });
        //键盘工具
        binding.keyboardTv.setOnClickListener(v -> {
            CommonLayoutUtil.jumpToTestUtilActivity(mActivity, TestUtilActivity.TEST_KEYBOARD);
        });
        //代码创建Drawable
        binding.drawableTv.setOnClickListener(v -> {
            CommonLayoutUtil.jumpToTestUtilActivity(mActivity, TestUtilActivity.TEST_DRAWABLE);
        });
        //字符串工具类
        binding.stringTv.setOnClickListener(v -> {
            CommonLayoutUtil.jumpToTestUtilActivity(mActivity, TestUtilActivity.TEST_STRING);
        });
        //二维码/条形码工具
        binding.codeTv.setOnClickListener(v -> {
            startActivity(TestCodeUtilActivity.class);
        });
        //通知管理
        binding.notificationTv.setOnClickListener(v -> {
            CommonLayoutUtil.jumpToTestUtilActivity(mActivity, TestUtilActivity.TEST_NOTIFICATION);
        });
        //TrafficStats
        binding.trafficTv.setOnClickListener(v -> {
            CommonLayoutUtil.jumpToTestUtilActivity(mActivity, TestUtilActivity.TEST_TRAFFICSTATS);
        });
        //NetworkStatsManager
        binding.networkStatsTv.setOnClickListener(v -> {
            CommonLayoutUtil.jumpToTestUtilActivity(mActivity, TestUtilActivity.TEST_NETWORK_STATS);
        });
        //连续点击事件监听
        binding.continuousClickTv.setOnClickListener(v -> {
            CommonLayoutUtil.jumpToTestUtilActivity(mActivity, TestUtilActivity.TEST_CONTINUOUS_CLICK);
        });
        //拼音工具
        binding.pinyinTv.setOnClickListener(v -> {
            CommonLayoutUtil.jumpToTestUtilActivity(mActivity, TestUtilActivity.TEST_PINYIN);
        });
        //Activity工具
        binding.activityTv.setOnClickListener(v -> {
            CommonLayoutUtil.jumpToTestUtilActivity(mActivity, TestUtilActivity.TEST_ACTIVITY);
        });
        //APP工具
        binding.appTv.setOnClickListener(v -> {
            CommonLayoutUtil.jumpToTestUtilActivity(mActivity, TestUtilActivity.TEST_APP);
        });
        //设备工具
        binding.deviceTv.setOnClickListener(v -> {
            CommonLayoutUtil.jumpToTestUtilActivity(mActivity, TestUtilActivity.TEST_DEVICE);
        });
        //Shell工具
        binding.shellTv.setOnClickListener(v -> {
            CommonLayoutUtil.jumpToTestUtilActivity(mActivity, TestUtilActivity.TEST_SHELL);
        });
        //底部选择器
        binding.pickerTv.setOnClickListener(v -> {
            CommonLayoutUtil.jumpToTestUtilActivity(mActivity, TestUtilActivity.TEST_PICKER_VIEW);
        });
        //崩溃异常监听
        binding.crashTv.setOnClickListener(v -> {
            CommonLayoutUtil.jumpToTestUtilActivity(mActivity, TestUtilActivity.TEST_CRASH);
        });
        //应用文件清除工具
        binding.cleanTv.setOnClickListener(v -> {
            CommonLayoutUtil.jumpToTestUtilActivity(mActivity, TestUtilActivity.TEST_CLEAN);
        });
        //SD卡工具
        binding.sdcardTv.setOnClickListener(v -> {
            CommonLayoutUtil.jumpToTestUtilActivity(mActivity, TestUtilActivity.TEST_SDCARD);
        });
        //屏幕工具
        binding.screenTv.setOnClickListener(v -> {
            CommonLayoutUtil.jumpToTestUtilActivity(mActivity, TestUtilActivity.TEST_SCREEN);
        });
        //磁盘缓存工具
        binding.cacheTv.setOnClickListener(v -> {
            CommonLayoutUtil.jumpToTestUtilActivity(mActivity, TestUtilActivity.TEST_CACHE);
        });
        //Glide工具
        binding.glideTv.setOnClickListener(v -> {
            startActivity(TestGlideActivity.class);
        });
        //SharedPreferences工具
        binding.spTv.setOnClickListener(v -> {
            CommonLayoutUtil.jumpToTestUtilActivity(mActivity, TestUtilActivity.TEST_SP);
        });
        //布局参数工具
        binding.layoutParamsTv.setOnClickListener(v -> {
            CommonLayoutUtil.jumpToTestUtilActivity(mActivity, TestUtilActivity.TEST_LAYOUT_PARAMS);
        });
        //线程池工具
        binding.threadPoolTv.setOnClickListener(v -> {
            startActivity(TestThreadPoolUtilActivity.class);
        });
        //手机工具
        binding.phoneTv.setOnClickListener(v -> {
            CommonLayoutUtil.jumpToTestUtilActivity(mActivity, TestUtilActivity.TEST_PHONE);
        });
        //正则表达式工具
        binding.regexTv.setOnClickListener(v -> {
            CommonLayoutUtil.jumpToTestUtilActivity(mActivity, TestUtilActivity.TEST_REGEX);
        });
        //编码解码工具
        binding.encodeTv.setOnClickListener(v -> {
            CommonLayoutUtil.jumpToTestUtilActivity(mActivity, TestUtilActivity.TEST_ENCODE);
        });
        //Service工具
        binding.serviceTv.setOnClickListener(v -> {
            CommonLayoutUtil.jumpToTestUtilActivity(mActivity, TestUtilActivity.TEST_SERVICE);
        });
        //图片工具
        binding.bitmapTv.setOnClickListener(v -> {
            startActivity(TestBitmapActivity.class);
        });
        //位置工具
        binding.locationTv.setOnClickListener(v -> {
            CommonLayoutUtil.jumpToTestUtilActivity(mActivity, TestUtilActivity.TEST_LOCATION);
        });
        //网络工具
        binding.networkTv.setOnClickListener(v -> {
            CommonLayoutUtil.jumpToTestUtilActivity(mActivity, TestUtilActivity.TEST_NETWORK);
        });
        //权限工具
        binding.permissionTv.setOnClickListener(v -> {
            startActivity(TestPermissionActivity.class);
        });
        //应用下载升级
        binding.downloadTv.setOnClickListener(v -> {
            CommonLayoutUtil.jumpToTestUtilActivity(mActivity, TestUtilActivity.TEST_APK_DOWNLOAD);
        });
        //SpannableString工具
        binding.spannableTv.setOnClickListener(v -> {
            CommonLayoutUtil.jumpToTestUtilActivity(mActivity, TestUtilActivity.TEST_SPANNABLE_STRING);
        });
        //CPU工具
        binding.cpuTv.setOnClickListener(v -> {
            CommonLayoutUtil.jumpToTestUtilActivity(mActivity, TestUtilActivity.TEST_CPU);
        });
        //震动工具
        binding.vibrationTv.setOnClickListener(v -> {
            CommonLayoutUtil.jumpToTestUtilActivity(mActivity, TestUtilActivity.TEST_VIBRATION);
        });
    }

    @Override
    public FragmentUtilBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentUtilBinding.inflate(inflater, container, false);
    }

}
