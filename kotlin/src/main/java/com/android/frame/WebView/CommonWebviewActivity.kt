package com.android.frame.WebView

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import android.view.ViewGroup
import android.view.WindowManager
import android.webkit.*
import android.widget.LinearLayout
import com.android.base.MainActivity
import com.android.basicproject.BuildConfig
import com.android.basicproject.R
import com.android.basicproject.databinding.ActivityCommonWebviewBinding
import com.android.frame.mvc.BaseActivity
import com.android.util.KeyboardUtil
import com.android.util.LogUtil
import com.android.util.StatusBar.StatusBarUtil
import com.android.widget.PicGetterDialog.OnPicGetterListener
import com.android.widget.PicGetterDialog.PicGetterDialog
import kotlinx.android.synthetic.main.activity_common_webview.*
import java.io.File

/**
 * Created by xuzhb on 2019/10/30
 * Desc:H5 Activity基类
 */
open class CommonWebviewActivity : BaseActivity<ActivityCommonWebviewBinding>() {

    companion object {
        private const val TAG = "CommonWebviewActivity"
        private const val EXTRA_TITLE = "EXTRA_TITLE"
        private const val EXTRA_URL = "EXTRA_URL"

        fun start(context: Context, title: String, url: String) {
            val intent = Intent()
            intent.setClass(context, CommonWebviewActivity::class.java)
            intent.putExtra(EXTRA_TITLE, title)
            intent.putExtra(EXTRA_URL, url)
            context.startActivity(intent)
        }
    }

    private var mTitle: String = ""
    private var mUrl: String = ""

    private var mUploadMsg: ValueCallback<Uri>? = null
    private var mFilePathCallback: ValueCallback<Array<Uri>>? = null

    protected var mWebView: WebView? = null

    override fun initBar() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)  //保证WebView的输入框不被软键盘遮挡
        StatusBarUtil.darkMode(this, resources.getColor(R.color.colorPrimaryDark), 1f)
        mTitleBar?.let {
            it.setOnLeftClickListener {
                goPageBack()
            }
            if (BuildConfig.DEBUG) {
                it.setOnLongClickListener {
                    val url = mWebView?.url.toString()
                    KeyboardUtil.copyToClipboard(applicationContext, url)
                    showToast("${url}\n已复制到剪切板！")
                    true
                }
            }
        }
    }

    //返回操作
    protected open fun goPageBack() {
        if (mWebView != null && mWebView!!.canGoBack()) {
            mWebView!!.goBack()
        } else {
            finish()
        }
    }

    override fun handleView(savedInstanceState: Bundle?) {
        mTitle = intent.getStringExtra(EXTRA_TITLE)
        mUrl = intent.getStringExtra(EXTRA_URL)
//        showLoadingDialog()  //显示加载框
        title_bar.titleText = mTitle
        createWebView()
        mWebView!!.loadUrl(mUrl)
        initWebView()
    }

    //创建WebView
    private fun createWebView() {
        val params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        mWebView = WebView(applicationContext)
        with(mWebView!!) {
            layoutParams = params
            overScrollMode = WebView.OVER_SCROLL_NEVER  //取消滑动到边缘时的波纹效果
            isVerticalScrollBarEnabled = false    //不显示垂直滚动条
            isHorizontalScrollBarEnabled = false  //不显示水平滚动条
        }
        web_ll.addView(mWebView)
    }

    //可以通过重写以下方法重新初始化WebView
    protected open fun initWebView() {
        mWebView?.let {
            with(it) {
                settings.javaScriptEnabled = true  //支持javascript
                //解决H5页面中的图片不显示的问题
                //安卓5.0之后，WebView默认不允许加载http与https混合内容，需要设置webview允许其加载混合网络协议内容
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                }
                settings.textZoom = 100            //防止系统字体改变导致H5页面排版错乱
                settings.domStorageEnabled = true  //支持html5的DomStorage
                settings.cacheMode = WebSettings.LOAD_DEFAULT  //设置缓存模式，根据cache-control决定是否从网络上取数据
                //支持缩放
                settings.setSupportZoom(true)
                settings.builtInZoomControls = true
                settings.displayZoomControls = false  //隐藏缩放工具
                settings.useWideViewPort = true       //支持meta标签的viewport属性
                //自适应屏幕
                settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
                settings.loadWithOverviewMode = true
                addJavascriptInterface(JavaScriptMethods(), "androidjs")  //js调用Android的方法
                requestFocus()
                initWebViewClient()
                initWebChromeClient()
            }
        }
    }

    //可以通过重写以下方法改变WebView的setWebViewClient行为
    protected open fun initWebViewClient() {
        mWebView?.let {
            it.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    LogUtil.i(TAG, "url===" + url)
                    url?.let {
                        if (!it.equals("about:blank", ignoreCase = true)) {
                            if (it.startsWith("http://") or it.startsWith("https://")) {
//                                    showLoadingDialog()  //显示加载框
                                view?.loadUrl(url)
                            }
//                                else if (url.equals("yeahkalepos://goback")) {
//                                    goPageBack()
//                                }
                        }
                    }
                    return true
                }

                //页面加载结束时调用
                override fun onPageFinished(view: WebView?, url: String?) {
//                        dismissLoadingDialog()  //取消加载框
                }

                //加载页面的服务器出现错误时调用
                override fun onReceivedError(
                    view: WebView?,
                    errorCode: Int,
                    description: String?,
                    failingUrl: String?
                ) {
//                        dismissLoadingDialog()  //取消加载框
//                        view?.loadUrl("file:///android_asset/html/error.html")
                }

            }
        }
    }

    //可以通过重写以下方法改变WebView的SetWebChromeClient行为
    protected open fun initWebChromeClient() {
        mWebView?.let {
            it.webChromeClient = object : WebChromeClient() {

                //Android 3.0以下
                fun openFileChooser(uploadMsg: ValueCallback<Uri>) {
                    openFileChooser(uploadMsg, "")
                }

                //Android 3.0 - 4.0
                fun openFileChooser(
                    uploadMsg: ValueCallback<Uri>,
                    acceptType: String
                ) {
                    openFileChooser(uploadMsg, acceptType, "")
                }

                //Android 4.0 - 5.0
                fun openFileChooser(
                    uploadMsg: ValueCallback<Uri>,
                    acceptType: String, capture: String
                ) {
                    mUploadMsg = uploadMsg
                    showImageChooseDialog()
                }

                //Android 5.0以上，包括5.0
                override fun onShowFileChooser(
                    webView: WebView?,
                    filePathCallback: ValueCallback<Array<Uri>>?,
                    fileChooserParams: FileChooserParams?
                ): Boolean {
                    mFilePathCallback = filePathCallback
                    showImageChooseDialog()
                    return true
                }

                // 配置权限（同样在WebChromeClient中实现）
                override fun onGeolocationPermissionsShowPrompt(
                    origin: String,
                    callback: GeolocationPermissions.Callback
                ) {
                    callback.invoke(origin, true, false)
                    super.onGeolocationPermissionsShowPrompt(origin, callback)
                }

                //获取网页标题
                override fun onReceivedTitle(view: WebView?, title: String?) {
                    super.onReceivedTitle(view, title)
                    LogUtil.i(TAG, "title===" + title)
                    title_bar.titleText = title
                }
            }
        }
    }

    //选择拍照或相册
    private fun showImageChooseDialog() {
        val dialog = PicGetterDialog()
        dialog.setOnPicGetterListener(object : OnPicGetterListener {

            override fun onSuccess(bitmap: Bitmap?, picPath: String?) {
                if (TextUtils.isEmpty(picPath)) {
                    if (mFilePathCallback != null) {
                        mFilePathCallback!!.onReceiveValue(arrayOf())
                        mFilePathCallback = null
                    } else if (mUploadMsg != null) {
                        mUploadMsg!!.onReceiveValue(null)
                        mUploadMsg = null
                    }
                } else {
                    val result = Uri.fromFile(File(picPath))
                    if (mFilePathCallback != null) {
                        mFilePathCallback!!.onReceiveValue(arrayOf(result))
                        mFilePathCallback = null
                    } else {
                        mUploadMsg!!.onReceiveValue(result)
                        mUploadMsg = null
                    }
                }
            }

            override fun onFailure(errorMsg: String?) {
                dialog.dismiss()
                showToast(errorMsg ?: "发生异常了")
                releaseUploadMessage()
            }

            override fun onCancel() {
                releaseUploadMessage()
            }

        })
        dialog.show(supportFragmentManager)
    }

    private fun releaseUploadMessage() {
        if (mFilePathCallback != null) {
            mFilePathCallback!!.onReceiveValue(null)
            mFilePathCallback = null
        }
        if (mUploadMsg != null) {
            mUploadMsg!!.onReceiveValue(null)
            mUploadMsg = null
        }
    }

    override fun initListener() {

    }

    override fun getViewBinding() = ActivityCommonWebviewBinding.inflate(layoutInflater)

    override fun onDestroy() {
        destroyWebView()
        super.onDestroy()
    }

    //销毁WebView
    private fun destroyWebView() {
        mWebView?.let {
            with(it) {
                //加载null的内容
                loadDataWithBaseURL(null, "", "text/html", "utf-8", null)
                //清除历史记录
                clearHistory()

                //先移除WebView再销毁WebView
                (parent as ViewGroup).removeView(it)
                it.destroy()
            }
            mWebView = null
        }

    }

    //back键控制网页后退
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (event?.action == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK && mWebView!!.canGoBack()) {
                mWebView!!.goBack()
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    inner class JavaScriptMethods {
        //返回首页
        @JavascriptInterface
        fun toHome() {
            startActivity(MainActivity::class.java)
        }
    }

}