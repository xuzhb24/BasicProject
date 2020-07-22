package com.android.frame.mvp;

import com.android.frame.http.SchedulerUtil;
import com.android.frame.http.model.BaseListResponse;
import com.android.frame.mvp.extra.CustomObserver;

import java.util.ArrayList;

import io.reactivex.Observable;

/**
 * Created by xuzhb on 2020/7/22
 * Desc:列表数据对应的基类Presenter
 */
public class BaseListPresenter<T, V extends IBaseListView<T>> extends BasePresenter<V> {

    //分页加载数据，默认不显示加载框
    public void loadData(int page) {
        loadData(page, false);
    }

    //分页加载数据，showLoading：是否显示加载框
    public void loadData(int page, boolean showLoading) {
        if (loadDataFromServer(page) != null) {
            loadDataFromServer(page).compose(SchedulerUtil.ioToMain()).subscribe(new CustomObserver<BaseListResponse<T>>(mView, showLoading) {
                @Override
                public void onSuccess(BaseListResponse<T> response) {
                    mView.showData(page, response.getData() != null ? response.getData() : new ArrayList<>());
                }
            });
        }
    }

    //从服务器按页加载数据，一般子类的Presenter重写这个方法即可
    public Observable<BaseListResponse<T>> loadDataFromServer(int page) {
        return null;
    }

}
