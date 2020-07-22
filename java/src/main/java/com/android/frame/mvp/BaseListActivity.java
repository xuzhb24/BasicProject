package com.android.frame.mvp;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.android.widget.RecyclerView.LoadMoreAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuzhb on 2020/7/22
 * Desc:列表数据对应的基类Activity
 */
public abstract class BaseListActivity<T, V extends IBaseListView<T>, P extends BaseListPresenter<T, V>> extends BaseActivity<V, P> implements
        IBaseListView<T>, SwipeRefreshLayout.OnRefreshListener, LoadMoreAdapter.OnLoadMoreListener, LoadMoreAdapter.OnLoadFailListener {

    private int mCurrentPage = 1;  //记录当前页面
    protected LoadMoreAdapter<T> mAdapter;

    @Override
    protected void initBaseView() {
        super.initBaseView();
        initAdapter();
    }

    protected void initAdapter() {
        mAdapter = getAdapter();
        mRecyclerView.setAdapter(mAdapter);
        //上拉加载更多
        mAdapter.setOnLoadMoreListener(mRecyclerView, this);
        //加载异常
        mAdapter.setOnLoadFailListener(this);
        //加载第一页的数据
        mPresenter.loadData(mCurrentPage, true);
    }

    public abstract LoadMoreAdapter<T> getAdapter();

    //下拉刷新
    @Override
    public void onRefresh() {
        dismissLoading();
        mCurrentPage = 1;
        mAdapter.setData(new ArrayList<>());
        mAdapter.notifyDataSetChanged();
        mPresenter.loadData(mCurrentPage);
    }

    //上拉加载更多
    @Override
    public void onLoadMore() {
        mPresenter.loadData(mCurrentPage);
    }

    //加载失败时点击重试
    @Override
    public void onLoadFail(View v) {
        mPresenter.loadData(mCurrentPage);
    }

    //一次加载的个数
    @Override
    public int getLoadSize() {
        return 10;
    }

    //获取当前加载的页码
    @Override
    public int getCurrentPage() {
        return mCurrentPage;
    }

    //展示列表数据
    @Override
    public void showData(int page, List<T> list) {
        if (mCurrentPage == 1) {  //首次加载
            mAdapter.setData(list);
        } else {
            mAdapter.addData(list);
        }
        mCurrentPage++;
        if (list == null || list.size() < getLoadSize()) {
            mAdapter.loadMoreEnd();
        } else {
            mAdapter.loadMoreComplete();
        }
    }

    //数据加载失败
    @Override
    public void loadFail() {
        super.loadFail();
        mAdapter.loadMoreFail();
    }

}
