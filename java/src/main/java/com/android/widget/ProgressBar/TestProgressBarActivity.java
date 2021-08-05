package com.android.widget.ProgressBar;

import android.graphics.Color;
import android.os.Bundle;

import com.android.frame.mvc.BaseActivity;
import com.android.java.databinding.ActivityTestProgressBarBinding;

/**
 * Created by xuzhb on 2020/7/11
 * Desc:
 */
public class TestProgressBarActivity extends BaseActivity<ActivityTestProgressBarBinding> {
    @Override
    public void handleView(Bundle savedInstanceState) {
        int[] rindColorArray = new int[]{Color.parseColor("#0888FF"), Color.parseColor("#6CD0FF")};
        binding.arcAv1.startRotate();
        binding.arcAv2.setRindColorArray(rindColorArray);
        binding.arcAv2.startRotate();
    }

    @Override
    public void initListener() {

    }

}
