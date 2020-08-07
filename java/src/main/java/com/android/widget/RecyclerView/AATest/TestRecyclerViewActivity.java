package com.android.widget.RecyclerView.AATest;

import android.os.Bundle;

import com.android.frame.mvc.BaseActivity;
import com.android.java.databinding.ActivityCommonLayoutBinding;
import com.android.util.CommonLayoutUtil;

/**
 * Create by xuzhb on 2020/1/21
 * Desc:
 */
public class TestRecyclerViewActivity extends BaseActivity<ActivityCommonLayoutBinding> {

    @Override
    public void handleView(Bundle savedInstanceState) {
        CommonLayoutUtil.initCommonLayout(
                this, "RecyclerView",
                "单一布局", "多布局", "头部Header", "尾部Footer", "下拉刷新和上拉加载更多",
                "单一布局上拉加载更多", "多布局上拉加载更多"
        );
    }

    @Override
    public void initListener() {
        binding.btn1.setOnClickListener(v -> {  //单一布局
            startActivity(TestSingleAdapterActivity.class);
        });
        binding.btn2.setOnClickListener(v -> {  //多布局
            startActivity(TestMultiAdapterActivity.class);
        });
        binding.btn3.setOnClickListener(v -> {  //头部Header
            startActivity(TestHeaderAdapterActivity.class);
        });
        binding.btn4.setOnClickListener(v -> {  //尾部Footer
            startActivity(TestFooterAdapterActivity.class);
        });
        binding.btn5.setOnClickListener(v -> {  //下拉刷新和上拉加载更多
            startActivity(TestLoadMoreWrapperActivity.class);
        });
        binding.btn6.setOnClickListener(v -> {  //单一布局上拉加载更多
            startActivity(TestSingleLoadMoreAdapterActivity.class);
        });
        binding.btn7.setOnClickListener(v -> {  //多布局上拉加载更多
            startActivity(TestMultiLoadMoreAdapterActivity.class);
        });
    }

    @Override
    public ActivityCommonLayoutBinding getViewBinding() {
        return ActivityCommonLayoutBinding.inflate(getLayoutInflater());
    }

}
