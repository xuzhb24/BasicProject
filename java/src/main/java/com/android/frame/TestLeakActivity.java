package com.android.frame;

import android.content.Context;
import android.os.Bundle;
import com.android.frame.mvc.BaseActivity;
import com.android.java.R;
import com.android.util.CommonLayoutUtil;

/**
 * Created by xuzhb on 2019/11/2
 * Desc:测试内存泄漏
 */
public class TestLeakActivity extends BaseActivity {

    private static Context mContext;

    @Override
    public void handleView(Bundle savedInstanceState) {
        CommonLayoutUtil.initCommonLayout(this, "测试内存泄漏");
        mContext = this;  //测试内存泄漏
    }

    @Override
    public void initListener() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_common_layout;
    }
}

