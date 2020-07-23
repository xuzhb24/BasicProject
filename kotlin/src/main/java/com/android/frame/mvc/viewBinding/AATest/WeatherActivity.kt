package com.android.frame.mvc.viewBinding.AATest

import android.os.Bundle
import android.text.TextUtils
import com.android.basicproject.databinding.ActivityWeatherBinding
import com.android.frame.http.RetrofitFactory
import com.android.frame.http.SchedulerUtil
import com.android.frame.http.model.BaseResponse
import com.android.frame.mvc.viewBinding.BaseActivity_VB
import com.android.frame.mvc.viewBinding.extra.CustomObserver
import com.android.frame.mvp.AATest.ApiServiceMvp
import com.android.frame.mvp.AATest.UrlConstantMvp
import com.android.frame.mvp.AATest.bean.WeatherBeanMvp
import com.android.frame.mvp.AATest.convert.WeatherFunction
import com.android.util.regex.RegexUtil

/**
 * Created by xuzhb on 2020/7/23
 * Desc:
 */
class WeatherActivity : BaseActivity_VB<ActivityWeatherBinding>() {

    override fun handleView(savedInstanceState: Bundle?) {
        getWeatherInfo("深圳")
    }

    override fun initListener() {
        binding.queryBtn.setOnClickListener {
            binding.resultTv.text = ""
            if (TextUtils.isEmpty(binding.areaIl.inputText.trim())) {
                showToast("请先输入要查询的地区")
                return@setOnClickListener
            }
            getWeatherInfo(binding.areaIl.inputText.trim())
        }
    }

    override fun getViewBinding() = ActivityWeatherBinding.inflate(layoutInflater)

    private fun getWeatherInfo(city: String) {
        RetrofitFactory.instance.createService(
            ApiServiceMvp::class.java,
            UrlConstantMvp.WEATHER_URL
        )
            .getWeather(city)
            .map(WeatherFunction())
            .compose(SchedulerUtil.ioToMain())
            .subscribe(object : CustomObserver<BaseResponse<WeatherBeanMvp>>(this, true) {
                override fun onSuccess(response: BaseResponse<WeatherBeanMvp>) {
                    response.data?.let {
                        val list = it.forecast
                        if (list != null && list.size > 0) {
                            val sb = StringBuilder()
                            val today = list.get(0)
                            sb.append(it.city).append("天气\n今天：").append(today.date).append("  ")
                                .append(getWenduRange(today.high, today.low)).append("  ")
                                .append(today.type)
                                .append("\n当前温度：")
                                .append(it.wendu).append("℃\n感冒指数：").append(it.ganmao).append("\n")
                            if (list.size > 1) {
                                sb.append("未来").append(list.size - 1).append("天天气：\n")
                                for (i in list.indices) {
                                    if (i == 0) {
                                        continue
                                    }
                                    val forecast = list.get(i)
                                    sb.append(forecast.date).append("  ")
                                        .append(getWenduRange(forecast.high, forecast.low))
                                        .append("  ").append(forecast.type).append("\n")
                                }
                            }
                            binding.resultTv.text = sb.toString()
                        } else {
                            binding.resultTv.text = "未获取到天气信息"
                        }
                    }
                }
            })
    }

    private fun getWenduRange(high: String, low: String): String =
        "${RegexUtil.extractDigit(low).trim()}℃ 至 ${RegexUtil.extractDigit(high).trim()}℃"

}