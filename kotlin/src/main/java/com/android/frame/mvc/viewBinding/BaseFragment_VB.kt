package com.android.frame.mvc.viewBinding

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.viewbinding.ViewBinding
import com.android.base.BaseApplication
import com.android.util.ToastUtil
import com.google.gson.Gson

/**
 * Created by xuzhb on 2020/4/13
 * Desc:基类Fragment(MVC结合ViewBinding)
 */
abstract class BaseFragment_VB<VB : ViewBinding> : Fragment() {

    protected val gson = Gson()
    protected lateinit var binding: VB
    private var hasCreateView = false

    protected var mActivity: FragmentActivity? = null
    protected var mContext: Context? = null

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        //第一次加载时，setUserVisibleHint会比onAttach先回调，此时布局为null;
        //之后切换Fragment时，当Fragment变得可见或可见时都会回调setUserVisibleHint，此时布局不为null
        if (hasCreateView) {
            if (isVisibleToUser) {
                onVisible()
            } else {
                onInvisible()
            }
        }
    }

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
        hasCreateView = true
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

    //页面可见且布局不为null时回调
    protected open fun onVisible() {

    }

    //页面不可见且布局不为null时回调
    protected open fun onInvisible() {

    }

    //显示Toast
    fun showToast(text: CharSequence, isCenter: Boolean = true, longToast: Boolean = false) {
        activity?.runOnUiThread {
            ToastUtil.showToast(text, isCenter, longToast)
        }
    }

    //启动指定的Activity
    protected fun startActivity(clazz: Class<*>, extras: Bundle? = null) {
        activity?.let {
            val intent = Intent()
            extras?.let {
                intent.putExtras(it)
            }
            intent.setClass(it, clazz)
            startActivity(intent)
        }
    }

    //启动指定的Activity并接收返回的结果
    protected fun startActivityForResult(
        clazz: Class<*>,
        requestCode: Int,
        extras: Bundle? = null
    ) {
        activity?.let {
            val intent = Intent()
            extras?.let {
                intent.putExtras(it)
            }
            intent.setClass(it, clazz)
            startActivityForResult(clazz, requestCode)
        }
    }

    //跳转到登录界面
    fun gotoLogin() {
        BaseApplication.instance.finishAllActivities()
        val intent = Intent()
        intent.setAction("登录页的action")
        startActivity(intent)
    }

}