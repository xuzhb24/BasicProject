package com.android.frame.mvp.AATest.activity.newslist

import com.android.frame.mvp.AATest.bean.NewsListBeanMvp
import com.android.frame.mvp.IBaseView

/**
 * Created by xuzhb on 2020/1/4
 * Desc:
 */
interface NewsListView : IBaseView {

    //下拉刷新
    fun refreshData()

    //展示新闻列表
    fun showData(list: MutableList<NewsListBeanMvp>)

    //加载数据出现异常
    fun loadFail()

}