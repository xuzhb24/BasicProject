package com.android.universal

import android.animation.ValueAnimator
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_test_system_widget3.*

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

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mLayoutId = arguments?.getInt(EXTRA_LAYOUT_ID) ?: -1
    }

    @LayoutRes
    private var mLayoutId: Int = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(mLayoutId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        when (mLayoutId) {
            R.layout.fragment_test_system_widget1 -> page1()
            R.layout.fragment_test_system_widget2 -> page2()
            R.layout.fragment_test_system_widget3 -> page3()
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

}