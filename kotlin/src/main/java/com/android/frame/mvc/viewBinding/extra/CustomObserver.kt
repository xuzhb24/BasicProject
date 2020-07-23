package com.android.frame.mvc.viewBinding.extra

import com.android.frame.http.ExceptionUtil
import com.android.frame.http.model.BaseListResponse
import com.android.frame.http.model.BaseResponse
import com.android.frame.mvc.viewBinding.IBaseView
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import java.lang.ref.WeakReference

/**
 * Created by xuzhb on 2019/12/29
 * Desc:自定义Observer，加入加载框和网络错误的处理
 */
abstract class CustomObserver<T>(
    private val mView: IBaseView,
    private val showLoading: Boolean = true,  //是否显示加载框
    private val mMessage: String = "",
    private val mCancelable: Boolean = false
) : Observer<T> {

    private var mWeakReference: WeakReference<IBaseView> = WeakReference(mView)

    override fun onSubscribe(d: Disposable) {
        val view = mWeakReference.get()
        view?.let {
            it.addDisposable(d)  //方便取消订阅关系避免内存泄漏
            if (showLoading) {
                it.showLoading(mMessage, mCancelable)
            }
        }
    }

    override fun onNext(t: T) {
        val view = mWeakReference.get()
        when (t) {
            is BaseResponse<*> -> {
                if (t.isSuccess()) {
                    onSuccess(t)
                } else {
                    if (t.isTokenOut()) {  //登录失效
                        view?.gotoLogin()
                    }
                    onFailure(view, t.getMessage(), false, t)
                }
            }
            is BaseListResponse<*> -> {
                if (t.isSuccess()) {
                    onSuccess(t)
                } else {
                    if (t.isTokenOut()) {  //登录失效
                        view?.gotoLogin()
                    }
                    onFailure(view, t.getMessage(), false, t)
                }
            }
        }
    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
        observerEnd(true)
        val view = mWeakReference.get()
        val msg = ExceptionUtil.convertExceptopn(e)
        onFailure(view, msg, true, null)
    }

    override fun onComplete() {
        observerEnd(false)
    }

    private fun observerEnd(isError: Boolean) {
        val view = mWeakReference.get()
        view?.let {
            it.dismissLoading()
            if (isError) {
                it.loadFail()
            }
            it.loadFinish()  //结束数据加载，收起SwipeRefreshLayout的刷新头部
        }
    }

    abstract fun onSuccess(response: T)

    //isError表示onNext中的错误还是onError中的错误
    protected open fun onFailure(
        view: IBaseView?,
        message: String,
        isError: Boolean,
        response: T? = null
    ) {
        view?.showToast(message)
    }

}