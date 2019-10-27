package com.android.frame.http.AATest.bean

import java.io.Serializable

/**
 * Created by xuzhb on 2019/10/27
 * Desc:
 */
data class NewsListBean(
    val path: String,
    val image: String,
    val title: String,
    val passtime: String
) : Serializable