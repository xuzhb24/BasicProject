package com.android.frame.mvvm

import android.view.LayoutInflater
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.android.frame.mvc.extra.RecyclerView.CustomLoadMoreView
import com.android.util.LogUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import java.lang.reflect.ParameterizedType

/**
 * Created by xuzhb on 2021/8/7
 * Desc:列表数据对应的基类Fragment(MVVM)
 */
abstract class BaseListFragment<T, VB : ViewBinding, VM : BaseListViewModel<T, VB>> : BaseFragment<VB, VM>(), IBaseListView<T> {

    protected var mCurrentPage = getFirstPage()  //记录当前页面
    protected lateinit var mAdapter: BaseQuickAdapter<T, BaseViewHolder>

    override fun initViewBindingAndViewModel() {
        val superclass = javaClass.genericSuperclass
        val vbClass = (superclass as ParameterizedType).actualTypeArguments[1] as Class<VB>
        val vmClass = (superclass as ParameterizedType).actualTypeArguments[2] as Class<VM>
        val method = vbClass.getDeclaredMethod("inflate", LayoutInflater::class.java)
        binding = method.invoke(null, layoutInflater) as VB
        viewModel = ViewModelProvider(this).get(vmClass)
        viewModel.bind(binding)
        viewModel.observe(this, this)
    }

    override fun initViewModelObserver() {
        super.initViewModelObserver()
        observerListDataChange()
    }

    //监听列表数据变化
    protected open fun observerListDataChange() {
        //获取到列表数据成功
        viewModel.successData.observe(this, Observer {
            showData(mCurrentPage, it.data)  //展示列表数据
        })
        //获取列表数据失败
        viewModel.errorData.observe(this, Observer {
            showToast(it.message)  //提示错误信息
        })
    }

    override fun initBaseView() {
        super.initBaseView()
        initAdapter()
    }

    protected open fun initAdapter() {
        mAdapter = getAdapter()
        if (mAdapter is LoadMoreModule) {  //上拉加载更多
            mAdapter.loadMoreModule.loadMoreView = CustomLoadMoreView()
            mAdapter.loadMoreModule.setOnLoadMoreListener {
                viewModel.loadData(mCurrentPage, getLoadSize(), mLoadingLayout != null, mLoadingLayout == null && isFirstLoad())
            }
        }
        mRecyclerView?.adapter = mAdapter
    }

    //获取页面对应的adapter，注意adapter需要实现LoadMoreModule接口才能集成上拉加载更多的逻辑，如果没有实现就会当成普通的adapter进行处理，不能上拉加载更多
    abstract fun getAdapter(): BaseQuickAdapter<T, BaseViewHolder>

    override fun refreshData() {
        //加载第一页的数据
        mCurrentPage = getFirstPage()
        viewModel.loadData(mCurrentPage, getLoadSize(), mLoadingLayout != null, mLoadingLayout == null && isFirstLoad())
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
    override fun getFirstPage(): Int = 0

    //一页加载的个数
    override fun getLoadSize(): Int = 15

    //是否是首次加载
    override fun isFirstLoad() = mCurrentPage == getFirstPage()

    //展示列表数据
    override fun showData(page: Int, list: MutableList<T>?) {
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
    override fun convertData(response: MutableList<T>?): MutableList<T>? {
        return response
    }

}