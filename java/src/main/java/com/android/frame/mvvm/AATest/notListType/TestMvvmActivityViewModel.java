package com.android.frame.mvvm.AATest.notListType;

import androidx.annotation.Nullable;

import com.android.frame.http.model.BaseResponse;
import com.android.frame.mvc.AATest.entity.WeatherBean;
import com.android.frame.mvvm.AATest.server.ApiHelper;
import com.android.frame.mvvm.BaseViewModelWithData;
import com.android.java.databinding.ActivityTestMvcBinding;
import com.android.util.JsonUtil;
import com.google.gson.Gson;

/**
 * Created by xuzhb on 2021/8/9
 * Desc:BaseViewModelWithData的使用，简化了很多，BaseViewModel的使用参见TestMvvmFragmentViewModel
 */
public class TestMvvmActivityViewModel extends BaseViewModelWithData<BaseResponse<WeatherBean>, ActivityTestMvcBinding> {

    private String mCity;

    public void showWeatherInfo(String city) {
        mCity = city;
        launchMain(ApiHelper.getWeatherByQuery(city));
    }

    @Override
    public void onSuccess(BaseResponse<WeatherBean> response) {
        String tip = "下拉刷新获取更多城市天气\n\n";
        binding.tv.setText(tip + JsonUtil.formatJson(new Gson().toJson(response)));
    }

    @Override
    protected void onFailure(String message, boolean isException, @Nullable @org.jetbrains.annotations.Nullable Throwable exception, @Nullable @org.jetbrains.annotations.Nullable BaseResponse<WeatherBean> response) {
        super.onFailure(message, isException, exception, response);
        String tip = "下拉刷新获取更多城市天气\n\n";
        binding.tv.setText(tip + "获取\"" + mCity + "\"天气情况失败");
    }
}
