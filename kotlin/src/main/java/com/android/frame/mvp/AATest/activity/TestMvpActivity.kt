package com.android.frame.mvp.AATest.activity

import android.os.Bundle
import com.android.basicproject.R
import com.android.frame.mvp.AATest.activity.newslist.NewsListActivity
import com.android.frame.mvp.AATest.activity.weather.WeatherActivity
import com.android.frame.mvp.BaseCompatActivity
import com.android.frame.mvp.CommonBaseActivity
import com.android.util.initCommonLayout
import kotlinx.android.synthetic.main.activity_common_layout.*

/**
 * Created by xuzhb on 2020/1/4
 * Desc:
 */
class TestMvpActivity : CommonBaseActivity() {

    override fun handleView(savedInstanceState: Bundle?) {
        initCommonLayout(this, "MVP框架", "天气信息", "网易新闻")
    }

    override fun initListener() {
        btn1.setOnClickListener {
            startActivity(WeatherActivity::class.java)
        }
        btn2.setOnClickListener {
            startActivity(NewsListActivity::class.java)
        }
    }

    override fun getLayoutId(): Int = R.layout.activity_common_layout

}