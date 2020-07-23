package com.android.frame.mvc.viewBinding.AATest;

import android.os.Bundle;

import com.android.frame.mvc.viewBinding.BaseActivity_VB;
import com.android.java.databinding.ActivityCommonLayoutBinding;
import com.android.util.CommonLayoutUtil;

/**
 * Created by xuzhb on 2020/7/23
 * Desc:
 */
public class TestMvcActivity extends BaseActivity_VB<ActivityCommonLayoutBinding> {

    @Override
    public void handleView(Bundle savedInstanceState) {
        CommonLayoutUtil.initCommonLayout(this, "MVC框架", "天气信息", "网易新闻");
    }

    @Override
    public void initListener() {
        binding.btn1.setOnClickListener(v -> startActivity(WeatherActivity.class));
        binding.btn2.setOnClickListener(v -> startActivity(NewsListActivity.class));
    }

    @Override
    public ActivityCommonLayoutBinding getViewBinding() {
        return ActivityCommonLayoutBinding.inflate(getLayoutInflater());
    }

}
