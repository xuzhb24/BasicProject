package com.android.frame.mvp;

/**
 * Created by xuzhb on 2020/1/5
 * Desc:MVP中的基类Presenter
 */
public class BasePresenter<V extends IBaseView> {

    protected V mView;

    //绑定View
    public void attachView(V view) {
        this.mView = view;
    }

    //解绑View
    public void detachView() {
        this.mView = null;
    }

    public boolean isAttach() {
        return mView != null;
    }

    //加载数据，进入页面时默认就会进行加载，请务必重写refreshData，当加载失败点击重试或者下拉刷新时会调用这个方法
    public void refreshData() {

    }

}
