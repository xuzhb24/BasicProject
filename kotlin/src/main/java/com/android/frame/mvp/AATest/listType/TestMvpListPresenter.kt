package com.android.frame.mvp.AATest.listType

import com.android.frame.http.model.BaseListResponse
import com.android.frame.mvc.AATest.entity.NewsListBean
import com.android.frame.mvp.BaseListPresenter
import com.android.frame.mvp.extra.http.CustomObserver

/**
 * Created by xuzhb on 2021/1/5
 * Desc:
 */
class TestMvpListPresenter : BaseListPresenter<NewsListBean, TestMvpListView>() {

    private val mModel by lazy { TestMvpListModel() }

    //第一种写法
//    override fun loadDataFromServer(page: Int): Observable<BaseListResponse<NewsListBean>>? {
//        return mModel.getWangYiNewsByField(page, mView?.getLoadSize() ?: 10)
//    }

    //第二种写法
    //如果只是从服务器获取并展示列表数据，那么采用第一种写法重写loadDataFromServer返回相应的接口即可
    //但如果有需要处理其他逻辑，那么就需要重写loadData，并且在获取到数据后调用showData展示列表数据
    override fun loadData(page: Int, showLoadLayout: Boolean, showLoadingDialog: Boolean) {
        mView?.let {
            mModel.getWangYiNewsByField(page, it.getLoadSize())
                .subscribe(object : CustomObserver<BaseListResponse<NewsListBean>>(it, false, it.isFirstLoad()) {
                    override fun onSuccess(response: BaseListResponse<NewsListBean>) {
                        if (page < 2) {
                            it.showData(page, response.data)
                        } else {  //模拟数据加载到底
                            it.showData(page, mutableListOf())
                        }
                    }

                })
        }
    }

}