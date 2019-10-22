package com.android.widget.Dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import com.android.java.R;
import com.android.widget.ViewHolder;

/**
 * Created by xuzhb on 2019/10/21
 * Desc:单/双按钮Dialog
 */
public class ConfirmDialog extends BaseDialog {

    private static final String EXTRA_TITLE = "EXTRA_TITLE";
    private static final String EXTRA_CONTENT = "EXTRA_CONTENT";
    private static final String EXTRA_CONFIRM_TEXT = "EXTRA_CONFIRM_TEXT";
    private static final String EXTRA_CANCEL_TEXT = "EXTRA_CANCEL_TEXT";
    private static final String EXTRA_CANCEL_VISIBLE = "EXTRA_CANCEL_VISIBLE";

    private String mTitle;
    private String mContent;
    private String mConfirmText;
    private String mCancelText;
    private boolean mCancelVisible = true;  //是否显示两个按钮，默认显示两个按钮
    private OnConfirmListener mOnConfirmListener;
    private OnCancelListener mOnCancelListener;

    public static ConfirmDialog newInstance() {
        return new ConfirmDialog();
    }

    public static ConfirmDialog newInstance(
            String title, String content, String confirmText,
            String cancelText, boolean cancelVisible
    ) {
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_TITLE, title);
        bundle.putString(EXTRA_CONTENT, content);
        bundle.putString(EXTRA_CONFIRM_TEXT, confirmText);
        bundle.putString(EXTRA_CANCEL_TEXT, cancelText);
        bundle.putBoolean(EXTRA_CANCEL_VISIBLE, cancelVisible);
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setArguments(bundle);
        return dialog;
    }

    //设置标题
    public ConfirmDialog setTitle(String title) {
        this.mTitle = title;
        return this;
    }

    //设置内容
    public ConfirmDialog setContent(String content) {
        this.mContent = content;
        return this;
    }

    //设置确定按钮的文本
    public ConfirmDialog setConfirmText(String confirmText) {
        this.mConfirmText = confirmText;
        return this;
    }

    //设置取消按钮的文本
    public ConfirmDialog setCancelText(String cancelText) {
        this.mCancelText = cancelText;
        return this;
    }

    //是否显示取消按钮
    public ConfirmDialog setCancelVisible(boolean cancelVisible) {
        this.mCancelVisible = cancelVisible;
        return this;
    }

    //点击确定按钮后回调
    public ConfirmDialog setOnConfirmListener(OnConfirmListener listener) {
        this.mOnConfirmListener = listener;
        return this;
    }

    //点击取消按钮后回调
    public ConfirmDialog setOnCancelListener(OnCancelListener listener) {
        this.mOnCancelListener = listener;
        return this;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle == null) {
            return;
        }
        mTitle = bundle.getString(EXTRA_TITLE);
        mContent = bundle.getString(EXTRA_CONTENT);
        mConfirmText = bundle.getString(EXTRA_CONFIRM_TEXT);
        mCancelText = bundle.getString(EXTRA_CANCEL_TEXT);
        mCancelVisible = bundle.getBoolean(EXTRA_CANCEL_VISIBLE, true);
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_confirm_dialog;
    }

    @Override
    public void convertView(ViewHolder holder, final BaseDialog dialog) {
        holder.setText(R.id.title_tv, mTitle)
                .setText(R.id.content_tv, mContent)
                .setText(R.id.confirm_tv, mConfirmText);
        if (mCancelVisible) {
            holder.setText(R.id.cancel_tv, mCancelText);
        } else {
            holder.setViewGone(R.id.cancel_tv)
                    .setViewGone(R.id.vertical_view);
        }
        if (mOnConfirmListener != null) {
            holder.setOnClickListener(R.id.confirm_tv, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnConfirmListener.onConfirm(dialog);
                }
            });
        }
        if (mOnCancelListener != null) {
            holder.setOnClickListener(R.id.cancel_tv, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnCancelListener.onCancel(dialog);
                }
            });
        }
    }

    public interface OnConfirmListener {
        void onConfirm(BaseDialog dialog);
    }

    public interface OnCancelListener {
        void onCancel(BaseDialog dialog);
    }

}
