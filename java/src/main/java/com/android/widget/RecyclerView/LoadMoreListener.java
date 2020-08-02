package com.android.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Create by xuzhb on 2020/1/20
 * Desc:RecyclerView滑动监听
 */
public abstract class LoadMoreListener extends RecyclerView.OnScrollListener {

    private boolean isSlideUpward = false;  //是否正在向上滑动
    private float mLastY = 0;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        recyclerView.setOnTouchListener((v, event) -> {
            float yPos = event.getY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mLastY = yPos;
                    break;
                case MotionEvent.ACTION_MOVE:
                    isSlideUpward = mLastY - yPos > 1;  //取向上滑动的最小距离
                    break;
            }
            mLastY = yPos;
            return false;
        });
        //当不滑动时
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            //recyclerView.canScrollVertically(1)：是否能向上滑动，返回false表示不能往上滑动，即代表到底部了
            //recyclerView.canScrollVertically(-1):是否能向下滑动，返回false表示不能往下滑动，即代表到顶部了
            if (!recyclerView.canScrollVertically(1) && isSlideUpward) {  //这里加上mIsSlideUpward是因为当RecyclerView的item没有占满屏幕时，手指往下滑也会触发onLoadMore()
                //加载更多
                onLoadMore();
            }
        }
    }

    //加载更多回调
    public abstract void onLoadMore();

}
