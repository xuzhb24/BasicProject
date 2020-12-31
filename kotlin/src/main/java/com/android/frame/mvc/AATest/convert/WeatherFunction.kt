package com.android.frame.mvc.AATest.convert

import com.android.frame.http.model.BaseResponse
import com.android.frame.mvc.AATest.entity.WeatherBean
import com.android.util.JsonUtil
import com.android.util.LogUtil
import com.google.gson.Gson
import io.reactivex.functions.Function
import okhttp3.ResponseBody
import java.io.Serializable

/**
 * Created by xuzhb on 2020/1/4
 * Desc:
 */
class WeatherFunction : Function<ResponseBody, BaseResponse<WeatherBean>> {

    companion object {
        private const val TAG = "Function"
    }

    override fun apply(responseBody: ResponseBody): BaseResponse<WeatherBean> {
        val body = responseBody.string()
        val gson = Gson()
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