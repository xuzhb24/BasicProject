package com.android.frame.mvc.extra.http;

import androidx.annotation.Nullable;

import com.android.frame.http.ExceptionUtil;
import com.android.frame.http.model.BaseListResponse;
import com.android.frame.http.model.BaseResponse;
import com.android.frame.mvc.IBaseView;
import com.android.util.LogUtil;

import java.lang.ref.WeakReference;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by xuzhb on 2020/12/31
 * Desc:自定义Observer，加入加载状态布局、加载弹窗、网络错误的处理
 */
public abstract class CustomObserver<T> implements Observer<T> {

    private WeakReference<IBaseView> mWeakReference;
    private boolean showLoadLayout;     //是否显示加载状态布局
    private boolean showLoadingDialog;  //是否显示加载弹窗
    private String message;             //加载弹窗的文本
    private boolean cancelable;         //加载弹窗是否可取消
    private boolean needLoadFinish;     //是否需要集成加载完成的逻辑，配置这个参数是为了解决当一个页面存在多个接口请求时，只让主接口控制加载完成的逻辑

    public CustomObserver(IBaseView view) {
        this(view, true, false, "加载中", true, true);
    }

    public CustomObserver(IBaseView view, boolean showLoadLayout, boolean showLoadingDialog) {
        this(view, showLoadLayout, showLoadingDialog, "加载中", true, true);
    }

    public CustomObserver(IBaseView view, boolean needLoadFinish) {
        this(view, true, false, "加载中", true, needLoadFinish);
    }

    public CustomObserver(IBaseView view, boolean showLoadLayout, boolean showLoadingDialog, boolean needLoadFinish) {
        this(view, showLoadLayout, showLoadingDialog, "加载中", true, needLoadFinish);
    }

    public CustomObserver(IBaseView view, boolean showLoadLayout, boolean showLoadingDialog, String message, boolean cancelable, boolean needLoadFinish) {
        this.mWeakReference = new WeakReference<>(view);
        this.showLoadLayout = showLoadLayout;
        this.showLoadingDialog = showLoadingDialog;
        this.message = message;
        this.cancelable = cancelable;
        this.needLoadFinish = needLoadFinish;
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        IBaseView view = mWeakReference.get();
        if (view == null) {
            d.dispose();
            return;
        }
        view.addDisposable(d);
        if (showLoadLayout) {
            view.showLoadingLayout();
        }
        if (showLoadingDialog) {
            view.showLoadingDialog(message, cancelable);
        }
    }

    @Override
    public void onNext(@NonNull T t) {
        IBaseView view = mWeakReference.get();
        if (view == null) {
            return;
        }
        if (t instanceof BaseResponse) {
            BaseResponse response = (BaseResponse) t;
            if (response.isSuccess()) {
                onSuccess(t);
            } else {
                if (response.isTokenOut()) {  //登录失效
                    view.gotoLogin();       //跳转到登录页
                }
                onFailure(view, response.getMessage(), false, null, t);
            }
        } else if (t instanceof BaseListResponse) {
            BaseListResponse response = (BaseListResponse) t;
            if (response.isSuccess()) {
                onSuccess(t);
            } else {
                if (response.isTokenOut()) {  //登录失效
                    view.gotoLogin();       //跳转到登录页
                }
                onFailure(view, response.getMessage(), false, null, t);
            }
        } else {
            onSuccess(t);
        }
    }

    @Override
    public void onError(@NonNull Throwable e) {
        LogUtil.e("CustomObserver", e);
        observerEnd(true);
        IBaseView view = mWeakReference.get();
        String msg = ExceptionUtil.convertExceptopn(e);
        onFailure(view, msg, true, e, null);
    }

    @Override
    public void onComplete() {
        observerEnd(false);
    }

    private void observerEnd(boolean isError) {
        IBaseView view = mWeakReference.get();
        if (view != null && needLoadFinish) {
            view.loadFinish(isError);
        }
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
     * @param message  异常信息
     * @param isError  是否在onError中执行，返回false时表示在onNext中执行，注意下面t和response一定有一个为空
     * @param t        onError中异常Throwable
     * @param response onNext中接口非成功请求时返回的结果
     */
    protected void onFailure(IBaseView view, String message, boolean isError, @Nullable Throwable t, @Nullable T response) {
        if (view != null) {
            view.showToast(message);
        }
    }

}
