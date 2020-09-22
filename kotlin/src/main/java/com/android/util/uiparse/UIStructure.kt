package com.android.util.uiparse

import android.text.TextUtils
import java.io.Serializable

/**
 * Created by xuzhb on 2020/9/22
 */
data class UIStructure(
    val packageName: String,   //应用包名
    val activityName: String,  //Activity类名
    val activityPath: String,  //Activity包路径
    val fragmentList: MutableList<FragmentStructure>
) : Serializable {

    data class FragmentStructure(
        val name: String,
        val visible: Boolean,      //是否可见
        val parrentName: String?   //父Fragment的名称
    ) : Serializable {
        //是否是二级Fragment
        fun isChildFragment() = !TextUtils.isEmpty(parrentName)
    }

    fun getTopFragmentInfo(): String {
        val firstList: MutableList<FragmentStructure> = mutableListOf()
        val secondList: MutableList<FragmentStructure> = mutableListOf()
        for (f in fragmentList) {
            if (f.visible) {
                if (!f.isChildFragment()) {
                    firstList.add(f)
                } else {
                    secondList.add(f)
                }
            }
        }
        println("一级可见：$firstList")
        println("二级可见：$secondList")
        var firstName = ""
        if (firstList.isNotEmpty()) {
            firstName = firstList[0].name  //取第一条，一般一级列表只有一个可见
        }
        if (secondList.isNotEmpty()) {
            for (f in secondList) {
                if (TextUtils.equals(f.parrentName, firstName)) {
                    return f.name
                }
            }
        }
        return firstName
    }

}