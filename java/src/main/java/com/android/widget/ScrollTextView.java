package com.android.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;

/**
 * Created by xuzhb on 2020/1/6
 * Desc:TextView文字滚动显示，即跑马灯效果（不需要获取焦点）
 */
public class ScrollTextView extends AppCompatTextView {

    public ScrollTextView(Context context) {
        this(context, null);
    }

    public ScrollTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        setSingleLine(true);
        setEllipsize(TextUtils.TruncateAt.MARQUEE);  //设置跑马灯效果
        setMarqueeRepeatLimit(-1);  //设置循环滚动为无限循环
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}
