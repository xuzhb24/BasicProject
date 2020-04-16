package com.android.frame.mvc.viewBinding.AATest

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.basicproject.databinding.FragmentTestViewbindingBinding
import com.android.frame.mvc.viewBinding.BaseFragment_VB
import com.android.util.LogUtil

/**
 * Created by xuzhb on 2020/4/16
 * Desc:
 */
class TestViewBindingFragment : BaseFragment_VB<FragmentTestViewbindingBinding>() {

    companion object {
        private const val EXTRA_DATA = "EXTRA_TYPE"
        fun newInstance(data: String): TestViewBindingFragment {
            val fragment = TestViewBindingFragment()
            val bundle = Bundle()
            bundle.putString(EXTRA_DATA, data)
            fragment.arguments = bundle
            return fragment
        }
    }

    private var mData = ""

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mData = arguments?.getString(EXTRA_DATA) ?: ""
    }

    override fun handleView(savedInstanceState: Bundle?) {
        binding.tv.text = mData
    }

    override fun initListener() {
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentTestViewbindingBinding {
        return FragmentTestViewbindingBinding.inflate(inflater, container, false)
    }

    override fun onVisible() {
        log("onVisible 可见")
    }

    override fun onInvisible() {
        log("onInvisible 不可见")
    }

    private fun log(tag: String) {
        showToast(mData + " " + tag)
        LogUtil.e("TestViewBindingFragment", mData + " " + tag)
    }
}