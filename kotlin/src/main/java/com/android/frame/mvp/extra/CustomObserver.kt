package com.android.frame.mvp.extra

import com.android.base.BaseApplication
import com.android.frame.http.ExceptionUtil
import com.android.frame.http.model.BaseListResponse
import com.android.frame.http.model.BaseResponse
import com.android.frame.mvp.IBaseView
import com.android.util.NetworkUtil
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

    private var mWeakReference: WeakReference<IBaseView>

    init {
        mWeakReference = WeakReference(mView)
    }

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
                    if (t.code == -1001) {  //登录失效（假设code是-1001）
                        view?.gotoLogin()
                    }
                    onFailure(view, t.msg, false, t)
                }
            }
            is BaseListResponse<*> -> {
                if (t.isSuccess()) {
                    onSuccess(t)
                } else {
                    if (t.code == -1001) {  //登录失效（假设code是-1001）
                        view?.gotoLogin()
                    }
                    onFailure(view, t.msg, false, t)
                }
            }
        }
    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
        observerEnd()
        val view = mWeakReference.get()
        val msg = ExceptionUtil.convertExceptopn(e)
        onFailure(view, msg, true, null)
    }

    override fun onComplete() {
        observerEnd()
    }

    private fun observerEnd() {
        val view = mWeakReference.get()
        view?.let {
            if (showLoading) {
                it.dismissLoading()
            }
            it.showNetErrorLayout(!NetworkUtil.isConnected(BaseApplication.instance))
            it.loadFinish()  //结束数据加载，收起SwipeRefreshLayout的刷新头部
        }
    }

    abstract fun onSuccess(response: T)

    //isError表示onNext中的错误还是onError中的错误
    protected open fun onFailure(view: IBaseView?, message: String, isError: Boolean, response: T? = null) {
        view?.showToast(message, true)
    }

}