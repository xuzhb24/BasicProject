package com.android.frame.mvc.viewBinding;

import android.view.View;

import androidx.viewbinding.ViewBinding;

import com.android.frame.http.SchedulerUtil;
import com.android.frame.http.model.BaseListResponse;
import com.android.frame.mvc.viewBinding.extra.CustomObserver;
import com.android.widget.RecyclerView.LoadMoreAdapter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/**
 * Created by xuzhb on 2020/7/23
 * Desc:列表数据对应的基类Fragment
 */
public abstract class BaseListFragment<T, VB extends ViewBinding> extends BaseFragment_VB<VB>
        implements LoadMoreAdapter.OnLoadMoreListener, LoadMoreAdapter.OnLoadFailListener {

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
        showLoading();
        loadData(mCurrentPage);
    }

    public abstract LoadMoreAdapter<T> getAdapter();

    //分页加载
    protected void loadData(int page) {
        if (loadDataFromServer(page) != null) {
            loadDataFromServer(page).compose(SchedulerUtil.ioToMain()).subscribe(new CustomObserver<BaseListResponse<T>>(this, false) {
                @Override
                public void onSuccess(BaseListResponse<T> response) {
                    showData(page, response.getData() != null ? response.getData() : new ArrayList<>());
                }
            });
        }
    }

    //从服务器按页加载数据
    protected abstract Observable<BaseListResponse<T>> loadDataFromServer(int page);

    //下拉刷新
    @Override
    public void refreshData() {
        dismissLoading();
        mCurrentPage = 1;
        mAdapter.setData(new ArrayList<>());
        mAdapter.notifyDataSetChanged();
        loadData(mCurrentPage);
    }

    //数据加载失败
    @Override
    public void loadFail() {
        super.loadFail();
        mAdapter.loadMoreFail();
    }

    //上拉加载更多
    @Override
    public void onLoadMore() {
        loadData(mCurrentPage);
    }

    //加载失败时点击重试
    @Override
    public void onLoadFail(View v) {
        loadData(mCurrentPage);
    }

    //一次加载的个数
    protected int getLoadSize() {
        return 10;
    }

    //展示列表数据
    protected void showData(int page, List<T> list) {
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

}
