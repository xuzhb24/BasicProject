package com.android.frame.mvp;

/**
 * Created by xuzhb on 2020/1/8
 * Desc:不需要额外声明View和Presenter的Activity的父类
 */
public abstract class CommonBaseActivity extends BaseCompatActivity<IBaseView, BasePresenter<IBaseView>> implements IBaseView {

    @Override
    public BasePresenter<IBaseView> getPresenter() {
        return new BasePresenter<>();
    }
}
