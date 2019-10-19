package com.android.widget.LineChart

import android.os.Bundle
import com.android.basicproject.R
import com.android.frame.mvc.BaseActivity
import kotlinx.android.synthetic.main.activity_test_line_chart.*
import kotlin.random.Random

/**
 * Created by xuzhb on 2019/10/16
 * Desc:
 */
class TestLineChartActivity : BaseActivity() {
    override fun handleView(savedInstanceState: Bundle?) {
        val list: MutableList<Float> = mutableListOf()
        for (i in 1..6) {
            list.add(Random.nextDouble(1000.0).toFloat())
        }
        type1_lc.setData(list)
    }

    override fun initListener() {
        title_bar.setOnLeftClickListener {
            finish()
        }
    }

    override fun getLayoutId(): Int = R.layout.activity_test_line_chart
}