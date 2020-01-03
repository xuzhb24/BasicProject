package com.android.frame.http.AATest.bean

import java.io.Serializable

/**
 * Created by xuzhb on 2019/10/27
 * Desc:
 */
data class WeatherBean(
    val status: Int,
    val desc: String,
    val data: DataBean?
) : Serializable {

    data class DataBean(
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

    fun isSuccess(): Boolean = status == 1000
}