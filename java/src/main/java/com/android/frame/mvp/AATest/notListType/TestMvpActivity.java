package com.android.frame.mvp.AATest.notListType;

import android.os.Bundle;

import com.android.frame.mvc.AATest.entity.WeatherBean;
import com.android.frame.mvp.BaseActivity;
import com.android.java.databinding.ActivityTestMvcBinding;
import com.android.util.JsonUtil;
import com.google.gson.Gson;

/**
 * Created by xuzhb on 2021/1/4
 * Desc:
 */
public class TestMvpActivity extends BaseActivity<ActivityTestMvcBinding, TestMvpView, TestMvpPresenter> implements TestMvpView {

    @Override
    public void handleView(Bundle savedInstanceState) {
        mTitleBar.setTitleText("基类Activity(MVP)");
    }

    @Override
    public void initListener() {
    }

    @Override
    public TestMvpPresenter getPresenter() {
        return new TestMvpPresenter(this);
    }

    @Override
    public void showWeatherInfo(WeatherBean bean) {
        String tip = "下拉刷新获取更多城市天气\n\n";
        if (bean != null) {  //获取数据成功
            binding.tv.setText(tip + JsonUtil.formatJson(new Gson().toJson(bean)));
        } else {  //获取数据失败
            binding.tv.setText(tip);
        }
    }
}
