package com.android.widget.PopupWindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import androidx.annotation.FloatRange;

import com.android.widget.ViewHolder;

/**
 * Create by xuzhb on 2020/1/21
 * Desc:通用的PopupWindow
 */
public class CommonPopupWindow extends PopupWindow {

    private WindowHelper mWindowHelper;

    private CommonPopupWindow(Context context) {
        super(context);
        if (context instanceof Activity) {
            mWindowHelper = new WindowHelper((Activity) context);
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (mWindowHelper != null) {
            mWindowHelper.setBackGroundAlpha(1.0f);
        }
    }

    public static class Builder {

        private Context mContext;

        public Builder(Context context) {
            this.mContext = context;
        }

        private int mLayoutId = -1;         //弹窗的布局id
        private int mWidth;                 //弹窗的宽度
        private int mHeight;                //弹窗的高度
        private float mAlpha = 1.0f;        //背景透明度
        private int mAnimationStyle = -1;   //动画
        private boolean mTouchable = true;  //是否可点击
        private Drawable mBackgroundDrawable = new ColorDrawable(0x00000000);  //背景drawable
        private OnViewListener mOnViewListener;

        //通过布局id设置弹窗布局的View
        public Builder setContentView(int layoutId) {
            mLayoutId = layoutId;
            return this;
        }

        //设置宽高
        public Builder setViewParams(int width, int height) {
            mWidth = width;
            mHeight = height;
            return this;
        }

        //设置外部区域背景透明度，0：完全不透明，1：完全透明
        public Builder setBackGroundAlpha(@FloatRange(from = 0.0, to = 1.0) float alpha) {
            mAlpha = alpha;
            return this;
        }

        //设置显示和消失动画
        public Builder setAnimationStyle(int animationStyle) {
            mAnimationStyle = animationStyle;
            return this;
        }

        //设置外部区域是否可点击取消对话框
        public Builder setOutsideTouchable(boolean touchable) {
            mTouchable = touchable;
            return this;
        }

        //设置弹窗背景
        public Builder setBackgroundDrawable(Drawable drawable) {
            mBackgroundDrawable = drawable;
            return this;
        }

        //设置事件监听
        public Builder setOnViewListener(OnViewListener listener) {
            mOnViewListener = listener;
            return this;
        }

        public CommonPopupWindow build() {
            CommonPopupWindow popupWindow = new CommonPopupWindow(mContext);
            if (mLayoutId != -1) {
                //设置contentView
                View view = LayoutInflater.from(mContext).inflate(mLayoutId, null);
                //因为PopupWindow在显示前无法获取准确的宽高值(getWidth和getHeight可能会返回0或-2），
                //通过提前测量contentView的宽高就可以通过getMeasuredWidth和getMeasuredHeight获取contentView的宽高
                view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                popupWindow.setContentView(view);
            } else {
                throw new NullPointerException("The contentView of PopupWindow is null");
            }
            //设置宽高，没有设置宽高的话默认为ViewGroup.LayoutParams.WRAP_CONTENT
            if (mWidth == 0) {
                popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
            } else {
                popupWindow.setWidth(mWidth);
            }
            if (mHeight == 0) {
                popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            } else {
                popupWindow.setHeight(mHeight);
            }
            //设置外部区域的透明度
            if (mContext instanceof Activity) {
                WindowHelper helper = new WindowHelper((Activity) mContext);
                helper.setBackGroundAlpha(mAlpha);
            }
            //设置弹窗显示和消失的动画效果
            if (mAnimationStyle != -1) {
                popupWindow.setAnimationStyle(mAnimationStyle);
            }
            //设置弹窗背景，如果contentView对应的View已经设置android:background可能会覆盖弹窗背景
            popupWindow.setBackgroundDrawable(mBackgroundDrawable);
            //设置点击外部区域是否可取消弹窗
            popupWindow.setOutsideTouchable(mTouchable);
            popupWindow.setFocusable(mTouchable);
            //设置contentView上控件的事件监听
            if (mOnViewListener != null) {
                mOnViewListener.onView(new ViewHolder(popupWindow.getContentView()), popupWindow);
            }
            return popupWindow;
        }

        public interface OnViewListener {
            void onView(ViewHolder holder, PopupWindow popupWindow);
        }

    }

}
