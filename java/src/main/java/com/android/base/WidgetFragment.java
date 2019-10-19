package com.android.base;

import android.os.Bundle;
import com.android.frame.mvc.BaseFragment;
import com.android.java.R;

/**
 * Created by xuzhb on 2019/10/19
 * Desc:控件篇
 */
public class WidgetFragment extends BaseFragment {

    public static WidgetFragment newInstance() {
        return new WidgetFragment();
    }

    @Override
    public void handleView(Bundle savedInstanceState) {

    }

    @Override
    public void initListener() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_widget;
    }
}
