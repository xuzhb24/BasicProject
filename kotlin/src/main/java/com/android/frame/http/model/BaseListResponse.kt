package com.android.frame.http.model

/**
 * Created by xuzhb on 2019/8/8
 * Desc:bean基类，列表类型
 */
class BaseListResponse<T>(
    val code: Int,
    val msg: String,
    val data: ArrayList<T>?,
    val sign: String
) {
    fun isSuccess(): Boolean = code == 200
}