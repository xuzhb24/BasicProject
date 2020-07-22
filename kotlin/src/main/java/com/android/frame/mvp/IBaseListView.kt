package com.android.frame.mvp

/**
 * Created by xuzhb on 2020/7/22
 * Desc:列表数据对应的基类View
 */
interface IBaseListView<T> : IBaseView {

    //一次加载的个数
    fun getLoadSize(): Int

    //获取当前加载的页码
    fun getCurrentPage(): Int

    //显示数据
    fun showData(page: Int, list: MutableList<T>?)

}