package com.android.frame.mvvm.AATest.listType

import com.android.frame.mvc.AATest.entity.NewsListBean
import com.android.frame.mvvm.AATest.server.ApiHelper
import com.android.frame.mvvm.BaseListViewModel

/**
 * Created by xuzhb on 2021/8/8
 * Desc:
 */
class TestMvvmListFragmentViewModel : BaseListViewModel<NewsListBean>() {

    //第一种写法
//    override fun loadDataFromServer(page: Int, count: Int): (suspend CoroutineScope.() -> BaseListResponse<NewsListBean>)? {
//        return { ApiHelper.getWangYiNewsByField(page, count) }
//    }

    //第二种写法
    //如果只是从服务器获取并展示列表数据，那么采用第一种写法重写loadDataFromServer返回相应的接口即可
    //但如果有需要处理其他逻辑，那么就需要重写loadData，并且在获取到数据后重写对应Fragment的observerListDataChange方法，
    //在里面调用showData展示列表数据，参见TestMvvmListFragment里的observerListDataChange
    override fun loadData(page: Int, count: Int, showLoadLayout: Boolean, showLoadingDialog: Boolean) {
        launch({ ApiHelper.getWangYiNewsByField(page, count) }, successData, errorData, false, page == 1)
    }

}