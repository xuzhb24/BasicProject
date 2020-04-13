package com.android.frame.mvp.AATest.activity.newslist;

import android.os.Bundle;

import com.android.frame.http.AATest.WangYiNewsWebviewActivity;
import com.android.frame.mvp.AATest.UrlConstantMvp;
import com.android.frame.mvp.AATest.adapter.NewsListAdapter;
import com.android.frame.mvp.AATest.bean.NewsListBeanMvp;
import com.android.frame.mvp.BaseCompatActivity;
import com.android.java.R;
import com.android.widget.RecyclerView.LoadMoreWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuzhb on 2020/1/8
 * Desc:
 */
public class NewsListActivity extends BaseCompatActivity<NewsListView, NewsListPresenter> implements NewsListView {

    private List<NewsListBeanMvp> mList = new ArrayList<>();
    //实现上拉加载更多
    private LoadMoreWrapper mAdapter;
    private int mCurrentPage = 1;  //记录当前页面

    @Override
    public void handleView(Bundle savedInstanceState) {
        mTitleBar.setTitleText("新闻列表");
        mAdapter = new LoadMoreWrapper(new NewsListAdapter(this, mList));
        mRecyclerView.setAdapter(mAdapter);
        mPresenter.getNews(mCurrentPage, true);
    }

    @Override
    public void initListener() {
        //上拉加载更多
        mAdapter.setOnLoadMoreListener(mRecyclerView, () -> {
            mPresenter.getNews(mCurrentPage);
        });
        //设置加载异常监听，如网络异常导致无法加载数据
        mAdapter.setOnLoadFailListener(v -> {
            mPresenter.getNews(mCurrentPage);
        });
        ((NewsListAdapter) mAdapter.getItemAdapter()).setOnItemClickListener((data, position) -> {
            WangYiNewsWebviewActivity.start(this, "", ((NewsListBeanMvp) data).getPath());
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_list_layout;
    }

    @Override
    public NewsListPresenter getPresenter() {
        return new NewsListPresenter(this);
    }

    @Override
    public void refreshData() {
        mCurrentPage = 1;
        mList.clear();
        mAdapter.notifyDataSetChanged();
        mPresenter.getNews(mCurrentPage);
    }

    @Override
    public void showData(List<NewsListBeanMvp> list) {
        mCurrentPage++;
        mList.addAll(list);
        if (list.size() < Integer.parseInt(UrlConstantMvp.ONCE_LOAD_SIZE)) {
            mAdapter.loadMoreEnd();
        } else {
            mAdapter.loadMoreComplete();
        }
    }

    @Override
    public void loadFail() {
        mAdapter.loadMoreFail();
    }

}
