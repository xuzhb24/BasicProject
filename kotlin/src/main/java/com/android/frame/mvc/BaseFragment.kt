package com.android.frame.mvc

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewbinding.ViewBinding

/**
 * Created by xuzhb on 2019/9/7
 * Desc:基类Fragment(MVC)
 */
abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    protected lateinit var binding: VB

    protected var mActivity: FragmentActivity? = null
    protected var mContext: Context? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mActivity = this.activity
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = getViewBinding(inflater, container!!)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleView(savedInstanceState)
        initListener()
    }

    //执行onCreate接下来的逻辑
    abstract fun handleView(savedInstanceState: Bundle?)

    //所有的事件回调均放在该层，如onClickListener等
    abstract fun initListener()

    //获取ViewBinding
    abstract fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    fun startActivity(clazz: Class<*>) {
        val intent = Intent()
        intent.setClass(activity, clazz)
        startActivity(intent)
    }

}