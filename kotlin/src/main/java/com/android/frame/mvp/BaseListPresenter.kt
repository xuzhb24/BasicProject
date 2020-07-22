package com.android.frame.mvp

import com.android.frame.http.SchedulerUtil
import com.android.frame.http.model.BaseListResponse
import com.android.frame.mvp.extra.CustomObserver
import io.reactivex.Observable

/**
 * Created by xuzhb on 2020/7/22
 * Desc:列表数据对应的基类Presenter
 */
open class BaseListPresenter<T, V : IBaseListView<T>> : BasePresenter<V>() {

    //分页加载数据，showLoading：是否显示加载框
    open fun loadData(page: Int, showLoading: Boolean = false) {
        if (mView != null) {
            loadDataFromServer(page)?.let {
                it.compose(SchedulerUtil.ioToMain())
                    .subscribe(object : CustomObserver<BaseListResponse<T>>(mView!!, showLoading) {
                        override fun onSuccess(response: BaseListResponse<T>) {
                            mView!!.showData(page, response.data ?: mutableListOf())
                        }
                    })
            }
        }
    }

    //从服务器按页加载数据，一般子类的Presenter重写这个方法即可
    open fun loadDataFromServer(page: Int): Observable<BaseListResponse<T>>? = null

}