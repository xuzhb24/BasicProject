package com.android.frame.mvc.viewBinding

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import android.viewbinding.ViewBinding
import com.android.base.BaseApplication
import com.android.basicproject.R
import com.android.util.StatusBar.StatusBarUtil
import com.android.util.ToastUtil
import com.android.util.getTopActivityName
import com.android.widget.TitleBar
import com.google.gson.Gson

/**
 * Created by xuzhb on 2020/4/13
 * Desc:基类Activity(MVC结合ViewBinding)
 */
abstract class BaseActivity_VB<VB : ViewBinding> : AppCompatActivity() {

    protected val gson = Gson()
    protected lateinit var binding: VB

    //标题栏，需在布局文件中固定id名为title_bar
    protected var mTitleBar: TitleBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewBinding()
        setContentView(binding.root)
        BaseApplication.instance.addActivity(this)
        initBar()
        handleView(savedInstanceState)
        initListener()
        getTopActivityName(this)
    }

    //实现默认的沉浸式状态栏样式，特殊的Activity可以通过重写该方法改变状态栏样式，如颜色等
    protected open fun initBar() {
        mTitleBar = findViewById(R.id.title_bar)
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

    //获取ViewBinding，其实可以通过反射获取，只是运行时反射影响性能
    abstract fun getViewBinding(): VB

    //通过反射获取ViewBinding
//    private fun getViewBinding(): VB? {
//        val type = javaClass.genericSuperclass
//        val clazz = (type as ParameterizedType).actualTypeArguments[0] as Class<VB>
//        val method = clazz.getMethod("inflate", LayoutInflater::class.java)
//        return method.invoke(null, layoutInflater) as VB
//    }

    override fun onDestroy() {
        super.onDestroy()
        BaseApplication.instance.removeActivity(this)
        //监控内存泄漏
        BaseApplication.getRefWatcher().watch(this)
    }

    //显示Toast
    fun showToast(text: CharSequence, isCenter: Boolean = true, longToast: Boolean = false) {
        runOnUiThread {
            ToastUtil.showToast(text, isCenter, longToast)
        }
    }

    //启动指定的Activity
    protected fun startActivity(clazz: Class<*>, extras: Bundle? = null) {
        val intent = Intent()
        extras?.let {
            intent.putExtras(it)
        }
        intent.setClass(this, clazz)
        startActivity(intent)
    }

    //启动指定的Activity并接收返回的结果
    protected fun startActivityForResult(
        clazz: Class<*>,
        requestCode: Int,
        extras: Bundle? = null
    ) {
        val intent = Intent()
        extras?.let {
            intent.putExtras(it)
        }
        intent.setClass(this, clazz)
        startActivityForResult(clazz, requestCode)
    }

    //跳转到登录界面
    fun gotoLogin() {
        BaseApplication.instance.finishAllActivities()
        val intent = Intent()
        intent.setAction("登录页的action")
        startActivity(intent)
    }

}