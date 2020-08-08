package com.android.frame.mvc.viewBinding.AATest;

import android.os.Bundle;

import com.android.frame.http.RetrofitFactory;
import com.android.frame.http.model.BaseListResponse;
import com.android.frame.mvc.viewBinding.BaseListActivity;
import com.android.frame.mvp.AATest.ApiServiceMvp;
import com.android.frame.mvp.AATest.UrlConstantMvp;
import com.android.frame.mvp.AATest.adapter.NewsListAdapter;
import com.android.frame.mvp.AATest.bean.NewsListBeanMvp;
import com.android.frame.mvp.AATest.convert.NewsFunction;
import com.android.java.databinding.ActivityListLayoutBinding;
import com.android.widget.RecyclerView.LoadMoreAdapter;

import java.util.ArrayList;

import io.reactivex.Observable;

/**
 * Created by xuzhb on 2020/7/23
 * Desc:
 */
public class NewsListActivity extends BaseListActivity<NewsListBeanMvp, ActivityListLayoutBinding> {

    @Override
    public LoadMoreAdapter<NewsListBeanMvp> getAdapter() {
        return new NewsListAdapter(this, new ArrayList<>());
    }

    @Override
    public Observable<BaseListResponse<NewsListBeanMvp>> loadDataFromServer(int page) {
        //缓存接口请求
        return RetrofitFactory.getInstance().createService(ApiServiceMvp.class, UrlConstantMvp.NEWS_URL, true)
                .getNews(page + "", getLoadSize() + "")
                .map(new NewsFunction());
    }

    @Override
    public void handleView(Bundle savedInstanceState) {
        mTitleBar.setTitleText("新闻列表");
    }

    @Override
    public void initListener() {

    }

    @Override
    public ActivityListLayoutBinding getViewBinding() {
        return ActivityListLayoutBinding.inflate(getLayoutInflater());
    }
}
