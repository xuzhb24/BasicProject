package com.android.frame.mvp;

import com.android.frame.http.SchedulerUtil;
import com.android.frame.http.model.BaseListResponse;
import com.android.frame.mvp.extra.http.CustomObserver;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/**
 * Created by xuzhb on 2020/7/22
 * Desc:列表数据对应的基类Presenter
 */
public class BaseListPresenter<T, V extends IBaseListView<T>> extends BasePresenter<V> {

    @Override
    public void refreshData() {
        //加载第一页的数据
        mView.setCurrentPage(mView.getFirstPage());
        loadData(mView.getCurrentPage());
    }

    //分页加载
    public void loadData(int page) {
        loadData(page,
                mView.getLoadingLayout() != null,  //默认当前布局包含LoadingLayout时才用LoadingLayout来显示加载状态
                mView.getLoadingLayout() == null && mView.isFirstLoad()  //默认当前无加载状态布局且第一次加载时才显示加载弹窗
        );
    }

    //分页加载
    public void loadData(int page, boolean showLoadLayout, boolean showLoadingDialog) {
        if (loadDataFromServer(page) != null) {
            loadDataFromServer(page)
                    .compose(SchedulerUtil.ioToMain())
                    .subscribe(new CustomObserver<BaseListResponse<T>>(mView, showLoadLayout, showLoadingDialog) {
                        @Override
                        public void onSuccess(BaseListResponse<T> response) {
                            mView.showData(page, response.getData() != null ? response.getData() : new ArrayList<>());
                        }
                    });
        }
    }

    //从服务器按页加载数据
    public Observable<BaseListResponse<T>> loadDataFromServer(int page) {
        return null;
    }

    //可以通过重写这个方法处理返回的列表数据
    protected List<T> convertData(List<T> response) {
        return response;
    }

}
