package com.android.frame.mvvm.AATest.notListType;

import android.os.Bundle;

import com.android.frame.mvvm.BaseActivity;
import com.android.java.databinding.ActivityTestMvcBinding;
import com.android.util.JsonUtil;
import com.google.gson.Gson;

import java.util.Random;

/**
 * Created by xuzhb on 2021/8/9
 * Desc:
 */
public class TestMvvmActivity extends BaseActivity<ActivityTestMvcBinding, TestMvvmActivityViewModel> {

    private String mCity;
    private String[] mCities = new String[]{
            "深圳", "福州", "北京", "广州", "上海", "厦门",
            "泉州", "重庆", "天津", "啦啦", "哈哈", "随便"
    };

    @Override
    public void handleView(Bundle savedInstanceState) {
        mTitleBar.setTitleText("基类Activity(MVVM)");
    }

    @Override
    protected void initViewModelObserver() {
        super.initViewModelObserver();
        viewModel.successData.observe(this, it -> {
            String tip = "下拉刷新获取更多城市天气\n\n";
            binding.tv.setText(tip + JsonUtil.formatJson(new Gson().toJson(it)));
        });
        viewModel.errorData.observe(this, it -> {
            showToast(it.getMessage());
            String tip = "下拉刷新获取更多城市天气\n\n";
            binding.tv.setText(tip + "获取\"" + mCity + "\"天气情况失败");
        });
    }

    @Override
    public void initListener() {

    }

    //在这里处理从服务器加载和显示数据的逻辑，请务必重写refreshData，当加载失败点击重试时会调用这个方法
    @Override
    protected void refreshData() {
        mCity = mCities[new Random().nextInt(mCities.length)];
        viewModel.showWeatherInfo(mCity);
    }
}
