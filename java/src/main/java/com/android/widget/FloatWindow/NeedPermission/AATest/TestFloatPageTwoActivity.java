package com.android.widget.FloatWindow.NeedPermission.AATest;

import android.os.Bundle;

import com.android.frame.mvc.BaseActivity;
import com.android.java.databinding.ActivityCommonLayoutBinding;
import com.android.util.CommonLayoutUtil;

/**
 * Created by xuzhb on 2021/3/10
 * Desc:
 */
public class TestFloatPageTwoActivity extends BaseActivity<ActivityCommonLayoutBinding> {

    @Override
    public void handleView(Bundle savedInstanceState) {
        CommonLayoutUtil.initCommonLayout(this, "悬浮窗页面二", false, true, "跳转到悬浮窗页面三");
        binding.tv.setText("悬浮窗A不在页面二显示");
    }

    @Override
    public void initListener() {
        binding.btn1.setOnClickListener(v -> {
            startActivity(TestFloatPageThreeActivity.class);
        });
    }

}