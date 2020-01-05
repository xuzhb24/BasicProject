package com.android.frame.mvp.AATest.activity.weather

import android.os.Bundle
import android.text.TextUtils
import com.android.basicproject.R
import com.android.frame.mvp.AATest.bean.WeatherBeanMvp
import com.android.frame.mvp.BaseCompatActivity
import com.android.util.RegexUtil
import kotlinx.android.synthetic.main.activity_weather.*

/**
 * Created by xuzhb on 2020/1/4
 * Desc:
 */
class WeatherActivity : BaseCompatActivity<WeatherView, WeatherPresenter>(), WeatherView {

    override fun handleView(savedInstanceState: Bundle?) {
        mPresenter?.getWeatherInfo("深圳")
    }

    override fun initListener() {
        title_bar.setOnLeftClickListener {
            finish()
        }
        query_btn.setOnClickListener {
            result_tv.text = ""
            if (TextUtils.isEmpty(area_il.inputText.trim())) {
                showToast("请先输入要查询的地区")
                return@setOnClickListener
            }
            mPresenter?.getWeatherInfo(area_il.inputText.trim())
        }
    }

    override fun getLayoutId(): Int = R.layout.activity_weather

    override fun getPresenter(): WeatherPresenter = WeatherPresenter(this)

    override fun showData(bean: WeatherBeanMvp) {
        val list = bean.forecast
        if (list != null && list.size > 0) {
            val sb = StringBuilder()
            val today = list.get(0)
            sb.append(bean.city).append("天气\n今天：").append(today.date).append("  ")
                .append(getWenduRange(today.high, today.low)).append("  ").append(today.type).append("\n当前温度：")
                .append(bean.wendu).append("℃\n感冒指数：").append(bean.ganmao).append("\n")
            if (list.size > 1) {
                sb.append("未来").append(list.size - 1).append("天天气：\n")
                for (i in list.indices) {
                    if (i == 0) {
                        continue
                    }
                    val forecast = list.get(i)
                    sb.append(forecast.date).append("  ").append(getWenduRange(forecast.high, forecast.low))
                        .append("  ").append(forecast.type).append("\n")
                }
            }
            result_tv.text = sb.toString()
        } else {
            result_tv.text = "未获取到天气信息"
        }
    }

    private fun getWenduRange(high: String, low: String): String =
        "${RegexUtil.extractNumber(low)}℃ 至 ${RegexUtil.extractNumber(high)}℃"
}