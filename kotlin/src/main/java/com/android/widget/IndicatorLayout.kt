package com.android.widget

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.AttrRes
import com.android.universal.R
import com.android.util.DrawableUtil
import com.android.util.SizeUtil

/**
 * Created by xuzhb on 2020/4/6
 * Desc:指示器
 */
class IndicatorLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, @AttrRes defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    companion object {
        val DEFAULT_SELECTED_WIDTH = SizeUtil.dp2px(15f)
        val DEFAULT_NORMAL_SIZE = SizeUtil.dp2px(5f)
        val DEFAULT_ITEM_MARGIN = SizeUtil.dp2px(6f)
        val DEFAULT_SELECTED_COLOR = Color.parseColor("#DA251D")
        val DEFAULT_NORMAL_COLOR = Color.parseColor("#C4C4C4")
    }

    var selectedWidth: Float = DEFAULT_SELECTED_WIDTH
    var normalSize: Float = DEFAULT_NORMAL_SIZE
    var itemMargin: Float = DEFAULT_ITEM_MARGIN
    var selectedDrawable: Drawable? = null  //选中时背景，优先级高于selectedColor
    var selectedColor: Int = DEFAULT_SELECTED_COLOR
    var normalColor: Int = DEFAULT_NORMAL_COLOR

    private var mIndicatorCount: Int = 0   //指示器个数
    private var mCurrentPosition: Int = 0  //指示器当前位置
    private val mIndicatorList: MutableList<ImageView> = mutableListOf()

    init {
        attrs?.let {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.IndicatorLayout)
            selectedWidth = ta.getDimension(R.styleable.IndicatorLayout_selectedWidth, DEFAULT_SELECTED_WIDTH)
            normalSize = ta.getDimension(R.styleable.IndicatorLayout_normalSize, DEFAULT_NORMAL_SIZE)
            itemMargin = ta.getDimension(R.styleable.IndicatorLayout_itemMargin, DEFAULT_ITEM_MARGIN)
            selectedDrawable = ta.getDrawable(R.styleable.IndicatorLayout_selectedDrawable)
            selectedColor = ta.getColor(R.styleable.IndicatorLayout_selectedColor, DEFAULT_SELECTED_COLOR)
            normalColor = ta.getColor(R.styleable.IndicatorLayout_normalColor, DEFAULT_NORMAL_COLOR)
            ta.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        var height = 0
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize
        } else {
            height = normalSize.toInt()
            if (heightMode == MeasureSpec.AT_MOST) {
                height = height.coerceAtMost(heightSize)
            }
        }
        setMeasuredDimension(measuredWidth, height)
    }

    //设置指示器个数，必须大于1才生效
    fun setIndicatorCount(indicatorCount: Int) {
        mIndicatorList.clear()
        removeAllViews()
        mIndicatorCount = indicatorCount
        if (indicatorCount <= 1) {
            return
        }
        for (i in 1..indicatorCount) {
            val indicatorIv = ImageView(context)
            if (i == 1) {
                indicatorIv.layoutParams = LayoutParams(selectedWidth.toInt(), normalSize.toInt()).apply {
                    leftMargin = 0
                }
                indicatorIv.setImageDrawable(createSelectedShape())
            } else {
                indicatorIv.layoutParams = LayoutParams(normalSize.toInt(), normalSize.toInt()).apply {
                    leftMargin = itemMargin.toInt()
                }
                indicatorIv.setImageDrawable(createNormalShape())
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
            val params = mIndicatorList[i].layoutParams as LayoutParams
            if (i == mCurrentPosition) {
                params.width = selectedWidth.toInt()
                mIndicatorList[i].layoutParams = params
                mIndicatorList[i].setImageDrawable(createSelectedShape())
            } else {
                params.width = normalSize.toInt()
                mIndicatorList[i].layoutParams = params
                mIndicatorList[i].setImageDrawable(createNormalShape())
            }
        }
    }

    private fun createSelectedShape() = if (selectedDrawable != null) selectedDrawable else DrawableUtil.createSolidShape(normalSize / 2, selectedColor)

    private fun createNormalShape() = DrawableUtil.createOvalShape(normalColor, normalSize.toInt(), normalSize.toInt())

}