package com.android.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.AttrRes
import com.android.basicproject.R
import com.android.util.SizeUtil

/**
 * Created by xuzhb on 2020/4/6
 * Desc:指示器
 */
class IndicatorLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null, @AttrRes defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    companion object {
        val DEFAULT_SELECTED_WIDTH = SizeUtil.dp2px(20f).toInt()
        val DEFAULT_SIZE = SizeUtil.dp2px(5f).toInt()
        val DEFAULT_MARGIN = SizeUtil.dp2px(10f).toInt()
    }

    private var mIndicatorCount: Int = 0   //指示器个数
    private var mCurrentPosition: Int = 0  //指示器当前位置
    private val mIndicatorList: MutableList<ImageView> = mutableListOf()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        var height = 0
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize
        } else {
            height = DEFAULT_SIZE
            if (heightMode == MeasureSpec.AT_MOST) {
                height = Math.min(height, heightSize)
            }
        }
        setMeasuredDimension(measuredWidth, height)
    }

    //设置指示器个数，必须大于1才生效
    fun setIndicatorCount(indicatorCount: Int) {
        mIndicatorList.clear()
        mIndicatorCount = indicatorCount
        if (indicatorCount <= 1) {
            return
        }
        for (i in 1..indicatorCount) {
            val indicatorIv = ImageView(context)
            if (i == 1) {
                val params = LayoutParams(DEFAULT_SELECTED_WIDTH, DEFAULT_SIZE)
                params.leftMargin = 0
                indicatorIv.layoutParams = params
                indicatorIv.setImageResource(R.drawable.shape_indicator_select)
            } else {
                val params = LayoutParams(DEFAULT_SIZE, DEFAULT_SIZE)
                params.leftMargin = DEFAULT_MARGIN
                indicatorIv.layoutParams = params
                indicatorIv.setImageResource(R.drawable.shape_indicator_no_select)
            }
            addView(indicatorIv)
            mIndicatorList.add(indicatorIv)
        }
    }

    //设置指示器当前位置，下标从0开始
    fun setCurrentPosition(position: Int) {
        if (mIndicatorCount <= 1) {
            return
        }
        mCurrentPosition = position
        if (position < 0) {
            mCurrentPosition = 0
        }
        if (position > mIndicatorList.size - 1) {
            mCurrentPosition = mIndicatorList.size - 1
        }
        for (i in mIndicatorList.indices) {
            val params = mIndicatorList.get(i).layoutParams as LayoutParams
            if (i == mCurrentPosition) {
                params.width = DEFAULT_SELECTED_WIDTH
                mIndicatorList.get(i).layoutParams = params
                mIndicatorList.get(i).setImageResource(R.drawable.shape_indicator_select)
            } else {
                params.width = DEFAULT_SIZE
                mIndicatorList.get(i).layoutParams = params
                mIndicatorList.get(i).setImageResource(R.drawable.shape_indicator_no_select)
            }
        }
    }


}