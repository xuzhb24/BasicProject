package com.android.frame.mvc

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.android.base.BaseApplication
import com.android.basicproject.R
import com.android.util.StatusBar.StatusBarUtil
import com.android.util.ToastUtil
import com.android.util.getTopActivityName
import com.android.widget.TitleBar

/**
 * Created by xuzhb on 2019/8/31
 * Desc:基类Activity(MVC)
 */
abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    protected lateinit var binding: VB

    //标题栏，需在布局文件中固定id名为title_bar
    protected var mTitleBar: TitleBar? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewBinding()
        setContentView(binding.root)
        initBaseView()
        initBar()
        handleView(savedInstanceState)
        initListener()
    }

    //初始化一些通用控件，如加载框、SwipeRefreshLayout、网络错误提示布局
    protected open fun initBaseView() {
        mTitleBar = findViewById(R.id.title_bar)
    }

    //实现默认的沉浸式状态栏样式，特殊的Activity可以通过重写该方法改变状态栏样式，如颜色等
    protected open fun initBar() {
        if (mTitleBar != null) {  //如果当前布局包含id为title_bar的标题栏控件，以该控件为基准实现沉浸式状态栏
            StatusBarUtil.darkModeAndPadding(this, mTitleBar!!)
            if (isBarBack()) {
                mTitleBar?.setOnLeftClickListener {
                    finish()
                }
            }
        } else {  //以ContentView为基准实现沉浸式状态栏，颜色是整个布局的背景色
            val content: ViewGroup = findViewById(android.R.id.content)
            StatusBarUtil.darkModeAndPadding(this, content)
        }
    }

    //点击标题栏左侧图标是否退出Activity，默认true
    protected open fun isBarBack(): Boolean = true

    //执行onCreate接下来的逻辑
    abstract fun handleView(savedInstanceState: Bundle?)

    //所有的事件回调均放在该层，如onClickListener等
    abstract fun initListener()

    //获取ViewBinding
    abstract fun getViewBinding(): VB

    fun startActivity(clazz: Class<*>) {
        val intent = Intent()
        intent.setClass(this, clazz)
        startActivity(intent)
    }

    //弹出Toast
    fun showToast(
        text: CharSequence,
        isCenter: Boolean = true,
        longToast: Boolean = false,
        context: Context = BaseApplication.instance
    ) {
        runOnUiThread {
            ToastUtil.showToast(text, isCenter, longToast, context.applicationContext)
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        //屏幕顶部中间区域双击获取当前Activity类名，只在debug环境下有效
        getTopActivityName(this, ev)
        return super.dispatchTouchEvent(ev)
    }
}