package com.android.frame.mvp.AATest.activity.newslist

import com.android.frame.http.model.BaseListResponse
import com.android.frame.mvp.AATest.UrlConstantMvp
import com.android.frame.mvp.AATest.bean.NewsListBeanMvp
import com.android.frame.mvp.BasePresenter
import com.android.frame.mvp.IBaseView
import com.android.frame.mvp.extra.CustomObserver

/**
 * Created by xuzhb on 2020/1/4
 * Desc:
 */
class NewsListPresenter(private val mView: NewsListView) : BasePresenter<NewsListView>() {

    private val mModel by lazy { NewsListModel() }

    override fun loadData() {
        //下拉刷新
        mView.refreshData()
    }

    fun getNews(
        page: Int,
        count: String = UrlConstantMvp.ONCE_LOAD_SIZE,
        showLoading: Boolean = false
    ) {
        mModel.getNews(page.toString(), count)
            .subscribe(object : CustomObserver<BaseListResponse<NewsListBeanMvp>>(mView, showLoading) {
                override fun onSuccess(response: BaseListResponse<NewsListBeanMvp>) {
                    mView.showData(response.data ?: mutableListOf())
                }

                override fun onFailure(
                    view: IBaseView?,
                    message: String,
                    isError: Boolean,
                    response: BaseListResponse<NewsListBeanMvp>?
                ) {
                    super.onFailure(view, message, isError, response)
                    mView.loadFail()
                }
            })
    }

}