package com.android.frame.mvp

/**
 * Created by xuzhb on 2020/7/22
 * Desc:列表数据对应的基类View
 */
interface IBaseListView<T> : IBaseView {

    /**
     * 获取当前加载的页码
     */
    fun getCurrentPage(): Int

    /**
     * 设置当前加载的页码
     */
    fun setCurrentPage(page: Int)

    /**
     * 获取加载的起始页码
     */
    fun getFirstPage(): Int

    /**
     * 是否是首次加载
     */
    fun isFirstLoad(): Boolean

    /**
     * 获取一页加载的个数
     */
    fun getLoadSize(): Int

    /**
     * 显示数据
     */
    fun showData(page: Int, list: MutableList<T>?)

}