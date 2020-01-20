package com.android.widget.RecyclerView;

/**
 * Create by xuzhb on 2020/1/20
 * Desc:RecyclerView实现多种Item布局
 */
public interface MultiViewType<T> {

    /**
     * 实现多种item布局
     *
     * @param data       Item对应的数据
     * @param position   Item的位置
     * @param totalCount Item的总数
     * @return Item对应的布局类型，使用布局的id进行区分
     */
    int getLayoutId(T data, int position, int totalCount);

}
