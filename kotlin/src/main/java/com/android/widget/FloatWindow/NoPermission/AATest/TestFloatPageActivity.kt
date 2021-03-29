package com.android.widget.FloatWindow.NoPermission.AATest

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.FrameLayout
import com.android.basicproject.R
import com.android.basicproject.databinding.ActivityCommonLayoutBinding
import com.android.frame.mvc.BaseActivity
import com.android.util.SizeUtil
import com.android.util.initCommonLayout
import com.android.widget.FloatWindow.NoPermission.FloatWindow
import com.android.widget.FloatWindow.NoPermission.MoveType
import com.android.widget.FloatWindow.NoPermission.ScreenType

/**
 * Created by xuzhb on 2021/3/25
 * Desc:
 */
class TestFloatPageActivity : BaseActivity<ActivityCommonLayoutBinding>() {

    companion object {
        private const val EXTRA_INDEX = "EXTRA_INDEX"
        fun start(context: Context, index: Int) {
            val intent = Intent(context, TestFloatPageActivity::class.java)
            intent.putExtra(EXTRA_INDEX, index)
            context.startActivity(intent)
        }
    }

    private var mIndex = 0

    override fun handleView(savedInstanceState: Bundle?) {
        mIndex = intent.getIntExtra(EXTRA_INDEX, 1)
        initCommonLayout(this, "悬浮窗页面$mIndex", "跳转下一个页面", "setX", "setY", "setXY", "show", "hide")
    }

    override fun onResume() {
        super.onResume()
        //绑定悬浮窗
        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            gravity = Gravity.END or Gravity.BOTTOM
            bottomMargin = SizeUtil.dp2pxInt(300f)
        }
        FloatWindow.get()
            .setView(R.layout.layout_float)
            .setContentViewId(R.id.content_iv)
            .setLayoutParams(params)
            .setMoveType(MoveType.active, 100f, -100f)
            .setOnViewListener { holder, view ->
                holder.setOnClickListener(R.id.content_iv) {
                    showToast("点到我了")
                }.setOnClickListener(R.id.clear_iv) {
                    FloatWindow.get().hide()
                }
            }.attach(this)
    }

    override fun onPause() {
        FloatWindow.get().detach(this)
        super.onPause()
    }

    override fun initListener() {
        binding.btn1.setOnClickListener {
            start(this, ++mIndex)
        }
        binding.btn2.setOnClickListener {
            FloatWindow.get().setX(ScreenType.width, 0.4f)
        }
        binding.btn3.setOnClickListener {
            FloatWindow.get().setY(ScreenType.height, 0.4f)
        }
        binding.btn4.setOnClickListener {
            FloatWindow.get().setXY(100f, 100f)
        }
        binding.btn5.setOnClickListener {
            if (FloatWindow.get().isShowing()) {
                showToast("已显示")
                return@setOnClickListener
            }
            FloatWindow.get().show()
        }
        binding.btn6.setOnClickListener {
            if (!FloatWindow.get().isShowing()) {
                showToast("已隐藏")
                return@setOnClickListener
            }
            FloatWindow.get().hide()
        }
    }

    override fun getViewBinding() = ActivityCommonLayoutBinding.inflate(layoutInflater)

}