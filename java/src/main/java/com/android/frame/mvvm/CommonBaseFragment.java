package com.android.frame.mvvm;

import android.view.LayoutInflater;

import androidx.lifecycle.ViewModelProvider;
import androidx.viewbinding.ViewBinding;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by xuzhb on 2021/8/9
 * Desc:不需要额外声明ViewModel的Fragment的父类
 */
public abstract class CommonBaseFragment<VB extends ViewBinding> extends BaseFragment<VB, BaseViewModel<VB>> implements IBaseView {

    @Override
    protected void initViewBindingAndViewModel() {
        Type superclass = getClass().getGenericSuperclass();
        Class<VB> vbClass = (Class<VB>) ((ParameterizedType) superclass).getActualTypeArguments()[0];
        try {
            Method method = vbClass.getDeclaredMethod("inflate", LayoutInflater.class);
            binding = (VB) method.invoke(null, getLayoutInflater());
            viewModel = new ViewModelProvider(this).get(BaseViewModel.class);
            viewModel.bind(binding);
            viewModel.observe(this, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
