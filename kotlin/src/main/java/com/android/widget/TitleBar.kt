package com.android.widget

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.support.annotation.AttrRes
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.android.basicproject.R
import com.android.util.SizeUtil

/**
 * Created by xuzhb on 2019/7/30
 * Desc:标题栏
 */
class TitleBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, @AttrRes defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    companion object {
        val DEFAULT_MARGIN = SizeUtil.dp2px(20f)
        val DEFAULT_TEXT_MARGIN = SizeUtil.dp2px(20f)
        val DEFAULT_SIDE_TEXT_SIZE = SizeUtil.sp2px(15f)
        val DEFAULT_SIDE_TEXT_COLOR = Color.WHITE
        val DEFAULT_TEXT_SIZE = SizeUtil.sp2px(18f)
        val DEFAULT_TEXT_COLOR = Color.WHITE
    }

    var leftIcon: Drawable? = null  //左侧图标
        set(value) {
            field = value
            mLeftIv.setImageDrawable(value)
        }
    var leftIconMargin: Float = 0f  //左侧图标的左边距
        set(value) {
            field = value
            with(mLeftFl) {
                val params = layoutParams as MarginLayoutParams
                params.leftMargin = value.toInt()
                requestLayout()
            }
        }
    var showLeftIcon: Boolean = true  //是否显示左侧图标，默认相似
        set(value) {
            field = value
            with(mLeftFl) {
                if (value) {
                    visibility = View.VISIBLE
                } else {
                    visibility = View.GONE
                }
            }
        }
    var rightIcon: Drawable? = null  //右侧图标
        set(value) {
            field = value
            mRightIv.setImageDrawable(value)
        }
    var rightIconMargin: Float = 0f  //右侧图标的右边距
        set(value) {
            field = value
            with(mRightFl) {
                val params = layoutParams as MarginLayoutParams
                params.rightMargin = value.toInt()
                requestLayout()
            }
        }
    var showRightIcon: Boolean = false  //是否显示右侧图标，默认不显示
        set(value) {
            field = value
            with(mRightFl) {
                if (value) {
                    visibility = View.VISIBLE
                } else {
                    visibility = View.GONE
                }
            }
        }
    var leftText: String? = ""  //左侧文本
        set(value) {
            field = value
            mLeftTv.setText(value)
        }
    var leftTextSize: Float = 0f  //左侧文本的字体大小
        set(value) {
            field = value
            mLeftTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, value)
        }
    var leftTextColor: Int = Color.BLACK  //左侧文本的字体颜色
        set(value) {
            field = value
            mLeftTv.setTextColor(value)
        }
    var leftTextMargin: Float = 0f  //左侧文本的左边距
        set(value) {
            field = value
            with(mLeftTv) {
                val params = layoutParams as MarginLayoutParams
                params.leftMargin = value.toInt()
                requestLayout()
            }
        }
    var titleText: String? = ""  //标题文本
        set(value) {
            field = value
            mTitleTv.setText(value)
        }
    var titleTextSize: Float = 0f  //标题文本的字体大小
        set(value) {
            field = value
            mTitleTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, value)
        }
    var titleTextColor: Int = Color.BLACK  //标题文本的字体颜色
        set(value) {
            field = value
            mTitleTv.setTextColor(value)
        }
    var rightText: String? = ""  //右侧文本
        set(value) {
            field = value
            mRightTv.setText(value)
        }
    var rightTextSize: Float = 0f  //右侧文本的字体大小
        set(value) {
            field = value
            mRightTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, value)
        }
    var rightTextColor: Int = Color.BLACK  //右侧文本的字体颜色
        set(value) {
            field = value
            mRightTv.setTextColor(value)
        }
    var rightTextMargin: Float = 0f  //右侧文本的右边距
        set(value) {
            field = value
            with(mRightTv) {
                val params = layoutParams as MarginLayoutParams
                params.rightMargin = value.toInt()
                requestLayout()
            }
        }
    var showDivider: Boolean = true  //是否显示底部分割线
        set(value) {
            field = value
            with(mDividerLine) {
                if (value) {
                    visibility = View.VISIBLE
                } else {
                    visibility = View.GONE
                }
            }
        }

    //设置背景色
    override fun setBackgroundColor(color: Int) {
        mTitleFl.setBackgroundColor(color)
    }

    //以下非空属性，需要在init中初始化
    private var mTitleFl: FrameLayout
    private var mLeftFl: FrameLayout
    private var mLeftIv: ImageView
    private var mLeftTv: TextView
    private var mTitleTv: TextView
    private var mRightTv: TextView
    private var mRightFl: FrameLayout
    private var mRightIv: ImageView
    private var mDividerLine: View

    init {
        //先findViewById再obtainStyledAttributes，否则在构造函数constructor(context:Context)中无法初始化控件
        val layout = LayoutInflater.from(context).inflate(R.layout.layout_title_bar, this)
        with(layout) {
            mTitleFl = findViewById(R.id.title_fl)
            mLeftFl = findViewById(R.id.left_fl)
            mLeftIv = findViewById(R.id.left_iv)
            mLeftTv = findViewById(R.id.left_tv)
            mTitleTv = findViewById(R.id.title_tv)
            mRightTv = findViewById(R.id.right_tv)
            mRightFl = findViewById(R.id.right_fl)
            mRightIv = findViewById(R.id.right_iv)
            mDividerLine = findViewById(R.id.divider_line)
        }

        val ta = getContext().obtainStyledAttributes(attrs, R.styleable.TitleBar)
        with(ta) {
            leftIcon = getDrawable(R.styleable.TitleBar_leftIcon)
                ?: resources.getDrawable(R.drawable.ic_back)
            leftIconMargin = getDimension(R.styleable.TitleBar_leftIconMargin, DEFAULT_MARGIN)
            showLeftIcon = getBoolean(R.styleable.TitleBar_showLeftIcon, true)
            rightIcon = getDrawable(R.styleable.TitleBar_rightIcon)
                ?: resources.getDrawable(R.drawable.ic_add)
            rightIconMargin = getDimension(R.styleable.TitleBar_rightIconMargin, DEFAULT_MARGIN)
            showRightIcon = getBoolean(R.styleable.TitleBar_showRightIcon, false)
            leftText = getString(R.styleable.TitleBar_leftText)
            leftTextSize = getDimension(R.styleable.TitleBar_leftTextSize, DEFAULT_SIDE_TEXT_SIZE)
            leftTextColor = getColor(R.styleable.TitleBar_leftTextColor, DEFAULT_SIDE_TEXT_COLOR)
            leftTextMargin = getDimension(R.styleable.TitleBar_leftTextMargin, DEFAULT_TEXT_MARGIN)
            titleText = getString(R.styleable.TitleBar_titleText)
            titleTextSize = getDimension(R.styleable.TitleBar_titleTextSize, DEFAULT_TEXT_SIZE)
            titleTextColor = getColor(R.styleable.TitleBar_titleTextColor, DEFAULT_TEXT_COLOR)
            rightText = getString(R.styleable.TitleBar_rightText)
            rightTextSize = getDimension(R.styleable.TitleBar_rightTextSize, DEFAULT_SIDE_TEXT_SIZE)
            rightTextColor = getColor(R.styleable.TitleBar_rightTextColor, DEFAULT_SIDE_TEXT_COLOR)
            rightTextMargin =
                getDimension(R.styleable.TitleBar_rightTextMargin, DEFAULT_TEXT_MARGIN)
            showDivider = getBoolean(R.styleable.TitleBar_showDivider, false)
            recycle()
        }

        with(mLeftFl) {
            mLeftIv.setImageDrawable(leftIcon)
            val params = layoutParams as MarginLayoutParams
            params.leftMargin = leftIconMargin.toInt()
            requestLayout()
            if (showLeftIcon) {
                visibility = View.VISIBLE
            } else {
                visibility = View.GONE
            }
        }

        with(mRightFl) {
            mRightIv.setImageDrawable(rightIcon)
            val params = layoutParams as MarginLayoutParams
            params.rightMargin = rightIconMargin.toInt()
            requestLayout()
            if (showRightIcon) {
                visibility = View.VISIBLE
            } else {
                visibility = View.GONE
            }
        }

        with(mLeftTv) {
            setText(leftText)
            setTextSize(TypedValue.COMPLEX_UNIT_PX, leftTextSize)
            setTextColor(leftTextColor)
            val params = layoutParams as MarginLayoutParams
            params.leftMargin = leftTextMargin.toInt()
            requestLayout()
        }

        with(mTitleTv) {
            setText(titleText)
            setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize)
            setTextColor(titleTextColor)
        }

        with(mRightTv) {
            setText(rightText)
            setTextSize(TypedValue.COMPLEX_UNIT_PX, rightTextSize)
            setTextColor(rightTextColor)
            val params = layoutParams as MarginLayoutParams
            params.rightMargin = rightTextMargin.toInt()
            requestLayout()
        }

        with(mDividerLine) {
            if (showDivider) {
                visibility = View.VISIBLE
            } else {
                visibility = View.GONE
            }
        }

    }

    //点击左侧图标/文本回调
    fun setOnLeftClickListener(listener: (view: View) -> Unit) {
        mLeftFl.setOnClickListener { v -> listener.invoke(v) }
//        mLeftTv.setOnClickListener { v -> listener.invoke(v) }
    }

    //点击右侧图标/文本回调
    fun setOnRightClickListener(listener: (view: View) -> Unit) {
//        mRightFl.setOnClickListener { v -> listener.invoke(v) }
        mRightTv.setOnClickListener { v -> listener.invoke(v) }
    }

}