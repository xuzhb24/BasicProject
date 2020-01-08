package com.android.frame.mvp.extra;

import com.android.base.BaseApplication;
import com.android.frame.http.ExceptionUtil;
import com.android.frame.http.model.BaseListResponse;
import com.android.frame.http.model.BaseResponse;
import com.android.frame.mvp.IBaseView;
import com.android.util.NetworkUtil;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import java.lang.ref.WeakReference;

/**
 * Created by xuzhb on 2020/1/5
 * Desc:自定义Observer，加入加载框和网络错误的处理
 */
public abstract class CustomObserver<T> implements Observer<T> {

    private WeakReference<IBaseView> mWeakReference;
    private boolean mShowLoading;  //是否显示加载框
    private String mMessage;
    private boolean mCancelable;

    public CustomObserver(IBaseView view) {
        this(view, true, null, false);
    }

    public CustomObserver(IBaseView view, boolean showLoading) {
        this(view, showLoading, null, false);
    }

    public CustomObserver(IBaseView view, boolean showLoading, boolean cancelable) {
        this(view, showLoading, null, cancelable);
    }

    public CustomObserver(IBaseView view, boolean showLoading, String message, boolean cancelable) {
        mWeakReference = new WeakReference<>(view);
        mShowLoading = showLoading;
        mMessage = message;
        mCancelable = cancelable;
    }

    @Override
    public void onSubscribe(Disposable d) {
        IBaseView view = mWeakReference.get();
        if (view == null) {
            d.dispose();
            return;
        }
        view.addDisposable(d);
        if (mShowLoading) {
            view.showLoading(mMessage, mCancelable);
        }
    }

    @Override
    public void onNext(T t) {
        IBaseView view = mWeakReference.get();
        if (view == null) {
            return;
        }
        if (t instanceof BaseResponse) {
            BaseResponse response = (BaseResponse) t;
            if (response.isSuccess()) {
                onSuccess(t);
            } else {
                if (response.getCode() == -1001) {  //登录失效（假设code是-1001）
                    view.gotoLogin();
                }
                onFailure(view, response.getMsg(), false, t);
            }
        } else if (t instanceof BaseListResponse) {
            BaseListResponse response = (BaseListResponse) t;
            if (response.isSuccess()) {
                onSuccess(t);
            } else {
                if (response.getCode() == -1001) {  //登录失效（假设code是-1001）
                    view.gotoLogin();
                }
                onFailure(view, response.getMsg(), false, t);
            }
        }
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        observerEnd();
        IBaseView view = mWeakReference.get();
        String msg = ExceptionUtil.convertExceptopn(e);
        onFailure(view, msg, true, null);
    }

    @Override
    public void onComplete() {
        observerEnd();
    }

    private void observerEnd() {
        IBaseView view = mWeakReference.get();
        if (view != null) {
            if (mShowLoading) {
                view.dismissLoading();
            }
            view.showNetErrorLayout(!NetworkUtil.isConnected(BaseApplication.getInstance()));
            view.loadFinish();  //结束数据加载，收起SwipeRefreshLayout的刷新头部
        }
    }

    public abstract void onSuccess(T response);

    //isError表示onNext中的错误还是onError中的错误
    protected void onFailure(IBaseView view, String message, boolean isError, T response) {
        if (view != null) {
            view.showToast(message);
        }
    }

}
