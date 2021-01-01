package com.android.frame.mvc.AATest;

import android.os.Bundle;

import com.android.frame.mvc.BaseActivity;
import com.android.java.databinding.ActivityCommonLayoutBinding;
import com.android.util.CommonLayoutUtil;

/**
 * Created by xuzhb on 2021/1/1
 * Desc:
 */
public class TestActivityMvc extends BaseActivity<ActivityCommonLayoutBinding> {

    @Override
    public void handleView(Bundle savedInstanceState) {
        CommonLayoutUtil.initCommonLayout(this, "MVC框架", "BaseActivity", "BaseListActivity", "BaseFragment", "BaseListFragment");
    }

    @Override
    public void initListener() {
        binding.btn1.setOnClickListener(v -> {
            startActivity(TestMvcActivity.class);
        });
        binding.btn2.setOnClickListener(v -> {
            startActivity(TestMvcListActivity.class);
        });
        binding.btn3.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("fragment_type", 1);
            startActivity(TestFragmentMvc.class, bundle);
        });
        binding.btn4.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("fragment_type", 2);
            startActivity(TestFragmentMvc.class, bundle);
        });
    }

    @Override
    public ActivityCommonLayoutBinding getViewBinding() {
        return ActivityCommonLayoutBinding.inflate(getLayoutInflater());
    }

}
