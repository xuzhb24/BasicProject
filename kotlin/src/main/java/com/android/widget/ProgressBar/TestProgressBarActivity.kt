package com.android.widget.ProgressBar

import android.graphics.Color
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import com.android.basicproject.databinding.ActivityTestProgressBarBinding
import com.android.frame.mvc.BaseActivity

class TestProgressBarActivity : BaseActivity<ActivityTestProgressBarBinding>() {

    override fun handleView(savedInstanceState: Bundle?) {
        binding.circleCpb1.startAnim(75)
        with(binding.circleCpb2) {
            rindColorArray = intArrayOf(
                Color.parseColor("#0888FF"),
                Color.parseColor("#6CD0FF")
            )
            startAnim(85)
        }
        binding.arcAv1.startRotate()
        with(binding.arcAv2) {
            rindColorArray = intArrayOf(
                Color.parseColor("#0888FF"),
                Color.parseColor("#6CD0FF")
            )
            startRotate(500, AccelerateDecelerateInterpolator())
        }
    }

    override fun initListener() {
    }

    override fun getViewBinding() = ActivityTestProgressBarBinding.inflate(layoutInflater)

}