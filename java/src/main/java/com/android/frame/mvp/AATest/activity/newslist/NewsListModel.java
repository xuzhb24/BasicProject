package com.android.frame.mvp.AATest.activity.newslist;

import com.android.frame.http.RetrofitFactory;
import com.android.frame.http.SchedulerUtil;
import com.android.frame.http.model.BaseListResponse;
import com.android.frame.mvp.AATest.ApiServiceMvp;
import com.android.frame.mvp.AATest.UrlConstantMvp;
import com.android.frame.mvp.AATest.bean.NewsListBeanMvp;
import com.android.frame.mvp.AATest.convert.NewsFunction;
import io.reactivex.Observable;

/**
 * Created by xuzhb on 2020/1/8
 * Desc:
 */
public class NewsListModel {

    public Observable<BaseListResponse<NewsListBeanMvp>> getNews(String page, String count) {
        return RetrofitFactory.getInstance().createService(ApiServiceMvp.class, UrlConstantMvp.NEWS_URL)
                .getNews(page, count)
                .map(new NewsFunction())
                .compose(SchedulerUtil.ioToMain());
    }

}
