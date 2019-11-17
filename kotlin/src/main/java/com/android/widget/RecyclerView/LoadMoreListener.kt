package com.android.widget.RecyclerView

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.MotionEvent

/**
 * Created by xuzhb on 2019/10/29
 * Desc:RecyclerView滑动监听
 */
abstract class LoadMoreListener : RecyclerView.OnScrollListener() {

    private var isSlideUpward = false  //是否正在向上滑动
    private var mLastY = 0f

    @SuppressLint("ClickableViewAccessibility")
    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        recyclerView.setOnTouchListener { v, event ->
            val yPos = event.y
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    mLastY = yPos
                }
                MotionEvent.ACTION_MOVE -> {
                    isSlideUpward = mLastY - yPos > 1  //取向上滑动的最小距离
                }
            }
            mLastY = yPos
            false
        }
        //当不滑动时
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            //recyclerView.canScrollVertically(1)：是否能向上滑动，返回false表示不能往上滑动，即代表到底部了
            //recyclerView.canScrollVertically(-1):是否能向下滑动，返回false表示不能往下滑动，即代表到顶部了
            if (!recyclerView.canScrollVertically(1) && isSlideUpward) {  //这里加上mIsSlideUpward是因为当RecyclerView的item没有占满屏幕时，手指往下滑也会触发onLoadMore()
                try {
                    val loadMoreWrapper = recyclerView.adapter as LoadMoreWrapper
                    val isEmptyViewCanLoadMore = loadMoreWrapper.isEmptyViewLoadMoreEnable
                    if (loadMoreWrapper.itemAdapter.itemCount == 0 && !isEmptyViewCanLoadMore) {
                        return  //如果无数据且设置了空布局时不能上拉加载更多，则不执行onLoadMore()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                //加载更多
                onLoadMore()
            }
        }
    }

    //加载更多回调
    abstract fun onLoadMore()

}