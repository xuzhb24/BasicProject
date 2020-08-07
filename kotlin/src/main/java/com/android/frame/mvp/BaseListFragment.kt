package com.android.frame.mvp

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewbinding.ViewBinding
import com.android.widget.RecyclerView.LoadMoreAdapter

/**
 * Created by xuzhb on 2020/7/22
 * Desc:列表数据对应的基类Fragment
 */
abstract class BaseListFragment<VB : ViewBinding, T, V : IBaseListView<T>, P : BaseListPresenter<T, V>> :
    BaseFragment<VB, V, P>(), IBaseListView<T>, SwipeRefreshLayout.OnRefreshListener {

    private var mCurrentPage = 1  //记录当前页面
    protected lateinit var mAdapter: LoadMoreAdapter<T>

    override fun initBaseView() {
        super.initBaseView()
        initAdapter()
    }

    protected fun initAdapter() {
        mAdapter = getAdapter()
        mRecyclerView?.let {
            it.adapter = mAdapter
            with(mAdapter) {
                //上拉加载更多
                setOnLoadMoreListener(it) { mPresenter?.loadData(mCurrentPage) }
                //加载异常
                setOnLoadFailListener { mPresenter?.loadData(mCurrentPage) }
            }
        }
        //加载第一页的数据
        mPresenter?.loadData(mCurrentPage, true)
    }

    abstract fun getAdapter(): LoadMoreAdapter<T>

    override fun onRefresh() {
        dismissLoading()
        mCurrentPage = 1
        with(mAdapter) {
            setData(mutableListOf())
            notifyDataSetChanged()
        }
        mPresenter?.loadData(mCurrentPage)
    }

    override fun getLoadSize(): Int = 10

    override fun getCurrentPage(): Int = mCurrentPage

    override fun showData(page: Int, list: MutableList<T>?) {
        if (mCurrentPage == 1) {  //首次加载
            mAdapter.setData(list)
        } else {
            mAdapter.addData(list)
        }
        mCurrentPage++
        if (list == null || list.size < getLoadSize()) {
            mAdapter.loadMoreEnd()
        } else {
            mAdapter.loadMoreComplete()
        }
    }

    //数据加载失败
    override fun loadFail() {
        super.loadFail()
        mAdapter.loadMoreFail()
    }

}