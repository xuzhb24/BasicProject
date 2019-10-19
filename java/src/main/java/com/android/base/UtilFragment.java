package com.android.base;

import android.os.Bundle;
import com.android.frame.mvc.BaseFragment;
import com.android.java.R;

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
}
