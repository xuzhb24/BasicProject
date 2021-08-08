package com.android.frame.mvvm

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.viewbinding.ViewBinding
import com.android.frame.http.ExceptionUtil
import com.android.frame.http.model.BaseListResponse
import com.android.frame.http.model.BaseResponse
import com.android.frame.mvvm.extra.LiveDataEntity.DialogConfig
import com.android.frame.mvvm.extra.LiveDataEntity.ErrorResponse
import com.android.util.LogUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Created by xuzhb on 2021/8/5
 * Desc:基类ViewModel
 */
open class BaseViewModel<VB : ViewBinding> : ViewModel() {

    protected lateinit var binding: VB
    var showLoadLayoutData = MutableLiveData<Boolean>()          //是否显示加载状态布局，前提是当前布局包含加载控件，如果不包含，此参数无效
    var showLoadingDialogData = MutableLiveData<DialogConfig>()  //是否显示加载弹窗
    var loadFinishErrorData = MutableLiveData<Boolean>()         //是否加载失败

    //绑定ViewBinding和ViewModel
    fun bind(binding: VB) {
        this.binding = binding
    }

    //Activity中监听
    open fun observe(activity: Activity, owner: LifecycleOwner) {

    }

    //Fragment中监听
    open fun observe(fragment: Fragment, owner: LifecycleOwner) {

    }

    //接口请求，加入加载状态布局、加载弹窗、网络错误的处理
    fun <T> launch(
        block: suspend CoroutineScope.() -> T,  //请求接口方法，T表示data实体泛型
        successData: MutableLiveData<T>,        //接口返回的成功结果
        errorData: MutableLiveData<ErrorResponse<T>>,  //接口返回的异常结果
        showLoadLayout: Boolean = true,      //是否显示加载状态布局
        showLoadingDialog: Boolean = false,  //是否显示加载弹窗
        message: String = "加载中",          //加载弹窗的提示文本
        cancelable: Boolean = true,          //加载弹窗是否可以点击取消
        needLoadFinish: Boolean = true       //是否需要集成加载完成的逻辑，配置这个参数是为了解决当一个页面存在多个接口请求时，只让主接口控制加载完成的逻辑
    ) {
        //请求之前
        if (showLoadLayout) {
            showLoadLayoutData.value = true  //显示加载状态布局
        }
        if (showLoadingDialog) {
            showLoadingDialogData.value = DialogConfig(message, cancelable)  //显示加载弹窗
        }
        viewModelScope.launch {
            kotlin.runCatching {
                val t = block()  //开始请求
                when (t) {
                    is BaseResponse<*> -> {  //非列表类型数据
                        if (t.isSuccess()) {  //请求成功
                            successData.value = t
                        } else {  //请求失败
                            errorData.value = ErrorResponse(false, t.getMessage(), null, t)  //封装失败结果
                        }
                    }
                    is BaseListResponse<*> -> {  //列表类型数据
                        if (t.isSuccess()) {  //请求成功
                            successData.value = t
                        } else {  //请求失败
                            errorData.value = ErrorResponse(false, t.getMessage(), null, t)  //封装失败结果
                        }
                    }
                    else -> successData.value = t
                }
                //请求完成
                if (needLoadFinish) {
                    loadFinishErrorData.value = false
                }
            }.onFailure {
                LogUtil.e("BaseViewModel", it)
                //请求完成
                if (needLoadFinish) {
                    loadFinishErrorData.value = true
                }
                //请求异常
                val msg = ExceptionUtil.convertExceptopn(it)
                errorData.value = ErrorResponse(true, msg, it, null)  //封装失败结果
            }
        }
    }

}