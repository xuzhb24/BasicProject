package com.android.widget.FloatWindow.NeedPermission.AATest;

import android.os.Bundle;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;

import com.android.base.MainActivity;
import com.android.frame.mvc.BaseActivity;
import com.android.java.R;
import com.android.java.databinding.ActivityCommonLayoutBinding;
import com.android.util.CommonLayoutUtil;
import com.android.util.LogUtil;
import com.android.util.SizeUtil;
import com.android.widget.FloatWindow.NeedPermission.FloatWindow;
import com.android.widget.FloatWindow.NeedPermission.MoveType;
import com.android.widget.FloatWindow.NeedPermission.OnPermissionListener;
import com.android.widget.FloatWindow.NeedPermission.OnViewStateListener;
import com.android.widget.FloatWindow.NeedPermission.ScreenType;

/**
 * Created by xuzhb on 2021/3/10
 * Desc:
 */
public class TestFloatPageOneActivity extends BaseActivity<ActivityCommonLayoutBinding> {

    private static final String TAG = "TestFloatAActivity";

    @Override
    public void handleView(Bundle savedInstanceState) {
        CommonLayoutUtil.initCommonLayout(this, "悬浮窗页面一",
                "创建悬浮窗A", "销毁悬浮窗A", "创建悬浮窗B", "销毁悬浮窗B", "跳转到悬浮窗页面二");
    }

    @Override
    public void initListener() {
        //创建悬浮窗
        binding.btn1.setOnClickListener(v -> {
            //这个悬浮窗的创建尽量放在Application的onCreate中，否则可能导致内存泄漏
            FloatWindow.with(getApplicationContext())
                    .setView(R.layout.layout_float)
                    .setContentViewId(R.id.content_iv)
                    .setWidth(SizeUtil.dp2pxInt(100f))
                    .setHeight(SizeUtil.dp2pxInt(100f))
                    .setX(0)
                    .setY(ScreenType.height, 0.1f)
                    .setMoveType(MoveType.slide, -SizeUtil.dp2pxInt(50), SizeUtil.dp2pxInt(50))
                    .setMoveStyle(500, new BounceInterpolator())
                    .setDesktopShow(true, MainActivity.class)
                    .setFilter(false, TestFloatPageTwoActivity.class)
                    .setOnPermissionListener(new OnPermissionListener() {
                        @Override
                        public void onSuccess() {
                            LogUtil.i(TAG, "onSuccess");
                        }

                        @Override
                        public void onFailure() {
                            LogUtil.i(TAG, "onFailure");
                        }
                    })
                    .setOnViewListener((holder, view) -> {
                        holder.setOnClickListener(R.id.clear_iv, v2 -> FloatWindow.destroy())
                                .setOnClickListener(R.id.content_iv, v2 -> showToast("点到我了"));
                    })
                    .setOnViewStateListener(new OnViewStateListener() {
                        @Override
                        public void onPositionUpdate(int x, int y) {
                            LogUtil.i(TAG, "onPositionUpdate " + x + " " + y);
                        }

                        @Override
                        public void onShow() {
                            LogUtil.i(TAG, "onShow");
                        }

                        @Override
                        public void onHide() {
                            LogUtil.i(TAG, "onHide");
                        }

                        @Override
                        public void onDismiss() {
                            LogUtil.i(TAG, "onDismiss");
                        }

                        @Override
                        public void onMoveAnimStart() {
                            LogUtil.i(TAG, "onMoveAnimStart");
                        }

                        @Override
                        public void onMoveAnimEnd() {
                            LogUtil.i(TAG, "onMoveAnimEnd");
                        }

                        @Override
                        public void onBackToDesktop() {
                            LogUtil.i(TAG, "onBackToDesktop");
                        }
                    })
                    .build();
        });
        binding.btn2.setOnClickListener(v -> {
            FloatWindow.destroy();
        });
        binding.btn3.setOnClickListener(v -> {
            ImageView imageView = new ImageView(getApplicationContext());
            imageView.setImageResource(R.drawable.ic_face1);
            FloatWindow.with(getApplicationContext())
                    .setView(imageView)
                    .setTag("FloatWindowB")
                    .setWidth(ScreenType.width, 0.3f)
                    .setHeight(ScreenType.width, 0.3f)
                    .setX(SizeUtil.dp2pxInt(100f))
                    .setY(SizeUtil.dp2pxInt(100f))
                    .setMoveType(MoveType.active, 0, 50)
                    .setDesktopShow(false, MainActivity.class)
                    .build();
        });
        binding.btn4.setOnClickListener(v -> {
            FloatWindow.destroy("FloatWindowB");
        });
        binding.btn5.setOnClickListener(v -> {
            startActivity(TestFloatPageTwoActivity.class);
        });
    }

    @Override
    public ActivityCommonLayoutBinding getViewBinding() {
        return ActivityCommonLayoutBinding.inflate(getLayoutInflater());
    }

}
