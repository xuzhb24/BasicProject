package com.android.frame.mvp.AATest.listType;

import com.android.frame.http.model.BaseListResponse;
import com.android.frame.mvc.AATest.entity.NewsListBean;
import com.android.frame.mvp.BaseListPresenter;
import com.android.frame.mvp.extra.http.CustomObserver;

import java.util.ArrayList;

/**
 * Created by xuzhb on 2021/1/4
 * Desc:
 */
public class TestMvpListPresenter extends BaseListPresenter<NewsListBean, TestMvpListView> {

    private TestMvpListModel mModel;

    public TestMvpListPresenter() {
        this.mModel = new TestMvpListModel();
    }

    //第一种写法
//    @Override
//    public Observable<BaseListResponse<NewsListBean>> loadDataFromServer(int page) {
//        return mModel.getWangYiNewsByField(page, mView.getLoadSize());
//    }

    //第二种写法
    //如果只是从服务器获取并展示列表数据，那么采用第一种写法重写loadDataFromServer返回相应的接口即可
    //但如果有需要处理其他逻辑，那么就需要重写loadData，并且在获取到数据后调用showData展示列表数据
    @Override
    public void loadData(int page, boolean showLoadLayout, boolean showLoadingDialog) {
        mModel.getWangYiNewsByField(page, mView.getLoadSize())
                .subscribe(new CustomObserver<BaseListResponse<NewsListBean>>(mView, false, mView.isFirstLoad()) {
                    @Override
                    public void onSuccess(BaseListResponse<NewsListBean> response) {
                        if (page < 3) {
                            mView.showData(page, response.getData());
                        } else {  //模拟数据加载到底
                            mView.showData(page, new ArrayList<>());
                        }
                    }
                });
    }

}
