package com.android.base;

import android.os.Bundle;
import android.view.View;
import butterknife.OnClick;
import com.android.frame.mvc.BaseFragment;
import com.android.java.R;
import com.android.util.CommonLayoutUtil;
import com.android.util.TestUtilActivity;
import com.android.util.code.TestCodeUtilActivity;

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
            R.id.pinyin_tv, R.id.activity_tv, R.id.app_tv, R.id.device_tv, R.id.shell_tv, R.id.picker_tv})
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
        }
    }
}
