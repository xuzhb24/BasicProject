package com.android.util.CapturePicture;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.android.frame.mvc.BaseActivity;
import com.android.java.databinding.ActivityTestCaptureWebviewBinding;
import com.android.util.bitmap.BitmapUtil;

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
        binding.webView.loadUrl("https://xw.qq.com/");
    }

    @Override
    public void initListener() {
        mTitleBar.setOnRightTextClickListener(v -> {
            Bitmap bitmap = CapturePictureUtil.captureByWebView(binding.webView);
            BitmapUtil.saveBitmapToGallery(this, bitmap, "WebView截图");
        });
    }

}
