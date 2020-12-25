package com.android.widget.LetterIndexBar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.AttrRes
import com.android.basicproject.R
import com.android.util.SizeUtil

/**
 * Created by xuzhb on 2020/12/24
 * Desc:通讯录字母索引
 */
class LetterIndexBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, @AttrRes defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private val DEFAULT_TEXT_SIZE = SizeUtil.dp2px(15f)
        private val DEFAULT_TEXT_COLOR = Color.BLACK
    }

    var letters: Array<String> = arrayOf(
        "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
        "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#"
    )
    var normalTextSize: Float = DEFAULT_TEXT_SIZE   //正常时字母的字体大小
    var pressedTextSize: Float = DEFAULT_TEXT_SIZE  //按下时字母的字体大小
    var normalTextColor: Int = DEFAULT_TEXT_COLOR   //正常时字母的字体颜色
    var pressedTextColor: Int = DEFAULT_TEXT_COLOR  //按下时字母的字体颜色

    private var mPaint: Paint = Paint()
    private var mTextBound: Rect = Rect()
    private var mCellWidth: Float = 0f   //一个字母所在小格子的宽度
    private var mCellHeight: Float = 0f  //一个字母所在小格子的高度
    private var mPreIndex: Int = -1      //上次索引
    private var mCurIndex: Int = -1      //当前索引

    init {
        attrs?.let {
            val ta = context.obtainStyledAttributes(it, R.styleable.LetterIndexBar)
            normalTextSize = ta.getDimension(R.styleable.LetterIndexBar_normalTextSize, DEFAULT_TEXT_SIZE)
            pressedTextSize = ta.getDimension(R.styleable.LetterIndexBar_pressedTextSize, DEFAULT_TEXT_SIZE)
            normalTextColor = ta.getColor(R.styleable.LetterIndexBar_normalTextColor, DEFAULT_TEXT_COLOR)
            pressedTextColor = ta.getColor(R.styleable.LetterIndexBar_pressedTextColor, DEFAULT_TEXT_COLOR)
            ta.recycle()
        }
        mPaint.isAntiAlias = true
        mPaint.textAlign = Paint.Align.CENTER
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        var width = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        var height = MeasureSpec.getSize(heightMeasureSpec)
        width = when (widthMode) {
            MeasureSpec.EXACTLY -> width
            else -> {
                val letterWidth = SizeUtil.dp2px(20f)   //字母所在格子默认宽度
                (paddingLeft + paddingRight + letterWidth).toInt()
            }
        }
        height = when (heightMode) {
            MeasureSpec.EXACTLY -> height
            else -> {
                val letterHeight = SizeUtil.dp2px(20f)  //字母所在格子默认高度
                (paddingTop + paddingBottom + letters.size * letterHeight).toInt()
            }
        }
        setMeasuredDimension(width, height)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mCellWidth = measuredWidth.toFloat() - paddingLeft - paddingRight
        mCellHeight = (measuredHeight.toFloat() - paddingTop - paddingBottom) / letters.size
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        for (i in letters.indices) {
            val letter = letters[i]
            mPaint.getTextBounds(letter, 0, letter.length, mTextBound)
            val x = mCellWidth / 2.0f + paddingLeft
            val y = mCellHeight / 2.0f + mTextBound.height() / 2.0f + i * mCellHeight + paddingTop
            if (i == mCurIndex) {
                mPaint.color = pressedTextColor
                mPaint.textSize = pressedTextSize
            } else {
                mPaint.color = normalTextColor
                mPaint.textSize = normalTextSize
            }
            canvas?.drawText(letter, x, y, mPaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                mCurIndex = ((event.y - paddingTop) / mCellHeight).toInt()
                if (mCurIndex >= 0 && mCurIndex < letters.size && mCurIndex != mPreIndex) {
                    mLetterChangedListener?.onLetterChanged(
                        letters[mCurIndex],
                        paddingLeft + mCellWidth / 2f,
                        paddingTop + mCellHeight * mCurIndex + mCellHeight / 2f
                    )
                    mPreIndex = mCurIndex
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                mCurIndex = -1
                mPreIndex = -1
                mLetterChangedListener?.onLetterGone()
            }
        }
        invalidate()
        return true
    }

    //监听字母变化
    interface OnLetterChangedListener {
        //当字母改变时调用，letter为改变后的字母，x、y为字母中心的x、y坐标
        fun onLetterChanged(letter: String, x: Float, y: Float)

        //当手指抬起时调用
        fun onLetterGone()
    }

    private var mLetterChangedListener: OnLetterChangedListener? = null

    fun setOnLetterChangedListener(listener: OnLetterChangedListener?) {
        mLetterChangedListener = listener
    }

}