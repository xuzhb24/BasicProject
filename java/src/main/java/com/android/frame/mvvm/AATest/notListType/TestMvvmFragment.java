package com.android.frame.mvvm.AATest.notListType;

import android.os.Bundle;

import com.android.frame.mvvm.BaseFragment;
import com.android.java.databinding.FragmentTestMvcBinding;
import com.android.util.JsonUtil;
import com.android.util.LogUtil;
import com.google.gson.Gson;

import java.util.Random;

/**
 * Created by xuzhb on 2021/8/9
 * Desc:
 */
public class TestMvvmFragment extends BaseFragment<FragmentTestMvcBinding, TestMvvmFragmentViewModel> {

    private static final String TAG = "TestMvvmFragment";

    public static TestMvvmFragment newInstance() {
        return new TestMvvmFragment();
    }

    private String mCity;
    private String[] mCities = new String[]{
            "深圳", "福州", "北京", "广州", "上海", "厦门",
            "泉州", "重庆", "天津", "啦啦", "哈哈", "随便"
    };

    @Override
    public void handleView(Bundle savedInstanceState) {
    }

    @Override
    protected void initViewModelObserver() {
        super.initViewModelObserver();
        String tip = "下拉刷新获取更多城市天气\n\n";
        viewModel.mSuccessData1.observe(this, it -> {
            binding.tv.setText(tip + JsonUtil.formatJson(new Gson().toJson(it)));
            LogUtil.i(TAG, "请求成功");
        });
        viewModel.mErrorData1.observe(this, it -> {
            showToast(it.getMessage());
            binding.tv.setText(tip + "获取\"" + mCity + "\"天气情况失败");
            LogUtil.i(TAG, "请求失败");
        });
        viewModel.mSuccessData2.observe(this, it -> {
            LogUtil.i(TAG, "保存成功，" + tip + JsonUtil.formatJson(new Gson().toJson(it)));
        });
        viewModel.mErrorData2.observe(this, it -> {
            LogUtil.i(TAG, "保存失败，" + it.getMessage());
        });
    }

    @Override
    public void initListener() {

    }

    //在这里处理从服务器加载和显示数据的逻辑，请务必重写refreshData，当加载失败点击重试时会调用这个方法
    @Override
    protected void refreshData() {
        //模拟一个页面多个接口请求的情况，假设其中一个接口不参与UI显示的逻辑，只保存数据
        viewModel.saveWeatherInfo(mCities[new Random().nextInt(mCities.length)]);  //假定这个接口只保存数据
        mCity = mCities[new Random().nextInt(mCities.length)];
        viewModel.showWeatherInfo(mCity);  //主接口，会将接口结果反馈给UI
    }

    @Override
    public void loadFinish(boolean isError) {
        LogUtil.i(TAG, "加载完成：" + isError);
        super.loadFinish(isError);
    }
}
