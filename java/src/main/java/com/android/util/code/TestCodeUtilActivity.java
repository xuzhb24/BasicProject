package com.android.util.code;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;

import com.android.frame.mvc.BaseActivity;
import com.android.java.R;
import com.android.java.databinding.ActivityTestCodeUtilBinding;
import com.android.util.SizeUtil;
import com.android.util.bitmap.BitmapUtil;

/**
 * Created by xuzhb on 2019/11/18
 * Desc:
 */
public class TestCodeUtilActivity extends BaseActivity<ActivityTestCodeUtilBinding> {

    @Override
    public void handleView(Bundle savedInstanceState) {
        binding.il.setInputText("1234567890abcdefg");
    }

    @Override
    public void initListener() {
        binding.btn1.setOnClickListener(v -> {
            String content = binding.il.getInputText().trim();
            if (TextUtils.isEmpty(content)) {
                showToast("输入内容不能为空！");
                return;
            }
            int width = (int) SizeUtil.dp2px(250);
            int height = (int) SizeUtil.dp2px(250);
            Bitmap bitmap = QRCodeUtil.createQRCode(content, width, height);
            if (bitmap != null) {
                binding.iv.setImageBitmap(bitmap);
            }
        });
        binding.btn2.setOnClickListener(v -> {
            String content = binding.il.getInputText().trim();
            if (TextUtils.isEmpty(content)) {
                showToast("输入内容不能为空！");
                return;
            }
            int width = (int) SizeUtil.dp2px(250);
            int height = (int) SizeUtil.dp2px(250);
            Bitmap logo = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            Bitmap bitmap = QRCodeUtil.createQRCode(content, width, height, logo);
            if (bitmap != null) {
                binding.iv.setImageBitmap(bitmap);
            }
        });
        binding.btn3.setOnClickListener(v -> {
            String content = binding.il.getInputText().trim();
            if (TextUtils.isEmpty(content)) {
                showToast("输入内容不能为空！");
                return;
            }
            int width = (int) SizeUtil.dp2px(250);
            int height = (int) SizeUtil.dp2px(250);
            Bitmap bitmap = BarCodeUtil.creatBarcode(content, width, height / 3);
            if (bitmap != null) {
                binding.iv.setImageBitmap(bitmap);
            }
        });
        binding.btn4.setOnClickListener(v -> {
            if (binding.iv.getDrawable() == null) {
                showToast("请先生成二维码");
                return;
            }
            boolean flag = BitmapUtil.saveBitmapToGallery(this,
                    ((BitmapDrawable) binding.iv.getDrawable()).getBitmap(),
                    System.currentTimeMillis() + "");
            if (flag) {
                showToast("保存成功！");
            } else {
                showToast("保存失败！");
            }
        });
    }

}
