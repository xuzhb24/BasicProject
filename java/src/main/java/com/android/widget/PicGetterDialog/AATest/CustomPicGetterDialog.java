package com.android.widget.PicGetterDialog.AATest;

import android.app.Dialog;
import com.android.java.R;
import com.android.widget.PicGetterDialog.BasePicGetterDialog;
import com.android.widget.ViewHolder;

/**
 * Created by xuzhb on 2020/1/27
 * Desc:
 */
public class CustomPicGetterDialog extends BasePicGetterDialog {

    public static CustomPicGetterDialog newInstance() {
        return new CustomPicGetterDialog();
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_custom_pic_getter;
    }

    @Override
    public void convertView(ViewHolder holder, Dialog dialog) {
        holder.setOnClickListener(R.id.camera_btn, v -> {
            openCamera();  //拍照
        });
        holder.setOnClickListener(R.id.gallery_btn, v -> {
            openGallery();  //从相册选取照片
        });
    }
}
