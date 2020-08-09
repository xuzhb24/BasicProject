package com.android.frame.mvc.viewBinding.AATest

import android.os.Bundle
import com.android.basicproject.databinding.ActivityListLayoutBinding
import com.android.frame.http.RetrofitFactory
import com.android.frame.http.model.BaseListResponse
import com.android.frame.mvc.viewBinding.BaseListActivity
import com.android.frame.mvp.AATest.ApiServiceMvp
import com.android.frame.mvp.AATest.UrlConstantMvp
import com.android.frame.mvp.AATest.adapter.NewsListAdapter
import com.android.frame.mvp.AATest.bean.NewsListBeanMvp
import com.android.frame.mvp.AATest.convert.NewsFunction
import com.android.widget.RecyclerView.LoadMoreAdapter
import io.reactivex.Observable

/**
 * Created by xuzhb on 2020/7/23
 * Desc:
 */
class NewsListActivity : BaseListActivity<NewsListBeanMvp, ActivityListLayoutBinding>() {

    override fun getAdapter(): LoadMoreAdapter<NewsListBeanMvp> =
        NewsListAdapter(this, mutableListOf())

    override fun handleView(savedInstanceState: Bundle?) {
        mTitleBar?.titleText = "新闻列表"
    }

    override fun initListener() {

    }

    override fun getViewBinding() = ActivityListLayoutBinding.inflate(layoutInflater)

    override fun loadDataFromServer(page: Int): Observable<BaseListResponse<NewsListBeanMvp>>? {
        //缓存接口请求
        return RetrofitFactory.instance.createService(ApiServiceMvp::class.java, UrlConstantMvp.NEWS_URL, cache = true)
            .getNews("$page", "${getLoadSize()}")
            .map(NewsFunction())
    }

}