package com.android.frame.mvp.AATest.activity.newslist;

import android.os.Bundle;

import com.android.frame.http.AATest.WangYiNewsWebviewActivity;
import com.android.frame.mvp.AATest.adapter.NewsListAdapter;
import com.android.frame.mvp.AATest.bean.NewsListBeanMvp;
import com.android.frame.mvp.BaseListActivity;
import com.android.java.R;
import com.android.widget.RecyclerView.LoadMoreAdapter;

import java.util.ArrayList;

/**
 * Created by xuzhb on 2020/1/8
 * Desc:
 */
public class NewsListActivity extends BaseListActivity<NewsListBeanMvp, NewsListView, NewsListPresenter> implements NewsListView {

    @Override
    public void handleView(Bundle savedInstanceState) {
        mTitleBar.setTitleText("新闻列表");
    }

    @Override
    public void initListener() {
        mAdapter.setOnItemClickListener((data, position) -> {
            WangYiNewsWebviewActivity.start(this, "", ((NewsListBeanMvp) data).getPath());
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_list_layout;
    }

    @Override
    public NewsListPresenter getPresenter() {
        return new NewsListPresenter();
    }

    @Override
    public LoadMoreAdapter<NewsListBeanMvp> getAdapter() {
        return new NewsListAdapter(this, new ArrayList<>());
    }

}
