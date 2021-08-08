package com.android.frame.mvvm.AATest.convert

import com.android.frame.http.model.BaseResponse
import com.android.frame.mvc.AATest.entity.WeatherBean
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

/**
 * Created by xuzhb on 2021/8/6
 * Desc:将天气接口的返回结果转换为BaseResponse结构
 */
class WeatherConverterFactory private constructor(val gson: Gson) : Converter.Factory() {

    companion object {
        fun create() = WeatherConverterFactory(Gson())
    }

    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<out Annotation>,
        methodAnnotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<*, RequestBody>? {
        val adapter = gson.getAdapter(TypeToken.get(type))
        return CustomRequestBodyConverter(gson, adapter)
    }

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        val adapter = gson.getAdapter(TypeToken.get(type))
        return WeatherResponseBodyConverter(gson, adapter as TypeAdapter<BaseResponse<WeatherBean>>)
    }

}