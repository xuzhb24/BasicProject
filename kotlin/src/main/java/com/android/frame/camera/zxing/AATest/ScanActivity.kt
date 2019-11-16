package com.android.frame.camera.zxing.AATest

import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.TranslateAnimation
import com.android.basicproject.R
import com.android.frame.camera.zxing.ui.CaptureActivity
import kotlinx.android.synthetic.main.activity_scan.*

/**
 * Created by xuzhb on 2019/11/16
 * Desc:CaptureActivity子类
 */
class ScanActivity : CaptureActivity() {

    override fun handleView(savedInstanceState: Bundle?) {
        startScanAnim()
    }

    private fun startScanAnim() {
        val animation = TranslateAnimation(
            TranslateAnimation.ABSOLUTE, 0f, TranslateAnimation.ABSOLUTE, 0f,
            TranslateAnimation.RELATIVE_TO_PARENT, -1f, TranslateAnimation.RELATIVE_TO_PARENT, 0f
        )
        with(animation) {
            duration = 2000
            repeatCount = -1
            repeatMode = Animation.RESTART
            interpolator = DecelerateInterpolator()
        }
        grid_iv.setAnimation(animation)
    }

    override fun getLayoutId(): Int = R.layout.activity_scan

}