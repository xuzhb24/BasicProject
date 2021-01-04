package com.android.frame.mvp.extra.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by xuzhb on 2020/12/31
 * Desc:上拉加载更多
 */
public abstract class LoadMoreAdapter<T> extends BaseQuickAdapter<T, BaseViewHolder> implements LoadMoreModule {

    public LoadMoreAdapter(int layoutResId) {
        super(layoutResId);
    }

    public LoadMoreAdapter(int layoutResId, @Nullable List<T> data) {
        super(layoutResId, data);
    }

}
