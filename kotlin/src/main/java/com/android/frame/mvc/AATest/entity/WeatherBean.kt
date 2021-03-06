package com.android.frame.mvc.AATest.entity

import java.io.Serializable

/**
 * Created by xuzhb on 2020/12/30
 * Desc:
 */
data class WeatherBean(
    val yesterday: YesterdayBean,
    val city: String,
    val forecast: List<ForecastBean>,
    val ganmao: String,
    val wendu: String
) : Serializable {

    data class YesterdayBean(
        val date: String,
        val high: String,
        val fx: String,
        val low: String,
        val fl: String,
        val type: String
    ) : Serializable

    data class ForecastBean(
        val date: String,
        val high: String,
        val fengli: String,
        val low: String,
        val fengxiang: String,
        val type: String
    ) : Serializable

}