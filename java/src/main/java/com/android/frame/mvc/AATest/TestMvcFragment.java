package com.android.frame.mvc.AATest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.android.frame.http.model.BaseResponse;
import com.android.frame.mvc.AATest.entity.WeatherBean;
import com.android.frame.mvc.AATest.server.ApiHelper;
import com.android.frame.mvc.BaseFragment;
import com.android.frame.mvc.IBaseView;
import com.android.frame.mvc.extra.http.CustomObserver;
import com.android.java.databinding.FragmentTestMvcBinding;
import com.android.util.JsonUtil;
import com.google.gson.Gson;

import java.util.Random;

/**
 * Created by xuzhb on 2021/1/1
 * Desc:
 */
public class TestMvcFragment extends BaseFragment<FragmentTestMvcBinding> {

    public static TestMvcFragment newInstance() {
        return new TestMvcFragment();
    }

    private String[] mCities = new String[]{
            "深圳", "福州", "北京", "广州", "上海", "厦门",
            "泉州", "重庆", "天津", "啦啦", "哈哈", "随便"
    };

    @Override
    public void handleView(Bundle savedInstanceState) {
    }

    @Override
    public void initListener() {
    }

    @Override
    public FragmentTestMvcBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentTestMvcBinding.inflate(inflater, container, false);
    }

    //在这里处理从服务器加载和显示数据的逻辑，请务必重写refreshData，当加载失败点击重试时会调用这个方法
    @Override
    protected void refreshData() {
        //模拟一个页面多个接口请求的情况，假设其中一个接口不参与UI显示的逻辑，只保存数据
        saveWeatherInfo(mCities[new Random().nextInt(mCities.length)]);  //假定这个接口只保存数据
        showWeatherInfo(mCities[new Random().nextInt(mCities.length)]);  //主接口，会将接口结果反馈给UI
    }

    //这个接口只保存数据，不参与UI逻辑
    private void saveWeatherInfo(String city) {
        ApiHelper.getWeatherByQuery(city)
                //不显示加载状态，不显示加载弹窗，不处理加载完成的逻辑（needLoadFinish设为false，因为这个接口不参与UI界面的显示，让主接口处理加载状态）
                .subscribe(new CustomObserver<BaseResponse<WeatherBean>>(this, false, false, false) {
                    @Override
                    public void onSuccess(BaseResponse<WeatherBean> response) {
                        System.out.println("TestMvcFragment 请求成功，可以保存数据了，" + new Gson().toJson(response));
                    }

                    @Override
                    protected void onFailure(IBaseView view, String message, boolean isError, Throwable t, BaseResponse<WeatherBean> response) {
                        //如果这个接口加载异常时不需要提示Toast，重写onFailure并去掉super即可，onFailure中也可以处理其他失败的逻辑
//                        super.onFailure(view, message, isError, t, response);
                        System.out.println("TestMvcFragment 请求失败，" + response);
                    }
                });
    }

    private void showWeatherInfo(String city) {
        String tip = "下拉刷新获取更多城市天气\n\n";
        ApiHelper.getWeatherByQuery(city)
                .subscribe(new CustomObserver<BaseResponse<WeatherBean>>(this, false, true) {
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
