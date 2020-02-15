package com.android.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Create by xuzhb on 2020/1/20
 * Desc:通用的ViewHolder(RecyelerView)
 */
public class ViewHolder extends RecyclerView.ViewHolder {

    //缓存View
    private SparseArray<View> mViewList;

    public ViewHolder(View itemView) {
        super(itemView);
        mViewList = new SparseArray<>();
    }

    //查找View中的控件
    public <T extends View> T getView(int viewId) {
        //对已有的view做缓存
        View view = mViewList.get(viewId);
        //使用缓存的方式减少findViewById的次数
        if (view == null) {
            view = itemView.findViewById(viewId);
            mViewList.put(viewId, view);
        }
        return (T) view;
    }

    //设置文本
    public ViewHolder setText(int viewId, CharSequence text) {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;  //链式调用
    }

    //设置文本字体颜色
    public ViewHolder setTextColor(int viewId, int color) {
        TextView tv = getView(viewId);
        tv.setTextColor(color);
        return this;
    }

    //设置文本字体大小，单位默认为SP，故设置时只需要传递数值就可以，如setTextSize(R.id.xxx,15f)
    public ViewHolder setTextSize(int viewId, float textSize) {
        TextView tv = getView(viewId);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        return this;
    }

    //设置图片
    public ViewHolder setImageResource(int viewId, int resId) {
        ImageView iv = getView(viewId);
        iv.setImageResource(resId);
        return this;
    }

    //设置图片
    public ViewHolder setImageDrawable(int viewId, Drawable drawable) {
        ImageView iv = getView(viewId);
        iv.setImageDrawable(drawable);
        return this;
    }

    //显示View
    public ViewHolder setViewVisible(int viewId) {
        getView(viewId).setVisibility(View.VISIBLE);
        return this;
    }

    //隐藏View
    public ViewHolder setViewGone(int viewId) {
        getView(viewId).setVisibility(View.GONE);
        return this;
    }

    //设置View宽度
    public ViewHolder setViewWidth(int view, int width) {
        return setViewParams(view, width, -1);
    }

    //设置View高度
    public ViewHolder setViewHeight(int view, int height) {
        return setViewParams(view, -1, height);
    }

    //设置View的宽度和高度
    public ViewHolder setViewParams(int viewId, int width, int height) {
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

    //子View的点击事件
    public interface OnItemChildClickListener {
        void onClick(View v);
    }

    //设置子View的点击事件，通过ViewHolder调用
    public ViewHolder setOnItemChildClickListener(int viewId, OnItemChildClickListener listener) {
        getView(viewId).setOnClickListener(listener::onClick);
        return this;
    }

    //子View的长按事件
    public interface OnItemChildLongClickListener {
        boolean onLongClick(View v);
    }

    //设置子View的长按事件，通过ViewHolder调用
    public ViewHolder setOnItemChildLongClickListener(int viewId, OnItemChildLongClickListener listener) {
        getView(viewId).setOnLongClickListener(listener::onLongClick);
        return this;
    }

    //Item点击事件
    public interface OnItemClickListener {
        void onClick(View v);
    }

    //设置Item点击事件，通过ViewHolder调用
    public ViewHolder setOnItemClickListener(OnItemClickListener listener) {
        itemView.setOnClickListener(listener::onClick);
        return this;
    }

    //Item长按事件
    public interface OnItemLongClickListener {
        boolean onLongClick(View v);
    }

    //设置Item长按事件，通过ViewHolder调用
    public ViewHolder setOnItemLongClickListener(OnItemLongClickListener listener) {
        itemView.setOnLongClickListener(listener::onLongClick);
        return this;
    }

}
