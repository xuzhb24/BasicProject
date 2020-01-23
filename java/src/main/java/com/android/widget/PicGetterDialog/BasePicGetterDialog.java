package com.android.widget.PicGetterDialog;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.*;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.*;
import com.android.java.R;
import com.android.widget.ViewHolder;
import com.yalantis.ucrop.UCrop;

/**
 * Create by xuzhb on 2020/1/22
 * Desc:拍照或从相册选取选择框，提供一些基础的方法
 */
public abstract class BasePicGetterDialog extends DialogFragment {

    private static final int OPEN_CAMERA_REQUEST_CODE = 0x0001;   //拍照
    private static final int OPEN_GALLERY_REQUEST_CODE = 0x0002;  //从相册选取
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 0x0100;      //申请相机权限
    private static final int READ_WRITE_PERMISSION_REQUEST_CODE = 0x0200;  //申请读写权限
    private static final String PIC_URL = "PIC_URL";

    @LayoutRes
    protected int mLayoutId = -1;           //对话框的布局id
    private int mWidth;                     //对话框的宽度
    private int mHeight;                    //对话框的高度
    private int mMargin;                    //对话框左右边距
    private float mDimAmount = 0.5f;        //背景透明度
    private int mAnimationStyle = -1;       //对话框出现消失的动画
    private boolean mCancelable = true;     //是否可点击取消
    private int mGravity = Gravity.CENTER;  //对话框显示的位置，默认正中间

    private OnPicGetterListener mOnPicGetterListener;  //图片获取回调
    private UCrop.Options mCropOptions;     //
    private String mCurrentPhotoPath;  //当前拍照的地址

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DialogStyle);
        mLayoutId = getLayoutId();  //设置dialog布局
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(mLayoutId, container, false);
        convertView(new ViewHolder(view), getDialog());  //获取dialog布局的控件
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(PIC_URL, mCurrentPhotoPath);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            mCurrentPhotoPath = savedInstanceState.getString(PIC_URL);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        initParams();
    }

    //初始化参数
    private void initParams() {
        Window window = getDialog().getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            params.dimAmount = mDimAmount;
            params.gravity = mGravity;
            //设置dialog宽度
            if (mWidth == 0) {
                params.width = getContext().getResources().getDisplayMetrics().widthPixels - 2 * mMargin;
            } else {
                params.width = mWidth;
            }
            //设置dialog高度
            if (mHeight == 0) {
                params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            } else {
                params.height = mHeight;
            }
            //设置dialog动画
            if (mAnimationStyle != -1) {
                window.setWindowAnimations(mAnimationStyle);
            }
            window.setAttributes(params);
        }
        setCancelable(mCancelable);
    }

    //设置宽高
    public BasePicGetterDialog setViewParams(int width, int height) {
        mWidth = width;
        mHeight = height;
        return this;
    }

    //设置左右的边距
    public BasePicGetterDialog setHorizontalMargin(int margin) {
        mMargin = margin;
        return this;
    }

    //设置背景昏暗度
    public BasePicGetterDialog setDimAmount(@FloatRange(from = 0, to = 1) float dimAmount) {
        mDimAmount = dimAmount;
        return this;
    }

    //设置动画
    public BasePicGetterDialog setAnimationStyle(@StyleRes int animationStyle) {
        mAnimationStyle = animationStyle;
        return this;
    }

    //设置Outside是否可点击
    public BasePicGetterDialog setOutsideCancelable(boolean cancelable) {
        mCancelable = cancelable;
        return this;
    }

    //在中间显示
    public BasePicGetterDialog show(FragmentManager manager) {
        super.show(manager, BasePicGetterDialog.class.getName());
        return this;
    }

    //在底部显示
    public BasePicGetterDialog showAtBottom(FragmentManager manager) {
        mGravity = Gravity.BOTTOM;
        super.show(manager, BasePicGetterDialog.class.getName());
        return this;
    }

    //监听图片获取事件
    public void setOnPicGetterListener(OnPicGetterListener listener) {
        mOnPicGetterListener = listener;
    }

    //拍照
    protected void openCamera() {
        if (getActivity() == null) {
            return;
        }
        if (!hasCameraPermission()) {
            return;
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    }

    //从相册选取
    protected void openGallery() {

    }

    //启动剪裁页
    protected void openUCrop(Uri uri) {

    }

    //是否开启相机权限
    private boolean hasCameraPermission() {
        if (getActivity() != null) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {  //Android 6.0以后权限动态申请
                    requestPermissions(new String[]{
                                    Manifest.permission.CAMERA,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_EXTERNAL_STORAGE},
                            CAMERA_PERMISSION_REQUEST_CODE);
                }
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    //是否开启读写权限
    private boolean hasReadWritePermission() {
        if (getActivity() != null) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {  //Android 6.0以后权限动态申请
                    requestPermissions(new String[]{
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_EXTERNAL_STORAGE},
                            READ_WRITE_PERMISSION_REQUEST_CODE);
                }
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    //获取dialog的布局Id
    public abstract int getLayoutId();

    //处理dialog布局上的控件
    public abstract void convertView(ViewHolder holder, Dialog dialog);

    //申请权限后回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {  //申请相机权限

        }
        if (requestCode == READ_WRITE_PERMISSION_REQUEST_CODE) {  //申请读写权限

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OPEN_CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

        }
        if (requestCode == OPEN_GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

        }
//        if(requestCode==)
    }
}
