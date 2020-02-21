package com.android.universal

import android.animation.ValueAnimator
import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.animation.LinearInterpolator
import kotlinx.android.synthetic.main.activity_test_system_widget.*
import kotlinx.android.synthetic.main.layout_title_bar.*

/**
 * Created by xuzhb on 2020/2/16
 * Desc:系统自带控件的使用
 */
class TestSystemWidgetActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_system_widget)
        initTitleBar()
        initListener()
    }

    private fun initTitleBar() {
        title_fl.setBackgroundColor(resources.getColor(R.color.colorPrimary))
        val dp20 = (20 * resources.displayMetrics.density + 0.5f).toInt()
        title_fl.setPadding(dp20, 0, dp20, 0)
        title_tv.text = "系统控件"
        title_tv.setTextColor(Color.WHITE)
        right_tv.text = "说明"
        right_tv.setTextColor(Color.WHITE)
        left_fl.setOnClickListener {
            finish()
        }
        right_tv.setOnClickListener {
            AlertDialog.Builder(this)
                .setMessage("本页面主要记录一些系统自带控件的属性和使用方法")
                .show()
        }
    }

    private fun initListener() {
        //ProgressBar
        val animator = ValueAnimator.ofInt(0, 100)
        animator.addUpdateListener {
            //onAnimationUpdate的调用次数和setDuration有关
            val value = it.animatedValue  //数值
            val fraction = it.animatedFraction  //百分比
            progress_pb.progress = value as Int
            if ((fraction * 100).toInt() % 1 == 0) {  //调用100次
                progress_tv.text = "${value} ${fraction}"
            }
        }
        animator.interpolator = LinearInterpolator()
        animator.setDuration(5 * 1000).start()
    }

}