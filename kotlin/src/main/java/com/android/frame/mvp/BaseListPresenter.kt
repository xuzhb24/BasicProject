package com.android.frame.mvp

import com.android.frame.http.SchedulerUtil
import com.android.frame.http.model.BaseListResponse
import com.android.frame.mvp.extra.http.CustomObserver
import io.reactivex.Observable

/**
 * Created by xuzhb on 2020/7/22
 * Desc:列表数据对应的基类Presenter
 */
open class BaseListPresenter<T, V : IBaseListView<T>> : BasePresenter<V>() {

    override fun refreshData() {
        //加载第一页的数据
        mView?.let {
            it.setCurrentPage(it.getFirstPage())
            loadData(it.getCurrentPage())
        }
    }

    //分页加载
    open fun loadData(
        page: Int,
        showLoadLayout: Boolean = (mView?.getLoadingLayout() != null),  //默认当前布局包含LoadingLayout时才用LoadingLayout来显示加载状态
        showLoadingDialog: Boolean = (mView?.getLoadingLayout() == null && mView?.isFirstLoad() ?: false)  //默认当前无加载状态布局且第一次加载时才显示加载弹窗
    ) {
        if (mView != null) {
            loadDataFromServer(page)?.let {
                it.compose(SchedulerUtil.ioToMain())
                    .subscribe(object : CustomObserver<BaseListResponse<T>>(mView!!, showLoadLayout, showLoadingDialog) {
                        override fun onSuccess(response: BaseListResponse<T>) {
                            mView!!.showData(page, response.data ?: mutableListOf())
                        }
                    })
            }
        }
    }

    //从服务器按页加载数据，一般子类的Presenter重写这个方法即可
    open fun loadDataFromServer(page: Int): Observable<BaseListResponse<T>>? = null

    //可以通过重写这个方法处理返回的列表数据
    open fun convertData(response: MutableList<T>?): MutableList<T>? {
        return response
    }

}