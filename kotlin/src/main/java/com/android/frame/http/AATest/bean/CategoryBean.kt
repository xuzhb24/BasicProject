package com.android.frame.http.AATest.bean

import java.io.Serializable

/**
 * desc:分类 Bean
 */
data class CategoryBean(
    val id: Long,
    val name: String,
    val description: String,
    val bgPicture: String,
    val bgColor: String,
    val headerImage: String
) : Serializable
