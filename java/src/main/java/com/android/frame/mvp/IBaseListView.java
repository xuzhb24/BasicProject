package com.android.frame.mvp;

import java.util.List;

/**
 * Created by xuzhb on 2020/7/22
 * Desc:列表数据对应的基类View
 */
public interface IBaseListView<T> extends IBaseView {

    //一次加载的个数
    int getLoadSize();

    //获取当前加载的页码
    int getCurrentPage();

    //显示数据
    void showData(int page, List<T> list);

}
