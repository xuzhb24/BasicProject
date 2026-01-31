package com.android.universal

import android.animation.ValueAnimator
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide

/**
 * Created by xuzhb on 2020/3/1
 * Desc: TabLayout + ViewPager + Fragment
 */
class TestSystemWidgetFragment : Fragment() {

    companion object {
        private val EXTRA_LAYOUT_ID = "EXTRA_LAYOUT_ID"
        fun newInstance(@LayoutRes layoutId: Int): TestSystemWidgetFragment {
            val fragment = TestSystemWidgetFragment()
            val bundle = Bundle()
            bundle.putInt(EXTRA_LAYOUT_ID, layoutId)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var progress_btn: Button
    private lateinit var progress_pb: ProgressBar
    private lateinit var progress_tv: TextView
    private lateinit var iv: ImageView

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mLayoutId = arguments?.getInt(EXTRA_LAYOUT_ID) ?: -1
    }

    @LayoutRes
    private var mLayoutId: Int = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(mLayoutId, container, false)
        if (mLayoutId == R.layout.fragment_test_system_widget3) {
            progress_btn = view.findViewById(R.id.progress_btn)
            progress_pb = view.findViewById(R.id.progress_pb)
            progress_tv = view.findViewById(R.id.progress_tv)
        }
        if (mLayoutId == R.layout.fragment_test_system_widget4) {
            iv = view.findViewById(R.id.iv)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        when (mLayoutId) {
            R.layout.fragment_test_system_widget1 -> page1()
            R.layout.fragment_test_system_widget2 -> page2()
            R.layout.fragment_test_system_widget3 -> page3()
            R.layout.fragment_test_system_widget4 -> page4()
        }
    }

    //第一个页面
    private fun page1() {

    }

    //第二个页面
    private fun page2() {

    }

    //第三个页面
    private fun page3() {
        //ProgressBar
        progress_btn.setOnClickListener {
            val animator = ValueAnimator.ofInt(0, 100)
            animator.addUpdateListener {
                //onAnimationUpdate的调用次数和setDuration有关
                val value = it.animatedValue  //数值
                val fraction = it.animatedFraction  //百分比
                progress_pb.progress = value as Int
                if ((fraction * 100).toInt() % 1 == 0) {  //调用100次
                    progress_tv.text = "${value}   ${fraction}"
                }
            }
            animator.interpolator = LinearInterpolator()
            animator.setDuration(5 * 1000).start()
        }
    }

    //第四个页面
    private fun page4() {
        Glide.with(this)
            .load("https://img.ivsky.com/img/tupian/pre/202005/31/xingkong-012.jpg")
            .into(iv)
    }

}