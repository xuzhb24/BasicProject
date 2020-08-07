package com.android.frame.mvp.AATest.activity;

import android.os.Bundle;

import com.android.frame.mvp.AATest.activity.newslist.NewsListActivity;
import com.android.frame.mvp.AATest.activity.weather.WeatherActivity;
import com.android.frame.mvp.CommonBaseActivity;
import com.android.java.databinding.ActivityCommonLayoutBinding;
import com.android.util.CommonLayoutUtil;

/**
 * Created by xuzhb on 2020/1/8
 * Desc:
 */
public class TestMvpActivity extends CommonBaseActivity<ActivityCommonLayoutBinding> {

    @Override
    public void handleView(Bundle savedInstanceState) {
        CommonLayoutUtil.initCommonLayout(this, "MVP框架", "天气信息", "网易新闻");
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
