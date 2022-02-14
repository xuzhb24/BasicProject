package com.android.frame.mvvm

import androidx.lifecycle.MutableLiveData
import com.android.frame.http.model.BaseListResponse
import com.android.frame.mvvm.extra.LiveDataEntity.ErrorResponse
import kotlinx.coroutines.CoroutineScope

/**
 * Created by xuzhb on 2021/8/7
 * Desc:列表数据对应的基类ViewModel
 */
open class BaseListViewModel<T> : BaseViewModel() {

    var successData = MutableLiveData<BaseListResponse<T>>()
    var errorData = MutableLiveData<ErrorResponse<BaseListResponse<T>>>()

    //分页加载
    open fun loadData(
        page: Int,
        count: Int,
        showLoadLayout: Boolean = true,      //是否显示加载状态布局
        showLoadingDialog: Boolean = false
    ) {
        loadDataFromServer(page, count)?.let {
            launch(it, successData, errorData, showLoadLayout, showLoadingDialog)
        }
    }

    //从服务器按页加载数据
    open fun loadDataFromServer(page: Int, count: Int): (suspend CoroutineScope.() -> BaseListResponse<T>)? = null

}