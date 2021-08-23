package com.android.frame.mvp.AATest.notListType;

import android.os.Bundle;

import com.android.frame.mvc.AATest.entity.WeatherBean;
import com.android.frame.mvp.BaseFragment;
import com.android.java.databinding.FragmentTestMvcBinding;
import com.android.util.JsonUtil;
import com.google.gson.Gson;

/**
 * Created by xuzhb on 2021/1/4
 * Desc:
 */
public class TestMvpFragment extends BaseFragment<FragmentTestMvcBinding, TestMvpView, TestMvpPresenter> implements TestMvpView {

    public static TestMvpFragment newInstance() {
        return new TestMvpFragment();
    }

    @Override
    public void handleView(Bundle savedInstanceState) {
    }

    @Override
    public void initListener() {
    }

    @Override
    public TestMvpPresenter getPresenter() {
        return new TestMvpPresenter(this);
    }

    @Override
    public void showWeatherInfo(String city, WeatherBean bean) {
        String tip = "下拉刷新获取更多城市天气\n\n";
        if (bean != null) {  //获取数据成功
            binding.tv.setText(tip + JsonUtil.formatJson(new Gson().toJson(bean)));
        } else {  //获取数据失败
            binding.tv.setText(tip + "获取\"" + city + "\"天气情况失败");
        }
    }

}
