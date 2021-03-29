package com.android.widget.FloatWindow.NoPermission.AATest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.FrameLayout;

import com.android.frame.mvc.BaseActivity;
import com.android.java.R;
import com.android.java.databinding.ActivityCommonLayoutBinding;
import com.android.util.CommonLayoutUtil;
import com.android.util.SizeUtil;
import com.android.widget.FloatWindow.NoPermission.FloatWindow;
import com.android.widget.FloatWindow.NoPermission.MoveType;
import com.android.widget.FloatWindow.NoPermission.ScreenType;

/**
 * Created by xuzhb on 2021/3/18
 * Desc:
 */
public class TestFloatPageActivity extends BaseActivity<ActivityCommonLayoutBinding> {

    private static final String EXTRA_INDEX = "EXTRA_INDEX";

    public static void start(Context context, int index) {
        Intent intent = new Intent(context, TestFloatPageActivity.class);
        intent.putExtra(EXTRA_INDEX, index);
        context.startActivity(intent);
    }

    private int mIndex = 0;

    @Override
    public void handleView(Bundle savedInstanceState) {
        mIndex = getIntent().getIntExtra(EXTRA_INDEX, 1);
        CommonLayoutUtil.initCommonLayout(this, "悬浮窗页面" + mIndex,
                "跳转下一个页面", "setX", "setY", "setXY", "show", "hide");
    }

    @Override
    protected void onResume() {
        super.onResume();
        //绑定悬浮窗
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.END | Gravity.BOTTOM;
        params.bottomMargin = SizeUtil.dp2pxInt(300);
        FloatWindow.get()
                .setView(R.layout.layout_float)
                .setContentViewId(R.id.content_iv)
                .setLayoutParams(params)
                .setMoveType(MoveType.active, 100, -100)
                .setOnViewListener((holder, view) -> {
                    holder.setOnClickListener(R.id.content_iv, v1 -> {
                        showToast("点到我了");
                    }).setOnClickListener(R.id.clear_iv, v1 -> {
                        FloatWindow.get().hide();
                    });
                })
                .attach(this);
    }

    @Override
    protected void onPause() {
        FloatWindow.get().detach(this);
        super.onPause();
    }

    @Override
    public void initListener() {
        binding.btn1.setOnClickListener(v -> {
            TestFloatPageActivity.start(this, ++mIndex);
        });
        binding.btn2.setOnClickListener(v -> {
            FloatWindow.get().setX(ScreenType.width, 0.4f);
        });
        binding.btn3.setOnClickListener(v -> {
            FloatWindow.get().setY(ScreenType.height, 0.4f);
        });
        binding.btn4.setOnClickListener(v -> {
            FloatWindow.get().setXY(100, 100);
        });
        binding.btn5.setOnClickListener(v -> {
            if (FloatWindow.get().isShowing()) {
                showToast("已显示");
                return;
            }
            FloatWindow.get().show();
        });
        binding.btn6.setOnClickListener(v -> {
            if (!FloatWindow.get().isShowing()) {
                showToast("已隐藏");
                return;
            }
            FloatWindow.get().hide();
        });
    }

    @Override
    public ActivityCommonLayoutBinding getViewBinding() {
        return ActivityCommonLayoutBinding.inflate(getLayoutInflater());
    }
}
