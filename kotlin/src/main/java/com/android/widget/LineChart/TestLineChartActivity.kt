package com.android.widget.LineChart

import android.os.Bundle
import com.android.basicproject.databinding.ActivityTestLineChartBinding
import com.android.frame.mvc.BaseActivity
import com.android.util.DateUtil
import com.android.util.SizeUtil
import kotlin.random.Random

/**
 * Created by xuzhb on 2019/10/16
 * Desc:
 */
class TestLineChartActivity : BaseActivity<ActivityTestLineChartBinding>() {

    override fun handleView(savedInstanceState: Bundle?) {
        testType1()
        testType2()
    }

    private fun testType1() {
        createType1Data(4, 6)
        binding.type1Btn1.setOnClickListener { createType1Data(6) }
        binding.type1Btn2.setOnClickListener { createType1Data(14) }
        binding.type1Btn3.setOnClickListener { createType1Data(29) }
    }

    private fun createType1Data(count: Int, xLabelCount: Int = 0) {
        val list: MutableList<Float> = mutableListOf()
        for (i in 0..count) {
            list.add(Random.nextDouble(1000.0).toFloat())
        }
        binding.type1Lc.xLabelCount = if (xLabelCount == 0) list.size - 1 else xLabelCount
        binding.type1Lc.setData(list)
    }

    private fun testType2() {
        createType2Data(4, true)
        binding.type2Btn1.setOnClickListener { createType2Data(0) }
        binding.type2Btn2.setOnClickListener { createType2Data(5) }
        binding.type2Btn3.setOnClickListener { createType2Data(4, showYLabelText = true) }
        binding.type2Btn4.setOnClickListener { createType2Data(5, startAnim = true) }
    }

    private fun createType2Data(
        count: Int,
        isDateMore: Boolean = false,
        startAnim: Boolean = false,
        showYLabelText: Boolean = false
    ) {
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
            binding.type2Lc.leftMargin = SizeUtil.dp2px(40f)
        } else {
            binding.type2Lc.leftMargin = SizeUtil.dp2px(15f)
        }
        binding.type2Lc.showYLabelText = showYLabelText
        binding.type2Lc.drawData(list1, list2, dateList, startAnim)
    }

    override fun initListener() {
    }

    override fun getViewBinding() = ActivityTestLineChartBinding.inflate(layoutInflater)

}