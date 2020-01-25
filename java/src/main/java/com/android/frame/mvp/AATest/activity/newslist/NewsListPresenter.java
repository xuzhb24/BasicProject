package com.android.frame.mvp.AATest.activity.newslist;

import com.android.frame.http.model.BaseListResponse;
import com.android.frame.mvp.AATest.UrlConstantMvp;
import com.android.frame.mvp.AATest.bean.NewsListBeanMvp;
import com.android.frame.mvp.BasePresenter;
import com.android.frame.mvp.IBaseView;
import com.android.frame.mvp.extra.CustomObserver;

import java.util.ArrayList;

/**
 * Created by xuzhb on 2020/1/8
 * Desc:
 */
public class NewsListPresenter extends BasePresenter<NewsListView> {

    private NewsListView mView;
    private NewsListModel mModel;

    public NewsListPresenter(NewsListView view) {
        this.mView = view;
        mModel = new NewsListModel();
    }

    @Override
    public void loadData() {
        //下拉刷新
        mView.refreshData();
    }

    public void getNews(int page) {
        getNews(page, UrlConstantMvp.ONCE_LOAD_SIZE, false);
    }

    public void getNews(int page, boolean showLoading) {
        getNews(page, UrlConstantMvp.ONCE_LOAD_SIZE, showLoading);
    }

    public void getNews(int page, String count, boolean showLoading) {
        mModel.getNews(String.valueOf(page), count)
                .subscribe(new CustomObserver<BaseListResponse<NewsListBeanMvp>>(mView, showLoading) {
                    @Override
                    public void onSuccess(BaseListResponse<NewsListBeanMvp> response) {
                        mView.showData(response.getData() != null ? response.getData() : new ArrayList<>());
                    }

                    @Override
                    protected void onFailure(IBaseView view, String message, boolean isError, BaseListResponse<NewsListBeanMvp> response) {
                        super.onFailure(view, message, isError, response);
                        mView.loadFail();
                    }
                });
    }

}
