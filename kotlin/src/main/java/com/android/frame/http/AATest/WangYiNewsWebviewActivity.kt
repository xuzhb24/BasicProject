package com.android.frame.http.AATest

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.webkit.*
import android.widget.LinearLayout
import com.android.base.MainActivity
import com.android.basicproject.BuildConfig
import com.android.basicproject.R
import com.android.frame.mvc.BaseActivity
import com.android.util.KeyboardUtil
import com.android.util.LogUtil
import com.android.util.StatusBarUtil
import kotlinx.android.synthetic.main.layout_common_webview.*

/**
 * Created by xuzhb on 2020/1/4
 * Desc:
 */
class WangYiNewsWebviewActivity : BaseActivity() {

    companion object {
        private const val TAG = "WangYiNewsWebviewActivity"
        private const val EXTRA_TITLE = "EXTRA_TITLE"
        private const val EXTRA_URL = "EXTRA_URL"

        fun start(context: Context, title: String, url: String) {
            val intent = Intent()
            intent.setClass(context, WangYiNewsWebviewActivity::class.java)
            intent.putExtra(EXTRA_TITLE, title)
            intent.putExtra(EXTRA_URL, url)
            context.startActivity(intent)
        }
    }

    private var mTitle: String = ""
    private var mUrl: String = ""

    private var mUploadMessageDown: ValueCallback<Uri>? = null
    private var mUploadMessageUp: ValueCallback<Array<Uri>>? = null

    protected var mWebView: WebView? = null

    override fun initBar() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)  //保证WebView的输入框不被软键盘遮挡
        StatusBarUtil.darkMode(this, resources.getColor(R.color.colorPrimaryDark), 1f)
    }

    override fun handleView(savedInstanceState: Bundle?) {
        mTitle = intent.getStringExtra(EXTRA_TITLE)
        mUrl = intent.getStringExtra(EXTRA_URL)
//        showLoadingDialog()
        title_bar.titleText = mTitle
        createWebView()
        mWebView!!.loadUrl(mUrl)
        initWebView()
    }

    override fun initListener() {
        title_bar.setOnLeftClickListener {
            goPageBack()
        }
        if (BuildConfig.DEBUG) {
            title_bar.setOnLongClickListener {
                val url = mWebView?.url.toString()
                KeyboardUtil.copyToClipboard(applicationContext, url)
                showToast("${url}\n已复制到剪切板！")
                true
            }
        }
    }

    override fun getLayoutId(): Int = R.layout.layout_common_webview

    override fun onDestroy() {
        destroyWebView()
        super.onDestroy()
    }

    //创建WebView
    private fun createWebView() {
        val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        mWebView = WebView(applicationContext)
        with(mWebView!!) {
            layoutParams = params
            overScrollMode = WebView.OVER_SCROLL_NEVER
            isVerticalScrollBarEnabled = false //不显示垂直滚动条
            isHorizontalFadingEdgeEnabled = false  //不显示水平滚动条
        }
        web_ll.addView(mWebView)
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

    //可以通过重写以下方法重新初始化WebView
    protected open fun initWebView() {
        mWebView?.let {
            with(it) {
                settings.javaScriptEnabled = true
                //解决H5页面中的图片不显示的问题
                //安卓5.0之后，WebView默认不允许加载http与https混合内容，需要设置webview允许其加载混合网络协议内容
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                }
                addJavascriptInterface(JavaScriptMethods(), "androidjs")
                requestFocus()
                settings.textZoom = 100  //防止系统字体改变导致H5页面排版错乱
                settings.domStorageEnabled = true
                settings.cacheMode = WebSettings.LOAD_DEFAULT
                scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
                webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                        LogUtil.i(TAG, "url===" + url)
                        url?.let {
                            if (!it.equals("about:blank", ignoreCase = true)) {
                                if (it.startsWith("http://") or it.startsWith("https://")) {
//                                    showLoadingDialog()
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
//                        dismissLoadingDialog()
                    }

                    //加载页面的服务器出现错误时调用
                    override fun onReceivedError(
                        view: WebView?,
                        errorCode: Int,
                        description: String?,
                        failingUrl: String?
                    ) {
//                        dismissLoadingDialog()
//                        view?.loadUrl("file:///android_asset/html/error.html")
                    }

                }
                initWebChromeClient()
            }
        }
    }

    //可以通过重写以下方法改变WebView的SetWebChromeClient行为
    protected open fun initWebChromeClient() {
        mWebView?.let {
            it.webChromeClient = object : WebChromeClient() {

                //Android 3.0+
                fun openFileChooser(uploadMsg: ValueCallback<Uri>) {
                    mUploadMessageDown = uploadMsg
                    showImageChooseDialog()
                }

                //Android 3.0+
                fun openFileChooser(
                    uploadMsg: ValueCallback<Uri>,
                    acceptType: String
                ) {
                    mUploadMessageDown = uploadMsg
                    showImageChooseDialog()
                }

                //Android 4.1
                fun openFileChooser(
                    uploadMsg: ValueCallback<Uri>,
                    acceptType: String, capture: String
                ) {
                    mUploadMessageDown = uploadMsg
                    showImageChooseDialog()
                }

                //Android 5.0以上，包括5.0
                override fun onShowFileChooser(
                    webView: WebView?,
                    filePathCallback: ValueCallback<Array<Uri>>?,
                    fileChooserParams: FileChooserParams?
                ): Boolean {
                    mUploadMessageUp = filePathCallback
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
//        val dialog = PicChooseDialog()
//        dialog.setOnPicGetterListener(object : OnPicGetterListener {
//            override fun onSuc(bitmap: Bitmap?, picPath: String?) {
//                if (picPath == null) {
//                    if (mUploadMessageUp != null) {
//                        mUploadMessageUp!!.onReceiveValue(arrayOf())
//                        mUploadMessageUp = null
//                    } else if (mUploadMessageDown != null) {
//                        mUploadMessageDown!!.onReceiveValue(null)
//                        mUploadMessageDown = null
//                    }
//                } else {
//                    val result = Uri.fromFile(File(picPath))
//                    if (mUploadMessageUp != null) {
//                        mUploadMessageUp!!.onReceiveValue(arrayOf(result))
//                        mUploadMessageUp = null
//                    } else {
//                        mUploadMessageDown!!.onReceiveValue(result)
//                        mUploadMessageDown = null
//                    }
//                }
//            }
//
//            override fun onFail(errorCode: Int, errorMsg: String?) {
//                dialog.dismiss()
//                showErrorToast(errorMsg ?: "发生异常了！")
//                releaseUploadMessage()
//            }
//
//            override fun onCancel() {
//                releaseUploadMessage()
//            }
//
//        })
//        dialog.show(fragmentManager, PicChooseDialog::class.java.simpleName)
    }

    private fun releaseUploadMessage() {
        if (mUploadMessageUp != null) {
            mUploadMessageUp!!.onReceiveValue(null)
            mUploadMessageUp = null
        }
        if (mUploadMessageDown != null) {
            mUploadMessageDown!!.onReceiveValue(null)
            mUploadMessageDown = null
        }
    }

    //返回操作
    private fun goPageBack() {
        finish()
    }

    inner class JavaScriptMethods {
        //返回首页
        @JavascriptInterface
        fun toHome() {
            runOnUiThread {
                startActivity(MainActivity::class.java)
            }
        }
    }

}