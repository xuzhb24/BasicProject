package com.android.frame.mvp

import androidx.viewbinding.ViewBinding

/**
 * Created by xuzhb on 2019/12/29
 * Desc:不需要额外声明View和Presenter的Fragment的父类
 */
abstract class CommonBaseFragment<VB : ViewBinding> : BaseFragment<VB, IBaseView, BasePresenter<IBaseView>>(), IBaseView {
    override fun getPresenter(): BasePresenter<IBaseView> = BasePresenter()
}