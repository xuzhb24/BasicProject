package com.android.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by xuzhb on 2019/10/19
 * Desc:控制ViewPager是否可以左右滑动
 */
public class CustomViewPager extends ViewPager {

    private Boolean mIsScrollable = true;  //控制ViewPager是否可以左右滑动

    public Boolean isScrollable() {
        return mIsScrollable;
    }

    public void setScrollable(Boolean scrollable) {
        mIsScrollable = scrollable;
    }

    public CustomViewPager(@NonNull Context context) {
        super(context);
    }

    public CustomViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * dispatchTouchEvent一般情况不做处理，如果修改了默认的返回值，子View都无法收到事件
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 是否拦截
     * 拦截:会走到自己的onTouchEvent方法里面来
     * 不拦截:事件传递给子View
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // return false;//可行,不拦截事件,
        // return true;//不行,子View无法处理事件
        //return super.onInterceptTouchEvent(ev);//不行,会有细微移动
        if (mIsScrollable) {
            return super.onInterceptTouchEvent(ev);
        } else {
            return false;
        }
    }

    /**
     * 是否消费事件
     * 消费:事件就结束
     * 不消费:往父控件传
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //return false;// 可行,不消费,传给父控件
        //return true;// 可行,消费,拦截事件
        //super.onTouchEvent(ev); //不行,
        //虽然onInterceptTouchEvent中拦截了,
        //但是如果viewpage里面子控件不是viewgroup,还是会调用这个方法.
        if (mIsScrollable) {
            return super.onTouchEvent(ev);
        } else {
            return true;
        }
    }
}
