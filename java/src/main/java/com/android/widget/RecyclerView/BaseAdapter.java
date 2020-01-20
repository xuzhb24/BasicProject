package com.android.widget.RecyclerView;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.java.R;

import java.util.ArrayList;

/**
 * Create by xuzhb on 2020/1/20
 * Desc:通用的RecyclerView适配器基类
 */
public abstract class BaseAdapter<T> extends RecyclerView.Adapter<ViewHolder> {

    private static final int TYPE_EMPTY_VIEW = -1;  //增加setEmptyView的支持

    private Context mContext;
    private ArrayList<T> mDataList;      //数据列表
    private int mLayoutId;               //对应的布局
    private MultiViewType<T> mViewType;  //布局的类型

    //单布局的构造函数
    public BaseAdapter(Context context, ArrayList<T> dataList, int layoutId) {
        this.mContext = context;
        this.mDataList = dataList;
        this.mLayoutId = layoutId;
        this.mViewType = null;
    }

    //实现多种Item布局的构造函数，如添加头部Item和底部Item
    public BaseAdapter(Context context, ArrayList<T> dataList, MultiViewType<T> viewType) {
        this.mContext = context;
        this.mDataList = dataList;
        this.mLayoutId = -1;
        this.mViewType = viewType;
    }

    @LayoutRes
    private int mEmptyViewId = R.layout.layout_empty_view;  //通过ID设置空布局
    private boolean isEmptyViewEnable = true;  //是否需要设置空布局，增加对setEmptyView的支持

    public void setEmptyViewId(int emptyViewId) {
        this.mEmptyViewId = emptyViewId;
    }

    public void setEmptyViewEnable(boolean emptyViewEnable) {
        this.isEmptyViewEnable = emptyViewEnable;
    }

    @Override
    public int getItemCount() {
        if (isEmptyViewEnable && mDataList.size() == 0) {  //如果Item的数量为0，返回1个布局，表示EmptyView
            return 1;
        }
        return mDataList.size();
    }

    //获取布局的类型
    @Override
    public int getItemViewType(int position) {
        if (isEmptyViewEnable && mDataList.size() == 0) {  //如果Item的数量为0，就显示EmptyView
            return TYPE_EMPTY_VIEW;
        }
        if (mViewType != null) {
            return mViewType.getLayoutId(mDataList.get(position), position, mDataList.size());
        }
        return position;  //如果没有使用多布局要返回position防止数据错乱
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_EMPTY_VIEW) {  //空布局
            View view = LayoutInflater.from(mContext).inflate(mEmptyViewId, parent, false);
            return new ViewHolder(view);
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

    //绑定数据，由具体的adapter类实现
    protected abstract void bindData(ViewHolder holder, T data, int position);

    //设置新数据
    public void setData(ArrayList<T> dataList) {
        mDataList = dataList;
        notifyDataSetChanged();
    }

    //添加数据
    public void addData(ArrayList<T> dataList) {
        mDataList.addAll(dataList);
//        notifyDataSetChanged();  //全局刷新
        if (dataList.size() == 0 || mDataList.size() == dataList.size()) {
            notifyDataSetChanged();
        } else {
            notifyItemRangeInserted(mDataList.size() - dataList.size(), dataList.size());
        }
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

}
