package com.android.frame.mvp.AATest.activity.newslist;

import android.os.Bundle;
import com.android.frame.mvp.AATest.bean.NewsListBeanMvp;
import com.android.frame.mvp.BaseCompatActivity;

import java.util.ArrayList;

/**
 * Created by xuzhb on 2020/1/8
 * Desc:
 */
public class NewsListActivity extends BaseCompatActivity<NewsListView, NewsListPresenter> implements NewsListView {

    @Override
    public void handleView(Bundle savedInstanceState) {

    }

    @Override
    public void initListener() {

    }

    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    public NewsListPresenter getPresenter() {
        return null;
    }

    @Override
    public void refreshData() {

    }

    @Override
    public void showData(ArrayList<NewsListBeanMvp> list) {

    }

    @Override
    public void loadFail() {

    }

}
