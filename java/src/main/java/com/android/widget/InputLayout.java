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
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.android.java.R;
import com.android.util.SizeUtil;

/**
 * Created by xuzhb on 2019/11/22
 * Desc:带删除按钮的输入框
 */
public class InputLayout extends RelativeLayout {

    private String inputText;         //EditText输入文本
    private int inputTextType;       //EditText输入类型
    private String inputTextHint;    //EditText未输入时的hint文本
    private float inputTextSize;     //EditText字体大小
    private int inputTextColor;      //EditText字体颜色
    private int inputTextColorHint;  //EditText的hint文本字体颜色
    private float clearMarginRight;  //右侧删除图标的右边距
    private boolean showBottomLine;  //是否显示下划线

    public void setInputText(String inputText) {
        this.inputText = inputText;
        mInputEt.setText(inputText);
    }

    public void setInputTextType(int inputTextType) {
        this.inputTextType = inputTextType;
        mInputEt.setInputType(inputTextType);
    }

    public void setInputTextHint(String inputTextHint) {
        this.inputTextHint = inputTextHint;
        mInputEt.setHint(inputTextHint);
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

    public void setClearMarginRight(float clearMarginRight) {
        this.clearMarginRight = clearMarginRight;
        if (clearMarginRight != 0) {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mClearIv.getLayoutParams();
            params.rightMargin += clearMarginRight;
            mClearIv.requestLayout();
        }
    }

    public void setShowBottomLine(boolean showBottomLine) {
        this.showBottomLine = showBottomLine;
        if (showBottomLine) {
            mDivierLine.setVisibility(View.VISIBLE);
        } else {
            mDivierLine.setVisibility(View.GONE);
        }
    }

    //设置是否可编辑
    public void setEditable(boolean editable) {
        mInputEt.setEnabled(editable);
        if (editable) {
            if (!TextUtils.isEmpty(mInputEt.getText().toString())) {
                mClearIv.setVisibility(View.VISIBLE);
            }
        } else {
            mClearIv.setVisibility(View.GONE);
        }
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
        mClearIv.setVisibility(View.GONE);

        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.InputLayout);
            inputText = ta.getString(R.styleable.InputLayout_inputText);
            inputTextType = ta.getInt(R.styleable.InputLayout_inputTextType, InputType.TYPE_CLASS_TEXT);
            inputTextHint = ta.getString(R.styleable.InputLayout_inputTextHint);
            inputTextSize = ta.getDimension(R.styleable.InputLayout_inputTextSize, SizeUtil.sp2px(15));
            inputTextColor = ta.getColor(R.styleable.InputLayout_inputTextColor, Color.BLACK);
            inputTextColorHint = ta.getColor(R.styleable.InputLayout_inputTextColorHint, Color.parseColor("#E6E6E6"));
            clearMarginRight = ta.getDimension(R.styleable.InputLayout_clearMarginRight, 0);
            showBottomLine = ta.getBoolean(R.styleable.InputLayout_showBottomLine, true);
            ta.recycle();
        }

        if (!TextUtils.isEmpty(inputText)) {
            mInputEt.setText(inputText);
            mClearIv.setVisibility(View.VISIBLE);
        }
        mInputEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (onTextChangedListener != null) {
                    onTextChangedListener.onTextChanged(s, start, before, count);
                }
                if (TextUtils.isEmpty(s)) {
                    mClearIv.setVisibility(View.GONE);
                } else {
                    mClearIv.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mInputEt.setInputType(inputTextType);
        mInputEt.setHint(inputTextHint);
        mInputEt.setTextSize(TypedValue.COMPLEX_UNIT_PX, inputTextSize);
        mInputEt.setTextColor(inputTextColor);
        mInputEt.setHintTextColor(inputTextColorHint);
        if (clearMarginRight != 0) {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mClearIv.getLayoutParams();
            params.rightMargin += clearMarginRight;
            mClearIv.requestLayout();
        }
        if (showBottomLine) {
            mDivierLine.setVisibility(View.VISIBLE);
        } else {
            mDivierLine.setVisibility(View.GONE);
        }
        mClearIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mInputEt.setText("");
                mInputEt.requestFocus();
                if (onTextClearListener != null) {
                    onTextClearListener.onTextClear();
                }
            }
        });
    }

    private OnTextChangedListener onTextChangedListener;

    public interface OnTextChangedListener {
        void onTextChanged(CharSequence s, int start, int before, int count);
    }

    //输入文字变化时回调
    public void setOnTextChangedListener(OnTextChangedListener listener) {
        this.onTextChangedListener = listener;
    }

    private OnTextClearListener onTextClearListener;

    public interface OnTextClearListener {
        void onTextClear();
    }

    //清空文本时回调
    public void setOnTextClearListener(OnTextClearListener listener) {
        this.onTextClearListener = listener;
    }

}
