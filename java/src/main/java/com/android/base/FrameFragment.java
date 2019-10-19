package com.android.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.android.frame.mvc.BaseFragment;
import com.android.java.R;
import com.android.widget.TitleBar;

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

    @OnClick({R.id.leak_tv, R.id.orignal_permission_tv, R.id.easy_permission_tv, R.id.retrofit_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.leak_tv:
                break;
            case R.id.orignal_permission_tv:
                break;
            case R.id.easy_permission_tv:
                break;
            case R.id.retrofit_tv:
                break;
        }
    }
}
