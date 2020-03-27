package com.android.widget.ProgressBar

import android.graphics.Color
import android.os.Bundle
import com.android.basicproject.R
import com.android.frame.mvc.BaseActivity
import kotlinx.android.synthetic.main.activity_test_progress_bar.*

class TestProgressBarActivity : BaseActivity() {

    override fun handleView(savedInstanceState: Bundle?) {
        circle_cpb1.startAnim(75)
        with(circle_cpb2) {
            rindColorArray = intArrayOf(
                Color.parseColor("#0888FF"),
                Color.parseColor("#6CD0FF")
            )
            startAnim(85)
        }
    }

    override fun initListener() {
        title_bar.setOnLeftClickListener {
            finish()
        }
    }

    override fun getLayoutId(): Int = R.layout.activity_test_progress_bar
}