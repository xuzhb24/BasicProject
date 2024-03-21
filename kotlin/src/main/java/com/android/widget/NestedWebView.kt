package com.android.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewParent
import android.webkit.WebView
import android.widget.AbsListView
import android.widget.GridView
import android.widget.HorizontalScrollView
import android.widget.ScrollView
import androidx.annotation.AttrRes
import androidx.core.widget.NestedScrollView
import androidx.viewpager.widget.ViewPager

/**
 * Created by xuzhb on 2024/3/21
 * Desc:解决Webview和ScrollView等滚动视图的滑动冲突，解决思路：
 * 1、首先响应WebView的滑动事件
 * 2、WebView的滑动事件完成，响应父控件的滑动事件
 */
class NestedWebView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, @AttrRes defStyleAttr: Int = 0
) : WebView(context, attrs, defStyleAttr) {

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.actionMasked == MotionEvent.ACTION_DOWN) {
            val viewParent = findViewParentIfNeeds(this)
            viewParent?.requestDisallowInterceptTouchEvent(true)  //滑动事件由当前WebView接管
        }
        return super.onTouchEvent(event)
    }

    override fun onOverScrolled(scrollX: Int, scrollY: Int, clampedX: Boolean, clampedY: Boolean) {
        if (clampedX) {  //到达左/右边界后滑动事件由父View接管
            val viewParent = findViewParentIfNeeds(this)
            viewParent?.requestDisallowInterceptTouchEvent(false)
        }
        if (clampedY) {  //到达上/下边界后滑动事件由父View接管
            val viewParent = findViewParentIfNeeds(this)
            viewParent?.requestDisallowInterceptTouchEvent(false)
        }
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY)
    }

    //判断是否嵌套在滚动视图里
    private fun findViewParentIfNeeds(tag: View): ViewParent? {
        val parent = tag.parent
        if (parent == null) {
            return parent
        }
        return if (parent is ViewPager ||
            parent is AbsListView ||
            parent is ScrollView ||
            parent is NestedScrollView ||
            parent is HorizontalScrollView ||
            parent is GridView
        ) {
            parent
        } else {
            if (parent is View) {
                findViewParentIfNeeds(parent as View)
            } else {
                parent
            }
        }
    }
}