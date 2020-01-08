package com.android.frame.mvp;

/**
 * Created by xuzhb on 2020/1/5
 * Desc:MVP中的基类Presenter
 */
public class BasePresenter<V extends IBaseView> {

    private V mView;

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

    //加载数据
    public void loadData() {

    }

}
