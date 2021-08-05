package com.android.widget.PieChart

import android.graphics.Color
import android.os.Bundle
import com.android.basicproject.databinding.ActivityTestPieChartBinding
import com.android.frame.mvc.BaseActivity

/**
 * Created by xuzhb on 2019/10/15
 * Desc:
 */
class TestPieChartActivity : BaseActivity<ActivityTestPieChartBinding>() {
    override fun handleView(savedInstanceState: Bundle?) {

        val list: MutableList<PieData> = mutableListOf()
        list.add(PieData(20f, Color.parseColor("#3CACEC"), "餐饮美食"))
        list.add(PieData(10f, Color.parseColor("#7CC653"), "通讯物流"))
        list.add(PieData(10f, Color.parseColor("#FFBA00"), "生活日用"))
        list.add(PieData(10f, Color.parseColor("#FF7054"), "交通出行"))
        list.add(PieData(10f, Color.parseColor("#097DE2"), "其他"))
        with(binding.type1Pc) {
            setData(list)
            startAnimation(true, 2000)
        }
        binding.type2Pc.setData(list)

    }

    override fun initListener() {
    }

}