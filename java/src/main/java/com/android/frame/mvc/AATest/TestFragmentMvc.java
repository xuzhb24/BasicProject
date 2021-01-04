package com.android.frame.mvc.AATest;

import android.os.Bundle;

import com.android.frame.mvc.BaseActivity;
import com.android.java.R;
import com.android.java.databinding.ActivityTestFragmentBinding;

/**
 * Created by xuzhb on 2021/1/1
 * Desc:
 */
public class TestFragmentMvc extends BaseActivity<ActivityTestFragmentBinding> {

    @Override
    public void handleView(Bundle savedInstanceState) {
        switch (getIntent().getIntExtra("fragment_type", 1)) {
            case 1:
                mTitleBar.setTitleText("基类Fragment(MVC)");
                getSupportFragmentManager().beginTransaction().add(R.id.content_fl, TestMvcFragment.newInstance()).commit();
                break;
            case 2:
                mTitleBar.setTitleText("列表Fragment(MVC)");
                getSupportFragmentManager().beginTransaction().add(R.id.content_fl, TestMvcListFragment.newInstance()).commit();
                break;
        }
    }

    @Override
    public void initListener() {
    }

    @Override
    public ActivityTestFragmentBinding getViewBinding() {
        return ActivityTestFragmentBinding.inflate(getLayoutInflater());
    }

}
