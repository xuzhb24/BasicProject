package com.android.frame.mvvm.AATest.notListType;

import android.os.Bundle;

import com.android.frame.mvvm.BaseActivity;
import com.android.java.databinding.ActivityTestMvcBinding;

import java.util.Random;

/**
 * Created by xuzhb on 2021/8/9
 * Desc:
 */
public class TestMvvmActivity extends BaseActivity<ActivityTestMvcBinding, TestMvvmActivityViewModel> {

    private String[] mCities = new String[]{
            "深圳", "福州", "北京", "广州", "上海", "厦门",
            "泉州", "重庆", "天津", "啦啦", "哈哈", "随便"
    };

    @Override
    public void handleView(Bundle savedInstanceState) {
        mTitleBar.setTitleText("基类Activity(MVVM)");
    }

    @Override
    public void initListener() {

    }

    //在这里处理从服务器加载和显示数据的逻辑，请务必重写refreshData，当加载失败点击重试时会调用这个方法
    @Override
    protected void refreshData() {
        viewModel.showWeatherInfo(mCities[new Random().nextInt(mCities.length)]);
    }
}
