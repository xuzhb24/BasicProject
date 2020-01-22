package com.android.widget.Dialog;

import android.app.Dialog;
import android.support.annotation.LayoutRes;
import com.android.widget.ViewHolder;

/**
 * Created by xuzhb on 2019/10/21
 * Desc:使用该类实现不同布局的Dialog
 */
public class CommonDialog extends BaseDialog {

    private OnViewListener mListener;

    public static CommonDialog newInstance() {
        return new CommonDialog();
    }

    //设置dialog的布局
    public CommonDialog setLayoutId(@LayoutRes int layoutId) {
        this.mLayoutId = layoutId;
        return this;
    }

    //设置事件监听
    public CommonDialog setOnViewListener(OnViewListener listener) {
        this.mListener = listener;
        return this;
    }

    @Override
    public int getLayoutId() {
        return mLayoutId;
    }

    @Override
    public void convertView(ViewHolder holder, Dialog dialog) {
        if (mListener != null) {
            mListener.convertView(holder, dialog);
        }
    }

    public interface OnViewListener {
        void convertView(ViewHolder holder, Dialog dialog);
    }

}

