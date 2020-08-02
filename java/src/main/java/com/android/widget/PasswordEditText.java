package com.android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.InputType;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

import com.android.java.R;
import com.android.util.SizeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuzhb on 2020/1/28
 * Desc:密码输入框
 */
public class PasswordEditText extends AppCompatEditText {

    private static final int DEFAULT_PASSWORD_COUNT = 6;

    private float frameSize;            //边框的大小，即长宽
    private float frameStrokeWidth;     //边框的宽度
    private float frameCorner;          //边框的圆角
    private int frameColor;             //边框的颜色
    private int frameBackgroundColor;  //边框的背景色
    private float frameMargin;         //边框的左右间距
    private int dotColor;              //圆圈颜色
    private float dotRadius;           //圈圈半径
    private int passwordCount;         //密码个数

    public float getFrameSize() {
        return frameSize;
    }

    public void setFrameSize(float frameSize) {
        this.frameSize = frameSize;
    }

    public float getFrameStrokeWidth() {
        return frameStrokeWidth;
    }

    public void setFrameStrokeWidth(float frameStrokeWidth) {
        this.frameStrokeWidth = frameStrokeWidth;
    }

    public float getFrameCorner() {
        return frameCorner;
    }

    public void setFrameCorner(float frameCorner) {
        this.frameCorner = frameCorner;
    }

    public int getFrameColor() {
        return frameColor;
    }

    public void setFrameColor(int frameColor) {
        this.frameColor = frameColor;
    }

    public int getFrameBackgroundColor() {
        return frameBackgroundColor;
    }

    public void setFrameBackgroundColor(int frameBackgroundColor) {
        this.frameBackgroundColor = frameBackgroundColor;
    }

    public float getFrameMargin() {
        return frameMargin;
    }

    public void setFrameMargin(float frameMargin) {
        this.frameMargin = frameMargin;
    }

    public int getDotColor() {
        return dotColor;
    }

    public void setDotColor(int dotColor) {
        this.dotColor = dotColor;
    }

    public float getDotRadius() {
        return dotRadius;
    }

    public void setDotRadius(float dotRadius) {
        this.dotRadius = dotRadius;
    }

    public int getPasswordCount() {
        return passwordCount;
    }

    public void setPasswordCount(int passwordCount) {
        this.passwordCount = (passwordCount <= 0) ? DEFAULT_PASSWORD_COUNT : passwordCount;
    }

    //清空所有输入的内容
    public void clearText() {
        setText("");
        setInputType(InputType.TYPE_CLASS_NUMBER);
    }

    private Paint mFramePaint;       //绘制边框
    private Paint mBgPaint;          //绘制边框内部的背景
    private Paint mDotPaint;         //绘制圆圈
    private List<RectF> mRectFList;  //各个边框对应的RectF
    private String mText;            //输入的内容

    public PasswordEditText(Context context) {
        super(context);
        init();

    }

    public PasswordEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        parseAttributeSet(context.obtainStyledAttributes(attrs, R.styleable.PasswordEditText));
        init();
    }

    private void parseAttributeSet(TypedArray ta) {
        frameSize = ta.getDimension(R.styleable.PasswordEditText_frameSize, SizeUtil.dp2px(50));
        frameStrokeWidth = ta.getDimension(R.styleable.PasswordEditText_frameStrokeWidth, SizeUtil.dp2px(1));
        frameCorner = ta.getDimension(R.styleable.PasswordEditText_frameCorner, SizeUtil.dp2px(3));
        frameColor = ta.getColor(R.styleable.PasswordEditText_frameColor, Color.parseColor("#D6D8DA"));
        frameBackgroundColor = ta.getColor(R.styleable.PasswordEditText_frameBackgroundColor, Color.TRANSPARENT);
        frameMargin = ta.getDimension(R.styleable.PasswordEditText_frameMargin, SizeUtil.dp2px(3.5f));
        dotColor = ta.getColor(R.styleable.PasswordEditText_dotColor, Color.BLACK);
        dotRadius = ta.getDimension(R.styleable.PasswordEditText_dotRadius, SizeUtil.dp2px(5));
        passwordCount = ta.getInt(R.styleable.PasswordEditText_passwordCount, DEFAULT_PASSWORD_COUNT);
        if (passwordCount <= 0) {
            passwordCount = DEFAULT_PASSWORD_COUNT;
        }
    }

    private void init() {
        mFramePaint = new Paint();
        mFramePaint.setAntiAlias(true);
        mFramePaint.setStrokeWidth(frameStrokeWidth);
        mFramePaint.setStyle(Paint.Style.STROKE);
        mFramePaint.setColor(frameColor);
        mBgPaint = new Paint();
        mBgPaint.setAntiAlias(true);
        mBgPaint.setStyle(Paint.Style.FILL);
        mBgPaint.setColor(frameBackgroundColor);
        mDotPaint = new Paint();
        mDotPaint.setAntiAlias(true);
        mDotPaint.setStyle(Paint.Style.FILL);
        mDotPaint.setColor(dotColor);
        mRectFList = new ArrayList<>();
        mText = "";
        for (int i = 0; i < passwordCount; i++) {
            RectF rectF = new RectF(i * (frameSize + 2 * frameMargin) + frameMargin + frameStrokeWidth / 2f,
                    frameStrokeWidth / 2f,
                    i * (frameSize + 2 * frameMargin) + frameMargin + frameStrokeWidth / 2f + frameSize,
                    frameStrokeWidth / 2f + frameSize);
            mRectFList.add(rectF);
        }

        setTextColor(Color.TRANSPARENT);  //设置用户输入的内容透明
        setInputType(InputType.TYPE_CLASS_NUMBER);
        setBackgroundDrawable(null);
        setLongClickable(false);
        setTextIsSelectable(false);
        setCursorVisible(false);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        if (text.toString().length() <= passwordCount) {
            mText = text.toString();
        } else {
            setText(mText);
            setSelection(getText().toString().length());  //光标制动到最后
            //调用setText(mText)之后键盘会还原，再次把键盘设置为数字键盘
            setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        if (mOnTextChangeListener != null) {
            mOnTextChangeListener.onTextChange(mText);
        }
        if (mOnTextCompleteListener != null) {
            if (text.toString().length() == passwordCount) {
                mOnTextCompleteListener.onTextComplete(mText);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        if (widthMode == MeasureSpec.AT_MOST) {  //支持wrap_content属性
            widthSize = (int) (passwordCount * (frameSize + 2 * frameMargin) + frameStrokeWidth) + 1;
        }
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (heightMode == MeasureSpec.AT_MOST) {  //支持wrap_content属性
            heightSize = Math.max((int) SizeUtil.dp2px(50), (int) (frameSize + frameStrokeWidth) + 1);  //不小于50dp
        }
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (frameMargin > 0) {
            for (RectF rectF : mRectFList) {
                if (frameBackgroundColor != Color.TRANSPARENT) {
                    canvas.drawRoundRect(rectF, frameCorner, frameCorner, mBgPaint);  //绘制边框背景颜色
                }
                canvas.drawRoundRect(rectF, frameCorner, frameCorner, mFramePaint);  //绘制边框
            }
        } else {  //如果水平间距小于0，那只显示第一个边框的左上和左下圆角和最后一个边框的右上和右下圆角就好
            if (mRectFList.size() == 1) {
                for (RectF rectF : mRectFList) {
                    if (frameBackgroundColor != Color.TRANSPARENT) {
                        canvas.drawRoundRect(rectF, frameCorner, frameCorner, mBgPaint);  //绘制边框背景颜色
                    }
                    canvas.drawRoundRect(rectF, frameCorner, frameCorner, mFramePaint);  //绘制边框
                }
            } else if (mRectFList.size() > 1) {
                RectF rectF = new RectF(mRectFList.get(0).left, mRectFList.get(0).top,
                        mRectFList.get(mRectFList.size() - 1).right, mRectFList.get(0).bottom);
                if (frameBackgroundColor != Color.TRANSPARENT) {
                    canvas.drawRoundRect(rectF, frameCorner, frameCorner, mBgPaint);  //绘制边框背景颜色
                }
                canvas.drawRoundRect(rectF, frameCorner, frameCorner, mFramePaint);  //绘制边框
                for (int i = 0; i < mRectFList.size() - 1; i++) {
                    canvas.drawLine(mRectFList.get(i).right, mRectFList.get(i).top,
                            mRectFList.get(i).right, mRectFList.get(i).bottom, mFramePaint);
                }
            }
        }
        for (int i = 0; i < mText.length(); i++) {
            canvas.drawCircle(mRectFList.get(i).centerX(), mRectFList.get(i).centerY(), dotRadius, mDotPaint);  //绘制圆圈
        }
    }

    //输入监听
    interface OnTextChangeListener {
        void onTextChange(String text);
    }

    private OnTextChangeListener mOnTextChangeListener;

    public void setOnTextChangeListener(OnTextChangeListener onTextChangeListener) {
        this.mOnTextChangeListener = onTextChangeListener;
    }

    //输入完成监听
    public interface OnTextCompleteListener {
        void onTextComplete(String text);
    }

    private OnTextCompleteListener mOnTextCompleteListener;

    public void setOnTextCompleteListener(OnTextCompleteListener onTextCompleteListener) {
        this.mOnTextCompleteListener = onTextCompleteListener;
    }
}
