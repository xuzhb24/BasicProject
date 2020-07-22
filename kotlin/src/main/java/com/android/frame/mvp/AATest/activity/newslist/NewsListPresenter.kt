package com.android.frame.mvp.AATest.activity.newslist

import com.android.frame.http.model.BaseListResponse
import com.android.frame.mvp.AATest.bean.NewsListBeanMvp
import com.android.frame.mvp.BaseListPresenter
import io.reactivex.Observable

/**
 * Created by xuzhb on 2020/1/4
 * Desc:
 */
class NewsListPresenter() : BaseListPresenter<NewsListBeanMvp, NewsListView>() {

    private val mModel by lazy { NewsListModel() }

    override fun loadDataFromServer(page: Int): Observable<BaseListResponse<NewsListBeanMvp>>? {
        return mModel.getNews("$page", "${mView?.getLoadSize() ?: 10}")
    }

}