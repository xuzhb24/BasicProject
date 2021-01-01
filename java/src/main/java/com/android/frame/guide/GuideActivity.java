package com.android.frame.guide;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.viewpager.widget.ViewPager;

import com.android.base.MainActivity;
import com.android.frame.mvc.BaseActivity;
import com.android.java.R;
import com.android.java.databinding.ActivityGuideBinding;
import com.android.util.SizeUtil;
import com.android.util.StatusBar.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuzhb on 2020/7/4
 * Desc:引导页
 */
public class GuideActivity extends BaseActivity<ActivityGuideBinding> {

    private int[] mLayoutIds = new int[]{R.layout.layout_guide_one, R.layout.layout_guide_two, R.layout.layout_guide_three};

    @Override
    protected void initBar() {
        StatusBarUtil.darkMode(this);
    }

    @Override
    public void handleView(Bundle savedInstanceState) {
        List<View> viewList = new ArrayList<>();
        for (int i = 0; i < mLayoutIds.length; i++) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setButtonDrawable(R.drawable.selector_guide_indicator);
            ViewGroup.MarginLayoutParams layoutParams = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            int margin = (int) SizeUtil.dp2px(6f);
            layoutParams.leftMargin = margin;
            layoutParams.rightMargin = margin;
            radioButton.setLayoutParams(layoutParams);
            radioButton.setTag(i);
            radioButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    binding.guideVp.setCurrentItem((int) buttonView.getTag());
                }
            });
            binding.guideRg.addView(radioButton, layoutParams);
            if (i == 0) {
                radioButton.setChecked(true);
            }
            View view = getLayoutInflater().inflate(mLayoutIds[i], null);
            viewList.add(view);
        }
        binding.guideVp.setAdapter(new SimpleViewAdapter(viewList));
        binding.guideVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int position) {
                RadioButton radioButton = binding.guideRg.findViewWithTag(position);
                if (!radioButton.isChecked()) {
                    radioButton.setChecked(true);
                }
                if (position == mLayoutIds.length - 1) {
                    binding.useBtn.setVisibility(View.VISIBLE);
                } else {
                    binding.useBtn.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    @Override
    public void initListener() {
        binding.useBtn.setOnClickListener(v -> {
            startActivity(MainActivity.class);
            finish();
        });
    }

    @Override
    public ActivityGuideBinding getViewBinding() {
        return ActivityGuideBinding.inflate(getLayoutInflater());
    }
}
