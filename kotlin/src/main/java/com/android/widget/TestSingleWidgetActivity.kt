package com.android.widget

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.widget.ImageView
import androidx.viewpager.widget.ViewPager
import com.android.basicproject.databinding.ActivityTestSingleWidgetBinding
import com.android.frame.mvc.BaseActivity
import com.android.util.SizeUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer
import com.youth.banner.loader.ImageLoader

/**
 * Created by xuzhb on 2019/10/20
 * Desc:单一控件
 */
class TestSingleWidgetActivity : BaseActivity<ActivityTestSingleWidgetBinding>() {

    override fun handleView(savedInstanceState: Bundle?) {

    }

    override fun initListener() {
        mTitleBar?.setOnRightClickListener {
            AlertDialog.Builder(this@TestSingleWidgetActivity)
                .setMessage("本页面主要是查看编写的一些单一控件的样式")
                .show()
        }
        //加载框
        binding.loadingBtn1.setOnClickListener {
            LoadingDialog(this).show()
        }
        binding.loadingBtn2.setOnClickListener {
            LoadingDialog(this).show("加载中...")
        }
        binding.loadingBtn3.setOnClickListener {
            LoadingDialog(this).show("", false)
        }
        //密码输入框
        with(binding.passwordEdittext) {
            setOnTextChangeListener {
                binding.petTv.text = it
            }
            setOnTextCompleteListener {
                showToast(it)
            }
        }
        binding.petBtn.setOnClickListener {
            binding.passwordEdittext.clearText()
        }
        //搜索框
        with(binding.searchLayout) {
            setOnTextChangedListener { s, start, before, count ->
                binding.searchlayoutTv.text = s
            }
        }
        //带删除按钮的输入框
        with(binding.inputLayout) {
            setOnTextChangedListener { s, start, before, count ->
                binding.inputlayoutTv.text = s
            }
            setOnTextClearListener {
                showToast("文本被清空了")
            }
        }
        //轮播图和自定义指示器
        val imageList: MutableList<String> = mutableListOf(
            "http://img8.zol.com.cn/bbs/upload/23197/23196119.jpg",
            "http://pic1.win4000.com/wallpaper/b/55597435bb036.jpg",
            "http://attach.bbs.miui.com/forum/201306/23/110328s72xxse7lfis9fnd.jpg",
            "http://attach.bbs.miui.com/forum/201304/25/195151szk8umd8or8fmfa5.jpg",
            "http://img1.imgtn.bdimg.com/it/u=2391986294,913424495&fm=26&gp=0.jpg"
        )
        binding.idicatorLayout.setIndicatorCount(imageList.size)
        binding.banner.setBannerStyle(BannerConfig.NOT_INDICATOR)  //设置banner样式，不显示指示器和标题
//            .setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
//            .setIndicatorGravity(Gravity.CENTER)  //设置指示器位置
            .setImageLoader(GlideImageLoader())  //设置图片加载器
            .setBannerAnimation(Transformer.Default)  //设置banner动画效果
            .setDelayTime(3000)  //设置轮播时间
            .isAutoPlay(true)  //设置自动轮播，默认为true
            .setImages(imageList)  //设置图片集合
            .setOnBannerListener { position ->
                //点击图片回调
                showToast("第${position + 1}张图片")
            }
            .start()
        binding.banner.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {

            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {

            }

            override fun onPageSelected(p0: Int) {
                binding.idicatorLayout.setCurrentPosition(p0)
            }

        })
    }

    //Glide加载图片
    private inner class GlideImageLoader : ImageLoader() {
        override fun displayImage(context: Context?, path: Any, imageView: ImageView) {
            Glide.with(this@TestSingleWidgetActivity).load(path as String)
                .apply(
                    RequestOptions.bitmapTransform(
                        RoundedCorners(
                            SizeUtil.dp2px(10f).toInt()
                        )
                    )
                )  //转换成圆角图片
                .into(imageView)
        }
    }

    override fun getViewBinding() = ActivityTestSingleWidgetBinding.inflate(layoutInflater)

}