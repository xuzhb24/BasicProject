package com.android.frame.mvp.AATest.activity.newslist;

import com.android.frame.http.model.BaseListResponse;
import com.android.frame.mvp.AATest.bean.NewsListBeanMvp;
import com.android.frame.mvp.BaseListPresenter;

import io.reactivex.Observable;

/**
 * Created by xuzhb on 2020/1/8
 * Desc:
 */
public class NewsListPresenter extends BaseListPresenter<NewsListBeanMvp, NewsListView> {

    private NewsListModel mModel;

    public NewsListPresenter() {
        mModel = new NewsListModel();
    }

    @Override
    public Observable<BaseListResponse<NewsListBeanMvp>> loadDataFromServer(int page) {
        return mModel.getNews(page + "", mView.getLoadSize() + "");
    }
}
