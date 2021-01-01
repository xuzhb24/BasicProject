package com.android.frame.mvc;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.GeolocationPermissions;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.android.java.BuildConfig;
import com.android.java.databinding.ActivityWebviewBinding;
import com.android.util.KeyboardUtil;
import com.android.util.LogUtil;
import com.android.util.StatusBar.StatusBarUtil;
import com.android.widget.PicGetterDialog.OnPicGetterListener;
import com.android.widget.PicGetterDialog.PicGetterDialog;

import java.io.File;

/**
 * Created by xuzhb on 2020/12/31
 * Desc:H5 Activity基类
 */
public class WebviewActivity extends BaseActivity<ActivityWebviewBinding> {

    private static final String TAG = "WebviewActivity1";
    private static final String EXTRA_TITLE = "EXTRA_TITLE";
    private static final String EXTRA_URL = "EXTRA_URL";
    private static final String EXTRA_IS_TITLE_FIXED = "EXTRA_IS_TITLE_FIXED";

    private String mTitle;
    private String mUrl;
    private boolean isTitleFixed = false;  //传进来的标题是否固定不变

    private ValueCallback<Uri> mUploadMsg;
    private ValueCallback<Uri[]> mFilePathCallback;

    protected WebView mWebView;
    private boolean hasReceivedError = false;  //是否加载失败

    public static void start(Context context, String title, String url, boolean isTitleFixed) {
        Intent intent = new Intent();
        intent.setClass(context, WebviewActivity.class);
        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_URL, url);
        intent.putExtra(EXTRA_IS_TITLE_FIXED, isTitleFixed);
        context.startActivity(intent);
    }

    @Override
    protected void initBar() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);  //保证WebView的输入框不被软键盘遮挡
        if (mTitleBar != null) {
            StatusBarUtil.darkModeAndPadding(this, mTitleBar);
            mTitleBar.setOnLeftIconClickListener(v -> goPageBack());
            if (BuildConfig.DEBUG) {  //长按复制网页地址
                mTitleBar.setOnLongClickListener(v -> {
                    if (mWebView != null) {
                        String url = mWebView.getUrl();
                        KeyboardUtil.copyToClipboard(getApplicationContext(), url);
                        showToast(url + "\n已复制到剪切板！");
                    }
                    return true;
                });
            }
        }
    }

    //返回操作
    protected void goPageBack() {
        if (mWebView != null && mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            finish();
        }
    }

    @Override
    public void handleView(Bundle savedInstanceState) {
        mTitle = getIntent().getStringExtra(EXTRA_TITLE);
        mUrl = getIntent().getStringExtra(EXTRA_URL);
        isTitleFixed = getIntent().getBooleanExtra(EXTRA_IS_TITLE_FIXED, false);
        mTitleBar.setTitleText(mTitle);  //设置标题
//        showLoadingDialog();  //显示加载框
        showLoadingLayout();
        createWebView();                 //创建WebView
        initWebView();                   //设置WebView属性
        initWebViewClient();             //设置WebViewClient
        initWebChromeClient();           //设置SetWebChromeClient
    }

    @Override
    protected void refreshData() {
        if (!TextUtils.isEmpty(mUrl)) {
            mWebView.loadUrl(mUrl);  //加载网页
        }
    }

    //创建WebView
    private void createWebView() {
        mWebView = new WebView(getApplicationContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mWebView.setLayoutParams(params);
        mWebView.setOverScrollMode(WebView.OVER_SCROLL_NEVER);  //取消滑动到边缘时的波纹效果
        mWebView.setVerticalScrollBarEnabled(false);    //不显示垂直滚动条
        mWebView.setHorizontalScrollBarEnabled(false);  //不显示水平滚动条
        binding.webLl.addView(mWebView);
    }

    //可以通过重写以下方法重新初始化WebView
    protected void initWebView() {
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);  //支持javascript
        //解决H5页面中的图片不显示的问题
        //安卓5.0之后，WebView默认不允许加载http与https混合内容，需要设置webview允许其加载混合网络协议内容
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        settings.setTextZoom(100);            //防止系统字体改变导致H5页面排版错乱
        settings.setDomStorageEnabled(true);  //支持html5的DomStorage
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);  //设置缓存模式，根据cache-control决定是否从网络上取数据
        //支持缩放
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);  //隐藏缩放工具
        settings.setUseWideViewPort(true);       //支持meta标签的viewport属性
        //自适应屏幕
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setLoadWithOverviewMode(true);
//        mWebView.addJavascriptInterface(new JavaScriptMethods(), "androidjs");  //js调用Android的方法
        mWebView.requestFocus();
    }

    //可以通过重写以下方法改变WebView的setWebViewClient行为
    protected void initWebViewClient() {
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                LogUtil.i(TAG, "shouldOverrideUrlLoading,url:" + url);
                if (!"about:blank".equalsIgnoreCase(url)) {
                    if (url.startsWith("http://") || url.startsWith("https://")) {
                        view.loadUrl(url);
                    }
                }
                return true;
            }

            //页面开始加载时调用
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                LogUtil.i(TAG, "onPageStarted,url:" + url);
                hasReceivedError = false;
            }

            //加载页面的服务器出现错误时调用
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                LogUtil.i(TAG, "onReceivedError,error:" + error);
                hasReceivedError = true;
            }

            //页面加载结束时调用
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                LogUtil.i(TAG, "onPageFinished,url:" + url);
                loadFinish(hasReceivedError);
            }

        });
    }

    //可以通过重写以下方法改变WebView的SetWebChromeClient行为
    protected void initWebChromeClient() {
        mWebView.setWebChromeClient(new WebChromeClient() {

            //Android 3.0以下
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                openFileChooser(uploadMsg, "");
            }

            //Android 3.0 - 4.0
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                openFileChooser(uploadMsg, acceptType, "");
            }

            //Android 4.0 - 5.0
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                mUploadMsg = uploadMsg;
                showImageChooseDialog();
            }

            //Android 5.0以上，包括5.0
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                mFilePathCallback = filePathCallback;
                showImageChooseDialog();
                return true;
            }

            // 配置权限（同样在WebChromeClient中实现）
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                LogUtil.i(TAG, "newProgress：" + newProgress);
            }

            //获取网页标题
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                LogUtil.i(TAG, "title：" + title + "，isTitleFixed：" + isTitleFixed);
                if (!isTitleFixed) {
                    mTitleBar.setTitleText(title);
                }
            }
        });
    }

    //拍照或从相册选取照片
    private void showImageChooseDialog() {
        PicGetterDialog dialog = new PicGetterDialog();
        dialog.setOnPicGetterListener(new OnPicGetterListener() {
            @Override
            public void onSuccess(Bitmap bitmap, String picPath) {
                if (TextUtils.isEmpty(picPath)) {
                    if (mFilePathCallback != null) {
                        mFilePathCallback.onReceiveValue(new Uri[]{});
                        mFilePathCallback = null;
                    } else if (mUploadMsg != null) {
                        mUploadMsg.onReceiveValue(null);
                        mUploadMsg = null;
                    }
                } else {
                    Uri result = Uri.fromFile(new File(picPath));
                    if (mFilePathCallback != null) {
                        mFilePathCallback.onReceiveValue(new Uri[]{result});
                        mFilePathCallback = null;
                    } else {
                        mUploadMsg.onReceiveValue(result);
                        mUploadMsg = null;
                    }
                }
            }

            @Override
            public void onFailure(String errorMsg) {
                dialog.dismiss();
                showToast(!TextUtils.isEmpty(errorMsg) ? errorMsg : "发生异常了");
                releaseUploadMessage();
            }

            @Override
            public void onCancel() {
                releaseUploadMessage();
            }
        });
        dialog.show(getSupportFragmentManager());
    }

    private void releaseUploadMessage() {
        if (mFilePathCallback != null) {
            mFilePathCallback.onReceiveValue(null);
            mFilePathCallback = null;
        }
        if (mUploadMsg != null) {
            mUploadMsg.onReceiveValue(null);
            mUploadMsg = null;
        }
    }

    @Override
    public void initListener() {

    }

    @Override
    public ActivityWebviewBinding getViewBinding() {
        return ActivityWebviewBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void onDestroy() {
        destroyWebView();
        super.onDestroy();
    }

    //销毁WebView
    private void destroyWebView() {
        if (mWebView != null) {
            //加载null的内容
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            //清除历史记录
            mWebView.clearHistory();

            //先移除WebView再销毁WebView
            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.destroy();
            mWebView = null;
        }
    }

    //back键控制网页后退
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK && mWebView != null && mWebView.canGoBack()) {
                mWebView.goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    //js调用Android的方法
//    private class JavaScriptMethods {
//        @JavascriptInterface
//        public void toHome() {
//            startActivity(MainActivity.class);
//        }
//    }

}
