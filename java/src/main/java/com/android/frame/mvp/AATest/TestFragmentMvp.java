package com.android.frame.mvp.AATest;

import android.os.Bundle;

import com.android.frame.mvp.AATest.listType.TestMvpListFragment;
import com.android.frame.mvp.AATest.notListType.TestMvpFragment;
import com.android.frame.mvp.CommonBaseActivity;
import com.android.java.R;
import com.android.java.databinding.ActivityTestFragmentBinding;

/**
 * Created by xuzhb on 2021/1/4
 * Desc:
 */
public class TestFragmentMvp extends CommonBaseActivity<ActivityTestFragmentBinding> {

    @Override
    public void handleView(Bundle savedInstanceState) {
        switch (getIntent().getIntExtra("fragment_type", 1)) {
            case 1:
                mTitleBar.setTitleText("基类Fragment(MVP)");
                getSupportFragmentManager().beginTransaction().add(R.id.content_fl, TestMvpFragment.newInstance()).commit();
                break;
            case 2:
                mTitleBar.setTitleText("列表Fragment(MVP)");
                getSupportFragmentManager().beginTransaction().add(R.id.content_fl, TestMvpListFragment.newInstance()).commit();
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
