package com.android.widget;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.android.frame.mvc.BaseActivity;
import com.android.java.R;
import com.android.util.ToastUtil;

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
                new AlertDialog.Builder(TestSingleWidgetActivity.this)
                        .setMessage("本页面主要是查看编写的一些单一控件的样式")
                        .show();
            }
        });
        inputLayout.setOnTextChangedListener(new InputLayout.OnTextChangedListener() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                inputlayoutTv.setText(s);
            }
        });
        inputLayout.setOnTextClearListener(new InputLayout.OnTextClearListener() {
            @Override
            public void onTextClear() {
                showToast("文本被清空了");
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_test_single_widget;
    }

    @OnClick({R.id.verify_code_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //验证码
            case R.id.verify_code_btn:
                String code = verifyCodeEt.getText().toString().trim();
                if (!TextUtils.isEmpty(code)) {
                    if (verifyCodeView.isEquals(code)) {
                        ToastUtil.toast("验证正确");
                    } else {
                        ToastUtil.toast("验证错误");
                    }
                } else {
                    ToastUtil.toast("请输入验证码");
                }
                break;
        }
    }

}
