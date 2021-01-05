package com.android.frame.mvp

import androidx.viewbinding.ViewBinding
import com.android.frame.mvc.extra.RecyclerView.CustomLoadMoreView
import com.android.util.LogUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * Created by xuzhb on 2020/7/22
 * Desc:列表数据对应的基类Fragment
 */
abstract class BaseListFragment<T, VB : ViewBinding, V : IBaseListView<T>, P : BaseListPresenter<T, V>> :
    BaseFragment<VB, V, P>(), IBaseListView<T> {

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
            mAdapter.loadMoreModule.setOnLoadMoreListener { mPresenter?.loadData(mCurrentPage) }
        }
        mRecyclerView?.adapter = mAdapter
    }

    //获取页面对应的adapter，注意adapter需要实现LoadMoreModule接口才能集成上拉加载更多的逻辑，如果没有实现就会当成普通的adapter进行处理，不能上拉加载更多
    abstract fun getAdapter(): BaseQuickAdapter<T, BaseViewHolder>

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

    //获取当前加载的页码
    override fun getCurrentPage(): Int = mCurrentPage

    //设置当前加载的页码
    override fun setCurrentPage(page: Int) {
        mCurrentPage = page
    }

    //获取加载的起始页码
    override fun getFirstPage(): Int = 0

    //是否是首次加载
    override fun isFirstLoad(): Boolean = mCurrentPage == getFirstPage()

    //获取一页加载的个数
    override fun getLoadSize(): Int = 10

    //展示列表数据
    override fun showData(page: Int, list: MutableList<T>?) {
        val data = mPresenter?.convertData(list)
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

}