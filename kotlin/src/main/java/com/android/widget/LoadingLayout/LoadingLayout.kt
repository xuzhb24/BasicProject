package com.android.widget.LoadingLayout

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.AttrRes
import com.android.basicproject.R

/**
 * Created by xuzhb on 2020/7/18
 * Desc:加载状态布局
 */
class LoadingLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, @AttrRes defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val STATE_LOADING = 0  //加载中
        private const val STATE_EMPTY = 1    //无数据
        private const val STATE_FAIL = 2     //加载失败
        private const val STATE_HIDE = 3     //隐藏
    }

    var loadingSrc: Drawable? = null  //加载中的图片
        set(value) {
            field = value
            mLoadingIv.setImageDrawable(value)
        }
    var loadingDesc: String = ""  //加载中的文本描述
        set(value) {
            field = value
            setDescText(mDescTv, value)
        }
    var emptySrc: Drawable? = null  //无数据的图片
        set(value) {
            field = value
            mEmptyIv.setImageDrawable(value)
        }
    var emptyDesc: String = ""  //无数据的文本描述
        set(value) {
            field = value
            setDescText(mDescTv, value)
        }
    var failSrc: Drawable? = null  //加载失败的图片
        set(value) {
            field = value
            mFailIv.setImageDrawable(value)
        }
    var failDesc: String = ""  //加载失败的文本描述
        set(value) {
            field = value
            setDescText(mDescTv, value)
        }
    var retryDesc: String = ""  //重试的文本描述
        set(value) {
            field = value
            setDescText(mDescTv, value)
        }

    private val mLoadingIv: ImageView
    private val mEmptyIv: ImageView
    private val mFailIv: ImageView
    private val mDescTv: TextView
    private val mRetryTv: TextView

    init {
        val layout = LayoutInflater.from(context).inflate(R.layout.layout_loading, this)
        with(layout) {
            mLoadingIv = findViewById(R.id.loading_iv)
            mEmptyIv = findViewById(R.id.empty_iv)
            mFailIv = findViewById(R.id.fail_iv)
            mDescTv = findViewById(R.id.desc_tv)
            mRetryTv = findViewById(R.id.retry_tv)
        }
        attrs?.let {
            val ta = context.obtainStyledAttributes(it, R.styleable.LoadingLayout)
            loadingSrc = ta.getDrawable(R.styleable.LoadingLayout_loadingSrc)
                ?: resources.getDrawable(R.drawable.ic_loading)
            loadingDesc = ta.getString(R.styleable.LoadingLayout_loadingDesc) ?: ""
            emptySrc = ta.getDrawable(R.styleable.LoadingLayout_emptySrc)
                ?: resources.getDrawable(R.drawable.ic_empty_data)
            emptyDesc = ta.getString(R.styleable.LoadingLayout_emptyDesc) ?: ""
            failSrc = ta.getDrawable(R.styleable.LoadingLayout_failSrc)
                ?: resources.getDrawable(R.drawable.ic_fail)
            failDesc = ta.getString(R.styleable.LoadingLayout_failDesc) ?: ""
            retryDesc = ta.getString(R.styleable.LoadingLayout_retryDesc) ?: ""
            ta.recycle()
        }
        loadStart()
    }

    //开始加载
    fun loadStart() {
        setLoadState(STATE_LOADING)
    }

    //无数据
    fun loadEmpty() {
        setLoadState(STATE_EMPTY)
    }

    //加载失败
    fun loadFail() {
        setLoadState(STATE_FAIL)
    }

    //加载完成，则隐藏
    fun loadComplete() {
        setLoadState(STATE_HIDE)
    }

    private fun setLoadState(loadState: Int) {
        if (loadState == STATE_HIDE) {
            visibility = View.GONE
        } else {
            visibility = View.VISIBLE
            mLoadingIv.clearAnimation()  //取消动画
            mLoadingIv.visibility = View.GONE
            mEmptyIv.visibility = View.GONE
            mFailIv.visibility = View.GONE
            mRetryTv.visibility = View.GONE
            if (loadState == STATE_LOADING) {
                mLoadingIv.visibility = View.VISIBLE
                mLoadingIv.setImageDrawable(loadingSrc)
                val animation = RotateAnimation(
                    0f, 359f,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
                )
                mLoadingIv.startAnimation(animation.apply {
                    duration = 1500
                    repeatCount = Animation.INFINITE
                    repeatMode = Animation.RESTART
                    interpolator = LinearInterpolator()
                })
                setDescText(mDescTv, loadingDesc)
            } else if (loadState == STATE_EMPTY) {
                mEmptyIv.visibility = View.VISIBLE
                mEmptyIv.setImageDrawable(emptySrc)
                setDescText(mDescTv, emptyDesc)
            } else if (loadState == STATE_FAIL) {
                mFailIv.visibility = View.VISIBLE
                mFailIv.setImageDrawable(failSrc)
                setDescText(mDescTv, failDesc)
                setDescText(mRetryTv, retryDesc)
            }
        }
    }

    private fun setDescText(tv: TextView, desc: String) {
        if (TextUtils.isEmpty(desc)) {
            tv.visibility = View.GONE
        } else {
            tv.visibility = View.VISIBLE
            tv.text = desc
        }
    }

    fun setOnRetryListener(listener: () -> Unit) {
        mRetryTv.setOnClickListener { listener.invoke() }
    }

}