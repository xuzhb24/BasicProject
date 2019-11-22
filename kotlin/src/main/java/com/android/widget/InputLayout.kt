package com.android.widget

import android.content.Context
import android.graphics.Color
import android.support.annotation.AttrRes
import android.text.Editable
import android.text.InputType
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import com.android.basicproject.R
import com.android.util.SizeUtil

/**
 * Created by xuzhb on 2019/11/21
 * Desc:带删除按钮的输入框
 */
class InputLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, @AttrRes defStyleAttrs: Int = 0
) : RelativeLayout(context, attrs, defStyleAttrs) {

    companion object {
        val DEFAULT_TEXT_TYPE = InputType.TYPE_CLASS_TEXT
        val DEFAULT_TEXT_SIZE = SizeUtil.sp2px(15f)
        val DEFAULT_TEXT_COLOR = Color.BLACK
        val DEFAULT_TEXT_COLOR_HINT = Color.parseColor("#E6E6E6")
        val DEFAULT_CLEAR_MARGIN = 0f
        val DEFAULT_SHOW_BOTTOM_LINE = true
    }

    var inputText: String = ""  //EditText输入文本
        set(value) {
            field = value
            mInputEt.setText(value)
        }
        get() = mInputEt.text.toString()
    var inputTextType: Int = DEFAULT_TEXT_TYPE  //EditText输入类型
        set(value) {
            field = value
            mInputEt.inputType = value
        }
    var inputTextHint: String = ""  //EditText未输入时的hint文本
        set(value) {
            field = value
            mInputEt.hint = value
        }
    var inputTextSize = DEFAULT_TEXT_SIZE  //EditText字体大小
        set(value) {
            field = value
            mInputEt.setTextSize(TypedValue.COMPLEX_UNIT_PX, value)
        }
    var inputTextColor = DEFAULT_TEXT_COLOR  //EditText字体颜色
        set(value) {
            field = value
            mInputEt.setTextColor(value)
        }
    var inputTextColorHint = DEFAULT_TEXT_COLOR_HINT  //EditText的hint文本字体颜色
        set(value) {
            field = value
            mInputEt.setHintTextColor(value)
        }
    var clearMarginRight = DEFAULT_CLEAR_MARGIN  //右侧删除图标的右边距
        set(value) {
            field = value
            if (value != 0f) {
                val params = mClearIv.layoutParams as MarginLayoutParams
                params.rightMargin += value.toInt()
                mClearIv.requestLayout()
            }
        }
    var showBottomLine = DEFAULT_SHOW_BOTTOM_LINE  //是否显示下划线
        set(value) {
            field = value
            with(mDivierLine) {
                if (value) {
                    visibility = View.VISIBLE
                } else {
                    visibility = View.GONE
                }
            }
        }
    var isEditable: Boolean = true      //是否可编辑
        set(value) {
            field = value
            mInputEt.isEnabled = value
            if (value) {
                if (!TextUtils.isEmpty(mInputEt.text.toString())) {
                    mClearIv.visibility = View.VISIBLE
                }
            } else {
                mClearIv.visibility = View.GONE
            }
        }

    //获取输入框对应的EditText
    fun getEditText(): EditText = mInputEt

    protected var mInputEt: EditText
    protected var mClearIv: ImageView
    protected var mDivierLine: View

    init {
        val layout = LayoutInflater.from(context).inflate(R.layout.layout_input, this)
        mInputEt = layout.findViewById(R.id.input_et)
        mClearIv = layout.findViewById(R.id.clear_iv)
        mDivierLine = layout.findViewById(R.id.divider_line)
        mClearIv.visibility = View.GONE

        attrs?.let {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.InputLayout)
            inputText = ta.getString(R.styleable.InputLayout_inputText) ?: ""
            inputTextType = ta.getInt(R.styleable.InputLayout_inputTextType, DEFAULT_TEXT_TYPE)
            inputTextHint = ta.getString(R.styleable.InputLayout_inputTextHint) ?: ""
            inputTextSize = ta.getDimension(R.styleable.InputLayout_inputTextSize, DEFAULT_TEXT_SIZE)
            inputTextColor = ta.getColor(R.styleable.InputLayout_inputTextColor, DEFAULT_TEXT_COLOR)
            inputTextColorHint = ta.getColor(R.styleable.InputLayout_inputTextColorHint, DEFAULT_TEXT_COLOR_HINT)
            clearMarginRight = ta.getDimension(R.styleable.InputLayout_clearMarginRight, DEFAULT_CLEAR_MARGIN)
            showBottomLine = ta.getBoolean(R.styleable.InputLayout_showBottomLine, DEFAULT_SHOW_BOTTOM_LINE)
            ta.recycle()
        }

        with(mInputEt) {
            if (!TextUtils.isEmpty(inputText)) {
                setText(inputText)
                mClearIv.visibility = View.VISIBLE
            }
            addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    onTextChangedListener?.invoke(s, start, before, count)
                    with(mClearIv) {
                        if (TextUtils.isEmpty(s)) {
                            visibility = View.GONE
                        } else {
                            visibility = View.VISIBLE
                        }
                    }
                }
            })
            inputType = inputTextType
            hint = inputTextHint
            setTextSize(TypedValue.COMPLEX_UNIT_PX, inputTextSize)
            setTextColor(inputTextColor)
            setHintTextColor(inputTextColorHint)
//            setOnFocusChangeListener { v, hasFocus ->
//                if (hasFocus) {
//                    mDivierLine.setBackgroundColor(Color.parseColor("#db4b3c"))
//                } else {
//                    mDivierLine.setBackgroundColor(Color.parseColor("#E8E8E8"))
//                }
//            }
        }
        if (clearMarginRight != 0f) {
            val params = mClearIv.layoutParams as MarginLayoutParams
            params.rightMargin += clearMarginRight.toInt()
            mClearIv.requestLayout()
        }
        with(mDivierLine) {
            if (showBottomLine) {
                visibility = View.VISIBLE
            } else {
                visibility = View.GONE
            }
        }
        mClearIv.setOnClickListener {
            mInputEt.setText("")
            mInputEt.requestFocus()
            onTextClearListener?.invoke()
        }
    }

    private var onTextChangedListener: ((s: CharSequence?, start: Int, before: Int, count: Int) -> Unit)? = null

    //输入文字变化时回调
    fun setOnTextChangedListener(listener: (s: CharSequence?, start: Int, before: Int, count: Int) -> Unit) {
        this.onTextChangedListener = listener;
    }

    private var onTextClearListener: (() -> Unit)? = null

    //清空文本时回调
    fun setOnTextClearListener(onTextClearListener: (() -> Unit)) {
        this.onTextClearListener = onTextClearListener
    }

}