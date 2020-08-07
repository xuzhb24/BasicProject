package com.android.frame.camera.zxing.AATest

import android.graphics.Color
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.TranslateAnimation
import com.android.basicproject.databinding.ActivityScanBinding
import com.android.frame.camera.zxing.ui.CaptureActivity
import com.android.util.StatusBar.StatusBarUtil
import kotlinx.android.synthetic.main.activity_scan.*

/**
 * Created by xuzhb on 2019/11/16
 * Desc:CaptureActivity子类
 */
class ScanActivity : CaptureActivity<ActivityScanBinding>() {

    override fun initBar() {
        StatusBarUtil.darkMode(this, Color.BLACK, 0f, false)
    }

    override fun handleView(savedInstanceState: Bundle?) {
        back_Iv.setOnClickListener { finish() }
        album_tv.setOnClickListener { openAlbum() }
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

    override fun getViewBinding() = ActivityScanBinding.inflate(layoutInflater)

}