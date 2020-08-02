package com.android.frame.mvc

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.android.base.BaseApplication
import com.android.basicproject.BuildConfig
import com.android.basicproject.R
import com.android.util.*
import com.android.util.StatusBar.StatusBarUtil
import com.android.widget.TitleBar

/**
 * Created by xuzhb on 2019/8/31
 * Desc:基类Activity(MVC)
 */
abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initBar()
        handleView(savedInstanceState)
        initListener()
    }

    //实现默认的沉浸式状态栏样式，特殊的Activity可以通过重写该方法改变状态栏样式，如颜色等
    protected open fun initBar() {
        val titleBar: TitleBar? = findViewById(R.id.title_bar)
        if (titleBar != null) {
            StatusBarUtil.darkModeAndPadding(this, titleBar)
        } else {
            val content: ViewGroup = findViewById(android.R.id.content)
            StatusBarUtil.darkModeAndPadding(this, content)
        }
    }

    //执行onCreate接下来的逻辑
    abstract fun handleView(savedInstanceState: Bundle?)

    //所有的事件回调均放在该层，如onClickListener等
    abstract fun initListener()

    //获取布局
    abstract fun getLayoutId(): Int

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
        ev?.let {
            //屏幕顶部中间区域连续点击2次获取当前Activity包名和类名，只在debug环境下有效
            if (BuildConfig.DEBUG && it.action == MotionEvent.ACTION_DOWN &&
                it.rawY < SizeUtil.dp2px(50f) && it.rawX > SizeUtil.dp2px(80f) &&
                it.rawX < ScreenUtil.getScreenWidth(this) - SizeUtil.dp2px(80f)
            ) {
                CheckFastClickUtil.setOnMultiClickListener {
                    if (it == 2) {
                        getTopActivityName(this)
                    }
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }
}