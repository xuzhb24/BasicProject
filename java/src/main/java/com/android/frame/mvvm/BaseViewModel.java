package com.android.frame.mvvm;

import android.app.Activity;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.viewbinding.ViewBinding;

import com.android.frame.http.ExceptionUtil;
import com.android.frame.http.model.BaseListResponse;
import com.android.frame.http.model.BaseResponse;
import com.android.frame.mvvm.extra.LiveDataEntity.DialogConfig;
import com.android.frame.mvvm.extra.LiveDataEntity.ErrorResponse;
import com.android.util.LogUtil;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by xuzhb on 2021/8/9
 * Desc:基类ViewModel
 */
public class BaseViewModel<VB extends ViewBinding> extends ViewModel {

    private static final String TAG = "BaseViewModel";

    protected VB binding;
    protected CompositeDisposable mCompositeDisposable = new CompositeDisposable();        //防止RxJava内存泄漏
    public MutableLiveData<Boolean> showLoadLayoutData = new MutableLiveData<>();          //是否显示加载状态布局，前提是当前布局包含加载控件，如果不包含，此参数无效
    public MutableLiveData<DialogConfig> showLoadingDialogData = new MutableLiveData<>();  //是否显示加载弹窗
    public MutableLiveData<Boolean> loadFinishErrorData = new MutableLiveData<>();         //是否加载失败

    //绑定ViewBinding和ViewModel
    public void bind(VB binding) {
        this.binding = binding;
    }

    //Activity中监听
    public void observe(Activity activity, LifecycleOwner owner) {

    }

    //Fragment中监听
    public void observe(Fragment fragment, LifecycleOwner owner) {

    }

    //接口请求
    public <T> void launch(
            Observable<T> block,
            MutableLiveData<T> successData,
            MutableLiveData<ErrorResponse<T>> errorData
    ) {
        launch(block, successData, errorData, true, false, "加载中", true, true);
    }

    //接口请求
    public <T> void launch(
            Observable<T> block,
            MutableLiveData<T> successData,
            MutableLiveData<ErrorResponse<T>> errorData,
            boolean showLoadLayout,
            boolean showLoadingDialog
    ) {
        launch(block, successData, errorData, showLoadLayout, showLoadingDialog, "加载中", true, true);
    }

    //接口请求
    public <T> void launch(
            Observable<T> block,
            MutableLiveData<T> successData,
            MutableLiveData<ErrorResponse<T>> errorData,
            boolean needLoadFinish
    ) {
        launch(block, successData, errorData, true, false, "加载中", true, needLoadFinish);
    }

    //接口请求
    public <T> void launch(
            Observable<T> block,
            MutableLiveData<T> successData,
            MutableLiveData<ErrorResponse<T>> errorData,
            boolean showLoadLayout,
            boolean showLoadingDialog,
            boolean needLoadFinish
    ) {
        launch(block, successData, errorData, showLoadLayout, showLoadingDialog, "加载中", true, needLoadFinish);
    }

    //接口请求，加入加载状态布局、加载弹窗、网络错误的处理
    public <T> void launch(
            Observable<T> block,             //请求接口方法，T表示data实体泛型
            MutableLiveData<T> successData,  //接口返回的成功结果
            MutableLiveData<ErrorResponse<T>> errorData,  //接口返回的异常结果
            boolean showLoadLayout,     //是否显示加载状态布局
            boolean showLoadingDialog,  //是否显示加载弹窗
            String message,             //加载弹窗的提示文本
            boolean cancelable,         //加载弹窗是否可以点击取消
            boolean needLoadFinish      //是否需要集成加载完成的逻辑，配置这个参数是为了解决当一个页面存在多个接口请求时，只让主接口控制加载完成的逻辑
    ) {
        //请求之前
        if (showLoadLayout) {
            showLoadLayoutData.setValue(true);  //显示加载状态布局
        }
        if (showLoadingDialog) {
            showLoadingDialogData.setValue(new DialogConfig(message, cancelable));  //显示加载弹窗
        }
        //开始请求
        block.subscribe(new Observer<T>() {
            @Override
            public void onSubscribe(@NotNull Disposable d) {
                LogUtil.i(TAG, "onSubscribe");
                mCompositeDisposable.add(d);
            }

            @Override
            public void onNext(@NotNull T t) {
                LogUtil.i(TAG, "onNext");
                if (t instanceof BaseResponse) {  //非列表类型数据
                    BaseResponse response = (BaseResponse) t;
                    if (response.isSuccess()) {  //请求成功
                        successData.setValue(t);
                    } else {  //请求失败
                        errorData.setValue(new ErrorResponse<>(false, response.getMessage(), null, t));
                    }
                } else if (t instanceof BaseListResponse) {  //列表类型数据
                    BaseListResponse response = (BaseListResponse) t;
                    if (response.isSuccess()) {  //请求成功
                        successData.setValue(t);
                    } else {  //请求失败
                        errorData.setValue(new ErrorResponse<>(false, response.getMessage(), null, t));
                    }
                } else {
                    successData.setValue(t);
                }
            }

            @Override
            public void onError(@NotNull Throwable e) {
                LogUtil.i(TAG, "onError");
                LogUtil.e(TAG, e);
                //请求完成
                if (needLoadFinish) {
                    loadFinishErrorData.setValue(true);
                }
                //请求异常
                String msg = ExceptionUtil.convertExceptopn(e);
                errorData.setValue(new ErrorResponse<>(true, msg, e, null));
            }

            @Override
            public void onComplete() {
                LogUtil.i(TAG, "onComplete");
                //请求完成
                if (needLoadFinish) {
                    loadFinishErrorData.setValue(false);
                }
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        LogUtil.e(TAG, "onCleared");
        mCompositeDisposable.clear();
    }

}
