package com.android.frame.mvc

import android.app.Activity
import android.webkit.JavascriptInterface
import android.webkit.WebView
import com.android.util.LogUtil
import com.android.widget.PhotoViewer.PhotoViewActivity

/**
 * Created by xuzhb on 2022/12/4
 * Desc:WebView中的图片查看
 */
class ImageInterface constructor(val mActivity: Activity) {

    companion object {
        private const val TAG = "ImageInterface"
        const val INTERFACE_NAME = "imageListener"

        fun imageInject(webView: WebView) {
            webView.loadUrl(
                "javascript:(function()" +
                        "{" + "var objs = document.getElementsByTagName('img'); " +
                        "for(var i=0;i<objs.length;i++)  " + "{" + "var img = objs[i];   " +
                        "img.style.minHeight = '10px'; img.style.maxWidth = '100%';img.style.height='auto';" + "}" + "})()"
            )
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
            )
        }
    }

    private val mImageUrls: ArrayList<String> = arrayListOf()

    @JavascriptInterface
    fun clearImages() {
        LogUtil.i(TAG, "=========================清空图片集合=========================")
        mImageUrls.clear()
    }

    @JavascriptInterface
    fun addImage(imgSrc: String) {
        LogUtil.i(TAG, "添加图片，$imgSrc")
        mImageUrls.add(imgSrc)
    }

    @JavascriptInterface
    fun openImage(imgSrc: String) {
        LogUtil.i(TAG, "打开图片，$imgSrc")
        var position = 0
        for (i in mImageUrls.indices) {
            if (imgSrc == mImageUrls[i]) {
                position = i;
            }
        }
        PhotoViewActivity.start(mActivity, mImageUrls, position)
    }

}