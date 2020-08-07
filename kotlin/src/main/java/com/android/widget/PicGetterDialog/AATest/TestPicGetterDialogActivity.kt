package com.android.widget.PicGetterDialog.AATest

import android.graphics.Bitmap
import android.os.Bundle
import android.view.ViewGroup
import com.android.basicproject.R
import com.android.basicproject.databinding.ActivityTestPicGetterDialogBinding
import com.android.frame.mvc.BaseActivity
import com.android.util.SizeUtil
import com.android.widget.PicGetterDialog.OnPicGetterListener
import com.android.widget.PicGetterDialog.PicGetterDialog
import com.yalantis.ucrop.UCrop

/**
 * Created by xuzhb on 2020/1/28
 * Desc:
 */
class TestPicGetterDialogActivity : BaseActivity<ActivityTestPicGetterDialogBinding>() {

    private var mPicGetterDialog: PicGetterDialog? = null

    override fun handleView(savedInstanceState: Bundle?) {

    }

    override fun initListener() {
        binding.dialogBtn1.setOnClickListener {
            showPicGetDialog()
        }
        binding.dialogBtn2.setOnClickListener {
            showCustomPicGetDialog()
        }
    }

    private fun showPicGetDialog() {
        if (mPicGetterDialog != null && mPicGetterDialog!!.dialog != null &&
            mPicGetterDialog!!.dialog.isShowing
        ) {
            return
        }
        val options = UCrop.Options()
        options.setToolbarTitle("裁剪图片")
        options.setToolbarColor(resources.getColor(R.color.colorPrimary))
        options.setStatusBarColor(resources.getColor(R.color.colorPrimary))
        mPicGetterDialog = PicGetterDialog()
        mPicGetterDialog!!.setAnimationStyle(R.style.AnimTranslateBottom)
            .setCropOptions(options)
            .setMaxCropSize(800, 2400)
            .setOnPicGetterListener(object : OnPicGetterListener {
                override fun onSuccess(bitmap: Bitmap?, picPath: String?) {
                    binding.picIv.setImageBitmap(bitmap)
                    binding.picTv.text = picPath
                }

                override fun onFailure(errorMsg: String?) {
                    showToast(errorMsg ?: "发生异常了")
                }

                override fun onCancel() {
                    showToast("用户取消了弹窗")
                }

            })
        mPicGetterDialog!!.show(supportFragmentManager)
    }

    private fun showCustomPicGetDialog() {
        val dialog = CustomPicGetterDialog()
        dialog.setViewParams(SizeUtil.dp2px(315f).toInt(), ViewGroup.MarginLayoutParams.WRAP_CONTENT)
            .setOnPicGetterListener(object : OnPicGetterListener {
                override fun onSuccess(bitmap: Bitmap?, picPath: String?) {
                    binding.picIv.setImageBitmap(bitmap)
                    binding.picTv.text = picPath
                }

                override fun onFailure(errorMsg: String?) {
                    showToast(errorMsg ?: "发生异常了")
                }

                override fun onCancel() {
                }

            })
        dialog.showAtCenter(supportFragmentManager)
    }

    override fun getViewBinding() = ActivityTestPicGetterDialogBinding.inflate(layoutInflater)

}