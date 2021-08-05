package com.android.util.CapturePicture;

import android.Manifest;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.android.frame.mvc.BaseActivity;
import com.android.java.databinding.ActivityTestCaptureWebviewBinding;
import com.android.util.bitmap.BitmapUtil;
import com.android.util.permission.PermissionUtil;

/**
 * Created by xuzhb on 2021/5/12
 * Desc:测试WebView截图
 */
public class TestCaptureWebViewActivity extends BaseActivity<ActivityTestCaptureWebviewBinding> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        CapturePictureUtil.enableSlowWholeDocumentDraw();
        super.onCreate(savedInstanceState);
    }

    @Override
    public void handleView(Bundle savedInstanceState) {
        binding.webView.loadUrl("https://sports.sina.cn");
    }

    @Override
    public void initListener() {
        mTitleBar.setOnRightTextClickListener(v -> {
            if (!PermissionUtil.requestPermissions(this, 1,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                showToast("请先允许权限");
                return;
            }
            Bitmap bitmap = CapturePictureUtil.captureByWebView(binding.webView);
            if (BitmapUtil.saveBitmapToGallery(this, bitmap, "WebView截图")) {
                showToast("保存成功，请在相册查看");
            } else {
                showToast("保存失败");
            }
        });
    }

}
