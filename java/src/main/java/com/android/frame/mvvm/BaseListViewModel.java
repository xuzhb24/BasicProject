package com.android.frame.mvvm;

import androidx.lifecycle.MutableLiveData;

import com.android.frame.http.model.BaseListResponse;
import com.android.frame.mvvm.extra.LiveDataEntity.ErrorResponse;

import io.reactivex.Observable;

/**
 * Created by xuzhb on 2021/8/9
 * Desc:列表数据对应的基类ViewModel
 */
public class BaseListViewModel<T> extends BaseViewModel {

    public MutableLiveData<BaseListResponse<T>> successData = new MutableLiveData<>();
    public MutableLiveData<ErrorResponse<BaseListResponse<T>>> errorData = new MutableLiveData<>();

    //分页加载
    public void loadData(int page, int count) {
        loadData(page, count, true, false);
    }

    //分页加载
    public void loadData(int page, int count, boolean showLoadLayout, boolean showLoadingDialog) {
        if (loadDataFromServer(page, count) != null) {
            launch(loadDataFromServer(page, count), successData, errorData, showLoadLayout, showLoadingDialog);
        }
    }

    //从服务器按页加载数据
    public Observable<BaseListResponse<T>> loadDataFromServer(int page, int count) {
        return null;
    }

}
