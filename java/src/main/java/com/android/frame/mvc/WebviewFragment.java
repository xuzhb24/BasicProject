package com.android.frame.mvc;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.android.java.databinding.FragmentWebviewBinding;
import com.android.util.LogUtil;
import com.android.widget.PicGetterDialog.OnPicGetterListener;
import com.android.widget.PicGetterDialog.PicGetterDialog;

import java.io.File;

/**
 * Created by xuzhb on 2020/12/31
 * Desc:H5 Fragment基类
 */
public class WebviewFragment extends BaseFragment<FragmentWebviewBinding> {

    private static final String TAG = "WebviewFragment";
    private static final String EXTRA_URL = "EXTRA_URL";

    //通过这种方式构建，可以先添加WebView，后续再通过loadUrl方法加载网页
    public static WebviewFragment newInstance() {
        return new WebviewFragment();
    }

    //通过这种方式构建，添加WebView时就开始加载网页
    public static WebviewFragment newInstance(String url) {
        WebviewFragment fragment = new WebviewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_URL, url);
        fragment.setArguments(bundle);
        return fragment;
    }

    private String mUrl;

    private ValueCallback<Uri> mUploadMsg;
    private ValueCallback<Uri[]> mFilePathCallback;

    protected WebView mWebView;
    private boolean hasReceivedError = false;  //是否加载失败

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getArguments() != null) {
            mUrl = getArguments().getString(EXTRA_URL);
        }
    }

    @Override
    public void handleView(Bundle savedInstanceState) {
        createWebView();        //创建WebView
        initWebView();          //设置WebView属性
        initWebViewClient();    //设置WebViewClient
        initWebChromeClient();  //设置SetWebChromeClient
    }

    //如果网页没有加载成功，每次可见时再重新加载一次
    @Override
    public void onResume() {
        super.onResume();
        if (hasReceivedError) {
            refreshData();
        }
    }

    @Override
    protected void refreshData() {
        if (!TextUtils.isEmpty(mUrl)) {
//            showLoadingDialog();  //显示加载框
            showLoadingLayout();
            mWebView.loadUrl(mUrl);  //加载网页
        }
    }

    //加载指定网页
    public void loadUrl(String url) {
        mUrl = url;
        refreshData();
    }

    //创建WebView
    private void createWebView() {
        mWebView = new WebView(mContext.getApplicationContext());
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
                LogUtil.i(TAG, "title：" + title);
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
        dialog.show(getChildFragmentManager());
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
    public FragmentWebviewBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentWebviewBinding.inflate(inflater, container, false);
    }

    @Override
    public void onDestroy() {
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

    //js调用Android的方法
//    private class JavaScriptMethods {
//        @JavascriptInterface
//        public void toHome() {
//            startActivity(MainActivity.class);
//        }
//    }

}
