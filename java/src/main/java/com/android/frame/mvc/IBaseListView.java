package com.android.frame.mvc;

import java.util.List;

/**
 * Created by xuzhb on 2021/8/13
 */
public interface IBaseListView<T> extends IBaseView {

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

    /**
     * 处理数据
     */
    List<T> convertData(List<T> response);

}
