package com.android.widget.LineChart

import android.os.Bundle
import com.android.basicproject.R
import com.android.frame.mvc.BaseActivity
import com.android.util.DateUtil
import com.android.util.SizeUtil
import kotlinx.android.synthetic.main.activity_test_line_chart.*
import kotlin.random.Random

/**
 * Created by xuzhb on 2019/10/16
 * Desc:
 */
class TestLineChartActivity : BaseActivity() {

    override fun handleView(savedInstanceState: Bundle?) {
        testType1()
        testType2()
    }

    private fun testType1() {
        createType1Data(4, 6)
        type1_btn1.setOnClickListener { createType1Data(6) }
        type1_btn2.setOnClickListener { createType1Data(14) }
        type1_btn3.setOnClickListener { createType1Data(29) }
    }

    private fun createType1Data(count: Int, xLabelCount: Int = 0) {
        val list: MutableList<Float> = mutableListOf()
        for (i in 0..count) {
            list.add(Random.nextDouble(1000.0).toFloat())
        }
        type1_lc.xLabelCount = if (xLabelCount == 0) list.size - 1 else xLabelCount
        type1_lc.setData(list)
    }

    private fun testType2() {
        createType2Data(4, true)
        type2_btn1.setOnClickListener { createType2Data(0) }
        type2_btn2.setOnClickListener { createType2Data(5) }
        type2_btn3.setOnClickListener { createType2Data(4, showYLabelText = true) }
        type2_btn4.setOnClickListener { createType2Data(5, startAnim = true) }
    }

    private fun createType2Data(count: Int, isDateMore: Boolean = false, startAnim: Boolean = false, showYLabelText: Boolean = false) {
        val list1: MutableList<Float> = mutableListOf()
        val list2: MutableList<Float> = mutableListOf()
        val dateList: MutableList<String> = mutableListOf()
        for (i in 0..count) {
            list1.add(Random.nextDouble(80.0).toFloat())
            list2.add(Random.nextDouble(80.0).toFloat())
            dateList.add(DateUtil.getDistanceDateByDay(i - count, DateUtil.M_D))
        }
        if (isDateMore) {
            dateList.add(DateUtil.getDistanceDateByDay(1, DateUtil.M_D))
        }
        if (showYLabelText) {
            type2_lc.leftMargin = SizeUtil.dp2px(40f)
        } else {
            type2_lc.leftMargin = SizeUtil.dp2px(15f)
        }
        type2_lc.showYLabelText = showYLabelText
        type2_lc.drawData(list1, list2, dateList, startAnim)
    }

    override fun initListener() {
        title_bar.setOnLeftClickListener {
            finish()
        }
    }

    override fun getLayoutId(): Int = R.layout.activity_test_line_chart
}