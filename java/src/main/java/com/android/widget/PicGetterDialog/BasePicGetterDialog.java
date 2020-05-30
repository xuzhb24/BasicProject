package com.android.widget.PicGetterDialog;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.FloatRange;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.android.java.R;
import com.android.util.bitmap.BitmapUtil;
import com.android.widget.ViewHolder;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;

/**
 * Create by xuzhb on 2020/1/22
 * Desc:拍照或从相册选取选择框，提供一些基础的方法
 */
public abstract class BasePicGetterDialog extends DialogFragment {

    private static final int OPEN_CAMERA_REQUEST_CODE = 0x0001;   //拍照
    private static final int OPEN_GALLERY_REQUEST_CODE = 0x0002;  //从相册选取
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 0x0100;      //申请相机权限
    private static final int READ_WRITE_PERMISSION_REQUEST_CODE = 0x0200;  //申请读写权限
    private static final String EXTRA_PIC_URL = "EXTRA_PIC_URL";

    @LayoutRes
    protected int mLayoutId = -1;           //对话框的布局id
    private int mWidth;                     //对话框的宽度
    private int mHeight;                    //对话框的高度
    private int mMargin;                    //对话框左右边距
    private float mDimAmount = 0.5f;        //背景透明度
    private int mAnimationStyle = -1;       //对话框出现消失的动画
    private boolean mCancelable = true;     //是否可点击取消
    private UCrop.Options mCropOptions;      //裁剪配置
    private int mMaxCropWidth = 1080;       //裁剪图片支持的最大宽度
    private int mMaxCropHeight = 2400;      //裁剪图片支持的最大高度
    private int mGravity = Gravity.BOTTOM;  //对话框显示的位置，默认底部
    private OnPicGetterListener mOnPicGetterListener;  //图片获取回调

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
        outState.putString(EXTRA_PIC_URL, mCurrentPhotoPath);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            mCurrentPhotoPath = savedInstanceState.getString(EXTRA_PIC_URL);
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        if (mOnPicGetterListener != null) {
            mOnPicGetterListener.onCancel();
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

    //裁剪配置
    public BasePicGetterDialog setCropOptions(UCrop.Options options) {
        mCropOptions = options;
        return this;
    }

    //设置裁剪的图片支持的最大宽度和高度
    public BasePicGetterDialog setMaxCropSize(int width, int height) {
        mMaxCropWidth = width;
        mMaxCropHeight = height;
        return this;
    }

    //在底部显示
    public void show(FragmentManager manager) {
        super.show(manager, BasePicGetterDialog.class.getName());
    }

    //在中间显示
    public void showAtCenter(FragmentManager manager) {
        mGravity = Gravity.CENTER;
        super.show(manager, BasePicGetterDialog.class.getName());
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
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            String picName = "temp_" + System.currentTimeMillis();
            File picDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            mCurrentPhotoPath = "";
            try {
                File photoFile = File.createTempFile(picName, ".jpg", picDir);
                mCurrentPhotoPath = photoFile.getAbsolutePath();
                Uri photoUri = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N   //Android 7.0后通过FileProvider共享文件，如系统照片
                        ? FileProvider.getUriForFile(getActivity(), getActivity().getApplicationContext().getApplicationInfo().packageName + ".provider", photoFile)
                        : Uri.fromFile(photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intent, OPEN_CAMERA_REQUEST_CODE);  //开启拍照
            } catch (IOException e) {
                e.printStackTrace();
                getPicFailure(e.getMessage());
            }
        }
    }

    //从相册选取
    protected void openGallery() {
        if (getActivity() == null) {
            return;
        }
        if (!hasReadWritePermission()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, OPEN_GALLERY_REQUEST_CODE);  //开启相册
    }

    //是否开启相机权限
    private boolean hasCameraPermission() {
        if (getActivity() != null) {
            if (!isPermissionGranted(getActivity(), Manifest.permission.CAMERA)
                    || !isPermissionGranted(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    || !isPermissionGranted(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
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
            if (!isPermissionGranted(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    || !isPermissionGranted(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
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

    private boolean isPermissionGranted(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
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
            if (hasAllPermissions(grantResults)) {
                openCamera();
            } else {
                getPicFailure("相机权限获取失败");
            }
        }
        if (requestCode == READ_WRITE_PERMISSION_REQUEST_CODE) {  //申请读写权限
            if (hasAllPermissions(grantResults)) {
                openGallery();
            } else {
                getPicFailure("读写权限获取失败");
            }
        }
    }

    private boolean hasAllPermissions(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OPEN_CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            onCameraResult();
        }
        if (requestCode == OPEN_GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            onGalleryResult(data);
        }
        if (requestCode == UCrop.REQUEST_CROP) {
            onCropResult(resultCode, data);
        }
    }

    //拍照回调
    private void onCameraResult() {
        if (TextUtils.isEmpty(mCurrentPhotoPath)) {
            getPicFailure("无法获取图片地址");
            return;
        }
        Uri uri = Uri.fromFile(new File(mCurrentPhotoPath));
        openUCrop(uri);
    }

    //相册采集回调
    private void onGalleryResult(Intent data) {
        if (data == null) {
            getPicFailure("获取相册图片失败");
            return;
        }
        Uri uri = data.getData();
        if (uri == null) {
            getPicFailure("无法获取图片地址");
            return;
        }
        openUCrop(uri);
    }

    //启动剪裁页
    protected void openUCrop(Uri uri) {
        if (getActivity() == null) {
            return;
        }
        File cacheDir = getActivity().getCacheDir();
        UCrop.Options options = mCropOptions != null ? mCropOptions : new UCrop.Options();
        options.setShowCropFrame(false);
        UCrop.of(uri, Uri.fromFile(new File(cacheDir, System.currentTimeMillis() + "_cache.jpg")))
                .withOptions(options)
                .withMaxResultSize(mMaxCropWidth, mMaxCropHeight)
                .start(getActivity(), this);
    }

    //图片裁剪回调
    private void onCropResult(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (data == null) {
                getPicFailure("获取图片失败");
                return;
            }
            Uri uri = UCrop.getOutput(data);
            if (uri == null) {
                getPicFailure("图片剪裁出错");
                return;
            }
            if (mOnPicGetterListener != null) {
                Bitmap bitmap = BitmapUtil.bytes2Bitmap(BitmapUtil.compressByQuality(BitmapFactory.decodeFile(uri.getPath()), 500, true));
                mOnPicGetterListener.onSuccess(bitmap, uri.getPath());
                dismiss();
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            getPicFailure("图片剪裁出错");
        } else {
            getPicFailure("图片剪裁已取消");
        }
    }

    //获取图片失败
    private void getPicFailure(String errorMsg) {
        if (mOnPicGetterListener != null) {
            mOnPicGetterListener.onFailure(errorMsg);
        }
    }

}
