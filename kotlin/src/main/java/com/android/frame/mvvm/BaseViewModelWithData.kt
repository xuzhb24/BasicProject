package com.android.frame.mvvm

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding
import com.android.frame.mvvm.extra.LiveDataEntity.ErrorResponse
import com.android.util.ToastUtil
import kotlinx.coroutines.CoroutineScope

/**
 * Created by xuzhb on 2021/8/7
 * Desc:基类ViewModel的进一步封装，携带数据结构，包含了主接口的请求方法，
 * 可以在onSuccess中处理成功的回调，在onFailure中处理失败的回调
 */
abstract class BaseViewModelWithData<T, VB : ViewBinding> : BaseViewModel<VB>() {

    private var successData = MutableLiveData<T>()
    private var errorData = MutableLiveData<ErrorResponse<T>>()

    //请求主接口
    fun launchMain(
        block: suspend CoroutineScope.() -> T,  //请求接口方法，T表示data实体泛型
        showLoadLayout: Boolean = true,         //是否显示加载状态布局
        showLoadingDialog: Boolean = false,     //是否显示加载弹窗
        message: String = "加载中",             //加载弹窗的提示文本
        cancelable: Boolean = true              //加载弹窗是否可以点击取消
    ) {
        launch(block, successData, errorData, showLoadLayout, showLoadingDialog, message, cancelable, true)
    }

    override fun observe(activity: Activity, owner: LifecycleOwner) {
        observer(owner)
    }

    override fun observe(fragment: Fragment, owner: LifecycleOwner) {
        observer(owner)
    }

    private fun observer(owner: LifecycleOwner) {
        successData.observe(owner, Observer {
            onSuccess(it)
        })
        errorData.observe(owner, Observer {
            onFailure(it.message, it.isException, it.exception, it.response)
        })
    }

    /**
     * 接口请求成功(onNext中返回成功码)后执行的回调
     * @param response 返回的结果
     */
    abstract fun onSuccess(response: T)

    /**
     * 接口请求失败(onNext中返回状态码非成功码)或者网络异常(执行到onError中)执行的回调
     * @param message      异常信息
     * @param isException  是否在onError中执行，返回false时表示在onNext中执行，注意下面t和response一定有一个为空
     * @param exception    onError中异常Throwable
     * @param response     onNext中接口非成功请求时返回的结果
     */
    protected open fun onFailure(
        message: String,
        isException: Boolean,
        exception: Throwable?,
        response: T?
    ) {
        ToastUtil.showToast(message, true)
    }

}