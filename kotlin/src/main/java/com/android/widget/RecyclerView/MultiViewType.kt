package com.android.widget.RecyclerView

/**
 * Created by xuzhb on 2019/7/30
 * Desc:RecyclerView实现多个Item布局
 */
interface MultiViewType<in T> {

    //position:item的位置；totalCount：item的总数
    fun getLayoutId(item: T, position: Int, totalCount: Int): Int

}