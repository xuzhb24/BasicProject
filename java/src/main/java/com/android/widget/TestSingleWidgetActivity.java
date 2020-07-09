package com.android.widget;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.frame.mvc.BaseActivity;
import com.android.java.R;
import com.android.util.ExtraUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xuzhb on 2019/10/20
 * Desc:
 */
public class TestSingleWidgetActivity extends BaseActivity {

    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.verify_code_view)
    VerifyCodeView verifyCodeView;
    @BindView(R.id.verify_code_et)
    EditText verifyCodeEt;
    @BindView(R.id.input_layout)
    InputLayout inputLayout;
    @BindView(R.id.inputlayout_tv)
    TextView inputlayoutTv;
    @BindView(R.id.password_edittext)
    PasswordEditText passwordEdittext;
    @BindView(R.id.pet_tv)
    TextView petTv;

    @Override
    public void handleView(Bundle savedInstanceState) {

    }

    @Override
    public void initListener() {
        //标题栏
        titleBar.setOnLeftClickListener(new TitleBar.OnLeftClickListener() {
            @Override
            public void onLeftClick(View v) {
                finish();
            }
        });
        titleBar.setOnRightClickListener(new TitleBar.OnRightClickListener() {
            @Override
            public void onRightClick(View v) {
                ExtraUtil.alert(TestSingleWidgetActivity.this, "本页面主要是查看编写的一些单一控件的样式");
            }
        });
        //带删除按钮的输入框
        inputLayout.setOnTextChangedListener((s, start, before, count) -> inputlayoutTv.setText(s));
        inputLayout.setOnTextClearListener(() -> showToast("文本被清空了"));
        //密码输入框
        passwordEdittext.setOnTextChangeListener(text -> {
            petTv.setText(text);
        });
        passwordEdittext.setOnTextCompleteListener(this::showToast);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_test_single_widget;
    }

    @OnClick({R.id.verify_code_btn, R.id.pet_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.verify_code_btn:  //验证码
                String code = verifyCodeEt.getText().toString().trim();
                if (!TextUtils.isEmpty(code)) {
                    if (verifyCodeView.isEquals(code)) {
                        showToast("验证正确");
                    } else {
                        showToast("验证错误");
                    }
                } else {
                    showToast("请输入验证码");
                }
                break;
            case R.id.pet_btn:  //密码输入框
                passwordEdittext.clearText();
                break;
        }
    }

}
