package com.android.frame.mvvm;

import android.view.LayoutInflater;

import androidx.lifecycle.ViewModelProvider;
import androidx.viewbinding.ViewBinding;

import com.android.frame.mvc.extra.RecyclerView.CustomLoadMoreView;
import com.android.util.LogUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuzhb on 2021/8/9
 * Desc:列表数据对应的基类Activity(MVVM)
 */
public abstract class BaseListActivity<T, VB extends ViewBinding, VM extends BaseListViewModel<T, VB>> extends BaseActivity<VB, VM> implements IBaseListView<T> {

    protected int mCurrentPage = getFirstPage();  //记录当前页面
    protected BaseQuickAdapter<T, BaseViewHolder> mAdapter;

    @Override
    protected void initViewBindingAndViewModel() {
        Type superclass = getClass().getGenericSuperclass();
        Class<VB> vbClass = (Class<VB>) ((ParameterizedType) superclass).getActualTypeArguments()[1];
        Class<VM> vmClass = (Class<VM>) ((ParameterizedType) superclass).getActualTypeArguments()[2];
        try {
            Method method = vbClass.getDeclaredMethod("inflate", LayoutInflater.class);
            binding = (VB) method.invoke(null, getLayoutInflater());
            viewModel = new ViewModelProvider(this).get(vmClass);
            viewModel.bind(binding);
            viewModel.observe(this, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initViewModelObserver() {
        super.initViewModelObserver();
        observerListDataChange();
    }

    //监听列表数据变化
    protected void observerListDataChange() {
        //获取到列表数据成功
        viewModel.successData.observe(this, response -> {
            showData(mCurrentPage, response.getData());
        });
        //获取列表数据失败
        viewModel.errorData.observe(this, it -> {
            showToast(it.getMessage());
        });
    }

    @Override
    protected void initBaseView() {
        super.initBaseView();
        initAdapter();
    }

    protected void initAdapter() {
        mAdapter = getAdapter();
        if (mAdapter instanceof LoadMoreModule) {  //上拉加载更多
            mAdapter.getLoadMoreModule().setLoadMoreView(new CustomLoadMoreView());
            mAdapter.getLoadMoreModule().setOnLoadMoreListener(() -> {
                viewModel.loadData(mCurrentPage, getLoadSize(), mLoadingLayout != null, mLoadingLayout == null && isFirstLoad());
            });
        }
        if (mRecyclerView != null) {
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    //获取页面对应的adapter，注意adapter需要实现LoadMoreModule接口才能集成上拉加载更多的逻辑，如果没有实现就会当成普通的adapter进行处理，不能上拉加载更多
    public abstract BaseQuickAdapter<T, BaseViewHolder> getAdapter();

    @Override
    protected void refreshData() {
        //加载第一页的数据
        mCurrentPage = getFirstPage();
        viewModel.loadData(mCurrentPage, getLoadSize(), mLoadingLayout != null, mLoadingLayout == null && isFirstLoad());
    }

    @Override
    public void showLoadingLayout() {
        runOnUiThread(() -> {
            if (mLoadingLayout != null) {
                if (mAdapter.getData().size() > 0) {  //已有数据则不显示加载状态
                    mLoadingLayout.loadComplete();
                } else {
                    if (!isRefreshing) {  //下拉刷新不显示加载状态
                        mLoadingLayout.loadStart();
                    }
                }
            }
        });
    }

    @Override
    public void loadFinish(boolean isError) {
        runOnUiThread(() -> {
            if (!isError) {
                hasDataLoadedSuccess = true;
            }
            showNetErrorLayout();
            if (isError) {  //数据加载失败
                if (mAdapter instanceof LoadMoreModule) {
                    mAdapter.getLoadMoreModule().loadMoreFail();
                }
                if (mLoadingLayout != null) {
                    if (mAdapter.getData().size() > 0) {
                        mLoadingLayout.loadComplete();
                    } else {
                        mLoadingLayout.loadFail();
                    }
                }
            } else {
                if (mLoadingLayout != null) {
                    if (mAdapter.getData().size() > 0) {
                        mLoadingLayout.loadComplete();
                    } else {
                        mLoadingLayout.loadEmpty();  //空数据
                    }
                }
            }
            //取消加载弹窗
            if (mLoadingDialog != null) {
                mLoadingDialog.dismiss();
            }
            //完成下拉刷新动作
            if (mSmartRefreshLayout != null) {
                if (isRefreshing) {
                    mSmartRefreshLayout.finishRefresh(!isError);
                }
                isRefreshing = false;
            }
        });
    }

    //加载的起始页
    @Override
    public int getFirstPage() {
        return 0;
    }

    //一页加载的个数
    @Override
    public int getLoadSize() {
        return 15;
    }

    //是否是首次加载
    @Override
    public boolean isFirstLoad() {
        return mCurrentPage == getFirstPage();
    }

    //展示列表数据
    @Override
    public void showData(int page, List<T> list) {
        List<T> data = convertData(list);
        LogUtil.i("showData", "conver list size：" + (data == null ? -1 : data.size()));
        if (isFirstLoad()) {  //首次加载
            mAdapter.setNewInstance(data);
        } else {
            mAdapter.addData(data != null ? data : new ArrayList<>());
        }
        mCurrentPage++;
        if (mAdapter instanceof LoadMoreModule) {
            if (list == null || list.size() < getLoadSize()) {  //加载到底，没有更多数据
                mAdapter.getLoadMoreModule().loadMoreEnd();
//                mAdapter.getLoadMoreModule().setEnableLoadMore(false);
            } else {  //完成一页的加载
                mAdapter.getLoadMoreModule().loadMoreComplete();
//                mAdapter.getLoadMoreModule().setEnableLoadMore(true);
            }
        }
    }

    //可以通过重写这个方法处理返回的列表数据
    @Override
    public List<T> convertData(List<T> response) {
        return response;
    }

}
