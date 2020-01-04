package com.android.frame.mvp

/**
 * Created by xuzhb on 2019/12/29
 * Desc:MVP中的基类Presenter
 */
open class BasePresenter<V : IBaseView> {

    private var mView: V? = null

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

    //加载数据
    open fun loadData() {

    }

}