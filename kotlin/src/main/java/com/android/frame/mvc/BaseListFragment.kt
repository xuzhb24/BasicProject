package com.android.frame.mvc

import androidx.viewbinding.ViewBinding
import com.android.frame.http.SchedulerUtil
import com.android.frame.http.model.BaseListResponse
import com.android.frame.mvc.extra.RecyclerView.CustomLoadMoreView
import com.android.frame.mvc.extra.http.CustomObserver
import com.android.util.LogUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import io.reactivex.Observable

/**
 * Created by xuzhb on 2020/7/28
 * Desc:列表数据对应的基类Fragment，和CustomObserver配合使用
 */
abstract class BaseListFragment<T, VB : ViewBinding> : BaseFragment<VB>() {

    private var mCurrentPage = getFirstPage()  //记录当前页面
    protected lateinit var mAdapter: BaseQuickAdapter<T, BaseViewHolder>

    override fun initBaseView() {
        super.initBaseView()
        initAdapter()
    }

    protected open fun initAdapter() {
        mAdapter = getAdapter()
        if (mAdapter is LoadMoreModule) {  //上拉加载更多
            mAdapter.loadMoreModule.loadMoreView = CustomLoadMoreView()
            mAdapter.loadMoreModule.setOnLoadMoreListener { loadData(mCurrentPage) }
        }
        mRecyclerView?.adapter = mAdapter
    }

    //获取页面对应的adapter，注意adapter需要实现LoadMoreModule接口才能集成上拉加载更多的逻辑，如果没有实现就会当成普通的adapter进行处理，不能上拉加载更多
    abstract fun getAdapter(): BaseQuickAdapter<T, BaseViewHolder>

    //分页加载
    protected open fun loadData(
        page: Int,
        showLoadLayout: Boolean = (mLoadingLayout != null),  //默认当前布局包含LoadingLayout时才用LoadingLayout来显示加载状态
        showLoadingDialog: Boolean = (mLoadingLayout == null && isFirstLoad())  //默认当前无加载状态布局且第一次加载时才显示加载弹窗
    ) {
        loadDataFromServer(page)?.let {
            it.compose(SchedulerUtil.ioToMain())
                .subscribe(object : CustomObserver<BaseListResponse<T>>(this, showLoadLayout, showLoadingDialog) {
                    override fun onSuccess(response: BaseListResponse<T>) {
                        showData(page, response.data)
                    }
                })
        }
    }

    //从服务器按页加载数据
    protected open fun loadDataFromServer(page: Int): Observable<BaseListResponse<T>>? = null

    override fun refreshData() {
        //加载第一页的数据
        mCurrentPage = getFirstPage()
        loadData(mCurrentPage)
    }

    override fun showLoadingLayout() {
        activity?.runOnUiThread {
            if (mAdapter.data.size > 0) {  //已有数据则不显示加载状态
                mLoadingLayout?.loadComplete()
            } else {
                if (!isRefreshing) {  //下拉刷新不显示加载状态
                    mLoadingLayout?.loadStart()
                }
            }
        }
    }

    override fun loadFinish(isError: Boolean) {
        activity?.runOnUiThread {
            if (!isError) {
                hasDataLoadedSuccess = true
            }
            showNetErrorLayout()
            if (isError) {  //数据加载失败
                if (mAdapter is LoadMoreModule) {
                    mAdapter.loadMoreModule.loadMoreFail()
                }
                if (mAdapter.data.size > 0) {
                    mLoadingLayout?.loadComplete()
                } else {
                    mLoadingLayout?.loadFail()
                }
            } else {
                if (mAdapter.data.size > 0) {
                    mLoadingLayout?.loadComplete()
                } else {
                    mLoadingLayout?.loadEmpty()  //空数据
                }
            }
            //取消加载弹窗
            mLoadingDialog?.dismiss()
            //完成下拉刷新动作
            mSmartRefreshLayout?.let {
                if (isRefreshing) {
                    mSmartRefreshLayout?.finishRefresh(!isError)
                }
                isRefreshing = false
            }
        }
    }

    //加载的起始页
    protected open fun getFirstPage(): Int = 0

    //一页加载的个数
    protected open fun getLoadSize(): Int = 10

    //是否是首次加载
    protected open fun isFirstLoad() = mCurrentPage == getFirstPage()

    //展示列表数据
    protected open fun showData(page: Int, list: MutableList<T>?) {
        val data = convertData(list)
        LogUtil.i("showData", "conver list size：${data?.size ?: -1}")
        if (isFirstLoad()) {  //首次加载
            mAdapter.setNewInstance(data)
        } else {
            mAdapter.addData(data ?: mutableListOf())
        }
        mCurrentPage++
        if (mAdapter is LoadMoreModule) {
            if (list == null || list.size < getLoadSize()) {  //加载到底，没有更多数据
                mAdapter.loadMoreModule.loadMoreEnd()
//                mAdapter.loadMoreModule.isEnableLoadMore = false
            } else {  //完成一页的加载
                mAdapter.loadMoreModule.loadMoreComplete()
//                mAdapter.loadMoreModule.isEnableLoadMore = true
            }
        }
    }

    //可以通过重写这个方法处理返回的列表数据
    protected open fun convertData(response: MutableList<T>?): MutableList<T>? {
        return response
    }

}