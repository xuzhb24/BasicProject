package com.android.frame.mvc.viewBinding

import androidx.viewbinding.ViewBinding
import com.android.frame.http.SchedulerUtil
import com.android.frame.http.model.BaseListResponse
import com.android.frame.mvc.viewBinding.extra.CustomObserver
import com.android.widget.RecyclerView.LoadMoreAdapter
import io.reactivex.Observable

/**
 * Created by xuzhb on 2020/7/23
 * Desc:列表数据对应的基类Fragment
 */
abstract class BaseListFragment<T, VB : ViewBinding> : BaseFragment_VB<VB>() {

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
                setOnLoadMoreListener(it) { loadData(mCurrentPage) }
                //加载异常
                setOnLoadFailListener { loadData(mCurrentPage) }
            }
        }
        //加载第一页的数据
        showLoading()
        loadData(mCurrentPage)
    }

    abstract fun getAdapter(): LoadMoreAdapter<T>

    //分页加载
    protected open fun loadData(page: Int) {
        loadDataFromServer(page)?.let {
            it.compose(SchedulerUtil.ioToMain())
                .subscribe(object : CustomObserver<BaseListResponse<T>>(this, false) {
                    override fun onSuccess(response: BaseListResponse<T>) {
                        showData(page, response.data ?: mutableListOf())
                    }
                })
        }
    }

    //从服务器按页加载数据
    protected open fun loadDataFromServer(page: Int): Observable<BaseListResponse<T>>? = null

    override fun refreshData() {
        dismissLoading()
        mCurrentPage = 1
        with(mAdapter) {
            setData(mutableListOf())
            notifyDataSetChanged()
        }
        loadData(mCurrentPage)
    }

    //数据加载失败
    override fun loadFail() {
        super.loadFail()
        mAdapter.loadMoreFail()
    }

    protected open fun getLoadSize(): Int = 10

    protected open fun showData(page: Int, list: MutableList<T>?) {
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

}