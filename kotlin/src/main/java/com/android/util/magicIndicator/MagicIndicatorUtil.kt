package com.android.util.magicIndicator

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.core.graphics.toColorInt
import androidx.viewpager.widget.ViewPager
import com.android.kotlin.R
import com.android.util.MeasureUtil
import com.android.util.ScreenUtil
import com.android.util.dp2px
import com.android.util.invisible
import com.android.util.px2dp
import com.android.util.setStyleBold
import com.android.util.setStyleNormal
import com.android.util.visible
import net.lucode.hackware.magicindicator.MagicIndicator
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.CommonPagerTitleView

/**
 * Created by xuzhb on 2026/1/28
 * Desc:
 */
object MagicIndicatorUtil {

    //TitleView默认配置
    private const val TITLE_VIEW_MIN_SCALE = 14f / 16f
    private const val TITLE_VIEW_SCALE_TEXT_SIZE = 16f
    private const val TITLE_VIEW_NORMAL_TEXT_SIZE = 16f
    private const val TITLE_VIEW_SELECTED_TEXT_SIZE = 16f
    private val TITLE_VIEW_NORMAL_COLOR = "#737373".toColorInt()
    private val TITLE_VIEW_SELECTED_COLOR = "#db4b3c".toColorInt()

    //指示器默认配置
    private const val INDICATOR_LINE_WIDTH = 23f
    private const val INDICATOR_LINE_HEIGHT = 3f
    private const val INDICATOR_ROUND_RADIUS = 1.5f
    private val INDICATOR_LINE_COLOR = "#db4b3c".toColorInt()

    private const val NOT_SET_PADDING = -999999f

    fun bindScaleTitleView(
        context: Context,
        magicIndicator: MagicIndicator,
        list: MutableList<String>,
        listener: (index: Int) -> Unit,
        minScale: Float = TITLE_VIEW_MIN_SCALE,
        isBoldSelected: Boolean = true,
        @ColorInt normalColorInt: Int = TITLE_VIEW_NORMAL_COLOR,
        @ColorInt selectedColorInt: Int = TITLE_VIEW_SELECTED_COLOR,
        textSizeSp: Float = TITLE_VIEW_SCALE_TEXT_SIZE,
        itemPaddingDp: Float = NOT_SET_PADDING,     //MagicIndicator各个子Item的padding，默认不设置，不设置则默认居中对齐，即isAdjustMode为true
        leftPaddingDp: Float = NOT_SET_PADDING,   //MagicIndicator内部左边距
        rightPaddingDp: Float = NOT_SET_PADDING,  //MagicIndicator内部右边距
        indicator: IPagerIndicator? = null,
    ) {
        bind(context, null, magicIndicator, list, { index ->
            createScaleTitleView(context, index, list, {
                magicIndicator.onPageSelected(index)
                magicIndicator.onPageScrolled(index, 0f, 0)
                listener.invoke(index)
            }, minScale, isBoldSelected, normalColorInt, selectedColorInt, textSizeSp, itemPaddingDp)
        }, indicator, itemPaddingDp == NOT_SET_PADDING, leftPaddingDp, rightPaddingDp)
    }

    fun bindScaleTitleView(
        context: Context,
        viewPager: ViewPager,
        magicIndicator: MagicIndicator,
        list: MutableList<String>,
        minScale: Float = TITLE_VIEW_MIN_SCALE,
        isBoldSelected: Boolean = true,
        @ColorInt normalColorInt: Int = TITLE_VIEW_NORMAL_COLOR,
        @ColorInt selectedColorInt: Int = TITLE_VIEW_SELECTED_COLOR,
        textSizeSp: Float = TITLE_VIEW_SCALE_TEXT_SIZE,
        itemPaddingDp: Float = NOT_SET_PADDING,     //MagicIndicator各个子Item的padding，默认不设置，不设置则默认居中对齐，即isAdjustMode为true
        leftPaddingDp: Float = NOT_SET_PADDING,   //MagicIndicator内部左边距
        rightPaddingDp: Float = NOT_SET_PADDING,  //MagicIndicator内部右边距
        indicator: IPagerIndicator? = null,
    ) {
        bind(context, viewPager, magicIndicator, list, { index ->
            createScaleTitleView(context, index, list, {
                viewPager.setCurrentItem(index, false)
            }, minScale, isBoldSelected, normalColorInt, selectedColorInt, textSizeSp, itemPaddingDp)
        }, indicator, itemPaddingDp == NOT_SET_PADDING, leftPaddingDp, rightPaddingDp)
    }

    fun bindCommonTitleView(
        context: Context,
        magicIndicator: MagicIndicator,
        @LayoutRes layoutResId: Int,
        listener: (index: Int) -> Unit,
        titleList: MutableList<String>,
        imageList: MutableList<Int> = mutableListOf(),
        @DrawableRes imageResId: Int? = null,
        isBoldSelected: Boolean? = null,
        normalTextSizeSp: Float = TITLE_VIEW_NORMAL_TEXT_SIZE,
        selectedTextSizeSp: Float = TITLE_VIEW_SELECTED_TEXT_SIZE,
        itemPaddingDp: Float = NOT_SET_PADDING,     //MagicIndicator各个子Item的padding，默认不设置，不设置则默认居中对齐，即isAdjustMode为true
        leftPaddingDp: Float = NOT_SET_PADDING,   //MagicIndicator内部左边距
        rightPaddingDp: Float = NOT_SET_PADDING,  //MagicIndicator内部右边距
        indicator: IPagerIndicator? = null,
    ) {
        bind(context, null, magicIndicator, titleList, { index ->
            if (imageResId != null) {
                createCommonTitleView(context, index, layoutResId, titleList, imageResId, {
                    magicIndicator.onPageSelected(index)
                    magicIndicator.onPageScrolled(index, 0f, 0)
                    listener.invoke(index)
                }, isBoldSelected, normalTextSizeSp, selectedTextSizeSp, itemPaddingDp)
            } else {
                createCommonTitleView(context, index, layoutResId, titleList, imageList, {
                    magicIndicator.onPageSelected(index)
                    magicIndicator.onPageScrolled(index, 0f, 0)
                    listener.invoke(index)
                }, isBoldSelected, normalTextSizeSp, selectedTextSizeSp, itemPaddingDp)
            }
        }, indicator, itemPaddingDp == NOT_SET_PADDING, leftPaddingDp, rightPaddingDp)
    }

    fun bindCommonTitleView(
        context: Context,
        viewPager: ViewPager,
        magicIndicator: MagicIndicator,
        @LayoutRes layoutResId: Int,
        titleList: MutableList<String>,
        imageList: MutableList<Int> = mutableListOf(),
        @DrawableRes imageResId: Int? = null,
        isBoldSelected: Boolean? = null,
        normalTextSizeSp: Float = TITLE_VIEW_NORMAL_TEXT_SIZE,
        selectedTextSizeSp: Float = TITLE_VIEW_SELECTED_TEXT_SIZE,
        itemPaddingDp: Float = NOT_SET_PADDING,     //MagicIndicator各个子Item的padding，默认不设置，不设置则默认居中对齐，即isAdjustMode为true
        leftPaddingDp: Float = NOT_SET_PADDING,   //MagicIndicator内部左边距
        rightPaddingDp: Float = NOT_SET_PADDING,  //MagicIndicator内部右边距
        indicator: IPagerIndicator? = null,
    ) {
        bind(context, viewPager, magicIndicator, titleList, { index ->
            if (imageResId != null) {
                createCommonTitleView(context, index, layoutResId, titleList, imageResId, {
                    viewPager.setCurrentItem(index, false)
                }, isBoldSelected, normalTextSizeSp, selectedTextSizeSp, itemPaddingDp)
            } else {
                createCommonTitleView(context, index, layoutResId, titleList, imageList, {
                    viewPager.setCurrentItem(index, false)
                }, isBoldSelected, normalTextSizeSp, selectedTextSizeSp, itemPaddingDp)
            }
        }, indicator, itemPaddingDp == NOT_SET_PADDING, leftPaddingDp, rightPaddingDp)
    }

    fun bind(
        context: Context,
        viewPager: ViewPager?,
        magicIndicator: MagicIndicator,
        list: MutableList<String>,
        titleViewFactory: (index: Int) -> IPagerTitleView,
        indicator: IPagerIndicator? = null,
        adjustMode: Boolean = true,
        leftPaddingDp: Float = NOT_SET_PADDING,
        rightPaddingDp: Float = NOT_SET_PADDING,
    ) {
        magicIndicator.navigator = createNavigator(context, list, titleViewFactory, indicator, adjustMode, leftPaddingDp, rightPaddingDp)
        viewPager?.let {
            it.offscreenPageLimit = list.size
            ViewPagerHelper.bind(magicIndicator, it)
        }
    }

    fun createNavigator(
        context: Context,
        list: MutableList<String>,
        titleViewFactory: (index: Int) -> IPagerTitleView,
        indicator: IPagerIndicator? = null,
        adjustMode: Boolean = true,
        leftPaddingDp: Float = NOT_SET_PADDING,
        rightPaddingDp: Float = NOT_SET_PADDING,
    ) = CommonNavigator(context).apply {
        isAdjustMode = adjustMode
        if (leftPaddingDp != NOT_SET_PADDING) {
            leftPadding = leftPaddingDp.dp2px().toInt()
        }
        if (rightPaddingDp != NOT_SET_PADDING) {
            rightPadding = rightPaddingDp.dp2px().toInt()
        }
        adapter = createNavigatorAdapter(list, titleViewFactory, indicator)
    }

    fun createNavigatorAdapter(
        list: MutableList<String>,
        titleViewFactory: (index: Int) -> IPagerTitleView,
        indicator: IPagerIndicator? = null
    ) = object : CommonNavigatorAdapter() {
        override fun getCount() = list.size

        override fun getTitleView(context: Context?, index: Int) = titleViewFactory(index)

        override fun getIndicator(context: Context?) = indicator
    }

    fun createScaleTitleView(
        context: Context,
        index: Int,
        list: MutableList<String>,
        listener: View.OnClickListener,
        minScale: Float = TITLE_VIEW_MIN_SCALE,
        isBoldSelected: Boolean = true,
        @ColorInt normalColorInt: Int = TITLE_VIEW_NORMAL_COLOR,
        @ColorInt selectedColorInt: Int = TITLE_VIEW_SELECTED_COLOR,
        textSizeSp: Float = TITLE_VIEW_SCALE_TEXT_SIZE,
        itemPaddingDp: Float = NOT_SET_PADDING,
    ) = ScaleTransitionPagerTitleView(context, minScale, isBoldSelected).apply {
        normalColor = normalColorInt
        selectedColor = selectedColorInt
        textSize = textSizeSp
        text = list[index]
        if (itemPaddingDp != NOT_SET_PADDING) {
            val padding = itemPaddingDp.dp2px().toInt()
            setPadding(padding, 0, padding, 0)
        }
        setOnClickListener(listener)
    }

    fun createCommonTitleView(
        context: Context,
        index: Int,
        @LayoutRes layoutResId: Int,
        titleList: MutableList<String>,
        imageList: MutableList<Int>,
        listener: View.OnClickListener,
        isBoldSelected: Boolean? = null,
        normalTextSizeSp: Float = TITLE_VIEW_NORMAL_TEXT_SIZE,
        selectedTextSizeSp: Float = TITLE_VIEW_SELECTED_TEXT_SIZE,
        itemPaddingDp: Float = NOT_SET_PADDING,
    ) = CommonPagerTitleView(context).apply {
        val layout = LayoutInflater.from(context.applicationContext).inflate(layoutResId, null)
        val tv: TextView = layout.findViewById(R.id.tv)
        val iv: ImageView = layout.findViewById(R.id.iv)
        tv.text = titleList[index]
        iv.setImageResource(imageList[index])
        if (itemPaddingDp != NOT_SET_PADDING) {
            val padding = itemPaddingDp.dp2px().toInt()
            tv.setPadding(padding, 0, padding, 0)
        }
        setContentView(layout)
        onPagerTitleChangeListener = object : CommonPagerTitleView.OnPagerTitleChangeListener {
            override fun onDeselected(index: Int, totalCount: Int) {
                isBoldSelected?.takeIf { it }?.let { tv.setStyleNormal() }
                tv.textSize = normalTextSizeSp
                tv.isSelected = false
                iv.isSelected = false
            }

            override fun onSelected(index: Int, totalCount: Int) {
                isBoldSelected?.takeIf { it }?.let { tv.setStyleBold() }
                tv.textSize = selectedTextSizeSp
                tv.isSelected = true
                iv.isSelected = true
            }

            override fun onLeave(index: Int, totalCount: Int, leavePercent: Float, leftToRight: Boolean) {
            }

            override fun onEnter(index: Int, totalCount: Int, enterPercent: Float, leftToRight: Boolean) {
            }
        }
        setOnClickListener(listener)
    }

    fun createCommonTitleView(
        context: Context,
        index: Int,
        @LayoutRes layoutResId: Int,
        titleList: MutableList<String>,
        @DrawableRes imageResId: Int,
        listener: View.OnClickListener,
        isBoldSelected: Boolean? = null,
        normalTextSizeSp: Float = TITLE_VIEW_NORMAL_TEXT_SIZE,
        selectedTextSizeSp: Float = TITLE_VIEW_SELECTED_TEXT_SIZE,
        itemPaddingDp: Float = NOT_SET_PADDING,
    ) = CommonPagerTitleView(context).apply {
        val layout = LayoutInflater.from(context.applicationContext).inflate(layoutResId, null)
        val tv: TextView = layout.findViewById(R.id.tv)
        val iv: ImageView = layout.findViewById(R.id.iv)
        tv.text = titleList[index]
        if (itemPaddingDp != NOT_SET_PADDING) {
            val padding = itemPaddingDp.dp2px().toInt()
            tv.setPadding(padding, 0, padding, 0)
        }
        iv.setImageResource(imageResId)
        setContentView(layout)
        onPagerTitleChangeListener = object : CommonPagerTitleView.OnPagerTitleChangeListener {
            override fun onDeselected(index: Int, totalCount: Int) {
                isBoldSelected?.takeIf { it }?.let { tv.setStyleNormal() }
                tv.textSize = normalTextSizeSp
                tv.isSelected = false
                iv.invisible()
            }

            override fun onSelected(index: Int, totalCount: Int) {
                isBoldSelected?.takeIf { it }?.let { tv.setStyleBold() }
                tv.textSize = selectedTextSizeSp
                tv.isSelected = true
                iv.visible()
            }

            override fun onLeave(index: Int, totalCount: Int, leavePercent: Float, leftToRight: Boolean) {
            }

            override fun onEnter(index: Int, totalCount: Int, enterPercent: Float, leftToRight: Boolean) {
            }
        }
        setOnClickListener(listener)
    }

    fun createIndicator(
        context: Context,
        lineWidthDp: Float = INDICATOR_LINE_WIDTH,
        lineHeightDp: Float = INDICATOR_LINE_HEIGHT,
        roundRadiusDp: Float = INDICATOR_ROUND_RADIUS,
        @ColorInt color: Int = INDICATOR_LINE_COLOR,
        modeValue: Int = LinePagerIndicator.MODE_EXACTLY,
    ) = LinePagerIndicator(context).apply {
        mode = modeValue
        lineWidth = lineWidthDp.dp2px()
        lineHeight = lineHeightDp.dp2px()
        roundRadius = roundRadiusDp.dp2px()
        setColors(color)
    }

    //计算并均分各item的padding，单位是dp，适用于list中字符串长短不一的情况
    fun calculateItemPaddingDp(list: MutableList<String>, adjustMinCount: Int, textSizeSp: Float, defaultPaddingDp: Float = NOT_SET_PADDING): Float {
        if (list.size in 2..adjustMinCount) {  //当list个数少于adjustMinCount时，计算子Item的padding，使得各个子Item的padding相同
            val totalWidth = list.map { MeasureUtil.measureTextWidthInSP(it, textSizeSp) }.sum()
            return ((ScreenUtil.getScreenWidth() - totalWidth) / (list.size * 2f + 2) - 2f).px2dp()
        }
        return defaultPaddingDp
    }


}