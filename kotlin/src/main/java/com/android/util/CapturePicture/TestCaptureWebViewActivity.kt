package com.android.util.CapturePicture

import android.Manifest
import android.os.Bundle
import com.android.basicproject.databinding.ActivityTestCaptureWebviewBinding
import com.android.frame.mvc.BaseActivity
import com.android.util.bitmap.BitmapUtil
import com.android.util.permission.PermissionUtil

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
        binding.webView.loadUrl("https://sports.sina.cn")
    }

    override fun initListener() {
        mTitleBar?.setOnRightTextClickListener {
            if (!PermissionUtil.requestPermissions(
                    this, 1,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {
                showToast("请先允许权限")
                return@setOnRightTextClickListener
            }
            val bitmap = CapturePictureUtil.captureByWebView(binding.webView)
            if (BitmapUtil.saveBitmapToGallery(this, bitmap, "WebView截图")) {
                showToast("保存成功，请在相册查看")
            } else {
                showToast("保存失败")
            }
        }
    }

    override fun getViewBinding() = ActivityTestCaptureWebviewBinding.inflate(layoutInflater)

}