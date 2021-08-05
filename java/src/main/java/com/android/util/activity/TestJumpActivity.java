package com.android.util.activity;

import android.os.Bundle;

import com.android.frame.mvc.BaseActivity;
import com.android.java.R;
import com.android.java.databinding.ActivityCommonLayoutBinding;
import com.android.util.CommonLayoutUtil;

/**
 * Created by xuzhb on 2020/2/3
 * Desc:
 */
public class TestJumpActivity extends BaseActivity<ActivityCommonLayoutBinding> {

    public static final String EXTRA_DATA = "EXTRA_DATA";

    @Override
    public void handleView(Bundle savedInstanceState) {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            CommonLayoutUtil.initCommonLayout(this, "TestJumpActivity", false, true);
            binding.tv.setText(bundle.getString(EXTRA_DATA));
        } else {
            CommonLayoutUtil.initCommonLayout(this, "TestJumpActivity", "返回");
        }

    }

    @Override
    public void initListener() {
        binding.btn1.setOnClickListener(v -> {
            finish();
            overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
        });
    }

}
