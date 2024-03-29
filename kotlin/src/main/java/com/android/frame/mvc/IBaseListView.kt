package com.android.frame.mvc

/**
 * Created by xuzhb on 2021/8/13
 */
interface IBaseListView<T> : IBaseView {

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

    /**
     * 处理数据
     */
    fun convertData(response: MutableList<T>?): MutableList<T>?

}