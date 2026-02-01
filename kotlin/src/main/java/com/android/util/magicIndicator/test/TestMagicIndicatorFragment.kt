package com.android.util.magicIndicator.test

import android.content.Context
import android.os.Bundle
import com.android.frame.mvc.BaseFragment
import com.android.kotlin.databinding.FragmentTestMagicindicatorBinding

/**
 * Created by xuzhb on 2026/1/31
 * Desc:
 */
class TestMagicIndicatorFragment : BaseFragment<FragmentTestMagicindicatorBinding>() {

    companion object {
        private const val EXTRA_TYPE = "EXTRA_TYPE"

        fun newInstance(type: String) = TestMagicIndicatorFragment().apply {
            arguments = Bundle().apply {
                putString(EXTRA_TYPE, type)
            }
        }
    }

    private var mType = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mType = arguments?.getString(EXTRA_TYPE) ?: ""
    }

    override fun handleView(savedInstanceState: Bundle?) {
        binding.tv.text = mType
    }

    override fun initListener() {
    }
}