package com.android.frame.http

import org.json.JSONException
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.ParseException

/**
 * Created by xuzhb on 2019/8/8
 * Desc:异常处理类
 */
object ExceptionUtil {

    fun convertExceptopn(t: Throwable): String =
        when (t) {
            is UnknownHostException -> "网络不可用"
            is SocketTimeoutException -> "请求网络超时"
            is HttpException -> convertStatusCode(t)
            is ParseException, is JSONException -> "数据解析错误"
            else -> "未知错误，${t.message}"
        }


    private fun convertStatusCode(e: HttpException): String =
        when (e.code()) {
            400 -> "错误请求[${e.code()}]"
            401 -> "未授权[${e.code()}]"
            403 -> "服务器拒绝请求[${e.code()}]"
            404 -> "请求的资源不存在[${e.code()}]"
            408 -> "请求超时[${e.code()}]"
            500 -> "服务器内部错误[${e.code()}]"
            502 -> "错误网关[${e.code()}]"
            503 -> "服务器暂不可用[${e.code()}]"
            504 -> "网关超时[${e.code()}]"
            in 500..600 -> "服务器处理请求出错[${e.code()}]"
            in 400..499 -> "服务器无法处理请求[${e.code()}]"
            in 300..399 -> "请求被重定向到其他页面[${e.code()}]"
            else -> "未知错误[${e.code()}]"
        }

}