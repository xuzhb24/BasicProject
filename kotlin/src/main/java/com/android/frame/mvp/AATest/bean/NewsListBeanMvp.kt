package com.android.frame.mvp.AATest.bean

import java.io.Serializable

/**
 * Created by xuzhb on 2020/1/1
 * Desc:
 */
data class NewsListBeanMvp(
    val path: String,
    val image: String,
    val title: String,
    val passtime: String
) : Serializable