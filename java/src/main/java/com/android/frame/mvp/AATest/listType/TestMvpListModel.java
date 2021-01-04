package com.android.frame.mvp.AATest.listType;

import com.android.frame.http.RetrofitFactory;
import com.android.frame.http.SchedulerUtil;
import com.android.frame.http.model.BaseListResponse;
import com.android.frame.mvc.AATest.convert.NewsFunction;
import com.android.frame.mvc.AATest.entity.NewsListBean;
import com.android.frame.mvc.AATest.server.ApiService;
import com.android.frame.mvc.AATest.server.Config;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

/**
 * Created by xuzhb on 2021/1/4
 * Desc:
 */
public class TestMvpListModel {

    public Observable<BaseListResponse<NewsListBean>> getWangYiNewsByField(int page, int count) {
        return RetrofitFactory.getInstance().createService(ApiService.class, Config.NEWS_URL)
                .getWangYiNewsByField(page, count)
                .delay(1, TimeUnit.SECONDS)  //模拟延迟一段时间后请求到数据的情况
                .map(new NewsFunction())
                .compose(SchedulerUtil.ioToMain());
    }

}
