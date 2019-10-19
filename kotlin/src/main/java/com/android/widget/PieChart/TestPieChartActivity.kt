package com.android.widget.PieChart

import android.graphics.Color
import android.os.Bundle
import com.android.basicproject.R
import com.android.frame.mvc.BaseActivity
import kotlinx.android.synthetic.main.activity_test_pie_chart.*

/**
 * Created by xuzhb on 2019/10/15
 * Desc:
 */
class TestPieChartActivity : BaseActivity() {
    override fun handleView(savedInstanceState: Bundle?) {

        val list: MutableList<PieData> = mutableListOf()
        list.add(PieData(20f, Color.parseColor("#3CACEC"), "餐饮美食"))
        list.add(PieData(10f, Color.parseColor("#7CC653"), "通讯物流"))
        list.add(PieData(10f, Color.parseColor("#FFBA00"), "生活日用"))
        list.add(PieData(10f, Color.parseColor("#FF7054"), "交通出行"))
        list.add(PieData(10f, Color.parseColor("#097DE2"), "其他"))
        with(type1_pc) {
            setData(list)
            startAnimation(true, 2000)
        }

        type2_pc.setData(list)

    }

    override fun initListener() {
        title_bar.setOnLeftClickListener {
            finish()
        }
    }

    override fun getLayoutId(): Int = R.layout.activity_test_pie_chart

}