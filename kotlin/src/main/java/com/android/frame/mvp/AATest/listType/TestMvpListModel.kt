package com.android.frame.mvp.AATest.listType

import com.android.frame.http.RetrofitFactory
import com.android.frame.http.SchedulerUtil
import com.android.frame.http.model.BaseListResponse
import com.android.frame.mvc.AATest.convert.NewsFunction
import com.android.frame.mvc.AATest.entity.NewsListBean
import com.android.frame.mvc.AATest.server.ApiService
import com.android.frame.mvc.AATest.server.Config
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

/**
 * Created by xuzhb on 2021/1/5
 * Desc:
 */
class TestMvpListModel {

    fun getWangYiNewsByField(page: Int, count: Int): Observable<BaseListResponse<NewsListBean>> {
        return RetrofitFactory.instance.createService(ApiService::class.java, Config.NEWS_URL)
            .getWangYiNewsByField(page, count)
            .delay(1, TimeUnit.SECONDS)  //模拟延迟一段时间后请求到数据的情况
            .map(NewsFunction())
            .compose(SchedulerUtil.ioToMain())
    }

}