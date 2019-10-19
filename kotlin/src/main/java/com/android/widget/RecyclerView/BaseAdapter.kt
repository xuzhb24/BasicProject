package com.android.widget.RecyclerView

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

/**
 * Created by xuzhb on 2019/8/16
 * Desc:通用的RecyclerView适配器基类
 * @param mDataList:数据列表
 * @param mLayoutId：对应的布局
 * @param mLayoutType：布局的类型
 */
abstract class BaseAdapter<T>(
    var mContext: Context, var mDataList: MutableList<T>, private var mLayoutId: Int
) : RecyclerView.Adapter<ViewHolder>() {

    companion object {
        //增加setEmptyView的支持
        val TYPE_EMPTY_VIEW = -1
    }

    private var mViewType: MultiViewType<T>? = null

    //是否需要设置空布局
    private var mEmptyViewEnable: Boolean = true
    //空布局的ID
    private var mEmptyViewId: Int = -1
//    private var mEmptyViewId: Int = R.layout.layout_empty_view

    //实现多种Item布局，如添加头部Item和底部Item
    constructor(context: Context, data: MutableList<T>, viewType: MultiViewType<T>)
            : this(context, data, -1) {
        this.mViewType = viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (mEmptyViewEnable) {
            if (viewType == TYPE_EMPTY_VIEW) {
                val view = LayoutInflater.from(mContext).inflate(mEmptyViewId, parent, false)
                return ViewHolder(view)
            }
        }
        //实现多种Item布局
        if (mViewType != null) {
            mLayoutId = viewType
        }
        val view = LayoutInflater.from(mContext).inflate(mLayoutId, parent, false)
        return ViewHolder(view)
    }

    //获取布局的类型
    override fun getItemViewType(position: Int): Int {
        if (mEmptyViewEnable) {
            //如果item的数量为0，就显示EmptyView
            if (mDataList.size == 0) {
                return TYPE_EMPTY_VIEW
            }
        }
        return mViewType?.getLayoutId(mDataList[position], position, mDataList.size)
            ?: position  //如果没有使用多布局要返回position防止数据错乱
    }

    override fun getItemCount(): Int {
        if (mEmptyViewEnable) {
            //如果item的数量为0，返回1个布局，表示EmptyView
            if (mDataList.size == 0) {
                return 1
            }
        }
        return mDataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (mDataList.size == 0) {
            return
        }
        //绑定数据
        bindData(holder, mDataList[position], position)
        //设置item点击事件，通过adapter调用
        onItemClickListener?.let {
            holder.itemView.setOnClickListener {
                onItemClickListener?.invoke(mDataList[position], position)
            }
        }
        //设置item长按事件，通过adapter调用
        onItemLongClickListener?.let {
            holder.itemView.setOnLongClickListener {
                onItemLongClickListener!!.invoke(mDataList[position], position)
            }
        }
    }

    //绑定数据，由具体的adapter类实现
    protected abstract fun bindData(holder: ViewHolder, data: T, position: Int)

    //设置新数据
    fun setData(newDataList: MutableList<T>) {
        mDataList = newDataList
        notifyDataSetChanged()
    }

    //添加数据
    fun addData(newDataList: MutableList<T>) {
        mDataList.addAll(newDataList)
//        notifyDataSetChanged() //全局刷新
        if (newDataList.size == 0 || mDataList.size == newDataList.size) {
            notifyDataSetChanged()
        } else {
            notifyItemRangeInserted(mDataList.size - newDataList.size, newDataList.size)
        }
    }

    //增加对setEmptyView的支持
    fun setEmptyViewEnable(enable: Boolean) {
        mEmptyViewEnable = enable
    }

    //通过ID设置空布局
    fun setEmptyViewId(viewId: Int) {
        mEmptyViewId = viewId
    }

    //item点击事件
    private var onItemClickListener: ((obj: Any?, position: Int) -> Unit)? = null
    //item长按事件
    private var onItemLongClickListener: ((obj: Any?, position: Int) -> Boolean)? = null

    //设置Item点击事件，通过Adapter调用
    fun setOnItemClickListener(onItemClickListener: ((obj: Any?, position: Int) -> Unit)) {
        this.onItemClickListener = onItemClickListener
    }

    //设置Item长按事件，通过Adapter调用
    fun setOnItemLongClickListener(onItemLongClickListener: ((obj: Any?, position: Int) -> Boolean)) {
        this.onItemLongClickListener = onItemLongClickListener
    }

}