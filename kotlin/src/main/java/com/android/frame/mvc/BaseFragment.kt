package com.android.frame.mvc

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.base.BaseApplication
import com.android.basicproject.R
import com.android.util.StatusBar.StatusBarUtil
import com.android.util.getTopActivityName
import com.android.widget.TitleBar

/**
 * Created by xuzhb on 2019/9/7
 * Desc:基类Fragment(MVC)
 */
abstract class BaseFragment : Fragment() {

    protected var mActivity: FragmentActivity? = null
    protected var mContext: Context? = null
    protected var mRootView: View? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mActivity = this.activity
        mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (mRootView == null) {
            mRootView = inflater.inflate(getLayoutId(), container, false)
        }
        return mRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBar()
        handleView(savedInstanceState)
        initListener()
        getTopActivityName(mActivity!!)
    }

    //实现默认的沉浸式状态栏样式，特殊的Activity可以通过重写该方法改变状态栏样式，如颜色等
    protected open fun initBar() {
        mRootView?.let {
            val titleBar: TitleBar? = it.findViewById(R.id.title_bar)
            if (titleBar != null) {
                StatusBarUtil.darkModeAndPadding(activity as Activity, titleBar)
            } else {
                StatusBarUtil.darkModeAndPadding(activity as Activity, it)
            }
        }
    }

    //执行onCreate接下来的逻辑
    abstract fun handleView(savedInstanceState: Bundle?)

    //所有的事件回调均放在该层，如onClickListener等
    abstract fun initListener()

    //获取布局
    abstract fun getLayoutId(): Int

    override fun onDestroy() {
        //监控内存泄漏
        activity?.let { BaseApplication.getRefWatcher().watch(it) }
        super.onDestroy()
    }

    fun startActivity(clazz: Class<*>) {
        val intent = Intent()
        intent.setClass(activity, clazz)
        startActivity(intent)
    }

}