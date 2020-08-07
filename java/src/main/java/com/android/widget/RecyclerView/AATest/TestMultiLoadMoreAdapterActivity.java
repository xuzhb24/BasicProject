package com.android.widget.RecyclerView.AATest;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.frame.http.AATest.ApiService;
import com.android.frame.http.AATest.UrlConstant;
import com.android.frame.http.AATest.WangYiNewsWebviewActivity;
import com.android.frame.http.AATest.bean.NewsListBean;
import com.android.frame.http.ExceptionUtil;
import com.android.frame.http.RetrofitFactory;
import com.android.frame.http.SchedulerUtil;
import com.android.frame.mvc.BaseActivity;
import com.android.java.R;
import com.android.java.databinding.ActivityTestAdapterBinding;
import com.android.util.DateUtil;
import com.android.widget.RecyclerView.AATest.entity.DateBean;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Create by xuzhb on 2020/1/21
 * Desc:
 */
public class TestMultiLoadMoreAdapterActivity extends BaseActivity<ActivityTestAdapterBinding> implements SwipeRefreshLayout.OnRefreshListener {

    private static final int ONCE_LOAD_SIEE = 10;

    private TestMultiLoadMoreAdapter mAdapter;
    private int mCurrentPage = 1;  //记录当前页面
    private String mCurrentDate = DateUtil.getDistanceDateByDay(1, DateUtil.Y_M_D);

    @Override
    public void handleView(Bundle savedInstanceState) {
        binding.srl.setOnRefreshListener(this);  //下拉监听
        binding.srl.setColorSchemeColors(getResources().getColor(R.color.colorPrimaryDark));
//        rv.setLayoutManager(new GridLayoutManager(this, 2));
        mAdapter = new TestMultiLoadMoreAdapter(this, new ArrayList<>());
        binding.rv.setAdapter(mAdapter);
        queryData(mCurrentPage);
    }

    @Override
    public void initListener() {
        //底部上拉加载更多
        mAdapter.setOnLoadMoreListener(binding.rv, () -> {
            queryData(mCurrentPage);
        });
        //设置加载异常监听，如网络异常导致无法加载数据
        mAdapter.setOnLoadFailListener(v -> {
            queryData(mCurrentPage);
        });
        //获取新闻信息
        mAdapter.setOnGetNewsListener((data, position) -> {
            WangYiNewsWebviewActivity.start(this, "", ((NewsListBean.ResultBean) data).getPath());
        });
    }

    @Override
    public ActivityTestAdapterBinding getViewBinding() {
        return ActivityTestAdapterBinding.inflate(getLayoutInflater());
    }

    @Override
    public void onRefresh() {
        mCurrentPage = 1;
        mCurrentDate = DateUtil.getDistanceDateByDay(1, DateUtil.Y_M_D);
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
                            if (bean.getResult() != null && bean.getResult().size() > 0) {
                                Gson gson = new Gson();
                                List<String> list = new ArrayList<>();
                                for (int i = 0; i < bean.getResult().size(); i++) {
                                    String date = bean.getResult().get(i).getPasstime().split(" ")[0].trim();
                                    if (!TextUtils.equals(mCurrentDate, date)) {
                                        list.add(i, gson.toJson(new DateBean(date)));
                                        mCurrentDate = date;
                                    } else {
                                        list.add(gson.toJson(bean.getResult().get(i)));
                                    }
                                }
                                showData(list);
                            } else {
                                showData(new ArrayList<>());
                            }
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
    public void showData(List<String> list) {
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
        if (binding.srl.isRefreshing()) {
            binding.srl.setRefreshing(false);
        }
    }

}
