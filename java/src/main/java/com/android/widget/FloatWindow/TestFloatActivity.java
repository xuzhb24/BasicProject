package com.android.widget.FloatWindow;

import android.os.Bundle;

import com.android.frame.mvc.BaseActivity;
import com.android.java.databinding.ActivityCommonLayoutBinding;
import com.android.util.CommonLayoutUtil;
import com.android.widget.FloatWindow.NeedPermission.AATest.TestFloatPageOneActivity;
import com.android.widget.FloatWindow.NoPermission.AATest.TestFloatPageActivity;

/**
 * Created by xuzhb on 2021/3/22
 * Desc:
 */
public class TestFloatActivity extends BaseActivity<ActivityCommonLayoutBinding> {
    @Override
    public void handleView(Bundle savedInstanceState) {
        CommonLayoutUtil.initCommonLayout(this, "悬浮窗", "申请权限", "无需权限");
    }

    @Override
    public void initListener() {
        binding.btn1.setOnClickListener(v -> {
            startActivity(TestFloatPageOneActivity.class);
        });
        binding.btn2.setOnClickListener(v -> {
            TestFloatPageActivity.start(this, 1);
        });
    }

    @Override
    public ActivityCommonLayoutBinding getViewBinding() {
        return ActivityCommonLayoutBinding.inflate(getLayoutInflater());
    }
}
