package com.android.frame.mvvm;

import android.view.LayoutInflater;

import androidx.lifecycle.ViewModelProvider;
import androidx.viewbinding.ViewBinding;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by xuzhb on 2021/8/9
 * Desc:不需要额外声明ViewModel的Activity的父类
 */
public abstract class CommonBaseActivity<VB extends ViewBinding> extends BaseActivity<VB, BaseViewModel> implements IBaseView {

    @Override
    protected void initViewBindingAndViewModel() {
        Type superclass = getClass().getGenericSuperclass();
        Class<VB> vbClass = (Class<VB>) ((ParameterizedType) superclass).getActualTypeArguments()[0];
        try {
            Method method = vbClass.getDeclaredMethod("inflate", LayoutInflater.class);
            binding = (VB) method.invoke(null, getLayoutInflater());
            viewModel = new ViewModelProvider(this).get(BaseViewModel.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
