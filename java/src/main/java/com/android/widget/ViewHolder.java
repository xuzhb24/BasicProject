package com.android.widget;

import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.IdRes;

/**
 * Created by xuzhb on 2019/10/21
 * Desc:
 */
public class ViewHolder {

    //缓存View
    private SparseArray<View> mViewList;
    private View mView;

    public ViewHolder(View view) {
        mView = view;
        mViewList = new SparseArray<>();
    }

    //查找View中的控件
    public <T extends View> T getView(@IdRes int viewId) {
        //对已有的view做缓存
        View view = mViewList.get(viewId);
        //使用缓存的方式减少findViewById的次数
        if (view == null) {
            view = mView.findViewById(viewId);
            mViewList.put(viewId, view);
        }
        return (T) view;
    }

    //设置文本
    public ViewHolder setText(@IdRes int viewId, CharSequence text) {
        TextView textView = getView(viewId);
        textView.setText(text);
        return this;  //链式调用
    }

    //设置文本字体颜色
    public ViewHolder setTextColor(@IdRes int viewId, int color) {
        TextView textView = getView(viewId);
        textView.setTextColor(color);
        return this;
    }

    //设置文本字体大小，单位默认为SP，故设置时只需要传递数值就可以，如setTextSize(R.id.xxx,15f)
    public ViewHolder setTextSize(@IdRes int viewId, float textSize) {
        TextView textView = getView(viewId);
        textView.setTextSize(textSize);
        return this;
    }

    //设置图片
    public ViewHolder setImageResource(@IdRes int viewId, int resId) {
        ImageView imageView = getView(viewId);
        imageView.setImageResource(resId);
        return this;
    }

    //显示View
    public ViewHolder setViewVisible(@IdRes int viewId) {
        getView(viewId).setVisibility(View.VISIBLE);
        return this;
    }

    //隐藏View
    public ViewHolder setViewGone(@IdRes int viewId) {
        getView(viewId).setVisibility(View.GONE);
        return this;
    }

    //设置View宽度
    public ViewHolder setViewWidth(@IdRes int viewId, int width) {
        setViewParams(viewId, width, -1);
        return this;
    }

    //设置View高度
    public ViewHolder setViewHeight(@IdRes int viewId, int height) {
        setViewParams(viewId, -1, height);
        return this;
    }

    //设置View的宽度和高度
    public ViewHolder setViewParams(@IdRes int viewId, int width, int height) {
        View view = getView(viewId);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        if (width >= 0) {
            params.width = width;
        }
        if (height >= 0) {
            params.height = height;
        }
        view.setLayoutParams(params);
        return this;
    }

    //设置点击事件
    public ViewHolder setOnClickListener(@IdRes int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }

    //设置长按事件
    public ViewHolder setOnLongClickListener(@IdRes int viewId, View.OnLongClickListener listener) {
        View view = getView(viewId);
        view.setOnLongClickListener(listener);
        return this;
    }

}
