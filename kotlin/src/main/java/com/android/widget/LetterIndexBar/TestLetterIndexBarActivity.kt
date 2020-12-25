package com.android.widget.LetterIndexBar

import android.os.Bundle
import android.view.View
import com.android.basicproject.databinding.ActivityTestLetterIndexBarBinding
import com.android.frame.mvc.viewBinding.BaseActivity_VB

/**
 * Created by xuzhb on 2020/12/24
 * Desc:
 */
class TestLetterIndexBarActivity : BaseActivity_VB<ActivityTestLetterIndexBarBinding>() {

    override fun handleView(savedInstanceState: Bundle?) {
    }

    override fun initListener() {
        binding.leftLetterIndexBar.setOnLetterChangedListener(object : LetterIndexBar.OnLetterChangedListener {
            override fun onLetterChanged(letter: String, x: Float, y: Float) {
                binding.letterTv.text = letter
                binding.letterTv.visibility = View.VISIBLE
            }

            override fun onLetterGone() {
                binding.letterTv.visibility = View.GONE
            }

        })
    }

    override fun getViewBinding() = ActivityTestLetterIndexBarBinding.inflate(layoutInflater)

}