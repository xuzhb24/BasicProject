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

    @OnClick({R.id.time_tv, R.id.keyboard_tv, R.id.drawable_tv, R.id.code_tv, R.id.notification_tv,
            R.id.traffic_tv, R.id.network_stats_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.time_tv:
                CommonLayoutUtil.jumpToTestUtilActivity(mActivity, TestUtilActivity.TEST_DATE);
                break;
            case R.id.keyboard_tv:
                CommonLayoutUtil.jumpToTestUtilActivity(mActivity, TestUtilActivity.TEST_KEYBOARD);
                break;
            case R.id.drawable_tv:
                CommonLayoutUtil.jumpToTestUtilActivity(mActivity, TestUtilActivity.TEST_DRAWABLE);
                break;
            case R.id.code_tv:
                startActivity(TestCodeUtilActivity.class);
                break;
            case R.id.notification_tv:
                CommonLayoutUtil.jumpToTestUtilActivity(mActivity, TestUtilActivity.TEST_NOTIFICATION);
                break;
            case R.id.traffic_tv:
                CommonLayoutUtil.jumpToTestUtilActivity(mActivity, TestUtilActivity.TEST_TRAFFICSTATS);
                break;
            case R.id.network_stats_tv:
                CommonLayoutUtil.jumpToTestUtilActivity(mActivity, TestUtilActivity.TEST_NETWORK_STATS);
                break;
        }
    }
}
