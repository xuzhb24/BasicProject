package com.android.widget.RecyclerView

import android.os.Handler
import android.support.annotation.LayoutRes
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.basicproject.R

/**
 * Created by xuzhb on 2019/11/16
 * Desc:使用装饰着模式实现上拉加载更多，目前支持LinearLayoutManager和GridLayoutManager
 */
class LoadMoreWrapper(
    var itemAdapter: RecyclerView.Adapter<ViewHolder>
) : RecyclerView.Adapter<ViewHolder>() {

    companion object {
        private const val TYPE_EMPTY_VIEW = -1   //数据为空时的布局
        private const val TYPE_FOOTER_VIEW = -2  //脚布局
        //加载状态
        private const val STATE_DEFAULT = 0  //默认状态
        private const val STATE_LOADING = 1  //加载中
        private const val STATE_FAIL = 2     //加载失败，如网络异常
        private const val STATE_END = 3      //已加载全部数据
    }

    @LayoutRes
    var emptyViewId: Int = R.layout.layout_empty_view  //通过ID设置空布局
    var isEmptyViewEnable = true           //是否需要设置空布局，增加对setEmptyView的支持
    var isEmptyViewLoadMoreEnable = false  //空布局时是否也支持上拉加载更多
    var showEndTip = true                  //是否显示加载到底没有更多数据的提示

    var loadingTip: String = "正在努力加载..."    //设置加载中的文字提示
    var failTip: String = "加载失败，请点我重试"  //设置加载失败的文字提示
    var endTip: String = "没有更多数据了"         //设置加载到底时的文字提示

    private var mLoadState = STATE_DEFAULT  //当前加载状态
        set(value) {  //设置上拉加载状态，同时刷新数据
            field = value
            notifyDataSetChanged()
        }

    //加载完成
    fun loadMoreComplete() {
        mLoadState = STATE_DEFAULT
    }

    //加载异常
    fun loadMoreFail() {
        mLoadState = STATE_FAIL
    }

    //加载到底，没有更多数据了
    fun loadMoreEnd() {
        mLoadState = STATE_END
    }

    private var mOnLoadFailListener: ((v: View) -> Unit)? = null
    //设置加载失败时点击重试
    fun setOnLoadFailListener(listener: (v: View) -> Unit) {
        this.mOnLoadFailListener = listener
    }

    private var mOnLoadMoreListener: (() -> Unit)? = null
    //监听上拉加载更多
    fun setOnLoadMoreListener(recyclerView: RecyclerView, listener: () -> Unit) {
        this.mOnLoadMoreListener = listener
        loadMore(recyclerView)
    }

    private fun loadMore(recyclerView: RecyclerView) {
        recyclerView.addOnScrollListener(object : LoadMoreListener() {
            override fun onLoadMore() {
                if (mLoadState != STATE_LOADING) {
                    mLoadState = STATE_LOADING  //设置上拉时只加载一次
                    mOnLoadMoreListener?.invoke()
                }
            }
        })
    }

    init {
        try {
            //继承至BaseAdapter的Adapter默认支持空布局显示，
            //如果需要实现上拉加载更多，则需要取消掉被包装的Adapter默认支持空布局显示的属性
            (itemAdapter as BaseAdapter<*>).isEmptyViewEnable = false
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        if (isEmptyViewEnable && itemAdapter.itemCount == 0) {  //无数据
            if (isEmptyViewLoadMoreEnable) {  //设置了空布局时也支持上拉加载更多
                return 2  //1个是空布局，1个是上拉加载的脚布局
            } else {
                return 1  //空布局
            }
        }
        return itemAdapter.itemCount + 1  //尾部的1表示脚布局
    }

    override fun getItemViewType(position: Int): Int {
        if (isEmptyViewEnable && itemAdapter.itemCount == 0) {  //无数据
            if (isEmptyViewLoadMoreEnable) {  //设置了空布局时也支持上拉加载更多
                return if (position + 1 == itemCount) TYPE_FOOTER_VIEW
                else TYPE_EMPTY_VIEW
            } else {
                return TYPE_EMPTY_VIEW
            }
        }
        return if (position + 1 == itemCount) TYPE_FOOTER_VIEW
        else itemAdapter.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            TYPE_EMPTY_VIEW -> {
                val view = LayoutInflater.from(parent.context).inflate(emptyViewId, parent, false)
                ViewHolder(view)
            }
            TYPE_FOOTER_VIEW -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_refresh_footer, parent, false)
                FootViewHolder(view)
            }
            else -> {
                itemAdapter.onCreateViewHolder(parent, viewType)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is FootViewHolder) {
            when (mLoadState) {
                STATE_DEFAULT -> {  //默认状态
                    holder.setViewGone(R.id.loading_ll)
                        .setViewGone(R.id.fail_fl)
                        .setViewGone(R.id.end_fl)
                }
                STATE_LOADING -> {  //加载中
                    holder.setViewVisible(R.id.loading_ll)
                        .setViewGone(R.id.fail_fl)
                        .setViewGone(R.id.end_fl)
                        .setText(R.id.loading_tv, loadingTip)
                }
                STATE_FAIL -> {  //加载异常
                    holder.setViewGone(R.id.loading_ll)
                        .setViewVisible(R.id.fail_fl)
                        .setViewGone(R.id.end_fl)
                        .setText(R.id.fail_tv, failTip)
                        .setOnItemChildClickListener(R.id.fail_fl) { v ->
                            mOnLoadFailListener?.let {
                                mLoadState = STATE_LOADING  //重新加载数据
                                it.invoke(v)
                            }
                        }
                }
                STATE_END -> {  //加载到底
                    holder.setViewGone(R.id.loading_ll)
                        .setViewGone(R.id.fail_fl)
                        .setViewVisible(R.id.end_fl)
                    if (showEndTip) {
                        holder.setViewVisible(R.id.end_fl)
                            .setText(R.id.end_tv, endTip)
                        if (isEmptyViewLoadMoreEnable && itemAdapter.itemCount == 0) {
                            //空布局时上拉重新加载，如果显示没有更多数据，则1秒后隐藏提示
                            Handler().postDelayed({
                                holder.setViewGone(R.id.end_fl)
                            }, 1000)
                        }
                    } else {
                        holder.setViewGone(R.id.end_fl)
                    }
                }
            }
        } else {
            itemAdapter.onBindViewHolder(holder, position)
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        val manager = recyclerView.layoutManager
        if (manager is GridLayoutManager) {
            manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    //如果当前是footer的位置或者是空布局，那么该item占据2个单元格，正常情况下占据1个单元格
                    //注意RecyclerView要先设置layoutManager再设置adapter
                    return if (getItemViewType(position) == TYPE_FOOTER_VIEW ||
                        getItemViewType(position) == TYPE_EMPTY_VIEW
                    ) manager.spanCount else 1
                }

            }
        }
    }

    private class FootViewHolder(view: View) : ViewHolder(view)
}