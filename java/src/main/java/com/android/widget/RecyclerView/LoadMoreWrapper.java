package com.android.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.java.R;

/**
 * Create by xuzhb on 2020/1/20
 * Desc:使用装饰着模式实现上拉加载更多，目前支持LinearLayoutManager和GridLayoutManager
 */
public class LoadMoreWrapper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_EMPTY_VIEW = -1;   //数据为空时的布局
    private static final int TYPE_FOOTER_VIEW = -2;  //脚布局
    //加载状态
    private static final int STATE_DEFAULT = 0;  //默认状态
    private static final int STATE_LOADING = 1;  //加载中
    private static final int STATE_FAIL = 2;     //加载失败，如网络异常
    private static final int STATE_END = 3;      //已加载全部数据

    private RecyclerView.Adapter mItemAdapter;

    public LoadMoreWrapper(RecyclerView.Adapter itemAdapter) {
        this.mItemAdapter = itemAdapter;
        try {
            //继承至BaseAdapter的Adapter默认支持空布局显示，
            //如果需要实现上拉加载更多，则需要取消掉被包装的Adapter默认支持空布局显示的属性
            ((BaseAdapter) mItemAdapter).setEmptyViewEnable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @LayoutRes
    private int mEmptyViewId = R.layout.layout_empty_view;  //通过ID设置空布局
    private boolean isEmptyViewEnable = true;               //是否需要设置空布局，增加对setEmptyView的支持
    private boolean isEmptyViewLoadMoreEnable = false;      //空布局时是否也支持上拉加载更多
    private boolean mShowEndTip = true;                     //是否显示加载到底没有更多数据的提示

    private String mLoadingTip = "正在努力加载...";
    private String mFailTip = "加载失败，请点我重试";
    private String mEndTip = "没有更多数据了";
    private int mLoadState = STATE_DEFAULT;  //当前加载状态

    public RecyclerView.Adapter getItemAdapter() {
        return mItemAdapter;
    }

    public void setEmptyViewId(int emptyViewId) {
        this.mEmptyViewId = emptyViewId;
    }

    public void setEmptyViewEnable(boolean emptyViewEnable) {
        this.isEmptyViewEnable = emptyViewEnable;
    }

    public void setEmptyViewLoadMoreEnable(boolean emptyViewLoadMoreEnable) {
        this.isEmptyViewLoadMoreEnable = emptyViewLoadMoreEnable;
    }

    public void setShowEndTip(boolean showEndTip) {
        this.mShowEndTip = showEndTip;
    }

    public void setLoadingTip(String loadingTip) {
        this.mLoadingTip = loadingTip;
    }

    public void setFailTip(String failTip) {
        this.mFailTip = failTip;
    }

    public void setEndTip(String endTip) {
        this.mEndTip = endTip;
    }

    //设置上拉加载状态，同时刷新数据
    private void setLoadState(int loadState) {
        this.mLoadState = loadState;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (isEmptyViewEnable && mItemAdapter.getItemCount() == 0) {  //无数据
            if (isEmptyViewLoadMoreEnable) {  //设置了空布局时也支持上拉加载更多
                return 2;  //1个是空布局，1个是上拉加载的脚布局
            } else {
                return 1;  //空布局
            }
        }
        return mItemAdapter.getItemCount() + 1;  //尾部的1表示脚布局
    }

    @Override
    public int getItemViewType(int position) {
        if (isEmptyViewEnable && mItemAdapter.getItemCount() == 0) {  //无数据
            if (isEmptyViewLoadMoreEnable) {  //设置了空布局时也支持上拉加载更多
                return (position + 1 == getItemCount()) ? TYPE_FOOTER_VIEW : TYPE_EMPTY_VIEW;
            } else {
                return TYPE_EMPTY_VIEW;
            }
        }
        return (position + 1 == getItemCount()) ? TYPE_FOOTER_VIEW : mItemAdapter.getItemViewType(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_EMPTY_VIEW) {
            View view = LayoutInflater.from(parent.getContext()).inflate(mEmptyViewId, parent, false);
            return new ViewHolder(view);
        }
        if (viewType == TYPE_FOOTER_VIEW) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_refresh_footer, parent, false);
            return new FootViewHolder(view);
        }
        return mItemAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof FootViewHolder) {
            FootViewHolder holder = (FootViewHolder) viewHolder;
            switch (mLoadState) {
                case STATE_DEFAULT:  //默认状态
                    holder.setViewGone(R.id.loading_ll)
                            .setViewGone(R.id.fail_fl)
                            .setViewGone(R.id.end_fl);
                    break;
                case STATE_LOADING:  //加载中
                    holder.setViewVisible(R.id.loading_ll)
                            .setViewGone(R.id.fail_fl)
                            .setViewGone(R.id.end_fl)
                            .setText(R.id.loading_tv, mLoadingTip);
                    break;
                case STATE_FAIL:  //加载异常
                    holder.setViewGone(R.id.loading_ll)
                            .setViewVisible(R.id.fail_fl)
                            .setViewGone(R.id.end_fl)
                            .setText(R.id.fail_tv, mFailTip)
                            .setOnItemChildClickListener(R.id.fail_fl, v -> {
                                if (mOnLoadFailListener != null) {
                                    setLoadState(STATE_LOADING);  //重新加载数据
                                    mOnLoadFailListener.onLoadFail(v);
                                }
                            });
                    break;
                case STATE_END:  //加载到底
                    holder.setViewGone(R.id.loading_ll)
                            .setViewGone(R.id.fail_fl)
                            .setViewVisible(R.id.end_fl);
                    if (mShowEndTip) {
                        holder.setViewVisible(R.id.end_fl)
                                .setText(R.id.end_tv, mEndTip);
                        if (isEmptyViewLoadMoreEnable && mItemAdapter.getItemCount() == 0) {
                            //空布局时上拉重新加载，如果显示没有更多数据，则1秒后隐藏提示
                            new Handler().postDelayed(() -> {
                                holder.setViewGone(R.id.end_fl);
                            }, 1000);
                        }
                    } else {
                        holder.setViewGone(R.id.end_fl);
                    }
                    break;
            }
        } else {
            mItemAdapter.onBindViewHolder(viewHolder, position);
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    //如果当前是footer的位置或者是空布局，那么该item占据2个单元格，正常情况下占据1个单元格
                    //注意RecyclerView要先设置layoutManager再设置adapter
                    return (getItemViewType(position) == TYPE_FOOTER_VIEW ||
                            getItemViewType(position) == TYPE_EMPTY_VIEW
                    ) ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }

    //加载完成
    public void loadMoreComplete() {
        setLoadState(STATE_DEFAULT);
    }

    //加载异常
    public void loadMoreFail() {
        setLoadState(STATE_FAIL);
    }

    //加载到底，没有更多数据了
    public void loadMoreEnd() {
        setLoadState(STATE_END);
    }

    private OnLoadFailListener mOnLoadFailListener;

    public interface OnLoadFailListener {
        void onLoadFail(View v);
    }

    //设置加载失败时点击重试
    public void setOnLoadFailListener(OnLoadFailListener listener) {
        this.mOnLoadFailListener = listener;
    }

    private OnLoadMoreListener mOnLoadMoreListener;

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    //监听上拉加载更多
    public void setOnLoadMoreListener(RecyclerView recyclerView, OnLoadMoreListener listener) {
        this.mOnLoadMoreListener = listener;
        loadMore(recyclerView);
    }

    private void loadMore(RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(new LoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (mItemAdapter.getItemCount() == 0 && (!isEmptyViewLoadMoreEnable || !isEmptyViewEnable)) {
                    return;  //如果无数据且设置了空布局时不能上拉加载更多，则不执行loadMore
                }
                if (mOnLoadMoreListener != null) {
                    if (mLoadState != STATE_LOADING) {
                        setLoadState(STATE_LOADING);  //设置上拉时只加载一次
                        mOnLoadMoreListener.onLoadMore();
                    }
                }
            }
        });
    }

    private static class FootViewHolder extends ViewHolder {

        public FootViewHolder(View itemView) {
            super(itemView);
        }
    }

}
