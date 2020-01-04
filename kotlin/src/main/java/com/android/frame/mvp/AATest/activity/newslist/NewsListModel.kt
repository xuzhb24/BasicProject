package com.android.frame.mvp.AATest.activity.newslist

import com.android.frame.http.RetrofitFactory
import com.android.frame.http.SchedulerUtil
import com.android.frame.http.model.BaseListResponse
import com.android.frame.mvp.AATest.ApiServiceMvp
import com.android.frame.mvp.AATest.UrlConstantMvp
import com.android.frame.mvp.AATest.bean.NewsListBeanMvp
import com.android.frame.mvp.AATest.convert.NewsFunction
import io.reactivex.Observable

/**
 * Created by xuzhb on 2020/1/4
 * Desc:
 */
class NewsListModel {

    fun getNews(page: String, count: String): Observable<BaseListResponse<NewsListBeanMvp>> {
        return RetrofitFactory.instance.createService(ApiServiceMvp::class.java, UrlConstantMvp.NEWS_URL)
            .getNews(page, count)
            .map(NewsFunction())
            .compose(SchedulerUtil.ioToMain())
    }

}