package com.android.widget;

import android.os.Bundle;
import android.text.TextUtils;

import com.android.frame.mvc.BaseActivity;
import com.android.java.databinding.ActivityTestSingleWidgetBinding;
import com.android.util.ExtraUtil;
import com.android.widget.LoadingDialog.LoadingDialog;

/**
 * Created by xuzhb on 2019/10/20
 * Desc:
 */
public class TestSingleWidgetActivity extends BaseActivity<ActivityTestSingleWidgetBinding> {

    @Override
    public void handleView(Bundle savedInstanceState) {

    }

    @Override
    public void initListener() {
        //标题栏
        mTitleBar.setOnRightTextClickListener(v -> ExtraUtil.alert(TestSingleWidgetActivity.this, "本页面主要是查看编写的一些单一控件的样式"));
        //加载框
        binding.loadingBtn1.setOnClickListener(v -> {
            LoadingDialog dialog = new LoadingDialog(this);
            dialog.show();
        });
        binding.loadingBtn2.setOnClickListener(v -> {
            LoadingDialog dialog = new LoadingDialog(this);
            dialog.show("加载中...");
        });
        binding.loadingBtn3.setOnClickListener(v -> {
            LoadingDialog dialog = new LoadingDialog(this);
            dialog.show(null, false);
        });
        //密码输入框
        binding.passwordEdittext.setOnTextChangeListener(text -> binding.petTv.setText(text));
        binding.passwordEdittext.setOnTextCompleteListener(this::showToast);
        binding.petBtn.setOnClickListener(v -> binding.passwordEdittext.clearText());
        //验证码
        binding.verifyCodeBtn.setOnClickListener(v -> {
            String code = binding.verifyCodeEt.getText().toString().trim();
            if (!TextUtils.isEmpty(code)) {
                if (binding.verifyCodeView.isEquals(code)) {
                    showToast("验证正确");
                } else {
                    showToast("验证错误");
                }
            } else {
                showToast("请输入验证码");
            }
        });
        //带删除按钮的输入框
        binding.inputLayout.setOnTextChangedListener((s, start, before, count) -> binding.inputlayoutTv.setText(s));
        binding.inputLayout.setOnTextClearListener(() -> showToast("文本被清空了"));
    }

    @Override
    public ActivityTestSingleWidgetBinding getViewBinding() {
        return ActivityTestSingleWidgetBinding.inflate(getLayoutInflater());
    }

}
