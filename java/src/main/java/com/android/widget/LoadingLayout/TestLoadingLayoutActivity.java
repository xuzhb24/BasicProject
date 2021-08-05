package com.android.widget.LoadingLayout;

import android.os.Bundle;
import android.os.Handler;

import com.android.frame.mvc.BaseActivity;
import com.android.java.databinding.ActivityTestLoadingLayoutBinding;

/**
 * Created by xuzhb on 2020/7/18
 * Desc:
 */
public class TestLoadingLayoutActivity extends BaseActivity<ActivityTestLoadingLayoutBinding> {
    @Override
    public void handleView(Bundle savedInstanceState) {
        binding.loadingLayout.loadStart();
        new Handler().postDelayed(() -> binding.loadingLayout.loadComplete(), 2000);
    }

    @Override
    public void initListener() {
        //加载成功
        binding.btn1.setOnClickListener(v -> {
            binding.loadingLayout.loadStart();
            new Handler().postDelayed(() -> binding.loadingLayout.loadComplete(), 2000);
        });
        //加载失败
        binding.btn2.setOnClickListener(v -> {
            binding.loadingLayout.loadStart();
            new Handler().postDelayed(() -> binding.loadingLayout.loadFail(), 2000);
        });
        //加载后为空
        binding.btn3.setOnClickListener(v -> {
            binding.loadingLayout.loadStart();
            new Handler().postDelayed(() -> binding.loadingLayout.loadEmpty(), 2000);
        });
        //点击重试
        binding.loadingLayout.setOnFailListener(() -> {
            binding.loadingLayout.loadStart();
            new Handler().postDelayed(() -> binding.loadingLayout.loadComplete(), 2000);
        });
        //空数据时点击按钮回调
        binding.loadingLayout.setOnEmptyListener(() -> {
            showToast("页面无数据");
        });
    }

}
