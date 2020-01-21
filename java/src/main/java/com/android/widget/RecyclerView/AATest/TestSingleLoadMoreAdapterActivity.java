package com.android.widget.RecyclerView.AATest;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import butterknife.BindView;
import com.android.frame.http.AATest.ApiService;
import com.android.frame.http.AATest.UrlConstant;
import com.android.frame.http.AATest.WangYiNewsWebviewActivity;
import com.android.frame.http.AATest.bean.NewsListBean;
import com.android.frame.http.ExceptionUtil;
import com.android.frame.http.RetrofitFactory;
import com.android.frame.http.SchedulerUtil;
import com.android.frame.mvc.BaseActivity;
import com.android.java.R;
import com.android.widget.TitleBar;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by xuzhb on 2020/1/21
 * Desc:单布局时上拉加载更多
 */
public class TestSingleLoadMoreAdapterActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static final int ONCE_LOAD_SIEE = 10;

    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;

    private TestSingleLoadMoreAdapter mAdapter;
    private int mCurrentPage = 1;  //记录当前页面

    @Override
    public void handleView(Bundle savedInstanceState) {
        srl.setOnRefreshListener(this);  //下拉监听
        srl.setColorSchemeColors(getResources().getColor(R.color.colorPrimaryDark));
//        rv.setLayoutManager(new GridLayoutManager(this, 2));
        mAdapter = new TestSingleLoadMoreAdapter(this, new ArrayList<>());
        rv.setAdapter(mAdapter);
        queryData(mCurrentPage);
    }

    @Override
    public void initListener() {
        titleBar.setOnLeftClickListener(v -> {
            finish();
        });
        //底部上拉加载更多
        mAdapter.setOnLoadMoreListener(rv, () -> {
            queryData(mCurrentPage);
        });
        //设置加载异常监听，如网络异常导致无法加载数据
        mAdapter.setOnLoadFailListener(v -> {
            queryData(mCurrentPage);
        });
        //设置点击事件
        mAdapter.setOnItemClickListener((data, position) -> {
            WangYiNewsWebviewActivity.start(this, "", ((NewsListBean.ResultBean) data).getPath());
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_test_adapter;
    }

    @Override
    public void onRefresh() {
        mCurrentPage = 1;
        mAdapter.setData(new ArrayList<>());
        mAdapter.notifyDataSetChanged();
        queryData(mCurrentPage);
    }

    //加载数据
    private void queryData(int page) {
        RetrofitFactory.getInstance().createService(ApiService.class, UrlConstant.NEWS_URL)
                .getWangYiNewsByBody(page, ONCE_LOAD_SIEE)
                .compose(SchedulerUtil.ioToMain())
                .subscribe(new Observer<NewsListBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(NewsListBean bean) {
                        if (bean.isSuccess()) {
                            showData(bean.getResult() != null ? bean.getResult() : new ArrayList<>());
                        } else {
                            showToast(bean.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadFail();
                        endRefresh();
                        showToast(ExceptionUtil.convertExceptopn(e));
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        endRefresh();
                    }
                });
    }

    //展示数据
    public void showData(List<NewsListBean.ResultBean> list) {
        mCurrentPage++;
        mAdapter.addData(list);
        if (list.size() < ONCE_LOAD_SIEE) {
            mAdapter.loadMoreEnd();
        } else {
            mAdapter.loadMoreComplete();
        }
    }

    //加载异常
    private void loadFail() {
        mAdapter.loadMoreFail();
    }

    //停止刷新，即收起刷新头部
    private void endRefresh() {
        if (srl.isRefreshing()) {
            srl.setRefreshing(false);
        }
    }

}
