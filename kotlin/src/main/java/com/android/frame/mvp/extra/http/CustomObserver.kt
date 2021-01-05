package com.android.frame.mvp.extra.http

import com.android.frame.http.ExceptionUtil
import com.android.frame.http.model.BaseListResponse
import com.android.frame.http.model.BaseResponse
import com.android.frame.mvp.IBaseView
import com.android.util.LogUtil
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import java.lang.ref.WeakReference

/**
 * Created by xuzhb on 2019/12/29
 * Desc:自定义Observer，加入加载状态布局、加载弹窗、网络错误的处理
 */
abstract class CustomObserver<T>(
    private val mView: IBaseView,
    private val showLoadLayout: Boolean = true,      //是否显示加载状态布局
    private val showLoadingDialog: Boolean = false,  //是否显示加载弹窗
    private val message: String = "加载中",
    private val cancelable: Boolean = true,
    private val needLoadFinish: Boolean = true  //是否需要集成加载完成的逻辑，配置这个参数是为了解决当一个页面存在多个接口请求时，只让主接口控制加载完成的逻辑
) : Observer<T> {

    private var mWeakReference: WeakReference<IBaseView> = WeakReference(mView)

    override fun onSubscribe(d: Disposable) {
        val view = mWeakReference.get()
        view?.let {
            it.addDisposable(d)  //方便取消订阅关系避免内存泄漏
            if (showLoadLayout) {
                it.showLoadingLayout()
            }
            if (showLoadingDialog) {
                it.showLoadingDialog(message, cancelable)
            }
        }
    }

    override fun onNext(t: T) {
        val view = mWeakReference.get()
        when (t) {
            is BaseResponse<*> -> {  //非列表类型数据
                if (t.isSuccess()) {
                    onSuccess(t)
                } else {
                    if (t.isTokenOut()) {  //登录失效
                        view?.gotoLogin()  //跳转到登录页
                    }
                    onFailure(view, t.getMessage(), false, null, t)
                }
            }
            is BaseListResponse<*> -> {  //列表类型数据
                if (t.isSuccess()) {
                    onSuccess(t)
                } else {
                    if (t.isTokenOut()) {  //登录失效
                        view?.gotoLogin()  //跳转到登录页
                    }
                    onFailure(view, t.getMessage(), false, null, t)
                }
            }
            else -> onSuccess(t)
        }
    }

    override fun onError(e: Throwable) {
        LogUtil.e("CustomObserver", e)
        observerEnd(true)
        val view = mWeakReference.get()
        val msg = ExceptionUtil.convertExceptopn(e)
        onFailure(view, msg, true, e, null)
    }

    override fun onComplete() {
        observerEnd(false)
    }

    private fun observerEnd(isError: Boolean) {
        val view = mWeakReference.get()
        if (needLoadFinish) {
            view?.loadFinish(isError)
        }
    }

    /**
     * 接口请求成功(onNext中返回成功码)后执行的回调
     * @param response 返回的结果
     */
    abstract fun onSuccess(response: T)

    /**
     * 接口请求失败(onNext中返回状态码非成功码)或者网络异常(执行到onError中)执行的回调
     * @param message  异常信息
     * @param isError  是否在onError中执行，返回false时表示在onNext中执行，注意下面t和response一定有一个为空
     * @param t        onError中异常Throwable
     * @param response onNext中接口非成功请求时返回的结果
     */
    protected open fun onFailure(
        view: IBaseView?,
        message: String,
        isError: Boolean,
        t: Throwable?,
        response: T?
    ) {
        view?.showToast(message)
    }

}