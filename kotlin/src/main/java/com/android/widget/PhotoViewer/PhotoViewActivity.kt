package com.android.widget.PhotoViewer

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.android.basicproject.R
import com.android.basicproject.databinding.ActivityPhotoViewBinding
import com.android.util.StatusBar.StatusBarUtil
import com.android.util.ToastUtil
import com.android.util.bitmap.BitmapUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

/**
 * Created by xuzhb on 2021/3/30
 * Desc:图片查看器Activity
 */
class PhotoViewActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_IMAGE_URL_LIST = "EXTRA_IMAGE_URL_LIST"
        private const val EXTRA_IMAGE_POSITION = "EXTRA_IMAGE_POSITION"

        fun start(activity: Activity, imageUrlArray: Array<String>?, position: Int = 0) {
            if (imageUrlArray.isNullOrEmpty()) {
                return
            }
            start(activity, ArrayList(listOf(*imageUrlArray)), position)
        }

        fun start(activity: Activity, imageUrlList: ArrayList<String>?, position: Int = 0) {
            if (imageUrlList.isNullOrEmpty()) {
                return
            }
            val intent = Intent(activity, PhotoViewActivity::class.java)
            val bundle = Bundle().apply {
                putStringArrayList(EXTRA_IMAGE_URL_LIST, imageUrlList)
                putInt(EXTRA_IMAGE_POSITION, position)
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
        StatusBarUtil.darkModeAndPadding(this, binding.rootFl)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.navigationBarColor = Color.BLACK
        }
        mImageUrlList = intent.getStringArrayListExtra(EXTRA_IMAGE_URL_LIST)
        mCurrentPosition = intent.getIntExtra(EXTRA_IMAGE_POSITION, -1)
        initPhoto(mImageUrlList)
        //关闭
        binding.closeIv.setOnClickListener {
            finish()
        }
        //下载
        binding.downloadIv.setOnClickListener {
            if (mCurrentPosition != -1 && !mImageUrlList.isNullOrEmpty()) {
                downloadPicture(mImageUrlList!![mCurrentPosition])
            }
        }
    }

    private fun initPhoto(imageUrlList: ArrayList<String>?) {
        if (imageUrlList.isNullOrEmpty()) {
            binding.downloadIv.visibility = View.GONE
            return
        }
        if (mCurrentPosition < 0 || mCurrentPosition >= imageUrlList.size) {
            mCurrentPosition = 0
        }
        binding.indicatorTv.text = "${mCurrentPosition + 1}/${imageUrlList.size}"
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
        binding.photoVp.setCurrentItem(mCurrentPosition, false)
    }

    //下载并保存图片
    private fun downloadPicture(url: String) {
        Glide.with(this).load(url).into(object : CustomTarget<Drawable>() {

            override fun onLoadStarted(placeholder: Drawable?) {
                super.onLoadStarted(placeholder)
            }

            override fun onLoadFailed(errorDrawable: Drawable?) {
                super.onLoadFailed(errorDrawable)
                ToastUtil.showToast("下载失败", true)
            }

            override fun onLoadCleared(placeholder: Drawable?) {
            }

            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                val bitmap = (resource as BitmapDrawable).bitmap
                BitmapUtil.saveBitmapToGallery(this@PhotoViewActivity, bitmap, System.currentTimeMillis().toString() + "")
            }
        })
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.alpha_in_300, R.anim.scale_center_out) //缩小退出Activity
    }

}