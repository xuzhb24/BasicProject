package com.android.base;

import android.os.Bundle;
import android.view.View;

import com.android.frame.TestLeakActivity;
import com.android.frame.guide.GuideActivity;
import com.android.frame.http.AATest.TestRetrofitActivity;
import com.android.frame.mvc.BaseFragment;
import com.android.frame.mvc.viewBinding.AATest.TestMvcActivity;
import com.android.frame.mvp.AATest.activity.TestMvpActivity;
import com.android.java.R;
import com.android.widget.TitleBar;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xuzhb on 2019/10/19
 * Desc:框架篇
 */
public class FrameFragment extends BaseFragment {

    @BindView(R.id.title_bar)
    TitleBar titleBar;

    public static FrameFragment newInstance() {
        return new FrameFragment();
    }

    @Override
    public void handleView(Bundle savedInstanceState) {

    }

    @Override
    public void initListener() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_frame;
    }

    @OnClick({R.id.leak_tv, R.id.orignal_permission_tv, R.id.easy_permission_tv, R.id.retrofit_tv,
            R.id.mvp_tv, R.id.mvc_tv, R.id.guide_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.leak_tv:
                startActivity(TestLeakActivity.class);
                break;
            case R.id.orignal_permission_tv:
                break;
            case R.id.easy_permission_tv:
                break;
            case R.id.retrofit_tv:
                startActivity(TestRetrofitActivity.class);
                break;
            case R.id.mvp_tv:
                startActivity(TestMvpActivity.class);
                break;
            case R.id.mvc_tv:
                startActivity(TestMvcActivity.class);
                break;
            case R.id.guide_tv:
                startActivity(GuideActivity.class);
                break;
        }
    }
}
