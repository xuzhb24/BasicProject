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
            sb.append("${bean.city}天气\n今天：${today.date}  ${getWenduRange(today.high, today.low)}  ${today.type}\n")
            sb.append("当前温度：${bean.wendu}℃\n感冒指数：${bean.ganmao}\n")
            if (list.size > 1) {
                sb.append("未来${list.size - 1}天天气：\n")
                for (i in list.indices) {
                    if (i == 0) {
                        continue
                    }
                    val forecast = list.get(i)
                    sb.append("${forecast.date}  ${getWenduRange(forecast.high, forecast.low)}  ${forecast.type}\n")
                }
            }
            result_tv.text = sb.toString()
        } else {
            result_tv.text = "未获取到天气信息"
        }
    }

    private fun getWenduRange(high: String, low: String): String =
        "${RegexUtil.extractDigit(low)}℃ - ${RegexUtil.extractDigit(high)}℃"
}