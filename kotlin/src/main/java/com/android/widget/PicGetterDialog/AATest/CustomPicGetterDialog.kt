package com.android.widget.PicGetterDialog.AATest

import android.app.Dialog
import com.android.basicproject.R
import com.android.widget.PicGetterDialog.BasePicGetterDialog
import com.android.widget.ViewHolder

/**
 * Created by xuzhb on 2020/1/28
 * Desc:
 */
class CustomPicGetterDialog : BasePicGetterDialog() {

    override fun getLayoutId(): Int = R.layout.dialog_custom_pic_getter

    override fun convertView(holder: ViewHolder, dialog: Dialog?) {
        holder.setOnClickListener(R.id.camera_btn) {
            openCamera()  //拍照
        }
        holder.setOnClickListener(R.id.gallery_btn) {
            openGallery()  //从相册选取照片
        }
    }

}