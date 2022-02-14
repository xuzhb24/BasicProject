package com.android.frame.mvvm;

import androidx.lifecycle.MutableLiveData;

import com.android.frame.mvvm.extra.LiveDataEntity.ErrorResponse;

import io.reactivex.Observable;

/**
 * Created by xuzhb on 2021/8/9
 * Desc:基类ViewModel的进一步封装，携带数据结构，包含了主接口的请求方法，
 * 可以通过successData处理请求成功的情况，通过errorData处理请求失败的情况
 */
public abstract class BaseViewModelWithData<T> extends BaseViewModel {

    public MutableLiveData<T> successData = new MutableLiveData<>();
    public MutableLiveData<ErrorResponse<T>> errorData = new MutableLiveData<>();

    //请求主接口
    public void launchMain(Observable<T> block) {
        launchMain(block, true, false, "加载中", true);
    }

    //请求主接口
    public void launchMain(
            Observable<T> block,        //请求接口方法，T表示data实体泛型
            boolean showLoadLayout,     //是否显示加载状态布局
            boolean showLoadingDialog   //是否显示加载弹窗
    ) {
        launchMain(block, showLoadLayout, showLoadingDialog, "加载中", true);
    }

    //请求主接口
    public void launchMain(
            Observable<T> block,        //请求接口方法，T表示data实体泛型
            boolean showLoadLayout,     //是否显示加载状态布局
            boolean showLoadingDialog,  //是否显示加载弹窗
            String message,             //加载弹窗的提示文本
            boolean cancelable          //加载弹窗是否可以点击取消
    ) {
        launch(block, successData, errorData, showLoadLayout, showLoadingDialog, message, cancelable, true);
    }

}
