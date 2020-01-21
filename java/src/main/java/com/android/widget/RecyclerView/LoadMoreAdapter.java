package com.android.widget.RecyclerView;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.java.R;

import java.util.List;

/**
 * Create by xuzhb on 2020/1/20
 * Desc:上拉加载更多
 */
public abstract class LoadMoreAdapter<T> extends RecyclerView.Adapter<ViewHolder> {

    private static final int TYPE_EMPTY_VIEW = -1;   //数据为空时的布局
    private static final int TYPE_FOOTER_VIEW = -2;  //脚布局
    //加载状态
    private static final int STATE_DEFAULT = 0;  //默认状态
    private static final int STATE_LOADING = 1;  //加载中
    private static final int STATE_FAIL = 2;     //加载失败，如网络异常
    private static final int STATE_END = 3;      //已加载全部数据

    private Context mContext;
    private List<T> mDataList;      //数据列表
    private int mLayoutId;               //对应的布局
    private MultiViewType<T> mViewType;  //布局的类型

    //单布局的构造函数
    public LoadMoreAdapter(Context context, List<T> dataList, int layoutId) {
        this.mContext = context;
        this.mDataList = dataList;
        this.mLayoutId = layoutId;
        this.mViewType = null;
    }

    //实现多种Item布局的构造函数，如添加头部Item和底部Item
    public LoadMoreAdapter(Context context, List<T> dataList, MultiViewType<T> viewType) {
        this.mContext = context;
        this.mDataList = dataList;
        this.mLayoutId = -1;
        this.mViewType = viewType;
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
        if (isEmptyViewEnable && mDataList.size() == 0) {  //无数据
            if (isEmptyViewLoadMoreEnable) {  //设置了空布局时也支持上拉加载更多
                return 2;  //1个是空布局，1个是上拉加载的脚布局
            } else {
                return 1;  //空布局
            }
        }
        return mDataList.size() + 1;  //尾部的1表示脚布局
    }

    @Override
    public int getItemViewType(int position) {
        if (isEmptyViewEnable && mDataList.size() == 0) {  //无数据
            if (isEmptyViewLoadMoreEnable) {  //设置了空布局时也支持上拉加载更多
                return (position + 1 == getItemCount()) ? TYPE_FOOTER_VIEW : TYPE_EMPTY_VIEW;
            } else {
                return TYPE_EMPTY_VIEW;
            }
        }
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER_VIEW;
        }
        if (mViewType != null) {
            return mViewType.getLayoutId(mDataList.get(position), position, mDataList.size());
        }
        return position;  //如果没有使用多布局要返回position防止数据错乱
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_EMPTY_VIEW) {
            View view = LayoutInflater.from(parent.getContext()).inflate(mEmptyViewId, parent, false);
            return new ViewHolder(view);
        }
        if (viewType == TYPE_FOOTER_VIEW) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_refresh_footer, parent, false);
            return new FootViewHolder(view);
        }
        //实现多种Item布局
        if (mViewType != null) {
            mLayoutId = viewType;
        }
        View view = LayoutInflater.from(mContext).inflate(mLayoutId, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (holder instanceof FootViewHolder) {
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
                        if (isEmptyViewLoadMoreEnable && mDataList.size() == 0) {
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
            if (mDataList.size() == 0) {
                return;
            }
            //绑定数据
            bindData(holder, mDataList.get(position), position);
            //设置Item点击事件，通过adapter调用
            if (mOnItemClickListener != null) {
                holder.itemView.setOnClickListener(v -> mOnItemClickListener.onClick(mDataList.get(position), position));
            }
            //设置Item长按事件，通过adapter调用
            if (mOnItemLongClickListener != null) {
                holder.itemView.setOnLongClickListener(v -> mOnItemLongClickListener.onLongClick(mDataList.get(position), position));
            }
        }
    }

    //绑定数据，由具体的adapter类实现
    protected abstract void bindData(ViewHolder holder, T data, int position);

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
                if (mDataList.size() == 0 && (!isEmptyViewLoadMoreEnable || !isEmptyViewEnable)) {
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

    //设置新数据
    public void setData(List<T> dataList) {
        mDataList = dataList;
    }

    //添加数据
    public void addData(List<T> dataList) {
        mDataList.addAll(dataList);
    }

    private OnItemClickListener mOnItemClickListener;

    //Item点击事件
    public interface OnItemClickListener {
        void onClick(Object data, int position);
    }

    //设置Item点击事件，通过Adapter调用
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    private OnItemLongClickListener mOnItemLongClickListener;

    //Item长按事件
    public interface OnItemLongClickListener {
        boolean onLongClick(Object data, int position);
    }

    //设置Item长按事件，通过Adapter调用
    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.mOnItemLongClickListener = listener;
    }

    private static class FootViewHolder extends ViewHolder {

        public FootViewHolder(View itemView) {
            super(itemView);
        }
    }

}
