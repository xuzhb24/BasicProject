package com.android.frame.mvp;

import java.util.List;

/**
 * Created by xuzhb on 2020/7/22
 * Desc:列表数据对应的基类View
 */
public interface IBaseListView<T> extends IBaseView {

    /**
     * 获取当前加载的页码
     */
    int getCurrentPage();

    /**
     * 设置当前加载的页码
     */
    void setCurrentPage(int page);

    /**
     * 获取加载的起始页码
     */
    int getFirstPage();

    /**
     * 是否是首次加载
     */
    boolean isFirstLoad();

    /**
     * 获取一页加载的个数
     */
    int getLoadSize();

    /**
     * 显示数据
     */
    void showData(int page, List<T> list);

}
