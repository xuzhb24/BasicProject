package com.android.base;

import android.os.Bundle;
import android.view.View;

import com.android.frame.mvc.BaseFragment;
import com.android.java.R;
import com.android.util.CommonLayoutUtil;
import com.android.util.TestUtilActivity;
import com.android.util.code.TestCodeUtilActivity;
import com.android.util.threadPool.AATest.TestThreadPoolUtilActivity;

import butterknife.OnClick;

/**
 * Created by xuzhb on 2019/10/19
 * Desc:工具篇
 */
public class UtilFragment extends BaseFragment {

    public static UtilFragment newInstance() {
        return new UtilFragment();
    }

    @Override
    public void handleView(Bundle savedInstanceState) {

    }

    @Override
    public void initListener() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_util;
    }

    @OnClick({R.id.statusbar_tv, R.id.time_tv, R.id.keyboard_tv, R.id.drawable_tv, R.id.string_tv, R.id.code_tv,
            R.id.notification_tv, R.id.traffic_tv, R.id.network_stats_tv, R.id.continuous_click_tv,
            R.id.pinyin_tv, R.id.activity_tv, R.id.app_tv, R.id.device_tv, R.id.shell_tv, R.id.picker_tv,
            R.id.crash_tv, R.id.clean_tv, R.id.sdcard_tv, R.id.screen_tv, R.id.cache_tv, R.id.sp_tv,
            R.id.layout_params_tv, R.id.thread_pool_tv, R.id.phone_tv, R.id.regex_tv, R.id.encode_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.statusbar_tv:  //实现沉浸式状态栏
                CommonLayoutUtil.jumpToTestUtilActivity(mActivity, TestUtilActivity.TEST_STATUS_BAR);
                break;
            case R.id.time_tv:  //时间工具
                CommonLayoutUtil.jumpToTestUtilActivity(mActivity, TestUtilActivity.TEST_DATE);
                break;
            case R.id.keyboard_tv:  //键盘工具
                CommonLayoutUtil.jumpToTestUtilActivity(mActivity, TestUtilActivity.TEST_KEYBOARD);
                break;
            case R.id.drawable_tv:  //代码创建Drawable
                CommonLayoutUtil.jumpToTestUtilActivity(mActivity, TestUtilActivity.TEST_DRAWABLE);
                break;
            case R.id.string_tv:  //字符串工具类
                CommonLayoutUtil.jumpToTestUtilActivity(mActivity, TestUtilActivity.TEST_STRING);
                break;
            case R.id.code_tv:  //二维码/条形码工具
                startActivity(TestCodeUtilActivity.class);
                break;
            case R.id.notification_tv:  //通知管理
                CommonLayoutUtil.jumpToTestUtilActivity(mActivity, TestUtilActivity.TEST_NOTIFICATION);
                break;
            case R.id.traffic_tv:  //TrafficStats
                CommonLayoutUtil.jumpToTestUtilActivity(mActivity, TestUtilActivity.TEST_TRAFFICSTATS);
                break;
            case R.id.network_stats_tv:  //NetworkStatsManager
                CommonLayoutUtil.jumpToTestUtilActivity(mActivity, TestUtilActivity.TEST_NETWORK_STATS);
                break;
            case R.id.continuous_click_tv:  //连续点击事件监听
                CommonLayoutUtil.jumpToTestUtilActivity(mActivity, TestUtilActivity.TEST_CONTINUOUS_CLICK);
                break;
            case R.id.pinyin_tv:  //拼音工具
                CommonLayoutUtil.jumpToTestUtilActivity(mActivity, TestUtilActivity.TEST_PINYIN);
                break;
            case R.id.activity_tv:  //Activity工具
                CommonLayoutUtil.jumpToTestUtilActivity(mActivity, TestUtilActivity.TEST_ACTIVITY);
                break;
            case R.id.app_tv:  //APP工具
                CommonLayoutUtil.jumpToTestUtilActivity(mActivity, TestUtilActivity.TEST_APP);
                break;
            case R.id.device_tv:  //设备工具
                CommonLayoutUtil.jumpToTestUtilActivity(mActivity, TestUtilActivity.TEST_DEVICE);
                break;
            case R.id.shell_tv:  //Shell工具
                CommonLayoutUtil.jumpToTestUtilActivity(mActivity, TestUtilActivity.TEST_SHELL);
                break;
            case R.id.picker_tv:  //底部选择器
                CommonLayoutUtil.jumpToTestUtilActivity(mActivity, TestUtilActivity.TEST_PICKER_VIEW);
                break;
            case R.id.crash_tv:  //崩溃异常监听
                CommonLayoutUtil.jumpToTestUtilActivity(mActivity, TestUtilActivity.TEST_CRASH);
                break;
            case R.id.clean_tv:  //应用文件清除工具
                CommonLayoutUtil.jumpToTestUtilActivity(mActivity, TestUtilActivity.TEST_CLEAN);
                break;
            case R.id.sdcard_tv:  //SD卡工具
                CommonLayoutUtil.jumpToTestUtilActivity(mActivity, TestUtilActivity.TEST_SDCARD);
                break;
            case R.id.screen_tv:  //屏幕工具
                CommonLayoutUtil.jumpToTestUtilActivity(mActivity, TestUtilActivity.TEST_SCREEN);
                break;
            case R.id.cache_tv:  //磁盘缓存工具
                CommonLayoutUtil.jumpToTestUtilActivity(mActivity, TestUtilActivity.TEST_CACHE);
                break;
            case R.id.sp_tv:  //SharedPreferences工具
                CommonLayoutUtil.jumpToTestUtilActivity(mActivity, TestUtilActivity.TEST_SP);
                break;
            case R.id.layout_params_tv:  //布局参数工具
                CommonLayoutUtil.jumpToTestUtilActivity(mActivity, TestUtilActivity.TEST_LAYOUT_PARAMS);
                break;
            case R.id.thread_pool_tv:  //线程池工具
                startActivity(TestThreadPoolUtilActivity.class);
                break;
            case R.id.phone_tv:  //手机工具
                CommonLayoutUtil.jumpToTestUtilActivity(mActivity, TestUtilActivity.TEST_PHONE);
                break;
            case R.id.regex_tv:  //正则表达式工具
                CommonLayoutUtil.jumpToTestUtilActivity(mActivity, TestUtilActivity.TEST_REGEX);
                break;
            case R.id.encode_tv:  //编码解码工具
                CommonLayoutUtil.jumpToTestUtilActivity(mActivity, TestUtilActivity.TEST_ENCODE);
                break;
        }
    }
}
