package com.android.widget.PhotoViewer

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.android.basicproject.R
import com.android.basicproject.databinding.ActivityPhotoViewBinding
import com.android.util.StatusBar.StatusBarUtil
import com.android.util.ToastUtil
import com.android.util.bitmap.BitmapUtil
import com.android.util.permission.PermissionUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import java.io.File

/**
 * Created by xuzhb on 2021/3/30
 * Desc:图片查看器Activity
 */
class PhotoViewActivity : AppCompatActivity() {

    companion object {
        private val REQUEST_PERMISSION: Array<String> = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        private const val REQUEST_PERMISSION_CODE = 1
        private const val EXTRA_IMAGE_URL_LIST = "EXTRA_IMAGE_URL_LIST"

        fun start(activity: Activity, imageUrlArray: Array<String>?) {
            if (imageUrlArray.isNullOrEmpty()) {
                return
            }
            start(activity, ArrayList(listOf(*imageUrlArray)))
        }

        fun start(activity: Activity, imageUrlList: ArrayList<String>?) {
            if (imageUrlList.isNullOrEmpty()) {
                return
            }
            val intent = Intent(activity, PhotoViewActivity::class.java)
            val bundle = Bundle().apply {
                putStringArrayList(EXTRA_IMAGE_URL_LIST, imageUrlList)
            }
            intent.putExtras(bundle)
            activity.startActivity(intent)
            activity.overridePendingTransition(R.anim.scale_center_in, R.anim.alpha_out_300)  //放大进入Activity
        }
    }

    private lateinit var binding: ActivityPhotoViewBinding
    private var mImageUrlList: ArrayList<String>? = null
    private var mCurrentPosition = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        StatusBarUtil.darkMode(this, Color.BLACK, 0f, false)
        mImageUrlList = intent.getStringArrayListExtra(EXTRA_IMAGE_URL_LIST)
        initPhoto(mImageUrlList)
        //关闭
        binding.closeIv.setOnClickListener {
            finish()
        }
        //下载
        binding.downloadIv.setOnClickListener {
            if (PermissionUtil.requestPermissions(this, REQUEST_PERMISSION_CODE, *REQUEST_PERMISSION)) {
                if (mCurrentPosition != -1 && !mImageUrlList.isNullOrEmpty()) {
                    downloadPicture(mImageUrlList!![mCurrentPosition])
                }
            }
        }
    }

    private fun initPhoto(imageUrlList: ArrayList<String>?) {
        if (imageUrlList.isNullOrEmpty()) {
            binding.downloadIv.visibility = View.GONE
            return
        }
        mCurrentPosition = 0
        binding.indicatorTv.text = "1/${imageUrlList.size}"
        binding.photoVp.adapter = PhotoViewAdapter(this, imageUrlList)
        binding.photoVp.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                mCurrentPosition = position
                binding.indicatorTv.text = "${position + 1}/${imageUrlList.size}"
            }
        })
        binding.photoVp.offscreenPageLimit = imageUrlList.size
    }

    //下载并保存图片
    private fun downloadPicture(url: String) {
        Glide.with(this).downloadOnly().load(url).into(object : CustomTarget<File>() {

            override fun onLoadStarted(placeholder: Drawable?) {
                super.onLoadStarted(placeholder)
            }

            override fun onLoadFailed(errorDrawable: Drawable?) {
                super.onLoadFailed(errorDrawable)
                ToastUtil.showToast("下载失败")
            }

            override fun onLoadCleared(placeholder: Drawable?) {
            }

            override fun onResourceReady(resource: File, transition: Transition<in File>?) {
                val isSuccess = BitmapUtil.saveImageFileToGallery(applicationContext, resource, "${System.currentTimeMillis()}")
                ToastUtil.showToast(if (isSuccess) "保存成功" else "保存失败")
            }
        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionUtil.onRequestPermissionsResult(
            this, requestCode, permissions, grantResults,
            object : PermissionUtil.OnPermissionListener {
                override fun onPermissionGranted() {
                    if (mCurrentPosition != -1 && !mImageUrlList.isNullOrEmpty()) {
                        downloadPicture(mImageUrlList!![mCurrentPosition])
                    }
                }

                override fun onPermissionDenied(deniedPermissions: Array<String>) {
                    ToastUtil.showToast("请先允许权限")
                }

                override fun onPermissionDeniedForever(deniedForeverPermissions: Array<String>) {
                }

            })
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.alpha_in_300, R.anim.scale_center_out) //缩小退出Activity
    }

}