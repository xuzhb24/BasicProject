package com.android.frame.mvc.AATest;

import android.os.Bundle;

import com.android.frame.http.model.BaseResponse;
import com.android.frame.mvc.AATest.entity.WeatherBean;
import com.android.frame.mvc.AATest.server.ApiHelper;
import com.android.frame.mvc.BaseActivity;
import com.android.frame.mvc.IBaseView;
import com.android.frame.mvc.extra.http.CustomObserver;
import com.android.java.databinding.ActivityTestMvcBinding;
import com.android.util.JsonUtil;
import com.google.gson.Gson;

import java.util.Random;

/**
 * Created by xuzhb on 2021/1/1
 * Desc:
 */
public class TestMvcActivity extends BaseActivity<ActivityTestMvcBinding> {

    private String[] mCities = new String[]{
            "深圳", "福州", "北京", "广州", "上海", "厦门",
            "泉州", "重庆", "天津", "啦啦", "哈哈", "随便"
    };

    @Override
    public void handleView(Bundle savedInstanceState) {
        mTitleBar.setTitleText("基类Activity(MVC)");
    }

    @Override
    public void initListener() {
    }

    //在这里处理从服务器加载和显示数据的逻辑，请务必重写refreshData，当加载失败点击重试时会调用这个方法
    @Override
    protected void refreshData() {
        showWeatherInfo(mCities[new Random().nextInt(mCities.length)]);
    }

    private void showWeatherInfo(String city) {
        String tip = "下拉刷新获取更多城市天气\n\n";
        ApiHelper.getWeatherByQuery(city)
                .subscribe(new CustomObserver<BaseResponse<WeatherBean>>(this) {
                    @Override
                    public void onSuccess(BaseResponse<WeatherBean> response) {
                        binding.tv.setText(tip + JsonUtil.formatJson(new Gson().toJson(response)));
                    }

                    @Override
                    protected void onFailure(IBaseView view, String message, boolean isError, Throwable t, BaseResponse<WeatherBean> response) {
                        super.onFailure(view, message, isError, t, response);
                        binding.tv.setText(tip);
                    }
                });
    }

}
