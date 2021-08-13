package com.android.frame.mvc.AATest;

import android.os.Bundle;

import com.android.frame.http.AATest.WangYiNewsWebviewActivity;
import com.android.frame.http.model.BaseListResponse;
import com.android.frame.mvc.AATest.adapter.NewsListAdapter;
import com.android.frame.mvc.AATest.entity.NewsListBean;
import com.android.frame.mvc.AATest.server.ApiHelper;
import com.android.frame.mvc.BaseListActivity;
import com.android.java.R;
import com.android.java.databinding.ActivityTestMvcListBinding;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import io.reactivex.Observable;

/**
 * Created by xuzhb on 2021/1/1
 * Desc:
 */
public class TestMvcListActivity extends BaseListActivity<NewsListBean, ActivityTestMvcListBinding> {

    @Override
    public BaseQuickAdapter<NewsListBean, BaseViewHolder> getAdapter() {
        return new NewsListAdapter();
    }

    //第一种写法
    @Override
    protected Observable<BaseListResponse<NewsListBean>> loadDataFromServer(int page) {
        return ApiHelper.getWangYiNewsByField(page, getLoadSize());
    }

    //第二种写法
    //如果只是从服务器获取并展示列表数据，那么采用第一种写法重写loadDataFromServer返回相应的接口即可
    //但如果有需要处理其他逻辑，那么就需要重写loadData，并且在获取到数据后调用showData展示列表数据
//    @Override
//    protected void loadData(int page, boolean showLoadLayout, boolean showLoadingDialog) {
//        ApiHelper.getWangYiNewsByField(page, getLoadSize())
//                .subscribe(new CustomObserver<BaseListResponse<NewsListBean>>(this, false, isFirstLoad()) {
//                    @Override
//                    public void onSuccess(BaseListResponse<NewsListBean> response) {
//                        if (page < 3) {
//                            showData(page, response.getData());
//                        } else {  //模拟数据加载到底
//                            showData(page, new ArrayList<>());
//                        }
//                    }
//                });
//    }

    @Override
    public void handleView(Bundle savedInstanceState) {
        mTitleBar.setTitleText("列表Activity(MVC)");
    }

    @Override
    public void initListener() {
        //Item点击事件
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            NewsListBean bean = mAdapter.getItem(position);
            WangYiNewsWebviewActivity.start(this, "", bean.getPath());
        });
        //Item内子View的点击事件
        mAdapter.addChildClickViewIds(R.id.image_iv);
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (view.getId() == R.id.image_iv) {
                showToast("点击了第" + position + "项的图片");
            }
        });
    }

    @Override
    public int getFirstPage() {
        return 1;
    }

}
