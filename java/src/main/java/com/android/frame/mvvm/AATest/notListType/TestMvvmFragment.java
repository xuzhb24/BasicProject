package com.android.frame.mvvm.AATest.notListType;

import android.os.Bundle;

import com.android.frame.mvvm.BaseFragment;
import com.android.java.databinding.FragmentTestMvcBinding;

import java.util.Random;

/**
 * Created by xuzhb on 2021/8/9
 * Desc:
 */
public class TestMvvmFragment extends BaseFragment<FragmentTestMvcBinding, TestMvvmFragmentViewModel> {

    public static TestMvvmFragment newInstance() {
        return new TestMvvmFragment();
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

    //在这里处理从服务器加载和显示数据的逻辑，请务必重写refreshData，当加载失败点击重试时会调用这个方法
    @Override
    protected void refreshData() {
        //模拟一个页面多个接口请求的情况，假设其中一个接口不参与UI显示的逻辑，只保存数据
        viewModel.saveWeatherInfo(mCities[new Random().nextInt(mCities.length)]);  //假定这个接口只保存数据
        viewModel.showWeatherInfo(mCities[new Random().nextInt(mCities.length)]);  //主接口，会将接口结果反馈给UI
    }

    @Override
    public void loadFinish(boolean isError) {
        System.out.println("TestMvvmFragment 加载完成：" + isError);
        super.loadFinish(isError);
    }
}
