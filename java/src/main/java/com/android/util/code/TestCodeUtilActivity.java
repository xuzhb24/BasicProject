package com.android.util.code;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.OnClick;
import com.android.frame.mvc.BaseActivity;
import com.android.java.R;
import com.android.util.BitmapUtil;
import com.android.util.SizeUtil;
import com.android.widget.TitleBar;

/**
 * Created by xuzhb on 2019/11/18
 * Desc:
 */
public class TestCodeUtilActivity extends BaseActivity {
    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.et)
    EditText et;
    @BindView(R.id.iv)
    ImageView iv;

    @Override
    public void handleView(Bundle savedInstanceState) {
        et.setText("1234567890abcdefg");
    }

    @Override
    public void initListener() {
        titleBar.setOnLeftClickListener(new TitleBar.OnLeftClickListener() {
            @Override
            public void onLeftClick(View v) {
                finish();
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_test_code_util;
    }

    @OnClick({R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5})
    public void onViewClicked(View view) {
        String content = et.getText().toString().trim();
        if (TextUtils.isEmpty(content) && view.getId() != R.id.btn4) {
            showToast("输入内容不能为空！");
            return;
        }
        int width = (int) SizeUtil.dp2px(250);
        int height = (int) SizeUtil.dp2px(250);
        switch (view.getId()) {
            case R.id.btn1:
                Bitmap bitmap1 = QRCodeUtil.createQRCode(content, width, height);
                if (bitmap1 != null) {
                    iv.setImageBitmap(bitmap1);
                }
                break;
            case R.id.btn2:
                Bitmap logo = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_logo);
                Bitmap bitmap2 = QRCodeUtil.createQRCode(content, width, height, logo);
                if (bitmap2 != null) {
                    iv.setImageBitmap(bitmap2);
                }
                break;
            case R.id.btn3:
                Bitmap bitmap3 = BarCodeUtil.creatBarcode(content, width, height / 3);
                if (bitmap3 != null) {
                    iv.setImageBitmap(bitmap3);
                }
                break;
            case R.id.btn4:
                boolean flag = BitmapUtil.saveImageToGallery(this,
                        ((BitmapDrawable) iv.getDrawable()).getBitmap(),
                        System.currentTimeMillis() + "");
                if (flag) {
                    showToast("保存成功！");
                } else {
                    showToast("保存失败！");
                }
                break;
            case R.id.btn5:
                et.setText("");
                break;
        }
    }
}
