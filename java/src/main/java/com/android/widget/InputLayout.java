package com.android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.java.R;
import com.android.util.LayoutParamsUtil;
import com.android.util.SizeUtil;

/**
 * Created by xuzhb on 2019/11/22
 * Desc:带删除按钮的输入框
 */
public class InputLayout extends LinearLayout {

    private static final int DEFAULT_TEXT_TYPE = InputType.TYPE_CLASS_TEXT;
    private static final float DEFAULT_TEXT_SIZE = SizeUtil.sp2px(15f);
    private static final int DEFAULT_TEXT_COLOR = Color.BLACK;
    private static final int DEFAULT_TEXT_COLOR_HINT = Color.parseColor("#a3a3a3");
    private static final boolean DEFAULT_MULTI_LINE = true;
    private static final boolean DEFAULT_EDITABLE = true;
    private static final boolean DEFAULT_SHOW_DIVIDER_LINE = true;
    private static final int DEFAULT_DIVIDER_COLOR = Color.parseColor("#dbdbdb");
    private static final float DEFAULT_DIVIDER_HEIGHT = SizeUtil.dp2px(1);

    private String inputText = "";                                //EditText输入文本
    private String inputTextHint = "";                            //EditText未输入时的hint文本
    private int inputTextType = DEFAULT_TEXT_TYPE;                //EditText输入类型
    private float inputTextSize = DEFAULT_TEXT_SIZE;              //EditText字体大小
    private int inputTextColor = DEFAULT_TEXT_COLOR;              //EditText字体颜色
    private int inputTextColorHint = DEFAULT_TEXT_COLOR_HINT;     //EditText的hint文本字体颜色
    private boolean multiLine = DEFAULT_MULTI_LINE;               //是否支持输入多行文本，默认支持
    private boolean editable = DEFAULT_EDITABLE;                  //是否可编辑，默认可以
    private boolean showDividerLine = DEFAULT_SHOW_DIVIDER_LINE;  //是否显示下划线，默认显示
    private int dividerColor = DEFAULT_DIVIDER_COLOR;             //下划线颜色
    private float dividerHeight = DEFAULT_DIVIDER_HEIGHT;         //下划线高度

    public void setInputText(String inputText) {
        this.inputText = inputText;
        mInputEt.setText(inputText);
    }

    public String getInputText() {
        return mInputEt.getText().toString();
    }

    public void setInputTextHint(String inputTextHint) {
        this.inputTextHint = inputTextHint;
        mInputEt.setHint(inputTextHint);
    }

    public void setInputTextType(int inputTextType) {
        this.inputTextType = inputTextType;
        setInputType(multiLine, inputTextType);
    }

    public void setInputTextSize(float inputTextSize) {
        this.inputTextSize = inputTextSize;
        mInputEt.setTextSize(TypedValue.COMPLEX_UNIT_PX, inputTextSize);
    }

    public void setInputTextColor(int inputTextColor) {
        this.inputTextColor = inputTextColor;
        mInputEt.setTextColor(inputTextColor);
    }

    public void setInputTextColorHint(int inputTextColorHint) {
        this.inputTextColorHint = inputTextColorHint;
        mInputEt.setHintTextColor(inputTextColorHint);
    }

    public void setMultiLine(boolean multiLine) {
        this.multiLine = multiLine;
        setInputType(multiLine, inputTextType);
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
        mInputEt.setEnabled(editable);
        mClearIv.setVisibility(editable && !TextUtils.isEmpty(inputText) ? View.VISIBLE : View.GONE);
    }

    public void setShowDividerLine(boolean showDividerLine) {
        this.showDividerLine = showDividerLine;
        mDivierLine.setVisibility(showDividerLine ? View.VISIBLE : View.GONE);
    }

    public void setDividerColor(int dividerColor) {
        this.dividerColor = dividerColor;
        mDivierLine.setBackgroundColor(dividerColor);
    }

    public void setDividerHeight(float dividerHeight) {
        this.dividerHeight = dividerHeight;
        LayoutParamsUtil.setHeight(mDivierLine, (int) dividerHeight);
    }

    //获取输入框对应的EditText
    public EditText getEditText() {
        return mInputEt;
    }

    private EditText mInputEt;
    private ImageView mClearIv;
    private View mDivierLine;

    public InputLayout(Context context) {
        this(context, null);
    }

    public InputLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InputLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View layout = LayoutInflater.from(context).inflate(R.layout.layout_input, this);
        mInputEt = layout.findViewById(R.id.input_et);
        mClearIv = layout.findViewById(R.id.clear_iv);
        mDivierLine = layout.findViewById(R.id.divider_line);
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.InputLayout);
            inputText = ta.getString(R.styleable.InputLayout_inputText);
            inputTextHint = ta.getString(R.styleable.InputLayout_inputTextHint);
            inputTextType = ta.getInt(R.styleable.InputLayout_inputTextType, DEFAULT_TEXT_TYPE);
            inputTextSize = ta.getDimension(R.styleable.InputLayout_inputTextSize, DEFAULT_TEXT_SIZE);
            inputTextColor = ta.getColor(R.styleable.InputLayout_inputTextColor, DEFAULT_TEXT_COLOR);
            inputTextColorHint = ta.getColor(R.styleable.InputLayout_inputTextColorHint, DEFAULT_TEXT_COLOR_HINT);
            multiLine = ta.getBoolean(R.styleable.InputLayout_multiLine, DEFAULT_MULTI_LINE);
            editable = ta.getBoolean(R.styleable.InputLayout_editable, DEFAULT_EDITABLE);
            showDividerLine = ta.getBoolean(R.styleable.InputLayout_showDividerLine, DEFAULT_SHOW_DIVIDER_LINE);
            dividerColor = ta.getColor(R.styleable.InputLayout_dividerColor, DEFAULT_DIVIDER_COLOR);
            dividerHeight = ta.getDimension(R.styleable.InputLayout_dividerHeight, DEFAULT_DIVIDER_HEIGHT);
            ta.recycle();
        }

        mInputEt.setText(inputText);
        mInputEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mClearIv.setVisibility(editable && !TextUtils.isEmpty(s) ? View.VISIBLE : View.GONE);
                if (mOnTextChangedListener != null) {
                    mOnTextChangedListener.onTextChanged(s, start, before, count);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mInputEt.setHint(inputTextHint);
        setInputType(multiLine, inputTextType);
        mInputEt.setTextSize(TypedValue.COMPLEX_UNIT_PX, inputTextSize);
        mInputEt.setTextColor(inputTextColor);
        mInputEt.setHintTextColor(inputTextColorHint);
        mInputEt.setEnabled(editable);

        mClearIv.setVisibility(editable && !TextUtils.isEmpty(inputText) ? View.VISIBLE : View.GONE);
        mClearIv.setOnClickListener(v -> {
            mInputEt.setText("");
            mInputEt.requestFocus();
            if (mOnTextClearListener != null) {
                mOnTextClearListener.onTextClear();
            }
        });

        mDivierLine.setVisibility(showDividerLine ? View.VISIBLE : View.GONE);
        mDivierLine.setBackgroundColor(dividerColor);
        LayoutParamsUtil.setHeight(mDivierLine, (int) dividerHeight);
    }

    //支持输入多行文本
    private void setInputType(boolean multiLine, int inputType) {
        mInputEt.setInputType(multiLine && inputType == InputType.TYPE_CLASS_TEXT
                ? inputType | InputType.TYPE_TEXT_FLAG_MULTI_LINE : inputType);
    }

    private OnTextChangedListener mOnTextChangedListener;

    public interface OnTextChangedListener {
        void onTextChanged(CharSequence s, int start, int before, int count);
    }

    //输入文字变化时回调
    public void setOnTextChangedListener(OnTextChangedListener listener) {
        this.mOnTextChangedListener = listener;
    }

    private OnTextClearListener mOnTextClearListener;

    public interface OnTextClearListener {
        void onTextClear();
    }

    //清空文本时回调
    public void setOnTextClearListener(OnTextClearListener listener) {
        this.mOnTextClearListener = listener;
    }

}
