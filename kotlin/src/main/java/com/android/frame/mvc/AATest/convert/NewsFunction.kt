package com.android.frame.mvc.AATest.convert

import com.android.frame.http.model.BaseListResponse
import com.android.frame.mvc.AATest.entity.NewsListBean
import com.android.util.JsonUtil
import com.android.util.LogUtil
import com.google.gson.Gson
import io.reactivex.functions.Function
import okhttp3.ResponseBody
import java.io.Serializable

/**
 * Created by xuzhb on 2020/1/1
 * Desc:
 */
class NewsFunction : Function<ResponseBody, BaseListResponse<NewsListBean>> {

    companion object {
        private const val TAG = "Function"
    }

    override fun apply(responseBody: ResponseBody): BaseListResponse<NewsListBean> {
        val body = responseBody.string()
        val gson = Gson()
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