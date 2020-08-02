package com.android.widget.RecyclerView

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.android.basicproject.R

/**
 * Created by xuzhb on 2019/8/16
 * Desc:通用的RecyclerView适配器基类
 * @param mDataList 数据列表
 * @param mLayoutId 对应的布局
 * @param mViewType 布局的类型
 */
abstract class BaseAdapter<T>(
    var mContext: Context, var mDataList: MutableList<T>, private var mLayoutId: Int
) : RecyclerView.Adapter<ViewHolder>() {

    companion object {
        private const val TYPE_EMPTY_VIEW = -1  //增加setEmptyView的支持
    }

    private var mViewType: MultiViewType<T>? = null  //布局类型

    //实现多种Item布局，如添加头部Item和底部Item
    constructor(context: Context, data: MutableList<T>, viewType: MultiViewType<T>)
            : this(context, data, -1) {
        this.mViewType = viewType
    }

    @LayoutRes
    var emptyViewId: Int = R.layout.layout_empty_view  //通过ID设置空布局
    var isEmptyViewEnable: Boolean = true  //是否需要设置空布局，增加对setEmptyView的支持

    override fun getItemCount(): Int {
        if (isEmptyViewEnable && mDataList.size == 0) {  //如果Item的数量为0，返回1个布局，表示EmptyView
            return 1
        }
        return mDataList.size
    }

    //获取布局的类型
    override fun getItemViewType(position: Int): Int {
        if (isEmptyViewEnable && mDataList.size == 0) {  //如果Item的数量为0，就显示EmptyView
            return TYPE_EMPTY_VIEW
        }
        return mViewType?.getLayoutId(mDataList[position], position, mDataList.size)
            ?: position  //如果没有使用多布局要返回position防止数据错乱
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (viewType == TYPE_EMPTY_VIEW) {  //空布局
            val view = LayoutInflater.from(mContext).inflate(emptyViewId, parent, false)
            return ViewHolder(view)
        }
        //实现多种Item布局
        if (mViewType != null) {
            mLayoutId = viewType
        }
        val view = LayoutInflater.from(mContext).inflate(mLayoutId, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (mDataList.size == 0) {
            return
        }
        //绑定数据
        bindData(holder, mDataList[position], position)
        //设置Item点击事件，通过adapter调用
        onItemClickListener?.let {
            holder.itemView.setOnClickListener {
                onItemClickListener!!.invoke(mDataList[position], position)
            }
        }
        //设置Item长按事件，通过adapter调用
        onItemLongClickListener?.let {
            holder.itemView.setOnLongClickListener {
                onItemLongClickListener!!.invoke(mDataList[position], position)
            }
        }
    }

    //绑定数据，由具体的adapter类实现
    protected abstract fun bindData(holder: ViewHolder, data: T, position: Int)

    //设置新数据
    fun setData(dataList: MutableList<T>?) {
        mDataList = dataList ?: ArrayList()
        notifyDataSetChanged()
    }

    //添加数据
    fun addData(dataList: MutableList<T>?) {
        dataList?.let {
            mDataList.addAll(it)
//            notifyDataSetChanged()  //全局刷新
            if (it.size == 0 || mDataList.size == it.size) {
                notifyDataSetChanged()
            } else {
                notifyItemRangeInserted(mDataList.size - it.size, it.size)
            }
        }
    }

    //Item点击事件
    private var onItemClickListener: ((obj: Any?, position: Int) -> Unit)? = null

    //设置Item点击事件，通过Adapter调用
    fun setOnItemClickListener(onItemClickListener: ((obj: Any?, position: Int) -> Unit)) {
        this.onItemClickListener = onItemClickListener
    }

    //Item长按事件
    private var onItemLongClickListener: ((obj: Any?, position: Int) -> Boolean)? = null

    //设置Item长按事件，通过Adapter调用
    fun setOnItemLongClickListener(onItemLongClickListener: ((obj: Any?, position: Int) -> Boolean)) {
        this.onItemLongClickListener = onItemLongClickListener
    }

}