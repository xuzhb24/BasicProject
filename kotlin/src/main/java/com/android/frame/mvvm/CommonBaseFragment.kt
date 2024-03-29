package com.android.frame.mvvm

import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType

/**
 * Created by xuzhb on 2021/8/7
 * Desc:不需要额外声明ViewModel的Fragment的父类
 */
abstract class CommonBaseFragment<VB : ViewBinding> : BaseFragment<VB, BaseViewModel>(), IBaseView {

    override fun initViewBindingAndViewModel() {
        val superclass = javaClass.genericSuperclass
        val vbClass = (superclass as ParameterizedType).actualTypeArguments[0] as Class<VB>
        val method = vbClass.getDeclaredMethod("inflate", LayoutInflater::class.java)
        binding = method.invoke(null, layoutInflater) as VB
        viewModel = ViewModelProvider(this).get((BaseViewModel())::class.java)
    }

}