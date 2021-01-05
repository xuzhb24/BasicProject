package com.android.frame.mvp

/**
 * Created by xuzhb on 2019/12/29
 * Desc:MVP中的基类Presenter
 */
open class BasePresenter<V : IBaseView> {

    protected var mView: V? = null

    //绑定View
    fun attachView(view: V) {
        this.mView = view
    }

    //解绑View
    fun detachView() {
        this.mView = null
    }

    fun isAttach(): Boolean {
        return mView != null
    }

    //加载数据，进入页面时默认就会进行加载，请务必重写refreshData，当加载失败点击重试或者下拉刷新时会调用这个方法
    open fun refreshData() {

    }

}