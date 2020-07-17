package com.android.widget

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.widget.ImageView
import com.android.basicproject.R
import com.android.frame.mvc.BaseActivity
import com.android.util.SizeUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer
import com.youth.banner.loader.ImageLoader
import kotlinx.android.synthetic.main.activity_test_single_widget.*

/**
 * Created by xuzhb on 2019/10/20
 * Desc:单一控件
 */
class TestSingleWidgetActivity : BaseActivity() {
    override fun handleView(savedInstanceState: Bundle?) {

    }

    override fun initListener() {
        //标题栏
        with(title_bar) {
            setOnLeftClickListener {
                finish()
            }
            setOnRightClickListener {
                AlertDialog.Builder(this@TestSingleWidgetActivity)
                    .setMessage("本页面主要是查看编写的一些单一控件的样式")
                    .show()
            }
        }
        //加载框
        loading_btn1.setOnClickListener {
            LoadingDialog(this).show()
        }
        loading_btn2.setOnClickListener {
            LoadingDialog(this, "加载中...").show()
        }
        loading_btn3.setOnClickListener {
            LoadingDialog(this, "", false).show()
        }
        //密码输入框
        with(password_edittext) {
            setOnTextChangeListener {
                pet_tv.text = it
            }
            setOnTextCompleteListener {
                showToast(it)
            }
        }
        pet_btn.setOnClickListener {
            password_edittext.clearText()
        }
        //搜索框
        with(search_layout) {
            setOnTextChangedListener { s, start, before, count ->
                searchlayout_tv.text = s
            }
        }
        //带删除按钮的输入框
        with(input_layout) {
            setOnTextChangedListener { s, start, before, count ->
                inputlayout_tv.text = s
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
        idicatorLayout.setIndicatorCount(imageList.size)
        banner.setBannerStyle(BannerConfig.NOT_INDICATOR)  //设置banner样式，不显示指示器和标题
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
        banner.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {

            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {

            }

            override fun onPageSelected(p0: Int) {
                idicatorLayout.setCurrentPosition(p0)
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

    override fun getLayoutId(): Int = R.layout.activity_test_single_widget
}