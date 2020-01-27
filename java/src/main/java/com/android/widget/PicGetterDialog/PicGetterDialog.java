package com.android.widget.PicGetterDialog;

import android.app.Dialog;
import com.android.java.R;
import com.android.widget.ViewHolder;

/**
 * Create by xuzhb on 2020/1/22
 * Desc:拍照或从相册选取选择框
 */
public class PicGetterDialog extends BasePicGetterDialog {

    public static PicGetterDialog newInstance() {
        return new PicGetterDialog();
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_pic_getter;
    }

    @Override
    public void convertView(ViewHolder holder, Dialog dialog) {
        holder.setOnClickListener(R.id.camera_tv, v -> {
            openCamera();  //拍照
        });
        holder.setOnClickListener(R.id.gallery_tv, v -> {
            openGallery();  //从相册选取照片
        });
        holder.setOnClickListener(R.id.cancel_tv, v -> {
            onCancel(getDialog());  //取消弹窗
            dismiss();
        });
    }
}
