package com.android.widget

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.widget.ImageView
import androidx.viewpager.widget.ViewPager
import com.android.basicproject.databinding.ActivityTestSingleWidgetBinding
import com.android.frame.mvc.BaseActivity
import com.android.util.SizeUtil
import com.android.widget.LoadingDialog.LoadingDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer
import com.youth.banner.loader.ImageLoader
import kotlin.random.Random

/**
 * Created by xuzhb on 2019/10/20
 * Desc:单一控件
 */
class TestSingleWidgetActivity : BaseActivity<ActivityTestSingleWidgetBinding>() {

    override fun handleView(savedInstanceState: Bundle?) {

    }

    override fun initListener() {
        mTitleBar?.setOnRightTextClickListener {
            AlertDialog.Builder(this@TestSingleWidgetActivity)
                .setMessage("本页面主要是查看编写的一些单一控件的样式")
                .show()
        }
        //展开/收起的TextView
        binding.expandTv1.contentText = "这是可以展开收起的文本，${createExpandText(360)}"
        binding.expandTv1.setOnTextClickListener(object : ExpandTextView.OnTextClickListener {
            override fun onContentTextClick(isExpand: Boolean) {
                binding.expandTv1.contentText = createExpandText(Random.nextInt(160))
            }

            override fun onLabelTextClick(isExpand: Boolean) {
                binding.expandTv1.isExpand = !isExpand
            }
        })
        binding.expandTv2.contentText = createExpandText(200)
        binding.expandTv2.setOnTextClickListener(object : ExpandTextView.OnTextClickListener {
            override fun onContentTextClick(isExpand: Boolean) {
                showToast(if (isExpand) "已展开" else "已收起")
            }

            override fun onLabelTextClick(isExpand: Boolean) {
                binding.expandTv2.isExpand = !isExpand
            }
        })
        //带有后缀的TextView
        binding.suffixTv.setOnTextClickListener(object : SuffixTextView.OnTextClickListener {
            override fun onContentTextClick() {
                binding.suffixTv.contentText = createSuffixText(Random.nextInt(4))
            }

            override fun onSuffixTextClick() {
                showToast("查看详情")
            }

        })
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
        binding.alignTv.setOnClickListener {
            binding.alignTv.text = createExpandText(200)
        }
    }

    private fun createExpandText(count: Int): String {
        var array = arrayOf(
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "0",
            "a", "b", "c", "d", "e", "f", "t", "g", "q", "w", "e",
            "p", "l", "k", "i", "n", "m", "G", "H", "J", "I", "L", "C", "V", "B"
            , "你", "我", "他", "天", "地", "动", "进", "啊", "去", "改", "酒",
            "一", "会", "年", "收", "好", "嗯", "这", "有",
            "\r", "\n", "\r\n", "\t", "，", "！", "%", "@"
        )
        val sb = StringBuilder()
        for (i in 0 until count) {
            sb.append(array[Random.nextInt(array.size)])
        }
        return sb.toString()
    }

    private fun createSuffixText(index: Int): String {
        return when (index) {
            0 -> {
                //一行文本，后缀不需要换行
                "一二三四五六七八九十"
            }
            1 -> {
                //一行文本，后缀需要换行
                "一二三四五六七八九十一二三四五六七八九十"
            }
            2 -> {
                //两行文本，不需要绘制内容文本的后缀
                "一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十"
            }
            else -> {
                //超过两行文本
                "一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十"
            }
        }
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