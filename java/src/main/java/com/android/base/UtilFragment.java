package com.android.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.android.frame.mvc.BaseFragment;
import com.android.java.R;
import com.android.util.CommonLayoutUtil;
import com.android.util.TestUtilActivity;

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

    @OnClick({R.id.time_tv, R.id.keyboard_tv, R.id.drawable_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.time_tv:
                break;
            case R.id.keyboard_tv:
                break;
            case R.id.drawable_tv:
                CommonLayoutUtil.jumpToTestUtilActivity(mActivity, TestUtilActivity.TEST_DRAWABLE);
                break;
        }
    }
}
