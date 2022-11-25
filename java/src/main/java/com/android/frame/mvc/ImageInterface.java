package com.android.frame.mvc;

import android.app.Activity;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.android.util.LogUtil;
import com.android.widget.PhotoViewer.PhotoViewActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuzhb on 2022/11/15
 * Desc:WebView中的图片查看
 */
public class ImageInterface {

    private static final String TAG = "ImageInterface";
    public static final String INTERFACE_NAME = "imageListener";

    public static void imageInject(WebView webView) {
        webView.loadUrl(
                "javascript:(function()" +
                        "{" + "var objs = document.getElementsByTagName('img'); " +
                        "for(var i=0;i<objs.length;i++)  " + "{" + "var img = objs[i];   " +
                        "img.style.minHeight = '10px'; img.style.maxWidth = '100%';img.style.height='auto';" + "}" + "})()"
        );
        webView.loadUrl(
                "javascript:(function(){" +
                        "var objs = document.getElementsByTagName('img'); " +
                        "window." + INTERFACE_NAME + ".clearImages();" +
                        "for(var i=0;i<objs.length;i++)  " +
                        "{" + "window." + INTERFACE_NAME + ".addImage(objs[i].src);"
                        + "    objs[i].onclick=function()  " +
                        "    {  "
                        + "        window." + INTERFACE_NAME + ".openImage(this.src);  " +
                        "    }  " +
                        "}" +
                        "})()"
        );
    }

    private final Activity mActivity;
    private final List<String> mImageUrls = new ArrayList<>();

    public ImageInterface(Activity activity) {
        this.mActivity = activity;
    }

    @JavascriptInterface
    public void clearImages() {
        LogUtil.i(TAG, "=========================清空图片集合=========================");
        mImageUrls.clear();
    }

    @JavascriptInterface
    public void addImage(String imgSrc) {
        LogUtil.i(TAG, "添加图片，" + imgSrc);
        mImageUrls.add(imgSrc);
    }

    @JavascriptInterface
    public void openImage(String imgSrc) {
        LogUtil.i(TAG, "打开图片，" + imgSrc);
        int position = 0;
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < mImageUrls.size(); i++) {
            if (TextUtils.equals(imgSrc, mImageUrls.get(i))) {
                position = i;
            }
            list.add(mImageUrls.get(i));
        }
        PhotoViewActivity.start(mActivity, list, position);
    }

}
