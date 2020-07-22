package com.android.frame.http.model

import java.io.Serializable

/**
 * Created by xuzhb on 2019/8/8
 * Desc:bean基类，列表类型
 */
class BaseListResponse<T>(
    val code: Int,
    val msg: String,
    val data: MutableList<T>?
) : Serializable {
    fun isSuccess(): Boolean = code == 200
    fun isTokenOut(): Boolean = code == -111  //登录失效，假设code是-111
    fun getMessage(): String = msg
}