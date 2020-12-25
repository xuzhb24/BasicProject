package com.android.widget.LetterIndexBar

import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.AttrRes
import com.android.basicproject.R
import com.android.util.SizeUtil

/**
 * Created by xuzhb on 2020/12/24
 * Desc:带气泡的通讯录字母索引
 */
class BubbleLetterIndexBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, @AttrRes defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private val DEFAULT_LETTERS = arrayOf(
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
            "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#"
        )
        private val DEFAULT_TEXT_SIZE = SizeUtil.dp2px(15f)
        private val DEFAULT_TEXT_COLOR = Color.BLACK
        private val DEFAULT_BUBBLE_MARGIN = SizeUtil.dp2px(10f)
        private val DEFAULT_BUBBLE_TEXT_MARGIN = SizeUtil.dp2px(35f)
    }

    var letters: Array<String> = DEFAULT_LETTERS
        set(value) {
            field = value
            invalidate()
        }
    var normalTextSize: Float = DEFAULT_TEXT_SIZE    //正常时字母的字体大小
    var pressedTextSize: Float = DEFAULT_TEXT_SIZE   //按下时字母的字体大小
    var bubbleTextSize: Float = DEFAULT_TEXT_SIZE    //气泡中字母的字体大小
    var normalTextColor: Int = DEFAULT_TEXT_COLOR    //正常时字母的字体颜色
    var pressedTextColor: Int = DEFAULT_TEXT_COLOR   //按下时字母的字体颜色
    var bubbleTextColor: Int = DEFAULT_TEXT_COLOR    //气泡中字母的字体颜色
    var bubbleMargin: Float = DEFAULT_BUBBLE_MARGIN  //气泡到字母一栏的中线距离
    var bubbleTextMargin: Float = DEFAULT_BUBBLE_TEXT_MARGIN  //气泡中字母到字母一栏的中线距离

    private var mPaint: Paint = Paint()
    private var mBubblePaint: Paint = TextPaint()  //绘制气泡中字母
    private var mBitmapPaint: Paint = Paint()      //绘制气泡背景图
    private var mTextBound: Rect = Rect()
    private var mCellWidth: Float = 0f   //一个字母所在小格子的宽度
    private var mCellHeight: Float = 0f  //一个字母所在小格子的高度
    private var mPreIndex: Int = -1      //上次索引
    private var mCurIndex: Int = -1      //当前索引
    private var mBitmapWidth: Float = SizeUtil.dp2px(45f)   //气泡背景图宽度
    private var mBitmapHeight: Float = SizeUtil.dp2px(45f)  //气泡背景图高度
    private var mBitmapRect: Rect = Rect()
    private var mBitmap: Bitmap
    private var isDownInLetter = false  //手指按下时是否在字母一栏

    init {
        attrs?.let {
            val ta = context.obtainStyledAttributes(it, R.styleable.BubbleLetterIndexBar)
            normalTextSize = ta.getDimension(R.styleable.BubbleLetterIndexBar_normalTextSize, DEFAULT_TEXT_SIZE)
            pressedTextSize = ta.getDimension(R.styleable.BubbleLetterIndexBar_pressedTextSize, DEFAULT_TEXT_SIZE)
            bubbleTextSize = ta.getDimension(R.styleable.BubbleLetterIndexBar_bubbleTextSize, DEFAULT_TEXT_SIZE)
            normalTextColor = ta.getColor(R.styleable.BubbleLetterIndexBar_normalTextColor, DEFAULT_TEXT_COLOR)
            pressedTextColor = ta.getColor(R.styleable.BubbleLetterIndexBar_pressedTextColor, DEFAULT_TEXT_COLOR)
            bubbleTextColor = ta.getColor(R.styleable.BubbleLetterIndexBar_bubbleTextColor, DEFAULT_TEXT_COLOR)
            bubbleMargin = ta.getDimension(R.styleable.BubbleLetterIndexBar_bubbleMargin, DEFAULT_BUBBLE_MARGIN)
            bubbleTextMargin = ta.getDimension(R.styleable.BubbleLetterIndexBar_bubbleTextMargin, DEFAULT_BUBBLE_TEXT_MARGIN)
            ta.recycle()
        }
        mPaint.isAntiAlias = true
        mPaint.textAlign = Paint.Align.CENTER
        mBubblePaint.isAntiAlias = true
        mBubblePaint.textAlign = Paint.Align.CENTER
        mBubblePaint.isFakeBoldText = true  //气泡中字母加粗显示
        mBitmapPaint.isAntiAlias = true
        mBitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_letter_bubble)
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
                //绘制字母一栏
                mPaint.color = pressedTextColor
                mPaint.textSize = pressedTextSize
                canvas?.drawText(letter, x, y, mPaint)
                //绘制气泡图片
                val cx = paddingLeft + mCellWidth / 2f
                val cy = paddingTop + mCellHeight * mCurIndex + mCellHeight / 2f
                mBitmapRect.left = (cx - bubbleMargin - mBitmapWidth).toInt()
                mBitmapRect.right = (cx - bubbleMargin).toInt()
                mBitmapRect.top = (cy - mBitmapHeight / 2f).toInt()
                mBitmapRect.bottom = (cy + mBitmapHeight / 2f).toInt()
                canvas?.drawBitmap(mBitmap, null, mBitmapRect, mBitmapPaint)
                //绘制气泡中的字母
                mBubblePaint.color = bubbleTextColor
                mBubblePaint.textSize = bubbleTextSize
                val fm = mBubblePaint.fontMetrics
                val distance = (fm.bottom - fm.top) / 2f - fm.bottom  //气泡文本的基线
                canvas?.drawText(
                    letter,
                    cx - bubbleTextMargin,
                    cy + distance,
                    mBubblePaint
                )
            } else {
                mPaint.color = normalTextColor
                mPaint.textSize = normalTextSize
                canvas?.drawText(letter, x, y, mPaint)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            when (it.action) {
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                    if (it.action == MotionEvent.ACTION_DOWN) {
                        isDownInLetter =
                            it.x > paddingLeft - SizeUtil.dp2px(10f) && it.x < width - paddingRight + SizeUtil.dp2px(10f)
                    }
                    if (isDownInLetter) {
                        mCurIndex = ((it.y - paddingTop) / mCellHeight).toInt()
                        if (mCurIndex >= 0 && mCurIndex < letters.size && mCurIndex != mPreIndex) {
                            mLetterChangedListener?.onLetterChanged(
                                letters[mCurIndex],
                                paddingLeft + mCellWidth / 2f,
                                paddingTop + mCellHeight * mCurIndex + mCellHeight / 2f
                            )
                            mPreIndex = mCurIndex
                        }
                        invalidate()
                    } else {  //不拦截其他区域的点击事件
                        return false
                    }
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    mCurIndex = -1
                    mPreIndex = -1
                    mLetterChangedListener?.onLetterGone()
                    invalidate()
                }
            }
        }
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