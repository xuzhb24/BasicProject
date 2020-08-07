package com.android.frame.mvc.viewBinding

import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewbinding.ViewBinding
import com.android.base.BaseApplication
import com.android.basicproject.R
import com.android.util.NetReceiver
import com.android.util.NetworkUtil
import com.android.util.StatusBar.StatusBarUtil
import com.android.util.ToastUtil
import com.android.util.getTopActivityName
import com.android.widget.LoadingDialog
import com.android.widget.TitleBar
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Created by xuzhb on 2020/4/13
 * Desc:基类Activity(MVC结合ViewBinding)
 */
abstract class BaseActivity_VB<VB : ViewBinding> : AppCompatActivity(),
    IBaseView, SwipeRefreshLayout.OnRefreshListener {

    protected lateinit var binding: VB

    //防止RxJava内存泄漏
    private var mCompositeDisposable = CompositeDisposable()

    //加载框
    private var mLoadingDialog: LoadingDialog? = null

    //标题栏，需在布局文件中固定id名为title_bar
    protected var mTitleBar: TitleBar? = null;

    //通用的下拉刷新组件，需在布局文件中固定id名为swipe_refresh_layout
    protected var mSwipeRefreshLayout: SwipeRefreshLayout? = null

    //通用的RecyclerView组件，需在布局文件中固定id名为R.id.recycler_view
    protected var mRecyclerView: RecyclerView? = null

    //网路异常的布局
    private var mNetErrorFl: FrameLayout? = null
    private var mNetReceiver: NetReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewBinding()
        setContentView(binding.root)
        BaseApplication.instance.addActivity(this)
        initBar()
        initBaseView()
        initNetReceiver()
        handleView(savedInstanceState)
        initListener()
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

    //初始化一些通用控件，如加载框、SwipeRefreshLayout、网络错误提示布局
    protected open fun initBaseView() {
        mLoadingDialog = LoadingDialog(this, R.style.LoadingDialogStyle)
        //获取布局中的SwipeRefreshLayout组件，重用BaseCompatActivity的下拉刷新逻辑
        //注意布局中SwipeRefreshLayout的id命名为swipe_refresh_layout，否则mSwipeRefreshLayout为null
        //如果SwipeRefreshLayout里面只包含RecyclerView，可引用<include layout="@layout/layout_recycler_view" />
        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout)
        //如果当前布局文件不包含id为swipe_refresh_layout的组件则不执行下面的逻辑
        mSwipeRefreshLayout?.let {
            it.setOnRefreshListener(this)
            it.setColorSchemeColors(resources.getColor(R.color.colorAccent))
        }
        //获取布局中的RecyclerView组件，注意布局中RecyclerView的id命名为recycler_view，否则mRecyclerView为null
        mRecyclerView = findViewById(R.id.recycler_view)
        //在当前布局的合适位置引用<include layout="@layout/layout_net_error" />，则当网络出现错误时会进行相应的提示
        mNetErrorFl = findViewById(R.id.net_error_fl)
    }

    //执行onCreate接下来的逻辑
    abstract fun handleView(savedInstanceState: Bundle?)

    //所有的事件回调均放在该层，如onClickListener等
    abstract fun initListener()

    //获取ViewBinding
    abstract fun getViewBinding(): VB

    //下拉刷新
    protected open fun refreshData() {

    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterNetReceiver()
        //销毁加载框
        mLoadingDialog?.dismiss()
        mLoadingDialog = null

        //取消所有正在执行的订阅
        mCompositeDisposable.clear()

        BaseApplication.instance.removeActivity(this)
    }

    //显示加载框
    override fun showLoading(message: String, cancelable: Boolean) {
        runOnUiThread {
            mLoadingDialog?.show(message, cancelable)
        }
    }

    //取消加载框
    override fun dismissLoading() {
        runOnUiThread {
            mLoadingDialog?.dismiss()
        }
    }

    //显示Toast
    override fun showToast(text: CharSequence, isCenter: Boolean, longToast: Boolean) {
        runOnUiThread {
            ToastUtil.showToast(text, isCenter, longToast)
        }
    }

    //数据加载失败
    override fun loadFail() {
        showNetErrorLayout()
    }

    //数据加载完成，收起下拉刷新组件SwipeRefreshLayout的刷新头部
    override fun loadFinish() {
        //如果布局文件中不包含id为swipe_refresh_layout的控件，则swipeRefreshLayout为null
        runOnUiThread {
            mSwipeRefreshLayout?.let {
                if (it.isRefreshing) {
                    it.isRefreshing = false  //停止刷新
                }
            }
        }
    }

    //跳转到登录界面
    override fun gotoLogin() {
        BaseApplication.instance.finishAllActivities()
        val intent = Intent()
        intent.setAction("登录页的action")
        startActivity(intent)
    }

    //RxJava建立订阅关系，方便Activity销毁时取消订阅关系防止内存泄漏
    override fun addDisposable(d: Disposable) {
        mCompositeDisposable.add(d)
    }

    //下拉刷新
    override fun onRefresh() {
        refreshData()     //重新加载数据
        dismissLoading()  //下拉时就不显示加载框了
    }

    //网络断开连接提示
    fun showNetErrorLayout() {
        //如果当前布局文件中不包含layout_net_error则netErrorFl为null，此时不执行下面的逻辑
        runOnUiThread {
            mNetErrorFl?.visibility =
                if (NetworkUtil.isConnected(applicationContext)) View.GONE else View.VISIBLE
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
        startActivityForResult(intent, requestCode)
    }

    //注册广播动态监听网络变化
    private fun initNetReceiver() {
        //如果不含有layout_net_error，则不注册广播
        if (mNetErrorFl == null) {
            return
        }
        //动态注册，Android 7.0之后取消了静态注册方式
        val filter = IntentFilter()
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        mNetReceiver = NetReceiver()
        registerReceiver(mNetReceiver, filter)
        mNetReceiver!!.setOnNetChangeListener {
            showNetErrorLayout()
        }
    }

    //注销广播
    private fun unregisterNetReceiver() {
        if (mNetErrorFl == null) {
            return
        }
        unregisterReceiver(mNetReceiver)
        mNetReceiver = null
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        //屏幕顶部中间区域双击获取当前Activity类名，只在debug环境下有效
        getTopActivityName(this, ev)
        return super.dispatchTouchEvent(ev)
    }
}