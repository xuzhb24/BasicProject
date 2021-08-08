package com.android.frame.mvvm.AATest.convert

import com.android.frame.http.model.BaseListResponse
import com.android.frame.mvc.AATest.entity.NewsListBean
import com.android.util.JsonUtil
import com.android.util.LogUtil
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import okhttp3.ResponseBody
import retrofit2.Converter
import java.io.Serializable

/**
 * Created by xuzhb on 2021/8/6
 * Desc:将新闻接口的返回结果转换为BaseListResponse结构
 */
class NewsResponseBodyConverter(val gson: Gson, val adapter: TypeAdapter<BaseListResponse<NewsListBean>>) :
    Converter<ResponseBody, BaseListResponse<NewsListBean>> {

    companion object {
        private const val TAG = "NewsResponseBodyConverter"
    }

    override fun convert(value: ResponseBody): BaseListResponse<NewsListBean>? {
        val body = value.string()
        value.close()
        val originBean: OriginBean = gson.fromJson(body, OriginBean::class.java)
        LogUtil.logLongTag(TAG, " \n===============转化前===============\n${JsonUtil.formatJson(gson.toJson(originBean))}")
        val response = BaseListResponse(originBean.code, originBean.message, originBean.result)
        LogUtil.logLongTag(TAG, " \n===============转化后===============\n${JsonUtil.formatJson(gson.toJson(response))}")
        return response
    }

    private data class OriginBean(
        val code: Int,
        val message: String,
        val result: MutableList<NewsListBean>?
    ) : Serializable
}