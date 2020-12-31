package com.android.frame.mvc.AATest.entity

import java.io.Serializable

/**
 * Created by xuzhb on 2020/12/30
 * Desc:
 */
data class NewsListBean(
    val path: String,
    val image: String,
    val title: String,
    val passtime: String
) : Serializable