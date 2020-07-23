package com.android.frame.mvc.viewBinding;

import io.reactivex.disposables.Disposable;

/**
 * Created by xuzhb on 2020/7/23
 */
public interface IBaseView {

    //显示加载框，表示正在加载数据
    void showLoading();

    //显示加载框，表示正在加载数据，cancelable表示是否可由用户取消，默认不可以
    void showLoading(String message, boolean cancelable);

    //取消加载框
    void dismissLoading();

    //显示Toast
    void showToast(CharSequence text);

    /*
     * 显示Toast
     * text：提示的文本内容
     * isCenter：是否居中显示，true表示居中显示，默认为false
     * longToast：显示长Toast还是短Toast，默认为Toast.LENGTH_SHORT，即短Toast
     */
    void showToast(CharSequence text, boolean isCenter, boolean longToast);

    //数据加载失败
    void loadFail();

    //数据加载完成，收起下拉刷新组件SwipeRefreshLayout的刷新头部
    void loadFinish();

    //跳转到登录界面
    void gotoLogin();

    //RxJava建立订阅关系，方便后续Activity或Fragment销毁时取消订阅关系防止内存泄漏
    void addDisposable(Disposable d);

}
