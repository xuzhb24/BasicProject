package com.android.widget.FloatWindow.NeedPermission.AATest

import android.os.Bundle
import android.view.animation.BounceInterpolator
import android.widget.ImageView
import com.android.base.MainActivity
import com.android.basicproject.R
import com.android.basicproject.databinding.ActivityCommonLayoutBinding
import com.android.frame.mvc.BaseActivity
import com.android.util.LogUtil
import com.android.util.SizeUtil
import com.android.util.initCommonLayout
import com.android.widget.FloatWindow.NeedPermission.*

/**
 * Created by xuzhb on 2021/3/30
 * Desc:
 */
class TestFloatPageOneActivity : BaseActivity<ActivityCommonLayoutBinding>() {

    companion object {
        private const val TAG = "TestFloatPageOneActivity"
    }

    override fun handleView(savedInstanceState: Bundle?) {
        initCommonLayout(
            this, "悬浮窗页面一",
            "创建悬浮窗A", "销毁悬浮窗A", "创建悬浮窗B", "销毁悬浮窗B", "跳转到悬浮窗页面二"
        )
    }

    override fun initListener() {
        //创建悬浮窗
        binding.btn1.setOnClickListener {
            //这个悬浮窗的创建尽量放在Application的onCreate中，否则可能导致内存泄漏
            FloatWindow.with(applicationContext)
                .setView(R.layout.layout_float)
                .setContentViewId(R.id.content_iv)
                .setWidth(SizeUtil.dp2pxInt(100f))
                .setHeight(SizeUtil.dp2pxInt(100f))
                .setX(0)
                .setY(ScreenType.height, 0.1f)
                .setMoveType(MoveType.slide, -SizeUtil.dp2pxInt(50f), SizeUtil.dp2pxInt(50f))
                .setMoveStyle(500, BounceInterpolator())
                .setDesktopShow(true, MainActivity::class.java)
                .setFilter(false, TestFloatPageTwoActivity::class.java)
                .setOnPermissionListener(object : OnPermissionListener {
                    override fun onSuccess() {
                        LogUtil.i(TAG, "onSuccess")
                    }

                    override fun onFailure() {
                        LogUtil.i(TAG, "onFailure")
                    }
                })
                .setOnViewListener { holder, view ->
                    holder.setOnClickListener(R.id.clear_iv) {
                        FloatWindow.destroy()
                    }.setOnClickListener(R.id.content_iv) {
                        showToast("点到我了")
                    }
                }
                .setOnViewStateListener(object : OnViewStateListener {
                    override fun onPositionUpdate(x: Int, y: Int) {
                        LogUtil.i(TAG, "onPositionUpdate $x $y")
                    }

                    override fun onShow() {
                        LogUtil.i(TAG, "onShow")
                    }

                    override fun onHide() {
                        LogUtil.i(TAG, "onHide")
                    }

                    override fun onDismiss() {
                        LogUtil.i(TAG, "onDismiss")
                    }

                    override fun onMoveAnimStart() {
                        LogUtil.i(TAG, "onMoveAnimStart")
                    }

                    override fun onMoveAnimEnd() {
                        LogUtil.i(TAG, "onMoveAnimEnd")
                    }

                    override fun onBackToDesktop() {
                        LogUtil.i(TAG, "onBackToDesktop")
                    }
                }).build()
        }
        binding.btn2.setOnClickListener {
            FloatWindow.destroy()
        }
        binding.btn3.setOnClickListener {
            val imageView = ImageView(applicationContext)
            imageView.setImageResource(R.drawable.ic_face1)
            FloatWindow.with(applicationContext)
                .setView(imageView)
                .setTag("FloatWindowB")
                .setWidth(ScreenType.width, 0.3f)
                .setHeight(ScreenType.width, 0.3f)
                .setX(SizeUtil.dp2pxInt(100f))
                .setY(SizeUtil.dp2pxInt(100f))
                .setMoveType(MoveType.active, 0, 50)
                .setDesktopShow(false, MainActivity::class.java)
                .build()
        }
        binding.btn4.setOnClickListener {
            FloatWindow.destroy("FloatWindowB")
        }
        binding.btn5.setOnClickListener {
            startActivity(TestFloatPageTwoActivity::class.java)
        }
    }

    override fun getViewBinding() = ActivityCommonLayoutBinding.inflate(layoutInflater)

}