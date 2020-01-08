package com.android.frame.mvp.AATest.activity.newslist;

import com.android.frame.mvp.AATest.bean.NewsListBeanMvp;
import com.android.frame.mvp.IBaseView;

import java.util.ArrayList;

/**
 * Created by xuzhb on 2020/1/8
 * Desc:
 */
public interface NewsListView extends IBaseView {

    //下拉刷新
    void refreshData();

    //展示新闻列表
    void showData(ArrayList<NewsListBeanMvp> list);

    //加载数据出现异常
    void loadFail();

}
