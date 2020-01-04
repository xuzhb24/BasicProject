package com.android.frame.mvp

/**
 * Created by xuzhb on 2019/12/29
 * Desc:不需要额外声明View和Presenter的Activity的父类
 */
abstract class CommonBaseActivity : BaseCompatActivity<IBaseView, BasePresenter<IBaseView>>(), IBaseView {
    override fun getPresenter(): BasePresenter<IBaseView> = BasePresenter()
}