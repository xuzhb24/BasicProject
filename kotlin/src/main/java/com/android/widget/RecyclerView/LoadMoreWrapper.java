package com.android.widget.RecyclerView;

import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.android.basicproject.R;
import com.android.util.SizeUtil;
import com.android.widget.RecyclerView.ViewHolder;

/**
 * Created by xuzhb on 2019/10/29
 * Desc:使用装饰着模式实现上拉加载更多，目前支持LinearLayoutManager和GridLayoutManager
 */
public class LoadMoreWrapper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private RecyclerView.Adapter mAdapter;
    private int mLoadMoreViewTopMargin;  //控制加载更多布局的上边距

    //数据为空时的布局
    private static final int TYPE_EMPTY_VIEW = -1;
    // 普通布局
    private static final int TYPE_NORMAL_VIEW = 1;
    // 脚布局
    private static final int TYPE_FOOTER_VIEW = 2;

    //默认状态
    public static final int STATE_DEFAULT = 0;
    //正在加载
    public static final int STATE_LOADING = 1;
    //加载失败，如网络异常
    public static final int STATE_LOAD_FAIL = 2;
    //已加载全部数据
    public static final int STATE_LOAD_END = 3;

    //当前加载状态
    private int mLoadState = STATE_DEFAULT;

    //空布局时是否也支持上拉加载更多
    private boolean mEmptyViewLoadMoreEnable = false;

    //设置上拉加载状态，同时刷新数据
    public void setLoadState(int loadState) {
        this.mLoadState = loadState;
        notifyDataSetChanged();
    }

    public int getLoadState() {
        return mLoadState;
    }

    public boolean isEmptyViewLoadMoreEnable() {
        return mEmptyViewLoadMoreEnable;
    }

    public void setEmptyViewLoadMoreEnable(boolean mEmptyViewLoadMoreEnable) {
        this.mEmptyViewLoadMoreEnable = mEmptyViewLoadMoreEnable;
    }

    private OnLoadFailListener mOnLoadFailListener;

    //设置加载失败时点击重试
    public void setOnLoadFailListener(OnLoadFailListener onLoadFailListener) {
        this.mOnLoadFailListener = onLoadFailListener;
    }

    public interface OnLoadFailListener {
        void onLoadFail(View view);
    }

    public LoadMoreWrapper(RecyclerView.Adapter adapter) {
        this.mAdapter = adapter;
        setBaseAdapterEmptyViewEnable(this.mAdapter);
    }

    public LoadMoreWrapper(RecyclerView.Adapter adapter, int loadMoreViewTopMargin) {
        this.mAdapter = adapter;
        this.mLoadMoreViewTopMargin = loadMoreViewTopMargin;
        setBaseAdapterEmptyViewEnable(this.mAdapter);
    }

    //继承至BaseAdapter的Adapter默认支持空布局显示，
    //如果需要实现上拉加载更多，则需要取消掉被包装的Adapter默认支持空布局显示的属性
    private void setBaseAdapterEmptyViewEnable(RecyclerView.Adapter adapter) {
        try {
            ((BaseAdapter) adapter).setEmptyViewEnable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public RecyclerView.Adapter getItemAdapter() {
        return mAdapter;
    }

    @Override
    public int getItemCount() {
        if (mAdapter.getItemCount() == 0) {
            if (mEmptyViewLoadMoreEnable) {
                return 2;  //1个是空布局，1个是上拉加载的脚布局
            } else {
                return 1;  //空布局
            }
        }
        return mAdapter.getItemCount() + 1;  //尾部的1表示脚布局
    }

    @Override
    public int getItemViewType(int position) {
        if (mAdapter.getItemCount() == 0) {
            if (mEmptyViewLoadMoreEnable) {
                if (position + 1 == getItemCount()) {
                    return TYPE_FOOTER_VIEW;
                } else {
                    return TYPE_EMPTY_VIEW;
                }
            } else {
                return TYPE_EMPTY_VIEW;
            }
        }
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER_VIEW;
        } else {
            return TYPE_NORMAL_VIEW;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 通过判断显示类型，来创建不同的View
        if (viewType == TYPE_EMPTY_VIEW) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_empty_view, parent, false);
            ImageView emptyIv = view.findViewById(R.id.empty_iv);
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) emptyIv.getLayoutParams();
            params.topMargin = (int) SizeUtil.INSTANCE.sp2px(146f);
            emptyIv.requestLayout();
            return new ViewHolder(view);
        } else if (viewType == TYPE_FOOTER_VIEW) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_refresh_footer, parent, false);
            if (mLoadMoreViewTopMargin != 0) {
                FrameLayout rootFl = view.findViewById(R.id.root_fl);
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) rootFl.getLayoutParams();
                params.topMargin = mLoadMoreViewTopMargin;
                rootFl.requestLayout();
            }
            return new FootViewHolder(view);
        } else {
            return mAdapter.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FootViewHolder) {
            final FootViewHolder footViewHolder = (FootViewHolder) holder;
            switch (mLoadState) {
                case STATE_DEFAULT:  //默认状态
                    footViewHolder.loadingLl.setVisibility(View.GONE);
                    footViewHolder.failFl.setVisibility(View.GONE);
                    footViewHolder.endFl.setVisibility(View.GONE);
                    break;
                case STATE_LOADING:  //加载中
                    footViewHolder.loadingLl.setVisibility(View.VISIBLE);
                    footViewHolder.failFl.setVisibility(View.GONE);
                    footViewHolder.endFl.setVisibility(View.GONE);
                    break;
                case STATE_LOAD_FAIL:  //加载异常
                    footViewHolder.loadingLl.setVisibility(View.GONE);
                    footViewHolder.failFl.setVisibility(View.VISIBLE);
                    footViewHolder.endFl.setVisibility(View.GONE);
                    footViewHolder.failFl.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mOnLoadFailListener.onLoadFail(v);
                        }
                    });
                    break;
                case STATE_LOAD_END:  //加载到底
                    footViewHolder.loadingLl.setVisibility(View.GONE);
                    footViewHolder.failFl.setVisibility(View.GONE);
                    footViewHolder.endFl.setVisibility(View.VISIBLE);
                    if (mEmptyViewLoadMoreEnable && mAdapter.getItemCount() == 0) {
                        //空布局时上拉重新加载，如果显示没有更多数据，则隐藏
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                footViewHolder.endFl.setVisibility(View.GONE);
                            }
                        }, 1000);
                    }
                    break;
                default:
                    break;
            }
        } else {
            mAdapter.onBindViewHolder(holder, position);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    // 如果当前是footer的位置，那么该item占据2个单元格，正常情况下占据1个单元格
                    return getItemViewType(position) == TYPE_FOOTER_VIEW ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }

    private class FootViewHolder extends RecyclerView.ViewHolder {

        private FrameLayout rootFl;
        private LinearLayout loadingLl;
        private FrameLayout failFl;
        private FrameLayout endFl;

        FootViewHolder(View itemView) {
            super(itemView);
            rootFl = (FrameLayout) itemView.findViewById(R.id.root_fl);
            loadingLl = (LinearLayout) itemView.findViewById(R.id.loading_ll);
            failFl = (FrameLayout) itemView.findViewById(R.id.fail_fl);
            endFl = (FrameLayout) itemView.findViewById(R.id.end_fl);
        }
    }

}

