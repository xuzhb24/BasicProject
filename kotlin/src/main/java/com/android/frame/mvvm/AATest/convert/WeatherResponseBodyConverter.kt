package com.android.frame.mvvm.AATest.convert

import com.android.frame.http.model.BaseResponse
import com.android.frame.mvc.AATest.entity.WeatherBean
import com.android.util.JsonUtil
import com.android.util.LogUtil
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import okhttp3.ResponseBody
import retrofit2.Converter
import java.io.Serializable

/**
 * Created by xuzhb on 2021/8/6
 * Desc:将天气接口的返回结果转换为BaseResponse结构
 */
class WeatherResponseBodyConverter(val gson: Gson, val adapter: TypeAdapter<BaseResponse<WeatherBean>>) :
    Converter<ResponseBody, BaseResponse<WeatherBean>> {

    companion object {
        private const val TAG = "WeatherResponseBodyConverter"
    }

    override fun convert(value: ResponseBody): BaseResponse<WeatherBean>? {
        val body = value.string()
        value.close()
        val originBean: OriginBean = gson.fromJson(body, OriginBean::class.java)
        LogUtil.logLongTag(TAG, " \n===============转化前===============\n${JsonUtil.formatJson(gson.toJson(originBean))}")
        val response = BaseResponse(
            if (originBean.status == 1000) 200 else originBean.status,
            originBean.desc, originBean.data
        )
        LogUtil.logLongTag(TAG, " \n===============转化后===============\n${JsonUtil.formatJson(gson.toJson(response))}")
        return response
    }

    private data class OriginBean(
        val status: Int,
        val desc: String,
        val data: WeatherBean?
    ) : Serializable
}