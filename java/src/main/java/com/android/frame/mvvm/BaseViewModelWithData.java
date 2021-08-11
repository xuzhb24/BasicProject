package com.android.frame.mvvm;

import android.app.Activity;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.viewbinding.ViewBinding;

import com.android.frame.mvvm.extra.LiveDataEntity.ErrorResponse;
import com.android.util.ToastUtil;

import io.reactivex.Observable;

/**
 * Created by xuzhb on 2021/8/9
 * Desc:基类ViewModel的进一步封装，携带数据结构，包含了主接口的请求方法，
 * 可以在onSuccess中处理成功的回调，在onFailure中处理失败的回调
 */
public abstract class BaseViewModelWithData<T, VB extends ViewBinding> extends BaseViewModel<VB> {

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

    @Override
    public void observe(Activity activity, LifecycleOwner owner) {
        observe(owner);
    }

    @Override
    public void observe(Fragment fragment, LifecycleOwner owner) {
        observe(owner);
    }

    private void observe(LifecycleOwner owner) {
        successData.observe(owner, this::onSuccess);
        errorData.observe(owner, it -> {
            onFailure(it.getMessage(), it.isException(), it.getException(), it.getResponse());
        });
    }

    /**
     * 接口请求成功(onNext中返回成功码)后执行的回调
     *
     * @param response 返回的结果
     */
    public abstract void onSuccess(T response);

    /**
     * 接口请求失败(onNext中返回状态码非成功码)或者网络异常(执行到onError中)执行的回调
     *
     * @param message     异常信息
     * @param isException 是否在onError中执行，返回false时表示在onNext中执行，注意下面t和response一定有一个为空
     * @param exception   onError中异常Throwable
     * @param response    onNext中接口非成功请求时返回的结果
     */
    protected void onFailure(String message, boolean isException, @Nullable Throwable exception, @Nullable T response) {
        ToastUtil.showToast(message);
    }

}
