package com.android.frame.mvp

import io.reactivex.disposables.Disposable

/**
 * Created by xuzhb on 2019/12/29
 * Desc:MVP中的基类View
 */
interface IBaseView {

    //显示加载框，表示正在加载数据，cancelable表示是否可由用户取消，默认不可以
    fun showLoading(message: String = "", cancelable: Boolean = false)

    //取消加载框
    fun dismissLoading()

    /*
     * 显示Toast
     * text：提示的文本内容
     * isCenter：是否居中显示，true表示居中显示，默认为false
     * longToast：显示长Toast还是短Toast，默认为Toast.LENGTH_SHORT，即短Toast
     */
    fun showToast(text: CharSequence, isCenter: Boolean = true, longToast: Boolean = false)

    //数据加载失败
    fun loadFail()

    //数据加载完成，收起下拉刷新组件SwipeRefreshLayout的刷新头部
    fun loadFinish()

    //跳转到登录界面
    fun gotoLogin()

    //RxJava建立订阅关系，方便后续Activity或Fragment销毁时取消订阅关系防止内存泄漏
    fun addDisposable(d: Disposable)

}