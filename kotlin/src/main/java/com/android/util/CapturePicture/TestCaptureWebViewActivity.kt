package com.android.util.CapturePicture

import android.os.Bundle
import com.android.basicproject.databinding.ActivityTestCaptureWebviewBinding
import com.android.frame.mvc.BaseActivity
import com.android.util.bitmap.BitmapUtil

/**
 * Created by xuzhb on 2021/5/13
 * Desc:
 */
class TestCaptureWebViewActivity : BaseActivity<ActivityTestCaptureWebviewBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        CapturePictureUtil.enableSlowWholeDocumentDraw()
        super.onCreate(savedInstanceState)
    }

    override fun handleView(savedInstanceState: Bundle?) {
        binding.webView.loadUrl("https://xw.qq.com/")
    }

    override fun initListener() {
        mTitleBar?.setOnRightTextClickListener {
            val bitmap = CapturePictureUtil.captureByWebView(binding.webView)
            BitmapUtil.saveBitmapToGallery(this, bitmap, "WebView截图")
        }
    }

}